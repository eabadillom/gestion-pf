package mx.com.ferbo.egresos.model.calogos;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "categoria_egreso")
@NamedQueries({
    @NamedQuery(
        name = "CategoriaEgreso.findByClave",
        query = "SELECT c FROM CategoriaEgreso c WHERE c.clave = :clave"
    ),
    @NamedQuery(
        name = "CategoriaEgreso.findActivosOInactivos",
        query = "SELECT c FROM CategoriaEgreso c WHERE c.activo = :activo ORDER BY c.orden"
    )
})
public class CategoriaEgreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_cate_egre")
    private Long id;

    @Column(name = "nb_cate_egre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "cl_cate_egre", nullable = false, unique = true, length = 10)
    private String clave;

    @Column(name = "tx_cate_egre")
    private String descripcion;

    @Column(name = "st_cate_egre", nullable = false)
    private Boolean activo;

    @Column(name = "nu_cate_egre")
    private Integer orden;

    @Column(name = "tm_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "tm_modificacion")
    private LocalDateTime fechaModificacion;

    public CategoriaEgreso() {

    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Override
    public int hashCode() {

        if (clave != null) 
            return clave.hashCode();
        
        return id != null ? id.hashCode() : 0;
        
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (!(obj instanceof CategoriaEgreso))
            return false;

        CategoriaEgreso that = (CategoriaEgreso) obj;
        
        if (this.clave != null && that.clave != null) {
            return this.clave.equals(that.clave);
        }

        return this.id != null && this.id.equals(that.id);

    }

    @Override
    public String toString() {
        return "CategoriaEgreso [id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", descripcion=" + descripcion
                + ", activo=" + activo + ", orden=" + orden + ", fechaCreacion=" + fechaCreacion
                + ", fechaModificacion=" + fechaModificacion + "]";
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaModificacion = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }
}