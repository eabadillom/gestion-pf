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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "serie_constancia")
@NamedQueries({
    @NamedQuery(name = "SerieConstancia.findAll", query = "SELECT s FROM SerieConstancia s"),
    @NamedQuery(name = "SerieConstancia.findByIdCliente", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.cliente.cteCve = :idCliente"),
    @NamedQuery(name = "SerieConstancia.findByTpSerie", query = "SELECT s FROM SerieConstancia s WHERE s.serieConstanciaPK.tpSerie = :tpSerie"),
    @NamedQuery(name = "SerieConstancia.findByNuSerie", query = "SELECT s FROM SerieConstancia s WHERE s.nuSerie = :nuSerie")})
public class SerieConstancia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    protected SerieConstanciaPK serieConstanciaPK;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nu_serie")
    private int nuSerie;
    
    @JoinColumn(name = "id_planta", referencedColumnName = "PLANTA_CVE")
    @ManyToOne
    private Planta idPlanta;
    

    public SerieConstancia() {
    }

    public SerieConstancia(SerieConstanciaPK serieConstanciaPK) {
        this.serieConstanciaPK = serieConstanciaPK;
    }

    public SerieConstancia(SerieConstanciaPK serieConstanciaPK, int nuSerie) {
        this.serieConstanciaPK = serieConstanciaPK;
        this.nuSerie = nuSerie;
    }

    public SerieConstancia(Cliente cliente, String tpSerie) {
        this.serieConstanciaPK = new SerieConstanciaPK(cliente, tpSerie);
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
    

    public Planta getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(Planta idPlanta) {
		this.idPlanta = idPlanta;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (serieConstanciaPK != null ? serieConstanciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
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
