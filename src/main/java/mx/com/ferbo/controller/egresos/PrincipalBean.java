package mx.com.ferbo.controller.egresos;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class PrincipalBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(PrincipalBean.class);

    private String vistaActual = "/protected/catalogos/egresos/consulta.xhtml";

    private Integer idEgreso;

    public void irAlta() {
        vistaActual = "/protected/catalogos/egresos/egreso.xhtml";
    }

    public void irConsulta() {
        vistaActual = "/protected/catalogos/egresos/consulta.xhtml";
    }

    public String irEditar(Integer id) {
        this.idEgreso = id;
        return "/protected/catalogos/egresos/egreso?faces-redirect=true";
    }

    public String getVistaActual() {
        return vistaActual;
    }

    public Integer getIdEgreso() {
        return idEgreso;
    }

    public void setIdEgreso(Integer idEgreso) {
        this.idEgreso = idEgreso;
    }
}
