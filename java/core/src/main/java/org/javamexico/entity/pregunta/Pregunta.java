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

import org.hibernate.annotations.Formula;
import org.javamexico.entity.Usuario;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Representa una pregunta que un usuario hace en el sistema.
 * 
 * @author Enrique Zamudio
 */
@Entity
public class Pregunta implements Comparable<Pregunta> {

	private int pid;
	private int status; //abierta, contestada, etc
	private Integer respId; //cuando ya fue seleccionada una respuesta por el usuario que hizo la pregunta original
	private Usuario autor;
	private String titulo;
	private String pregunta;
	private Date fechaP; //la fecha en que se hizo la pregunta
	private Date fechaR; //la respuesta en que fue seleccionada la respuesta
	private Set<TagPregunta> tags;
	private Set<ComentPregunta> coments;
	private Set<Respuesta> resps;
	private int votos;

	@Id
	@SequenceGenerator(name="pk", sequenceName="pregunta_pid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getPid() {
		return pid;
	}
	public void setPid(int value) {
		pid = value;
	}

	@ManyToOne
	@JoinColumn(name="uid", referencedColumnName="uid")
	public Usuario getAutor() {
		return autor;
	}
	public void setAutor(Usuario value) {
		autor = value;
	}

	@Column(name="fecha_p")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaPregunta() {
		return fechaP;
	}
	public void setFechaPregunta(Date value) {
		fechaP = value;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int value) {
		status = value;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String value) {
		titulo = value;
	}

	public String getPregunta() {
		return pregunta;
	}
	public void setPregunta(String value) {
		pregunta = value;
	}

	/** La fecha en que la respuesta fue elegida por el usuario que hizo la pregunta. */
	@Column(name="fecha_r")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaRespuesta() {
		return fechaR;
	}
	public void setFechaRespuesta(Date value) {
		fechaR = value;
	}

	@Column(name="resp_sel")
	public Integer getRespuestaElegida() {
		return respId;
	}
	public void setRespuestaElegida(Integer value) {
		respId = value;
	}

	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="tag_pregunta_join",
			joinColumns=@JoinColumn(name="pid"),
			inverseJoinColumns=@JoinColumn(name="tid"))
	public Set<TagPregunta> getTags() {
		return tags;
	}
	public void setTags(Set<TagPregunta> value) {
		tags = value;
	}

	@OneToMany(mappedBy="pregunta")
	@OrderBy("fecha DESC")
	public Set<Respuesta> getRespuestas() {
		return resps;
	}
	public void setRespuestas(Set<Respuesta> value) {
		resps = value;
	}

	@OneToMany(mappedBy="pregunta")
	@OrderBy("fecha")
	public Set<ComentPregunta> getComentarios() {
		return coments;
	}
	public void setComentarios(Set<ComentPregunta> value) {
		coments = value;
	}

	@Formula("((select count(*) from voto_pregunta vp where vp.pid=pid and vp.up)-(select count(*) from voto_pregunta vp where vp.pid=pid and not vp.up))")
	public int getVotos() {
		return votos;
	}
	public void setVotos(int value) {
		votos = value;
	}

	public int compareTo(Pregunta o) {
		if (o == null) {
			return 1;
		}
		return o.getPid() > pid ? -1 : o.getPid() < pid ? 1 : 0;
	}

	@Override
	public int hashCode() {
		return pid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pregunta)) {
			return false;
		}
		return ((Pregunta)obj).getPid() == pid;
	}

}
