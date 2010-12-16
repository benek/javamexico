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

import org.javamexico.entity.pregunta.ComentPregunta;
import org.javamexico.entity.pregunta.ComentRespuesta;
import org.javamexico.entity.pregunta.Pregunta;
import org.javamexico.entity.pregunta.Respuesta;
import org.javamexico.entity.pregunta.TagPregunta;
import org.javamexico.entity.pregunta.VotoPregunta;
import org.javamexico.entity.pregunta.VotoRespuesta;
import org.javamexico.entity.Usuario;
import org.javamexico.util.PrivilegioInsuficienteException;

import java.util.List;
import java.util.Set;

/** Esta interfaz define los metodos que debe implementar un DAO para el modulo de preguntas y respuestas.
 * 
 * @author Enrique Zamudio
 */
public interface PreguntaDao {

	/** Devuelve las preguntas mas recientes, ordenadas desde la mas reciente hacia la mas antigua.
	 * @param page La pagina de preguntas (comienza de la 1)
	 * @param pageSize El numero de resultados a devolver por pagina. */
	public List<Pregunta> getPreguntasRecientes(int page, int pageSize);

	/** Devuelve las preguntas hechas por el usuario indicado. */
	public List<Pregunta> getPreguntasUsuario(Usuario user);

	/** Devuelve las preguntas que contienen el tag especificado. */
	public List<Pregunta> getPreguntasConTag(TagPregunta tag);

	/** Devuelve todas las preguntas que tengan al menos uno de los tags especificados. */
	public List<Pregunta> getPreguntasConTags(Set<TagPregunta> tags);

	/** Devuelve las preguntas que contienen el tag especificado. */
	public List<Pregunta> getPreguntasConTag(String tag);

	/** Devuelve los tags mas utilizados en preguntas.
	 * @param limit El numero maximo de tags a devolver. */
	public List<TagPregunta> getTagsPopulares(int limit);

	/** Devuelve la pregunta con el id especificado. */
	public Pregunta getPregunta(int id);

	/** Devuelve las respuestas a la pregunta especificada.
	 * @param pageSize El numero maximo de preguntas a devolver.
	 * @param page El numero de pagina a devolver (la primera pagina es 1)
	 * @param crono Indica si se deben ordenar las respuestas en orden cronologico, en vez de por votos
	 * (default es por votos). */
	public List<Respuesta> getRespuestas(Pregunta q, int pageSize, int page, boolean crono);

	/** Devuelve la lista de preguntas con mas votos positivos.
	 * @param limit El numero maximo de preguntas a devolver. */
	public List<Pregunta> getPreguntasMasVotadas(int limit);

	/** Devuelve la lista de preguntas que no tienen ni una sola respuesta. */
	public List<Pregunta> getPreguntasSinResponder(int limit);

	/** Devuelve la lista de preguntas cuyo autor no ha elegido una respuesta. */
	public List<Pregunta> getPreguntasSinResolver(int limit);

	/** Registra un voto que un usuario hace a una pregunta.
	 * @param user El usuario que hace el voto
	 * @param pregunta La pregunta a la cual se aplica el voto
	 * @param up Indica si el voto es positivo (true) o negativo (false).
	 * @throws PrivilegioInsuficienteException si el usuario no tiene reputacion suficiente para dar un voto negativo. */
	public VotoPregunta vota(Usuario user, Pregunta pregunta, boolean up) throws PrivilegioInsuficienteException;

	/** Registra un voto que un usuario hace a una respuesta que se hizo a una pregunta.
	 * @param user El usuario que hace el voto.
	 * @param resp La respuesta a la cual se aplica el voto.
	 * @param up Indica si el voto es positivo (true) o negativo (false).
	 * @throws PrivilegioInsuficienteException si el usuario no tiene reputacion suficiente para dar un voto negativo. */
	public VotoRespuesta vota(Usuario user, Respuesta resp, boolean up) throws PrivilegioInsuficienteException;

	/** Busca y devuelve el voto realizado por el usuario indicado a la pregunta especificada, si es que existe. */
	public VotoPregunta findVoto(Usuario user, Pregunta pregunta);

	/** Busca y devuelve el voto hecho por el usuario indicado a la respuesta especificada, si es que existe. */
	public VotoRespuesta findVoto(Usuario user, Respuesta respuesta);

	public void insert(Pregunta p);

	public void update(Pregunta p);

	public void delete(Pregunta p);

	/** Crea una respuesta con el texto especificado, para la pregunta especificada, por el autor especificado.
	 * @return La respuesta recien creada. */
	public Respuesta addRespuesta(String resp, Pregunta p, Usuario autor);

	/** Agrega un comentario a la pregunta especificada, a nombre del usuario especificado. */
	public ComentPregunta addComentario(String c, Pregunta p, Usuario autor);

	/** Agrega un comentario a la respuesta especificada, a nombre del usuario especificado. */
	public ComentRespuesta addComentario(String c, Respuesta r, Usuario autor);

	/** Agrega el tag especificado a la pregunta. Primero se busca el tag existente para relacionarlo con la
	 * pregunta, pero si no existe, se crea uno nuevo. */
	public void addTag(String tag, Pregunta p);

	/** Devuelve una lista con tags que contengan el texto parcial especificado. */
	public List<TagPregunta> findMatchingTags(String parcial);

}
