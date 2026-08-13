package mx.com.ferbo.bitacora.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ferbo.tools.exception.ValidationException;
import java.io.Serializable;

import mx.com.ferbo.bitacora.enums.NombrePantalla;
import mx.com.ferbo.bitacora.enums.TipoPantalla;
import mx.com.ferbo.model.Usuario;

@NamedQueries({

        @NamedQuery(name = "EventoBitacora.findByFiltros", query = "SELECT be FROM EventoBitacora be " +
                "WHERE (:inicio IS NULL OR be.fecha >= :inicio) " +
                "AND (:fin IS NULL OR be.fecha <= :fin) " +
                "AND (:idUsuario IS NULL OR be.usuario.id = :idUsuario) " +
                "AND (:nombrePantalla IS NULL OR be.nombrePantalla = :nombrePantalla) " +
                "AND (:tipoPantalla IS NULL OR be.tipoPantalla = :tipoPantalla) " +
                "AND (:documento IS NULL OR be.documento = :documento) " +
                "ORDER BY be.id ASC")
})

@NamedNativeQueries ({
    @NamedNativeQuery(name = "EventoBitacora.findResumenByFiltros", query = "SELECT DISTINCT " + 
                "b.cd_docum, " + 
                "u.id, " +
                "u.nombre, " +
                "u.apellido_1, " +
                "u.apellido_2, " + 
                "b.nb_panta, " + 
                "b.tp_panta, " + 
                "b.fh_momento " + 
                "FROM bitacora b " + 
                "INNER JOIN usuario u ON b.cd_usuario = u.id " + 
                "WHERE (:inicio IS NULL OR b.fh_momento >= :inicio) " + 
                "AND (:fin IS NULL OR b.fh_momento <= :fin) " + 
                "AND (:idUsuario IS NULL OR b.cd_usuario = :idUsuario) " + 
                "AND (:nombrePantalla IS NULL OR b.nb_panta = :nombrePantalla) " + 
                "AND (:tipoPantalla IS NULL OR b.tp_panta = :tipoPantalla);")
})
@Entity
@Table(name = "bitacora")
public class EventoBitacora implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_bitac")
    private Long id;

    @Column(name = "cd_sesion", nullable = false, length = 36)
    private String idSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cd_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "nb_panta", nullable = false, length = 30)
    private NombrePantalla nombrePantalla;

    @Enumerated(EnumType.STRING)
    @Column(name = "tp_panta", nullable = false, length = 10)
    private TipoPantalla tipoPantalla;

    @Column(name = "fh_momento", nullable = false)
    private LocalDate fecha;

    @Column(name = "hr_momento", nullable = false)
    private LocalTime hora;

    @Column(name = "nb_bitac", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "cd_docum", nullable = false, length = 36)
    private String documento;

    public EventoBitacora() {
    }

    private EventoBitacora(Builder builder) {

        this.idSesion = builder.contexto.getIdSesion();
        this.usuario = builder.contexto.getUsuario();
        this.nombrePantalla = builder.contexto.getNombrePantalla();
        this.tipoPantalla = builder.contexto.getTipoPantalla();
        this.fecha = builder.momento.toLocalDate();
        this.hora = builder.momento.toLocalTime();
        this.descripcion = builder.descripcion;
        this.documento = builder.documento;

    }

    public static class Builder {

        private ContextoBitacora contexto;

        private LocalDateTime momento = LocalDateTime.now();

        private String documento;

        private String descripcion;

        public Builder(ContextoBitacora contexto) {
            if (contexto == null) {
                throw new ValidationException("El contexto de la bitactora no puede ser vacío");
            }

            this.contexto = contexto;
        }

        public Builder documento(String documento) {
            if (documento == null || "".equalsIgnoreCase(documento)) {
                throw new ValidationException(
                        "El documento con el cual estan relacionado el evento no puede ser vacío");
            }
            this.documento = documento;
            return this;
        }

        public Builder descripcion(String descripcion) {
            if (descripcion == null || "".equalsIgnoreCase(descripcion)) {
                throw new ValidationException("La descripción del evento no puede ser vacía");
            }
            this.descripcion = descripcion;
            return this;
        }

        public EventoBitacora build() {
            return new EventoBitacora(this);
        }
    }

    public static Builder of(ContextoBitacora context) {
        return new Builder(context);
    }

    public Long getId() {
        return id;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public NombrePantalla getNombrePantalla() {
        return nombrePantalla;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public TipoPantalla getTipoPantalla() {
        return tipoPantalla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDocumento() {
        return documento;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof EventoBitacora)) {
            return false;
        }

        EventoBitacora other = (EventoBitacora) obj;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BitacoraEvento [idSesion=" + idSesion
                + ", usuario=" + usuario.getNombre() + " " + usuario.getApellido1() + " " + usuario.getApellido2()
                + ", momento=" + getFecha() + " " + getHora()
                + ", nombrePantalla=" + nombrePantalla
                + ", tipoPantalla=" + tipoPantalla
                + ", documento=" + documento
                + ", descripcion= " + descripcion + "]";
    }
}
