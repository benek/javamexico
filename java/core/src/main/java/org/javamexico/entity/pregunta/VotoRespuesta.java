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

/** Una respuesta puede tener votos de los demas usuarios. Es la manera en que la gente puede
 * decir si considera util la respuesta o no.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="voto_respuesta")
public class VotoRespuesta {

	private int vid;
	private Respuesta resp;
	private Usuario user;
	private Date fecha;
	private boolean up;

	@Id
	@SequenceGenerator(name="pk", sequenceName="voto_respuesta_vid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getVid() {
		return vid;
	}
	public void setVid(int value) {
		vid = value;
	}

	@ManyToOne
	@JoinColumn(name="rid", referencedColumnName="rid")
	public Respuesta getRespuesta() {
		return resp;
	}
	public void setRespuesta(Respuesta value) {
		resp = value;
	}

	@ManyToOne
	@JoinColumn(name="uid", referencedColumnName="uid")
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario value) {
		user = value;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	public boolean isUp() {
		return up;
	}
	public void setUp(boolean flag) {
		up = flag;
	}

}
