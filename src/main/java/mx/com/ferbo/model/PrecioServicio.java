package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "precio_servicio")
@NamedQuery(name = "PrecioServicio.findAll", query = "SELECT p FROM PrecioServicio p")
@NamedQuery(name = "PrecioServicio.findByAvisoAndCliente", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve and p.avisoCve.avisoCve = :avisoCve ORDER BY p.servicio.servicioDs ASC")
@NamedQuery(name = "PrecioServicio.findById", query = "SELECT p FROM PrecioServicio p WHERE p.id = :id")
@NamedQuery(name = "PrecioServicio.findByCliente", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve")
@NamedQuery(name = "PrecioServicio.findByClienteServicio", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve and p.servicio.servicioCve = :servicioCve")
@NamedQuery(name = "PrecioServicio.findByClienteAvisoServicio", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve and p.avisoCve.avisoCve = :avisoCve and p.servicio.servicioCve = :servicioCve")
@NamedQuery(name = "PrecioServicio.getMaxPrecioServicioByIdServicio", query = "SELECT p.servicio, MIN(p.precio) AS precio FROM PrecioServicio p WHERE p.servicio = :idServicio")
@NamedQuery(name = "PrecioServicio.findByPrecio", query = "SELECT p FROM PrecioServicio p WHERE p.precio = :precio")
@NamedQuery(name = "PrecioServicio.findByClienteAviso", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve and p.avisoCve.avisoCve = :avisoCve")
@NamedQuery(name = "PrecioServicio.findByServicioAndAvisoAndCliente", query = "SELECT p FROM PrecioServicio p WHERE p.cliente.cteCve = :cteCve and p.avisoCve.avisoCve = :avisoCve and p.servicio.servicioCve = :servicioCve")
@NamedQuery(name = "PrecioServicio.findByAviso", query = "SELECT p FROM PrecioServicio p WHERE p.avisoCve.avisoCve = :idAviso ORDER BY p.servicio.servicioDs ASC")
@NamedQuery(name = "PrecioServicio.findByClienteSinAviso", query = "SELECT ps FROM PrecioServicio ps WHERE ps.cliente.cteCve = :cteCve AND ps.avisoCve IS NULL")
//@NamedQuery(name = "PrecioServicio.findByClienteSinAviso", query = "SELECT ps FROM PrecioServicio ps WHERE ps.cliente.cteCve = :cteCve AND ps.avisoCve IS NOT NULL")
public class PrecioServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "precio")
    private BigDecimal precio;

    @JoinColumn(name = "cliente", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cliente;

    @JoinColumn(name = "servicio", referencedColumnName = "SERVICIO_CVE")
    @ManyToOne(optional = false)
    private Servicio servicio;

    @JoinColumn(name = "unidad", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne(optional = false)
    private UnidadDeManejo unidad;

    @JoinColumn(name = "aviso_cve", referencedColumnName = "aviso_cve", nullable = true)
    @ManyToOne(optional = true)
    private Aviso avisoCve;
    
    @Override
    public int hashCode() {
    	if(this.id == null)
    		return System.identityHashCode(this);
    	return Objects.hashCode(this.id);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof PrecioServicio))
            return false;
        PrecioServicio other = (PrecioServicio) object;
        
        if(this == other)
        	return true;
        
        if(this == null || other.id == null) {
        	System.out.println(String.format("this: %d, other: %d", this.hashCode(), other.hashCode()));
        	return System.identityHashCode(this) == System.identityHashCode(other);
        }
        
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.PrecioServicio[ id=" + id + ", hashCode" + this.hashCode() + " ]";
    }

    public PrecioServicio() {
    }

    public PrecioServicio(Integer id) {
        this.id = id;
    }

    public PrecioServicio(Integer id, BigDecimal precio) {
        this.id = id;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public UnidadDeManejo getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadDeManejo unidad) {
        this.unidad = unidad;
    }

    public Aviso getAvisoCve() {
        return avisoCve;
    }

    public void setAvisoCve(Aviso avisoCve) {
        this.avisoCve = avisoCve;
    }
}
