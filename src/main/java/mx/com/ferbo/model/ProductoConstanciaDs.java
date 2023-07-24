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
@Table(name = "PRODUCTO_CONSTANCIA_DS")
@NamedQueries({
    @NamedQuery(name = "ProductoConstanciaDs.findAll", query = "SELECT p FROM ProductoConstanciaDs p"),
    @NamedQuery(name = "ProductoConstanciaDs.findById", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.id = :id"),
    @NamedQuery(name = "ProductoConstanciaDs.findByDescripcion", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "ProductoConstanciaDs.findByCatidadCobro", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.catidadCobro = :catidadCobro"),
    @NamedQuery(name = "ProductoConstanciaDs.findByUnidadCobro", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.unidadCobro = :unidadCobro"),
    @NamedQuery(name = "ProductoConstanciaDs.findByCantidadManejo", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.cantidadManejo = :cantidadManejo"),
    @NamedQuery(name = "ProductoConstanciaDs.findByUnidadManejo", query = "SELECT p FROM ProductoConstanciaDs p WHERE p.unidadManejo = :unidadManejo")})
public class ProductoConstanciaDs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATIDAD_COBRO")
    private BigDecimal catidadCobro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "UNIDAD_COBRO")
    private String unidadCobro;
    @Column(name = "CANTIDAD_MANEJO")
    private BigDecimal cantidadManejo;
    @Size(max = 255)
    @Column(name = "UNIDAD_MANEJO")
    private String unidadManejo;
    @JoinColumn(name = "CONSTANCIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConstanciaFacturaDs constancia;

    public ProductoConstanciaDs() {
    }

    public ProductoConstanciaDs(Integer id) {
        this.id = id;
    }

    public ProductoConstanciaDs(Integer id, String descripcion, BigDecimal catidadCobro, String unidadCobro) {
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

    public ConstanciaFacturaDs getConstancia() {
        return constancia;
    }

    public void setConstancia(ConstanciaFacturaDs constancia) {
        this.constancia = constancia;
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
        if (!(object instanceof ProductoConstanciaDs)) {
            return false;
        }
        ProductoConstanciaDs other = (ProductoConstanciaDs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ProductoConstanciaDs[ id=" + id + " ]";
    }
    
}
