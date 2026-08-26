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
@Table(name = "tipo_telefono")
@NamedQueries({
    @NamedQuery(name = "TipoTelefono.findAll", query = "SELECT t FROM TipoTelefono t"),
    @NamedQuery(name = "TipoTelefono.findByTpTelefono", query = "SELECT t FROM TipoTelefono t WHERE t.clave = :tpTelefono"),
    @NamedQuery(name = "TipoTelefono.findByNbTelefono", query = "SELECT t FROM TipoTelefono t WHERE t.nombre = :nbTelefono")})
public class TipoTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tp_telefono")
    private Short clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_telefono")
    private String nombre;
    
    @Override
    public int hashCode() {
    	if(this.clave == null)
    		return System.identityHashCode(this);
    	return Objects.hash(this.clave);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoTelefono)) {
            return false;
        }
        TipoTelefono other = (TipoTelefono) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TipoTelefono[ tpTelefono=" + clave + " ]";
    }

    public TipoTelefono() {
    }

    public TipoTelefono(Short clave) {
        this.clave = clave;
    }

    public TipoTelefono(Short clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    public Short getClave() {
        return clave;
    }

    public void setClave(Short clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
