/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "SERIE_NOTA")
@NamedQueries({
    @NamedQuery(name = "SerieNota.findAll", query = "SELECT s FROM SerieNota s"),
    @NamedQuery(name = "SerieNota.findById", query = "SELECT s FROM SerieNota s WHERE s.id = :id"),
    @NamedQuery(name = "SerieNota.findByFechaInicio", query = "SELECT s FROM SerieNota s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SerieNota.findByNumeroInicial", query = "SELECT s FROM SerieNota s WHERE s.numeroInicial = :numeroInicial"),
    @NamedQuery(name = "SerieNota.findByNumeroActual", query = "SELECT s FROM SerieNota s WHERE s.numeroActual = :numeroActual"),
    @NamedQuery(name = "SerieNota.findByNumeroFinal", query = "SELECT s FROM SerieNota s WHERE s.numeroFinal = :numeroFinal"),
    @NamedQuery(name = "SerieNota.findActivas", query = "SELECT s FROM SerieNota s WHERE s.statusSerie.id = 1 ")})
public class SerieNota implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Column(name = "NUMERO_INICIAL")
    private Integer numeroInicial;
    @Column(name = "NUMERO_ACTUAL")
    private Integer numeroActual;
    @Column(name = "NUMERO_FINAL")
    private Integer numeroFinal;
    @JoinColumn(name = "STATUS_SERIE", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StatusSerie statusSerie;

    public SerieNota() {
    }

    public SerieNota(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Integer getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(Integer numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public Integer getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(Integer numeroActual) {
        this.numeroActual = numeroActual;
    }

    public Integer getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(Integer numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public StatusSerie getStatusSerie() {
        return statusSerie;
    }

    public void setStatusSerie(StatusSerie statusSerie) {
        this.statusSerie = statusSerie;
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
        if (!(object instanceof SerieNota)) {
            return false;
        }
        SerieNota other = (SerieNota) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.SerieNota[ id=" + id + " ]";
    }
    
}
