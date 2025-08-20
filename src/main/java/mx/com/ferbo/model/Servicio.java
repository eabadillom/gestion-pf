package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "servicio")
@NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s ORDER BY s.servicioDs")
@NamedQuery(name = "Servicio.findByServicioCve", query = "SELECT s FROM Servicio s WHERE s.servicioCve = :servicioCve")
@NamedQuery(name = "Servicio.findByServicioDs", query = "SELECT s FROM Servicio s WHERE s.servicioDs = :servicioDs")
@NamedQuery(name = "Servicio.findByServicioCod", query = "SELECT s FROM Servicio s WHERE s.servicioCod = :servicioCod")
@NamedQuery(name = "Servicio.findByCdUnidad", query = "SELECT s FROM Servicio s WHERE s.cdUnidad = :cdUnidad")
@NamedQuery(name = "Servicio.findByUuId", query = "SELECT s FROM Servicio s WHERE s.uuId = :uuId")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SERVICIO_CVE")
    private Integer servicioCve;
    
    @Size(max = 80)
    @Column(name = "SERVICIO_DS")
    private String servicioDs;
    
    @Size(max = 20)
    @Column(name = "SERVICIO_COD")
    private String servicioCod;
    
    @Size(max = 5)
    @Column(name = "cd_unidad")
    private String cdUnidad;
    
    @Size(max = 50)
    @Column(name = "SERVICIO_NOM")
    private String servicioNombre;
    
    @Size(max=50)
    @Column(name = "uuid")
    private String uuId;
    
    @JoinColumn(name = "COBRO", referencedColumnName = "id")
    @ManyToOne
    private TipoCobro cobro;
    
    @JoinColumn(name = "cd_unidad", referencedColumnName = "cd_unidad", insertable=false,updatable=false)
    @ManyToOne
    private ClaveUnidad claveUnit; 
    
    @OneToMany(mappedBy = "servicio")
    private List<PrecioServicio> precioServicioList;
    
    @Override
    public int hashCode() {
		if(this.servicioCve == null)
			return System.identityHashCode(this);
		return Objects.hash(this.servicioCve);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Servicio)) {
            return false;
        }
        Servicio other = (Servicio) object;
        if ((this.servicioCve == null && other.servicioCve != null) || (this.servicioCve != null && !this.servicioCve.equals(other.servicioCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Servicio[ servicioCve=" + servicioCve + " ]";
    }
    
	public Servicio() {
    }

    public Servicio(Integer servicioCve) {
        this.servicioCve = servicioCve;
    }

    public Integer getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(Integer servicioCve) {
        this.servicioCve = servicioCve;
    }

    public String getServicioDs() {
        return servicioDs;
    }

    public void setServicioDs(String servicioDs) {
        this.servicioDs = servicioDs;
    }

    public String getServicioCod() {
        return servicioCod;
    }

    public void setServicioCod(String servicioCod) {
        this.servicioCod = servicioCod;
    }

    public String getCdUnidad() {
		return cdUnidad;
	}

	public void setCdUnidad(String cdUnidad) {
		this.cdUnidad = cdUnidad;
	}
	
	public String getServicioNombre() {
		return servicioNombre;
	}

	public void setServicioNombre(String servicioNombre) {
		this.servicioNombre = servicioNombre;
	}

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

    public TipoCobro getCobro() {
        return cobro;
    }

    public void setCobro(TipoCobro cobro) {
        this.cobro = cobro;
    }

	public ClaveUnidad getClaveUnit() {
		return claveUnit;
	}

	public void setClaveUnit(ClaveUnidad claveUnit) {
		this.claveUnit = claveUnit;
	}

    public List<PrecioServicio> getPrecioServicioList() {
        return precioServicioList;
    }

    public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
        this.precioServicioList = precioServicioList;
    }
}
