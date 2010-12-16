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
package org.javamexico.entity.pregunta;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.javamexico.entity.Usuario;

/** Una pregunta puede tener comentarios hechos por varios usuarios, que pueden ser solicitudes de
 * aclaracion y otro tipo de contenido que no es directamente una respuesta.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="coment_pregunta")
public class ComentPregunta {

	private int cid;
	private Pregunta pregunta;
	private Usuario user;
	private Date fecha;
	private String coment;

	@Id
	@SequenceGenerator(name="pk", sequenceName="coment_pregunta_cid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getCid() {
		return cid;
	}
	public void setCid(int value) {
		cid = value;
	}

	@ManyToOne
	@JoinColumn(name="pid", referencedColumnName="pid")
	public Pregunta getPregunta() {
		return pregunta;
	}
	public void setPregunta(Pregunta value) {
		pregunta = value;
	}

	@ManyToOne
	@JoinColumn(name="uid", referencedColumnName="uid")
	public Usuario getAutor() {
		return user;
	}
	public void setAutor(Usuario value) {
		user = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	@Column(name="coment")
	public String getComentario() {
		return coment;
	}
	public void setComentario(String value) {
		coment = value;
	}

	@Override
	public int hashCode() {
		return cid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ComentPregunta)) {
			return false;
		}
		return ((ComentPregunta)obj).getCid() == cid;
	}

}
