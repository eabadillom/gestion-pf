/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "aviso")
@NamedQueries({
    @NamedQuery(name = "Aviso.findAll", query = "SELECT a FROM Aviso a"),
    @NamedQuery(name = "Aviso.findByAvisoCve", query = "SELECT a FROM Aviso a WHERE a.avisoCve = :avisoCve"),
    @NamedQuery(name = "Aviso.findByAvisoPo", query = "SELECT a FROM Aviso a WHERE a.avisoPo = :avisoPo"),
    @NamedQuery(name = "Aviso.findByAvisoPedimento", query = "SELECT a FROM Aviso a WHERE a.avisoPedimento = :avisoPedimento"),
    @NamedQuery(name = "Aviso.findByAvisoSap", query = "SELECT a FROM Aviso a WHERE a.avisoSap = :avisoSap"),
    @NamedQuery(name = "Aviso.findByAvisoLote", query = "SELECT a FROM Aviso a WHERE a.avisoLote = :avisoLote"),
    @NamedQuery(name = "Aviso.findByAvisoCaducidad", query = "SELECT a FROM Aviso a WHERE a.avisoCaducidad = :avisoCaducidad"),
    @NamedQuery(name = "Aviso.findByAvisoTarima", query = "SELECT a FROM Aviso a WHERE a.avisoTarima = :avisoTarima"),
    @NamedQuery(name = "Aviso.findByAvisoOtro", query = "SELECT a FROM Aviso a WHERE a.avisoOtro = :avisoOtro"),
    @NamedQuery(name = "Aviso.findByAvisoTemp", query = "SELECT a FROM Aviso a WHERE a.avisoTemp = :avisoTemp"),
    @NamedQuery(name = "Aviso.findByAvisoFecha", query = "SELECT a FROM Aviso a WHERE a.avisoFecha = :avisoFecha"),
    @NamedQuery(name = "Aviso.findByAvisoObservaciones", query = "SELECT a FROM Aviso a WHERE a.avisoObservaciones = :avisoObservaciones"),
    @NamedQuery(name = "Aviso.findByAvisoVigencia", query = "SELECT a FROM Aviso a WHERE a.avisoVigencia = :avisoVigencia"),
    @NamedQuery(name = "Aviso.findByAvisoValSeg", query = "SELECT a FROM Aviso a WHERE a.avisoValSeg = :avisoValSeg"),
    @NamedQuery(name = "Aviso.findByAvisoPlazo", query = "SELECT a FROM Aviso a WHERE a.avisoPlazo = :avisoPlazo"),
    @NamedQuery(name = "Aviso.findByAvisoTpFacturacion", query = "SELECT a FROM Aviso a WHERE a.avisoTpFacturacion = :avisoTpFacturacion")})
public class Aviso implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aviso_cve")
    private Integer avisoCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_po")
    private boolean avisoPo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_pedimento")
    private boolean avisoPedimento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_sap")
    private boolean avisoSap;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_lote")
    private boolean avisoLote;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_caducidad")
    private boolean avisoCaducidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_tarima")
    private boolean avisoTarima;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_otro")
    private boolean avisoOtro;
    @Size(max = 50)
    @Column(name = "aviso_temp")
    private String avisoTemp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_fecha")
    @Temporal(TemporalType.DATE)
    private Date avisoFecha;
    @Size(max = 255)
    @Column(name = "aviso_observaciones")
    private String avisoObservaciones;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_vigencia")
    private int avisoVigencia;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "aviso_val_seg")
    private BigDecimal avisoValSeg;
    @Basic(optional = false)
    @NotNull
    @Column(name = "aviso_plazo")
    private int avisoPlazo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "aviso_tp_facturacion")
    private String avisoTpFacturacion;
    @JoinColumn(name = "cte_cve", referencedColumnName = "CTE_CVE")
    @ManyToOne
    private Cliente cteCve;
    @JoinColumn(name = "planta_cve", referencedColumnName = "PLANTA_CVE")
    @ManyToOne
    private Planta plantaCve;
    @JoinColumn(name = "categoria_cve", referencedColumnName = "categoria_cve")
    @ManyToOne
    private Categoria categoriaCve;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avisoCve")
    private List<CuotaMensualServicio> cuotaMensualServicioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avisoCve")
    private List<PrecioServicio> precioServicioList;
    @OneToMany(mappedBy = "avisoCve")
    private List<ConstanciaDeDeposito> constanciaDeDepositoList;

    public Aviso() {
    }

    public Aviso(Integer avisoCve) {
        this.avisoCve = avisoCve;
    }

    public Aviso(Integer avisoCve, boolean avisoPo, boolean avisoPedimento, boolean avisoSap, boolean avisoLote, boolean avisoCaducidad, boolean avisoTarima, boolean avisoOtro, Date avisoFecha, int avisoVigencia, int avisoPlazo, String avisoTpFacturacion) {
        this.avisoCve = avisoCve;
        this.avisoPo = avisoPo;
        this.avisoPedimento = avisoPedimento;
        this.avisoSap = avisoSap;
        this.avisoLote = avisoLote;
        this.avisoCaducidad = avisoCaducidad;
        this.avisoTarima = avisoTarima;
        this.avisoOtro = avisoOtro;
        this.avisoFecha = avisoFecha;
        this.avisoVigencia = avisoVigencia;
        this.avisoPlazo = avisoPlazo;
        this.avisoTpFacturacion = avisoTpFacturacion;
    }

    public Integer getAvisoCve() {
        return avisoCve;
    }

    public void setAvisoCve(Integer avisoCve) {
        this.avisoCve = avisoCve;
    }

    public boolean getAvisoPo() {
        return avisoPo;
    }

    public void setAvisoPo(boolean avisoPo) {
        this.avisoPo = avisoPo;
    }

    public boolean getAvisoPedimento() {
        return avisoPedimento;
    }

    public void setAvisoPedimento(boolean avisoPedimento) {
        this.avisoPedimento = avisoPedimento;
    }

    public boolean getAvisoSap() {
        return avisoSap;
    }

    public void setAvisoSap(boolean avisoSap) {
        this.avisoSap = avisoSap;
    }

    public boolean getAvisoLote() {
        return avisoLote;
    }

    public void setAvisoLote(boolean avisoLote) {
        this.avisoLote = avisoLote;
    }

    public boolean getAvisoCaducidad() {
        return avisoCaducidad;
    }

    public void setAvisoCaducidad(boolean avisoCaducidad) {
        this.avisoCaducidad = avisoCaducidad;
    }

    public boolean getAvisoTarima() {
        return avisoTarima;
    }

    public void setAvisoTarima(boolean avisoTarima) {
        this.avisoTarima = avisoTarima;
    }

    public boolean getAvisoOtro() {
        return avisoOtro;
    }

    public void setAvisoOtro(boolean avisoOtro) {
        this.avisoOtro = avisoOtro;
    }

    public String getAvisoTemp() {
        return avisoTemp;
    }

    public void setAvisoTemp(String avisoTemp) {
        this.avisoTemp = avisoTemp;
    }

    public Date getAvisoFecha() {
        return avisoFecha;
    }

    public void setAvisoFecha(Date avisoFecha) {
        this.avisoFecha = avisoFecha;
    }

    public String getAvisoObservaciones() {
        return avisoObservaciones;
    }

    public void setAvisoObservaciones(String avisoObservaciones) {
        this.avisoObservaciones = avisoObservaciones;
    }

    public int getAvisoVigencia() {
        return avisoVigencia;
    }

    public void setAvisoVigencia(int avisoVigencia) {
        this.avisoVigencia = avisoVigencia;
    }

    public BigDecimal getAvisoValSeg() {
        return avisoValSeg;
    }

    public void setAvisoValSeg(BigDecimal avisoValSeg) {
        this.avisoValSeg = avisoValSeg;
    }

    public int getAvisoPlazo() {
        return avisoPlazo;
    }

    public void setAvisoPlazo(int avisoPlazo) {
        this.avisoPlazo = avisoPlazo;
    }

    public String getAvisoTpFacturacion() {
        return avisoTpFacturacion;
    }

    public void setAvisoTpFacturacion(String avisoTpFacturacion) {
        this.avisoTpFacturacion = avisoTpFacturacion;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public Planta getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(Planta plantaCve) {
        this.plantaCve = plantaCve;
    }

    public Categoria getCategoriaCve() {
        return categoriaCve;
    }

    public void setCategoriaCve(Categoria categoriaCve) {
        this.categoriaCve = categoriaCve;
    }

    public List<CuotaMensualServicio> getCuotaMensualServicioList() {
        return cuotaMensualServicioList;
    }

    public void setCuotaMensualServicioList(List<CuotaMensualServicio> cuotaMensualServicioList) {
        this.cuotaMensualServicioList = cuotaMensualServicioList;
    }

    public List<PrecioServicio> getPrecioServicioList() {
        return precioServicioList;
    }

    public void setPrecioServicioList(List<PrecioServicio> precioServicioList) {
        this.precioServicioList = precioServicioList;
    }

    public List<ConstanciaDeDeposito> getConstanciaDeDepositoList() {
        return constanciaDeDepositoList;
    }

    public void setConstanciaDeDepositoList(List<ConstanciaDeDeposito> constanciaDeDepositoList) {
        this.constanciaDeDepositoList = constanciaDeDepositoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (avisoCve != null ? avisoCve.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aviso)) {
            return false;
        }
        Aviso other = (Aviso) object;
        if ((this.avisoCve == null && other.avisoCve != null) || (this.avisoCve != null && !this.avisoCve.equals(other.avisoCve))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Aviso[ avisoCve=" + avisoCve + " ]";
    }
    
}
