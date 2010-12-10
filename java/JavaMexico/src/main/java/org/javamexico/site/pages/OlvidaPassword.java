package org.javamexico.site.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.site.services.UserPasswordManager;
import org.slf4j.Logger;

/** Página de olvido de password, para generar un token especial y enviar un email al usuario
 * y con esa liga podrá reiniciar su password.
 *
 * @author Enrique Zamudio
 * 
 * Date: Dec 8, 2010 Time: 10:53:36 AM
 */
public class OlvidaPassword {
    @Inject
    private Logger log;
    @Property private String email;
    @Inject @Service("usuarioDao")
    private UserDao udao;
    @Property private String mensaje;
    @Inject private UserPasswordManager passman;

    public void onSuccessFromOlvido() {
        if (email == null || email.length() < 6) {
            mensaje = "El email no parece ser válido.";
        } else {
            Usuario u = udao.findByEmail(email);
            if (u == null) {
                mensaje = "El correo no pertenece a un usuario registrado.";
            } else {
                String token = passman.generaTokenOlvidoPassword(u);
                if (token == null) {
                    mensaje = "No se puede reiniciar password por ahora, reintente más tarde";
                } else {
                    u.setToken(token);
                    udao.update(u);
                    mensaje = "Un correo ha sido enviado a su cuenta con la liga para reiniciar su password.";
                }
            }
        }
    }

}