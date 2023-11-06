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
@Table(name = "CONSTANCIA_FACTURA_DS")
@NamedQueries({
    @NamedQuery(name = "ConstanciaFacturaDs.findAll", query = "SELECT c FROM ConstanciaFacturaDs c"),
    @NamedQuery(name = "ConstanciaFacturaDs.findById", query = "SELECT c FROM ConstanciaFacturaDs c WHERE c.id = :id"),
    @NamedQuery(name = "ConstanciaFacturaDs.findByFolioCliente", query = "SELECT c FROM ConstanciaFacturaDs c WHERE c.folioCliente = :folioCliente")})
public class ConstanciaFacturaDs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 30)
    @Column(name = "FOLIO_CLIENTE")
    private String folioCliente;
    @OneToMany(mappedBy = "constancia", cascade = CascadeType.ALL)//MODIFICADO 1 JUNIO
    private List<ServicioConstanciaDs> servicioConstanciaDsList;
    @OneToMany(mappedBy = "constancia", cascade = CascadeType.ALL)//MODIFICADO 1 JUNIO
    private List<ProductoConstanciaDs> productoConstanciaDsList;
    @JoinColumn(name = "FACTURA", referencedColumnName = "id")
    @ManyToOne
    private Factura factura;
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne
    private ConstanciaDeServicio constanciaDeServicio;

    public ConstanciaFacturaDs() {
    }

    public ConstanciaFacturaDs(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ConstanciaDeServicio getConstanciaDeServicio() {
        return constanciaDeServicio;
    }

    public void setConstanciaDeServicio(ConstanciaDeServicio constanciaDeServicio) {
        this.constanciaDeServicio = constanciaDeServicio;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public List<ServicioConstanciaDs> getServicioConstanciaDsList() {
        return servicioConstanciaDsList;
    }

    public void setServicioConstanciaDsList(List<ServicioConstanciaDs> servicioConstanciaDsList) {
        this.servicioConstanciaDsList = servicioConstanciaDsList;
    }

    public List<ProductoConstanciaDs> getProductoConstanciaDsList() {
        return productoConstanciaDsList;
    }

    public void setProductoConstanciaDsList(List<ProductoConstanciaDs> productoConstanciaDsList) {
        this.productoConstanciaDsList = productoConstanciaDsList;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
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
        if (!(object instanceof ConstanciaFacturaDs)) {
            return false;
        }
        ConstanciaFacturaDs other = (ConstanciaFacturaDs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ConstanciaFacturaDs[ id=" + id + " ]";
    }
    
}
