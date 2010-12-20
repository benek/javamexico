package org.javamexico.entity.bolsa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/** Representa una empresa, que puede registrar anuncios, avisos y ofertas de trabajo.
 * 
 * @author Enrique Zamudio
 */
@Entity(name="chamba_empresa")
public class Empresa {

	private int eid;
	private int status;
	private Date fechaAlta;
	private String nombre;
	private String password;
	private String nombreContacto;
	private String emailContacto;
	private String fon1;
	private String fon2;
	private String url;

	@Id
	@SequenceGenerator(name="pk", sequenceName="chamba_empresa_eid_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="pk")
	public int getEid() {
		return eid;
	}
	public void setEid(int eid) {
		this.eid = eid;
	}

	@Min(0) @Max(10)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Size(min=6, max=80)
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name="passwd")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="nom_contacto", length=80)
	@Size(min=6, max=80)
	public String getNombreContacto() {
		return nombreContacto;
	}
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}

	@Column(name="mail_contacto", length=80)
	@Pattern(regexp="[\\w\\._%-]+@\\w+(\\.\\w+)+")
	@Size(max=80)
	public String getMailContacto() {
		return emailContacto;
	}
	public void setMailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}

	//@Pattern(regexp="^(\\+\\d)*\\s*(\\(\\d{3}\\)\\s*)*\\d{3}(-{0,1}|\\s{0,1})\\d{2}(-{0,1}|\\s{0,1})\\d{2}$")
	@Pattern(regexp="\\d{10}")
	public String getTelefono1() {
		return fon1;
	}
	public void setTelefono1(String fon1) {
		this.fon1 = fon1;
	}

	@Pattern(regexp="\\d{10}")
	public String getTelefono2() {
		return fon2;
	}
	public void setTelefono2(String fon2) {
		this.fon2 = fon2;
	}

	@Size(min=4, max=1024)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
