package org.javamexico.site.services;

import org.javamexico.dao.UserDao;
import org.javamexico.entity.Usuario;
import org.javamexico.util.Base64;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Date: Dec 10, 2010 Time: 2:11:05 PM
 *
 * @author Enrique Zamudio
 */
public class UserPasswordManagerImpl implements UserPasswordManager {

    @Resource(name="usuarioDao")
    private UserDao udao;

    public String generaTokenOlvidoPassword(Usuario u) {
        //Este es el token
        String t = String.format("%1$d %2$TF %2$TT", u.getUid(), u.getFechaAlta());
        //Usamos su password actual como llave
        try {
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = Base64.base64Decode(u.getPassword());
            SecretKeySpec key = new SecretKeySpec(raw, 4, 16, "AES");
            aes.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(raw, 0, 16));
            raw = aes.doFinal(t.getBytes());
            return Base64.base64Encode(raw, 0, raw.length);
        } catch (GeneralSecurityException ex) {
            //TODO error
        }
        return null;
    }

    /** Valida que el usuario tenga el token indicado. */
    public boolean validaToken(int uid, String token) {
        Usuario u = udao.getUser(uid);
        if (u != null) {
            return validaToken(u, token);
        }
        return false;
    }

    public boolean validaToken(Usuario u, String token) {
        String t = String.format("%1$d %2$TF %2$TT", u.getUid(), u.getFechaAlta());
        try {
            Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = Base64.base64Decode(u.getPassword());
            SecretKeySpec key = new SecretKeySpec(raw, 4, 16, "AES");
            aes.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(raw, 0, 16));
            String t2 = new String(aes.doFinal(Base64.base64Decode(token)));
            System.out.printf("T(%s) vs T2(%s)%n", t, t2);
            return t.equals(t2);
        } catch (GeneralSecurityException ex) {
            //TODO error
        }
        return false;
    }

    /** Cambia el password del usuario indicado, siempre y cuando el password actual coincida con el almacenado
     * en la base de datos. Este método puede usarse también para usuarios que han olvidado su password, pasando
     * el token en vez del password actual.
     * @param uid El identificador unico del usuario.
     * @param curPass El password actual, o el token que se envió al usuario para reiniciar su password.
     * @param newPass El password que desea utilizar a partir de ahora.
     * @return true si se pudo cambiar el password, false si por alguna razon no se pudo cambiar. */
    public boolean cambiaPassword(int uid, String curPass, String newPass) {
        Usuario u = udao.getUser(uid);
        if (u != null) {
            return cambiaPassword(u, curPass, newPass);
        }
        return false;
    }

    public boolean cambiaPassword(Usuario u, String curPass, String newPass) {
        String p2 = udao.cifraPassword(curPass, u.getUsername(), u.getUid());
        if (p2.equals(u.getPassword()) || validaToken(u, curPass)) {
            u.setPassword(newPass);
            u.setToken(null);
            udao.update(u);
            return true;
        }
        return false;
    }

}
