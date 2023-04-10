package mx.com.ferbo.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "emisor")
@NamedQueries({
@NamedQuery(name = "EmisoresCFDIS.findAll", query ="SELECT e FROM EmisoresCFDIS e"),
@NamedQuery(name = "EmisoresCFDIS.findBycdEmisor", query = "SELECT e FROM EmisoresCFDIS e WHERE e.cd_emisor = :cd_emisor"),
@NamedQuery(name = "EmisoresCFDIS.findBynbEmisor", query = "SELECT e FROM EmisoresCFDIS e WHERE e.nb_emisor = :nb_emisor"),
@NamedQuery(name = "EmisoresCFDIS.findBytpPersona", query = "SELECT e FROM EmisoresCFDIS e WHERE e.tp_persona = :tp_persona"),
@NamedQuery(name = "EmisoresCFDIS.findByregimenCapital", query = "SELECT e FROM EmisoresCFDIS e WHERE e.nb_regimen_capital = :nb_regimen_capital"),
@NamedQuery(name = "EmisoresCFDIS.findByrRFC", query = "SELECT e FROM EmisoresCFDIS e WHERE e.nb_rfc = :nb_rfc"),
@NamedQuery(name = "EmisoresCFDIS.findByIniOperaciones", query = "SELECT e FROM EmisoresCFDIS e WHERE e.fh_inicio_op = :fh_inicio_op"),
@NamedQuery(name = "EmisoresCFDIS.findByultimoCambio", query = "SELECT e FROM EmisoresCFDIS e WHERE e.fh_ult_cambio = :ft_ult_cambio"),
@NamedQuery(name = "EmisoresCFDIS.findByPadron", query = "SELECT e FROM EmisoresCFDIS e WHERE e.st_padron = :st_padron"),
@NamedQuery(name = "EmisoresCFDIS.findByuuid", query ="SELECT e FROM EmisoresCFDIS e WHERE e.uuid = :uuid"),
@NamedQuery(name = "EmisoresCFDIS.findByregimenFiscal", query ="SELECT e FROM EmisoresCFDIS e WHERE e.cd_regimen = :cd_regimen")})
public class EmisoresCFDIS implements Serializable {

private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Basic(optional = false)
		@Column(name = "cd_emisor")
		private Integer cd_emisor;
		
		@Size(min = 1, max = 150)
		@Column(name = "nb_emisor")
		private String nb_emisor;
		
		@Size(min = 1, max = 1)
		@Column(name ="tp_persona")
		private String tp_persona;
		
		@Size(min = 0, max = 150)
		@Column(name = "nb_regimen_capital")
		private String nb_regimen_capital;
		
		@Size(min= 1, max = 20)
		@Column(name = "nb_rfc")
		private String nb_rfc;
		
		@Column(name = "fh_inicio_op")
		@Temporal(TemporalType.DATE)
		private Date fh_inicio_op;
		
		@Column(name = "fh_ult_cambio")
		@Temporal(TemporalType.DATE)
		private Date fh_ult_cambio;
		
		@Size(min = 0, max = 1)
		@Column(name = "st_padron")
		private String st_padron;
		
		@JoinColumn(name = "cd_regimen")
		 @ManyToOne
		 private RegimenFiscal cd_regimen;
		
		@Column(name = "uuid")
		@Size(min = 1, max = 50)
		private String uuid = null;
		
		//mappedBy = "<atributo>": el "atributo" debe coincidir con el nombre "java" del atributo al que se está mapeando.
		@OneToMany(cascade = CascadeType.ALL, mappedBy = "emisor")
		private List<Certificado> listaCertificado;
		
		public EmisoresCFDIS() {
		}
		
		public Integer getCd_emisor() {
		return cd_emisor;
		}
		
		public void setCd_emisor(Integer cd_emisor) {
		this.cd_emisor = cd_emisor;
		}
		
		public String getNb_emisor() {
			return nb_emisor;
		}
		
		public void setNb_emisor(String nb_emisor) {
			this.nb_emisor = nb_emisor;
		}
		
		public String getTp_persona() {
		return tp_persona;
		}
		
		public void setTp_persona(String tp_persona) {
		this.tp_persona = tp_persona;
		}
		
		public String getNb_regimen_capital() {
		return nb_regimen_capital;
		}
		
		public void setNb_regimen_capital(String nb_regimen_capital) {
		this.nb_regimen_capital = nb_regimen_capital;
		}
		
		public String getNb_rfc() {
		return nb_rfc;
		}
		
		public void setNb_rfc(String nb_rfc) {
		this.nb_rfc = nb_rfc;
		}
		
		public Date getFh_inicio_op() {
		return fh_inicio_op;
		}
		
		public void setFh_inicio_op(Date fh_inicio_op) {
		this.fh_inicio_op = fh_inicio_op;
		}
		
		public Date getFh_ult_cambio() {
		return fh_ult_cambio;
		}
		
		public void setFh_ult_cambio(Date fh_ult_cambio) {
		this.fh_ult_cambio = fh_ult_cambio;
		}
		
		public String getSt_padron() {
		return st_padron;
		}
		
		public void setSt_padron(String st_padron) {
		this.st_padron = st_padron;
		}
		
		public RegimenFiscal getCd_regimen() {
		return cd_regimen;
		}
		
		public void setCd_regimen(RegimenFiscal cd_regimen) {
		this.cd_regimen = cd_regimen;
		}
		
		
		public String getUuid() {
		return uuid;
		}
		
		public void setUuid(String uuid) {
		this.uuid = uuid;
		}
		
		
		public List<Certificado> getListaCertificado() {
			return listaCertificado;
		}

		public void setListaCertificado(List<Certificado> listaCertificado) {
			this.listaCertificado = listaCertificado;
		}

		@Override
		public int hashCode() {
			return Objects.hash(cd_emisor, cd_regimen, fh_inicio_op, fh_ult_cambio, listaCertificado, nb_emisor,
					nb_regimen_capital, nb_rfc, st_padron, tp_persona, uuid);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EmisoresCFDIS other = (EmisoresCFDIS) obj;
			return Objects.equals(cd_emisor, other.cd_emisor) && Objects.equals(cd_regimen, other.cd_regimen)
					&& Objects.equals(fh_inicio_op, other.fh_inicio_op)
					&& Objects.equals(fh_ult_cambio, other.fh_ult_cambio)
					&& Objects.equals(listaCertificado, other.listaCertificado)
					&& Objects.equals(nb_emisor, other.nb_emisor)
					&& Objects.equals(nb_regimen_capital, other.nb_regimen_capital)
					&& Objects.equals(nb_rfc, other.nb_rfc) && Objects.equals(st_padron, other.st_padron)
					&& Objects.equals(tp_persona, other.tp_persona) && Objects.equals(uuid, other.uuid);
		}

		@Override
		public String toString() {
			return "EmisoresCFDIS [cd_emisor=" + cd_emisor + ", nb_emisor=" + nb_emisor + ", tp_persona=" + tp_persona
					+ ", nb_regimen_capital=" + nb_regimen_capital + ", nb_rfc=" + nb_rfc + ", fh_inicio_op="
					+ fh_inicio_op + ", fh_ult_cambio=" + fh_ult_cambio + ", st_padron=" + st_padron + ", cd_regimen="
					+ cd_regimen + ", uuid=" + uuid + ", listaCertificado=" + listaCertificado + "]";
		}

		public EmisoresCFDIS(Integer cd_emisor, String nb_emisor,String tp_persona, String nb_regimen_capital, String nb_rfc, Date fh_inicio_op, Date fh_ult_cambio,
				String st_padron, RegimenFiscal cd_regimen, String uuid, List<Certificado> listaCertificado) {
			this.cd_emisor = cd_emisor;
			this.nb_emisor = nb_emisor;
			this.tp_persona = tp_persona;
			this.nb_regimen_capital = nb_regimen_capital;
			this.nb_rfc = nb_rfc;
			this.fh_inicio_op = fh_inicio_op;
			this.fh_ult_cambio = fh_ult_cambio;
			this.st_padron = st_padron;
			this.cd_regimen = cd_regimen;
			this.uuid = uuid;
			this.listaCertificado = listaCertificado;
		}

				
}
		

