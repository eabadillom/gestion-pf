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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "telefono")
@NamedQueries({
        @NamedQuery(name = "Telefono.findAll", query = "SELECT t FROM Telefono t"),
        @NamedQuery(name = "Telefono.findByIdTelefono", query = "SELECT t FROM Telefono t WHERE t.idTelefono = :idTelefono"),
        @NamedQuery(name = "Telefono.findByNbTelefono", query = "SELECT t FROM Telefono t WHERE t.nbTelefono = :nbTelefono"),
        @NamedQuery(name = "Telefono.findByStPrincipal", query = "SELECT t FROM Telefono t WHERE t.stPrincipal = :stPrincipal") })
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_telefono")
    private Integer idTelefono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "nb_telefono")
    private String nbTelefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "st_principal")
    private boolean stPrincipal;
    @JoinColumn(name = "tp_telefono", referencedColumnName = "tp_telefono")
    @ManyToOne(optional = false)
    private TipoTelefono tpTelefono;
    @OneToMany(mappedBy = "idTelefono")
    private List<MedioCnt> medioCntList;

    public Telefono() {
    }

    public Telefono(Integer idTelefono) {
        this.idTelefono = idTelefono;
    }

    public Telefono(Integer idTelefono, String nbTelefono, boolean stPrincipal) {
        this.idTelefono = idTelefono;
        this.nbTelefono = nbTelefono;
        this.stPrincipal = stPrincipal;
    }

    public Integer getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Integer idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getNbTelefono() {
        return nbTelefono;
    }

    public void setNbTelefono(String nbTelefono) {
        this.nbTelefono = nbTelefono;
    }

    public boolean getStPrincipal() {
        return stPrincipal;
    }

    public void setStPrincipal(boolean stPrincipal) {
        this.stPrincipal = stPrincipal;
    }

    public TipoTelefono getTpTelefono() {
        return tpTelefono;
    }

    public void setTpTelefono(TipoTelefono tpTelefono) {
        this.tpTelefono = tpTelefono;
    }

    public List<MedioCnt> getMedioCntList() {
        return medioCntList;
    }

    public void setMedioCntList(List<MedioCnt> medioCntList) {
        this.medioCntList = medioCntList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Telefono))
            return false;
        Telefono that = (Telefono) o;

        if (this.idTelefono != null && that.idTelefono != null) {
            return Objects.equals(this.idTelefono, that.idTelefono);
        } else {
            return this == that;
        }
    }

    @Override
    public int hashCode() {
        return (idTelefono != null) ? idTelefono.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Telefono[ idTelefono=" + idTelefono + " ]";
    }

}
