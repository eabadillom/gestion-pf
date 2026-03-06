package mx.com.ferbo.modulos.egresos.model.catsecundarios;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

import mx.com.ferbo.modulos.egresos.model.CatEgreso;
import mx.com.ferbo.modulos.egresos.model.egreso.PagoEgreso;

@NamedQueries({
        @NamedQuery(name = "StatusPago.findByNombre", query = "SELECT sp FROM StatusPago sp WHERE sp.nombre = :nombre"),
        @NamedQuery(name = "StatusPago.findAllVigentesONoVigentes", query = "SELECT sp FROM StatusPago sp WHERE sp.vigente = :vigente")
})
@Entity
@Table(name = "cat_status_pago")
public class StatusPagoEgreso implements Serializable, CatEgreso<Integer> {

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

    public StatusPagoEgreso() {
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
        if (!(o instanceof StatusPagoEgreso))
            return false;

        StatusPagoEgreso that = (StatusPagoEgreso) o;
                
        if (this.id != null && that.id != null) {
            return this.id.equals(that.id);
        }

        return Objects.equals(this.nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "StatusPago [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", vigente=" + vigente
                + "]";
    }

}
