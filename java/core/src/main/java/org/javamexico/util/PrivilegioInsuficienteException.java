package org.javamexico.util;

/** Esta excepcion se debe arrojar cuando un usuario quiere realizar una operacion para la cual
 * no tiene privilegio suficiente.
 * 
 * @author Enrique Zamudio
 */
public class PrivilegioInsuficienteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PrivilegioInsuficienteException() {
	}

	public PrivilegioInsuficienteException(String mensaje) {
		super(mensaje);
	}

}
