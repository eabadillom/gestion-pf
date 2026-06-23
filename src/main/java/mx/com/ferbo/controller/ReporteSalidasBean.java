package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.n.CamaraDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RepSalidasDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepSalidas;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.conexion;
import com.ferbo.gestion.reports.jasper.ReporteSalidasJR;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteSalidasBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ReporteSalidasBean.class);

    @Inject
    private RepSalidasDAO reporteDAO;

    @Inject
    private PlantaDAO plantaDAO;
    
    @Inject
    private CamaraDAO camaraDAO;

    private Date fecha;
    private Date fechaIni;
    private Date fechaFin;
    private Date maxDate;

    private Planta plantaSelect;
    private Camara camaraSelect;
    
    private List<Cliente> listClienteSelect;
    private List<Cliente> listaClientes;
    private List<Planta> listaPlanta;
    private List<Camara> listaCamara;

    private Usuario usuario;
    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;

    private List<RepSalidas> reporte;
    private StreamedContent file;

    private ReporteSalidasJR reporteSalidasJR = null;

    public ReporteSalidasBean() {
        fecha = new Date();

        listaClientes = new ArrayList<Cliente>();
        listaPlanta = new ArrayList<Planta>();
        listaCamara = new ArrayList<Camara>();
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {

        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");

        plantaSelect = new Planta();
        camaraSelect = new Camara();

        listClienteSelect = new ArrayList<Cliente>();
        listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");

        if ((usuario.getPerfil() == 1) || (usuario.getPerfil() == 4)) {
            listaPlanta.add(plantaDAO.findById(usuario.getIdPlanta()));
        } else {
            listaPlanta = plantaDAO.buscarTodos(false);
        }

        filtradoCamara();
        Date today = new Date();

        maxDate = new Date(today.getTime());
        this.fechaIni = new Date();
        this.fechaFin = new Date();
        log.info("El usuario {} esta ingresando a reporte de salidas", usuario.getUsuario());
    }

    public void filtradoCamara() {
        listaCamara = camaraDAO.findById(plantaSelect.getPlantaCve());
        plantaSelect.setCamaraList(listaCamara);
    }

    public void exportarPdf() throws JRException, IOException, SQLException {
        log.info("Exportando reporte de salidas a PDF...");
        String filename = "InventarioSalidas" + fecha + ".pdf";
        String images = "/images/logoF.png";
        String message = null;
        Severity severity = null;
        Connection connection = null;

        try {
            List<Integer> listClientes = null;
            if (this.listClienteSelect != null || !this.listClienteSelect.isEmpty()) {
                listClientes = new ArrayList<Integer>();
                for (Cliente cliente : listClienteSelect) {
                    listClientes.add(cliente.getCteCve());
                }
            }

            Integer camaraCve = null;
            if (camaraSelect != null) {
                camaraCve = camaraSelect.getCamaraCve();
            }

            Integer plantaCve = null;
            if (plantaSelect != null) {
                plantaCve = plantaSelect.getPlantaCve();
            }

            connection = EntityManagerUtil.getConnection();
            reporteSalidasJR = new ReporteSalidasJR(connection, images);
            byte[] bytes = reporteSalidasJR.getPDF(fechaIni, fechaFin, listClientes, plantaCve, camaraCve);
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder()
                    .contentEncoding("application/pdf")
                    .contentLength(bytes.length)
                    .name(filename)
                    .stream(() -> input)
                    .build();
            log.info("El usuario {} descargo el reporte de salidas {}", this.usuario.getUsuario(), filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioSalidas");
        } finally {
            conexion.close((Connection) connection);
        }

    }

    public void exportarExcel() throws JRException, IOException, SQLException {
        log.info("Exportando reporte de salidas a excel...");
        String filename = "InventarioSalidas" + fecha + ".xlsx";
        String images = "/images/logoF.png";
        String message = null;
        Severity severity = null;
        Connection connection = null;

        try {
            List<Integer> listClientes = null;
            if (this.listClienteSelect != null || !this.listClienteSelect.isEmpty()) {
                listClientes = new ArrayList<Integer>();
                for (Cliente cliente : listClienteSelect) {
                    listClientes.add(cliente.getCteCve());
                }
            }

            Integer camaraCve = null;
            if (camaraSelect != null) {
                camaraCve = camaraSelect.getCamaraCve();
            }

            Integer plantaCve = null;
            if (plantaSelect != null) {
                plantaCve = plantaSelect.getPlantaCve();
            }

            connection = EntityManagerUtil.getConnection();
            reporteSalidasJR = new ReporteSalidasJR(connection, images);
            byte[] bytes = reporteSalidasJR.getXLSX(fechaIni, fechaFin, listClientes, plantaCve, camaraCve);
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder()
                    .contentType("application/vnd.ms-excel")
                    .contentLength(bytes.length)
                    .name(filename)
                    .stream(() -> input)
                    .build();
            log.info("El usuario {} descargo el reporte de salidas {}", this.usuario.getUsuario(), filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-inventarioSalidas");
        } finally {
            conexion.close((Connection) connection);
        }
    }

    public void generaReporte() {
        List<Integer> listClientes = null;
        Integer plantaCve = null;
        Integer camaraCve = null;

        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Reporte de Salidas";

        try {
            if (this.listClienteSelect == null) {
                throw new InventarioException("Debe seleccionar un cliente.");
            }

            listClientes = new ArrayList<Integer>();
            for (Cliente cliente : listClienteSelect) {
                listClientes.add(cliente.getCteCve());
            }

            if (plantaSelect == null) {
                throw new InventarioException("Debe seleccionar una planta.");
            } else {
                plantaCve = plantaSelect.getPlantaCve();
            }

            if (camaraSelect == null) {
                camaraCve = null;
            } else {
                camaraCve = camaraSelect.getCamaraCve();
            }

            reporte = reporteDAO.buscar(fechaIni, fechaFin, listClientes, plantaCve, camaraCve);
            log.debug("Registros del reporte: {}", reporte.size());
            mensaje = "La consulta se generó correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Problema para consultar el reporte de salidas...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para consultar el reporte de salidas...", ex);
            mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:dt-reporte", "form:dtReporte", "form:messages");
        }

    }

    public String getClientesSeleccionadosLabel() {
        if (listClienteSelect == null || listClienteSelect.isEmpty()) {
            return "Todos los clientes";
        }

        int size = listClienteSelect.size();
        return size == 1 ? "1 cliente seleccionado" : size + " clientes seleccionados";
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<Cliente> getListClienteSelect() {
        return listClienteSelect;
    }

    public void setListClienteSelect(List<Cliente> listClienteSelect) {
        this.listClienteSelect = listClienteSelect;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Planta getPlantaSelect() {
        return plantaSelect;
    }

    public void setPlantaSelect(Planta plantaSelect) {
        this.plantaSelect = plantaSelect;
    }

    public List<Planta> getListaPlanta() {
        return listaPlanta;
    }

    public void setListaPlanta(List<Planta> listaPlanta) {
        this.listaPlanta = listaPlanta;
    }

    public Camara getCamaraSelect() {
        return camaraSelect;
    }

    public void setCamaraSelect(Camara camaraSelect) {
        this.camaraSelect = camaraSelect;
    }

    public List<Camara> getListaCamara() {
        return listaCamara;
    }

    public void setListaCamara(List<Camara> listaCamara) {
        this.listaCamara = listaCamara;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public List<RepSalidas> getReporte() {
        return reporte;
    }

    public void setReporte(List<RepSalidas> reporte) {
        this.reporte = reporte;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

}
