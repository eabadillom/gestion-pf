package mx.com.ferbo.egresos.controller.catalogos;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;

import mx.com.ferbo.egresos.business.EgresoBL;
import mx.com.ferbo.egresos.business.catalogos.CatalogoBL;
import mx.com.ferbo.egresos.business.catalogos.CategoriaEgresoBL;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.egresos.model.calogos.CategoriaEgreso;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.FacesUtils;

@Named
@ViewScoped
public class CategoriaEgresoBean extends AbstractCatalogoBean<CategoriaEgreso> {

    private static final Logger log = LogManager.getLogger(CategoriaEgresoBean.class);

    @Inject
    private CategoriaEgresoBL categoriaBL;

    @Inject
    private EgresoBL egresoBL;

    public CategoriaEgresoBean() {
    }

    @Override
    protected CatalogoBL<CategoriaEgreso> getBL() {
        return categoriaBL;
    }

    @Override
    protected CategoriaEgreso crearNuevo() {
        return new CategoriaEgreso();
    }

    @Override
    protected void cargaInicial() {
        try {
            titulo = "carga inicial de categoría egreso";
            FacesContext context = FacesContext.getCurrentInstance();

            HttpServletRequest request = (HttpServletRequest) context
                    .getExternalContext()
                    .getRequest();
            usuario = (Usuario) request.getSession(false).getAttribute("usuario");

            inicioLeyenda = "El usuario " + usuario.getUsuario();
            log.info("{} ha iniciado la {}.", inicioLeyenda, titulo);
            buscar();
            log.info("{} ha finalizado la {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha completado exitosamente la " + titulo + ".");
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo.toUpperCase(),
                    "Hubo un problema al " + titulo + ".");
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error desconocido. Contacte con el administrador de sistemas.");
        } finally {
            actualizarMensajes();
        }
    }

    public void procesarCategoriaEgreso() {
        try {
            String estado = (selected.getId() == null) ? "guardar" : "actualizar";
            titulo = estado + " categoría egreso";
            log.info("{} ha iniciado el proceso de {}.", inicioLeyenda, titulo);
            categoriaBL.validar(selected);
            categoriaBL.guardar(selected);
            log.info("{} ha finalizado el procesp de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "Se ha completado satisfactoriamente " + titulo + ".");
            actualizarTabla();
        } catch (ValidationException | SystemException ex) {
            log.warn("Error al momento de procesar el egreso. {}", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(), ex.getMessage());
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error desconocido. Contacte con el administrador de sistemas.");
        } finally {
            actualizarMensajes();
        }

    }

    public void desactivarOActivarCategoria(CategoriaEgreso categoria) {
        String estado;
        try {
            estado = categoria.getActivo() ? "desactivar" : "activar";
            titulo = estado + " categoría egreso";
            log.info("{} inicio el proceso de {}.", inicioLeyenda, titulo);
            List<Egreso> egresos = egresoBL.obtenerPorCategoria(categoria);
            categoriaBL.desactivarCategoria(egresos, categoria);
            categoriaBL.guardar(categoria);
            buscar();
            log.info("{} finalizo el proceso de {}.", inicioLeyenda, titulo);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo.toUpperCase(),
                    "El proceso de " + titulo + " concluyo exitosamente");
            actualizarTabla();
        } catch (SystemException | BusinessException ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo.toUpperCase(), ex.getMessage());
        } catch (Exception ex) {
            log.warn("Error al momento de {}. {}", titulo, ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo,
                    "Error desconocido. Contacte con el administrador de sistemas.");
        } finally {
            actualizarMensajes();
        }

    }

    public String mensajeDialogConfir(CategoriaEgreso categoria) {
        return "¿Desea " + (categoria.getActivo() ? "desactivar" : "activar") + " la categoría de egreso?";
    }
}
