package org.javamexico.site.pages.blogs;

import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.BlogDao;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.entity.blog.BlogPost;
import org.javamexico.entity.blog.TagBlog;
import org.javamexico.site.base.Pagina;

@Import(stylesheet="context:layout/preguntas.css")
public class Index extends Pagina {

	@Inject @Service("blogDao")
	private BlogDao bdao;
	@Inject @Service("usuarioDao")
	private UserDao udao;
	@Property private Usuario usuario;
	@Property private TagBlog tag;
	@Property private BlogPost blog;

	void onActivate(int uid) {
		usuario = udao.getUser(uid);
		if (usuario == null) {
			//algo?
		} else if (getUserExists() && getUser().getUid() == uid) {
			setUser(usuario);
		}
	}

	public List<BlogPost> getBlogs() {
		if (usuario == null) {
			return bdao.getBlogsRecientes(20);
		} else {
			return bdao.getUserBlog(usuario, true);
		}
	}

	public List<TagBlog> getPopTags() {
		return bdao.getTagsPopulares(10);
	}

}
