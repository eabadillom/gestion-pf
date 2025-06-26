package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepTarimasDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepTarimas;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
@Named
@ViewScoped
public class ReportesTarimasBean implements Serializable
{
    private static final long serialVersionUID = -1;
    private static Logger log = LogManager.getLogger(ConstanciaDeDepositoBean.class);
    
    private Cliente clienteSelected;
    private Planta plantaSelected;
    
    private ClienteDAO clienteDAO;
    private PlantaDAO plantaDAO;
    
    private List<Cliente> listaClientes;
    private List<Planta> listaPlanta;
    
    private RepTarimasDAO repTarimasDAO;
    private List<RepTarimas> repTarimas;
    
    private List<String> columnasPlantas;
    private List<Map<String, Object>> modeloTabla;
    private List<Integer> totalesTarimasPlantas;
    
    private Date fecha;
    private StreamedContent file;
    
    private FacesContext context;
    private HttpServletRequest request;
    private Usuario usuario;

    public ReportesTarimasBean() 
    {
        this.clienteDAO = new ClienteDAO();
        this.plantaDAO = new PlantaDAO();
        this.repTarimasDAO = new RepTarimasDAO();
        
        this.listaClientes = new ArrayList<Cliente>();
        this.listaPlanta = new ArrayList<Planta>();
        
        this.fecha = new Date();
    }
    
    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() 
    {
        this.context = FacesContext.getCurrentInstance();
        this.request = (HttpServletRequest) this.context.getExternalContext().getRequest();
        this.usuario = (Usuario) this.request.getSession(false).getAttribute("usuario");
        
        this.listaPlanta = this.plantaDAO.buscarTodos();
        this.listaClientes = (List<Cliente>) this.request.getSession(false).getAttribute("clientesActivosList");
        
        this.clienteSelected = new Cliente();
    }
    
    public void cargarDetalleCliente()
    {
        this.repTarimas = new ArrayList<>();
        
        this.repTarimas = this.repTarimasDAO.buscarPorClientePlanta(this.clienteSelected != null ? this.clienteSelected.getCteCve() : null, 
            this.plantaSelected != null ? this.plantaSelected.getPlantaCve() : null);
        
        this.cargarDatosUI(this.repTarimas);
    }
    
    private void cargarDatosUI(List<RepTarimas> listaTarimas)
    {
        this.columnasPlantas = new ArrayList<>();
        Map<String, Map<String, Object>> filas = new LinkedHashMap<>();
        Map<String, Integer> totalesTarimasPlanta = new LinkedHashMap<>();
        
        for(RepTarimas r : listaTarimas) 
        {
            String nombreCliente = r.getNombreCliente();
            String nombrePlanta = r.getNombrePlanta();
            Integer tarimas = r.getNumeroTarimas();

            // Registrar columna si es nueva
            if(!this.columnasPlantas.contains(nombrePlanta)) {
                this.columnasPlantas.add(nombrePlanta);
            }
            
            // Suma las tarimas por planta
            totalesTarimasPlanta.merge(nombrePlanta, tarimas, Integer::sum);

            // Obtener o crear fila para cliente
            Map<String, Object> fila = filas.computeIfAbsent(nombreCliente, k -> 
            {
                Map<String, Object> nueva = new HashMap<>();
                nueva.put("cliente", k); // columna fija cliente
                nueva.put("totalTarimas", 0); // inicializar total tarimas
                return nueva;
            });

            // Agregar tarimas a la columna dinámica
            fila.put(nombrePlanta, tarimas);
            
            // Sumar al total por cliente
            int totalActual = (Integer) fila.get("totalTarimas");
            fila.put("totalTarimas", totalActual + tarimas);
        }
        Collections.sort(this.columnasPlantas);
        
        // Asigna 0's si no hay en alguna de las columnas de plantas
        for(Map<String, Object> fila : filas.values()) 
        {
            for(String planta : this.columnasPlantas) 
            {
                fila.putIfAbsent(planta, 0);
            }
        }

        this.modeloTabla = new ArrayList<>(filas.values());
        
        // Asigna los totales de tarima por planta a una lista y si no hay deja un 0
        this.totalesTarimasPlantas = new ArrayList<>();
        for(String planta : this.columnasPlantas) 
        {
            this.totalesTarimasPlantas.add(totalesTarimasPlanta.getOrDefault(planta, 0));
        }
    }
    
    public void exportarPdf() throws JRException, IOException, SQLException 
    {
        log.info("Exportando a pdf.....");
        String jasperPath = "/jasper/InventarioTarimas.jrxml";
        String filename = "InventarioTarimas" + this.fecha + ".pdf";
        String images = "/images/logoF.png";
        String message = null;
        FacesMessage.Severity severity = null;
        File reportFile = new File(jasperPath);
        File imgfile = null;
        JasperReportUtil jasperReportUtil = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        Connection connection = null;
        parameters = new HashMap<String, Object>();
        try 
        {
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);
            log.info(reportFile.getPath());
            
            Integer clienteCve = null;
            if (this.clienteSelected == null) {
                    clienteCve = null;
            } else {
                    clienteCve = this.clienteSelected.getCteCve();
            }
            
            Integer plantaCve = null;
            if (this.plantaSelected == null) {
                    plantaCve = null;
            } else {
                    plantaCve = this.plantaSelected.getPlantaCve();
            }
            
            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("idCliente", clienteCve);
            parameters.put("Planta", plantaCve);
            parameters.put("Fecha", this.fecha);
            parameters.put("imagen", imgfile.getPath());
            log.info("Parametros: " + parameters.toString());
            jasperReportUtil = new JasperReportUtil();
            byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
                            .stream(() -> input).build();
            log.info("El usuario {} descargo el reporte de tarimas {}", this.usuario.getUsuario(), filename);
        }catch (Exception ex) 
        {
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
    
    public void exportarExcel() throws JRException, IOException, SQLException 
    {
        log.info("Exportando a excel.....");
        String jasperPath = "/jasper/InventarioTarimas.jrxml";
        String filename = "InventarioTarimas" + this.fecha + ".xlsx";
        String images = "/images/logoF.png";
        String message = null;
        FacesMessage.Severity severity = null;
        File reportFile = new File(jasperPath);
        File imgfile = null;
        JasperReportUtil jasperReportUtil = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        Connection connection = null;
        parameters = new HashMap<String, Object>();
        
        try 
        {
            URL resource = getClass().getResource(jasperPath);
            URL resourceimg = getClass().getResource(images);
            String file = resource.getFile();
            String img = resourceimg.getFile();
            reportFile = new File(file);
            imgfile = new File(img);
            log.info(reportFile.getPath());
            
            Integer clienteCve = null;
            if (this.clienteSelected == null) {
                    clienteCve = null;
            } else {
                    clienteCve = this.clienteSelected.getCteCve();
            }
            
            Integer plantaCve = null;
            if (this.plantaSelected == null) {
                    plantaCve = null;
            } else {
                    plantaCve = this.plantaSelected.getPlantaCve();
            }
            
            connection = EntityManagerUtil.getConnection();
            parameters.put("REPORT_CONNECTION", connection);
            parameters.put("idCliente", clienteCve);
            parameters.put("Planta", plantaCve);
            parameters.put("Fecha", this.fecha);
            parameters.put("imagen", imgfile.getPath());
            log.info("Parametros: " + parameters.toString());
            jasperReportUtil = new JasperReportUtil();
            byte[] bytes = jasperReportUtil.createXLSX(parameters, reportFile.getPath());
            InputStream input = new ByteArrayInputStream(bytes);
            this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename)
                            .stream(() -> input).build();
            log.info("El usuario {} descargo el reporte de tarimas {}", this.usuario.getUsuario(), filename);
        }catch (Exception ex) 
        {
            log.error("Problema general...", ex);
            message = String.format("No se pudo imprimir el reporte");
            severity = FacesMessage.SEVERITY_INFO;
            FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(severity, "Error en impresion", message));
            PrimeFaces.current().ajax().update("form:messages");
        } finally 
        {
            conexion.close((Connection) connection);
        }
    }

    public Cliente getClienteSelected() {
        return clienteSelected;
    }

    public void setClienteSelected(Cliente clienteSelected) {
        this.clienteSelected = clienteSelected;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public Planta getPlantaSelected() {
        return plantaSelected;
    }

    public void setPlantaSelected(Planta plantaSelected) {
        this.plantaSelected = plantaSelected;
    }

    public List<Planta> getListaPlanta() {
        return listaPlanta;
    }

    public void setListaPlanta(List<Planta> listaPlanta) {
        this.listaPlanta = listaPlanta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public List<RepTarimas> getRepTarimas() { 
        return repTarimas; 
    }

    public List<String> getColumnasPlantas() {
        return columnasPlantas;
    }

    public void setColumnasPlantas(List<String> listaColumnasPlantas) {
        this.columnasPlantas = listaColumnasPlantas;
    }

    public List<Map<String, Object>> getModeloTabla() {
        return modeloTabla;
    }

    public void setModeloTabla(List<Map<String, Object>> modeloTabla) {
        this.modeloTabla = modeloTabla;
    }

    public List<Integer> getTotalesTarimasPlantas() {
        return totalesTarimasPlantas;
    }

    public void setTotalesTarimasPlantas(List<Integer> totalesTarimasPlantas) {
        this.totalesTarimasPlantas = totalesTarimasPlantas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
}
