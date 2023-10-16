package mx.com.ferbo.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;


@Entity
@Table(name = "pre_salida")
@NamedQueries({
    @NamedQuery(name = "OrdenSalida.findAll", query = "SELECT os FROM OrdenSalida os ORDER BY os.idPreSalida"),
    @NamedQuery(name = "OrdenSalida.findByidPreSalida",  query = "SELECT os FROM OrdenSalida os WHERE os.idPreSalida = :idPreSalida"),
    @NamedQuery(name = "OrdenSalida.findByFolioSalida", query = "SELECT os FROM OrdenSalida  os  WHERE os.FolioSalida = :FolioSalida"),
    @NamedQuery(name = "OrdenSalida.findByfechaSalida",query = "SELECT os FROM OrdenSalida  os  WHERE os.fechaSalida = :fechaSalida AND os.stEstado ='A' "),
    @NamedQuery(name = "OrdenSalida.findBynombrePlacas",query = "SELECT os FROM OrdenSalida  os  WHERE os.nombrePlacas = :nombrePlacas"),
    @NamedQuery(name = "OrdenSalida.findBynombreOperador", query = "SELECT os FROM OrdenSalida  os  WHERE os.nombreOperador = :nombreOperador"),
    @NamedQuery(name = "OrdenSalida.findBypartidaClave", query = "SELECT os FROM OrdenSalida  os  WHERE os.partidaClave = :partidaClave "),
    @NamedQuery(name = "OrdenSalida.findByCliente", query = "SELECT os FROM OrdenSalida  os  WHERE os.partidaClave = :partidaClave "),
    @NamedQuery(name = "OrdenSalida.findByfolio", query = "SELECT os FROM OrdenSalida  os  WHERE os.folio = :folio")})

/* SELECT
distinct cd_folio_salida, fh_salida, tm_salida, nb_placa_tte, nb_operador_tte
FROM pre_salida ps
INNER JOIN PARTIDA p ON ps.partida_cve = p.PARTIDA_CVE
INNER JOIN CONSTANCIA_DE_DEPOSITO cdd ON p.FOLIO = cdd.FOLIO
WHERE ps.st_estado = 'A' AND ps.fh_salida = $P{fecha} AND cdd.CTE_CVE = $P{idCliente} */
public class OrdenSalida implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_pre_salida")
	private Integer idPreSalida;
	
	@Size(max = 10)
	@Column(name ="cd_folio_salida")
	private String FolioSalida;
	
	@Size(max = 1)
	@Column(name ="st_estado")
	private String stEstado;
	
	@Size(max = 23)
	@Column(name ="fh_salida")
	private Date fechaSalida;
	
	@Size(max = 23)
	@Column(name ="tm_salida")
	private Time horaSalida;
	
	@Size(max = 8)
	@Column(name ="nb_placa_tte")
	private String nombrePlacas;
	
	@Size(max = 10)
	@Column(name ="nb_operador_tte")
	private String nombreOperador;
	
	@Size(max = 10)
	@Column(name ="partida_cve")
	private Integer partidaClave;
	
	@Size(max = 10)
	@Column(name ="folio")
	private Integer folio;
	
	@Size(max = 10)
	@Column(name ="nu_cantidad")
	private Integer cantidad;
	
	@Size(max = 10)
	@Column(name ="id_contacto")
	private Integer idContacto;

	public OrdenSalida() {

	}

	public OrdenSalida(Integer idPreSalida, String folioSalida, String stEstado,Date fechaSalida,  Time horaSalida, String nombrePlacas,
			String nombreOperador, Integer partidaClave, Integer folio, Integer cantidad, Integer idContacto) {
		super();
		this.idPreSalida = idPreSalida;
		FolioSalida = folioSalida;
		this.stEstado = stEstado;
		this.fechaSalida = fechaSalida;
		this.horaSalida = horaSalida;
		this.nombrePlacas = nombrePlacas;
		this.nombreOperador = nombreOperador;
		this.partidaClave = partidaClave;
		this.folio = folio;
		this.cantidad = cantidad;
		this.idContacto = idContacto;
	}

	public Integer getIdPreSalida() {
		return idPreSalida;
	}

	public void setIdPreSalida(Integer idPreSalida) {
		this.idPreSalida = idPreSalida;
	}

	public String getFolioSalida() {
		return FolioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		FolioSalida = folioSalida;
	}

	public String getStEstado() {
		return stEstado;
	}

	public void setStEstado(String stEstado) {
		this.stEstado = stEstado;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public Time getTmSalida() {
		return horaSalida;
	}

	public void setTmSalida(Time horaSalida) {
		this.horaSalida = horaSalida;
	}

	public String getNombrePlacas() {
		return nombrePlacas;
	}

	public void setNombrePlacas(String nombrePlacas) {
		this.nombrePlacas = nombrePlacas;
	}

	public String getNombreOperador() {
		return nombreOperador;
	}

	public void setNombreOperador(String nombreOperador) {
		this.nombreOperador = nombreOperador;
	}

	public Integer getPartidaClave() {
		return partidaClave;
	}

	public void setPartidaClave(Integer partidaClave) {
		this.partidaClave = partidaClave;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Integer idContacto) {
		this.idContacto = idContacto;
	}

	@Override
	public int hashCode() {
	    int hash = 0;
        hash += (idPreSalida != null ? idPreSalida.hashCode() : 0);
        return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrdenSalida other = (OrdenSalida) obj;
		return Objects.equals(idPreSalida, other.idPreSalida);
	}

	@Override
	public String toString() {
		return "OrdenSalida [idPreSalida=" + idPreSalida + ", FolioSalida=" + FolioSalida + ", stEstado=" + stEstado
				+ ", fechaSalida=" + fechaSalida + ", horaSalida=" + horaSalida + ", nombrePlacas=" + nombrePlacas
				+ ", nombreOperador=" + nombreOperador + ", partidaClave=" + partidaClave + ", folio=" + folio
				+ ", cantidad=" + cantidad + ", idContacto=" + idContacto + "]";
	}
	
	
}
