/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
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

@Entity
@Table(name = "unidad_de_producto")
@NamedQueries({
        @NamedQuery(name = "UnidadDeProducto.findAll", query = "SELECT u FROM UnidadDeProducto u"),
        @NamedQuery(name = "UnidadDeProducto.findByUnidadDeProductoCve", query = "SELECT u FROM UnidadDeProducto u WHERE u.unidadDeProductoCve = :unidadDeProductoCve"),
        @NamedQuery(name = "UnidadDeProducto.findByProductoCveUnidadDeProductoCve", query = "SELECT u FROM UnidadDeProducto u WHERE u.productoCve.productoCve = :productoCve AND u.unidadDeManejoCve.unidadDeManejoCve = :unidadDeManejoCve"),
        @NamedQuery(name = "UnidadDeProducto.findByProductoCve", query = "SELECT u FROM UnidadDeProducto u WHERE u.productoCve = :productoCve"),
        @NamedQuery(name = "UnidadDeProducto.findByCliente", query = "SELECT udp FROM UnidadDeProducto udp JOIN udp.productoCve p JOIN udp.unidadDeManejoCve udm JOIN p.productoPorClienteList ppc WHERE ppc.cteCve.cteCve = :idCliente ")})
public class UnidadDeProducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UNIDAD_DE_PRODUCTO_CVE")
    private Integer unidadDeProductoCve;
    
    @JoinColumn(name = "UNIDAD_DE_MANEJO_CVE", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne(optional = false)
    private UnidadDeManejo unidadDeManejoCve;

    @JoinColumn(name = "PRODUCTO_CVE", referencedColumnName = "PRODUCTO_CVE")
    @ManyToOne(optional = false)
    private Producto productoCve;
    
    @Override
    public int hashCode() {
    	if(this.unidadDeProductoCve == null)
    		return System.identityHashCode(this);
        return Objects.hash(this.unidadDeProductoCve);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UnidadDeProducto)) {
            return false;
        }
        UnidadDeProducto other = (UnidadDeProducto) object;
        if ((this.unidadDeProductoCve == null && other.unidadDeProductoCve != null)
                || (this.unidadDeProductoCve != null && !this.unidadDeProductoCve.equals(other.unidadDeProductoCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.UnidadDeProducto[ hashCode=" + this.hashCode() + ", unidadDeProductoCve=" + unidadDeProductoCve + " ]";
    }

    public UnidadDeProducto() {
    }

    public UnidadDeProducto(Integer unidadDeProductoCve) {
        this.unidadDeProductoCve = unidadDeProductoCve;
    }

    public UnidadDeProducto(Integer unidadDeProductoCve, int productoCve) {
        this.unidadDeProductoCve = unidadDeProductoCve;
        this.productoCve = new Producto();
    }
    
    public UnidadDeProducto(UnidadDeProducto other) {
    	this.unidadDeProductoCve = other.unidadDeProductoCve;
    	this.productoCve         = other.productoCve == null ? null : new Producto(other.productoCve);
    	this.unidadDeManejoCve   = other.unidadDeManejoCve == null ? null : new UnidadDeManejo(other.unidadDeManejoCve);
    }

    public Integer getUnidadDeProductoCve() {
        return unidadDeProductoCve;
    }

    public void setUnidadDeProductoCve(Integer unidadDeProductoCve) {
        this.unidadDeProductoCve = unidadDeProductoCve;
    }

    public UnidadDeManejo getUnidadDeManejoCve() {
        return unidadDeManejoCve;
    }

    public void setUnidadDeManejoCve(UnidadDeManejo unidadDeManejoCve) {
        this.unidadDeManejoCve = unidadDeManejoCve;
    }

    public Producto getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(Producto productoCve) {
        this.productoCve = productoCve;
    }
}
