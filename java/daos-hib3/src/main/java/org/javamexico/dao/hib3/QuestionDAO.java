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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javamexico.dao.PreguntaDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.pregunta.ComentPregunta;
import org.javamexico.entity.pregunta.ComentRespuesta;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.Respuesta;
import org.javamexico.entity.pregunta.TagPregunta;
import org.javamexico.entity.pregunta.VotoPregunta;
import org.javamexico.entity.pregunta.VotoRespuesta;
import org.javamexico.util.PrivilegioInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/** Implementacion del DAO de preguntas, usando Hibernate 3 y el soporte de Spring.
 * 
 * @author Enrique Zamudio
 */
public class QuestionDAO implements PreguntaDao {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sfact;
	private int minRepVotaPu = 10;
	private int minRepVotaRu = 20;
	private int minRepVotaPd = 5;
	private int minRepVotaRd = 10;

	/** Indica la reputacion minima requerida para dar votos negativos a una pregunta. */
	public void setMinRepVotoPreguntaDown(int value) {
		minRepVotaPd = value;
	}
	/** Indica la reputacion minima requerida para dar votos negativos a una respuesta. */
	public void setMinRepVotoRespuestaDown(int value) {
		minRepVotaRd = value;
	}
	/** Indica la reputacion minima requerida para dar votos positivos a una pregunta. */
	public void setMinRepVotoPreguntaUp(int value) {
		minRepVotaPu = value;
	}
	/** Indica la reputacion minima requerida para dar votos positivos a una respuesta. */
	public void setMinRepVotoRespuestaUp(int value) {
		minRepVotaRu = value;
	}

	@Required
	public void setSessionFactory(SessionFactory value) {
		sfact = value;
	}
	public SessionFactory getSessionFactory() {
		return sfact;
	}

	public Pregunta getPregunta(int id) {
		Session sess = sfact.getCurrentSession();
		Pregunta p = (Pregunta)sess.get(Pregunta.class, id);
		if (p != null) {
			p.getTags().size();
			p.getComentarios().size();
			p.getRespuestas().size();
		}
		return p;
	}

	public List<Pregunta> getPreguntasConTag(TagPregunta tag) {
		Session sess = sfact.getCurrentSession();
		sess.refresh(tag);
		return new ArrayList<Pregunta>(tag.getPreguntas());
	}

	public List<Pregunta> getPreguntasConTag(String tag) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagPregunta> tags = sess.createCriteria(TagPregunta.class).add(Restrictions.ilike("tag", tag, MatchMode.EXACT)).setMaxResults(1).list();
		List<Pregunta> qus = null;
		if (tags.size() > 0) {
			qus = new ArrayList<Pregunta>(tags.get(0).getPreguntas());
		} else {
			qus = Collections.emptyList();
		}
		return qus;
	}

	public List<Pregunta> getPreguntasConTags(Set<TagPregunta> tags) {
		Session sess = sfact.getCurrentSession();
		Set<Pregunta> qus = new TreeSet<Pregunta>();
		for (TagPregunta tag : tags) {
			sess.refresh(tag);
			qus.addAll(tag.getPreguntas());
		}
		return new ArrayList<Pregunta>(qus);
	}

	public List<Pregunta> getPreguntasMasVotadas(int limit) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Pregunta> qus = sess.createCriteria(Pregunta.class).add(
				Restrictions.gt("status", 0)).addOrder(Order.desc("votos")).setMaxResults(limit).list();
		return qus;
	}

	public List<Pregunta> getPreguntasRecientes(int page, int pageSize) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Pregunta> qus = sess.createCriteria(Pregunta.class).add(
				Restrictions.gt("status", 0)).addOrder(Order.desc("fechaPregunta")
						).setMaxResults(pageSize).setFirstResult((page-1)*pageSize).list();
		return qus;
	}

	public List<Pregunta> getPreguntasSinResolver(int limit) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Pregunta> qus = sess.createCriteria(Pregunta.class).add(
				Restrictions.eq("status", 1)).addOrder(Order.desc("fechaPregunta")).list();
		return qus;
	}

	public List<Pregunta> getPreguntasSinResponder(int limit) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Pregunta> qus = sess.createCriteria(Pregunta.class).add(
				Restrictions.sizeEq("respuestas", 0)).addOrder(Order.desc("fechaPregunta")).setMaxResults(limit).list();
		return qus;
	}

	public List<Pregunta> getPreguntasUsuario(Usuario user) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Pregunta> qus = sess.createCriteria(Pregunta.class).add(
				Restrictions.eq("autor", user)).addOrder(Order.desc("fechaPregunta")).list();
		return qus;
	}

	public List<Respuesta> getRespuestas(Pregunta q, int pageSize, int page,
			boolean crono) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Respuesta> resps = sess.createCriteria(Respuesta.class).add(
				Restrictions.eq("pregunta", q)).setMaxResults(pageSize).setFirstResult((page-1)*pageSize).addOrder(
						crono ? Order.desc("fecha") : Order.desc("votos")).list();
		return resps;
	}

	public List<TagPregunta> getTagsPopulares(int limit) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagPregunta> tags = sess.createCriteria(TagPregunta.class).addOrder(
				Order.desc("count")).setMaxResults(limit).list();
		return tags;
	}

	public VotoPregunta vota(Usuario user, Pregunta pregunta, boolean up) throws PrivilegioInsuficienteException {
		if ((up && user.getReputacion() < minRepVotaPu) || (!up && user.getReputacion() < minRepVotaPd)) {
			throw new PrivilegioInsuficienteException("El usuario no tiene privilegio suficiente para votar por una pregunta");
		}
		Session sess = sfact.getCurrentSession();
		VotoPregunta v = findVoto(user, pregunta);
		int updateRep = 0;
		if (v == null) {
			//Insertamos un nuevo voto
			v = new VotoPregunta();
			v.setFecha(new Date());
			v.setPregunta(pregunta);
			v.setUp(up);
			v.setUser(user);
			sess.save(v);
			updateRep = up ? 1 : -1;
		} else if (v.isUp() != up) {
			//Modificamos el voto existente
			v.setUp(up);
			v.setFecha(new Date());
			sess.update(v);
			//Al cambiar voto, debe afectarse por 2
			updateRep = up ? 2 : -2;
		}
		if (updateRep != 0) {
			//Actualizamos la reputacion del autor de la pregunta
			sess.refresh(pregunta);
			//Esto no es intuitivo pero si no lo hago asi, hib3 arroja excepcion marciana.
			user = (Usuario)sess.merge(pregunta.getAutor());
			sess.lock(user, LockMode.UPGRADE);
			sess.refresh(user);
			user.setReputacion(user.getReputacion() + updateRep);
			sess.update(user);
			sess.flush();
		}
		return v;
	}

	public VotoRespuesta vota(Usuario user, Respuesta resp, boolean up) throws PrivilegioInsuficienteException {
		if ((up && user.getReputacion() < minRepVotaRu) || (!up && user.getReputacion() < minRepVotaRd)) {
			throw new PrivilegioInsuficienteException("El usuario no tiene privilegio suficiente para votar por una respuesta");
		}
		Session sess = sfact.getCurrentSession();
		VotoRespuesta v = findVoto(user, resp);
		int updateRep = 0;
		if (v == null) {
			v = new VotoRespuesta();
			v.setFecha(new Date());
			v.setRespuesta(resp);
			v.setUp(up);
			v.setUser(user);
			sess.save(v);
			updateRep = up ? 1 : -1;
		} else if (v.isUp() != up) {
			v.setUp(up);
			v.setFecha(new Date());
			sess.update(v);
			updateRep = up ? 2 : -2;
		}
		if (updateRep != 0) {
			sess.refresh(resp);
			user = (Usuario)sess.merge(resp.getAutor());
			sess.lock(user, LockMode.UPGRADE);
			sess.refresh(user);
			user.setReputacion(user.getReputacion() + updateRep);
			sess.update(user);
			sess.flush();
		}
		return v;
	}

	public VotoPregunta findVoto(Usuario user, Pregunta pregunta) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoPregunta> v = sess.createCriteria(VotoPregunta.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("pregunta", pregunta)).setMaxResults(1).list();
		return v.size() > 0 ? v.get(0) : null;
	}

	public VotoRespuesta findVoto(Usuario user, Respuesta respuesta) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoRespuesta> v = sess.createCriteria(VotoRespuesta.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("respuesta", respuesta)).setMaxResults(1).list();
		return v.size() > 0 ? v.get(0) : null;
	}

	public void insert(Pregunta p) {
		Session sess = sfact.getCurrentSession();
		if (p.getFechaPregunta() == null) {
			p.setFechaPregunta(new Date());
		}
		sess.save(p);
	}

	public void update(Pregunta p) {
		Session sess = sfact.getCurrentSession();
		if (p.getFechaPregunta() == null) {
			p.setFechaPregunta(new Date());
		}
		sess.update(p);
	}

	@Transactional
	public void delete(Pregunta p) {
		Session sess = sfact.getCurrentSession();
		sess.delete(p);
	}

	public Respuesta addRespuesta(String resp, Pregunta p, Usuario autor) {
		Session sess = sfact.getCurrentSession();
		Respuesta r = new Respuesta();
		r.setFecha(new Date());
		r.setAutor(autor);
		r.setPregunta(p);
		r.setRespuesta(resp);
		sess.save(r);
		sess.flush();
		sess.refresh(p);
		p.getRespuestas().size();
		return r;
	}

	public ComentPregunta addComentario(String c, Pregunta p, Usuario autor) {
		Session sess = sfact.getCurrentSession();
		ComentPregunta cp = new ComentPregunta();
		cp.setAutor(autor);
		cp.setComentario(c);
		cp.setFecha(new Date());
		cp.setPregunta(p);
		sess.save(cp);
		sess.flush();
		sess.refresh(p);
		p.getComentarios().size();
		return cp;
	}

	public ComentRespuesta addComentario(String c, Respuesta r, Usuario autor) {
		Session sess = sfact.getCurrentSession();
		ComentRespuesta cr = new ComentRespuesta();
		cr.setRespuesta(r);
		cr.setAutor(autor);
		cr.setComentario(c);
		cr.setFecha(new Date());
		sess.save(cr);
		sess.flush();
		sess.refresh(r);
		r.getComentarios().size();
		return cr;
	}

	@Transactional
	public void addTag(String tag, Pregunta p) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagPregunta> tags = sess.createCriteria(TagPregunta.class).add(
				Restrictions.ilike("tag", tag)).setMaxResults(1).list();
		TagPregunta utag = null;
		if (tags.size() == 0) {
			utag = new TagPregunta();
			utag.setTag(tag);
			sess.save(utag);
			sess.flush();
		} else {
			utag = tags.get(0);
		}
		if (p.getTags() == null) {
			sess.refresh(p);
		}
		p.getTags().add(utag);
		sess.update(p);
		p.getTags().size();
	}

	public List<TagPregunta> findMatchingTags(String parcial) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagPregunta> tags = sess.createCriteria(TagPregunta.class).add(
				Restrictions.ilike("tag", parcial, MatchMode.ANYWHERE)).list();
		return tags;
	}

}
