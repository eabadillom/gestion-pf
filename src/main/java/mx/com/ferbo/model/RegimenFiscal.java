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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table(name = "regimen_fiscal")
@NamedQueries({
	@NamedQuery(name = "RegimenFiscal.findAll", query = "SELECT r FROM  RegimenFiscal r"),
	@NamedQuery(name = "RegimenFiscal.findBycdRegimen", query = "SELECT r FROM RegimenFiscal r WHERE  r.cd_regimen = :cd_regimen"),
	@NamedQuery(name = "RegimenFiscal.findBynb_regimen", query = "SELECT r FROM RegimenFiscal r WHERE  r.nb_regimen = :nbr_regimen" ),
	@NamedQuery(name = "RegimenFiscal.findByst_per_fisica", query = "SELECT r FROM RegimenFiscal r WHERE r.personaFisica = :st_per_fisica" ),
	@NamedQuery(name = "RegimenFiscal.findByst_per_moral", query = "SELECT r FROM RegimenFiscal r WHERE r.personaMoral = :st_per_moral" ),
	@NamedQuery(name = "RegimenFiscal.findByfh_vigencia_ini", query = "SELECT r FROM RegimenFiscal r WHERE  r.vigenciaInicio = :fh_vigencia_ini" ),
	@NamedQuery(name = "RegimenFiscal.findByfh_vigencia_fin", query = "SELECT r FROM RegimenFiscal r WHERE r.vigenciaFin = :fh_vigencia_fin")
})
public class RegimenFiscal implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Size(max=5)
	@Basic(optional = false)
	@Column(name = "cd_regimen")
	private String cd_regimen;
	
	@Size(min = 1, max = 255)
	@Column (name = "nb_regimen")
	private String nb_regimen;
	
	@Basic(optional = false)
    @NotNull
	@Column(name = "st_per_fisica")
	private boolean personaFisica;
	
	@Basic(optional = false)
    @NotNull
	@Column(name = "st_per_moral")
	private boolean personaMoral;
	
	
	@Column(name = "fh_vigencia_ini")
	@Temporal(TemporalType.DATE)
	private Date vigenciaInicio;
	
	@Column(name = "fh_vigencia_fin")
	@Temporal(TemporalType.DATE)
	private Date vigenciaFin;

	public RegimenFiscal() {
	}

	public String getCd_regimen() {
		return cd_regimen;
	}

	public void setCd_regimen(String cd_regimen) {
		this.cd_regimen = cd_regimen;
	}

	public String getNb_regimen() {
		return nb_regimen;
	}

	public void setNb_regimen(String nb_regimen) {
		this.nb_regimen = nb_regimen;
	}

	public boolean isPersonaFisica() {
		return personaFisica;
	}

	public void setPersonaFisica(boolean personaFisica) {
		this.personaFisica = personaFisica;
	}

	public boolean isPersonaMoral() {
		return personaMoral;
	}

	public void setPersonaMoral(boolean personaMoral) {
		this.personaMoral = personaMoral;
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
		return Objects.hash(cd_regimen, nb_regimen, personaFisica, personaMoral, vigenciaFin, vigenciaInicio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegimenFiscal other = (RegimenFiscal) obj;
		return Objects.equals(cd_regimen, other.cd_regimen) && Objects.equals(nb_regimen, other.nb_regimen)
				&& personaFisica == other.personaFisica && personaMoral == other.personaMoral
				&& Objects.equals(vigenciaFin, other.vigenciaFin)
				&& Objects.equals(vigenciaInicio, other.vigenciaInicio);
	}

	@Override
	public String toString() {
		return "RegimenFiscal [cd_regimen=" + cd_regimen + ", nb_regimen=" + nb_regimen + ", personaFisica="
				+ personaFisica + ", personaMoral=" + personaMoral + ", vigenciaInicio=" + vigenciaInicio
				+ ", vigenciaFin=" + vigenciaFin + "]";
	}
	
	

	
	

	

	

	

	
	
	
}
