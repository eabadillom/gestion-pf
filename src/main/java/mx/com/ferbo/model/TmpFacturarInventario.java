/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tmp_facturar_inventario")
@NamedQueries({
    @NamedQuery(name = "TmpFacturarInventario.findAll", query = "SELECT t FROM TmpFacturarInventario t"),
    @NamedQuery(name = "TmpFacturarInventario.findByUsuario", query = "SELECT t FROM TmpFacturarInventario t WHERE t.tmpFacturarInventarioPK.usuario = :usuario"),
    @NamedQuery(name = "TmpFacturarInventario.findByCteCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.tmpFacturarInventarioPK.cteCve = :cteCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByTmpCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.tmpFacturarInventarioPK.tmpCve = :tmpCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByFolio", query = "SELECT t FROM TmpFacturarInventario t WHERE t.folio = :folio"),
    @NamedQuery(name = "TmpFacturarInventario.findByFolioCliente", query = "SELECT t FROM TmpFacturarInventario t WHERE t.folioCliente = :folioCliente"),
    @NamedQuery(name = "TmpFacturarInventario.findByFechaDeposito", query = "SELECT t FROM TmpFacturarInventario t WHERE t.fechaDeposito = :fechaDeposito"),
    @NamedQuery(name = "TmpFacturarInventario.findByServicioInicio", query = "SELECT t FROM TmpFacturarInventario t WHERE t.servicioInicio = :servicioInicio"),
    @NamedQuery(name = "TmpFacturarInventario.findByServicioFin", query = "SELECT t FROM TmpFacturarInventario t WHERE t.servicioFin = :servicioFin"),
    @NamedQuery(name = "TmpFacturarInventario.findByServicioDias", query = "SELECT t FROM TmpFacturarInventario t WHERE t.servicioDias = :servicioDias"),
    @NamedQuery(name = "TmpFacturarInventario.findByServicioCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.servicioCve = :servicioCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByServicio", query = "SELECT t FROM TmpFacturarInventario t WHERE t.servicio = :servicio"),
    @NamedQuery(name = "TmpFacturarInventario.findByCantidadSer", query = "SELECT t FROM TmpFacturarInventario t WHERE t.cantidadSer = :cantidadSer"),
    @NamedQuery(name = "TmpFacturarInventario.findByCuota", query = "SELECT t FROM TmpFacturarInventario t WHERE t.cuota = :cuota"),
    @NamedQuery(name = "TmpFacturarInventario.findByCuotaDiaria", query = "SELECT t FROM TmpFacturarInventario t WHERE t.cuotaDiaria = :cuotaDiaria"),
    @NamedQuery(name = "TmpFacturarInventario.findByProductoCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.productoCve = :productoCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByProducto", query = "SELECT t FROM TmpFacturarInventario t WHERE t.producto = :producto"),
    @NamedQuery(name = "TmpFacturarInventario.findByPeso", query = "SELECT t FROM TmpFacturarInventario t WHERE t.peso = :peso"),
    @NamedQuery(name = "TmpFacturarInventario.findByUnidad", query = "SELECT t FROM TmpFacturarInventario t WHERE t.unidad = :unidad"),
    @NamedQuery(name = "TmpFacturarInventario.findByCantidad", query = "SELECT t FROM TmpFacturarInventario t WHERE t.cantidad = :cantidad"),
    @NamedQuery(name = "TmpFacturarInventario.findByUnidadCobro", query = "SELECT t FROM TmpFacturarInventario t WHERE t.unidadCobro = :unidadCobro"),
    @NamedQuery(name = "TmpFacturarInventario.findByValor", query = "SELECT t FROM TmpFacturarInventario t WHERE t.valor = :valor"),
    @NamedQuery(name = "TmpFacturarInventario.findByPartidaCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.partidaCve = :partidaCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByImporte", query = "SELECT t FROM TmpFacturarInventario t WHERE t.importe = :importe"),
    @NamedQuery(name = "TmpFacturarInventario.findByIva", query = "SELECT t FROM TmpFacturarInventario t WHERE t.iva = :iva"),
    @NamedQuery(name = "TmpFacturarInventario.findByTotal", query = "SELECT t FROM TmpFacturarInventario t WHERE t.total = :total"),
    @NamedQuery(name = "TmpFacturarInventario.findByPlazo", query = "SELECT t FROM TmpFacturarInventario t WHERE t.plazo = :plazo"),
    @NamedQuery(name = "TmpFacturarInventario.findByPlantaCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.plantaCve = :plantaCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByCamaraCve", query = "SELECT t FROM TmpFacturarInventario t WHERE t.camaraCve = :camaraCve"),
    @NamedQuery(name = "TmpFacturarInventario.findByNoTarimas", query = "SELECT t FROM TmpFacturarInventario t WHERE t.noTarimas = :noTarimas")})
public class TmpFacturarInventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TmpFacturarInventarioPK tmpFacturarInventarioPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "folio")
    private int folio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "folio_cliente")
    private String folioCliente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_deposito")
    @Temporal(TemporalType.DATE)
    private Date fechaDeposito;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_inicio")
    @Temporal(TemporalType.DATE)
    private Date servicioInicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_fin")
    @Temporal(TemporalType.DATE)
    private Date servicioFin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_dias")
    private int servicioDias;
    @Basic(optional = false)
    @NotNull
    @Column(name = "servicio_cve")
    private int servicioCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "servicio")
    private String servicio;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad_ser")
    private BigDecimal cantidadSer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota")
    private BigDecimal cuota;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuota_diaria")
    private BigDecimal cuotaDiaria;
    @Column(name = "producto_cve")
    private Integer productoCve;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "producto")
    private String producto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "peso")
    private BigDecimal peso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "unidad")
    private String unidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "unidad_cobro")
    private String unidadCobro;
    @Column(name = "valor")
    private BigDecimal valor;
    @Column(name = "partida_cve")
    private Integer partidaCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "importe")
    private BigDecimal importe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iva")
    private BigDecimal iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigDecimal total;
    @Column(name = "plazo")
    private Integer plazo;
    @Column(name = "planta_cve")
    private Integer plantaCve;
    @Column(name = "camara_cve")
    private Integer camaraCve;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_tarimas")
    private BigDecimal noTarimas;

    public TmpFacturarInventario() {
    }

    public TmpFacturarInventario(TmpFacturarInventarioPK tmpFacturarInventarioPK) {
        this.tmpFacturarInventarioPK = tmpFacturarInventarioPK;
    }

    public TmpFacturarInventario(TmpFacturarInventarioPK tmpFacturarInventarioPK, int folio, String folioCliente, Date fechaDeposito, Date servicioInicio, Date servicioFin, int servicioDias, int servicioCve, String servicio, BigDecimal cantidadSer, BigDecimal cuota, BigDecimal cuotaDiaria, String producto, BigDecimal peso, String unidad, BigDecimal cantidad, String unidadCobro, BigDecimal importe, BigDecimal iva, BigDecimal total, BigDecimal noTarimas) {
        this.tmpFacturarInventarioPK = tmpFacturarInventarioPK;
        this.folio = folio;
        this.folioCliente = folioCliente;
        this.fechaDeposito = fechaDeposito;
        this.servicioInicio = servicioInicio;
        this.servicioFin = servicioFin;
        this.servicioDias = servicioDias;
        this.servicioCve = servicioCve;
        this.servicio = servicio;
        this.cantidadSer = cantidadSer;
        this.cuota = cuota;
        this.cuotaDiaria = cuotaDiaria;
        this.producto = producto;
        this.peso = peso;
        this.unidad = unidad;
        this.cantidad = cantidad;
        this.unidadCobro = unidadCobro;
        this.importe = importe;
        this.iva = iva;
        this.total = total;
        this.noTarimas = noTarimas;
    }

    public TmpFacturarInventario(String usuario, int cteCve, int tmpCve) {
        this.tmpFacturarInventarioPK = new TmpFacturarInventarioPK(usuario, cteCve, tmpCve);
    }

    public TmpFacturarInventarioPK getTmpFacturarInventarioPK() {
        return tmpFacturarInventarioPK;
    }

    public void setTmpFacturarInventarioPK(TmpFacturarInventarioPK tmpFacturarInventarioPK) {
        this.tmpFacturarInventarioPK = tmpFacturarInventarioPK;
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getFolioCliente() {
        return folioCliente;
    }

    public void setFolioCliente(String folioCliente) {
        this.folioCliente = folioCliente;
    }

    public Date getFechaDeposito() {
        return fechaDeposito;
    }

    public void setFechaDeposito(Date fechaDeposito) {
        this.fechaDeposito = fechaDeposito;
    }

    public Date getServicioInicio() {
        return servicioInicio;
    }

    public void setServicioInicio(Date servicioInicio) {
        this.servicioInicio = servicioInicio;
    }

    public Date getServicioFin() {
        return servicioFin;
    }

    public void setServicioFin(Date servicioFin) {
        this.servicioFin = servicioFin;
    }

    public int getServicioDias() {
        return servicioDias;
    }

    public void setServicioDias(int servicioDias) {
        this.servicioDias = servicioDias;
    }

    public int getServicioCve() {
        return servicioCve;
    }

    public void setServicioCve(int servicioCve) {
        this.servicioCve = servicioCve;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public BigDecimal getCantidadSer() {
        return cantidadSer;
    }

    public void setCantidadSer(BigDecimal cantidadSer) {
        this.cantidadSer = cantidadSer;
    }

    public BigDecimal getCuota() {
        return cuota;
    }

    public void setCuota(BigDecimal cuota) {
        this.cuota = cuota;
    }

    public BigDecimal getCuotaDiaria() {
        return cuotaDiaria;
    }

    public void setCuotaDiaria(BigDecimal cuotaDiaria) {
        this.cuotaDiaria = cuotaDiaria;
    }

    public Integer getProductoCve() {
        return productoCve;
    }

    public void setProductoCve(Integer productoCve) {
        this.productoCve = productoCve;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadCobro() {
        return unidadCobro;
    }

    public void setUnidadCobro(String unidadCobro) {
        this.unidadCobro = unidadCobro;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(Integer partidaCve) {
        this.partidaCve = partidaCve;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public Integer getPlantaCve() {
        return plantaCve;
    }

    public void setPlantaCve(Integer plantaCve) {
        this.plantaCve = plantaCve;
    }

    public Integer getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(Integer camaraCve) {
        this.camaraCve = camaraCve;
    }

    public BigDecimal getNoTarimas() {
        return noTarimas;
    }

    public void setNoTarimas(BigDecimal noTarimas) {
        this.noTarimas = noTarimas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tmpFacturarInventarioPK != null ? tmpFacturarInventarioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TmpFacturarInventario)) {
            return false;
        }
        TmpFacturarInventario other = (TmpFacturarInventario) object;
        if ((this.tmpFacturarInventarioPK == null && other.tmpFacturarInventarioPK != null) || (this.tmpFacturarInventarioPK != null && !this.tmpFacturarInventarioPK.equals(other.tmpFacturarInventarioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.TmpFacturarInventario[ tmpFacturarInventarioPK=" + tmpFacturarInventarioPK + " ]";
    }
    
}
