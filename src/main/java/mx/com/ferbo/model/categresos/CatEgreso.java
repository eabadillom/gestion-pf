package mx.com.ferbo.model.categresos;

public interface CatEgreso {
    Integer getId();

    String getNombre();
    void setNombre(String nombre);

    String getDescripcion();
    void setDescripcion(String descripcion);

    Boolean getVigente();
    void setVigente(Boolean vigente);
}
