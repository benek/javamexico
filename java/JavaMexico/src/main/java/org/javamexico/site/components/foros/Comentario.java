package org.javamexico.site.components.foros;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.ForoDao;
import org.javamexico.entity.foro.ComentForo;

/** Este componente despliega un comentario de un foro y, si el comentario tiene respuestas, una liga para
 * desplegar las mismas debajo del comentario, indentadas.
 * 
 * @author Enrique Zamudio
 */
@SuppressWarnings("unused")
public class Comentario {

	@Parameter(required=true, defaultPrefix="prop")
	@Property private ComentForo coment;
	@Property private ComentForo comresp;
	@Inject @Service("foroDao")
	private ForoDao dao;

	/** Este metodo lo invoca la liga para ver mas respuestas a un comentario. * /
	Object onActionFromShowResps() {
		return rzone.getBody();
	}*/

}
