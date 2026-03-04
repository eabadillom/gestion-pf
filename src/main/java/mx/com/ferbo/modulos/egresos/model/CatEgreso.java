package mx.com.ferbo.modulos.egresos.model;

public interface CatEgreso {
    Integer getId();

    String getNombre();
    void setNombre(String nombre);

    String getDescripcion();
    void setDescripcion(String descripcion);

    Boolean getVigente();
    void setVigente(Boolean vigente);
}
