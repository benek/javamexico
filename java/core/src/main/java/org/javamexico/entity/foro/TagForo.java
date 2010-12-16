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

/** Representa un tag que puede ponerse a varios
 * foros, para indicar los temas que trata.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="tag_foro")
public class TagForo {

	private int tid;
	private int count;
	private String tag;
	private Set<Foro> foros;

	@Id
	@SequenceGenerator(name="pk", sequenceName="tag_foro_tid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getTid() {
		return tid;
	}
	public void setTid(int value) {
		tid = value;
	}

	@Formula("(select count(*) from tag_foro_join j where j.tid=tid)")
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TagForo)) {
			return false;
		}
		return ((TagForo)obj).getTid() == tid;
	}

	@Override
	public int hashCode() {
		return tid;
	}

	@Override
	public String toString() {
		return String.format("Tag:%s", tag);
	}

	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinTable(name="tag_foro_join",
			joinColumns=@JoinColumn(name="tid"),
			inverseJoinColumns=@JoinColumn(name="fid"))
	public Set<Foro> getForos() {
		return foros;
	}
	public void setForos(Set<Foro> value) {
		foros = value;
	}

	/** Este comparador ordena los tags de foros segun el numero de foros que los usan.
	 * 
	 * @author Enrique Zamudio
	 */
	public class CountComparator implements Comparator<TagForo> {

		public int compare(TagForo o1, TagForo o2) {
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
