package mx.com.ferbo.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cat_tipo_documento")
public class TipoDocumento implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cd_tipo_docu")
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_tipo_docu", length = 50, nullable = false, unique = true)
    private String nombre;

    @Basic(optional = false)
    @Column(name = "tx_tipo_docu", length = 250, nullable = false)
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "st_fisc")
    private Boolean esFiscal;

    @Basic(optional = false)
    @Column(name = "st_obli")
    private Boolean esObligatorio;

    @Basic(optional = false)
    @Column(name = "st_acti")
    private Boolean activo;

    public TipoDocumento(){
        // Constructor sin parametro
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (!(o instanceof TipoDocumento))
            return false;

        TipoDocumento that = (TipoDocumento) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return 31;
    }

    @Override
    public String toString() {
        return "TipoDocumento [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", esFiscal="
                + esFiscal + ", esObligatorio=" + esObligatorio + ", activo=" + activo + "]";
    }

}
