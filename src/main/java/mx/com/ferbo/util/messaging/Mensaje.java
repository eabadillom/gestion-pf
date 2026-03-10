package mx.com.ferbo.util.messaging;

public class Mensaje {

    private final String campo;
    private final String texto;
    private final NivelMensaje nivel;

    public Mensaje(String campo, String texto, NivelMensaje nivel) {
        this.campo = campo;
        this.texto = texto;
        this.nivel = nivel;
    }

    public String getCampo() { return campo; }
    public String getTexto() { return texto; }
    public NivelMensaje getNivel() { return nivel; }

}