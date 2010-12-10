package org.javamexico.site.pages;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.site.base.Pagina;
import org.javamexico.site.services.UserPasswordManager;

/**
 * Created by IntelliJ IDEA.
 * User: ezamudio
 * Date: Dec 8, 2010
 * Time: 10:59:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class CambiaPassword extends Pagina {

    @Property private Usuario u;
    @Property private String newPass1;
    @Property private String newPass2;
    @Property private String curPass;
    @Property @Persist private String mensaje;
    @Inject @Service("usuarioDao")
    private UserDao udao;
    @Inject private UserPasswordManager passman;

    Object onActivate(int uid) {
        u = udao.getUser(uid);
        if (u == null) {
            return Index.class;
        }
        return null;
    }

    Object onSuccessFromNewpass(int uid) {
        onActivate(uid);
        if (curPass != null && curPass.length() > 0) {
            if (newPass1 != null && newPass1.length() > 0 && newPass1.equals(newPass2)) {
                if (passman.cambiaPassword(u, curPass, newPass1)) {
                    u.setPassword(newPass1);
                    udao.update(u);
                    setUser(u);
                    return Index.class;
                } else {
                    mensaje = "El pasword no puede ser modificado porque el anterior es incorrecto.";
                }
            } else {
                mensaje = "El password nuevo debe ser idéntico en ambos intentos.";
            }
        } else {
            mensaje = "El password actual no puede estar en blanco.";
        }
        System.out.println(mensaje);
        return null;
    }

    Object onPassivate() {
        return u == null ? null : u.getUid();
    }
}