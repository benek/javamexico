/*
This file is part of JavaMexico.

JavaMexico is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3
of the License, or (at your option) any later version.

JavaMexico is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with JavaMexico.
If not, see <http://www.gnu.org/licenses/>.
*/
package org.javamexico.dao.hib3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.foro.ComentForo;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.TagForo;
import org.javamexico.entity.foro.TemaForo;
import org.javamexico.entity.foro.VotoComentForo;
import org.javamexico.entity.foro.VotoForo;
import org.javamexico.util.PrivilegioInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/** Implementacion del DAO para el modulo de foros, usando Hibernate 3.
 * 
 * @author Enrique Zamudio
 */
public class ForumDAO implements ForoDao {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sfact;
	private int minRepVotaF;
	private int minRepVotaC;

	/** Establece la reputacion minima que debe tener un usuario para dar un voto negativo a un foro. */
	public void setMinRepVotoForo(int value) {
		minRepVotaF = value;
	}
	/** Establece la reputacion minima que debe tener un usuario para dar un voto negativo a un comentario de foro. */
	public void setMinRepVotoComent(int value) {
		minRepVotaC = value;
	}

	@Required
	public void setSessionFactory(SessionFactory value) {
		sfact = value;
	}
	public SessionFactory getSessionFactory() {
		return sfact;
	}

	public ComentForo addComment(String coment, ComentForo parent, Usuario autor) {
		Session sess = sfact.getCurrentSession();
		ComentForo cf = new ComentForo();
		cf.setForo(parent.getForo());
		cf.setInReplyTo(parent);
		cf.setAutor(autor);
		cf.setFecha(new Date());
		cf.setComentario(coment);
		sess.save(cf);
		sess.flush();
		sess.refresh(parent);
		parent.getRespuestas().size();
		return cf;
	}

	public ComentForo addComment(String coment, Foro foro, Usuario autor) {
		Session sess = sfact.getCurrentSession();
		ComentForo cf = new ComentForo();
		cf.setForo(foro);
		cf.setAutor(autor);
		cf.setFecha(new Date());
		cf.setComentario(coment);
		sess.save(cf);
		return cf;
	}

	@Transactional
	public void addTag(String tag, Foro foro) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagForo> tags = sess.createCriteria(TagForo.class).add(Restrictions.ilike("tag", tag)).setMaxResults(1).list();
		TagForo elTag = null;
		if (tags.size() == 0) {
			elTag = new TagForo();
			elTag.setTag(tag);
			sess.save(elTag);
			sess.flush();
		} else {
			elTag = tags.get(0);
		}
		if (foro.getTags() == null) {
			sess.refresh(foro);
		}
		foro.getTags().add(elTag);
		sess.update(foro);
		foro.getTags().size();
	}

	public void delete(Foro foro) {
		Session sess = sfact.getCurrentSession();
		sess.delete(foro);
	}

	public List<TagForo> findMatchingTags(String parcial) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagForo> tags = sess.createCriteria(TagForo.class).add(
				Restrictions.ilike("tag", parcial, MatchMode.ANYWHERE)).list();
		return tags;
	}

	public VotoForo findVoto(Usuario user, Foro foro) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoForo> v = sess.createCriteria(VotoForo.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("foro", foro)).setMaxResults(1).list();
		return v.size() > 0 ? v.get(0) : null;
	}

	public VotoComentForo findVoto(Usuario user, ComentForo coment) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoComentForo> v = sess.createCriteria(VotoComentForo.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("comentario", coment)).setMaxResults(1).list();
		return v.size() > 0 ? v.get(0) : null;
	}

	public List<ComentForo> getComentarios(Foro foro, int page, int pageSize,
			boolean crono) {
		Session sess = sfact.getCurrentSession();
		Order order = crono ? Order.desc("fecha") : Order.desc("votos");
		@SuppressWarnings("unchecked")
		List<ComentForo> coms = sess.createCriteria(ComentForo.class).add(
				Restrictions.eq("foro", foro)).add(Restrictions.isNull("inReplyTo")).setFirstResult(
						(page-1)*pageSize).setMaxResults(pageSize).addOrder(order).list();
		for (ComentForo cf : coms) {
			cf.getRespuestas().size();
		}
		return coms;
	}

	public Set<ComentForo> getRespuestas(ComentForo coment) {
		Session sess = sfact.getCurrentSession();
		sess.refresh(coment);
		coment.getRespuestas().size();
		return coment.getRespuestas();
	}

	public Foro getForo(int id) {
		Session sess = sfact.getCurrentSession();
		Foro f = (Foro)sess.get(Foro.class, id);
		if (f != null) {
			f.getTags().size();
		}
		return f;
	}

	public TemaForo getTema(int tid) {
		Session sess = sfact.getCurrentSession();
		TemaForo f = (TemaForo)sess.get(TemaForo.class, tid);
		return f;
	}

	public List<Foro> getForosByUser(Usuario user, boolean published) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(Foro.class).add(
				Restrictions.eq("autor", user)).addOrder(Order.desc("fecha"));
		if (published) {
			crit = crit.add(Restrictions.gt("status", 0));
		}
		@SuppressWarnings("unchecked")
		List<Foro> qus = crit.list();
		return qus;
	}

	public List<Foro> getForosMasActivos(int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Foro> getForosRecientes(int page, int pageSize) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Foro> qus = sess.createCriteria(Foro.class).add(
				Restrictions.gt("status", 0)).addOrder(Order.desc("fecha")
						).setMaxResults(pageSize).setFirstResult((page-1)*pageSize).list();
		return qus;
	}

	public void insert(Foro foro) {
		Session sess = sfact.getCurrentSession();
		if (foro.getFecha() == null) {
			foro.setFecha(new Date());
		}
		sess.save(foro);
	}

	public void update(Foro foro) {
		Session sess = sfact.getCurrentSession();
		if (foro.getFecha() == null) {
			foro.setFecha(new Date());
		}
		sess.update(foro);
	}

	public VotoForo vota(Usuario user, Foro foro, boolean up)
			throws PrivilegioInsuficienteException {
		if (!up && user.getReputacion() < minRepVotaF) {
			throw new PrivilegioInsuficienteException();
		}
		Session sess = sfact.getCurrentSession();
		//Buscamos el voto a ver si ya se hizo
		VotoForo voto = findVoto(user, foro);
		if (voto == null) {
			//Si no existe lo creamos
			voto = new VotoForo();
			voto.setFecha(new Date());
			voto.setForo(foro);
			voto.setUp(up);
			voto.setUser(user);
			sess.save(voto);
		} else if (voto.isUp() != up) {
			//Si ya existe pero quieren cambio, se actualiza
			voto.setFecha(new Date());
			voto.setUp(up);
			sess.update(voto);
		}
		return voto;
	}

	public VotoComentForo vota(Usuario user, ComentForo coment, boolean up)
			throws PrivilegioInsuficienteException {
		if (!up && user.getReputacion() < minRepVotaC) {
			throw new PrivilegioInsuficienteException();
		}
		Session sess = sfact.getCurrentSession();
		VotoComentForo voto = findVoto(user, coment);
		if (voto == null) {
			voto = new VotoComentForo();
			voto.setFecha(new Date());
			voto.setComentario(coment);
			voto.setUp(up);
			voto.setUser(user);
			sess.save(voto);
		} else if (voto.isUp() != up) {
			voto.setUp(up);
			voto.setFecha(new Date());
			sess.update(voto);
		}
		return voto;
	}

	public List<TagForo> getTagsPopulares(int limit) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagForo> tags = sess.createCriteria(TagForo.class).addOrder(
				Order.desc("count")).setMaxResults(limit).list();
		return tags;
	}

	public List<Foro> getForosConTag(TagForo tag) {
		Session sess = sfact.getCurrentSession();
		sess.refresh(tag);
		return new ArrayList<Foro>(tag.getForos());
	}

	@SuppressWarnings("unchecked")
	public List<Foro> getForosConTag(String tag) {
		Session sess = sfact.getCurrentSession();
		List<TagForo> tags = sess.createCriteria(TagForo.class).add(Restrictions.ilike("tag", tag, MatchMode.EXACT)).setMaxResults(1).list();
		List<Foro> list = null;
		if (tags.size() > 0) {
			list = new ArrayList<Foro>(tags.get(0).getForos());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Foro> getForosConTags(List<TagForo> tags) {
		Session sess = sfact.getCurrentSession();
		Set<Integer> foros = new TreeSet<Integer>();
		//Recorremos cada tag
		for (TagForo tag : tags) {
			sess.refresh(tag);
			//Obtenemos los foros del tag
			Set<Foro> sub = tag.getForos();
			//Por alguna rarisima razon mas alla de mi comprension,
			//si agrego el foro directamente a la lista, en algun momento truena esto
			//con un ClassCastException que no me da stack trace. Algun pedo muy satanico.
			//Asi que agregamos la llave de cada foro y luego hacemos un query.
			//Ineficiente, pero al menos funciona.
			for (Foro f : sub) {
				foros.add(f.getFid());
			}
		}
		return sess.createCriteria(Foro.class).add(Restrictions.in("fid", foros)).list();
	}

	public void insert(TemaForo tema) {
		if (tema.getFechaAlta() == null) {
			tema.setFechaAlta(new Date());
		}
		Session sess = sfact.getCurrentSession();
		sess.save(tema);
	}

	public void update(TemaForo tema) {
		if (tema.getFechaAlta() == null) {
			tema.setFechaAlta(new Date());
		}
		Session sess = sfact.getCurrentSession();
		sess.update(tema);
	}

	public void delete(TemaForo tema) {
		if (tema.getFechaAlta() == null) {
			tema.setFechaAlta(new Date());
		}
		Session sess = sfact.getCurrentSession();
		//TODO ver si hay foros y negar borrado si hay.
		sess.delete(tema);
	}

	public List<Foro> getForosConTema(TemaForo tema, int page, int pageSize) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Foro> list = sess.createCriteria(Foro.class).add(Restrictions.eq("tema", tema)).setMaxResults(pageSize).setFirstResult((page-1)*pageSize).addOrder(Order.desc("votos")).list();
		return list;
	}

	public List<TemaForo> getTemas() {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TemaForo> temas = sess.createCriteria(TemaForo.class).list();
		return temas;
	}

}
