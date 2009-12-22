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

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.TagUsuario;
import org.javamexico.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/** Implementacion del DAO para usuarios, usando Hibernate 3 y soporte de Spring (indirecto).
 * 
 * @author Enrique Zamudio
 */
public class UsuarioDAO implements UserDao {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sfact;

	public void setSessionFactory(SessionFactory value) {
		sfact = value;
	}
	public SessionFactory getSessionFactory() {
		return sfact;
	}

	@SuppressWarnings("unchecked")
	public List<Usuario> getAllUsers() {
		Session sess = sfact.getCurrentSession();
		return sess.createCriteria(Usuario.class).list();
	}

	public Usuario getUser(int id) {
		Session sess = sfact.getCurrentSession();
		return (Usuario)sess.get(Usuario.class, id);
	}

	public Usuario validaLogin(String username, String password) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Usuario> us = sess.createCriteria(Usuario.class).add(
				Restrictions.eq("username", username)).setFetchSize(1).list();
		Usuario u = us.size() > 0 ? us.get(0) : null;
		//TODO validar el password
		u.getTags().size();
		return u;
	}

	public void insert(Usuario u) {
		Session sess = sfact.getCurrentSession();
		if (u.getFechaAlta() == null) {
			u.setFechaAlta(new Date());
		}
		sess.save(u);
	}

	public void update(Usuario u) {
		Session sess = sfact.getCurrentSession();
		sess.update(u);
	}

	public void delete(Usuario u) {
		Session sess = sfact.getCurrentSession();
		sess.delete(u);
	}

	@Transactional
	public void addTag(String tag, Usuario u) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagUsuario> tags = sess.createCriteria(TagUsuario.class).add(Restrictions.ilike("tag", tag)).setFetchSize(1).list();
		TagUsuario utag = null;
		if (tags.size() == 0) {
			utag = new TagUsuario();
			utag.setTag(tag);
			sess.save(utag);
			sess.flush();
		} else {
			utag = tags.get(0);
		}
		if (u.getTags() == null) {
			sess.refresh(u);
		}
		utag.setCount(utag.getCount() + 1);
		sess.update(utag);
		u.getTags().add(utag);
		sess.update(u);
		u.getTags().size();
	}

	public List<TagUsuario> findMatchingTags(String parcial) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagUsuario> tags = sess.createCriteria(TagUsuario.class).add(Restrictions.ilike("tag", String.format("*%s*", parcial))).list();
		return tags;
	}

}
