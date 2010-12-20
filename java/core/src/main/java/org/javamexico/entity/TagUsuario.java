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
package org.javamexico.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Formula;

/** Representa un tag que se le puede poner a los usuarios.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="tag_usuario")
public class TagUsuario {

	private int tid;
	private int count;
	private String tag;

	@Id
	@SequenceGenerator(name="pk", sequenceName="tag_usuario_tid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getTid() {
		return tid;
	}
	public void setTid(int value) {
		tid = value;
	}

	@Formula("(select count(*) from tag_usuario_join j where j.tid=tid)")
	public int getCount() {
		return count;
	}
	public void setCount(int value) {
		count = value;
	}

	@Size(min=3, max=40)
	public String getTag() {
		return tag;
	}
	public void setTag(String value) {
		tag = value;
	}

	public String toString() {
		return String.format("Tag:%s", tag);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TagUsuario)) {
			return false;
		}
		return ((TagUsuario)obj).getTid() == tid;
	}

	@Override
	public int hashCode() {
		return tid;
	}

}
