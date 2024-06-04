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
import javax.persistence.Id;
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
@Table(name = "status_constancia_servicio")
@NamedQueries({
    @NamedQuery(name = "StatusConstanciaServicio.findAll", query = "SELECT s FROM StatusConstanciaServicio s"),
    @NamedQuery(name = "StatusConstanciaServicio.findById", query = "SELECT s FROM StatusConstanciaServicio s WHERE s.id = :id"),
    @NamedQuery(name = "StatusConstanciaServicio.findByNombre", query = "SELECT s FROM StatusConstanciaServicio s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "StatusConstanciaServicio.findByDescripcion", query = "SELECT s FROM StatusConstanciaServicio s WHERE s.descripcion = :descripcion")})
public class StatusConstanciaServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public StatusConstanciaServicio() {
    }

    public StatusConstanciaServicio(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof StatusConstanciaServicio)) {
            return false;
        }
        StatusConstanciaServicio other = (StatusConstanciaServicio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.StatusConstanciaServicio[ id=" + id + " ]";
    }
    
}
