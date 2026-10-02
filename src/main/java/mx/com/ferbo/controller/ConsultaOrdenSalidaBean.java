package mx.com.ferbo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.ferbo.gestion.reports.jasper.OrdenRetiroJR;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.exception.ValidationException;
import com.ferbo.tools.util.date.DateFormatter;

import mx.com.ferbo.business.n.ClienteBL;
import mx.com.ferbo.business.salidas.SalidasBL;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Partida;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Salida;
import mx.com.ferbo.model.SalidaDetalle;
import mx.com.ferbo.model.StatusSalida;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.ManageStatus;

@Named
@ViewScoped
public class ConsultaOrdenSalidaBean implements Serializable {

    private static final Logger log = LogManager.getLogger(ConsultaOrdenSalidaBean.class);

    private static final long serialVersionUID = 1L;

    @Inject
    private SalidasBL salidaBL;

    @Inject
    private ClienteBL clienteBL;

    private StreamedContent ordenSalida;

    private String titulo;

    private String mensaje;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Cliente clienteSelected;
    private List<Cliente> lstClientes;

    private List<Salida> lstOrdenesSalida;
    private Salida ordenSalidaSelected;

    private List<SalidaDetalle> lstOrdenSalidaDetalle;
    private SalidaDetalle ordenSalidaDetalleSelected;

    private Integer cantidadTotal;

    private BigDecimal pesoTotal;

    private StatusSalida cancelado;

    private FacesContext context;
    private HttpServletRequest request;

    private Usuario usuario;

    private ManageStatus status;

    public ConsultaOrdenSalidaBean() {

    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance();
            request = (HttpServletRequest) context.getExternalContext().getRequest();

            usuario = (Usuario) request.getSession(false)
                    .getAttribute("usuario");

            fechaInicio = LocalDate.now();
            fechaFin = LocalDate.now();
            clienteSelected = new Cliente();
            cantidadTotal = 0;
            pesoTotal = BigDecimal.ZERO;
            lstOrdenSalidaDetalle = new ArrayList<>();
            ordenSalidaDetalleSelected = new SalidaDetalle();
            lstClientes = new ArrayList<>();

            lstClientes = clienteBL.obtenerTodos();
            cancelado = salidaBL.obtenerStatusCancelado();
            status = new ManageStatus();

        } catch (DAOException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage(), ex);
        }
    }

    private void actualizarMensajes() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    public void consultarSalidas() {
        titulo = "Consulta de Ordenes de Retiro";

        try {
            lstOrdenesSalida = salidaBL.consultarSalidas(clienteSelected, fechaInicio, fechaFin);
            mensaje = "Las ordenes de salida se cargron correctamente";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);
        } catch (SystemException | ValidationException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage(), ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (ParseException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage());
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (Exception ex) {
            log.error("Error con consultar la solicitud... ", ex);
            mensaje = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        } finally {
            actualizarMensajes();
        }
    }

    public void obtenerDetalles(Salida salida) {
        titulo = "Consulta de Ordenes de Retiro";
        ordenSalidaSelected = salida;
        try {
            lstOrdenSalidaDetalle = ordenSalidaSelected.getListSalidaDetalle();
            Map<String, Object> totales = salidaBL.calcularPesoYCantidadTotales(salida);
            cantidadTotal = (Integer) totales.get("cantidad");
            pesoTotal = (BigDecimal) totales.get("peso");
            mensaje = "Los totales se calcularon de forma correcta";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);
        } catch (ValidationException ex) {
            log.warn("Solicitud incorrecta: {}", ex.getMessage(), ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (Exception ex) {
            log.error("Error con consultar la solicitud...", ex);
            mensaje = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        } finally {
            actualizarMensajes();
        }
    }

    public void cancelarOrden() {

        mensaje = "Cancelar orden de retiro";

        try {
            salidaBL.cancelarOrden(usuario, ordenSalidaSelected, cancelado);
            mensaje = "La orden de salida se cancelo correctamente";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);
        } catch (InventarioException ex) {
            log.warn("Eror al momento de cancelar la orden de salida: {}", ex.getMessage(), ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (Exception ex) {
            log.error("Error con consultar la solicitud...", ex);
            mensaje = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        } finally {
            actualizarMensajes();
        }
    }

    public String formatearDate(Date fecha) {
        return (fecha == null) ? DateFormatter.format(new Date(), "dd/MM/yyyy")
                : DateFormatter.format(fecha, "dd/MM/yyyy");
    }

    public void obtenerReporte() {
        String filename = null;
        String images = "/images/logo.jpeg";
        Connection conn = null;
        List<Integer> alPlantas = null;
        Boolean isHorarioNoLaboral = null;

        LocalDate fechaSalida = null;
        LocalTime horaSalida = null;
        LocalTime horaLimite = null;
        titulo = "Archivo orden de salida";

        try {

            if (ordenSalidaSelected == null) {
                throw new ValidationException(
                        "Error folio de salida no encontrado. Si el problema persiste, contacte al soporte del sistema");
            }

            filename = String.format("OrdenSalida%s.pdf", ordenSalidaSelected.getFolioSalida());
            conn = EntityManagerUtil.getConnection();

            alPlantas = new ArrayList<Integer>();
            if (horaSalida == null) {
                horaSalida = ordenSalidaSelected.getHoraSalida();
            }

            if (fechaSalida == null) {
                String fechaSalidaString = DateFormatter.format(ordenSalidaSelected.getFechaSalida(), "yyyy-MM-dd");
                fechaSalida = DateFormatter.parseToLocalDate(fechaSalidaString, "yyyy-MM-dd");
            }

            List<SalidaDetalle> listSalidaDetalle = ordenSalidaSelected.getListSalidaDetalle();

            for (SalidaDetalle detSalida : listSalidaDetalle) {
                Partida partida = detSalida.getPartida();
                Camara camara = partida.getCamaraCve();
                Planta planta = camara.getPlantaCve();

                if (alPlantas.contains(planta.getPlantaCve())) {
                    continue;
                }

                alPlantas.add(camara.getPlantaCve().getPlantaCve());
            }

            if (fechaSalida.getDayOfWeek() == DayOfWeek.SATURDAY) {
                horaLimite = LocalTime.of(13, 0, 0);
            } else if (fechaSalida.getDayOfWeek() == DayOfWeek.SUNDAY) {
                horaLimite = LocalTime.of(0, 0, 0);
            } else {
                horaLimite = LocalTime.of(17, 0, 0);
            }

            log.info("Hora salida: {}", horaSalida);
            log.info("Hora limite: {}", horaLimite);

            if (horaSalida.isBefore(horaLimite)) {
                isHorarioNoLaboral = new Boolean(true);
            }

            OrdenRetiroJR ordenRetiroJR = new OrdenRetiroJR(conn, images);

            for (Integer idPlanta : alPlantas) {
                byte[] bytes = ordenRetiroJR.getPDF(ordenSalidaSelected.getFolioSalida(), idPlanta, isHorarioNoLaboral);
                ordenSalida = FacesUtils.crearStreamedContentDesdeBytes(bytes, filename, "PDF");
                log.info("Generado {}...", filename);
            }

            log.info("Documentos generados");
            mensaje = "El archivo de orden de salida se generó correctamente";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, mensaje);
        } catch (ValidationException ex) {
            log.warn("Error al momento de validar datos: {}", ex.getMessage(), ex);
            mensaje = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, mensaje);
        } catch (Exception ex) {
            log.error("Error con consultar la solicitud... ", ex);
            mensaje = "Error interno del sistema. Intente nuevamente. Si el problema persiste, contacte al soporte del sistema.";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, mensaje);
        } finally {
            actualizarMensajes();
        }

    }

    public String obtenerBadge(Salida salida) {
        try {
            if (salida == null) {
                throw new ValidationException("La orden de salida no puede ser vacía");
            }

            if (salida.getFechaSalida() == null) {
                throw new ValidationException("La orden de salida no tiene una fecha de salida");
            }
            String fechaString = DateFormatter.format(salida.getFechaSalida(), "dd/MM/yyyy");
            LocalDate fechaLocal = DateFormatter.parseToLocalDate(fechaString, "dd/MM/yyyy");
            return status.getStatusClass(salida.getStatus().getClave(), fechaLocal);
        } catch (ValidationException ex) {
            log.warn("Error al momento de obtener el badge: {}", ex.getMessage(), ex);
        }
        return status.getStatusClass(salida.getStatus().getClave());
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<Cliente> getLstClientes() {
        return lstClientes;
    }

    public void setLstClientes(List<Cliente> lstClientes) {
        this.lstClientes = lstClientes;
    }

    public List<Salida> getLstOrdenesSalida() {
        return lstOrdenesSalida;
    }

    public void setLstOrdenesSalida(List<Salida> lstOrdenesSalida) {
        this.lstOrdenesSalida = lstOrdenesSalida;
    }

    public Salida getOrdenSalidaSelected() {
        return ordenSalidaSelected;
    }

    public void setOrdenSalidaSelected(Salida ordenSalidaSelected) {
        this.ordenSalidaSelected = ordenSalidaSelected;
    }

    public List<SalidaDetalle> getLstOrdenSalidaDetalle() {
        return lstOrdenSalidaDetalle;
    }

    public void setLstOrdenSalidaDetalle(List<SalidaDetalle> lstOrdenSalidaDetalle) {
        this.lstOrdenSalidaDetalle = lstOrdenSalidaDetalle;
    }

    public SalidaDetalle getOrdenSalidaDetalleSelected() {
        return ordenSalidaDetalleSelected;
    }

    public void setOrdenSalidaDetalleSelected(SalidaDetalle ordenSalidaDetalleSelected) {
        this.ordenSalidaDetalleSelected = ordenSalidaDetalleSelected;
    }

    public Integer getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(Integer cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public BigDecimal getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(BigDecimal pesoTotal) {
        this.pesoTotal = pesoTotal;
    }

    public StreamedContent getOrdenSalida() {
        return ordenSalida;
    }

    public void setOrdenSalida(StreamedContent ordenSalida) {
        this.ordenSalida = ordenSalida;
    }

    public ManageStatus getStatus() {
        return status;
    }

    public void setStatus(ManageStatus status) {
        this.status = status;
    }

}
