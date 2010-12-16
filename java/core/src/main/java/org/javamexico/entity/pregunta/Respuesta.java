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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.javamexico.entity.Usuario;

/** Representa una respuesta que un usuario da a una pregunta hecha por otro usuario.
 * 
 * @author Enrique Zamudio
 */
@Entity
public class Respuesta implements Comparable<Respuesta> {

	private int rid;
	private Pregunta pregunta;
	private Usuario user;
	private Date fecha;
	private String respuesta;
	private Set<ComentRespuesta> coments;
	private int votos;

	@Id
	@SequenceGenerator(name="pk", sequenceName="respuesta_rid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getRid() {
		return rid;
	}
	public void setRid(int value) {
		rid = value;
	}

	@ManyToOne
	@JoinColumn(name="pid")
	public Pregunta getPregunta() {
		return pregunta;
	}
	public void setPregunta(Pregunta value) {
		pregunta = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
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

	public String getRespuesta() {
		return respuesta;
	}
	public void setRespuesta(String value) {
		respuesta = value;
	}

	@OneToMany(mappedBy="respuesta", fetch=FetchType.EAGER)
	@OrderBy("fecha")
	public Set<ComentRespuesta> getComentarios() {
		return coments;
	}
	public void setComentarios(Set<ComentRespuesta> value) {
		coments = value;
	}

	@Formula("((select count(*) from voto_respuesta vr where vr.rid=rid and vr.up)-(select count(*) from voto_respuesta vr where vr.rid=rid and not vr.up))")
	public int getVotos() {
		return votos;
	}
	public void setVotos(int value) {
		votos = value;
	}

	public int compareTo(Respuesta o) {
		if (o == null) {
			return 1;
		}
		if (fecha == null) {
			return o.getFecha() == null ? 0 : 1;
		}
		return fecha.compareTo(o.getFecha());
	}

	@Override
	public int hashCode() {
		return rid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Respuesta)) {
			return false;
		}
		return ((Respuesta)obj).getRid() == rid;
	}

}
