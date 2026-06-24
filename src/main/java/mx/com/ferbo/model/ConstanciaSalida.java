package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.annotation.Generated;
import java.util.Collections;



@Entity
@Table(name = "constancia_salida")
@NamedQueries({

        @NamedQuery(name = "ConstanciaSalida.findAll", query = "SELECT c FROM ConstanciaSalida c"),
        @NamedQuery(name = "ConstanciaSalida.findById", query = "SELECT c FROM ConstanciaSalida c WHERE c.id = :id"),
        @NamedQuery(name = "ConstanciaSalida.findByFecha", query = "SELECT c FROM ConstanciaSalida c WHERE c.fecha = :fecha"),
        @NamedQuery(name = "ConstanciaSalida.findByNumero", query = "SELECT c FROM ConstanciaSalida c WHERE c.numero = :numero"),
        @NamedQuery(name = "ConstanciaSalida.findByNombreCte", query = "SELECT c FROM ConstanciaSalida c WHERE c.nombreCte = :nombreCte"),
        @NamedQuery(name = "ConstanciaSalida.findByStatus", query = "SELECT c FROM ConstanciaSalida c WHERE c.status = :status"),
        @NamedQuery(name = "ConstanciaSalida.findByObservaciones", query = "SELECT c FROM ConstanciaSalida c WHERE c.observaciones = :observaciones"),
        @NamedQuery(name = "ConstanciaSalida.findByFolioDeposito", query = "SELECT cs " +
                "FROM ConstanciaDeDeposito cdd " +
                "INNER JOIN cdd.partidaList p " +
                "INNER JOIN p.detalleConstanciaSalidaList dcs " +
                "INNER JOIN dcs.constanciaCve cs " +
                "WHERE cdd.folio = :folio")
})

public class ConstanciaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NUMERO")
    private String numero;

    @Size(max = 150)
    @Column(name = "NOMBRE_CTE")
    private String nombreCte;

    @JoinColumn(name = "STATUS", referencedColumnName = "ID")
    @ManyToOne
    private StatusConstanciaSalida status;

    @Size(max = 75)
    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre_transportista")
    private String nombreTransportista;

    @NotNull
    @Size(max = 10)
    @Column(name = "placas_transporte")
    private String placasTransporte;

    @NotNull
    @Column(name = "status_termo")
    private Boolean statusTermo;

    @NotNull
    @Column(name = "temp_transporte")
    private BigDecimal temperaturaTransporte;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "constanciaCve")
    private List<DetalleConstanciaSalida> detalleConstanciaSalidaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConstancia")
    private List<ConstanciaSalidaServicios> constanciaSalidaServiciosList;

    @JoinColumn(name = "CLIENTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente clienteCve;
    
	@Generated("SparkTools")
	private ConstanciaSalida(Builder builder) {
		this.id = builder.id;
		this.fecha = builder.fecha;
		this.numero = builder.numero;
		this.nombreCte = builder.nombreCte;
		this.status = builder.status;
		this.observaciones = builder.observaciones;
		this.nombreTransportista = builder.nombreTransportista;
		this.placasTransporte = builder.placasTransporte;
		this.statusTermo = builder.statusTermo;
		this.temperaturaTransporte = builder.temperaturaTransporte;
		this.detalleConstanciaSalidaList = builder.detalleConstanciaSalidaList;
		this.constanciaSalidaServiciosList = builder.constanciaSalidaServiciosList;
		this.clienteCve = builder.clienteCve;
	}
    
    @Override
    public int hashCode() {
        if(this.id == null)
        	return System.identityHashCode(this);
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ConstanciaSalida)) {
            return false;
        }
        ConstanciaSalida other = (ConstanciaSalida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaSalida[ id=" + id + " ]";
    }

    public ConstanciaSalida() {
    }

    public ConstanciaSalida(Integer id) {
        this.id = id;
    }

    public ConstanciaSalida(Integer id, Date fecha, String numero) {
        this.id = id;
        this.fecha = fecha;
        this.numero = numero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNombreCte() {
        return nombreCte;
    }

    public void setNombreCte(String nombreCte) {
        this.nombreCte = nombreCte;
    }

    public StatusConstanciaSalida getStatus() {
        return status;
    }

    public void setStatus(StatusConstanciaSalida status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getPlacasTransporte() {
        return placasTransporte;
    }

    public void setPlacasTransporte(String placasTransporte) {
        this.placasTransporte = placasTransporte;
    }

    public List<DetalleConstanciaSalida> getDetalleConstanciaSalidaList() {
        return detalleConstanciaSalidaList;
    }

    public void setDetalleConstanciaSalidaList(List<DetalleConstanciaSalida> detalleConstanciaSalidaList) {
        this.detalleConstanciaSalidaList = detalleConstanciaSalidaList;
    }

    public List<ConstanciaSalidaServicios> getConstanciaSalidaServiciosList() {
        return constanciaSalidaServiciosList;
    }

    public void setConstanciaSalidaServiciosList(List<ConstanciaSalidaServicios> constanciaSalidaServiciosList) {
        this.constanciaSalidaServiciosList = constanciaSalidaServiciosList;
    }

    public Cliente getClienteCve() {
        return clienteCve;
    }

    public void setClienteCve(Cliente clienteCve) {
        this.clienteCve = clienteCve;
    }

    public Boolean getStatusTermo() {
        return statusTermo;
    }

    public void setStatusTermo(Boolean statusTermo) {
        this.statusTermo = statusTermo;
    }

    public BigDecimal getTemperaturaTransporte() {
        return temperaturaTransporte;
    }

    public void setTemperaturaTransporte(BigDecimal temperaturaTransporte) {
        this.temperaturaTransporte = temperaturaTransporte;
    }

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private Integer id;
		private Date fecha;
		private String numero;
		private String nombreCte;
		private StatusConstanciaSalida status;
		private String observaciones;
		private String nombreTransportista;
		private String placasTransporte;
		private Boolean statusTermo;
		private BigDecimal temperaturaTransporte;
		private List<DetalleConstanciaSalida> detalleConstanciaSalidaList = Collections.emptyList();
		private List<ConstanciaSalidaServicios> constanciaSalidaServiciosList = Collections.emptyList();
		private Cliente clienteCve;

		public Builder() {
		}

		public Builder Id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder fecha(Date fecha) {
			this.fecha = fecha;
			return this;
		}

		public Builder numero(String numero) {
			this.numero = numero;
			return this;
		}

		public Builder nombreCliente(String nombreCliente) {
			this.nombreCte = nombreCliente;
			return this;
		}

		public Builder status(StatusConstanciaSalida status) {
			this.status = status;
			return this;
		}

		public Builder observaciones(String observaciones) {
			this.observaciones = observaciones;
			return this;
		}

		public Builder nombreTransportista(String nombreTransportista) {
			this.nombreTransportista = nombreTransportista;
			return this;
		}

		public Builder placasTransporte(String placasTransporte) {
			this.placasTransporte = placasTransporte;
			return this;
		}

		public Builder statusTermo(Boolean statusTermo) {
			this.statusTermo = statusTermo;
			return this;
		}

		public Builder temperaturaTransporte(BigDecimal temperaturaTransporte) {
			this.temperaturaTransporte = temperaturaTransporte;
			return this;
		}

		public Builder detalleConstanciaSalidaList(List<DetalleConstanciaSalida> detalleConstanciaSalidaList) {
			this.detalleConstanciaSalidaList = detalleConstanciaSalidaList;
			return this;
		}

		public Builder constanciaSalidaServiciosList(List<ConstanciaSalidaServicios> constanciaSalidaServiciosList) {
			this.constanciaSalidaServiciosList = constanciaSalidaServiciosList;
			return this;
		}

		public Builder cliente(Cliente cliente) {
			this.clienteCve = cliente;
			return this;
		}

		public ConstanciaSalida build() {
			return new ConstanciaSalida(this);
		}
	}

}
