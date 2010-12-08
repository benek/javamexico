package org.javamexico.entity.bolsa;

import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

/** Representa una oferta de trabajo, registrada por una empresa.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="chamba_oferta")
public class Oferta {

	private int ofid;
	private int status;
	private Empresa empresa;
	private Date fechaAlta;
	private Date fechaExpira;
	private String titulo;
	private String resumen;
	private String desc;
	private String nombreContacto;
	private String mailContacto;
	private String fon1;
	private String fon2;
	private Set<Tag> tags;
    int votos;

	@Id
	@SequenceGenerator(name="pk", sequenceName="chamba_oferta_ofid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getOfid() {
		return ofid;
	}
	public void setOfid(int ofid) {
		this.ofid = ofid;
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@ManyToOne
	@JoinColumn(name="eid")
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_expira")
	public Date getFechaExpira() {
		return fechaExpira;
	}
	public void setFechaExpira(Date fechaExpira) {
		this.fechaExpira = fechaExpira;
	}

	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResumen() {
		return resumen;
	}
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public String getDescripcion() {
		return desc;
	}
	public void setDescripcion(String desc) {
		this.desc = desc;
	}

	@Column(name="contacto")
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	@Column(name="mail_contacto")
	public String getMailContacto() {
		return mailContacto;
	}
	public void setMailContacto(String mailContacto) {
		this.mailContacto = mailContacto;
	}

	public String getTelefono1() {
		return fon1;
	}
	public void setTelefono1(String fon1) {
		this.fon1 = fon1;
	}
	public String getTelefono2() {
		return fon2;
	}
	public void setTelefono2(String fon2) {
		this.fon2 = fon2;
	}

	@ManyToMany(cascade=CascadeType.PERSIST)
	@JoinTable(name="chamba_oferta_tag_join",
			joinColumns=@JoinColumn(name="ofid"),
			inverseJoinColumns=@JoinColumn(name="tid"))
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> value) {
		tags = value;
	}

    @Formula("((select count(*) from chamba_voto_oferta cvo where cvo.ofid=ofid and cvo.up)-(select count(*) from chamba_voto_oferta cvo where cvo.ofid=ofid and not cvo.up))")
    public int getVotos() {
        return votos;
    }
    public void setVotos(int value) {
        votos = value;
    }
}
