package org.javamexico.site.pages.blogs;

import java.util.List;

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
import org.javamexico.site.pages.foros.Index;
import org.slf4j.Logger;

/** Esta pagina muestra una entrada de blog y acepta registrar comentarios (siempre y cuando
 * el blog lo permita).
 * 
 * @author Enrique Zamudio
 */
public class Ver extends Pagina {

	@Inject private Logger log;
	@Property private BlogPost blog;
	@Property private BlogPost otrob;
    @SuppressWarnings("unused")
	@Property private TagBlog tag;
	@Property private BlogComent coment;
	@Property private BlogComent comresp;
	@Property private String commtext;
	@Inject @Service("blogDao")
	private BlogDao bdao;
	/** Esta zona contiene una liga para mostrar respuestas a un comentario. */
	@InjectComponent private Zone rzone;
	/** Esta zona despliega las respuestas a un comentario. */
	@InjectComponent private Zone subcomentZone;
	/** Aqui vamos a guardar el ID de un comentario especifico, para AJAX */
	private int cid;

	Object onActivate(String ids) {
		int fid = 0;
		if (ids.indexOf('-') > 0) {
			fid = Integer.parseInt(ids.substring(0, ids.indexOf('-')));
			cid = Integer.parseInt(ids.substring(ids.indexOf('-') + 1));
			log.warn("Me activan con bid {} cid {}", fid, cid);
		} else {
			fid = Integer.parseInt(ids);
		}
		if (fid > 0) {
			blog = bdao.getBlog(fid);
		}
		if (blog == null) {
			return Index.class;
		} else if (cid > 0) {
			for (BlogComent cf : getComents()) {
				if (cf.getCid() == cid) {
					coment = cf;
				}
			}
		}
		return null;
	}
	Object onPassivate() {
		if (blog == null) {
			return null;
		}
		return coment == null ? blog.getBid() : String.format("%d-%d", blog.getBid(), coment.getCid());
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
		bdao.votaBlog(getUser(), blog, true);
	}
	void onActionFromVoteBlogDown() {
		bdao.votaBlog(getUser(), blog, false);
	}
	void onActionFromVoteComentUp() {
		bdao.votaComentario(getUser(), coment, true);
	}
	void onActionFromVoteComentDown() {
		bdao.votaComentario(getUser(), coment, false);
	}
	void onActionFromVoteComrespUp(int crid) {
		votaRespuestaComent(crid, true);
	}

	void onActionFromVoteComrespDown(int crid) {
		votaRespuestaComent(crid, false);
	}
	void votaRespuestaComent(int crid, boolean up) {
		if (coment == null) {
			log.warn("No puedo votar nada porque no hay comentario padre");
			return;
		} else {
			for (BlogComent cf : coment.getRespuestas()) {
				if (cf.getCid() == crid) {
					comresp = cf;
				}
			}
		}
		if (comresp == null) {
			log.warn("No encuentro respuesta {} hija de coment {}", crid, coment.getCid());
		} else {
			bdao.votaComentario(getUser(), comresp, up);
		}
	}

	Object onActionFromShowResps() {
		return subcomentZone.getBody();
	}

	void onSuccessFromResponder() {
		coment = bdao.addComment(commtext, coment, getUser());
	}

	void onSuccessFromComentar() {
		coment = bdao.addComment(commtext, blog, getUser());
	}

	/** Devuelve el ID del foro y del comentario actual (o el cid que ya teniamos si no hay comentario actual) */
	public String getCcformContext() {
		return String.format("%d-%d", blog.getBid(), coment == null ? cid : coment.getCid());
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
