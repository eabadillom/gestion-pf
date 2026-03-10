package mx.com.ferbo.util.messaging;

import java.util.List;

import java.util.Collections;

public class ResultadoOperacion {

    private final String titulo;
    private final String mensaje;
    private final int totalAfectados;
    private final NivelMensaje nivel;
    private final List<Mensaje> mensajes;

    // Constructor principal: debe ser público si lo usa el builder
    public ResultadoOperacion(String titulo,
                              String mensaje,
                              int totalAfectados,
                              NivelMensaje nivel,
                              List<Mensaje> mensajes) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.totalAfectados = totalAfectados;
        this.nivel = nivel;
        this.mensajes = (mensajes == null) ? Collections.emptyList() : mensajes;
    }

    // Factory para éxito simple
    public static ResultadoOperacion exito(String titulo, String mensaje, int totalAfectados) {
        return new ResultadoOperacion(titulo, mensaje, totalAfectados, NivelMensaje.SUCCESS, null);
    }

    // Factory para info
    public static ResultadoOperacion info(String titulo, String mensaje, int totalAfectados) {
        return new ResultadoOperacion(titulo, mensaje, totalAfectados, NivelMensaje.INFO, null);
    }

    // Factory para errores con lista de mensajes
    public static ResultadoOperacion error(String titulo, String mensaje, List<Mensaje> mensajes) {
        return new ResultadoOperacion(titulo, mensaje, 0, NivelMensaje.ERROR, mensajes);
    }

    // Builder
    public static ResultadoBuilder builder() {
        return new ResultadoBuilder();
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public int getTotalAfectados() { return totalAfectados; }
    public NivelMensaje getNivel() { return nivel; }
    public List<Mensaje> getMensajes() { return mensajes; }

    // Métodos de conveniencia
    public boolean esExitoso() {
        return nivel == NivelMensaje.SUCCESS || nivel == NivelMensaje.INFO;
    }

    public boolean esError() {
        return nivel == NivelMensaje.ERROR;
    }

    public boolean tieneMensajes() {
        return !mensajes.isEmpty();
    }
}