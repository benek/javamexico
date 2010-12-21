package org.javamexico.site.components.foros;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.Comment;

/** Este componente despliega un comentario de un foro y, si el comentario tiene respuestas, una liga para
 * desplegar las mismas debajo del comentario, indentadas.
 * 
 * @author Enrique Zamudio
 */
@SuppressWarnings("unused")
public class Comentario {

	@Parameter(required=true, defaultPrefix="prop")
	@Property private Comment coment;
	@Property private Comment comresp;

	/** Este metodo lo invoca la liga para ver mas respuestas a un comentario. * /
	Object onActionFromShowResps() {
		return rzone.getBody();
	}*/

}
