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
package org.javamexico.entity.blog;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.javamexico.entity.Usuario;

/** Representa un comentario hecho en el blog de un usuario.
 * 
 * @author Enrique Zamudio
 */
@Entity
public class BlogComent {

	private int cid;
	private BlogPost blog;
	private Usuario user;
	private BlogComent rt;
	private Date fecha;
	private String coment;
	private Set<BlogComent> resps;

	@Id
	public int getCid() {
		return cid;
	}
	public void setCid(int value) {
		cid = value;
	}

	public String getComentario() {
		return coment;
	}
	public void setComentario(String value) {
		coment = value;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	@ManyToOne
	@JoinColumn(name="bid")
	public BlogPost getBlog() {
		return blog;
	}
	public void setBlog(BlogPost value) {
		blog = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getAutor() {
		return user;
	}
	public void setAutor(Usuario value) {
		user = value;
	}

	@ManyToOne
	@JoinColumn(name="rt")
	public BlogComent getInReplyTo() {
		return rt;
	}
	public void setInReplyTo(BlogComent value) {
		rt = value;
	}

	@OneToMany(mappedBy="rt")
	public Set<BlogComent> getRespuestas() {
		return resps;
	}
	public void setRespuestas(Set<BlogComent> value) {
		resps = value;
	}

}
