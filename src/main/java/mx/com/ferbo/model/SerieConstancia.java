/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "serie_constancia")
@NamedQueries({
    @NamedQuery(name = "SerieConstancia.findAll", query = "SELECT s FROM SerieConstancia s ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByClienteTpSeriePlanta", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.cliente.cteCve = :idCliente AND s.serieConstanciaPK.tpSerie = :tpSerie AND s.serieConstanciaPK.planta.plantaCve = :idPlanta ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByClienteAndPlanta", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.cliente.cteCve = :idCliente AND s.serieConstanciaPK.planta.plantaCve = :idPlanta ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByClientePlanta", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.cliente.cteCve = :idCliente AND s.serieConstanciaPK.tpSerie = :tpSerie AND s.serieConstanciaPK.planta.plantaCve = :idPlanta ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByIdCliente", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.cliente.cteCve = :idCliente ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByTpSerie", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.tpSerie = :tpSerie ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByNuSerie", query = "SELECT s FROM SerieConstancia s WHERE s.nuSerie = :nuSerie ORDER BY s.serieConstanciaPK.cliente, s.serieConstanciaPK.planta, s.serieConstanciaPK.tpSerie")})
public class SerieConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected SerieConstanciaPK serieConstanciaPK;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nu_serie")
    private int nuSerie;
    
    public SerieConstancia() {
    }

    public SerieConstancia(SerieConstanciaPK serieConstanciaPK) {
        this.serieConstanciaPK = serieConstanciaPK;
    }

    public SerieConstancia(SerieConstanciaPK serieConstanciaPK, int nuSerie) {
        this.serieConstanciaPK = serieConstanciaPK;
        this.nuSerie = nuSerie;
    }

    public SerieConstancia(Cliente cliente, String tpSerie, Planta planta) {
        this.serieConstanciaPK = new SerieConstanciaPK(cliente, tpSerie, planta);
    }

    public SerieConstanciaPK getSerieConstanciaPK() {
        return serieConstanciaPK;
    }

    public void setSerieConstanciaPK(SerieConstanciaPK serieConstanciaPK) {
        this.serieConstanciaPK = serieConstanciaPK;
    }

    public int getNuSerie() {
        return nuSerie;
    }

    public void setNuSerie(int nuSerie) {
        this.nuSerie = nuSerie;
    }
    

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (serieConstanciaPK != null ? serieConstanciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SerieConstancia)) {
            return false;
        }
        SerieConstancia other = (SerieConstancia) object;
        if ((this.serieConstanciaPK == null && other.serieConstanciaPK != null) || (this.serieConstanciaPK != null && !this.serieConstanciaPK.equals(other.serieConstanciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.SerieConstancia[ serieConstanciaPK=" + serieConstanciaPK + " ]";
    }
    
}
