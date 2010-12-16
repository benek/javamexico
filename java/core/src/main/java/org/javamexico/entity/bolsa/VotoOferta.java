package org.javamexico.entity.bolsa;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.javamexico.entity.Usuario;

@Entity(name="chamba_voto_oferta")
public class VotoOferta {

	private int vid;
	private Oferta oferta;
	private Usuario usuario;
	private Date fecha;
	private boolean up;

	@Id
	@SequenceGenerator(name="pk", sequenceName="chamba_voto_oferta_vid_seq", allocationSize=1)
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
	public void setUp(boolean flag) {
		up = flag;
	}

	@ManyToOne
	@JoinColumn(name="ofid")
	public Oferta getOferta() {
		return oferta;
	}
	public void setOferta(Oferta oferta) {
		this.oferta = oferta;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date value) {
		fecha = value;
	}

	@ManyToOne
	@JoinColumn(name="uid")
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
