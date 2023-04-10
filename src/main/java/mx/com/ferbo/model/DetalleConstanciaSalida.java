/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gabriel Moreno <gabrielmos0309@gmail.com>
 */
@Entity
@Table(name = "DETALLE_CONSTANCIA_SALIDA")
@NamedQueries({
    @NamedQuery(name = "DetalleConstanciaSalida.findAll", query = "SELECT d FROM DetalleConstanciaSalida d"),
    @NamedQuery(name = "DetalleConstanciaSalida.findById", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByPartidaCve", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.partidaCve.partidaCve = :partidaCve"),//nueva
    @NamedQuery(name = "DetalleConstanciaSalida.findByCamaraCve", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.camaraCve = :camaraCve"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByCantidad", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByPeso", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.peso = :peso"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByUnidad", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.unidad = :unidad"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByProducto", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.producto = :producto"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByFolioEntrada", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.folioEntrada = :folioEntrada"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByCamaraCadena", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.camaraCadena = :camaraCadena"),
    //@NamedQuery(name = "DetalleConstanciaSalida.findByDetPartCve", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.detPartCve = :detPartCve"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByTemperatura", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.temperatura = :temperatura"),
    @NamedQuery(name = "DetalleConstanciaSalida.findByParams", query = "SELECT d FROM DetalleConstanciaSalida d WHERE d.partidaCve.partidaCve = :partidaCve AND d.folioEntrada =:folioEntrada AND d.producto = :producto") })
public class DetalleConstanciaSalida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CAMARA_CVE")
    private int camaraCve;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "PESO")
    private BigDecimal peso;
    @Size(max = 10)
    @Column(name = "UNIDAD")
    private String unidad;
    @Size(max = 150)
    @Column(name = "PRODUCTO")
    private String producto;
    @Size(max = 10)
    @Column(name = "FOLIO_ENTRADA")
    private String folioEntrada;
    @Size(max = 50)
    @Column(name = "CAMARA_CADENA")
    private String camaraCadena;
    @Size(max = 6)
    @Column(name = "TEMPERATURA")
    private String temperatura;
    @JoinColumn(name = "CONSTANCIA_CVE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ConstanciaSalida constanciaCve;
    @JoinColumns({
        @JoinColumn(name = "DET_PART_CVE", referencedColumnName = "DET_PART_CVE"),
        @JoinColumn(name = "PARTIDA_CVE", referencedColumnName = "PARTIDA_CVE")})
    @ManyToOne(optional = false)
    private DetallePartida detallePartida;
    @JoinColumn(name = "PARTIDA_CVE", referencedColumnName = "PARTIDA_CVE", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Partida partidaCve;

    public DetalleConstanciaSalida() {
    }

    public DetalleConstanciaSalida(Integer id) {
        this.id = id;
    }

    public DetalleConstanciaSalida(Integer id, int camaraCve, BigDecimal peso) {
        this.id = id;
        this.camaraCve = camaraCve;
        this.peso = peso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCamaraCve() {
        return camaraCve;
    }

    public void setCamaraCve(int camaraCve) {
        this.camaraCve = camaraCve;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getFolioEntrada() {
        return folioEntrada;
    }

    public void setFolioEntrada(String folioEntrada) {
        this.folioEntrada = folioEntrada;
    }

    public String getCamaraCadena() {
        return camaraCadena;
    }

    public void setCamaraCadena(String camaraCadena) {
        this.camaraCadena = camaraCadena;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public ConstanciaSalida getConstanciaCve() {
        return constanciaCve;
    }

    public void setConstanciaCve(ConstanciaSalida constanciaCve) {
        this.constanciaCve = constanciaCve;
    }

    public DetallePartida getDetallePartida() {
        return detallePartida;
    }

    public void setDetallePartida(DetallePartida detallePartida) {
        this.detallePartida = detallePartida;
    }

    public Partida getPartidaCve() {
        return partidaCve;
    }

    public void setPartidaCve(Partida partidaCve) {
        this.partidaCve = partidaCve;
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
        if (!(object instanceof DetalleConstanciaSalida)) {
            return false;
        }
        DetalleConstanciaSalida other = (DetalleConstanciaSalida) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.DetalleConstanciaSalida[ id=" + id + " ]";
    }

}
