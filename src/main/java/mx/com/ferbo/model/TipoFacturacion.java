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
@Table(name = "tipo_facturacion")
@NamedQueries({
    @NamedQuery(name = "TipoFacturacion.findAll", query = "SELECT t FROM TipoFacturacion t"),
    @NamedQuery(name = "TipoFacturacion.findById", query = "SELECT t FROM TipoFacturacion t WHERE t.id = :id"),
    @NamedQuery(name = "TipoFacturacion.findByNombre", query = "SELECT t FROM TipoFacturacion t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoFacturacion.findByDescripcion", query = "SELECT t FROM TipoFacturacion t WHERE t.descripcion = :descripcion")})
public class TipoFacturacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    @Size(max = 30)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 75)
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public TipoFacturacion() {
    }

    public TipoFacturacion(Integer id) {
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
    	if(this.id == null)
    		return System.identityHashCode(this);
        
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoFacturacion)) {
            return false;
        }
        TipoFacturacion other = (TipoFacturacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TipoFacturacion[ id=" + id + " ]";
    }
    
}
