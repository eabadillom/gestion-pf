package mx.com.ferbo.controller;

import com.ferbo.gestion.reports.jasper.ReporteOcupacionCamaraJR;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RepOcupacionCamaraDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.optionconfig.title.Title;

@Named
@ViewScoped
public class ReporteInventarioOcupacionCamaraBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(ReporteInventarioOcupacionCamaraBean.class);
    
    @Inject
    private RepOcupacionCamaraDAO ocupacionCamaraDAO;
    
    @Inject
    private PlantaDAO plantaDAO;

    private PieChartModel pieModel;
    private PieChartModel model;

    private Date fecha;

    private Planta plantaSelect;
    private List<Cliente> listClienteSelect;
    private List<Cliente> listaClientes;
    private List<Planta> listaPlanta;
    private List<OcupacionCamara> listaOcupacionCamara;

    private ReporteOcupacionCamaraJR reporteOcupacionCamaraJR = null; 
    private StreamedContent file;

    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;
    private Usuario usuario;

    public ReporteInventarioOcupacionCamaraBean() {
        fecha = new Date();

        listaClientes = new ArrayList<Cliente>();
        listaPlanta = new ArrayList<Planta>();
        listaOcupacionCamara = new ArrayList<OcupacionCamara>();
        pieModel = new PieChartModel();
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() 
    {
        faceContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");

        plantaSelect = new Planta();

        listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");

        if ((usuario.getPerfil() == 1) || (usuario.getPerfil() == 4)) {
            listaPlanta.add(plantaDAO.findById(usuario.getIdPlanta()));
        } else {
            listaPlanta = plantaDAO.buscarTodos(false);
        }
        
        log.info("El usuario {} esta ingresando a reporte de ocupacion de plantas", usuario.getUsuario());
    }

    public void ocupacionCamara() throws InventarioException 
    {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Reporte de Ocupación de Camaras";

        try {
            log.info("El usuario {} esta consultado la posiciones por planta", usuario.getUsuario());
            List<Integer> listClientes = null;
            if (this.listClienteSelect != null || !this.listClienteSelect.isEmpty()) {
                listClientes = new ArrayList();
                for(Cliente cliente : listClienteSelect) {
                    listClientes.add(cliente.getCteCve());
                }
            }
            
            Integer idPlanta = null;
            if (plantaSelect != null) {
                idPlanta = plantaSelect.getPlantaCve();
            } 
            
            listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, listClientes, idPlanta);
            mensaje = "La consulta se generó correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (Exception e) {
            mensaje = e.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:dt-OcupacionCamara", "form:messages");
        }
    }

    public void exportarPdf() throws JRException, IOException, SQLException {
        log.info("Exportando a pdf.....");
        String filename = "Ocupacion Camaras" + fecha + ".pdf";
        String images = "/images/logo.jpeg";
        String message = null;
        Severity severity = null;
        Connection connection = null;
        
        try {
            log.info("Exportando a pdf...");
            List<Integer> listClientes = null;
            if (this.listClienteSelect != null || !this.listClienteSelect.isEmpty()) {
                listClientes = new ArrayList();
                for(Cliente cliente : listClienteSelect) {
                    listClientes.add(cliente.getCteCve());
                }
            }

            Integer idPlanta = null;
            if (plantaSelect != null) {
                idPlanta = plantaSelect.getPlantaCve();
            }

            connection = EntityManagerUtil.getConnection();
            reporteOcupacionCamaraJR = new ReporteOcupacionCamaraJR(connection, images);
            byte[] bytes = reporteOcupacionCamaraJR.getPDFReporteOcupacionCamara(fecha, listClientes, idPlanta);
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/pdf")
                .name(filename)
                .stream(() -> input)
                .build();
            log.info("El usuario {} descargo el reporte de ocupacion de camaras {}", this.usuario.getUsuario(), filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
        } finally {
            conexion.close((Connection) connection);
        }
    }

    public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }

    public void exportarExcel() throws JRException, IOException, SQLException {
        log.info("Exportando a excel.....");
        String filename = "Ocupacion Camaras" + fecha + ".xlsx";
        String images = "/images/logo.jpeg";
        String message = null;
        Severity severity = null;
        Connection connection = null;
        
        try {
            List<Integer> listClientes = null;
            if (this.listClienteSelect != null || !this.listClienteSelect.isEmpty()) {
                listClientes = new ArrayList();
                for(Cliente cliente : listClienteSelect) {
                    listClientes.add(cliente.getCteCve());
                }
            }

            Integer plantaCve = null;
            if (plantaSelect != null) {
                plantaCve = plantaSelect.getPlantaCve();
            }

            connection = EntityManagerUtil.getConnection();
            reporteOcupacionCamaraJR = new ReporteOcupacionCamaraJR(connection, images);
            byte[] bytes = reporteOcupacionCamaraJR.getXLSReporteOcupacionCamara(fecha, listClientes, plantaCve);
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel")
                    .name(filename)
                    .stream(() -> input)
                    .build();
            log.info("El usuario {} descargo el reporte de inventario {}", this.usuario.getUsuario(), filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
        } finally {
            conexion.close((Connection) connection);
        }
    }

    public void createPieModel(String cliente) {
        log.info("El usuario {} esta cargando la grafica del cliente {}", usuario.getUsuario(), cliente);
        pieModel = new PieChartModel();
        ChartData data = new ChartData();
        Random rnd = new Random();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();

        Integer size = listaOcupacionCamara.size();
        Integer i = 0;

        List<String> labels = new ArrayList<>();

        for (OcupacionCamara oc : listaOcupacionCamara) {
            if (oc.getCte_nombre().equals(cliente)) {
                values.add(oc.getTarima());
                labels.add(oc.getPlanta_ds());
                i++;
            }
        }

        dataSet.setData(values);

        List<Integer> rgb = null;
        List<String> bgColors = new ArrayList<>();
        Integer numero;

        for (int bgcolor = 0; bgcolor < size; bgcolor++) { //repito las acciones la veces del tamaño de mi lista de ocupacionCamaras son los objetos que se van a crear de bgColors

            rgb = new ArrayList<Integer>();

            for (int color = 0; color < 3; color++) {//creo 3 numeros random tomando en cuenta el rango de valores RGB        		
                numero = (int) (rnd.nextDouble() * 256);
                rgb.add(numero);
            }

            bgColors.add("rgb(" + rgb.get(0).toString() + "," + rgb.get(1).toString() + "," + rgb.get(2).toString() + ")");//le agrego los datos a la lista bgColors
        }

        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);
        data.setLabels(labels);

        PieChartOptions options = new PieChartOptions();
        
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Ocupación de Cámaras - " + cliente);

        options.setTitle(title);
        
        Legend legend = new Legend();
        legend.setDisplay(true);
        //legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(12);

        legend.setLabels(legendLabels);
        options.setLegend(legend);

        pieModel.setOptions(options);

        pieModel.setData(data);
    }
    
    public String getClientesSeleccionadosLabel()
    {
        if (listClienteSelect == null || listClienteSelect.isEmpty())
        {
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

    public Planta getPlantaSelect() {
        return plantaSelect;
    }

    public void setPlantaSelect(Planta plantaSelect) {
        this.plantaSelect = plantaSelect;
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

    public List<Planta> getListaPlanta() {
        return listaPlanta;
    }

    public void setListaPlanta(List<Planta> listaPlanta) {
        this.listaPlanta = listaPlanta;
    }

    public FacesContext getFaceContext() {
        return faceContext;
    }

    public void setFaceContext(FacesContext faceContext) {
        this.faceContext = faceContext;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<OcupacionCamara> getListaOcupacionCamara() {
        return listaOcupacionCamara;
    }

    public void setListaOcupacionCamara(List<OcupacionCamara> listaOcupacionCamara) {
        this.listaOcupacionCamara = listaOcupacionCamara;
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    public PieChartModel getModel() {
        return model;
    }

    public void setModel(PieChartModel model) {
        this.model = model;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

}
