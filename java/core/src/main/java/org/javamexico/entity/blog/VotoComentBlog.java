package org.javamexico.entity.blog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.javamexico.entity.Usuario;

@Entity(name="voto_blog_coment")
public class VotoComentBlog {

	private int vid;
	private boolean up;
	private Date fecha;
	private Usuario user;
	private BlogComent coment;

	@Id
	@SequenceGenerator(name="pk", sequenceName="boto_blog_vid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getVid() {
		return vid;
	}
	public void setVid(int vid) {
		this.vid = vid;
	}

	public boolean isUp() {
		return up;
	}
	public void setUp(boolean up) {
		this.up = up;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}

	@ManyToOne
	@JoinColumn(name="cid")
	public BlogComent getComent() {
		return coment;
	}
	public void setComent(BlogComent value) {
		coment = value;
	}

}
