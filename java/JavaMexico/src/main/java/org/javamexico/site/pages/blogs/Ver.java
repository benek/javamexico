package org.javamexico.site.pages.blogs;

import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.BlogDao;
import org.javamexico.entity.blog.BlogComent;
import org.javamexico.entity.blog.BlogPost;
import org.javamexico.entity.blog.TagBlog;
import org.javamexico.site.base.Pagina;

@Import(stylesheet="context:layout/preguntas.css")
public class Ver extends Pagina {

	@Property private BlogPost blog;
	@Property private BlogPost otrob;
    @SuppressWarnings("unused")
	@Property private TagBlog tag;
	@Property private BlogComent coment;
    @SuppressWarnings("unused")
	@Property private BlogComent comresp;
    @SuppressWarnings("unused")
	@Property private String commtext;
	@Inject @Service("blogDao")
	private BlogDao bdao;
	/** Esta zona contiene una liga para mostrar respuestas a un comentario. */
	@InjectComponent private Zone rzone;
	/** Esta zona despliega las respuestas a un comentario. */
	@InjectComponent private Zone subcomentZone;

	Object onActivate(int bid) {
		blog = bdao.getBlog(bid);
		if (blog == null) {
			return Index.class;
		}
		return null;
	}
	Object onPassivate() {
		return blog == null ? null : blog.getBid();
	}

	public List<BlogComent> getComents() {
		return bdao.getComentarios(blog, 20, 1, true);
	}

	public List<BlogPost> getBlogsSimilares() {
		return bdao.getBlogsConTags(blog.getTags());
	}
	public List<BlogPost> getMiBlog() {
		return bdao.getUserBlog(getUser(), false);
	}

	void onActionFromVoteBlogUp() {
	}
	void onActionFromVoteBlogDown() {
	}
	void onActionFromVoteComentUp() {
	}
	void onActionFromVoteComentDown() {
	}

	public String getComentZoneId() {
		return String.format("czone%d", coment.getCid());
	}
	/** Esto es necesario para poder mostrar las respuestas a un comentario en donde realmente deben ir. */
	public String getRzoneClientId() {
		return rzone.getClientId();
	}

	public boolean isBlogDistinto() {
		return otrob == null || blog.getBid() != otrob.getBid();
	}

}
