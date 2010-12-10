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
package org.javamexico.dao;

import org.javamexico.entity.TagUsuario;
import org.javamexico.entity.Usuario;

import java.util.List;

/** Define la funcionalidad del DAO para manejo de usuarios.
 * 
 * @author Enrique Zamudio
 */
public interface UserDao {

	/** Busca un usuario con el nombre especificado; si existe, valida su
	 * password y si es correcto devuelve el usuario. */
	public Usuario validaLogin(String username, String password);

	/** Devuelve una lista con todos los usuarios registrados en el sistema. */
	public List<Usuario> getAllUsers();

	/** Devuelve al usuario con la clave especificada. */
	public Usuario getUser(int id);
    /** Devuelve el usuario que tenga el email indicado. */
    public Usuario findByEmail(String email);

	/** Inserta un nuevo usuario a la base de datos. */
	public void insert(Usuario u);

	/** Actualiza los datos de un usuario en base de datos. */
	public void update(Usuario u);

	/** Elimina de la base de datos al usuario especificado. */
	public void delete(Usuario u);

	/** Agrega el tag indicado al usuario. Si no existe el tag aun, se registra primero en base de datos. */
	public void addTag(String tag, Usuario u);

	/** Devuelve una lista de tags cuyo texto contenga la cadena parcial especificada. */
	public List<TagUsuario> findMatchingTags(String parcial);

    /** Cifra un password con los datos adicionales de ID y nombre de usuario. */
    public String cifraPassword(String pass, String uname, int uid);

}
