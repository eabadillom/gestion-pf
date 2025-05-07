package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;


@Entity
@Table(name = "uso_cfdi")
@NamedQueries({
    @NamedQuery(name = "UsoCfdi.findByPersonaFisica", query = "SELECT u FROM UsoCfdi u WHERE u.aplicaPersonaFisica = true "),
    @NamedQuery(name = "UsoCfdi.findByPersonaMoral", query = "SELECT u FROM  UsoCfdi u WHERE u.aplicaPersonaMoral = true "),
	@NamedQuery(name = "UsoCfdi.findAll", query = "SELECT u FROM UsoCfdi u ORDER BY u.usoCfdi")
})
public class UsoCfdi implements Serializable {
	
	private static final long serialVersionUID = 2460444038539852730L;
	
	@Id
	@Column(name = "cd_uso_cfdi")
	private String cdUsoCfdi;
	
	@Column(name = "nb_uso_cfdi")
	@Size(min=1, max=150)
	private String usoCfdi;
	
	@Column(name="st_apl_per_fisica")
	private Boolean aplicaPersonaFisica;
	
	@Column(name="st_apl_per_moral")
	private Boolean aplicaPersonaMoral;
	
	@Column(name="fh_vigencia_ini")
	@Temporal(TemporalType.DATE)
	private Date vigenciaInicio;
	
	@Column(name="fh_vigencia_fin")
	@Temporal(TemporalType.DATE)
	private Date vigenciaFin;

	public String getCdUsoCfdi() {
		return cdUsoCfdi;
	}

	public void setCdUsoCfdi(String cdUsoCfdi) {
		this.cdUsoCfdi = cdUsoCfdi;
	}

	public String getUsoCfdi() {
		return usoCfdi;
	}

	public void setUsoCfdi(String usoCfdi) {
		this.usoCfdi = usoCfdi;
	}

	public Boolean getAplicaPersonaFisica() {
		return aplicaPersonaFisica;
	}

	public void setAplicaPersonaFisica(Boolean aplicaPersonaFisica) {
		this.aplicaPersonaFisica = aplicaPersonaFisica;
	}

	public Boolean getAplicaPersonaMoral() {
		return aplicaPersonaMoral;
	}

	public void setAplicaPersonaMoral(Boolean aplicaPersonaMoral) {
		this.aplicaPersonaMoral = aplicaPersonaMoral;
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

	@Override
	public int hashCode() {
		return Objects.hash(cdUsoCfdi);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsoCfdi other = (UsoCfdi) obj;
		return Objects.equals(cdUsoCfdi, other.cdUsoCfdi);
	}

	@Override
	public String toString() {
		return "UsoCfdi [cdUsoCfdi=" + cdUsoCfdi + ", usoCfdi=" + usoCfdi + ", aplicaPersonaFisica="
				+ aplicaPersonaFisica + ", aplicaPersonaMoral=" + aplicaPersonaMoral + ", vigenciaInicio="
				+ vigenciaInicio + ", vigenciaFin=" + vigenciaFin + "]";
	}
}
