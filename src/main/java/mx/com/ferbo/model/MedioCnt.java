/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "medio_cnt")
@NamedQueries({
    @NamedQuery(name = "MedioCnt.findAll", query = "SELECT m FROM MedioCnt m"),
    @NamedQuery(name = "MedioCnt.findByIdMedio", query = "SELECT m FROM MedioCnt m WHERE m.idMedio = :idMedio"),
    @NamedQuery(name = "MedioCnt.findByTpMedio", query = "SELECT m FROM MedioCnt m WHERE m.tpMedio = :tpMedio"),
    @NamedQuery(name = "MedioCnt.findByStMedio", query = "SELECT m FROM MedioCnt m WHERE m.stMedio = :stMedio")})
public class MedioCnt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_medio")
    private Integer idMedio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tp_medio")
    private String tpMedio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "st_medio")
    private boolean stMedio;
    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    @ManyToOne(optional = false)
    private Contacto idContacto;
    @JoinColumn(name = "id_mail", referencedColumnName = "id_mail")
    @ManyToOne
    private Mail idMail;
    @JoinColumn(name = "id_telefono", referencedColumnName = "id_telefono")
    @ManyToOne
    private Telefono idTelefono;

    public MedioCnt() {
    }

    public MedioCnt(Integer idMedio) {
        this.idMedio = idMedio;
    }

    public MedioCnt(Integer idMedio, String tpMedio, boolean stMedio) {
        this.idMedio = idMedio;
        this.tpMedio = tpMedio;
        this.stMedio = stMedio;
    }

    public Integer getIdMedio() {
        return idMedio;
    }

    public void setIdMedio(Integer idMedio) {
        this.idMedio = idMedio;
    }

    public String getTpMedio() {
        return tpMedio;
    }

    public void setTpMedio(String tpMedio) {
        this.tpMedio = tpMedio;
    }

    public boolean getStMedio() {
        return stMedio;
    }

    public void setStMedio(boolean stMedio) {
        this.stMedio = stMedio;
    }

    public Contacto getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Contacto idContacto) {
        this.idContacto = idContacto;
    }

    public Mail getIdMail() {
        return idMail;
    }

    public void setIdMail(Mail idMail) {
        this.idMail = idMail;
    }

    public Telefono getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(Telefono idTelefono) {
        this.idTelefono = idTelefono;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedio != null ? idMedio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedioCnt)) {
            return false;
        }
        MedioCnt other = (MedioCnt) object;
        if ((this.idMedio == null && other.idMedio != null) || (this.idMedio != null && !this.idMedio.equals(other.idMedio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.MedioCnt[ idMedio=" + idMedio + " ]";
    }
    
}
