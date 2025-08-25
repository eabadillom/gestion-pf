/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "producto")
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByProductoCve", query = "SELECT p FROM Producto p WHERE p.productoCve = :productoCve"),
    @NamedQuery(name = "Producto.findByProductoDs", query = "SELECT p FROM Producto p WHERE p.productoDs = :productoDs"),
    @NamedQuery(name = "Producto.findByNumeroProd", query = "SELECT p FROM Producto p WHERE p.numeroProd = :numeroProd"),
    @NamedQuery(name = "Producto.findByCategoria", query = "SELECT p FROM Producto p WHERE p.categoria = :categoria"),
    @NamedQuery(name = "Producto.findByCliente", query = "SELECT p FROM Producto p JOIN p.productoPorClienteList ppc WHERE ppc.cteCve.cteCve = :idCliente ORDER BY p.productoDs ASC")
})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRODUCTO_CVE")
    private Integer productoCve;
    
    @Size(max = 80)
    @Column(name = "PRODUCTO_DS")
    private String productoDs;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "NUMERO_PROD")
    private String numeroProd;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "categoria")
    private int categoria;
    
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "productoCve")
    private List<ProductoPorCliente> productoPorClienteList;
    
    @Override
    public int hashCode() {
        if(this.productoCve == null)
        	return System.identityHashCode(this);
        return Objects.hash(this.productoCve);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.productoCve == null && other.productoCve != null) || (this.productoCve != null && !this.productoCve.equals(other.productoCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Producto[ productoCve=" + productoCve + " ]";
    }

    public Producto() {
    }

    public Producto(Integer productoCve) {
        this.productoCve = productoCve;
    }

    public Producto(Integer productoCve, String numeroProd, int categoria) {
        this.productoCve = productoCve;
        this.numeroProd = numeroProd;
        this.categoria = categoria;
    }

    public Integer getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(Integer productoCve) {
        this.productoCve = productoCve;
    }

    public String getProductoDs() {
        return productoDs;
    }

    public void setProductoDs(String productoDs) {
        this.productoDs = productoDs;
    }

    public String getNumeroProd() {
        return numeroProd;
    }

    public void setNumeroProd(String numeroProd) {
        this.numeroProd = numeroProd;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public List<ProductoPorCliente> getProductoPorClienteList() {
		return productoPorClienteList;
	}

	public void setProductoPorClienteList(List<ProductoPorCliente> productoPorClienteList) {
		this.productoPorClienteList = productoPorClienteList;
	}
}
