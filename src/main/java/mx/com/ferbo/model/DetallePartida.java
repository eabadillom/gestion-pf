/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "detalle_partida")
@NamedQueries({
    @NamedQuery(name = "DetallePartida.findAll", query = "SELECT d FROM DetallePartida d"),
    @NamedQuery(name = "DetallePartida.findByDetPartCve", query = "SELECT d FROM DetallePartida d WHERE d.detallePartidaPK.detPartCve = :detPartCve"),
    @NamedQuery(name = "DetallePartida.findByPartidaCve", query = "SELECT d FROM DetallePartida d WHERE d.detallePartidaPK.partidaCve.partidaCve = :partidaCve"),
    @NamedQuery(name = "DetallePartida.findByDetPadre", query = "SELECT d FROM DetallePartida d WHERE d.detPadre = :detPadre"),
    @NamedQuery(name = "DetallePartida.findByDetPartPadre", query = "SELECT d FROM DetallePartida d WHERE d.detPartPadre = :detPartPadre"),
    @NamedQuery(name = "DetallePartida.findByCantidadUManejo", query = "SELECT d FROM DetallePartida d WHERE d.cantidadUManejo = :cantidadUManejo"),
    @NamedQuery(name = "DetallePartida.findByCantidadUMedida", query = "SELECT d FROM DetallePartida d WHERE d.cantidadUMedida = :cantidadUMedida"),
    @NamedQuery(name = "DetallePartida.findByDtpCodigo", query = "SELECT d FROM DetallePartida d WHERE d.dtpCodigo = :dtpCodigo"),
    @NamedQuery(name = "DetallePartida.findByDtpLote", query = "SELECT d FROM DetallePartida d WHERE d.dtpLote = :dtpLote"),
    @NamedQuery(name = "DetallePartida.findByDtpCaducidad", query = "SELECT d FROM DetallePartida d WHERE d.dtpCaducidad = :dtpCaducidad"),
    @NamedQuery(name = "DetallePartida.findByDtpPO", query = "SELECT d FROM DetallePartida d WHERE d.dtpPO = :dtpPO"),
    @NamedQuery(name = "DetallePartida.findByDtpMP", query = "SELECT d FROM DetallePartida d WHERE d.dtpMP = :dtpMP"),
    @NamedQuery(name = "DetallePartida.findByDtpPedimento", query = "SELECT d FROM DetallePartida d WHERE d.dtpPedimento = :dtpPedimento"),
    @NamedQuery(name = "DetallePartida.findByDtpSAP", query = "SELECT d FROM DetallePartida d WHERE d.dtpSAP = :dtpSAP"),
    @NamedQuery(name = "DetallePartida.findByDtpTarimas", query = "SELECT d FROM DetallePartida d WHERE d.dtpTarimas = :dtpTarimas")})
public class DetallePartida implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private DetallePartidaPK detallePartidaPK;
    
    @Column(name = "det_padre")
    private Integer detPadre;
    
    @Column(name = "det_part_padre")
    private Integer detPartPadre;
    
    @Column(name = "cantidad_u_manejo")
    private Integer cantidadUManejo;
    
    @Column(name = "cantidad_u_medida")
    private BigDecimal cantidadUMedida;
    
    @Size(max = 12)
    @Column(name = "dtp_codigo")
    private String dtpCodigo;
    
    @Size(max = 20)
    @Column(name = "dtp_lote")
    private String dtpLote;
    
    @Column(name = "dtp_caducidad")
    @Temporal(TemporalType.DATE)
    private Date dtpCaducidad;
    
    @Size(max = 12)
    @Column(name = "dtp_PO")
    private String dtpPO;
    
    @Size(max = 20)
    @Column(name = "dtp_MP")
    private String dtpMP;
    
    @Size(max = 13)
    @Column(name = "dtp_pedimento")
    private String dtpPedimento;
    
    @Size(max = 20)
    @Column(name = "dtp_SAP")
    private String dtpSAP;
    
    @Size(max = 15)
    @Column(name = "dtp_tarimas")
    private String dtpTarimas;
    
//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "detallePartida")
//    private DetalleConstanciaSalida detalleConstanciaSalida;
    
    @JoinColumns({
        @JoinColumn(name = "det_anterior", referencedColumnName = "DET_PART_CVE"),
        @JoinColumn(name = "det_part_anterior", referencedColumnName = "PARTIDA_CVE")})
    @ManyToOne(cascade = CascadeType.MERGE)
    private DetallePartida detallePartida;
    
    @JoinColumn(name = "tipo_mov_cve", referencedColumnName = "CLAVE")
    @ManyToOne(cascade = CascadeType.DETACH)
    private TipoMovimiento tipoMovCve;
    
    @JoinColumn(name = "u_medida_cve", referencedColumnName = "UNIDAD_DE_MANEJO_CVE")
    @ManyToOne
    private UnidadDeManejo uMedidaCve;
    
    @JoinColumn(name = "edo_inv_cve", referencedColumnName = "edo_inv_cve")
    @ManyToOne(cascade = CascadeType.DETACH)
    private EstadoInventario edoInvCve;
    
    public DetallePartida clone() throws CloneNotSupportedException {
    	return (DetallePartida) super.clone();
    }

    public DetallePartida() {
    }

    public DetallePartida(DetallePartidaPK detallePartidaPK) {
        this.detallePartidaPK = detallePartidaPK;
    }

    public DetallePartida(int detPartCve, Partida partidaCve) {
        this.detallePartidaPK = new DetallePartidaPK(detPartCve, partidaCve);
    }

    public DetallePartidaPK getDetallePartidaPK() {
        return detallePartidaPK;
    }

    public void setDetallePartidaPK(DetallePartidaPK detallePartidaPK) {
        this.detallePartidaPK = detallePartidaPK;
    }

    public Integer getDetPadre() {
        return detPadre;
    }

    public void setDetPadre(Integer detPadre) {
        this.detPadre = detPadre;
    }

    public Integer getDetPartPadre() {
        return detPartPadre;
    }

    public void setDetPartPadre(Integer detPartPadre) {
        this.detPartPadre = detPartPadre;
    }

    public Integer getCantidadUManejo() {
        return cantidadUManejo;
    }

    public void setCantidadUManejo(Integer cantidadUManejo) {
        this.cantidadUManejo = cantidadUManejo;
    }

    public BigDecimal getCantidadUMedida() {
        return cantidadUMedida;
    }

    public void setCantidadUMedida(BigDecimal cantidadUMedida) {
        this.cantidadUMedida = cantidadUMedida;
    }

    public String getDtpCodigo() {
        return dtpCodigo;
    }

    public void setDtpCodigo(String dtpCodigo) {
        this.dtpCodigo = dtpCodigo;
    }

    public String getDtpLote() {
        return dtpLote;
    }

    public void setDtpLote(String dtpLote) {
        this.dtpLote = dtpLote;
    }

    public Date getDtpCaducidad() {
        return dtpCaducidad;
    }

    public void setDtpCaducidad(Date dtpCaducidad) {
        this.dtpCaducidad = dtpCaducidad;
    }

    public String getDtpPO() {
        return dtpPO;
    }

    public void setDtpPO(String dtpPO) {
        this.dtpPO = dtpPO;
    }

    public String getDtpMP() {
        return dtpMP;
    }

    public void setDtpMP(String dtpMP) {
        this.dtpMP = dtpMP;
    }

    public String getDtpPedimento() {
        return dtpPedimento;
    }

    public void setDtpPedimento(String dtpPedimento) {
        this.dtpPedimento = dtpPedimento;
    }

    public String getDtpSAP() {
        return dtpSAP;
    }

    public void setDtpSAP(String dtpSAP) {
        this.dtpSAP = dtpSAP;
    }

    public String getDtpTarimas() {
        return dtpTarimas;
    }

    public void setDtpTarimas(String dtpTarimas) {
        this.dtpTarimas = dtpTarimas;
    }

//    public DetalleConstanciaSalida getDetalleConstanciaSalida() {
//        return detalleConstanciaSalida;
//    }
//
//    public void setDetalleConstanciaSalida(DetalleConstanciaSalida detalleConstanciaSalida) {
//        this.detalleConstanciaSalida = detalleConstanciaSalida;
//    }

    public DetallePartida getDetallePartida() {
        return detallePartida;
    }

    public void setDetallePartida(DetallePartida detallePartida) {
        this.detallePartida = detallePartida;
    }

    public TipoMovimiento getTipoMovCve() {
        return tipoMovCve;
    }

    public void setTipoMovCve(TipoMovimiento tipoMovCve) {
        this.tipoMovCve = tipoMovCve;
    }

    public UnidadDeManejo getUMedidaCve() {
        return uMedidaCve;
    }

    public void setUMedidaCve(UnidadDeManejo uMedidaCve) {
        this.uMedidaCve = uMedidaCve;
    }

    public EstadoInventario getEdoInvCve() {
        return edoInvCve;
    }

    public void setEdoInvCve(EstadoInventario edoInvCve) {
        this.edoInvCve = edoInvCve;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detallePartidaPK != null ? detallePartidaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DetallePartida)) {
            return false;
        }
        DetallePartida other = (DetallePartida) object;
        if ((this.detallePartidaPK == null && other.detallePartidaPK != null) || (this.detallePartidaPK != null && !this.detallePartidaPK.equals(other.detallePartidaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetallePartida[ detallePartidaPK=" + detallePartidaPK + " ]";
    }
    
}
