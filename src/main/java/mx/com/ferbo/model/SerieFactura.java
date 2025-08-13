/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "serie_factura")
@NamedQueries({
    @NamedQuery(name = "SerieFactura.findAll", query = "SELECT s FROM SerieFactura s"),
    @NamedQuery(name = "SerieFactura.findById", query = "SELECT s FROM SerieFactura s WHERE s.id = :id"),
    @NamedQuery(name = "SerieFactura.findByFechaInicio", query = "SELECT s FROM SerieFactura s WHERE s.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "SerieFactura.findByNumeroInicial", query = "SELECT s FROM SerieFactura s WHERE s.numeroInicial = :numeroInicial"),
    @NamedQuery(name = "SerieFactura.findByNumeroActual", query = "SELECT s FROM SerieFactura s WHERE s.numeroActual = :numeroActual"),
    @NamedQuery(name = "SerieFactura.findByNumeroFinal", query = "SELECT s FROM SerieFactura s WHERE s.numeroFinal = :numeroFinal"),
    @NamedQuery(name = "SerieFactura.findByNomSerie", query = "SELECT s FROM SerieFactura s WHERE s.nomSerie = :nomSerie"),
    @NamedQuery(name = "SerieFactura.findByEmisor", query = "SELECT s FROM SerieFactura s WHERE s.statusSerie.id in (1,2) AND s.emisor.cd_emisor = :idEmisor")
})
public class SerieFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_inicial")
    private int numeroInicial;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_actual")
    private int numeroActual;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_final")
    private Integer numeroFinal;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "nom_serie")
    private String nomSerie;
    
    @JoinColumn(name = "status_serie", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StatusSerie statusSerie;
    
    @JoinColumn(name = "cd_emisor", referencedColumnName = "cd_emisor")
    @ManyToOne(optional = true)
    private EmisoresCFDIS emisor;
    
    @Override
    public int hashCode() {
        if(this.id == null)
        	return System.identityHashCode(this);
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SerieFactura)) {
            return false;
        }
        SerieFactura other = (SerieFactura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.SerieFactura[ id=" + id + " ]";
    }

    public SerieFactura() {
    }

    public SerieFactura(Integer id) {
        this.id = id;
    }

    public SerieFactura(Integer id, Date fechaInicio, int numeroInicial, int numeroActual, int numeroFinal, String nomSerie) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.numeroInicial = numeroInicial;
        this.numeroActual = numeroActual;
        this.numeroFinal = numeroFinal;
        this.nomSerie = nomSerie;
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

    public int getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(int numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public int getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(int numeroActual) {
        this.numeroActual = numeroActual;
    }

    public Integer getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(Integer numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public String getNomSerie() {
        return nomSerie;
    }

    public void setNomSerie(String nomSerie) {
        this.nomSerie = nomSerie;
    }

    public StatusSerie getStatusSerie() {
        return statusSerie;
    }

    public void setStatusSerie(StatusSerie statusSerie) {
        this.statusSerie = statusSerie;
    }
    
	public EmisoresCFDIS getEmisor() {
		return emisor;
	}
	
	public void setEmisor(EmisoresCFDIS emisor) {
		this.emisor = emisor;
	}
}
