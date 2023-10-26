package mx.com.ferbo.model;

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
@Table(name = "pre_salida_srv")
@NamedQueries({
	@NamedQuery(name = "PreSalidaServicio.findAll", query = "SELECT pss FROM PreSalidaServicio pss"),
	@NamedQuery(name = "PreSalidaServicio.findByfolioSalida", query = "SELECT pss FROM PreSalidaServicio pss WHERE pss.folioSalida = :folioSalida"),
	@NamedQuery(name = "PreSalidaServicio.findByidServicio", query = "SELECT pss FROM PreSalidaServicio pss WHERE pss.idServicio = :idServicio"),
	@NamedQuery(name = "PreSalidaServicio.findByCantidad", query = "SELECT pss FROM PreSalidaServicio pss WHERE pss.cantidad = :cantidad"),
	@NamedQuery(name = "PreSalidaServicio.findByUnidadManejo", query = "SELECT pss FROM PreSalidaServicio pss WHERE pss.idUnidadManejo = :idUnidad")})


public class PreSalidaServicio {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_folio_salida")
    private String folioSalida;	

    //@JoinColumn(name = "SERVICIO_CVE")
    @JoinColumn(name = "id_servicio", referencedColumnName = "SERVICIO_CVE", insertable = false, updatable = false)
    @ManyToOne
    //@Column(name = "id_servicio")
    private Servicio idServicio;
    
    @Column(name = "nu_cantidad ")
    private Integer cantidad;
    
    @Size(max = 100)
    @Column(name = "observacion")
    private String observacion;
    
    @JoinColumn(name = "id_unidad_manejo", referencedColumnName = "UNIDAD_DE_MANEJO_CVE", insertable = false, updatable = false)
    @ManyToOne
   //@Column(name = "id_unidad_manejo")
    private UnidadDeManejo idUnidadManejo;

	public String getFolioSalida() {
		return folioSalida;
	}

	public void setFolioSalida(String folioSalida) {
		this.folioSalida = folioSalida;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public Servicio getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Servicio idServicio) {
		this.idServicio = idServicio;
	}

	public UnidadDeManejo getIdUnidadManejo() {
		return idUnidadManejo;
	}

	public void setIdUnidadManejo(UnidadDeManejo idUnidadManejo) {
		this.idUnidadManejo = idUnidadManejo;
	}

	@Override
	public String toString() {
		return "PreSalidaServicio [folioSalida=" + folioSalida + ", idServicio=" + idServicio + ", cantidad=" + cantidad
				+ ", observacion=" + observacion + ", idUnidadManejo=" + idUnidadManejo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(cantidad, folioSalida, idServicio, idUnidadManejo, observacion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PreSalidaServicio other = (PreSalidaServicio) obj;
		return Objects.equals(cantidad, other.cantidad) && Objects.equals(folioSalida, other.folioSalida)
				&& Objects.equals(idServicio, other.idServicio) && Objects.equals(idUnidadManejo, other.idUnidadManejo)
				&& Objects.equals(observacion, other.observacion);
	}




}











