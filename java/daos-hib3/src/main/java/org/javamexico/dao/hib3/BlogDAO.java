package org.javamexico.dao.hib3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
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
	private int minRepVotaBd = 20;
	private int minRepVotaCd = 10;
	private int minRepVotaBu = 5;
	private int minRepVotaCu = 3;
	private int minRepAddBlog = 20;

	/** Establece la reputacion minima que debe tener un usuario para agregar un post al blog de un usuario. */
	public void setMinRepAddBlog(int value) {
		minRepAddBlog = value;
	}
	/** Establece la reputacion minima que debe tener un usuario para dar un voto negativo a un blog. */
	public void setMinRepVotoBlogDown(int value) {
		minRepVotaBd = value;
	}
	/** Establece la reputacion minima que debe tener un usuario para dar un voto negativo a un comentario de blog. */
	public void setMinRepVotoComentDown(int value) {
		minRepVotaCd = value;
	}
	/** Establece la reputacion minima que debe tener un usuario para dar un voto positivo a un blog. */
	public void setMinRepVotoBlogUp(int value) {
		minRepVotaBu = value;
	}
	/** Establece la reputacion minima que debe tener un usuario para dar un voto positivo a un comentario de blog. */
	public void setMinRepVotoComentUp(int value) {
		minRepVotaCu = value;
	}

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
		Session sess = sfact.getCurrentSession();
		Order order = crono ? Order.desc("fecha") : Order.desc("votos");
		@SuppressWarnings("unchecked")
		List<BlogComent> coms = sess.createCriteria(BlogComent.class).add(
				Restrictions.eq("blog", blog)).add(Restrictions.isNull("inReplyTo")).setFirstResult(
						(page-1)*pageSize).setMaxResults(pageSize).addOrder(order).list();
		for (BlogComent cf : coms) {
			cf.getRespuestas().size();
		}
		return coms;
	}

	@Override
	public void insert(BlogPost post) {
		Session sess = getSessionFactory().getCurrentSession();
		Usuario aut = post.getAutor();
		sess.refresh(aut);
		if (aut.getReputacion() < minRepAddBlog) {
			throw new PrivilegioInsuficienteException("El usuario no tiene suficiente reputacion para crear entradas en su blog");
		}
		post.setFecha(new Date());
		sess.save(post);
		post.getComentarios();
	}

	@Override
	public void update(BlogPost post) {
		Session sess = getSessionFactory().getCurrentSession();
		sess.update(post);
		sess.refresh(post);
		post.getComentarios();
	}

	@Override
	public void delete(BlogPost post) {
		Session sess = getSessionFactory().getCurrentSession();
		sess.delete(post);
	}

	@Override
	public BlogComent addComment(String coment, BlogPost post, Usuario autor) {
		if (autor.getReputacion() < minRepAddComent) {
			throw new PrivilegioInsuficienteException("El usuario no tiene suficiente reputacion para agregar comentarios");
		}
		Session sess = sfact.getCurrentSession();
		BlogComent cmt = new BlogComent();
		cmt.setAutor(autor);
		cmt.setBlog(post);
		cmt.setComentario(coment);
		cmt.setFecha(new Date());
		sess.save(cmt);
		return cmt;
	}
	@Override
	public BlogComent addComment(String coment, BlogComent parent, Usuario autor) {
		if (autor.getReputacion() < minRepAddComent) {
			throw new PrivilegioInsuficienteException("El usuario no tiene suficiente reputacion para agregar comentarios");
		}
		Session sess = sfact.getCurrentSession();
		BlogComent cmt = new BlogComent();
		cmt.setAutor(autor);
		cmt.setBlog(parent.getBlog());
		cmt.setComentario(coment);
		cmt.setFecha(new Date());
		cmt.setInReplyTo(parent);
		sess.save(cmt);
		sess.flush();
		sess.refresh(parent);
		parent.getRespuestas().size();
		return cmt;
	}

	@Override
	public VotoBlog votaBlog(Usuario user, BlogPost post, boolean up) {
		if ((up && user.getReputacion() < minRepVotaBu) || (!up && user.getReputacion() < minRepVotaBd)) {
			throw new PrivilegioInsuficienteException("El usuario no tiene privilegio suficiente para votar por un blog");
		}
		Session sess = sfact.getCurrentSession();
		//Buscamos el voto a ver si ya se hizo
		VotoBlog voto = findVotoBlog(user, post);
		int uprep = 0;
		if (voto == null) {
			//Si no existe lo creamos
			voto = new VotoBlog();
			voto.setFecha(new Date());
			voto.setBlog(post);
			voto.setUp(up);
			voto.setUser(user);
			sess.save(voto);
			uprep = up ? 1 : -1;
		} else if (voto.isUp() != up) {
			//Si ya existe pero quieren cambio, se actualiza
			voto.setFecha(new Date());
			voto.setUp(up);
			sess.update(voto);
			uprep = up ? 2 : -2;
		}
		if (uprep != 0) {
			sess.refresh(post);
			//Esto no es nada intuitivo pero si no lo hago asi, se arroja una excepcion marciana de Hib
			user = (Usuario)sess.merge(post.getAutor());
			sess.lock(user, LockMode.UPGRADE);
			sess.refresh(user);
			user.setReputacion(user.getReputacion() + uprep);
			sess.update(user);
			sess.flush();
		}
		return voto;
	}

	@Override
	public VotoComentBlog votaComentario(Usuario user, BlogComent comm,
			boolean up) {
		if ((up && user.getReputacion() < minRepVotaCu) || (!up && user.getReputacion() < minRepVotaCd)) {
			throw new PrivilegioInsuficienteException("El usuario no tiene privilegio suficiente para votar por un comentario");
		}
		Session sess = sfact.getCurrentSession();
		VotoComentBlog voto = findVotoComent(user, comm);
		int uprep = 0;
		if (voto == null) {
			voto = new VotoComentBlog();
			voto.setFecha(new Date());
			voto.setComent(comm);
			voto.setUp(up);
			voto.setUser(user);
			sess.save(voto);
			uprep = up ? 1 : -1;
		} else if (voto.isUp() != up) {
			voto.setUp(up);
			voto.setFecha(new Date());
			sess.update(voto);
			uprep = up ? 2 : -2;
		}
		if (uprep != 0) {
			sess.refresh(comm);
			user = (Usuario)sess.merge(comm.getAutor());
			sess.lock(user, LockMode.UPGRADE);
			sess.refresh(user);
			user.setReputacion(user.getReputacion() + uprep);
			sess.update(user);
			sess.flush();
		}
		return voto;
	}

	@Override
	public VotoBlog findVotoBlog(Usuario user, BlogPost post) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoBlog> v = sess.createCriteria(VotoBlog.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("blog", post)).setMaxResults(1).list();
		return v.size() == 0 ? null : v.get(0);
	}

	@Override
	public VotoComentBlog findVotoComent(Usuario user, BlogComent comm) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<VotoComentBlog> v = sess.createCriteria(VotoComentBlog.class).add(Restrictions.eq("user", user)).add(
				Restrictions.eq("coment", comm)).setMaxResults(1).list();
		return v.size() == 0 ? null : v.get(0);
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
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagBlog> tags = sess.createCriteria(TagBlog.class).add(
				Restrictions.ilike("tag", tag)).setMaxResults(1).list();
		TagBlog _tag = null;
		if (tags.size() == 0) {
			_tag = new TagBlog();
			_tag.setTag(tag);
			sess.save(tag);
			sess.flush();
		} else {
			_tag = tags.get(0);
		}
		if (blog.getTags() == null) {
			sess.refresh(blog);
		}
		blog.getTags().add(_tag);
		sess.update(blog);
		blog.getTags().size();
	}

	@Override
	public List<TagBlog> findMatchingTags(String parcial) {
		Session sess = sfact.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<TagBlog> tags = sess.createCriteria(TagBlog.class).add(
				Restrictions.ilike("tag", parcial, MatchMode.ANYWHERE)).list();
		return tags;
	}

	@Override
	public List<TagBlog> getTagsPopulares(int cuantos) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(TagBlog.class).addOrder(
				Order.desc("count")).setMaxResults(cuantos);
		@SuppressWarnings("unchecked")
		List<TagBlog> tags = crit.list();
		return tags;
	}

	public TagBlog findTag(String tag) {
		Session sess = sfact.getCurrentSession();
		Criteria crit = sess.createCriteria(TagBlog.class).add(
				Restrictions.ilike("tag", tag, MatchMode.EXACT));
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
