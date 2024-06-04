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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "cliente_domicilios")
@NamedQueries({
        @NamedQuery(name = "ClienteDomicilios.findAll", query = "SELECT c FROM ClienteDomicilios c"),
        @NamedQuery(name = "ClienteDomicilios.findById", query = "SELECT c FROM ClienteDomicilios c WHERE c.id = :id"),
        @NamedQuery(name = "ClienteDomicilios.findByCliente", query = "SELECT c FROM ClienteDomicilios c WHERE c.cteCve.cteCve = :cteCve"),
        @NamedQuery(name = "ClienteDomicilios.findByClienteDomFiscal", query = "SELECT c FROM ClienteDomicilios c WHERE c.domicilios.domicilioTipoCve.domicilioTipoCve = 1 AND c.cteCve.cteCve = :cteCve"),
        @NamedQuery(name = "ClienteDomicilios.findByClienteTipoDomicilio", query = "SELECT c FROM ClienteDomicilios c WHERE c.cteCve.cteCve = :cteCve AND c.domicilios.domicilioTipoCve.domicilioTipoCve = :tipoDomicilioCve")})

public class ClienteDomicilios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "CTE_CVE", referencedColumnName = "CTE_CVE")
    @ManyToOne(optional = false)
    private Cliente cteCve;
    @JoinColumns({
            @JoinColumn(name = "domicilio_tipo_cve", referencedColumnName = "domicilio_tipo_cve"),
            @JoinColumn(name = "dom_cve", referencedColumnName = "dom_cve") })
    @ManyToOne(optional = false)
    private Domicilios domicilios;

    public ClienteDomicilios() {
    }

    public ClienteDomicilios(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public Domicilios getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(Domicilios domicilios) {
        this.domicilios = domicilios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClienteDomicilios)) {
            return false;
        }
        ClienteDomicilios other = (ClienteDomicilios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.ClienteDomicilios[ id=" + id + " ]";
    }

}
