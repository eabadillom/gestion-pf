package mx.com.ferbo.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pago")
@NamedQueries({
        @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p"),
        @NamedQuery(name = "Pago.findById", query = "SELECT p FROM Pago p WHERE p.id = :id"),
        @NamedQuery(name = "Pago.findByMonto", query = "SELECT p FROM Pago p WHERE p.monto = :monto"),
        @NamedQuery(name = "Pago.findByFecha", query = "SELECT p FROM Pago p WHERE p.fecha = :fecha"),
        @NamedQuery(name = "Pago.findByReferencia", query = "SELECT p FROM Pago p WHERE p.referencia = :referencia"),
        @NamedQuery(name = "Pago.findByFacturaId", query = "SELECT p FROM Pago p WHERE p.factura.id = :facturaId"),
        @NamedQuery(name = "Pago.findByClienteFechas", query = "SELECT p FROM Pago p WHERE (p.factura.cliente.cteCve = :cteCve OR :cteCve IS NULL) AND (p.fecha BETWEEN :startDate AND :endDate)"),
        @NamedQuery(name = "Pago.findByFacturaFechas", query = "SELECT p FROM Pago p WHERE (p.factura.id = :idFactura OR :idFactura IS NULL) AND (p.fecha BETWEEN :startDate AND :endDate) AND p.factura.metodoPago = :metodoPago ORDER BY p.fecha ASC"),
        @NamedQuery(name = "Pago.findByParametros", query = "SELECT p FROM Pago p WHERE (p.factura.cliente.cteCve = :cteCve OR :cteCve IS NULL) AND (p.fecha BETWEEN :startDate AND :endDate) AND p.factura.metodoPago = :metodoPago")
})
public class Pago implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto")
    private BigDecimal monto;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Size(max = 20)
    @Column(name = "referencia")
    private String referencia;
    //El campo cheque se va a mostrar como "referencia" en la pantalla de usuario.
    
    @Basic(optional = true)
    @JoinColumn(name = "banco", referencedColumnName = "id")
    @ManyToOne
    private Bancos banco;
    
    @JoinColumn(name = "factura", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Factura factura;
    
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoPago tipo;
    
    @JoinColumn(name = "cd_comp_pago", referencedColumnName = "cd_comp_pago")
    @ManyToOne(optional = true)
    private ComplementoPago complementoPago;
    
    @Basic(optional = true)
    @Column(name = "nu_parcialidad")
    private Integer parcialidad;

    public Pago() {
    }

    public Pago(Integer id) {
        this.id = id;
    }

    public Pago(Integer id, BigDecimal monto, Date fecha) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Bancos getBanco() {
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public TipoPago getTipo() {
        return tipo;
    }

    public void setTipo(TipoPago tipo) {
        this.tipo = tipo;
    }

    public ComplementoPago getComplementoPago() {
        return complementoPago;
    }

    public void setComplementoPago(ComplementoPago complementoPago) {
        this.complementoPago = complementoPago;
    }

    public Integer getParcialidad() {
        return parcialidad;
    }

    public void setParcialidad(Integer parcialidad) {
        this.parcialidad = parcialidad;
    }

    @Override
    public int hashCode() {
        if (this.id == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        final Pago other = (Pago) object;
        if(this.id == null || other.id == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
       
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public Pago clone() throws CloneNotSupportedException {
        Pago pago = null;
        
        pago = new Pago();
        pago.setId(this.id == null ? null : new Integer(this.id));
        pago.setMonto(this.monto);
        pago.setFecha(this.fecha);
        pago.setReferencia(this.referencia);
        pago.setFactura(this.factura == null ? null : this.factura);
        pago.setTipo(this.tipo);
        pago.setComplementoPago(this.complementoPago == null ? null : this.complementoPago);
        pago.setParcialidad(this.parcialidad == null ? null : this.parcialidad);
        
        return pago;
    }

    @Override
    public String toString() {
        return "mx.com.ferbo.model.Pago[ id=" + id + " ]";
    }

}
