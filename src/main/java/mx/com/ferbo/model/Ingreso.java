package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ingreso")
@NamedQueries({
		@NamedQuery(name = "Ingreso.findAll", query = "SELECT i FROM Ingreso i"), 
		@NamedQuery(name = "Ingreso.findById", query = "SELECT i FROM Ingreso i WHERE i.idIngreso = :idIngreso")})

public class Ingreso implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_ingreso")
	private Integer idIngreso;
	
	@Column(name = "folio")
	@NotNull
	@Size(min = 1, max = 8)
	private String folio;
	
	@Column(name = "fecha_hora")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;
	
	@JoinColumn(name = "id_cliente", referencedColumnName = "CTE_CVE")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Cliente idCliente;
	
	@Column(name = "transportista")
	@Size(min = 1, max = 100)
	private String transportista;
	
	@Column(name = "placas")
	@Size(min = 1, max = 100)
	private String placas;
	
	@Column(name = "observaciones")
	@Size(min = 1, max = 100)
	private String observaciones;
	
	@JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
	@ManyToOne(fetch = FetchType.LAZY)
	private Contacto idContacto;
	
	@JoinColumn(name = "status", referencedColumnName = "id_status")
	@ManyToOne(fetch = FetchType.LAZY)
	private IngresoStatus ingresoStatus;
	
	
	
	public Ingreso() {
		
	}

	public Integer getIdIngreso() {
		return idIngreso;
	}

	public void setIdIngreso(Integer idIngreso) {
		this.idIngreso = idIngreso;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Cliente getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Cliente idCliente) {
		this.idCliente = idCliente;
	}

	public String getTransportista() {
		return transportista;
	}

	public void setTransportista(String transportista) {
		this.transportista = transportista;
	}

	public String getPlacas() {
		return placas;
	}

	public void setPlacas(String placas) {
		this.placas = placas;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Contacto getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(Contacto idContacto) {
		this.idContacto = idContacto;
	}

	public IngresoStatus getIngresoStatus() {
		return ingresoStatus;
	}

	public void setIngresoStatus(IngresoStatus ingresoStatus) {
		this.ingresoStatus = ingresoStatus;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idIngreso != null ? idIngreso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingreso)) {
            return false;
        }
        Ingreso other = (Ingreso) object;
        if ((this.idIngreso == null && other.idIngreso != null) || (this.idIngreso != null && !this.idIngreso.equals(other.idIngreso))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "Ingreso [idIngreso=" + idIngreso + ", folio=" + folio + ", fechaHora=" + fechaHora + ", idCliente="
				+ idCliente + ", transportista=" + transportista + ", placas=" + placas + ", observaciones="
				+ observaciones + ", idContacto=" + idContacto + ", ingresoStatus=" + ingresoStatus + "]";
	}
    
    
	
}
