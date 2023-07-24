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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "contacto")
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c"),
    @NamedQuery(name = "Contacto.findByIdContacto", query = "SELECT c FROM Contacto c WHERE c.idContacto = :idContacto"),
    @NamedQuery(name = "Contacto.findByNbNombre", query = "SELECT c FROM Contacto c WHERE c.nbNombre = :nbNombre"),
    @NamedQuery(name = "Contacto.findByNbApellido1", query = "SELECT c FROM Contacto c WHERE c.nbApellido1 = :nbApellido1"),
    @NamedQuery(name = "Contacto.findByNbApellido2", query = "SELECT c FROM Contacto c WHERE c.nbApellido2 = :nbApellido2")})
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contacto")
    private Integer idContacto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_nombre")
    private String nbNombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_apellido_1")
    private String nbApellido1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nb_apellido_2")
    private String nbApellido2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContacto")
    private List<ClienteContacto> clienteContactoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContacto")
    private List<MedioCnt> medioCntList;

    public Contacto() {
    }

    public Contacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Contacto(Integer idContacto, String nbNombre, String nbApellido1, String nbApellido2) {
        this.idContacto = idContacto;
        this.nbNombre = nbNombre;
        this.nbApellido1 = nbApellido1;
        this.nbApellido2 = nbApellido2;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getNbNombre() {
        return nbNombre;
    }

    public void setNbNombre(String nbNombre) {
        this.nbNombre = nbNombre;
    }

    public String getNbApellido1() {
        return nbApellido1;
    }

    public void setNbApellido1(String nbApellido1) {
        this.nbApellido1 = nbApellido1;
    }

    public String getNbApellido2() {
        return nbApellido2;
    }

    public void setNbApellido2(String nbApellido2) {
        this.nbApellido2 = nbApellido2;
    }

    public List<ClienteContacto> getClienteContactoList() {
        return clienteContactoList;
    }

    public void setClienteContactoList(List<ClienteContacto> clienteContactoList) {
        this.clienteContactoList = clienteContactoList;
    }
    
    public List<MedioCnt> getMedioCntList() {
        return medioCntList;
    }

    public void setMedioCntList(List<MedioCnt> medioCntList) {
        this.medioCntList = medioCntList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContacto != null ? idContacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.idContacto == null && other.idContacto != null) || (this.idContacto != null && !this.idContacto.equals(other.idContacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Contacto[ idContacto=" + idContacto + " ]";
    }
    
}
