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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "unidad_de_manejo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadDeManejo.findAll", query = "SELECT u FROM UnidadDeManejo u ORDER BY u.unidadDeManejoDs ASC"),
    @NamedQuery(name = "UnidadDeManejo.findByUnidadDeManejoCve", query = "SELECT u FROM UnidadDeManejo u WHERE u.unidadDeManejoCve = :unidadDeManejoCve"),
    @NamedQuery(name = "UnidadDeManejo.findByUnidadDeManejoDs", query = "SELECT u FROM UnidadDeManejo u WHERE u.unidadDeManejoDs = :unidadDeManejoDs ORDER BY u.unidadDeManejoDs ASC")})
public class UnidadDeManejo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "UNIDAD_DE_MANEJO_CVE")
    private Integer unidadDeManejoCve;
    
    @Size(max = 100)
    @Column(name = "UNIDAD_DE_MANEJO_DS")
    private String unidadDeManejoDs;
    
    @Override
    public int hashCode() {
        if(this.unidadDeManejoCve == null)
        	return System.identityHashCode(this);
        return Objects.hash(this.unidadDeManejoCve);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UnidadDeManejo)) {
            return false;
        }
        UnidadDeManejo other = (UnidadDeManejo) object;
        if ((this.unidadDeManejoCve == null && other.unidadDeManejoCve != null) || (this.unidadDeManejoCve != null && !this.unidadDeManejoCve.equals(other.unidadDeManejoCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.UnidadDeManejo[ unidadDeManejoCve=" + unidadDeManejoCve + " ]";
    }
    
    public UnidadDeManejo() {
    }

    public UnidadDeManejo(Integer unidadDeManejoCve) {
        this.unidadDeManejoCve = unidadDeManejoCve;
    }

    public Integer getUnidadDeManejoCve() {
        return unidadDeManejoCve;
    }

    public void setUnidadDeManejoCve(Integer unidadDeManejoCve) {
        this.unidadDeManejoCve = unidadDeManejoCve;
    }

    public String getUnidadDeManejoDs() {
        return unidadDeManejoDs;
    }

    public void setUnidadDeManejoDs(String unidadDeManejoDs) {
        this.unidadDeManejoDs = unidadDeManejoDs;
    }
}
