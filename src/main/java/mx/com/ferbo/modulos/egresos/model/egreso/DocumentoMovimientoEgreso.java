
package mx.com.ferbo.modulos.egresos.model.egreso;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mx.com.ferbo.modulos.egresos.model.Egreso;
import mx.com.ferbo.modulos.egresos.model.catsecundarios.TipoMovimientoEgreso;

@Entity
@Table(name = "documento_movimiento_egreso")
public class DocumentoMovimientoEgreso implements Serializable, Egreso <Integer, TipoMovimientoEgreso> {
    
    private static final long serialVersionUID = 1L;
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_docu_movi")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_docu_egre", nullable = false)
    private DocumentoEgreso documento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_tipo_movi", nullable = false)
    private TipoMovimientoEgreso tipoMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_pago_egre")
    private PagoEgreso pago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_carg_egre")
    private CargoEgreso cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_devo_egre")
    private DevolucionEgreso devolucion;
    
    public DocumentoMovimientoEgreso(){
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setStatus(TipoMovimientoEgreso entity) {
        // Méotodo sin implementar porque no es necesario
    }

    public DocumentoEgreso getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoEgreso documento) {
        this.documento = documento;
    }

    public TipoMovimientoEgreso getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimientoEgreso tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public PagoEgreso getPago() {
        return pago;
    }

    public void setPago(PagoEgreso pago) {
        this.pago = pago;
    }

    public CargoEgreso getCargo() {
        return cargo;
    }

    public void setCargo(CargoEgreso cargo) {
        this.cargo = cargo;
    }

    public DevolucionEgreso getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(DevolucionEgreso devolucion) {
        this.devolucion = devolucion;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DocumentoMovimientoEgreso))
            return false;
        
        DocumentoMovimientoEgreso that = (DocumentoMovimientoEgreso) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode(){
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "DocumentoMovimientoEgreso{" + "id=" + id + '}';
    }
    
    
}
