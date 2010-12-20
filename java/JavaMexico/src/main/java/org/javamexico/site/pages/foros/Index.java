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

import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.TagForo;
import org.javamexico.entity.foro.TemaForo;
import org.javamexico.site.base.Pagina;

/** Pagina principal de la seccion de foros.
 * 
 * @author Enrique Zamudio
 */
@Import(stylesheet="context:layout/preguntas.css")
public class Index extends Pagina {

	@Inject
	@Service("foroDao")
	private ForoDao fdao;
	@Property private Foro foro;
	@Property private TagForo tag;
	@Property private TemaForo tema;
	//El tag para buscar foros
	@Property private String stag;

	/** Esto se invoca cuando trae contexto la liga a esta pagina */
	void onActivate(String value) {
		stag = value;
	}

	public List<TemaForo> getTemas() {
		return fdao.getTemas();
	}

	public List<Foro> getForos() {
		//TODO paginar...
		if (stag == null) {
			return fdao.getForosConTema(tema, 1, 5);
			//return fdao.getForosRecientes(1, 10);
		} else {
			return fdao.getForosConTag(stag);
		}
	}

	public List<TagForo> getPopTags() {
		return fdao.getTagsPopulares(10);
	}

	public List<Foro> getMisForos() {
		return getUserExists() ? fdao.getForosByUser(getUser(), true) : null;
	}

}
