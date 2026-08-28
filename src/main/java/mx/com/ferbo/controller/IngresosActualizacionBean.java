package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import mx.com.ferbo.business.complemento.ComplementoBL;
import mx.com.ferbo.business.ComplementoPagoBL;
import mx.com.ferbo.dao.BancoDAO;
import mx.com.ferbo.dao.FacturaDAO;
import mx.com.ferbo.dao.MedioPagoDAO;
import mx.com.ferbo.dao.PagoDAO;
import mx.com.ferbo.dao.StatusFacturaDAO;
import mx.com.ferbo.dao.TipoPagoDAO;
import mx.com.ferbo.dao.n.EmisoresCFDISDAO;
import mx.com.ferbo.model.Bancos;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.ComplementoPago;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Factura;
import mx.com.ferbo.model.MedioPago;
import mx.com.ferbo.model.Pago;
import mx.com.ferbo.model.SerieComplementoPago;
import mx.com.ferbo.model.StatusFactura;
import mx.com.ferbo.model.TipoPago;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class IngresosActualizacionBean implements Serializable {

    private static final long serialVersionUID = -626048119540963939L;
    private static Logger log = LogManager.getLogger(IngresosActualizacionBean.class);

    @Inject
    private EmisoresCFDISDAO emisoresDAO;

    private ComplementoBL complementoBL;
    private String PAGO_EN_PARCIALIDADES = "PPD";

    private Date startDate;
    private Date endDate;

    private PagoDAO pagoDAO;
    private TipoPagoDAO tipoPagoDAO;
    private BancoDAO bancoDAO;
    private MedioPagoDAO medioPagoDAO;
    private FacturaDAO facturaDAO;
    private StatusFacturaDAO sfDAO;
    private StatusFactura statusPorCobrar;
    private StatusFactura statusPagada;
    private StatusFactura statusPagoParcial;
    private MedioPago medioPagoSelect;

    private List<Pago> listaPago;
    private List<Pago> listaPagosSeleccionados;
    private List<Cliente> listaCtes;
    private List<EmisoresCFDIS> listEmisores;
    private List<TipoPago> listatipoPago;
    private List<Bancos> listaBancos;
    private List<MedioPago> listaMedioPago;
    private List<SerieComplementoPago> listSerieComplemento;

    private Pago pagoSelected;
    private Cliente cteSelect;
    private EmisoresCFDIS emisoresSelected;
    private ComplementoPago complementoPago;
    private SerieComplementoPago serieComplementoPago;

    private BigDecimal totalPagos;
    private String tipoMetodoPago;
    private boolean habilitarTimbrado = false;

    private Usuario usuario;
    private FacesContext context;
    private HttpServletRequest request;

    public IngresosActualizacionBean() {
        listaPago = new ArrayList<Pago>();
        listaCtes = new ArrayList<Cliente>();
        listaMedioPago = new ArrayList<>();
        medioPagoDAO = new MedioPagoDAO();
        pagoSelected = new Pago();
        cteSelect = new Cliente();
        complementoBL = new ComplementoBL();
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance();
        request = (HttpServletRequest) context.getExternalContext().getRequest();
        usuario = (Usuario) request.getSession(false).getAttribute("usuario");
        log.info("El usuario {} entra a Actualizacion de Ingresos.", this.usuario.getUsuario());
        pagoDAO = new PagoDAO();
        tipoPagoDAO = new TipoPagoDAO();
        bancoDAO = new BancoDAO();
        sfDAO = new StatusFacturaDAO();
        facturaDAO = new FacturaDAO();

        listaCtes = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
        listEmisores = emisoresDAO.buscarTodos();
        listaBancos = bancoDAO.buscarTodos();
        listatipoPago = tipoPagoDAO.buscarTodos();
        listaPagosSeleccionados = new ArrayList();
        listaMedioPago = medioPagoDAO.buscarVigentes(new Date());
        this.startDate = new Date();
        this.endDate = new Date();

        statusPorCobrar = sfDAO.buscarPorId(StatusFactura.STATUS_POR_COBRAR);
        statusPagada = sfDAO.buscarPorId(StatusFactura.STATUS_PAGADA);
        statusPagoParcial = sfDAO.buscarPorId(StatusFactura.STATUS_PAGO_PARCIAL);
    }

    public void filtraPagos() {
        String messages = null;
        Severity severity = null;
        try {
            if (emisoresSelected == null) {
                throw new InventarioException("Debe seleccionar un emisor.");
            }

            if (tipoMetodoPago.isEmpty() || tipoMetodoPago == null) {
                throw new InventarioException("Debe seleccionar el método de pago.");
            }
            consultaListaPagos();
            this.totalPagos = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
            for (Pago pago : listaPago) {
                if (pago.getTipo().getId() == 5) {
                    continue;
                }
                this.totalPagos = this.totalPagos.add(pago.getMonto());
            }

            /*Obtener la serie del complemento de pago*/
            this.listSerieComplemento = complementoBL.obtenerSerieComplemento(emisoresSelected.getCd_emisor());
            this.listaPagosSeleccionados.clear();
            this.serieComplementoPago = this.listSerieComplemento.get(0);

            log.info("Se ha filtrado la lista de pagos");
            severity = FacesMessage.SEVERITY_INFO;
            messages = "La consulta se realizó correctamente.";
        } catch (InventarioException ex) {
            log.error("Ocurrió un problema al consultar la lista de pagos...", ex);
            severity = FacesMessage.SEVERITY_WARN;
            messages = ex.getMessage();
        } catch (Exception ex) {
            log.error("Ocurrió un problema al consultar la lista de pagos...", ex);
            severity = FacesMessage.SEVERITY_ERROR;
            messages = "Error al consultar la lista de pagos";
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Ingresos", messages));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void consultaListaPagos() throws DAOException {
        listaPago = pagoDAO.buscaPorParametros(emisoresSelected, cteSelect, startDate, endDate, tipoMetodoPago);
    }

    public void cargaInfoPago(Pago pPago) {
        String messages = null;
        Severity severity = null;
        try {
            pagoSelected = pPago;
            severity = FacesMessage.SEVERITY_INFO;
            messages = "Se seleccionó correctamente el pago";
        } catch (Exception ex) {
            log.error("Ocurrió un problema al seleccionar el pago...", ex);
            severity = FacesMessage.SEVERITY_ERROR;
            messages = "Error al seleccionar el pago";
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Ingresos", messages));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void limpiarDialogo() {
        pagoSelected = new Pago();
        //consultaListaPagos();
        PrimeFaces.current().ajax().update("form:messages", "panel-actualizaPago", "soParcialidad", "dt-pagos");
    }

    public void calcularParcialidad() {
        String messages = null;
        Severity severity = null;
        try {
            if (pagoSelected == null) {
                throw new InventarioException("No se cargo el pago seleccionado para actualizar.");
            }

            Integer parcialidad = 0;
            List<Pago> pagos = pagoDAO.buscaPorFacturaFechas(pagoSelected.getFactura(), startDate, endDate, tipoMetodoPago);
            for (Pago pago : pagos) {
                parcialidad += 1;
                if (pago.equals(pagoSelected)) {
                    pagoSelected.setParcialidad(parcialidad);
                }
            }

            severity = FacesMessage.SEVERITY_INFO;
            messages = "Se actualizo correctamente la parcialidad.";
        } catch (InventarioException ex) {
            log.error("Ocurrió un problema al consultar la lista de pagos...", ex);
            severity = FacesMessage.SEVERITY_WARN;
            messages = ex.getMessage();
        } catch (Exception ex) {
            log.error("Ocurrió un problema al consultar la lista de pagos...", ex);
            severity = FacesMessage.SEVERITY_ERROR;
            messages = "Error al consultar la lista de pagos";
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Ingresos", messages));
            PrimeFaces.current().ajax().update("form:messages", "form:panel-actualizaPago", "form:soParcialidad");
        }
    }

    public void updatePago() {
        String messages = null;
        Severity severity = null;

        Pago pago = null;
        Factura factura = null;
        BigDecimal saldo = null;

        String respuesta = null;

        try {
            log.debug("Pago: {}", pagoSelected);

            if (pagoSelected.getComplementoPago().getUuid() != null) {
                throw new InventarioException("Ya no se puede modificar el pago, ya tiene asociado un complemento de pago.");
            }

            respuesta = pagoDAO.actualizar(pagoSelected);
            if (respuesta != null) {
                throw new InventarioException("Ocurrió un problema al actualizar el pago.");
            }

            pago = pagoDAO.buscarPorId(this.pagoSelected.getId(), true);
            factura = pago.getFactura();
            saldo = factura.getTotal();

            for (Pago p : factura.getPagoList()) {
                saldo = saldo.subtract(p.getMonto());
            }

            if (saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) < 0) {
                factura.setStatus(statusPagoParcial);
            } else if (saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) == 0) {
                factura.setStatus(statusPorCobrar);
            } else if (saldo.compareTo(BigDecimal.ZERO) == 0) {
                factura.setStatus(statusPagada);
            } else {
                String msg = String.format("La suma de todos los pagos de la factura %s-%s excede el monto total.", factura.getNomSerie(), factura.getNumero());
                throw new InventarioException(msg);
            }

            facturaDAO.actualizaStatus(factura);

            consultaListaPagos();

            severity = FacesMessage.SEVERITY_INFO;
            messages = "Se actualizó correctamente.";

        } catch (InventarioException ex) {
            log.error("Ocurrió un problema al actualizar el pago...", ex);
            severity = FacesMessage.SEVERITY_ERROR;
            messages = ex.getMessage();
        } catch (Exception ex) {
            log.error("Ocurrió un problema al actualizar el pago...", ex);
            severity = FacesMessage.SEVERITY_ERROR;
            messages = "Error al actualizar pago";
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Pago", messages));
        PrimeFaces.current().ajax().update("form:messages", "dt-pagos", "pnlComplementoPago", "dtComplementoPago");
    }

    public void deletePago() {

        String mensaje = null;
        Severity severity = null;

        String respuesta = null;
        Pago pago = null;
        Factura factura = null;
        Integer idFactura = null;
        BigDecimal saldo = null;

        try {

            pago = pagoDAO.buscarPorId(this.pagoSelected.getId(), true);
            idFactura = pago.getFactura().getId();

            if (pago.getComplementoPago().getUuid() != null) {
                throw new InventarioException("Ya no se puede eliminar el pago, ya está asociado con un complemento de pago.");
            }

            respuesta = pagoDAO.eliminar(pagoSelected);

            if (respuesta != null) {
                log.error("Problema al eliminar el pago " + respuesta);
                throw new InventarioException("Ocurrió un problema para eliminar el pago.");
            }

            factura = facturaDAO.buscarPorId(idFactura, true);
            saldo = factura.getTotal();

            for (Pago p : factura.getPagoList()) {
                saldo = saldo.subtract(p.getMonto());
            }

            if (saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) < 0) {
                factura.setStatus(statusPagoParcial);
            } else if (saldo.compareTo(BigDecimal.ZERO) > 0 && saldo.compareTo(factura.getTotal()) == 0) {
                factura.setStatus(statusPorCobrar);
            } else if (saldo.compareTo(BigDecimal.ZERO) == 0) {
                factura.setStatus(statusPagada);
            } else {
                String msg = String.format("La suma de todos los pagos de la factura %s-%s excede el monto total.", factura.getNomSerie(), factura.getNumero());
                throw new InventarioException(msg);
            }

            respuesta = facturaDAO.actualizaStatus(factura);

            if (respuesta != null) {
                log.info("Problema al actualizar el status de la factura {}-{}: {}", factura.getNomSerie(), factura.getNumero(), respuesta);
                throw new InventarioException("Ocurrió un problema para actualizar el status de la factura " + factura.getNomSerie() + " - " + factura.getNumero());
            }

            filtraPagos();

            mensaje = "Se eliminado correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Ocurrió un problema al eliminar el pago...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Ocurrió un problema al eliminar el pago...", ex);
            mensaje = "Ocurrio un problema al eliminar el pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Pago", mensaje));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public synchronized void agregarPagoComplemento(Pago pPago) {
        String message = null;
        Severity severity = null;
        try {
            if (serieComplementoPago == null) {
                throw new InventarioException("Debe seleccionar un folio para el complemento de pago.");
            }

            if (pPago == null) {
                throw new InventarioException("El pago no se seleccionó correctamente.");
            }

            boolean existe = listaPagosSeleccionados.stream()
                    .anyMatch(p -> p.getId().equals(pPago.getId()));

            if (existe) {
                throw new InventarioException("El pago ya se encuentra registrado.");
            }

            log.info("Agregando pago {} a la lista de complemento de pago.", pPago.getId());
            listaPagosSeleccionados.add(pPago);
            listaPago.remove(pPago);

            message = "Pago agregado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            message = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para recuperar agregar el pago.", ex);
            message = "Ocurrió un problema para agregar el pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            if (severity != null && message != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Ingresos", message));
            }
            PrimeFaces.current().ajax().update("form:messages", "form:pnlComplementoPago", "form:dtComplementoPago");
        }
    }

    public synchronized void eliminarPagoComplemento(Pago pPago) {
        String message = null;
        Severity severity = null;
        try {
            if (pPago == null) {
                throw new InventarioException("El pago no se elimino correctamente.");
            }

            if (!listaPagosSeleccionados.contains(pPago)) {
                throw new InventarioException("El pago ya no se encuentra registrado.");
            }

            log.info("Eliminando el pago {} de la lista de complemento de pago.", pPago.getId());
            listaPagosSeleccionados.remove(pPago);
            listaPago.add(pPago);

            message = "Pago eliminado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            message = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para eliminar el pago.", ex);
            message = "Ocurrió un problema para eliminar el pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            if (severity != null && message != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Ingresos", message));
            }
            PrimeFaces.current().ajax().update("form:messages", "form:pnlComplementoPago", "form:dtComplementoPago");
        }
    }

    public boolean deshabilitarPagos(Pago pPago) {
        boolean respuesta = false;

        if (!PAGO_EN_PARCIALIDADES.equals(pPago.getFactura().getMetodoPago()) || (pPago.getComplementoPago() != null && pPago.getComplementoPago().getUuid() != null)) {
            respuesta = true;
        }

        return respuesta;
    }

    public synchronized void guardarComplementoPago() {
        String mensaje = null;
        Severity severity = null;
        try {
            if (medioPagoSelect == null) {
                throw new InventarioException("Debe seleccionar una forma de pago.");
            }
            log.info("Forma de pago del cliente {}: {}", this.cteSelect.getNombre(), medioPagoSelect.getFormaPago() + "-" + medioPagoSelect.getMpDescripcion());
            
            if (listaPagosSeleccionados.isEmpty() || listaPagosSeleccionados == null) {
                log.error("Debe seleccionar por lo menos un pago");
                throw new InventarioException("Debe seleccionar al menos un pago.");
            }

            if (cteSelect.getCteCve() == null) {
                throw new InventarioException("Debe seleccionar un cliente para generar el complemento de pago.");
            }

            complementoBL.guardarComplementoPago(serieComplementoPago, medioPagoSelect.getFormaPago());

            complementoPago = complementoBL.obtenerPorFolioSerie(serieComplementoPago.getNumero(), serieComplementoPago.getSerie());

            for (Pago pago : listaPagosSeleccionados) {
                pago.setComplementoPago(complementoPago);
                pagoDAO.actualizar(pago);
                log.info("Pago actualizado....");
            }

            habilitarTimbrado = true;
            complementoBL.actualizarSerieComplemento(serieComplementoPago);

            mensaje = String.format("El complemento de pago %s-%s se ha guardado correctamente", serieComplementoPago.getSerie(), serieComplementoPago.getNumero());
            severity = FacesMessage.SEVERITY_INFO;
            log.info(mensaje);
        } catch (InventarioException ex) {
            log.error("Ocurrió un problema para generar el complemento de pago...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Ocurrió un problema para generar el complemento de pago...", ex);
            mensaje = "Ocurrió un problema para generar el complemento de pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Complemento de pago", mensaje));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public synchronized void generarComplemento() {
        String mensaje = null;
        Severity severity = null;
        try {
            if (listaPagosSeleccionados.isEmpty() || listaPagosSeleccionados == null) {
                log.error("Debe seleccionar por lo menos un pago");
                throw new InventarioException("Debe seleccionar al menos un pago.");
            }

            if (cteSelect.getCteCve() == null) {
                throw new InventarioException("Debe seleccionar un cliente para el complemento de pago.");
            }

            Integer idCliente = listaPagosSeleccionados.get(0).getFactura().getCliente().getCteCve();

            for (Pago pago : listaPagosSeleccionados) {
                if (pago.getFactura().getCfdi() == null) {
                    throw new InventarioException("Todavía no se ha emitido la factura del pago");
                }

                if (pago.getComplementoPago().getUuid() != null) {
                    log.info("El complemento de pago con id: {} ya se encuentra timbrada (id Facturama {}).", pago.getComplementoPago().getId(), pago.getComplementoPago().getCertificadoSAT());
                    throw new InventarioException("El complemento de pago ya se encuentra timbrado");
                }

                if (!PAGO_EN_PARCIALIDADES.equals(pago.getFactura().getMetodoPago())) {
                    throw new InventarioException("Solo se pueden generar complementos para pagos con método de pago 'PPD'.");
                }

                if (!idCliente.equals(pago.getFactura().getCliente().getCteCve())) {
                    throw new InventarioException("Los pagos deben ser del mismo cliente");
                }
            }

            log.info("Cantidad de pagos a generar con el complemento de pago: {}", listaPagosSeleccionados.size());
            /*Enviar peticion a facturama*/
            ComplementoPagoBL complementoPagoBL = new ComplementoPagoBL(listaPagosSeleccionados, cteSelect.getCteCve(), emisoresSelected.getCd_emisor(), usuario, medioPagoSelect.getFormaPago());
            complementoPagoBL.timbrar();
            complementoPagoBL.sendMail();

            log.info("Timbrado completado correctamente.");
            mensaje = "El timbrado se ha hecho correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Ocurrió un problema para generar el complemento de pago...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Ocurrió un problema para generar el complemento de pago...", ex);
            mensaje = "Ocurrió un problema para generar el complemento de pago.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Complemento de pago", mensaje));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }

    public void reset() {
        ExternalContext ec = null;
        try {
            ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (IOException e) {
            log.error("Problema para crear una nueva factura...", e);
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Pago> getListaPago() {
        return listaPago;
    }

    public void setListaPago(List<Pago> listaPago) {
        this.listaPago = listaPago;
    }

    public Pago getPagoSelected() {
        return pagoSelected;
    }

    public void setPagoSelected(Pago pagoSelected) {
        this.pagoSelected = pagoSelected;
    }

    public Cliente getCteSelect() {
        return cteSelect;
    }

    public void setCteSelect(Cliente cteSelect) {
        this.cteSelect = cteSelect;
    }

    public List<Cliente> getListaCtes() {
        return listaCtes;
    }

    public void setListaCtes(List<Cliente> listaCtes) {
        this.listaCtes = listaCtes;
    }

    public List<EmisoresCFDIS> getListEmisores() {
        return listEmisores;
    }

    public void setListEmisores(List<EmisoresCFDIS> listEmisores) {
        this.listEmisores = listEmisores;
    }

    public EmisoresCFDIS getEmisoresSelected() {
        return emisoresSelected;
    }

    public void setEmisoresSelected(EmisoresCFDIS emisoresSelected) {
        this.emisoresSelected = emisoresSelected;
    }

    public List<TipoPago> getListatipoPago() {
        return listatipoPago;
    }

    public void setListatipoPago(List<TipoPago> listatipoPago) {
        this.listatipoPago = listatipoPago;
    }

    public List<Bancos> getListaBancos() {
        return listaBancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public MedioPago getMedioPagoSelect() {
        return medioPagoSelect;
    }

    public void setMedioPagoSelect(MedioPago medioPagoSelect) {
        this.medioPagoSelect = medioPagoSelect;
    }

    public List<MedioPago> getListaMedioPago() {
        return listaMedioPago;
    }

    public void setListaMedioPago(List<MedioPago> listaMedioPago) {
        this.listaMedioPago = listaMedioPago;
    }

    public BigDecimal getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(BigDecimal totalPagos) {
        this.totalPagos = totalPagos;
    }

    public String getPAGO_EN_PARCIALIDADES() {
        return PAGO_EN_PARCIALIDADES;
    }

    public void setPAGO_EN_PARCIALIDADES(String PAGO_EN_PARCIALIDADES) {
        this.PAGO_EN_PARCIALIDADES = PAGO_EN_PARCIALIDADES;
    }

    public String getTipoMetodoPago() {
        return tipoMetodoPago;
    }

    public void setTipoMetodoPago(String tipoMetodoPago) {
        this.tipoMetodoPago = tipoMetodoPago;
    }

    public List<Pago> getListaPagosSeleccionados() {
        return listaPagosSeleccionados;
    }

    public void setListaPagosSeleccionados(List<Pago> listaPagosSeleccionados) {
        this.listaPagosSeleccionados = listaPagosSeleccionados;
    }

    public List<SerieComplementoPago> getListSerieComplemento() {
        return listSerieComplemento;
    }

    public void setListSerieComplemento(List<SerieComplementoPago> listSerieComplemento) {
        this.listSerieComplemento = listSerieComplemento;
    }

    public ComplementoPago getComplementoPago() {
        return complementoPago;
    }

    public void setComplementoPago(ComplementoPago complementoPago) {
        this.complementoPago = complementoPago;
    }

    public SerieComplementoPago getSerieComplementoPago() {
        return serieComplementoPago;
    }

    public void setSerieComplementoPago(SerieComplementoPago serieComplementoPago) {
        this.serieComplementoPago = serieComplementoPago;
    }

    public boolean isHabilitarTimbrado() {
        return habilitarTimbrado;
    }

    public void setHabilitarTimbrado(boolean habilitarTimbrado) {
        this.habilitarTimbrado = habilitarTimbrado;
    }

}
