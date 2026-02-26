package mx.com.ferbo.model.egresos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import mx.com.ferbo.model.categresos.StatusCargoEgreso;
import mx.com.ferbo.model.categresos.TipoCargoEgreso;

@NamedQueries({
    @NamedQuery(name = "CargoEgreso.findAllByImporteEgreso", query = "SELECT ce FROM CargoEgreso ce WHERE ce.importeEgreso.id = :idImporteEgreso"),
    @NamedQuery(name = "CargoEgreso.findAllByImporteEgresoYStatus", query = "SELECT ce FROM CargoEgreso ce WHERE ce.importeEgreso.id = :idImporteEgreso AND ce.status.nombre = :status")
})
@Entity
@Table(name = "cargo_egreso")
public class CargoEgreso implements Serializable, Egreso<StatusCargoEgreso> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_carg_egre")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "im_carg", precision = 12, scale = 2)
    private BigDecimal importeCargo;

    @Basic(optional = true)
    @Column(name = "im_iva", precision = 12, scale = 2)
    private BigDecimal importeIVA;

    @Basic(optional = true)
    @Column(name = "im_ieps", precision = 12, scale = 2)
    private BigDecimal importeIEPS;

    @Basic(optional = true)
    @Column(name = "pc_tasa", precision = 7, scale = 4)
    private BigDecimal porcentajeTasa;

    @Basic(optional = true)
    @Column(name = "nu_dias")
    private Integer numeroDias;

    @Basic(optional = false)
    @Column(name = "fh_calculo")
    private Date fechaCalculo;

    @Basic(optional = false)
    @Column(name = "fh_aplicacion")
    private Date fechaAplicacion;
    
    @Basic(optional = false)
    @Column(name = "fh_alta")
    private Date fechaAlta;
    
    @Basic(optional = false)
    @Column(name = "fh_modi")
    private Date fechaModificacion;

    @Basic(optional = true)
    @Column(name = "tx_obser", nullable = true, length = 250)
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_pago_egre")
    private PagoEgreso pagoEgreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_impo_egre", nullable = false)
    private ImporteEgreso importeEgreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_carg", nullable = false)
    private TipoCargoEgreso tipoCargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_status_carg_egre", nullable = false)
    private StatusCargoEgreso status;
    
    @OneToMany(mappedBy = "cargo")
    private List<DocumentoMovimientoEgreso> movimientos;

    public CargoEgreso() {
        // Construcctor sin parametros
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getImporteCargo() {
        return importeCargo;
    }

    public void setImporteCargo(BigDecimal importeCargo) {
        this.importeCargo = importeCargo;
    }

    public BigDecimal getImporteIVA() {
        return importeIVA;
    }

    public void setImporteIVA(BigDecimal importeIVA) {
        this.importeIVA = importeIVA;
    }

    public BigDecimal getImporteIEPS() {
        return importeIEPS;
    }

    public void setImporteIEPS(BigDecimal importeIEPS) {
        this.importeIEPS = importeIEPS;
    }

    public BigDecimal getPorcentajeTasa() {
        return porcentajeTasa;
    }

    public void setPorcentajeTasa(BigDecimal porcentajeTasa) {
        this.porcentajeTasa = porcentajeTasa;
    }

    public Integer getNumeroDias() {
        return numeroDias;
    }

    public void setNumeroDias(Integer numeroDias) {
        this.numeroDias = numeroDias;
    }

    public Date getFechaCalculo() {
        return fechaCalculo;
    }

    public void setFechaCalculo(Date fechaCalculo) {
        this.fechaCalculo = fechaCalculo;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public PagoEgreso getPagoEgreso() {
        return pagoEgreso;
    }

    public void setPagoEgreso(PagoEgreso pagoEgreso) {
        this.pagoEgreso = pagoEgreso;
    }

    public TipoCargoEgreso getTipoCargo() {
        return tipoCargo;
    }

    public void setTipoCargo(TipoCargoEgreso tipoCargo) {
        this.tipoCargo = tipoCargo;
    }

    public ImporteEgreso getImporteEgreso() {
        return importeEgreso;
    }

    public void setImporteEgreso(ImporteEgreso importeEgreso) {
        this.importeEgreso = importeEgreso;
    }

    public StatusCargoEgreso getStatus() {
        return status;
    }

    @Override
    public void setStatus(StatusCargoEgreso status) {
        this.status = status;
    }

    public List<DocumentoMovimientoEgreso> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DocumentoMovimientoEgreso> movimientos) {
        this.movimientos = movimientos;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CargoEgreso)) {
            return false;
        }

        CargoEgreso that = (CargoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "CargoEgreso{" + "id=" + id + ", importeCargo=" + importeCargo + ", importeIVA=" + importeIVA + ", importeIEPS=" + importeIEPS + ", porcentajeTasa=" + porcentajeTasa + ", numeroDias=" + numeroDias + ", fechaCalculo=" + fechaCalculo + ", fechaAplicacion=" + fechaAplicacion + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion + ", observaciones=" + observaciones + '}';
    }

}
