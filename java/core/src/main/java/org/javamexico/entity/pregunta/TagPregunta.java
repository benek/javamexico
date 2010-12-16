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

import java.util.Comparator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Formula;

/** Representa un tag que el autor de una pregunta le pone, para indicar los temas
 * relacionados.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="tag_pregunta")
public class TagPregunta implements Comparable<TagPregunta> {

	private int tid;
	private int count;
	private String tag;
	private Set<Pregunta> qus;

	@Id
	@SequenceGenerator(name="pk", sequenceName="tag_pregunta_tid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getTid() {
		return tid;
	}
	public void setTid(int value) {
		tid = value;
	}

	@Formula("(select count(*) from tag_pregunta_join j where j.tid=tid)")
	public int getCount() {
		return count;
	}
	public void setCount(int value) {
		count = value;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String value) {
		tag = value;
	}

	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinTable(name="tag_pregunta_join",
			joinColumns=@JoinColumn(name="tid"),
			inverseJoinColumns=@JoinColumn(name="pid"))
	public Set<Pregunta> getPreguntas() {
		return qus;
	}
	public void setPreguntas(Set<Pregunta> value) {
		qus = value;
	}

	/** Compara dos tags por su nombre. */
	public int compareTo(TagPregunta o) {
		if (o == null) {
			return 1;
		}
		if (tag == null) {
			return o.getTag() == null ? 0 : -1;
		}
		return tag.compareTo(o.getTag());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TagPregunta)) {
			return false;
		}
		return ((TagPregunta)obj).getTid() == tid;
	}

	@Override
	public int hashCode() {
		return tid;
	}

	@Override
	public String toString() {
		return String.format("Tag:%s", tag);
	}

	/** Este comparador evalua la cuenta de cada tag en vez del nombre.
	 * 
	 * @author Enrique Zamudio
	 */
	public static class CountComparator implements Comparator<TagPregunta> {

		public int compare(TagPregunta o1, TagPregunta o2) {
			if (o1 == null) {
				return o2 == null ? 0 : 1;
			} else if (o2 == null) {
				return -1;
			} else {
				if (o1.getCount() == o2.getCount()) {
					return 0;
				}
				return o1.getCount() > o2.getCount() ? -1 : 1;
			}
		}
		
	}

}
