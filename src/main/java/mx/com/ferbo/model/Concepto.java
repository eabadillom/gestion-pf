package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "concepto")
@NamedQueries({
	@NamedQuery(name = "Concepto.findAll", query = "SELECT c FROM Concepto c ORDER BY c.nombre"),
	@NamedQuery(name = "Concepto.likeClave", query = "SELECT c FROM Concepto c WHERE c.clave LIKE :clave "),
	@NamedQuery(name = "Concepto.likeNombre", query = "SELECT c FROM Concepto c WHERE c.nombre LIKE :nombre "),
	@NamedQuery(name = "Concepto.likeClaveNombre", query = "SELECT c FROM Concepto c WHERE c.clave like :clave OR c.nombre LIKE :nombre")
})
public class Concepto implements Serializable {

	private static final long serialVersionUID = -4219319323024650558L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "cd_concepto")
	@Size(max = 10)
	private String clave;
	
	@Basic(optional = false)
	@Column(name = "nb_concepto")
	@Size(max = 200)
	private String nombre;
	
	@Basic(optional = false)
	@Column(name = "fh_vigencia_ini")
	private Date vigenciaInicio;
	
	@Basic(optional = true)
	@Column(name = "fh_vigencia_fin")
	private Date vigenciaFin;
	
	@Basic(optional = true)
	@Column(name = "nb_pal_similares")
	@Size(max = 1000)
	private String similares;

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFin() {
		return vigenciaFin;
	}

	public void setVigenciaFin(Date vigenciaFin) {
		this.vigenciaFin = vigenciaFin;
	}

	public String getSimilares() {
		return similares;
	}

	public void setSimilares(String similares) {
		this.similares = similares;
	}

	@Override
	public String toString() {
		return "Concepto [clave=" + clave + ", nombre=" + nombre + ", vigenciaInicio=" + vigenciaInicio
				+ ", vigenciaFin=" + vigenciaFin + ", similares=" + similares + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(clave);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concepto other = (Concepto) obj;
		return Objects.equals(clave, other.clave);
	}
}
