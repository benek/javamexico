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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.javamexico.entity.Usuario;

/** Representa un foro, creado por un usuario,
 * con un tema, titulo, texto y comentarios.
 * 
 * @author Enrique Zamudio
 */
@Entity
public class Foro {

	private int fid;
	private int status;
	private TemaForo tema;
	private Usuario creador;
	private Set<TagForo> tags;
	private Date fecha;
	private String titulo;
	private String texto;
	private int votos;

	@Id
	@SequenceGenerator(name="pk", sequenceName="foro_fid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getFid() {
		return fid;
	}
	public void setFid(int value) {
		fid = value;
	}

	@ManyToOne
	@JoinColumn(name="tid")
	public TemaForo getTema() {
		return tema;
	}
	public void setTema(TemaForo value) {
		tema = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getAutor() {
		return creador;
	}
	public void setAutor(Usuario value) {
		creador = value;
	}

	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="tag_foro_join",
			joinColumns=@JoinColumn(name="fid"),
			inverseJoinColumns=@JoinColumn(name="tid"))
	public Set<TagForo> getTags() {
		return tags;
	}
	public void setTags(Set<TagForo> value) {
		tags = value;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int value) {
		status = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Formula("((select count(*) from voto_foro vr where vr.fid=fid and vr.up)-(select count(*) from voto_foro vr where vr.fid=fid and not vr.up))")
	public int getVotos() {
		return votos;
	}
	public void setVotos(int value) {
		votos = value;
	}

	@Override
	public int hashCode() {
		return fid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Foro) {
			return ((Foro)obj).getFid() == fid;
		}
		return false;
	}

}
