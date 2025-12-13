/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "producto_constancia")
@NamedQueries({
    @NamedQuery(name = "ProductoConstancia.findAll", query = "SELECT p FROM ProductoConstancia p"),
    @NamedQuery(name = "ProductoConstancia.findById", query = "SELECT p FROM ProductoConstancia p WHERE p.id = :id"),
    @NamedQuery(name = "ProductoConstancia.findByDescripcion", query = "SELECT p FROM ProductoConstancia p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "ProductoConstancia.findByCatidadCobro", query = "SELECT p FROM ProductoConstancia p WHERE p.catidadCobro = :catidadCobro"),
    @NamedQuery(name = "ProductoConstancia.findByUnidadCobro", query = "SELECT p FROM ProductoConstancia p WHERE p.unidadCobro = :unidadCobro"),
    @NamedQuery(name = "ProductoConstancia.findByCantidadManejo", query = "SELECT p FROM ProductoConstancia p WHERE p.cantidadManejo = :cantidadManejo"),
    @NamedQuery(name = "ProductoConstancia.findByUnidadManejo", query = "SELECT p FROM ProductoConstancia p WHERE p.unidadManejo = :unidadManejo"),
    @NamedQuery(name = "ProductoConstancia.findByPlantaCve", query = "SELECT p FROM ProductoConstancia p WHERE p.plantaCve = :plantaCve"),
    @NamedQuery(name = "ProductoConstancia.findByPlantaDs", query = "SELECT p FROM ProductoConstancia p WHERE p.plantaDs = :plantaDs"),
    @NamedQuery(name = "ProductoConstancia.findByPlantaAbrev", query = "SELECT p FROM ProductoConstancia p WHERE p.plantaAbrev = :plantaAbrev"),
    @NamedQuery(name = "ProductoConstancia.findByCamaraCve", query = "SELECT p FROM ProductoConstancia p WHERE p.camaraCve = :camaraCve"),
    @NamedQuery(name = "ProductoConstancia.findByCamaraDs", query = "SELECT p FROM ProductoConstancia p WHERE p.camaraDs = :camaraDs"),
    @NamedQuery(name = "ProductoConstancia.findByCamaraAbrev", query = "SELECT p FROM ProductoConstancia p WHERE p.camaraAbrev = :camaraAbrev")})
public class ProductoConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "catidad_cobro")
    private BigDecimal catidadCobro;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "unidad_cobro")
    private String unidadCobro;
    
    @Column(name = "cantidad_manejo")
    private BigDecimal cantidadManejo;
    
    @Size(max = 255)
    @Column(name = "unidad_manejo")
    private String unidadManejo;
    
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
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "constancia", referencedColumnName = "id")
    private ConstanciaFactura constanciaFactura;

    public ProductoConstancia() {
    }

    public ProductoConstancia(Integer id) {
        this.id = id;
    }

    public ProductoConstancia(Integer id, String descripcion, int constancia, BigDecimal catidadCobro, String unidadCobro) {
        this.id = id;
        this.descripcion = descripcion;
        this.catidadCobro = catidadCobro;
        this.unidadCobro = unidadCobro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCatidadCobro() {
        return catidadCobro;
    }

    public void setCatidadCobro(BigDecimal catidadCobro) {
        this.catidadCobro = catidadCobro;
    }

    public String getUnidadCobro() {
        return unidadCobro;
    }

    public void setUnidadCobro(String unidadCobro) {
        this.unidadCobro = unidadCobro;
    }

    public BigDecimal getCantidadManejo() {
        return cantidadManejo;
    }

    public void setCantidadManejo(BigDecimal cantidadManejo) {
        this.cantidadManejo = cantidadManejo;
    }

    public String getUnidadManejo() {
        return unidadManejo;
    }

    public void setUnidadManejo(String unidadManejo) {
        this.unidadManejo = unidadManejo;
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
    
    public ConstanciaFactura getConstanciaFactura() {
		return constanciaFactura;
	}

	public void setConstanciaFactura(ConstanciaFactura constanciaFactura) {
		this.constanciaFactura = constanciaFactura;
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
        if (!(object instanceof ProductoConstancia)) {
            return false;
        }
        ProductoConstancia other = (ProductoConstancia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ProductoConstancia[ id=" + id + " ]";
    }
    
}
