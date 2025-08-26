package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "venta")
@NamedQueries({ 
    @NamedQuery(name = "Ventas.findByAll", query = "SELECT v FROM Ventas v"),
    @NamedQuery(name = "Ventas.findById", query = "SELECT v FROM Ventas v WHERE v.idVentas = :id"),
    @NamedQuery(name = "Ventas.findByfecha", query = "SELECT v FROM Ventas v WHERE v.fecha= :fecha"),
    @NamedQuery(name = "Ventas.findByCliente", query = "SELECT v FROM Ventas v WHERE v.cteCve = :cliente"),
    @NamedQuery(name = "Ventas.findByStatus", query = "SELECT v FROM Ventas v WHERE v.status = :status"),
    @NamedQuery(name = "Ventas.findByPlanta", query = "SELECT v FROM Ventas v WHERE v.cdEmisor = :emisor") 
})
public class Ventas implements Serializable 
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_venta")
    private Integer idVentas;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    Date fecha;

    @Column(name = "concepto")
    @NotNull
    private String concepto;

    @JoinColumn(name = "id_cliente", referencedColumnName = "CTE_CVE")
    @ManyToOne
    private Cliente cteCve;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "monto_letra")
    private String montoLetra;

    @Column(name = "status")
    private boolean status;

    @JoinColumn(name = "id_emisor", referencedColumnName = "cd_emisor")
    @ManyToOne
    private EmisoresCFDIS cdEmisor;

    public Integer getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(Integer idVentas) {
        this.idVentas = idVentas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Cliente getCteCve() {
        return cteCve;
    }

    public void setCteCve(Cliente cteCve) {
        this.cteCve = cteCve;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMontoLetra() {
        return montoLetra;
    }

    public void setMontoLetra(String montoLetra) {
        this.montoLetra = montoLetra;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public EmisoresCFDIS getCdEmisor() {
        return cdEmisor;
    }

    public void setCdEmisor(EmisoresCFDIS cdEmisor) {
        this.cdEmisor = cdEmisor;
    }
    
}
