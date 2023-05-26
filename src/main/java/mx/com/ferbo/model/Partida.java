/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "PARTIDA")
@NamedQueries({
        @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p"),
        @NamedQuery(name = "Partida.findByPartidaCve", query = "SELECT p FROM Partida p WHERE p.partidaCve = :partidaCve"),
        @NamedQuery(name = "Partida.findByPesoTotal", query = "SELECT p FROM Partida p WHERE p.pesoTotal = :pesoTotal"),
        @NamedQuery(name = "Partida.findByCantidadTotal", query = "SELECT p FROM Partida p WHERE p.cantidadTotal = :cantidadTotal"),
        @NamedQuery(name = "Partida.findByUnidadDeProductoCve", query = "SELECT p FROM Partida p WHERE p.unidadDeProductoCve = :unidadDeProductoCve"),
        @NamedQuery(name = "Partida.findByCantidadDeCobro", query = "SELECT p FROM Partida p WHERE p.cantidadDeCobro = :cantidadDeCobro"),
        @NamedQuery(name = "Partida.findByPartidaSeq", query = "SELECT p FROM Partida p WHERE p.partidaSeq = :partidaSeq"),
        @NamedQuery(name = "Partida.findByValorMercancia", query = "SELECT p FROM Partida p WHERE p.valorMercancia = :valorMercancia"),
        @NamedQuery(name = "Partida.findByRendimiento", query = "SELECT p FROM Partida p WHERE p.rendimiento = :rendimiento"),
        @NamedQuery(name = "Partida.findByNoTarimas", query = "SELECT p FROM Partida p WHERE p.noTarimas = :noTarimas"),
        @NamedQuery(name = "Partida.findByConstanciaDeDeposito", query = "SELECT p FROM Partida p WHERE p.folio.folioCliente = :folioCliente") })
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARTIDA_CVE")
    private Integer partidaCve;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "PESO_TOTAL")
    private BigDecimal pesoTotal;
    @Column(name = "CANTIDAD_TOTAL")
    private Integer cantidadTotal;
    // @Column(name = "UNIDAD_DE_PRODUCTO_CVE")
    // private Integer unidadDeProductoCve;
    @Column(name = "cantidad_de_cobro")
    private BigDecimal cantidadDeCobro;
    @Column(name = "partida_seq")
    private Integer partidaSeq;
    @Column(name = "valorMercancia")
    private BigDecimal valorMercancia;
    @Column(name = "rendimiento")
    private BigDecimal rendimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_tarimas")
    private BigDecimal noTarimas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partida")
    private List<DetallePartida> detallePartidaList;
    @JoinColumn(name = "CAMARA_CVE", referencedColumnName = "CAMARA_CVE")
    @ManyToOne
    private Camara camaraCve;
    @JoinColumn(name = "FOLIO", referencedColumnName = "FOLIO")
    @ManyToOne(optional = false)
    private ConstanciaDeDeposito folio;
    @JoinColumn(name = "unidad_de_cobro", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne
    private UnidadDeManejo unidadDeCobro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "partidaCve")
    private List<DetalleConstanciaSalida> detalleConstanciaSalidaList;

    @JoinColumn(name = "UNIDAD_DE_PRODUCTO_CVE", referencedColumnName = "UNIDAD_DE_PRODUCTO_CVE")
    @ManyToOne(optional = false)
    private UnidadDeProducto unidadDeProductoCve;
    
    public void add(DetallePartida detalle) {
    	if(this.detallePartidaList == null)
    		this.detallePartidaList = new ArrayList<DetallePartida>();
    	
    	if(detalle.getDetallePartidaPK() == null)
    		detalle.setDetallePartidaPK(new DetallePartidaPK(detallePartidaList.size(), this));
    	
    	this.detallePartidaList.add(detalle);
    }

    public void setUnidadDeProductoCve(UnidadDeProducto unidadDeProductoCve) {
        this.unidadDeProductoCve = unidadDeProductoCve;
    }

    public Partida() {
    }

    public Partida(Integer partidaCve) {
        this.partidaCve = partidaCve;
    }

    public Partida(Integer partidaCve, BigDecimal noTarimas) {
        this.partidaCve = partidaCve;
        this.noTarimas = noTarimas;
    }

    public Integer getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(Integer partidaCve) {
        this.partidaCve = partidaCve;
    }

    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public BigDecimal getCantidadDeCobro() {
        return cantidadDeCobro;
    }

    public void setCantidadDeCobro(BigDecimal cantidadDeCobro) {
        this.cantidadDeCobro = cantidadDeCobro;
    }

    public Integer getPartidaSeq() {
        return partidaSeq;
    }

    public void setPartidaSeq(Integer partidaSeq) {
        this.partidaSeq = partidaSeq;
    }

    public BigDecimal getValorMercancia() {
        return valorMercancia;
    }

    public void setValorMercancia(BigDecimal valorMercancia) {
        this.valorMercancia = valorMercancia;
    }

    public BigDecimal getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(BigDecimal rendimiento) {
        this.rendimiento = rendimiento;
    }

    public BigDecimal getNoTarimas() {
        return noTarimas;
    }

    public void setNoTarimas(BigDecimal noTarimas) {
        this.noTarimas = noTarimas;
    }

    public List<DetallePartida> getDetallePartidaList() {
        return detallePartidaList;
    }

    public void setDetallePartidaList(List<DetallePartida> detallePartidaList) {
        this.detallePartidaList = detallePartidaList;
        for(DetallePartida dp : detallePartidaList) {
        	dp.setPartida(this);
        }
    }

    public Camara getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(Camara camaraCve) {
        this.camaraCve = camaraCve;
    }

    public ConstanciaDeDeposito getFolio() {
        return folio;
    }

    public void setFolio(ConstanciaDeDeposito folio) {
        this.folio = folio;
    }

    public UnidadDeManejo getUnidadDeCobro() {
        return unidadDeCobro;
    }

    public void setUnidadDeCobro(UnidadDeManejo unidadDeCobro) {
        this.unidadDeCobro = unidadDeCobro;
    }

    public List<DetalleConstanciaSalida> getDetalleConstanciaSalidaList() {
        return detalleConstanciaSalidaList;
    }

    public void setDetalleConstanciaSalidaList(List<DetalleConstanciaSalida> detalleConstanciaSalidaList) {
        this.detalleConstanciaSalidaList = detalleConstanciaSalidaList;
    }

    public UnidadDeProducto getUnidadDeProductoCve() {
        return unidadDeProductoCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partidaCve != null ? partidaCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partida)) {
            return false;
        }
        Partida other = (Partida) object;
        if ((this.partidaCve == null && other.partidaCve != null)
                || (this.partidaCve != null && !this.partidaCve.equals(other.partidaCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Partida[ partidaCve=" + partidaCve + " ]";
    }

}
