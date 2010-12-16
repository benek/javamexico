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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/** Representa un tema para los foros.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="tema_foro")
public class TemaForo {

	private int tid;
	private String tema;
	private String descrip;
	private Date fechaAlta;

	@Id
	@SequenceGenerator(name="pk", sequenceName="tema_foro_tid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getTid() {
		return tid;
	}
	public void setTid(int value) {
		tid = value;
	}

	public String getTema() {
		return tema;
	}
	public void setTema(String value) {
		tema = value;
	}

	public String getDescripcion() {
		return descrip;
	}
	public void setDescripcion(String value) {
		descrip = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date value) {
		fechaAlta = value;
	}

}
