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
package org.javamexico.site.components;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.site.base.Pagina;
import org.slf4j.Logger;

/** Este componente presenta una liga con cierto texto a los usuarios no registrados
 * y una liga con texto distinto para usuarios registrados; la liga para usuarios no registrados
 * abre una seccion de Login y la liga de usuarios registrados hace visible una zona oculta en la pagina.
 * 
 * Extiende la clase Pagina para tener acceso al usuario en la sesion.
 * 
 * @author Enrique Zamudio
 */
public class LigaUsuario extends Pagina {

	@Inject private Logger log;
	/** Este es el texto de la liga cuando ya hay usuario en la sesion. */
	@Parameter(required=true, defaultPrefix="literal")
    @SuppressWarnings("unused")
	@Property private String text;
	/** El username para la forma de login. */
	@Property private String uname;
	/** El password para la forma de login. */
	@Property private String passwd;
	/** Este bloque contiene el contenido que envuelve este componente. */
	@Inject private Block innerBlock;
	/** Este bloque contiene la forma de login que se muestra si el usuario no esta registrado. */
	@Inject private Block loginBlock;
	@Inject @Service("usuarioDao")
	private UserDao udao;
	@InjectComponent
	private Form login;
	@Inject
	private Request request;
	/** Esta es la zona que contiene la liga de login o con el texto configurado. */
	@InjectComponent private Zone vzone;
	private String vzid;

	Object onSuccessFromLogin(String zid) {
		vzid = zid;
		log.debug("en login, contextPath={} context={}",request.getContextPath(), request.getParameter("t:ac"));
		Usuario u = udao.validaLogin(uname, passwd);
		if (u == null) {
			login.recordError("Usuario inexistente, o password invalido.");
			if (request.isXHR()) {
				return loginBlock;
			}
		} else {
			//Con esto ya se queda el usuario en la sesion
			setUser(u);
			if (request.isXHR()) {
				return innerBlock;
			}
		}
		return null;
	}

	Object onActionFromLoggedLink(String zid) {
		vzid = zid;
		return innerBlock;
	}

	Object onActionFromLoginLink(String zid) {
		vzid = zid;
		return loginBlock;
	}

	public String getZoneClientId() {
		if (vzid == null) {
			vzid = vzone.getClientId();
		}
		return vzid;
	}

	public void afterRender() {
		vzid = null;
	}

}
