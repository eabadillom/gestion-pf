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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "domicilios")
@NamedQueries({
    @NamedQuery(name = "Domicilios.findAll", query = "SELECT d FROM Domicilios d"),
    @NamedQuery(name = "Domicilios.findByDomCve", query = "SELECT d FROM Domicilios d WHERE d.domCve = :domCve"),
    @NamedQuery(name = "Domicilios.findByDomicilioCalle", query = "SELECT d FROM Domicilios d WHERE d.domicilioCalle = :domicilioCalle"),
    @NamedQuery(name = "Domicilios.findByDomicilioNumExt", query = "SELECT d FROM Domicilios d WHERE d.domicilioNumExt = :domicilioNumExt"),
    @NamedQuery(name = "Domicilios.findByDomicilioNumInt", query = "SELECT d FROM Domicilios d WHERE d.domicilioNumInt = :domicilioNumInt"),
    @NamedQuery(name = "Domicilios.findByDomicilioCp", query = "SELECT d FROM Domicilios d WHERE d.asentamiento.cp = :domicilioCp"),
    @NamedQuery(name = "Domicilios.findByDomicilioTel1", query = "SELECT d FROM Domicilios d WHERE d.domicilioTel1 = :domicilioTel1"),
    @NamedQuery(name = "Domicilios.findByDomicilioTel2", query = "SELECT d FROM Domicilios d WHERE d.domicilioTel2 = :domicilioTel2"),
    @NamedQuery(name = "Domicilios.findByDomicilioFax", query = "SELECT d FROM Domicilios d WHERE d.domicilioFax = :domicilioFax"),
    @NamedQuery(name = "Domicilios.findByAsentamiento", query = "SELECT d FROM Domicilios d WHERE d.asentamiento.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.pais.paisCve = :paisCve AND d.asentamiento.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.estados.estadosPK.estadoCve = :estadoCve AND d.asentamiento.asentamientoHumanoPK.ciudades.ciudadesPK.municipios.municipiosPK.municipioCve = :municipioCve AND d.asentamiento.asentamientoHumanoPK.ciudades.ciudadesPK.ciudadCve = :ciudadCve AND d.asentamiento.asentamientoHumanoPK.asentamientoCve = :asentamientoCve" )
})
public class Domicilios implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dom_cve")
    private Integer domCve;
    
    @Size(max = 100)
    @Column(name = "domicilio_calle")
    private String domicilioCalle;
    
    @Size(max = 50)
    @Column(name = "domicilio_num_ext")
    private String domicilioNumExt;
    
    @Size(max = 50)
    @Column(name = "domicilio_num_int")
    private String domicilioNumInt;
    
    @Size(max = 10)
    @Column(name = "domicilio_tel1")
    private String domicilioTel1;
    
    @Size(max = 10)
    @Column(name = "domicilio_tel2")
    private String domicilioTel2;
    
    @Size(max = 10)
    @Column(name = "domicilio_fax")
    private String domicilioFax;
    
    @Null
    @JoinColumns(value = {
        @JoinColumn(name = "pais_cve", referencedColumnName = "pais_cve"),
        @JoinColumn(name = "estado_cve", referencedColumnName = "estado_cve"),
        @JoinColumn(name = "municipio_cve", referencedColumnName = "municipio_cve"),
        @JoinColumn(name = "ciudad_cve", referencedColumnName = "ciudad_cve"),
        @JoinColumn(name = "asentamiento_cve", referencedColumnName = "asentamiento_cve")
    }, foreignKey = @ForeignKey(name = "FR_Domicilio_Asentamiento"))
    @OneToOne
    private AsentamientoHumano asentamiento;
    
    @JoinColumn(name = "domicilio_tipo_cve", referencedColumnName = "domicilio_tipo_cve")
    @ManyToOne(optional = false)
    private TiposDomicilio domicilioTipoCve;

    public Domicilios() {
    }

    public Domicilios(Integer domCve) {
        this.domCve = domCve;
    }

    public Integer getDomCve() {
        return domCve;
    }

    public void setDomCve(Integer domCve) {
        this.domCve = domCve;
    }

    public String getDomicilioCalle() {
        return domicilioCalle;
    }

    public void setDomicilioCalle(String domicilioCalle) {
        this.domicilioCalle = domicilioCalle;
    }

    public String getDomicilioNumExt() {
        return domicilioNumExt;
    }

    public void setDomicilioNumExt(String domicilioNumExt) {
        this.domicilioNumExt = domicilioNumExt;
    }

    public String getDomicilioNumInt() {
        return domicilioNumInt;
    }

    public void setDomicilioNumInt(String domicilioNumInt) {
        this.domicilioNumInt = domicilioNumInt;
    }

    public String getDomicilioTel1() {
        return domicilioTel1;
    }

    public void setDomicilioTel1(String domicilioTel1) {
        this.domicilioTel1 = domicilioTel1;
    }

    public String getDomicilioTel2() {
        return domicilioTel2;
    }

    public void setDomicilioTel2(String domicilioTel2) {
        this.domicilioTel2 = domicilioTel2;
    }

    public String getDomicilioFax() {
        return domicilioFax;
    }

    public void setDomicilioFax(String domicilioFax) {
        this.domicilioFax = domicilioFax;
    }

    public AsentamientoHumano getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(AsentamientoHumano asentamiento) {
        this.asentamiento = asentamiento;
    }

    public TiposDomicilio getDomicilioTipoCve() {
        return domicilioTipoCve;
    }

    public void setDomicilioTipoCve(TiposDomicilio domicilioTipoCve) {
        this.domicilioTipoCve = domicilioTipoCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (domCve != null ? domCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domicilios)) {
            return false;
        }
        Domicilios other = (Domicilios) object;
        if ((this.domCve == null && other.domCve != null) || (this.domCve != null && !this.domCve.equals(other.domCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Domicilios[ domCve=" + domCve + " ]";
    }
    
}
