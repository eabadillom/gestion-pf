package mx.com.ferbo.model.catalogos;

public interface Catalogo {
    Integer getId();

    String getNombre();
    void setNombre(String nombre);

    String getDescripcion();
    void setDescripcion(String descripcion);

    Boolean getVigente();
    void setVigente(Boolean vigente);
}
