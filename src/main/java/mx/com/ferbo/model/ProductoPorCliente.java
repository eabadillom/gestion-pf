/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
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

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "PRODUCTO_POR_CLIENTE")
@NamedQueries({
    @NamedQuery(name = "ProductoPorCliente.findAll", query = "SELECT p FROM ProductoPorCliente p"),
    @NamedQuery(name = "ProductoPorCliente.findByProdXCteCve", query = "SELECT p FROM ProductoPorCliente p WHERE p.prodXCteCve = :prodXCteCve"),
    @NamedQuery(name = "ProductoPorCliente.findByProductoCve", query = "SELECT p FROM ProductoPorCliente p WHERE p.productoCve = :productoCve")})
public class ProductoPorCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROD_X_CTE_CVE")
    private Integer prodXCteCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRODUCTO_CVE")
    private int productoCve;
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;

    public ProductoPorCliente() {
    }

    public ProductoPorCliente(Integer prodXCteCve) {
        this.prodXCteCve = prodXCteCve;
    }

    public ProductoPorCliente(Integer prodXCteCve, int productoCve) {
        this.prodXCteCve = prodXCteCve;
        this.productoCve = productoCve;
    }

    public Integer getProdXCteCve() {
        return prodXCteCve;
    }

    public void setProdXCteCve(Integer prodXCteCve) {
        this.prodXCteCve = prodXCteCve;
    }

    public int getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(int productoCve) {
        this.productoCve = productoCve;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (prodXCteCve != null ? prodXCteCve.hashCode() : 0);
        return hash;
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
