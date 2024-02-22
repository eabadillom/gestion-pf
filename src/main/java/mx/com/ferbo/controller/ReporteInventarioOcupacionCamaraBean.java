package mx.com.ferbo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
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

import mx.com.ferbo.dao.CamaraDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.RepOcupacionCamaraDAO;
import mx.com.ferbo.model.Camara;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteInventarioOcupacionCamaraBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteInventarioOcupacionCamaraBean.class);

	Integer idCliente = null;
	Integer idPlanta = null;
	Integer idCamara = null;
	
	private PieChartModel pieModel;
	private PieChartModel model;
	
	private Date fecha;

	private Planta plantaSelect;
	private Camara camaraSelect;
	private Cliente clienteSelect;

	private List<Cliente> listaClientes;
	private List<Planta> listaPlanta;
	private List<Camara> listaCamara;
	private List<OcupacionCamara> listaOcupacionCamara;

	private PlantaDAO plantaDAO;
	private CamaraDAO camaraDAO;
	private RepOcupacionCamaraDAO ocupacionCamaraDAO;
	
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	private Usuario usuario;
	
	public ReporteInventarioOcupacionCamaraBean() {
		
		fecha = new Date();

		plantaDAO = new PlantaDAO();
		camaraDAO = new CamaraDAO();
		ocupacionCamaraDAO = new RepOcupacionCamaraDAO();

		listaClientes = new ArrayList<Cliente>();
		listaPlanta = new ArrayList<Planta>();
		listaCamara = new ArrayList<Camara>();		
		listaOcupacionCamara = new ArrayList<OcupacionCamara>();
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
		
		plantaSelect = new Planta();
		camaraSelect = new Camara();
		clienteSelect = new Cliente();
		
//		listaClientes = clienteDAO.buscarHabilitados(true);
		listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		
		if((usuario.getPerfil()==1)||(usuario.getPerfil()==4)) {
			listaPlanta.add(plantaDAO.buscarPorId(usuario.getIdPlanta()));
		}else {
			listaPlanta = plantaDAO.buscarTodos();
		}
		
		filtradoCamara();
		createPieModel();
	}
	
	public void filtradoCamara() {
		
		if(plantaSelect!=null) {
		
			listaCamara = camaraDAO.buscarPorPlanta(plantaSelect);
		
			plantaSelect.setCamaraList(listaCamara);
		}
		
		//ocupacionCamara();
		
	}
	
	public void ocupacionCamara() throws InventarioException{
		
		idCliente = null;
		idPlanta = null;
		idCamara = null;
		
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Reporte entradas";
		
		try {
			
			if(clienteSelect!=null) {
				idCliente = clienteSelect.getCteCve();
			}else {
				throw new InventarioException("Debe seleccionar un cliente");
			}
			
			if(plantaSelect!=null) {
				idPlanta = plantaSelect.getPlantaCve();
			}else {
				throw new InventarioException("Debe seleccionar una planta");
			}
			
			if(camaraSelect!=null) {
				idCamara = camaraSelect.getCamaraCve();
			}else {
				throw new InventarioException("Debe seleccionar una camara");
			}
			
			
			listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, idCliente, idPlanta, idCamara);
			
		}catch(InventarioException ex) {
			mensaje = ex.getMessage();
			
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
		}catch(Exception e) {
			mensaje = e.getMessage();
			
			severity = FacesMessage.SEVERITY_WARN;
			
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);			
		}finally {	
			PrimeFaces.current().ajax().update("form:dt-OcupacionCamara", "form:messages");
		}
		
		//createPieModel();
		//System.out.println(listaOcupacionCamara.get(0).getTarima());
	}
	
	
	public void exportarPdf() throws JRException, IOException, SQLException{
		System.out.println("Exportando a pdf.....");
			String jasperPath = "/jasper/OcupacionCamara.jrxml";
			String filename = "Ocupacion Camaras" +fecha+".pdf";
			String images = "/images/logo.jpeg";
			String message = null;
			Severity severity = null;
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				Integer clienteCve = null;
				if(clienteSelect == null) {
					clienteCve = null;
				}else {
					clienteCve = clienteSelect.getCteCve();
			}
				
				Integer camaraCve = null;
				if(camaraSelect == null) {
					camaraCve= null;
				}else {
					camaraCve= camaraSelect.getCamaraCve();
				}
			
				Integer plantaCve = null;
				if(plantaSelect == null) {
				plantaCve = null;
				}else {
					plantaCve = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("Camara", camaraCve);
				parameters.put("Planta", plantaCve);
				parameters.put("Fecha",fecha );
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createPdf(filename, parameters, reportFile.getPath());
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
	
	
	public void exportarExcel() throws JRException, IOException, SQLException{
		System.out.println("Exportando a pdf.....");
			String jasperPath = "/jasper/OcupacionCamara.jrxml";
			String filename = "Ocupacion Camaras" +fecha+".xlsx";
			String images = "/images/logo.jpeg";
			String message = null;
			Severity severity = null;
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				Integer clienteCve = null;
				if(clienteSelect == null) {
					clienteCve = null;
				}else {
					clienteCve = clienteSelect.getCteCve();
			}
				
				Integer camaraCve = null;
				if(camaraSelect == null) {
					camaraCve= null;
				}else {
					camaraCve= camaraSelect.getCamaraCve();
				}
			
				Integer plantaCve = null;
				if(plantaSelect == null) {
				plantaCve = null;
				}else {
					plantaCve = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
				parameters.put("idCliente",clienteCve );
				parameters.put("Camara", camaraCve);
				parameters.put("Planta", plantaCve);
				parameters.put("Fecha",fecha );
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				jasperReportUtil.createXlsx(filename, parameters, reportFile.getPath());
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
	
	
	public void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();
        Random rnd = new Random();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        
        Integer size = listaOcupacionCamara.size();
        Integer i = 0;
        
        List<String> labels = new ArrayList<>();
        
        for(OcupacionCamara oc: listaOcupacionCamara) {
        	
        	if(i < size) {
        		values.add(oc.getPosiciones_Disponibles());//solo se deberian de ver saldos positivos?
        		labels.add(oc.getPlanta_ds()+"- "+oc.getCamara_ds());
        		i++;
        	}
        	
        }
        
        dataSet.setData(values);
        
        List<Integer> rgb = null;
        List<String> bgColors = new ArrayList<>();
        Integer numero;
        
        for(int bgcolor = 0 ; bgcolor < size;bgcolor++ ) { //repito las acciones la veces del tamaño de mi lista de ocupacionCamaras son los objetos que se van a crear de bgColors
        	
        	rgb = new ArrayList<Integer>();
        	
        	for(int color = 0; color < 3;color++) {//creo 3 numeros random tomando en cuenta el rango de valores RGB        		
        		numero = (int)(rnd.nextDouble()*256);
        		rgb.add(numero);
        	}
        	
        	bgColors.add("rgb("+rgb.get(0).toString()+","+rgb.get(1).toString()+","+rgb.get(2).toString()+")");//le agrego los datos a la lista bgColors
        	log.info(bgColors.get(bgcolor));
        }
        
        dataSet.setBackgroundColor(bgColors);
        data.addChartDataSet(dataSet);        
        data.setLabels(labels);
        
        PieChartOptions options = new PieChartOptions();
        
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

	public Camara getCamaraSelect() {
		return camaraSelect;
	}

	public void setCamaraSelect(Camara camaraSelect) {
		this.camaraSelect = camaraSelect;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
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

	public List<Camara> getListaCamara() {
		return listaCamara;
	}

	public void setListaCamara(List<Camara> listaCamara) {
		this.listaCamara = listaCamara;
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
	
	
	
	
}
