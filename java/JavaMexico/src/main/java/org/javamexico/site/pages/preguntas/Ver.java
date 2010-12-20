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
package org.javamexico.site.pages.preguntas;
	
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.javamexico.dao.PreguntaDao;
import org.javamexico.entity.pregunta.ComentPregunta;
import org.javamexico.entity.pregunta.ComentRespuesta;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.Respuesta;
import org.javamexico.entity.pregunta.TagPregunta;
import org.javamexico.site.base.Pagina;
import org.slf4j.Logger;

/** Esta pagina muestra una sola pregunta, con sus respuestas. Permite agregar una respuesta
 * 
 * @author Enrique Zamudio
 */
@Import(stylesheet="context:layout/preguntas.css")
public class Ver extends Pagina {

	@Inject
	private Logger log;
	@Inject
	@Service("preguntaDao")
	private PreguntaDao pdao;
	@Property private Pregunta pregunta;
	@Property private Pregunta otrap;
	@Property private Respuesta resp;
    @SuppressWarnings("unused")
	@Property private TagPregunta tag;
	@Property private String resptext;
    @SuppressWarnings("unused")
	@Property private ComentPregunta pcomm;
    @SuppressWarnings("unused")
	@Property private ComentRespuesta rcomm;
	private int rid;
	@Inject
	private Request req;

	/** Con esto obtenemos la pregunta con la clave indicada en el URL */
	Object onActivate(String ids) {
		int pid = 0;
		if (ids.indexOf('-') > 0) {
			pid = Integer.parseInt(ids.substring(0, ids.indexOf('-')));
			rid = Integer.parseInt(ids.substring(ids.indexOf('-') + 1));
			log.warn("Me activan con pid {} rid {}", pid, rid);
		} else {
			pid = Integer.parseInt(ids);
		}
		if (pid > 0) {
			pregunta = pdao.getPregunta(pid);
			if (pregunta != null && rid > 0) {
				log.warn("Buscando respuesta {}", rid);
				resp = findRespuesta();
			}
		}
		if (pregunta == null) {
			//Redirigir al indice
			return Index.class;
		}
		return null;
	}
	/** Con esto volvemos a poner la clave de la pregunta en el URL */
	Object onPassivate() {
		if (pregunta == null) {
			return null;
		}
		return resp == null ? pregunta.getPid() : String.format("%d-%d", pregunta.getPid(), resp.getRid());
	}

	void onSuccessFromResponder(int pid) {
		pregunta = pdao.getPregunta(pid);
		if (resptext == null) {
			resptext = req.getParameter("resptext");
		}
		resp = pdao.addRespuesta(resptext, pregunta, getUser());
	}

	void onSuccessFromComentarPregunta(int pid) {
		pregunta = pdao.getPregunta(pid);
		if (resptext == null) {
			resptext = req.getParameter("qcomment");
		}
		pcomm = pdao.addComentario(resptext, pregunta, getUser());
	}

	private Respuesta findRespuesta() {
		for (Respuesta r : pregunta.getRespuestas()) {
			if (r.getRid() == rid) {
				return r;
			}
		}
		return null;
	}

	void onSuccessFromComentarRespuesta(String ids) {
		onActivate(ids);
		resp = findRespuesta();
		if (resp == null) {
			log.warn("No se puede agregar comentario a respuesta nula");
			return;
		}
		if (resptext == null) {
			resptext = req.getParameter("rcomment");
		}
		log.info("Agregando comentario '{}' a resp {}", resptext, resp);
		pdao.addComentario(resptext, resp, getUser());
		resp = null;
	}

	/** Devuelve el ID de la pregunta y de la respuesta actual (o el rid que ya teniamos si no hay respuesta actual) */
	public String getRcformContext() {
		return String.format("%d-%d", pregunta.getPid(), resp == null ? rid : resp.getRid());
	}

	public List<Pregunta> getMisPreguntas() {
		return pdao.getPreguntasUsuario(getUser());
	}

	public boolean isResponderCurrentUser() {
		if (getUserExists()) {
			return getUser().getUid() == pregunta.getAutor().getUid();
		}
		return false;
	}

	void onActionFromPickAnswer(String ctxt) {
		int idx = ctxt.indexOf('-');
		if (idx > 0) {
			pregunta = pdao.getPregunta(Integer.parseInt(ctxt.substring(0, idx)));
			if (pregunta != null) {
				log.info("Seleccionando respuesta", ctxt);
				pregunta.setRespuestaElegida(new Integer(ctxt.substring(idx + 1)));
				pdao.update(pregunta);
			}
		}
	}

	public boolean isChosenAnswer() {
		return pregunta.getRespuestaElegida() != null && pregunta.getRespuestaElegida().intValue() == resp.getRid();
	}

	void onActionFromVoteQuestionUp(String ctxt) {
		onActivate(ctxt);
		pdao.vota(getUser(), pregunta, true);
	}

	void onActionFromVoteQuestionDown(String ctxt) {
		onActivate(ctxt);
		pdao.vota(getUser(), pregunta, false);
	}

	void onActionFromVoteAnswerUp(String ctxt) {
		onActivate(ctxt);
		resp = findRespuesta();
		pdao.vota(getUser(), resp, true);
	}

	void onActionFromVoteAnswerDown(String ctxt) {
		onActivate(ctxt);
		resp = findRespuesta();
		pdao.vota(getUser(), resp, false);
	}

	public boolean isPreguntaVotada() {
		return pregunta != null && getUserExists() && pdao.findVoto(getUser(), pregunta) != null;
	}

	public boolean isRespuestaVotada() {
		return resp != null && getUserExists() && pdao.findVoto(getUser(), resp) != null;
	}

	public List<Pregunta> getPregsSimilares() {
		if (pregunta.getTags().size() == 1) {
			return pdao.getPreguntasConTag(pregunta.getTags().iterator().next());
		} else {
			return pdao.getPreguntasConTags(pregunta.getTags());
		}
	}

	public boolean isPreguntaDistinta() {
		return otrap == null || pregunta.getPid() != otrap.getPid();
	}

}
