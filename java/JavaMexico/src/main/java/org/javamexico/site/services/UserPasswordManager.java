package org.javamexico.site.services;

import org.javamexico.entity.Usuario;

/** Este componente hace el manejo de passwords de los usuarios:
 * - Genera tokens para usuarios que han olvidado su password
 * - Valida tokens que se reciben para permitir que se reinicie su password
 * - Cambia el password de un usuario (de manera normal o por olvido).
 *
 * Date: Dec 10, 2010 Time: 1:11:25 PM
 *
 * @author Enrique Zamudio
 */
public interface UserPasswordManager {

    /** Genera un token para el usuario indicado y lo almacena en la base de datos. */
    public String generaTokenOlvidoPassword(Usuario u);

    /** Valida que el usuario tenga el token indicado. */
    public boolean validaToken(int uid, String token);

    public boolean validaToken(Usuario u, String token);

    /** Cambia el password del usuario indicado, siempre y cuando el password actual coincida con el almacenado
     * en la base de datos. Este método puede usarse también para usuarios que han olvidado su password, pasando
     * el token en vez del password actual.
     * @param uid El identificador unico del usuario.
     * @param curPass El password actual, o el token que se envió al usuario para reiniciar su password.
     * @param newPass El password que desea utilizar a partir de ahora.
     * @return true si se pudo cambiar el password, false si por alguna razon no se pudo cambiar. */
    public boolean cambiaPassword(int uid, String curPass, String newPass);

    public boolean cambiaPassword(Usuario u, String curPass, String newPass);

}
