package mx.com.ferbo.controller.egresos;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.categresos.CatConceptoEgresoBL;
import mx.com.ferbo.business.categresos.CategoriaEgresoBL;
import mx.com.ferbo.business.categresos.TipoEgresoBL;
import mx.com.ferbo.business.empresa.NEmisoresCFDISBL;
import mx.com.ferbo.manager.egresos.ActivoFijoMGR;
import mx.com.ferbo.manager.egresos.AsignacionMGR;
import mx.com.ferbo.manager.egresos.CargoEgresoMGR;
import mx.com.ferbo.manager.egresos.ConceptoEgresoMGR;
import mx.com.ferbo.manager.egresos.DevolucionEgresoMGR;
import mx.com.ferbo.manager.egresos.ImporteEgresoMGR;
import mx.com.ferbo.manager.egresos.PagoEgresoMGR;
import mx.com.ferbo.model.categresos.CatConceptoEgreso;
import mx.com.ferbo.model.categresos.CategoriaEgreso;
import mx.com.ferbo.model.egresos.ActivoFijo;
import mx.com.ferbo.model.egresos.CargoEgreso;
import mx.com.ferbo.model.egresos.ConceptoEgreso;
import mx.com.ferbo.model.egresos.DevolucionEgreso;
import mx.com.ferbo.model.categresos.TipoEgreso;
import mx.com.ferbo.model.egresos.ImporteEgreso;
import mx.com.ferbo.model.egresos.PagoEgreso;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.wrapper.CatEgresoWRP;
import mx.com.ferbo.wrapper.EgresoWRP;

@Named
@ApplicationScoped
public class EgresoBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(EgresoBean.class);

    private Integer id;

    private CatEgresoWRP catEgresoWRP;

    private EgresoWRP egresoWRP;

    private ActivoFijoMGR activoFijoMGR;

    private AsignacionMGR asignacionMGR;

    private CargoEgresoMGR cargoEgresoMGR;

    private ConceptoEgresoMGR conceptoEgresoMGR;

    private DevolucionEgresoMGR devolucionEgresoMGR;

    private PagoEgresoMGR pagoEgresoMGR;

    private ImporteEgresoMGR importeEgresoMGR;

    private String[] notificacion;

    String mensaje;
    String titulo;

    @Inject
    private TipoEgresoBL tipoEgresoBL;

    @Inject
    private CategoriaEgresoBL categoriaEgresoBL;
    
    @Inject
    private CatConceptoEgresoBL catConceptoEgresoBL;


    @Inject NEmisoresCFDISBL emisoresCFDISBL;


    public EgresoBean() {
        catEgresoWRP = new CatEgresoWRP();
        egresoWRP = new EgresoWRP();
        activoFijoMGR = new ActivoFijoMGR();
        asignacionMGR = new AsignacionMGR();
        cargoEgresoMGR = new CargoEgresoMGR();
        conceptoEgresoMGR = new ConceptoEgresoMGR();
        devolucionEgresoMGR = new DevolucionEgresoMGR();
        pagoEgresoMGR = new PagoEgresoMGR();
        importeEgresoMGR = new ImporteEgresoMGR();
    }

    @PostConstruct
    public void init() {

        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();

        String idParam = params.get("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                id = Integer.valueOf(idParam);
                importeEgresoMGR.cargar(id,  egresoWRP.getEgresoSelected());
            } catch (InventarioException ex) {

            }catch (NumberFormatException ex) {
                egresoWRP.setEgresoSelected(new ImporteEgreso());
            }
        } else {
            egresoWRP.setEgresoSelected(new ImporteEgreso());
        }

        try {
            catEgresoWRP.setLstTipos(tipoEgresoBL.vigentesONoVigentes(true));
            catEgresoWRP.setLstRazones(emisoresCFDISBL.obtenerTodos());
        } catch (InventarioException ex) {
            log.warn("Error al momento de cargar la información necesaria del sistema. {}", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Ajustes del sistema", "Hubo un error al momento de cargar la información necesaria.");
            actualizaciones();
        }
    }

    private void ejecutarGuardar(Supplier<String[]> operacion) {
        try {
            log.info("Iniciando operación");

            String[] resultado = operacion.get();
            titulo = resultado[0];
            mensaje = resultado[1];

            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);

        } catch (RuntimeException ex) {
            log.warn(ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (Exception ex) {
            log.error(ex);
            mensaje = "Ha ocurrido un problema inesperado. Si el inconveniente persiste, por favor contacte con el administrador.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        } finally {
            actualizaciones();
        }
    }

    public void actualizaciones() {
        PrimeFaces.current().ajax().update("formConsulta:messages");
    }

    public void guardarPagos() {
        ejecutarGuardar(() -> {
            try {
                return pagoEgresoMGR.guardar(egresoWRP.getPagoSelected(), egresoWRP.getEgresoSelected());
            } catch (InventarioException ex) {
                log.warn("Error: {}", ex);
                throw new RuntimeException(ex);
            }
        });
    }

    public void guardarCargos() {
        ejecutarGuardar(() -> {
            try {
                return cargoEgresoMGR.guardar(egresoWRP.getCargoSelected(), egresoWRP.getEgresoSelected());
            } catch (InventarioException ex) {
                log.warn("Error: {}", ex);
                throw new RuntimeException(ex);
            }
        });
    }

    public void guardarDevoluciones() {
        ejecutarGuardar(() -> {
            try {
                return devolucionEgresoMGR.guardar(egresoWRP.getDevolucionSelected(), egresoWRP.getEgresoSelected());
            } catch (InventarioException ex) {
                log.warn("Error: {}", ex);
                throw new RuntimeException(ex);
            }
        });
    }

    public void guardarActivosFijos() {
        ejecutarGuardar(() -> {
            try {
                return activoFijoMGR.guardar(egresoWRP.getActivoFijoSelected(), egresoWRP.getEgresoSelected());
            } catch (InventarioException ex) {
               log.warn("Error: {}", ex);
                throw new RuntimeException(ex);
            }
        });
    }

    public void guardarEgreso(){
        ejecutarGuardar(() -> {
            try {
                return importeEgresoMGR.guardar(egresoWRP.getEgresoSelected(), egresoWRP.getConceptoSelected());
            } catch (InventarioException ex) {
                log.warn("Error: {}", ex);
                throw new RuntimeException(ex);
            }
        });
    }
}
