package mx.com.ferbo.util;

public class ResultadoOperacion {

    private final String titulo;
    private final String mensaje;
    private final int totalAfectados;  // <- nombre más general
    private final NivelMensaje nivel;

    public ResultadoOperacion(String titulo, String mensaje, int totalAfectados, NivelMensaje nivel) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.totalAfectados = totalAfectados;
        this.nivel = nivel;
    }

    public String getTitulo() { return titulo; }
    public String getMensaje() { return mensaje; }
    public int getTotalAfectados() { return totalAfectados; }
    public NivelMensaje getNivel() { return nivel; }
}