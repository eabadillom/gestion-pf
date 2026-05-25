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
@Table(name = "tipo_asentamiento")
@NamedQueries({
    @NamedQuery(name = "TipoAsentamiento.findAll", query = "SELECT t FROM TipoAsentamiento t"),
    @NamedQuery(name = "TipoAsentamiento.findByTipoasntmntoCve", query = "SELECT t FROM TipoAsentamiento t WHERE t.tipoasntmntoCve = :tipoasntmntoCve"),
    @NamedQuery(name = "TipoAsentamiento.findByTipoasntmntoDs", query = "SELECT t FROM TipoAsentamiento t WHERE t.tipoasntmntoDs = :tipoasntmntoDs"),
    @NamedQuery(name = "TipoAsentamiento.findByTipoasntmntoDsCorta", query = "SELECT t FROM TipoAsentamiento t WHERE t.tipoasntmntoDsCorta = :tipoasntmntoDsCorta")
})
public class TipoAsentamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "tipoasntmnto_cve")
    private Short tipoasntmntoCve;
    
    @Size(max = 100)
    @Column(name = "tipoasntmnto_ds")
    private String tipoasntmntoDs;
    
    @Size(max = 4)
    @Column(name = "tipoasntmnto_ds_corta")
    private String tipoasntmntoDsCorta;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAsentamiento")
    private List<AsentamientoHumano> asentamientoHumanoList;

    public TipoAsentamiento() {
    }

    public TipoAsentamiento(Short tipoasntmntoCve) {
        this.tipoasntmntoCve = tipoasntmntoCve;
    }

    public Short getTipoasntmntoCve() {
        return tipoasntmntoCve;
    }

    public void setTipoasntmntoCve(Short tipoasntmntoCve) {
        this.tipoasntmntoCve = tipoasntmntoCve;
    }

    public String getTipoasntmntoDs() {
        return tipoasntmntoDs;
    }

    public void setTipoasntmntoDs(String tipoasntmntoDs) {
        this.tipoasntmntoDs = tipoasntmntoDs;
    }

    public String getTipoasntmntoDsCorta() {
        return tipoasntmntoDsCorta;
    }

    public void setTipoasntmntoDsCorta(String tipoasntmntoDsCorta) {
        this.tipoasntmntoDsCorta = tipoasntmntoDsCorta;
    }

    public List<AsentamientoHumano> getAsentamientoHumanoList() {
        return asentamientoHumanoList;
    }

    public void setAsentamientoHumanoList(List<AsentamientoHumano> asentamientoHumanoList) {
        this.asentamientoHumanoList = asentamientoHumanoList;
    }

    @Override
    public int hashCode() {
        if(this.tipoasntmntoCve == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.tipoasntmntoCve);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoAsentamiento other = (TipoAsentamiento) obj;
        return Objects.equals(this.tipoasntmntoCve, other.tipoasntmntoCve);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TipoAsentamiento[ tipoasntmntoCve=" + tipoasntmntoCve + " ]";
    }
    
}
