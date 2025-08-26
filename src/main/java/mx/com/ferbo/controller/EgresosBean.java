package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.charts.bar.BarChartModel;

import mx.com.ferbo.dao.CategoriaEgresosDAO;
import mx.com.ferbo.dao.EgresosDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.ImporteEgresosDAO;
import mx.com.ferbo.dao.ParametroDAO;
import mx.com.ferbo.dao.TipoEgresoDAO;
import mx.com.ferbo.graficas.ImporteUtilidadStackedBar;
import mx.com.ferbo.model.CategoriaEgreso;
import mx.com.ferbo.model.Egresos;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.ImporteEgreso;
import mx.com.ferbo.model.Parametro;
import mx.com.ferbo.model.TipoEgreso;
import mx.com.ferbo.ui.ImporteUtilidad;
import mx.com.ferbo.util.DateUtil;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
@Named
@ViewScoped
public class EgresosBean implements Serializable 
{
    private static Logger log = LogManager.getLogger(EgresosBean.class);
    private static final long serialVersionUID = 1L;

    private Egresos nuevoEgreso;
    private String conceptoEgreso;
    private Date fechaActual;
    private CategoriaEgreso categoriaSelect;
    private TipoEgreso tipoSelect;
    private EmisoresCFDIS emisor;
    private BarChartModel barModel;
    private BarChartModel stackedGroupBarModel;

    private Boolean ieps;

    private ImporteEgreso nuevoImporte;
    private ImporteEgreso importeSelected;
    private String importe;
    private Date mesActual;
    private Date maxDate;
    private BigDecimal iepsSelect;

    private List<CategoriaEgreso> listaCatEgresos;
    private List<TipoEgreso> listaTipoEgresos;
    private List<EmisoresCFDIS> listaEmisores;
    private List<ImporteEgreso> listaImporteEgreso;
    private List<ImporteUtilidad> listaImporteUtilidad;
    private List<Egresos> listaEgresos;
    private EmisoresCFDIS emisorCFDIS;
    private CategoriaEgresosDAO categoriaDAO;
    private TipoEgresoDAO tipoDAO;
    private EmisoresCFDISDAO emisoresDAO;
    private EgresosDAO egresosDAO;
    private ImporteEgresosDAO importeEgresosDAO;
    private ImporteEgreso i;
    private Parametro parametro;
    private ParametroDAO parametroDAO;
    
    private Date fechaInicio;
    private Date fechaFin;
    
    private static final String IVA = "IVA";
    private StreamedContent file;

    public EgresosBean() 
    {
        listaEmisores = new ArrayList<>();
        listaCatEgresos = new ArrayList<>();
        listaTipoEgresos = new ArrayList<>();
        listaEgresos = new ArrayList<>();
        categoriaDAO = new CategoriaEgresosDAO();
        tipoDAO = new TipoEgresoDAO();
        emisoresDAO = new EmisoresCFDISDAO();
        egresosDAO = new EgresosDAO();
        fechaActual = new Date();
        importeEgresosDAO = new ImporteEgresosDAO();
        listaImporteUtilidad = new ArrayList<>();
        parametro = new Parametro();
        parametroDAO = new ParametroDAO();
    }

    @PostConstruct
    public void Init() 
    {
        maxDate = new Date();
        mesActual = new Date();
        listaImporteUtilidad = importeEgresosDAO.obtenerUtilidadPorEmisor(null, mesActual);
        listaEgresos = egresosDAO.buscarTodos();
        listaCatEgresos = categoriaDAO.findByAll();
        listaTipoEgresos = tipoDAO.findByAll();
        buscar();
        actualizarListas();
        
        nuevoRegistroImporte();
        nuevoRegistroEgreso();
        barModel = new BarChartModel();
        stackedGroupBarModel = new BarChartModel();
        
        nuevoImporte = new ImporteEgreso();
        parametro = parametroDAO.buscarPorNombre(IVA);
    }
    
    public void buscar()
    {
        Date[] rango = DateUtil.obtenerRango(mesActual);
        fechaInicio = rango[0];
        fechaFin = rango[1];
        listaImporteEgreso = importeEgresosDAO.buscarPorEmisorYFechas(emisorCFDIS != null ? emisorCFDIS.getNb_emisor() : null, fechaInicio, fechaFin);
    }

    public void calcularTotal() 
    {
        BigDecimal ivaPorc = new BigDecimal(parametro.getValor() != null ? parametro.getValor() : "0")
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal iepsPorc = (iepsSelect != null ? iepsSelect : BigDecimal.ZERO)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        BigDecimal subTotal = importeSelected.getSubTotal();

        BigDecimal porcentajeIVA = subTotal.multiply(ivaPorc).setScale(2, RoundingMode.HALF_UP);
        BigDecimal porcentajeIEPS = subTotal.multiply(iepsPorc).setScale(2, RoundingMode.HALF_UP);

        importeSelected.setIva(porcentajeIVA);
        importeSelected.setIeps(porcentajeIEPS);
        importeSelected.setImporte(subTotal.add(porcentajeIVA).add(porcentajeIEPS));
    }

    public void handleToggle(ToggleEvent event) 
    {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @SuppressWarnings("unused")
    public void guardar() 
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = null;
        log.info("Iniciando el guardando de un egreso");
        
        try{
            listaEgresos.clear();
            nuevoEgreso.setNombreEgreso(conceptoEgreso);
            egresosDAO.guardar(nuevoEgreso);
            log.info("Egreso guardado exitosamente");
         
            titulo = "Egreso";
            mensaje = "Se guardo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        }catch(Exception e)
        {
            log.info("Error al guardar un egreso: ", e);
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        }finally
        {
            nuevoRegistroEgreso();
            actualizarListas();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-egresos", "form:emisor-som", "form:fs-egresos");
            PrimeFaces.current().executeInitScript("PF('dg-agregaConcepto').hide()");
        }
    }
    
    public void actualizar() 
    {
        FacesMessage message = null;
        FacesMessage.Severity severity = null;
        String mensaje = null;
        String titulo = null;
        log.info("Iniciando la actualización de un egreso");
        
        /*BigDecimal porcentajeIVA = new BigDecimal(0).setScale(2);
		BigDecimal porcentajeIESP = new BigDecimal(0).setScale(2);*/
        try {

            /*porcentajeIVA = importeSelected.getSubTotal().multiply(new BigDecimal(parametro.getValor()));
			porcentajeIESP = importeSelected.getSubTotal().multiply(iepsSelect);
						
			importeSelected.setIva(porcentajeIVA);
			importeSelected.setIeps(porcentajeIESP);
			importeSelected.setImporte(porcentajeIVA.add(porcentajeIESP).add(importeSelected.getSubTotal()));*/
            String msj = importeEgresosDAO.guardar(importeSelected);
            if (msj == null) {
                listaImporteEgreso = importeEgresosDAO.buscarTodos();
                log.info("Importe agregado: {}", importeSelected.getImporte());
                importeSelected = new ImporteEgreso();
                iepsSelect = new BigDecimal(0);
            }
            titulo = "Egreso";
            mensaje = "Se actualizo correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (Exception e) {
            log.warn("No se modifico el importe: {}", importeSelected.getImporte());
            log.error("Error al actualizar un importe: ", e);
            titulo = "Error";
            mensaje = "Consulte al administrador de sistemas";
            severity = FacesMessage.SEVERITY_ERROR;
        }finally
        {
            actualizarListas();
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dg-importe");
            PrimeFaces.current().executeInitScript("PF('dg-importe').hide()");
        }
    }

    @SuppressWarnings("unused")
    public void exportarPDF() throws JRException, IOException, SQLException 
    {
        log.info("Exportando a PDF");
        String jasperPath = "/jasper/ReporteEgresos.jrxml";
        String filename = "ReporteEgresos" +mesActual+ ".pdf";
        String images = "/images/logo.jpeg";
        String message = null;
        Severity severity = null;
        File reportFile = null;
        File imgfile = null;
        JasperReportUtil jasperReportUtil = new JasperReportUtil();
        Map<String, Object> parameters = null;
        Connection connection = null;
        parameters = new HashMap<String, Object>();

        try {
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);
            
            String emi = null;
            Integer cd_emi = null;

            if (cd_emi == null) {
                emi = null;
            } else {
                emi = importeSelected.getCdEmisor().getNb_emisor();
            }

            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("emisor", emi);
            parameters.put("fechaini", fechaInicio);
            parameters.put("fechafin", fechaFin);
            parameters.put("image", imgfile.getPath());
            byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename).stream(() -> input).build();
            // egresoPDF = jasperReportUtil.getPdf(filename, parameters,
            // reportFile.getPath());
            /*
			 * jasperReportUtil = new JasperReportUtil(); byte[] bytes =
			 * jasperReportUtil.createPDF(parameters, reportFile.getPath()); InputStream
			 * input = new ByteArrayInputStream(bytes); this.egresoPDF =
			 * DefaultStreamedContent.builder() .contentType("application/pdf")
			 * .name(filename) .stream(() -> input ) .build();
             */
            log.info("Reporte generado {}...", filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            conexion.close((Connection) connection);
        }
    }

    @SuppressWarnings("unused")
    public void exportarExcel() 
    {
        log.info("Exportando a EXCEL");
        String jasperPath = "/jasper/ReporteEgresos.jrxml";
        String filename = "ReporteEgresos" +mesActual+ ".xlsx";
        String images = "/images/logo.jpeg";
        String message = null;
        Severity severity = null;
        File reportFile = null;
        File imgfile = null;
        JasperReportUtil jasperReportUtil = new JasperReportUtil();
        Map<String, Object> parameters = null;
        Connection connection = null;
        parameters = new HashMap<String, Object>();

        try {
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);

            String emi = null;
            Integer cd_emi = null;

            if (cd_emi == null) {
                emi = null;
            } else {
                emi = importeSelected.getCdEmisor().getNb_emisor();
            }

            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("emisor", emi);
            parameters.put("fechaini", fechaInicio);
            parameters.put("fechafin", fechaFin);
            parameters.put("image", imgfile.getPath());
            byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename).stream(() -> input).build();
            // egresoPDF = jasperReportUtil.getPdf(filename, parameters,
            // reportFile.getPath());
            /*
			 * jasperReportUtil = new JasperReportUtil(); byte[] bytes =
			 * jasperReportUtil.createPDF(parameters, reportFile.getPath()); InputStream
			 * input = new ByteArrayInputStream(bytes); this.egresoPDF =
			 * DefaultStreamedContent.builder() .contentType("application/pdf")
			 * .name(filename) .stream(() -> input ) .build();
             */
            log.info("Reporte generado {}...", filename);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally {
            conexion.close((Connection) connection);
        }
    }

    public void grafica() throws ParseException 
    {
        stackedGroupBarModel = ImporteUtilidadStackedBar.construirModeloImporteUtilidad(mesActual);
    }
    
    public void nuevoRegistroImporte() {
        importeSelected = new ImporteEgreso();
    }

    public void nuevoRegistroEgreso() {
        nuevoEgreso = new Egresos();
    }
    
    public void actualizarListas(){
        listaEgresos = egresosDAO.buscarTodos();
        listaEmisores = emisoresDAO.buscarTodos();
    }

    public Egresos getNuevoEgreso() {
        return nuevoEgreso;
    }

    public void setNuevoEgreso(Egresos nuevoEgreso) {
        this.nuevoEgreso = nuevoEgreso;
    }

    public CategoriaEgreso getCategoriaSelect() {
        return categoriaSelect;
    }

    public void setCategoriaSelect(CategoriaEgreso categoriaSelect) {
        this.categoriaSelect = categoriaSelect;
    }

    public TipoEgreso getTipoSelect() {
        return tipoSelect;
    }

    public void setTipoSelect(TipoEgreso tipoSelect) {
        this.tipoSelect = tipoSelect;
    }

    public List<CategoriaEgreso> getListaCatEgresos() {
        return listaCatEgresos;
    }

    public void setListaCatEgresos(List<CategoriaEgreso> listaCatEgresos) {
        this.listaCatEgresos = listaCatEgresos;
    }

    public List<TipoEgreso> getListaTipoEgresos() {
        return listaTipoEgresos;
    }

    public void setListaTipoEgresos(List<TipoEgreso> listaTipoEgresos) {
        this.listaTipoEgresos = listaTipoEgresos;
    }

    public List<EmisoresCFDIS> getListaEmisores() {
        return listaEmisores;
    }

    public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
        this.listaEmisores = listaEmisores;
    }

    public ImporteEgreso getNuevoImporte() {
        return nuevoImporte;
    }

    public void setNuevoImporte(ImporteEgreso nuevoImporte) {
        this.nuevoImporte = nuevoImporte;
    }

    public List<Egresos> getListaEgresos() {
        return listaEgresos;
    }

    public void setListaEgresos(List<Egresos> listaEgresos) {
        this.listaEgresos = listaEgresos;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public List<ImporteEgreso> getListaImporteEgreso() {
        return listaImporteEgreso;
    }

    public void setListaImporteEgreso(List<ImporteEgreso> listaImporteEgreso) {
        this.listaImporteEgreso = listaImporteEgreso;
    }

    public ImporteEgreso getImporteSelected() {
        return importeSelected;
    }

    public void setImporteSelected(ImporteEgreso importeSelected) {
        this.importeSelected = importeSelected;
    }

    public Date getMesActual() {
        return mesActual;
    }

    public void setMesActual(Date mesActual) {
        this.mesActual = mesActual;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public EmisoresCFDIS getEmisor() {
        return emisor;
    }

    public void setEmisor(EmisoresCFDIS emisor) {
        this.emisor = emisor;
    }

    public String getConceptoEgreso() {
        return conceptoEgreso;
    }

    public void setConceptoEgreso(String conceptoEgreso) {
        this.conceptoEgreso = conceptoEgreso;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public ImporteEgreso getI() {
        return i;
    }

    public void setI(ImporteEgreso i) {
        this.i = i;
    }

    public List<ImporteUtilidad> getListaImporteUtilidad() {
        return listaImporteUtilidad;
    }

    public void setListaImporteUtilidad(List<ImporteUtilidad> listaImporteUtilidad) {
        this.listaImporteUtilidad = listaImporteUtilidad;
    }

    public BarChartModel getStackedGroupBarModel() {
        return stackedGroupBarModel;
    }

    public void setStackedGroupBarModel(BarChartModel stackedGroupBarModel) {
        this.stackedGroupBarModel = stackedGroupBarModel;
    }

    public Boolean getIeps() {
        return ieps;
    }

    public void setIeps(Boolean ieps) {
        this.ieps = ieps;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public BigDecimal getIepsSelect() {
        return iepsSelect;
    }

    public void setIepsSelect(BigDecimal iepsSelect) {
        this.iepsSelect = iepsSelect;
    }

    public EmisoresCFDIS getEmisorCFDIS() {
        return emisorCFDIS;
    }

    public void setEmisorCFDIS(EmisoresCFDIS emisorCFDIS) {
        this.emisorCFDIS = emisorCFDIS;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

}
