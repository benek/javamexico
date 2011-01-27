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

import org.javamexico.entity.foro.Foro;
import org.javamexico.entity.foro.ComentForo;
import org.javamexico.entity.foro.TagForo;
import org.javamexico.entity.foro.TemaForo;
import org.javamexico.entity.foro.VotoComentForo;
import org.javamexico.entity.foro.VotoForo;
import org.javamexico.entity.Usuario;
import org.javamexico.util.PrivilegioInsuficienteException;

import java.util.List;
import java.util.Set;

/** Define la funcionalidad del DAO para la seccion de foros.
 * 
 * @author Enrique Zamudio
 */
public interface ForoDao {

	/** Devuelve los foros creados por el usuario especificado.
	 * @param user El creador de los foros
	 * @param published Indica si se deben devolver solamente sus foros publicados, o todos. */
	public List<Foro> getForosByUser(Usuario user, boolean published);

	/** Devuelve los foros creados en orden cronologico inverso, sin importar el autor.
	 * @param page El numero de pagina (comienza en la 1)
	 * @param pageSize el numero de foros a mostrar por pagina. */
	public List<Foro> getForosRecientes(int page, int pageSize);

	/** Devuelve los foros con mayor actividad (numero de comentarios). */
	public List<Foro> getForosMasActivos(int limit);

	/** Devuelve una lista de los foros con el tema especificado, ordenados de los mas votados a los menos. */
	public List<Foro> getForosConTema(TemaForo tema, int page, int pageSize);

	/** Devuelve el foro con el ID especificado. */
	public Foro getForo(int id);

	/** Devuelve una lista de todos los temas de foros registrados. */
	public List<TemaForo> getTemas();

	/** Devuelve el tema con la clave especificada. */
	public TemaForo getTema(int tid);

	/** Devuelve los comentarios del foro indicado, segun se pida: puede ser en orden
	 * cronologico inverso, o por numero de votos (el mas votado primero).
	 * IMPORTANTE: Solamente se devuelven comentarios directos al foro; los comentarios
	 * que son respuestas a otros comentarios se deben obtener con el metodo {@link #getRespuestas(ComentForo)}
	 *  @param foro El foro cuyos comentarios se quieren obtener.
	 *  @param page El numero de pagina a obtener (la primera es 1)
	 *  @param pageSize El numero de comentarios por pagina.
	 *  @param crono Indica si los comentarios se deben mostrar en orden cronologico
	 *         (el mas nuevo primero); "false" significa ordenar por los mas votados primero.
	 */
	public List<ComentForo> getComentarios(Foro foro, int page, int pageSize, boolean crono);

	/** Devuelve las respuestas al comentario especificado (solamente navega un nivel). */
	public Set<ComentForo> getRespuestas(ComentForo coment);

	/** Inserta nuevo foro en la base de datos. */
	public void insert(Foro foro);
	/** Actualiza los datos del foro en la base de datos. */
	public void update(Foro foro);
	/** Elimina de la base de datos el foro especificado. */
	public void delete(Foro foro);

	public void insert(TemaForo tema);
	public void update(TemaForo tema);
	/** Elimina un tema de foro, siempre y cuando no haya foros registrados bajo el tema.
	 * @return true si el tema se puede eliminar, false si no se elimina porque hay contenido. */
	public boolean delete(TemaForo tema);

	/** Agrega un comentario al foro especificado.
	 * @param coment El comentario a agregar.
	 * @param foro El foro donde se va a agregar el comentario.
	 * @param autor El usuario que hace el comentario. */
	public ComentForo addComment(String coment, Foro foro, Usuario autor);

	/** Agrega un comentario como respuesta a otro comentario de un foro.
	 * @param coment El comentario a agregar.
	 * @param parent El comentario al cual se esta respondiendo (opcional).
	 * @param autor El usuario que hace el comentario. */
	public ComentForo addComment(String coment, ComentForo parent, Usuario autor);

	/** Registra un voto que un usuario hace a un foro.
	 * @param user El usuario que hace el voto.
	 * @param foro El foro al cual se aplica el voto.
	 * @param up Indica si el voto es positivo (true) o negativo (false).
	 * @throws PrivilegioInsuficienteException si el usuario no tiene reputacion suficiente para dar un voto negativo. */
	public VotoForo vota(Usuario user, Foro foro, boolean up) throws PrivilegioInsuficienteException;

	/** Registra un voto que un usuario hace a un comentario en un foro.
	 * @param user El usuario que hace el voto.
	 * @param coment El comentario de foro al cual se aplica el voto.
	 * @param up Indica si el voto es positivo (true) o negativo (false).
	 * @throws PrivilegioInsuficienteException si el usuario no tiene reputacion suficiente para dar un voto negativo. */
	public VotoComentForo vota(Usuario user, ComentForo coment, boolean up) throws PrivilegioInsuficienteException;

	public VotoForo findVoto(Usuario user, Foro foro);

	public VotoComentForo findVoto(Usuario user, ComentForo coment);

	public void addTag(String tag, Foro foro);

	/** Devuelve los tags de foros que contienen el texto parcial especificado. */
	public List<TagForo> findMatchingTags(String parcial);

	/** Devuelve los tags mas utilizados en el modulo de foros. */
	public List<TagForo> getTagsPopulares(int max);

	public List<Foro> getForosConTag(TagForo tag);

	public List<Foro> getForosConTag(String tag);

	public List<Foro> getForosConTags(List<TagForo> tags);

}
