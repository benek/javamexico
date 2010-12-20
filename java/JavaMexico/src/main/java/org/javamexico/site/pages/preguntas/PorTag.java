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

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.PreguntaDao;
import org.javamexico.entity.pregunta.Pregunta;

/** Lista las preguntas por tag.
 * 
 * @author Enrique Zamudio
 */
public class PorTag {

	@Property private String tag;
	@Inject @Service("preguntaDao")
	private PreguntaDao pdao;
    @SuppressWarnings("unused")
	@Property private Pregunta pregunta;

	void onActivate(String value) {
		tag = value;
	}
	String onPassivate() {
		return tag;
	}

	/** Devuelve las preguntas mas recientes para mostrarlas en la pagina. */
	public List<Pregunta> getPreguntas() {
		return pdao.getPreguntasConTag(tag);
	}

}
