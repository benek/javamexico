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
package org.javamexico.site.pages.foros;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.foro.ComentForo;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.TagForo;
import org.javamexico.site.base.Pagina;
import org.slf4j.Logger;

/** La pagina para mostrar un foro, ver y agregar comentarios, etc.
 * 
 * @author Enrique Zamudio
 */
public class Ver extends Pagina {

	@Inject @Service("foroDao")
	private ForoDao fdao;
	@Inject private Logger log;
	@Property private Foro foro;
	@Property private Foro otrof;
    @SuppressWarnings("unused")
	@Property private TagForo tag;
	@Property private ComentForo coment;
	@Property private ComentForo comresp;
	@Property private String commtext;
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
			log.warn("Me activan con fid {} cid {}", fid, cid);
		} else {
			fid = Integer.parseInt(ids);
		}
		if (fid > 0) {
			foro = fdao.getForo(fid);
		}
		if (foro == null) {
			return Index.class;
		} else if (cid > 0) {
			for (ComentForo cf : getComents()) {
				if (cf.getCfid() == cid) {
					coment = cf;
				}
			}
		}
		return null;
	}

	Object onPassivate() {
		if (foro == null) {
			return null;
		}
		return coment == null ? foro.getFid() : String.format("%d-%d", foro.getFid(), coment.getCfid());
	}

	public List<ComentForo> getComents() {
		return fdao.getComentarios(foro, 1, 30, false);
	}

	public List<Foro> getMisForos() {
		return getUserExists() ? fdao.getForosByUser(getUser(), true) : null;
	}

	public List<Foro> getForosSimilares() {
		if (foro.getTags().size() == 1) {
			return fdao.getForosConTag(foro.getTags().iterator().next());
		} else {
			return fdao.getForosConTags(new ArrayList<TagForo>(foro.getTags()));
		}
	}

	public boolean isForoVotado() {
		return foro != null && getUserExists() && fdao.findVoto(getUser(), foro) != null;
	}

	public boolean isComentVotado() {
		return coment != null && getUserExists() && fdao.findVoto(getUser(), coment) != null;
	}

	public String getComentZoneId() {
		return String.format("czone%d", coment.getCfid());
	}

	Object onActionFromShowResps() {
		return subcomentZone.getBody();
	}

	void onActionFromVoteForoUp() {
		fdao.vota(getUser(), foro, true);
	}

	void onActionFromVoteForoDown() {
		fdao.vota(getUser(), foro, false);
	}

	void onActionFromVoteComentUp() {
		fdao.vota(getUser(), coment, true);
	}

	void onActionFromVoteComentDown() {
		fdao.vota(getUser(), coment, false);
	}

	void votaRespuestaComent(int crid, boolean up) {
		if (coment == null) {
			log.warn("No puedo votar nada porque no hay comentario padre");
			return;
		} else {
			for (ComentForo cf : coment.getRespuestas()) {
				if (cf.getCfid() == crid) {
					comresp = cf;
				}
			}
		}
		if (comresp == null) {
			log.warn("No encuentro respuesta {} hija de coment {}", crid, coment.getCfid());
		} else {
			fdao.vota(getUser(), comresp, up);
		}
	}

	void onActionFromVoteComrespUp(int crid) {
		votaRespuestaComent(crid, true);
	}

	void onActionFromVoteComrespDown(int crid) {
		votaRespuestaComent(crid, false);
	}
	void onSuccessFromResponder() {
		coment = fdao.addComment(commtext, coment, getUser());
	}

	void onSuccessFromComentar() {
		coment = fdao.addComment(commtext, foro, getUser());
	}

	/** Devuelve el ID del foro y del comentario actual (o el cid que ya teniamos si no hay comentario actual) */
	public String getCcformContext() {
		return String.format("%d-%d", foro.getFid(), coment == null ? cid : coment.getCfid());
	}

	/** Esto es necesario para poder mostrar las respuestas a un comentario en donde realmente deben ir. */
	public String getRzoneClientId() {
		return rzone.getClientId();
	}

	public boolean isForoDistinto() {
		return otrof == null || foro.getFid() != otrof.getFid();
	}

}
