/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
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
import javax.validation.constraints.Size;



/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "SERVICIO")
@NamedQueries({
    @NamedQuery(name = "Servicio.findAll", query = "SELECT s FROM Servicio s"),
    @NamedQuery(name = "Servicio.findByServicioCve", query = "SELECT s FROM Servicio s WHERE s.servicioCve = :servicioCve"),
    @NamedQuery(name = "Servicio.findByServicioDs", query = "SELECT s FROM Servicio s WHERE s.servicioDs = :servicioDs"),
    @NamedQuery(name = "Servicio.findByServicioCod", query = "SELECT s FROM Servicio s WHERE s.servicioCod = :servicioCod"),
    @NamedQuery(name = "Servicio.findByCdUnidad", query = "SELECT s FROM Servicio s WHERE s.cdUnidad = :cdUnidad"),//cd unidad paso a ser clave foranea
    @NamedQuery(name = "Servicio.findByUuId", query = "SELECT s FROM Servicio s WHERE s.uuId = :uuId")})
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
    @Size(max=50)
    @Column(name = "uuid")
    private String uuId;
    @OneToMany(mappedBy = "servicioCve")
    private List<DetalleConstanciaServicios> detalleConstanciaServiciosList;
    @OneToMany(mappedBy = "servicioCve")
    private List<ConstanciaSalidaServicios> constanciaSalidaServiciosList;
    
    @JoinColumn(name = "COBRO", referencedColumnName = "id")
    @ManyToOne
    private TipoCobro cobro;
    
    @JoinColumn(name = "cd_unidad", referencedColumnName = "cd_unidad", insertable=false,updatable=false)
    @ManyToOne
    private ClaveUnidad claveUnit; //(AQUI HAY UN ERROR) 
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioCve")
    private List<CuotaMensualServicio> cuotaMensualServicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicio")
    private List<PrecioServicio> precioServicioList;
    @OneToMany(mappedBy = "servicioCve")
    private List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "servicioCve")
    private List<ConstanciaServicioDetalle> constanciaServicioDetalleList;
    

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

	public String getUuId() {
		return uuId;
	}

	public void setUuId(String uuId) {
		this.uuId = uuId;
	}

	public List<DetalleConstanciaServicios> getDetalleConstanciaServiciosList() {
        return detalleConstanciaServiciosList;
    }

    public void setDetalleConstanciaServiciosList(List<DetalleConstanciaServicios> detalleConstanciaServiciosList) {
        this.detalleConstanciaServiciosList = detalleConstanciaServiciosList;
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

	public List<CuotaMensualServicio> getCuotaMensualServicioList() {
        return cuotaMensualServicioList;
    }

    public void setCuotaMensualServicioList(List<CuotaMensualServicio> cuotaMensualServicioList) {
        this.cuotaMensualServicioList = cuotaMensualServicioList;
    }

    public List<PrecioServicio> getPrecioServicioList() {
        return precioServicioList;
    }

    public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
        this.precioServicioList = precioServicioList;
    }

    public List<ConstanciaDepositoDetalle> getConstanciaDepositoDetalleList() {
        return constanciaDepositoDetalleList;
    }

    public void setConstanciaDepositoDetalleList(List<ConstanciaDepositoDetalle> constanciaDepositoDetalleList) {
        this.constanciaDepositoDetalleList = constanciaDepositoDetalleList;
    }

    public List<ConstanciaServicioDetalle> getConstanciaServicioDetalleList() {
        return constanciaServicioDetalleList;
    }

    public void setConstanciaServicioDetalleList(List<ConstanciaServicioDetalle> constanciaServicioDetalleList) {
        this.constanciaServicioDetalleList = constanciaServicioDetalleList;
    }

    public List<ConstanciaSalidaServicios> getConstanciaSalidaServiciosList() {
		return constanciaSalidaServiciosList;
	}

	public void setConstanciaSalidaServiciosList(List<ConstanciaSalidaServicios> constanciaSalidaServiciosList) {
		this.constanciaSalidaServiciosList = constanciaSalidaServiciosList;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (servicioCve != null ? servicioCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
    
}
