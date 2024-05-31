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
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "detalle_constancia_servicios")
@NamedQueries({
    @NamedQuery(name = "DetalleConstanciaServicios.findAll", query = "SELECT d FROM DetalleConstanciaServicios d"),
    @NamedQuery(name = "DetalleConstanciaServicios.findById", query = "SELECT d FROM DetalleConstanciaServicios d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleConstanciaServicios.findByServicioDes", query = "SELECT d FROM DetalleConstanciaServicios d WHERE d.servicioDes = :servicioDes"),
    @NamedQuery(name = "DetalleConstanciaServicios.findByCantidad", query = "SELECT d FROM DetalleConstanciaServicios d WHERE d.cantidad = :cantidad")})
public class DetalleConstanciaServicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 200)
    @Column(name = "SERVICIO_DES")
    private String servicioDes;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @JoinColumn(name = "CONSTANCIA_CVE", referencedColumnName = "ID")
    @ManyToOne
    private ConstanciaServicios constanciaCve;
    @JoinColumn(name = "SERVICIO_CVE", referencedColumnName = "SERVICIO_CVE")
    @ManyToOne
    private Servicio servicioCve;

    public DetalleConstanciaServicios() {
    }

    public DetalleConstanciaServicios(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServicioDes() {
        return servicioDes;
    }

    public void setServicioDes(String servicioDes) {
        this.servicioDes = servicioDes;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public ConstanciaServicios getConstanciaCve() {
        return constanciaCve;
    }

    public void setConstanciaCve(ConstanciaServicios constanciaCve) {
        this.constanciaCve = constanciaCve;
    }

    public Servicio getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(Servicio servicioCve) {
        this.servicioCve = servicioCve;
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
        if (!(object instanceof DetalleConstanciaServicios)) {
            return false;
        }
        DetalleConstanciaServicios other = (DetalleConstanciaServicios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetalleConstanciaServicios[ id=" + id + " ]";
    }
    
}
