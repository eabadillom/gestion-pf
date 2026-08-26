/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "medio_cnt")
@NamedQuery(name = "MedioCnt.findAll", query = "SELECT m FROM MedioContacto m")
@NamedQuery(name = "MedioCnt.findByIdMedio", query = "SELECT m FROM MedioContacto m WHERE m.id = :idMedio")
@NamedQuery(name = "MedioCnt.findByTpMedio", query = "SELECT m FROM MedioContacto m WHERE m.tipoMedio = :tpMedio")
@NamedQuery(name = "MedioCnt.findByStMedio", query = "SELECT m FROM MedioContacto m WHERE m.statusMedio = :stMedio")
@NamedQuery(name = "MedioCnt.findByIdContacto", query = "SELECT m FROM MedioContacto m WHERE m.contacto.id = :idContacto")
public class MedioContacto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medio")
    private Integer id;

    /**Tipo de medio de contacto (T: Telefono, M: Mail)
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tp_medio")
    private String tipoMedio;

    @Basic(optional = false)
    @NotNull
    @Column(name = "st_medio")
    private boolean statusMedio;

    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    @ManyToOne
    private Contacto contacto;

    @JoinColumn(name = "id_mail", referencedColumnName = "id_mail")
    @OneToOne(cascade = CascadeType.ALL)
    private Mail mail;

    @JoinColumn(name = "id_telefono", referencedColumnName = "id_telefono")
    @OneToOne(cascade = CascadeType.ALL)
    private Telefono telefono;
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof MedioContacto))
            return false;
        MedioContacto that = (MedioContacto) o;

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
        return "mx.com.ferbo.model.MedioCnt[ idMedio=" + id + " ]";
    }
    
    public MedioContacto() {
    }

    public MedioContacto(Integer id) {
        this.id = id;
    }

    public MedioContacto(Integer id, String tipoMedio, boolean statusMedio) {
        this.id = id;
        this.tipoMedio = tipoMedio;
        this.statusMedio = statusMedio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoMedio() {
        return tipoMedio;
    }

    public void setTipoMedio(String tipoMedio) {
        this.tipoMedio = tipoMedio;
    }

    public boolean getStatusMedio() {
        return statusMedio;
    }

    public void setStatusMedio(boolean statusMedio) {
        this.statusMedio = statusMedio;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }
}
