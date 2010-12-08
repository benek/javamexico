package org.javamexico.dao.hib3;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javamexico.dao.BolsaTrabajoDao;
import org.javamexico.entity.TagUsuario;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.bolsa.Empresa;
import org.javamexico.entity.bolsa.Oferta;
import org.javamexico.entity.bolsa.Tag;
import org.javamexico.entity.bolsa.VotoOferta;
import org.javamexico.util.Base64;
import org.javamexico.util.PrivilegioInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;

/** Implementacion del DAO de bolsa de trabajo, usando Hibernate 3 con soporte de Spring.
 * 
 * @author Enrique Zamudio
 */
public class BolsaDAO implements BolsaTrabajoDao {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sfact;
	private int minRepVotaO;

	/** Establece la reputacion minima que debe tener un usuario para dar un voto negativo a un foro. */
	public void setMinRepVotoOferta(int value) {
		minRepVotaO = value;
	}

	@Required
	public void setSessionFactory(SessionFactory value) {
		sfact = value;
	}
	public SessionFactory getSessionFactory() {
		return sfact;
	}

	@Override
	public List<Oferta> getOfertasRecientes(Date desde) {
		Session sess = sfact.getCurrentSession();
		//TODO maxresults debe ser parm
		@SuppressWarnings("unchecked")
		List<Oferta> ofs = sess.createCriteria(Oferta.class).add(Restrictions.eq("status", 0)).add(
				Restrictions.gt("fechaAlta", desde)).add(Restrictions.lt("fechaExpira", new Date())
				).setMaxResults(20).list();
		return ofs;
	}

	@Override
	public List<Oferta> getOfertasConTag(Tag tag) {
		Session sess = sfact.getCurrentSession();
		sess.refresh(tag);
		return new ArrayList<Oferta>(tag.getOfertas());
	}

    @Override
    public List<Oferta> getOfertasConTag(String tag) {
        Session sess = sfact.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Tag> tags = sess.createCriteria(Tag.class).add(Restrictions.ilike("tag", tag, MatchMode.EXACT)).setMaxResults(1).list();
        List<Oferta> list = null;
        if (tags.size() > 0) {
            list = new ArrayList<Oferta>(tags.get(0).getOfertas());
        }
        return list;
    }

    @Override
	@SuppressWarnings("unchecked")
	public List<Oferta> getOfertasConTags(List<Tag> tags) {
		Session sess = sfact.getCurrentSession();
		Set<Integer> ofs = new TreeSet<Integer>();
		for (Tag t : tags) {
			sess.refresh(t);
			Set<Oferta> _o = t.getOfertas();
			//Por alguna rarisima razon mas alla de mi comprension,
			//si agrego la oferta directamente a la lista, en algun momento truena esto
			//con un ClassCastException que no me da stack trace. Algun pedo muy satanico.
			//Asi que agregamos la llave de cada foro y luego hacemos un query.
			//Ineficiente, pero al menos funciona.
			for (Oferta o : _o) {
				ofs.add(o.getOfid());
			}
		}
		return sess.createCriteria(Oferta.class).add(Restrictions.in("ofid", ofs)).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Oferta> getOfertasConTagsUsuario(List<TagUsuario> tags) {
		Set<Integer> ofs = new TreeSet<Integer>();
		Session sess = sfact.getCurrentSession();
		for (TagUsuario tu : tags) {
			Tag t = (Tag)sess.createCriteria(Tag.class).add(Restrictions.eq("tag", tu.getTag())).uniqueResult();
			if (t != null) {
				for (Oferta o : t.getOfertas()) {
					ofs.add(o.getOfid());
				}
			}
		}
		return sess.createCriteria(Oferta.class).add(Restrictions.in("ofid", ofs)).list();
	}

	@Override
	public VotoOferta votaOferta(Usuario user, Oferta oferta, boolean up) {
		if (!up && user.getReputacion() < minRepVotaO) {
			throw new PrivilegioInsuficienteException();
		}
		Session sess = sfact.getCurrentSession();
		VotoOferta voto = findVoto(user, oferta);
		if (voto == null) {
			voto = new VotoOferta();
			voto.setUsuario(user);
			voto.setOferta(oferta);
			voto.setFecha(new Date());
			voto.setUp(up);
			sess.save(voto);
		} else {
			voto.setFecha(new Date());
			voto.setUp(up);
			sess.update(voto);
		}
		return voto;
	}

	public VotoOferta findVoto(Usuario user, Oferta oferta) {
		Session sess = sfact.getCurrentSession();
		return (VotoOferta)sess.createCriteria(VotoOferta.class).add(Restrictions.eq("usuario", user)).add(
				Restrictions.eq("oferta", oferta)).uniqueResult();
	}

    @SuppressWarnings("unchecked")
    public List<Empresa> getEmpresas() {
        Session sess = sfact.getCurrentSession();
        return sess.createCriteria(Empresa.class).list();
    }

    public void update(Empresa e) {
        Session sess = sfact.getCurrentSession();
        Empresa e2 = (Empresa)sess.get(Empresa.class, e.getEid());
        if (e2.getPassword() == null || !e2.getPassword().equals(e.getPassword())) {
            try {
                //TODO esto se puede optimizar pero sin usar mucha memoria, tal vez un pool de MD's
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(String.format("%d:%s/%s", e.getEid(), e.getNombre(), e.getPassword()).getBytes());
                byte[] sha = md.digest();
                //Codificar a base 64
                e.setPassword(Base64.base64Encode(sha, 0, sha.length));
            } catch (GeneralSecurityException ex) {
                //No se pudo
                log.error("Cifrando password de empresa {}", e.getNombre(), ex);
                throw new DataIntegrityViolationException("No se puede modificar el password de la empresa", ex);
            }
        }
        sess.evict(e2);
        sess.update(e);
    }

    @Override
    public void addTag(String tag, Oferta oferta) {
        Session sess = sfact.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Tag> tags = sess.createCriteria(Tag.class).add(Restrictions.ilike("tag", tag)).setMaxResults(1).list();
        Tag elTag = null;
        if (tags.size() == 0) {
            elTag = new Tag();
            elTag.setTag(tag);
            sess.save(elTag);
            sess.flush();
        } else {
            elTag = tags.get(0);
        }
        if (oferta.getTags() == null) {
            sess.refresh(oferta);
        }
        oferta.getTags().add(elTag);
        sess.update(oferta);
        oferta.getTags().size();
    }

    @Override
    public List<Tag> findMatchingTags(String parcial) {
        Session sess = sfact.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Tag> tags = sess.createCriteria(Tag.class).add(
                Restrictions.ilike("tag", parcial, MatchMode.ANYWHERE)).list();
        return tags;
    }

    @Override
    public List<Tag> getTagsPopulares(int max) {
        Session sess = sfact.getCurrentSession();
        @SuppressWarnings("unchecked")
        List<Tag> tags = sess.createCriteria(Tag.class).addOrder(Order.desc("count")).setMaxResults(max).list();
        return tags;
    }

}
