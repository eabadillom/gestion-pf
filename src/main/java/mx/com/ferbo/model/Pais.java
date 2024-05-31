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
@Table(name = "pais")
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p"),
    @NamedQuery(name = "Pais.findByPaisCve", query = "SELECT p FROM Pais p WHERE p.paisCve = :paisCve"),
    @NamedQuery(name = "Pais.findByPaisDesc", query = "SELECT p FROM Pais p WHERE p.paisDesc = :paisDesc")})
public class Pais implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "pais_cve")
    private Integer paisCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "pais_desc")
    private String paisDesc;
    @OneToMany(mappedBy = "paisCved")
    private List<Domicilios> domiciliosList;

    public Pais() {
    }

    public Pais(Integer paisCve) {
        this.paisCve = paisCve;
    }

    public Pais(Integer paisCve, String paisDesc) {
        this.paisCve = paisCve;
        this.paisDesc = paisDesc;
    }

    public Integer getPaisCve() {
        return paisCve;
    }

    public void setPaisCve(Integer paisCve) {
        this.paisCve = paisCve;
    }

    public String getPaisDesc() {
        return paisDesc;
    }

    public void setPaisDesc(String paisDesc) {
        this.paisDesc = paisDesc;
    }

    public List<Domicilios> getDomiciliosList() {
        return domiciliosList;
    }

    public void setDomiciliosList(List<Domicilios> domiciliosList) {
        this.domiciliosList = domiciliosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paisCve != null ? paisCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.paisCve == null && other.paisCve != null) || (this.paisCve != null && !this.paisCve.equals(other.paisCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Pais[ paisCve=" + paisCve + " ]";
    }
    
}
