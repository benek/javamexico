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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.javamexico.entity.Usuario;

/** Representa una entrada en el blog de un usuario.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="blog_post")
public class BlogPost {

	private int bid;
	private int status;
	private Usuario user;
	private Date fecha;
	private String titulo;
	private String texto;
	private Set<BlogComent> comms;
	private boolean coments; //permite comentarios o no

	@Id
	@SequenceGenerator(name="pk", sequenceName="blog_post_bid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getBid() {
		return bid;
	}
	public void setBid(int value) {
		bid = value;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int value) {
		status = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getAutor() {
		return user;
	}
	public void setAutor(Usuario value) {
		user = value;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String value) {
		titulo = value;
	}

	public String getTexto() {
		return texto;
	}
	public void setTexto(String value) {
		texto = value;
	}

	public boolean isPermiteComentarios() {
		return coments;
	}
	public void setPermiteComentarios(boolean flag) {
		coments = flag;
	}

	@OneToMany(mappedBy="blog")
	public Set<BlogComent> getComentarios() {
		return comms;
	}
	public void setComentarios(Set<BlogComent> value) {
		comms = value;
	}

}
