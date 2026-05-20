package mx.com.ferbo.egresos.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@NamedQueries({
        @NamedQuery(name = "ArchivoEgreso.findByEgreso", query = "SELECT ae FROM ArchivoEgreso ae WHERE ar.egreso = :egreso")
})
@Entity
@Table(name = "archivo_egreso")
public class ArchivoEgreso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_arch_egre", nullable = false)
    private Long id;

    @Column(name = "tx_arch_egre", nullable = false)
    private String url;

    @Column(name = "nb_arch_egre", nullable = false)
    private String nombre;

    @Column(name = "tp_arch_egre", nullable = false)
    private String tipoArchivo;

    @Column(name = "nu_arch_egre", nullable = false)
    private Long tamanio;

    @OneToOne
    @JoinColumn(name = "cd_egre", referencedColumnName = "cd_egre", nullable = false)
    private Egreso egreso;

    @Column(name = "tm_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "tm_modificacion")
    private LocalDateTime fechaModificacion;

    public ArchivoEgreso() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public Long getTamanio() {
        return tamanio;
    }

    public void setTamanio(Long tamanio) {
        this.tamanio = tamanio;
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

    public Egreso getEgreso() {
        return egreso;
    }

    public void setEgreso(Egreso egreso) {
        this.egreso = egreso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchivoEgreso)) {
            return false;
        }

        ArchivoEgreso that = (ArchivoEgreso) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "ArchivoEgreso [id=" + id + ", url=" + url + ", nombre=" + nombre + ", tipoArchivo=" + tipoArchivo
                + ", tamanio=" + tamanio + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion="
                + fechaModificacion + "]";
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
