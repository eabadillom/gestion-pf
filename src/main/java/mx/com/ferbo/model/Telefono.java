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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "telefono")
@NamedQuery(name = "Telefono.findAll", query = "SELECT t FROM Telefono t")
@NamedQuery(name = "Telefono.findByIdTelefono", query = "SELECT t FROM Telefono t WHERE t.id = :idTelefono")
@NamedQuery(name = "Telefono.findByNbTelefono", query = "SELECT t FROM Telefono t WHERE t.descripcion = :nbTelefono")
@NamedQuery(name = "Telefono.findByStPrincipal", query = "SELECT t FROM Telefono t WHERE t.principal = :stPrincipal")
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_telefono")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "nb_telefono")
    private String descripcion;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "st_principal")
    private boolean principal;
    
    @JoinColumn(name = "tp_telefono", referencedColumnName = "tp_telefono")
    @ManyToOne(optional = false)
    private TipoTelefono tipoTelefono;
    
//    @OneToMany(mappedBy = "telefono")
//    private List<MedioContacto> mediosContacto;
//    @OneToOne(optional = true)
//    private MedioContacto medioContacto;
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Telefono))
            return false;
        Telefono that = (Telefono) o;

        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        } else {
            return this == that;
        }
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Telefono[ idTelefono=" + id + " ]";
    }

    public Telefono() {
    }

    public Telefono(Integer idTelefono) {
        this.id = idTelefono;
    }

    public Telefono(Integer id, String descripcion, boolean principal) {
        this.id = id;
        this.descripcion = descripcion;
        this.principal = principal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public TipoTelefono getTipoTelefono() {
        return tipoTelefono;
    }

    public void setTipoTelefono(TipoTelefono tipoTelefono) {
        this.tipoTelefono = tipoTelefono;
    }

//    public List<MedioContacto> getMediosContacto() {
//        return mediosContacto;
//    }
//
//    public void setMedioContacto(List<MedioContacto> mediosContacto) {
//        this.mediosContacto = mediosContacto;
//    }
    
//	public MedioContacto getMedioContacto() {
//		return medioContacto;
//	}
//
//	public void setMedioContacto(MedioContacto medioContacto) {
//		this.medioContacto = medioContacto;
//	}

}
