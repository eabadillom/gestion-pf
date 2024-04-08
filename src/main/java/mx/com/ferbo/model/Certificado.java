package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "certificado")
@NamedQueries({ @NamedQuery(name = "Certificado.findAll", query = "SELECT c FROM Certificado c"),
		@NamedQuery(name = "Certificado.findByCd_Certificado", query = "SELECT c FROM Certificado c WHERE c.cdCertificado = :cd_certificado"),
		@NamedQuery(name = "Certificado.findByAlta", query = "SELECT c FROM Certificado c WHERE c.fechaAlta = :fh_alta"),
		@NamedQuery(name = "Certificado.findByNombreCertificado", query = "SELECT c FROM Certificado c WHERE c.nombreCertificado = :nb_certificado"),
		@NamedQuery(name = "Certificado.findByFecha", query = "SELECT max(c.fechaAlta), c.dtCertificado FROM Certificado c GROUP BY c.dtCertificado"),
		@NamedQuery(name = "Certificado.findByemisor", query = "SELECT c FROM Certificado c WHERE c.emisor.cd_emisor = :emisor") })
public class Certificado implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cd_certificado")
	private Integer cdCertificado;

	@Column(name = "fh_alta")
	private Date fechaAlta;

	@Size(min = 1, max = 256)
	@Column(name = "nb_certificado")
	private String nombreCertificado;

	@Size(min = 1, max = 1638)
	@Column(name = "dt_certificado")
	private byte[] dtCertificado;

	@Size(min = 1, max = 256)
	@Column(name = "nb_llave_privada")
	private String llavePrivada;

	@Size(min = 1, max = 1638)
	@Column(name = "dt_llave_privada")
	private byte[] Dt_llavePrivada;

	@Size(min = 1, max = 100)
	@Column(name = "nb_pass")
	private String password;

	@JoinColumn(name = "cd_emisor")
	@ManyToOne
	private EmisoresCFDIS emisor;

	public Certificado() {
	}

	public Integer getCdCertificado() {
		return cdCertificado;
	}

	public void setCdCertificado(Integer cdCertificado) {
		this.cdCertificado = cdCertificado;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getNombreCertificado() {
		return nombreCertificado;
	}

	public void setNombreCertificado(String nombreCertificado) {
		this.nombreCertificado = nombreCertificado;
	}

	public byte[] getCertificado() {
		return dtCertificado;
	}

	public void setCertificado(byte[] certificado) {
		dtCertificado = certificado;
	}

	public String getLlavePrivada() {
		return llavePrivada;
	}

	public void setLlavePrivada(String llavePrivada) {
		this.llavePrivada = llavePrivada;
	}

	public byte[] getDt_llavePrivada() {
		return Dt_llavePrivada;
	}

	public void setDt_llavePrivada(byte[] dt_llavePrivada) {
		Dt_llavePrivada = dt_llavePrivada;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EmisoresCFDIS getEmisor() {
		return emisor;
	}

	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}

	public Certificado(Integer cdCertificado, Date fechaAlta, String nombreCertificado, byte[] certificado,
			String llavePrivada, byte[] dt_llavePrivada, String password, EmisoresCFDIS emisor) {
		this.cdCertificado = cdCertificado;
		this.fechaAlta = fechaAlta;
		this.nombreCertificado = nombreCertificado;
		dtCertificado = certificado;
		this.llavePrivada = llavePrivada;
		Dt_llavePrivada = dt_llavePrivada;
		this.password = password;
		this.emisor = emisor;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dtCertificado, Dt_llavePrivada, cdCertificado, emisor, fechaAlta, llavePrivada,
				nombreCertificado, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Certificado other = (Certificado) obj;
		return Objects.equals(dtCertificado, other.dtCertificado)
				&& Objects.equals(Dt_llavePrivada, other.Dt_llavePrivada)
				&& Objects.equals(cdCertificado, other.cdCertificado) && Objects.equals(emisor, other.emisor)
				&& Objects.equals(fechaAlta, other.fechaAlta) && Objects.equals(llavePrivada, other.llavePrivada)
				&& Objects.equals(nombreCertificado, other.nombreCertificado)
				&& Objects.equals(password, other.password);
	}

}
