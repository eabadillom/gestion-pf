/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "estado_inventario")
@NamedQueries({
    @NamedQuery(name = "EstadoInventario.findAll", query = "SELECT e FROM EstadoInventario e"),
    @NamedQuery(name = "EstadoInventario.findByEdoInvCve", query = "SELECT e FROM EstadoInventario e WHERE e.edoInvCve = :edoInvCve"),
    @NamedQuery(name = "EstadoInventario.findByEdoDescripcion", query = "SELECT e FROM EstadoInventario e WHERE e.edoDescripcion = :edoDescripcion")})
public class EstadoInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "edo_inv_cve")
    private Integer edoInvCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "edo_descripcion")
    private String edoDescripcion;
    @OneToMany(mappedBy = "edoInvCve")
    private List<DetallePartida> detallePartidaList;

    public EstadoInventario() {
    }

    public EstadoInventario(Integer edoInvCve) {
        this.edoInvCve = edoInvCve;
    }

    public EstadoInventario(Integer edoInvCve, String edoDescripcion) {
        this.edoInvCve = edoInvCve;
        this.edoDescripcion = edoDescripcion;
    }

    public Integer getEdoInvCve() {
        return edoInvCve;
    }

    public void setEdoInvCve(Integer edoInvCve) {
        this.edoInvCve = edoInvCve;
    }

    public String getEdoDescripcion() {
        return edoDescripcion;
    }

    public void setEdoDescripcion(String edoDescripcion) {
        this.edoDescripcion = edoDescripcion;
    }

    public List<DetallePartida> getDetallePartidaList() {
        return detallePartidaList;
    }

    public void setDetallePartidaList(List<DetallePartida> detallePartidaList) {
        this.detallePartidaList = detallePartidaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (edoInvCve != null ? edoInvCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoInventario)) {
            return false;
        }
        EstadoInventario other = (EstadoInventario) object;
        if ((this.edoInvCve == null && other.edoInvCve != null) || (this.edoInvCve != null && !this.edoInvCve.equals(other.edoInvCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.EstadoInventario[ edoInvCve=" + edoInvCve + " ]";
    }
    
}
