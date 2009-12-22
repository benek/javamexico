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
package org.javamexico.entity.foro;

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

/** Representa un comentario hecho en un foro por un usuario registrado.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="coment_foro")
public class ComentForo {

	private int cfid;
	private Foro foro;
	private Usuario autor;
	private ComentForo rt; //para manejar threads de comentarios
	private Set<ComentForo> replies;
	private Date fecha;
	private String coment;

	@Id
	@SequenceGenerator(name="pk", sequenceName="coment_foro_cfid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getCfid() {
		return cfid;
	}
	public void setCfid(int value) {
		cfid = value;
	}

	@ManyToOne
	@JoinColumn(name="fid")
	public Foro getForo() {
		return foro;
	}
	public void setForo(Foro value) {
		foro = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getAutor() {
		return autor;
	}
	public void setAutor(Usuario value) {
		autor = value;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	public String getComentario() {
		return coment;
	}
	public void setComentario(String value) {
		coment = value;
	}

	@ManyToOne
	@JoinColumn(name="rt")
	public ComentForo getInReplyTo() {
		return rt;
	}
	public void setInReplyTo(ComentForo value) {
		rt = value;
	}

	@OneToMany(mappedBy="foro")
	public Set<ComentForo> getRespuestas() {
		return replies;
	}
	public void setRespuestas(Set<ComentForo> value) {
		replies = value;
	}

}
