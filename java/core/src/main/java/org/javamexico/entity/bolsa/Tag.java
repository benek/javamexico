package org.javamexico.entity.bolsa;

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

/** Los tags que se pueden poner a las ofertas, anuncios, etc.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="chamba_tag")
public class Tag {

	private int tid;
	private int count;
	private String tag;
	private Set<Oferta> ofertas;

	@Id
	@SequenceGenerator(name="pk", sequenceName="chamba_tag_tid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}

	@Formula("(select count(*) from chamba_oferta_tag_join j where j.tid=tid)")
	public int getCount() {
		return count;
	}
	public void setCount(int value) {
		count = value;
	}

	public void setOfertas(Set<Oferta> value) {
		ofertas = value;
	}
	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinTable(name="chamba_oferta_tag_join",
			joinColumns=@JoinColumn(name="tid"),
			inverseJoinColumns=@JoinColumn(name="ofid"))
	public Set<Oferta> getOfertas() {
		return ofertas;
	}

	@Override
	public int hashCode() {
		return tid % 1000;
	}

	public class CountComparator implements Comparator<Tag> {
		public int compare(Tag o1, Tag o2) {
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
