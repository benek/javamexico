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

import org.javamexico.entity.blog.BlogPost;
import org.javamexico.entity.blog.BlogComent;
import org.javamexico.entity.blog.VotoBlog;
import org.javamexico.entity.blog.VotoComentBlog;
import org.javamexico.entity.Usuario;

import java.util.Date;
import java.util.List;

/** Define la funcionalidad del DAO para manejo de la seccion de blogs.
 * 
 * @author Enrique Zamudio
 */
public interface BlogDao {

	/** Devuelve todas las entradas de blog de un usuario, empezando por la mas reciente.
	 * @param user El usuario cuyas entradas se deben devolver.
	 * @param published Indica si solamente se deben devolver las entradas publicadas, o todas. */
	public List<BlogPost> getUserBlog(Usuario user, boolean published);

	/** Devuelve las entradas mas recientes de blogs publicadas, sin importar el autor.
	 * @param desde La fecha a partir de la cual se toman los blogs. */
	public List<BlogPost> getBlogsRecientes(Date desde);

	/** Devuelve la entrada de blog con la clave especificada. */
	public BlogPost getBlog(int id);

	public List<BlogComent> getComentarios(BlogPost blog, int pageSize, int page, boolean crono);

	public void insert(BlogPost post);

	public void update(BlogPost post);

	public void delete(BlogPost post);

	/** Agrega un comentario al blog indicado, como respuesta al comentario especificado.
	 * @param coment El comentario a agregar
	 * @param post El blog al cual se va a agregar el comentario
	 * @param parent El comentario al cual se esta contestando (opcional). */
	public void addComment(BlogComent coment, BlogPost post, BlogComent parent);

	/** Emite un voto del usuario al post de un blog. Si el usuario ya habia votado por el blog, se actualiza el voto. */
	public VotoBlog votaBlog(Usuario user, BlogPost post, boolean up);

	/** Emite un voto del usuario al comentario de un blog. Si el usuario ya habia votado por el comentario, se actualiza
	 * el voto. */
	public VotoComentBlog votaComentario(Usuario user, BlogComent comm, boolean up);

	public VotoBlog findVotoBlog(Usuario user, BlogPost post);

	public VotoComentBlog findVotoComent(Usuario user, BlogComent comm);

}
