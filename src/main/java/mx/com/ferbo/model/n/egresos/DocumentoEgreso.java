package mx.com.ferbo.model.n.egresos;

import java.io.Serializable;
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

import mx.com.ferbo.model.n.catalogos.TipoDocumento;

@NamedQueries({
    @NamedQuery(name = "DocumentoEgreso.findByPago", query = "SELECT de FROM DocumentoEgreso de WHERE de.pagoEgreso.id = :id"),
    @NamedQuery(name = "DocumentoEgreso.findAllByImporteEgreso", query = "SELECT de FROM DocumentoEgreso de WHERE de.pagoEgreso.importeEgreso.id = :id")
})
@Entity
@Table(name = "documento_egreso")
public class DocumentoEgreso implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_docu_egre")
    private Integer id;

    @Basic(optional = true)
    @Column(name = "nu_foli_fisc", length = 100, nullable = true)
    private String folioFiscal;

    @Basic(optional = true)
    @Column(name = "tx_tipo_docu", length = 250, nullable = true)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "fh_alta")
    private Date fechaAlta;

    @Basic(optional = false)
    @Column(name = "fh_docu")
    private Date fechaDocumento;

    @ManyToOne
    @JoinColumn(name = "cd_pago_egre")
    private PagoEgreso pagoEgreso;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cd_tipo_docu")
    private TipoDocumento tipoDocumento;

    public DocumentoEgreso(){
        // Constructor sin parametros
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolioFiscal() {
        return folioFiscal;
    }

    public void setFolioFiscal(String folioFiscal) {
        this.folioFiscal = folioFiscal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public PagoEgreso getPagoEgreso() {
        return pagoEgreso;
    }

    public void setPagoEgreso(PagoEgreso pagoEgreso) {
        this.pagoEgreso = pagoEgreso;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof DocumentoEgreso))
            return false;

        DocumentoEgreso that = (DocumentoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 32;
    }

    @Override
    public String toString() {
        return "DocumentoEgreso [id=" + id + ", folioFiscal=" + folioFiscal + ", descripcion=" + descripcion
                + ", fechaAlta=" + fechaAlta + ", fechaDocumento=" + fechaDocumento + "]";
    }

}
