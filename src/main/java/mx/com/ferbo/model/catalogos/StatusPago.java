package mx.com.ferbo.model.catalogos;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import mx.com.ferbo.model.egresos.PagoEgreso;

@NamedQueries({
        @NamedQuery(name = "StatusPago.findByNombre", query = "SELECT sp FROM StatusPago sp WHERE sp.nombre = :nombre"),
        @NamedQuery(name = "StatusPago.findAllVigentesONoVigentes", query = "SELECT sp FROM StatusPago sp WHERE sp.vigente = :vigente")
})
@Entity
@Table(name = "cat_status_pago")
public class StatusPago implements Serializable, Catalogo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_pago")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_pago", length = 30, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = true)
    @Column(name = "tx_status_pago", length = 100, nullable = true)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_activo")
    private Boolean vigente = Boolean.TRUE;

    @OneToMany(mappedBy = "status")
    private List<PagoEgreso> pagos;

    public StatusPago() {
        // Constuctor sin parametros
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public Boolean getVigente() {
        return vigente;
    }

    @Override
    public void setVigente(Boolean vigente) {
        this.vigente = vigente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StatusPago))
            return false;

        StatusPago that = (StatusPago) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "StatusPago [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente=" + vigente
                + "]";
    }

}
