package mx.com.ferbo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cat_status_pago")
public class StatusPago implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_status_pago")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_status_pago", length = 30, nullable = false)
    private String  nombre;

    @Basic(optional = true)
    @Column(name = "tx_satus_pago", length = 100, nullable = true)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_activo")
    private Boolean activo = Boolean.TRUE;
    
    @OneToMany(mappedBy = "status")
    private List<PagoEgreso> pagos;

    public StatusPago(){
        // Constuctor sin parametros
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(!(o instanceof StatusPago))
            return false;

        StatusPago that = (StatusPago) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "StatusPago [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", activo=" + activo
                + "]";
    }
    
}
