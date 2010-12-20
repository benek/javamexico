package org.javamexico.dao.hib3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.javamexico.dao.BlogDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.blog.BlogComent;
import org.javamexico.entity.blog.BlogPost;
import org.javamexico.entity.blog.TagBlog;
import org.javamexico.entity.blog.VotoBlog;
import org.javamexico.entity.blog.VotoComentBlog;
import org.javamexico.util.PrivilegioInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/** Implementacion del DAO para manejo de blogs.
 * 
 * @author Enrique Zamudio
 */
public class BlogDAO implements BlogDao {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	private SessionFactory sfact;
	private int minRepAddComent = 5;

	/** Establece la reputacion minima que debe tener un usuario para agregar un comentario a un blog. */
	public void setMinRepComentar(int value) {
		minRepAddComent = value;
	}
	@Required
	public void setSessionFactory(SessionFactory value) {
		sfact = value;
	}
	public SessionFactory getSessionFactory() {
		return sfact;
	}

	@Override
	public List<BlogPost> getUserBlog(Usuario user, boolean published) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(BlogPost.class).add(Restrictions.eq("autor", user));
		if (published) {
			crit = crit.add(Restrictions.eq("status", 1));
		}
		@SuppressWarnings("unchecked")
		List<BlogPost> blogs = crit.list();
		return blogs;
	}

	@Override
	public List<BlogPost> getBlogsRecientes(int cuantos) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BlogPost> blogs = sess.createCriteria(BlogPost.class).add(Restrictions.eq("status", 1)).addOrder(Order.desc("fecha")).setMaxResults(cuantos).list();
		return blogs;
	}

	@Override
	public List<BlogPost> getBlogsMasVotados(int cuantos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlogPost getBlog(int id) {
		Session sess = sfact.getCurrentSession();
		BlogPost b = (BlogPost)sess.get(BlogPost.class, id);
		b.getTags().size();
		b.getComentarios().size();
		return b;
	}

	@Override
	public List<BlogComent> getComentarios(BlogPost blog, int pageSize,
			int page, boolean crono) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(BlogPost post) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(BlogPost post) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(BlogPost post) {
		// TODO Auto-generated method stub

	}

	@Override
	public BlogComent addComment(String coment, BlogPost post, Usuario autor) {
		if (autor.getReputacion() < minRepAddComent) {
			throw new PrivilegioInsuficienteException("El usuario no tiene suficiente reputacion para agregar comentarios");
		}
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public BlogComent addComment(String coment, BlogComent parent, Usuario autor) {
		if (autor.getReputacion() < minRepAddComent) {
			throw new PrivilegioInsuficienteException("El usuario no tiene suficiente reputacion para agregar comentarios");
		}
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VotoBlog votaBlog(Usuario user, BlogPost post, boolean up) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VotoComentBlog votaComentario(Usuario user, BlogComent comm,
			boolean up) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VotoBlog findVotoBlog(Usuario user, BlogPost post) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VotoComentBlog findVotoComent(Usuario user, BlogComent comm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BlogPost> getBlogsConTag(TagBlog tag) {
		Session sess = sfact.getCurrentSession();
		ArrayList<BlogPost> blogs = new ArrayList<BlogPost>();
		if (tag != null) {
			sess.refresh(tag);
			blogs.addAll(tag.getBlogs());
		}
		return blogs;
	}

	@Override
	public List<BlogPost> getBlogsConTag(String tag) {
		TagBlog tb = findTag(tag);
		return getBlogsConTag(tb);
	}

	@Override
	public List<BlogPost> getBlogsConTags(Set<TagBlog> tags) {
		ArrayList<BlogPost> blogs = new ArrayList<BlogPost>();
		Session sess = sfact.getCurrentSession();
		for (TagBlog tag : tags) {
			sess.refresh(tag);
			for (BlogPost b : tag.getBlogs()) {
				if (!blogs.contains(b)) {
					blogs.add(b);
				}
			}
		}
		return blogs;
	}

	@Override
	public void addTag(String tag, BlogPost blog) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TagBlog> findMatchingTags(String parcial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TagBlog> getTagsPopulares(int cuantos) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(TagBlog.class).addOrder(Order.desc("count")).setMaxResults(cuantos);
		@SuppressWarnings("unchecked")
		List<TagBlog> tags = crit.list();
		return tags;
	}

	public TagBlog findTag(String tag) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(TagBlog.class).add(Restrictions.ilike("tag", tag, MatchMode.EXACT));
		@SuppressWarnings("unchecked")
		List<TagBlog> t = crit.setMaxResults(1).list();
		return t.size() == 0 ? null : t.get(0);
	}

	/** Devuelve los tags que coincidan con los tags que se indiquen (sin importar mayusc/minusc) */
	public List<TagBlog> findTags(String... tags) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(TagBlog.class);
		for (String s : tags) {
			crit.add(Restrictions.ilike("tag", s, MatchMode.EXACT));
		}
		@SuppressWarnings("unchecked")
		List<TagBlog> t = crit.list();
		return t;
	}

}
