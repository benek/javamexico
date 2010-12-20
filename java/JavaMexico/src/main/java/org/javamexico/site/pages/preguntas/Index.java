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
import org.javamexico.dao.PreguntaDao;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.TagPregunta;
import org.javamexico.site.base.Pagina;

/** La pagina principal del modulo de preguntas; muestra una lista de las preguntas mas recientes.
 * 
 * @author Enrique Zamudio
 */
@Import(stylesheet="context:layout/preguntas.css")
public class Index extends Pagina {

	@Inject @Service("preguntaDao")
	private PreguntaDao pdao;
	@SuppressWarnings("unused")
	@Property private Pregunta pregunta;
	@SuppressWarnings("unused")
	@Property private TagPregunta tag;
	//El tag para buscar preguntas
	private String stag;

	public void onActivate(String value) {
		stag = value;
	}

	/** Devuelve las preguntas mas recientes para mostrarlas en la pagina. */
	public List<Pregunta> getPreguntas() {
		if (stag == null) {
			return pdao.getPreguntasRecientes(1, 10);
		} else {
			return pdao.getPreguntasConTag(stag);
		}
	}

	/** Devuelve los 5 tags mas utilizados en el modulo. */
	public List<TagPregunta> getPopTags() {
		return pdao.getTagsPopulares(6);
	}

	public List<Pregunta> getMisPreguntas() {
		return pdao.getPreguntasUsuario(getUser());
	}

}
