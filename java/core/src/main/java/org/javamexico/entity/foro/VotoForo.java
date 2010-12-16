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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.javamexico.entity.Usuario;

/** Representa un voto de un usuario a un foro hecho por otro usuario.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="voto_foro")
public class VotoForo {

	private int vid;
	private Foro foro;
	private Usuario user;
	private Date fecha;
	private boolean up;

	@Id
	@SequenceGenerator(name="pk", sequenceName="voto_foro_vid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getVid() {
		return vid;
	}
	public void setVid(int value) {
		vid = value;
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
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario value) {
		user = value;
	}

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
