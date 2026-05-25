package mx.com.ferbo.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.validation.constraints.Size;

/**
 *
 * @author alberto
 */
@Entity
@Table(name = "salida")
@NamedQueries({
    @NamedQuery(name = "Salida.findAll", query = "SELECT s FROM Salida s"),
    @NamedQuery(name = "Salida.findById", query = "SELECT s FROM Salida s WHERE s.idSalida = :idStatus"),
    @NamedQuery(name = "Salida.findByFolioSalida", query = "SELECT s FROM Salida s WHERE s.folioSalida = :folioSalida"),
    @NamedQuery(name = "Salida.findByCliente", query = "SELECT s FROM Salida s INNER JOIN s.status st INNER JOIN s.cliente cl WHERE cl.cteCve = :idCliente AND st.clave = :clave AND s.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "Salida.findByParametros", query = "SELECT s FROM Salida s INNER JOIN s.status st INNER JOIN s.cliente cl WHERE st.clave = :clave AND s.fechaSalida = :fechaSalida AND cl.cteCve = :idCliente")
})
public class Salida implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_salida")
    private Integer idSalida;
    
    @Size(max = 15)
    @Column(name = "cd_folio_salida")
    private String folioSalida;
    
    @ManyToOne
    @JoinColumn(name = "cliente_cve", referencedColumnName = "CTE_CVE")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    private Contacto contacto;
    
    @ManyToOne
    @JoinColumn(name = "cd_status", referencedColumnName = "cd_status")
    private StatusSalida status;
    
    @Size(max = 10)
    @Column(name = "nb_placas_transporte")
    private String placasTransporte;
    
    @Size(max = 100)
    @Column(name = "nb_nombre_transportista")
    private String nombreTransportista;
    
    @Size(max = 10)
    @Column(name = "nb_observaciones")
    private String observaciones;
    
    @Column(name = "fh_salida")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaSalida;
    
    @Column(name = "tm_salida")
    private LocalTime horaSalida;
    
    @Column(name = "fh_registro")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaRegistro;
    
    @Column(name = "fh_modificacion")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaModificacion;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "salida")
    private List<SalidaDetalle> listSalidaDetalle;
    
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "salida")
    private List<ServiciosSalida> listServiciosSalida;

    public Salida() {
    }

    public Integer getIdSalida() {
        return idSalida;
    }

    public void setIdSalida(Integer idSalida) {
        this.idSalida = idSalida;
    }

    public String getFolioSalida() {
        return folioSalida;
    }

    public void setFolioSalida(String folioSalida) {
        this.folioSalida = folioSalida;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public StatusSalida getStatus() {
        return status;
    }

    public void setStatus(StatusSalida status) {
        this.status = status;
    }

    public String getPlacasTransporte() {
        return placasTransporte;
    }

    public void setPlacasTransporte(String placasTransporte) {
        this.placasTransporte = placasTransporte;
    }

    public String getNombreTransportista() {
        return nombreTransportista;
    }

    public void setNombreTransportista(String nombreTransportista) {
        this.nombreTransportista = nombreTransportista;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public List<SalidaDetalle> getListSalidaDetalle() {
        return listSalidaDetalle;
    }

    public void setListSalidaDetalle(List<SalidaDetalle> listSalidaDetalle) {
        this.listSalidaDetalle = listSalidaDetalle;
    }

    public List<ServiciosSalida> getListServiciosSalida() {
        return listServiciosSalida;
    }

    public void setListServiciosSalida(List<ServiciosSalida> listServiciosSalida) {
        this.listServiciosSalida = listServiciosSalida;
    }

    @Override
    public int hashCode() {
        if (this.idSalida == null) {
            return System.identityHashCode(this);
        }
        return Objects.hash(this.idSalida);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Salida other = (Salida) obj;
        if(this.idSalida == null || other.idSalida == null)
            return Objects.equals(System.identityHashCode(this), System.identityHashCode(other));
        
        return Objects.equals(this.idSalida, other.idSalida);
    }

    @Override
    public String toString() {
        return "Salida[" + "idSalida=" + idSalida + ", folioSalida=" + folioSalida + ", placasTransporte=" + placasTransporte 
            + ", nombreTransportista=" + nombreTransportista + ", observaciones=" + observaciones + ", fechaSalida=" + fechaSalida 
                + ", horaSalida=" + horaSalida + ", fechaRegistro=" + fechaRegistro + ", fechaModificacion=" + fechaModificacion + ']';
    }
    
}
