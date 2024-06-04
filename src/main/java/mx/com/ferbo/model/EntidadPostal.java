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
@Table(name = "entidad_postal")
@NamedQueries({
    @NamedQuery(name = "EntidadPostal.findAll", query = "SELECT e FROM EntidadPostal e"),
    @NamedQuery(name = "EntidadPostal.findByEntidadpostalCve", query = "SELECT e FROM EntidadPostal e WHERE e.entidadpostalCve = :entidadpostalCve"),
    @NamedQuery(name = "EntidadPostal.findByEntidadpostalDs", query = "SELECT e FROM EntidadPostal e WHERE e.entidadpostalDs = :entidadpostalDs")})
public class EntidadPostal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "entidadpostal_cve")
    private Integer entidadpostalCve;
    @Size(max = 100)
    @Column(name = "entidadpostal_ds")
    private String entidadpostalDs;
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "entidadPostal")
    private List<AsentamientoHumano> asentamientoHumanoList;

    public EntidadPostal() {
    }

    public EntidadPostal(Integer entidadpostalCve) {
        this.entidadpostalCve = entidadpostalCve;
    }

    public Integer getEntidadpostalCve() {
        return entidadpostalCve;
    }

    public void setEntidadpostalCve(Integer entidadpostalCve) {
        this.entidadpostalCve = entidadpostalCve;
    }

    public String getEntidadpostalDs() {
        return entidadpostalDs;
    }

    public void setEntidadpostalDs(String entidadpostalDs) {
        this.entidadpostalDs = entidadpostalDs;
    }

    public List<AsentamientoHumano> getAsentamientoHumanoList() {
        return asentamientoHumanoList;
    }

    public void setAsentamientoHumanoList(List<AsentamientoHumano> asentamientoHumanoList) {
        this.asentamientoHumanoList = asentamientoHumanoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entidadpostalCve != null ? entidadpostalCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntidadPostal)) {
            return false;
        }
        EntidadPostal other = (EntidadPostal) object;
        if ((this.entidadpostalCve == null && other.entidadpostalCve != null) || (this.entidadpostalCve != null && !this.entidadpostalCve.equals(other.entidadpostalCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.EntidadPostal[ entidadpostalCve=" + entidadpostalCve + " ]";
    }
    
}
