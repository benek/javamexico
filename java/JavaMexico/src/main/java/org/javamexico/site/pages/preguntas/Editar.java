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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.PreguntaDao;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.TagPregunta;
import org.javamexico.site.base.Pagina;
import org.slf4j.Logger;

/** Esta pagina permite editar una pregunta existente (solo admins, o el autor de la pregunta).
 * 
 * @author Enrique Zamudio
 */
@Import(stylesheet="context:layout/preguntas.css")
public class Editar extends Pagina {

	@Inject
	private Logger log;
	@Property
	private Pregunta pregunta;
	@Property
	private TagPregunta tag;
	@Inject
	@Service("preguntaDao")
	private PreguntaDao qdao;
	@Property
	private String newtag;
	@InjectComponent
	private Zone tagzone;

	Object onActivate(String ctxt) {
		if (!getUserExists()) {
			return Index.class;
		}
		try {
			int pid = Integer.parseInt(ctxt);
			pregunta = qdao.getPregunta(pid);
			if (pregunta == null) {
				return Index.class;
			}
			if (pregunta.getAutor().getUid() != getUser().getUid()) {
				log.info("Redirijo a index porque usuario no es autor de la pregunta");
				//TODO verificar si el usuario es administrador, en ese caso devolver null
				return Index.class;
			}
			return null;
		} catch (NumberFormatException ex) {
			return Index.class;
		}
	}
	Object onPassivate() {
		if (pregunta == null) {
			return "";
		}
		return pregunta.getPid();
	}

	void onSuccessFromEditar() {
		pregunta.setStatus(1);
		qdao.update(pregunta);
	}

	Object onActionFromEliminar() {
		qdao.delete(pregunta);
		return Index.class;
	}

	Object onActionFromRmtag(String ctxt) {
		int idx = ctxt.indexOf('-');
		if (idx > 0) {
			pregunta = qdao.getPregunta(Integer.parseInt(ctxt.substring(0, idx)));
			int tid = Integer.parseInt(ctxt.substring(idx + 1));
			for (TagPregunta t : pregunta.getTags()) {
				if (t.getTid() == tid) {
					tag = t;
					break;
				}
			}
			if (tag != null) {
				pregunta.getTags().remove(tag);
				qdao.update(pregunta);
			}
		}
		return tagzone.getBody();
	}

	public List<String> onProvideCompletionsFromAddtag(String parte) {
		List<TagPregunta> tags = qdao.findMatchingTags(parte);
		log.info("Tags encontrados: {}", tags);
		ArrayList<String> rv = new ArrayList<String>(tags.size());
		for (TagPregunta t : tags) {
			rv.add(t.getTag());
		}
		return rv;
	}

	void onSuccessFromAddtags() {
		qdao.addTag(newtag, pregunta);
	}

	public String getTagContext() {
		return String.format("%d-%d", pregunta.getPid(), tag.getTid());
	}

}
