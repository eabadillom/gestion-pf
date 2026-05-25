/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "constancia_factura")
@NamedQueries({
    @NamedQuery(name = "ConstanciaFactura.findAll", query = "SELECT c FROM ConstanciaFactura c"),
    @NamedQuery(name = "ConstanciaFactura.findById", query = "SELECT c FROM ConstanciaFactura c WHERE c.id = :id"),
    @NamedQuery(name = "ConstanciaFactura.findByFolioVigenciaInicioVigenciaFin", query = "SELECT c FROM ConstanciaFactura c WHERE c.folio.folio = :folio AND c.vigenciaInicio = :vigenciaInicio and c.vigenciaFin = :vigenciaFin"), 
    @NamedQuery(name = "ConstanciaFactura.findByFolio", query = "SELECT c FROM ConstanciaFactura c WHERE c.folio = :folio"),
    @NamedQuery(name = "ConstanciaFactura.findByFolioCliente", query = "SELECT c FROM ConstanciaFactura c WHERE c.folioCliente = :folioCliente"),
    @NamedQuery(name = "ConstanciaFactura.findByVigenciaInicio", query = "SELECT c FROM ConstanciaFactura c WHERE c.vigenciaInicio = :vigenciaInicio"),
    @NamedQuery(name = "ConstanciaFactura.findByVigenciaFin", query = "SELECT c FROM ConstanciaFactura c WHERE c.vigenciaFin = :vigenciaFin"),
    @NamedQuery(name = "ConstanciaFactura.findByPlantaCve", query = "SELECT c FROM ConstanciaFactura c WHERE c.plantaCve = :plantaCve"),
    @NamedQuery(name = "ConstanciaFactura.findByPlantaDs", query = "SELECT c FROM ConstanciaFactura c WHERE c.plantaDs = :plantaDs"),
    @NamedQuery(name = "ConstanciaFactura.findByPlantaAbrev", query = "SELECT c FROM ConstanciaFactura c WHERE c.plantaAbrev = :plantaAbrev"),
    @NamedQuery(name = "ConstanciaFactura.findByCamaraCve", query = "SELECT c FROM ConstanciaFactura c WHERE c.camaraCve = :camaraCve"),
    @NamedQuery(name = "ConstanciaFactura.findByCamaraDs", query = "SELECT c FROM ConstanciaFactura c WHERE c.camaraDs = :camaraDs"),
    @NamedQuery(name = "ConstanciaFactura.findByCamaraAbrev", query = "SELECT c FROM ConstanciaFactura c WHERE c.camaraAbrev = :camaraAbrev")})
public class ConstanciaFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne(optional = false)
    private ConstanciaDeDeposito folio;
    
    @Size(max = 30)
    @Column(name = "folio_cliente")
    private String folioCliente;
    
    @Column(name = "vigencia_inicio")
    @Temporal(TemporalType.DATE)
    private Date vigenciaInicio;
    
    @Column(name = "vigencia_fin")
    @Temporal(TemporalType.DATE)
    private Date vigenciaFin;
    
    @Column(name = "planta_cve")
    private Integer plantaCve;
    @Size(max = 80)
    @Column(name = "planta_ds")
    private String plantaDs;
    
    @Size(max = 6)
    @Column(name = "planta_abrev")
    private String plantaAbrev;
    
    @Column(name = "camara_cve")
    private Integer camaraCve;
    
    @Size(max = 80)
    @Column(name = "camara_ds")
    private String camaraDs;
    
    @Size(max = 6)
    @Column(name = "camara_abrev")
    private String camaraAbrev;
    
    @ManyToOne
    @JoinColumn(name = "factura", referencedColumnName = "id")
    private Factura factura;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "constancia")
    private List<ServicioConstancia> servicioConstanciaList;
    
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "constanciaFactura")
    private List<ProductoConstancia> productoConstanciaList;

    public ConstanciaFactura() {
    }

    public ConstanciaFactura(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
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

    public Integer getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(Integer plantaCve) {
        this.plantaCve = plantaCve;
    }

    public String getPlantaDs() {
        return plantaDs;
    }

    public void setPlantaDs(String plantaDs) {
        this.plantaDs = plantaDs;
    }

    public String getPlantaAbrev() {
        return plantaAbrev;
    }

    public void setPlantaAbrev(String plantaAbrev) {
        this.plantaAbrev = plantaAbrev;
    }

    public Integer getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(Integer camaraCve) {
        this.camaraCve = camaraCve;
    }

    public String getCamaraDs() {
        return camaraDs;
    }

    public void setCamaraDs(String camaraDs) {
        this.camaraDs = camaraDs;
    }

    public String getCamaraAbrev() {
        return camaraAbrev;
    }

    public void setCamaraAbrev(String camaraAbrev) {
        this.camaraAbrev = camaraAbrev;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    
    public List<ServicioConstancia> getServicioConstanciaList() {
        return servicioConstanciaList;
    }

    public void setServicioConstanciaList(List<ServicioConstancia> servicioConstanciaList) {
        this.servicioConstanciaList = servicioConstanciaList;
    }
    
    public List<ProductoConstancia> getProductoConstanciaList() {
        return productoConstanciaList;
    }

    public void setProductoConstanciaList(List<ProductoConstancia> productoConstanciaList) {
        this.productoConstanciaList = productoConstanciaList;
    }
    
    public ConstanciaDeDeposito getFolio() {
		return folio;
	}

	public void setFolio(ConstanciaDeDeposito constanciaDeDeposito) {
		this.folio = constanciaDeDeposito;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ConstanciaFactura)) {
            return false;
        }
        ConstanciaFactura other = (ConstanciaFactura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "ConstanciaFactura [id=" + id + ", folio=" + folio + ", folioCliente="
				+ folioCliente + ", vigenciaInicio=" + vigenciaInicio + ", vigenciaFin=" + vigenciaFin + ", plantaCve="
				+ plantaCve + ", plantaDs=" + plantaDs + ", plantaAbrev=" + plantaAbrev + ", camaraCve=" + camaraCve
				+ ", camaraDs=" + camaraDs + ", camaraAbrev=" + camaraAbrev + ", factura=" + factura
				+ ", servicioConstanciaList=" + servicioConstanciaList + ", productoConstanciaList="
				+ productoConstanciaList + "]";
	}
}
