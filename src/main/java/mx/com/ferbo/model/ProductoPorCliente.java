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

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "producto_por_cliente")
@NamedQueries({
    @NamedQuery(name = "ProductoPorCliente.findAll", query = "SELECT p FROM ProductoPorCliente p"),
    @NamedQuery(name = "ProductoPorCliente.findByProdXCteCve", query = "SELECT p FROM ProductoPorCliente p WHERE p.prodXCteCve = :prodXCteCve"),
    @NamedQuery(name = "ProductoPorCliente.findByProductoCve", query = "SELECT p FROM ProductoPorCliente p WHERE p.productoCve = :productoCve"),
    @NamedQuery(name = "ProductoPorCliente.findByCteCve", query = "SELECT p FROM ProductoPorCliente p WHERE p.cteCve.cteCve = :cteCve"),
    @NamedQuery(name = "ProductoPorCliente.findByCteCveOrderByProductoDs", query = "SELECT p FROM ProductoPorCliente p WHERE p.cteCve.cteCve = :cteCve ORDER BY p.productoCve.productoDs"),
    @NamedQuery(name = "ProductoPorCliente.findByCliente", query = "SELECT p FROM ProductoPorCliente p WHERE p.cteCve = :cteCve")})

public class ProductoPorCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROD_X_CTE_CVE")
    private Integer prodXCteCve;
    
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;
    
    @JoinColumn(name = "PRODUCTO_CVE", referencedColumnName = "PRODUCTO_CVE")
    @ManyToOne(optional = false)
    private Producto productoCve;

    public ProductoPorCliente() {
    }

    public ProductoPorCliente(Integer prodXCteCve) {
        this.prodXCteCve = prodXCteCve;
    }
    
    public ProductoPorCliente(Cliente cliente) {//nuevo
    	this.cteCve = cliente;
    }

    public Integer getProdXCteCve() {
        return prodXCteCve;
    }

    public void setProdXCteCve(Integer prodXCteCve) {
        this.prodXCteCve = prodXCteCve;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    
    public Producto getProductoCve() {
		return productoCve;
	}

	public void setProductoCve(Producto productoCve) {
		this.productoCve = productoCve;
	}

	@Override
    public int hashCode() {
		if(this.prodXCteCve == null)
			return System.identityHashCode(this);
		return Objects.hash(this.prodXCteCve);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductoPorCliente)) {
            return false;
        }
        ProductoPorCliente other = (ProductoPorCliente) object;
        if ((this.prodXCteCve == null && other.prodXCteCve != null) || (this.prodXCteCve != null && !this.prodXCteCve.equals(other.prodXCteCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ProductoPorCliente[ prodXCteCve=" + prodXCteCve + " ]";
    }
    
}
