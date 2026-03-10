package mx.com.ferbo.util.messaging;

import java.util.ArrayList;
import java.util.List;

public class ResultadoBuilder {

    private String titulo;
    private String mensaje;
    private int totalAfectados;
    private NivelMensaje nivel;
    private List<Mensaje> mensajes = new ArrayList<>();

    // Setter fluido para el título
    public ResultadoBuilder titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    // Setter fluido para el mensaje
    public ResultadoBuilder mensaje(String mensaje) {
        this.mensaje = mensaje;
        return this;
    }

    // Setter fluido para el nivel
    public ResultadoBuilder nivel(NivelMensaje nivel) {
        this.nivel = nivel;
        return this;
    }

    // Setter fluido para total de afectados
    public ResultadoBuilder totalAfectados(int total) {
        this.totalAfectados = total;
        return this;
    }

    // Agregar un mensaje individual
    public ResultadoBuilder addMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            this.mensajes.add(mensaje);
        }
        return this;
    }

    // Agregar varios mensajes a la vez
    public ResultadoBuilder addMensajes(List<Mensaje> mensajes) {
        if (mensajes != null) {
            this.mensajes.addAll(mensajes);
        }
        return this;
    }

    // Construir el ResultadoOperacion final
    public ResultadoOperacion build() {
        return new ResultadoOperacion(
                titulo,
                mensaje,
                totalAfectados,
                nivel,
                mensajes);
    }
}