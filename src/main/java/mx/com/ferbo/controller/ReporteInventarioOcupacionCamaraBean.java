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
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;
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
	
	private BarChartModel modelBar;
	
	
	private Date fecha;

	private Planta plantaSelect;
	private Planta plantaGrafica;
	private Boolean selectPlanta;
	private Boolean general;
	private Camara camaraSelect;
	private Cliente clienteSelect;
	private Boolean selectCamara;

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
		
		selectPlanta = true;
		selectCamara = true;
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
			if(plantaSelect.getPlantaCve()!=null) {
				listaCamara = camaraDAO.buscarPorPlanta(plantaSelect);
			
				plantaSelect.setCamaraList(listaCamara);
				selectPlanta = false;
				general = true;
			}else {		
				
				selectPlanta = true;
				general = false;
			}			
		}else {
			selectPlanta = true;
		}
				
		//ocupacionCamara();		
	}
	
	public void seleccionCamara() {
		
		if(camaraSelect!=null) {
			
			if(camaraSelect.getCamaraCve()!= null) {
				selectCamara = false;
			}else {
				selectCamara = true;
				general = false;
			}
			
		}else {
			selectCamara = true;
			general = false;
		}
		
		
	}
	
	public void ocupacionCamara() throws InventarioException{//se debe de modificar para obtener grafica general
		
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
				//throw new InventarioException("Debe seleccionar un cliente");
				clienteSelect = new Cliente();
				idCliente = clienteSelect.getCteCve();
			}
			
			if(plantaSelect!=null) {
				idPlanta = plantaSelect.getPlantaCve();
			}else {
				//throw new InventarioException("Debe seleccionar una planta");
				plantaSelect = new Planta();
				idPlanta = plantaSelect.getPlantaCve();
			}
			
			if(camaraSelect!=null) {
				idCamara = camaraSelect.getCamaraCve();
			}else {
				//throw new InventarioException("Debe seleccionar una camara");
				camaraSelect = new Camara();
				idCamara = camaraSelect.getCamaraCve();
			}
			
			
			listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, idCliente, idPlanta, idCamara);
			
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
	
	
	/*public void createPieModelCake() {
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
        
    }*/
	
	public void createPieModel2() {
		
		
		modelBar = new BarChartModel();		
		ChartData data = new ChartData();
		
		BarChartDataSet dataSetP1 = new BarChartDataSet();
		BarChartDataSet dataSetP2 = new BarChartDataSet();
		
		dataSetP1.setLabel("Posiciones Disponibles");
		dataSetP1.setBackgroundColor("rgb(255, 99, 132)");
		dataSetP1.setStack("Stack 0");
		
		/*dataSetP2.setLabel("Planta 2");
        dataSetP2.setBackgroundColor("rgb(54, 162, 235)");
        dataSetP2.setStack("Stack 0");*/
		
		List<Number> valuesP1 = new ArrayList<>();
		//List<Number> valuesP2 = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for(OcupacionCamara oc: listaOcupacionCamara) {	
			
			if(oc.getPlanta_ds().equals("P1 CENTRAL DE ABASTOS")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P2 TEPALCATES")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P3 CENTRAL DE ABASTOS")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P4 URBANA IXHUATEPEC")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P5 ORO")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
		}
		
		dataSetP1.setData(valuesP1);
		//dataSetP2.setData(valuesP2);
		
		data.addChartDataSet(dataSetP1);
		//data.addChartDataSet(dataSetP2);	
		data.setLabels(labels);
		modelBar.setData(data);
		
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(true);
        linearAxes.setOffset(true);        
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
     
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Ocupación de cámaras");
        options.setTitle(title);

        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(false);
        options.setTooltip(tooltip);

        modelBar.setOptions(options);
		
		
	}
	
	
	// PROPUESTA 2 --------------------------------------------
	
	public void graficaPorPlanta() {
		if(plantaSelect.getPlantaCve()!=null) {
			listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, idCliente, plantaSelect.getPlantaCve(), null);
		}
		
		createPieModel3();
	}
	
	public void graficaPorCamara() {
		if(plantaSelect.getPlantaCve()!=null && camaraSelect!=null) {
			listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, idCliente, plantaSelect.getPlantaCve(), camaraSelect.getCamaraCve());
		}
		createPieModel();
	}
	
	public void createPieModel() {
		
		
	
			modelBar = new BarChartModel();		
			ChartData data = new ChartData();
			
			BarChartDataSet dataSetP1 = new BarChartDataSet();
			BarChartDataSet dataSetP2 = new BarChartDataSet();
			
			dataSetP1.setLabel("Posiciones Disponibles");
			dataSetP1.setBackgroundColor("rgb(255, 99, 132)");
			dataSetP1.setStack("Stack 0");
			
			dataSetP2.setLabel("Posiciones Ocupadas");
	        dataSetP2.setBackgroundColor("rgb(54, 162, 235)");
	        dataSetP2.setStack("Stack 0");
			
			List<Number> valuesP1 = new ArrayList<>();
			List<Number> valuesP2 = new ArrayList<>();
			List<String> labels = new ArrayList<>();
			
			for(OcupacionCamara oc: listaOcupacionCamara) {	
				
				if(oc.getPlanta_ds().equals(plantaSelect.getPlantaDs())) {
					valuesP1.add(oc.getPosiciones_Disponibles());
					valuesP2.add(oc.getTarima());
					labels.add(oc.getCamara_ds());
				}
				
				
			}
			
			dataSetP1.setData(valuesP1);
			dataSetP2.setData(valuesP2);
			
			data.addChartDataSet(dataSetP1);
			data.addChartDataSet(dataSetP2);	
			data.setLabels(labels);
			modelBar.setData(data);
			
			BarChartOptions options = new BarChartOptions();
	        CartesianScales cScales = new CartesianScales();
	        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
	        linearAxes.setStacked(true);
	        linearAxes.setOffset(true);
	        cScales.addXAxesData(linearAxes);
	        cScales.addYAxesData(linearAxes);
	        options.setScales(cScales);
	
	        Title title = new Title();
	        title.setDisplay(true);
	        title.setText("Ocupación de cámaras");
	        options.setTitle(title);
	        
	        modelBar.setExtender("chartExtender");
	
	        Tooltip tooltip = new Tooltip();
	        tooltip.setMode("index");
	        tooltip.setIntersect(false);
	        options.setTooltip(tooltip);
	
	        modelBar.setOptions(options);
		
		
		
	}
	
	public void createPieModel3(){
		
		modelBar = new BarChartModel();
		
		ChartData data = new ChartData();
		
		BarChartDataSet dataSetP1 = new BarChartDataSet();
		BarChartDataSet dataSetP2 = new BarChartDataSet();
		
		dataSetP1.setLabel("Posiciones Disponibles");		
		dataSetP1.setBackgroundColor("rgb(255, 99, 132)");
		dataSetP1.setStack("Stack 0");
		
		/*dataSetP2.setLabel("Planta 2");
        dataSetP2.setBackgroundColor("rgb(54, 162, 235)");
        dataSetP2.setStack("Stack 0");*/
		
		List<Number> valuesP1 = new ArrayList<>();
		//List<Number> valuesP2 = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		
		for(OcupacionCamara oc: listaOcupacionCamara) {	
			
			if(oc.getPlanta_ds().equals("P1 CENTRAL DE ABASTOS")) {
				valuesP1.add(oc.getPosiciones_Disponibles());				
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P2 TEPALCATES")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P3 CENTRAL DE ABASTOS")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P4 URBANA IXHUATEPEC")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
			if(oc.getPlanta_ds().equals("P5 ORO")) {
				valuesP1.add(oc.getPosiciones_Disponibles());
				labels.add(oc.getPlanta_ds()+":"+oc.getCamara_ds());
			}
			
		}
		
		dataSetP1.setData(valuesP1);
		//dataSetP2.setData(valuesP2);
		
		data.addChartDataSet(dataSetP1);
		//data.addChartDataSet(dataSetP2);	
		data.setLabels(labels);
		
		modelBar.setData(data);
		
		BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setStacked(true);
        linearAxes.setOffset(true);        
        cScales.addXAxesData(linearAxes);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Ocupación de cámaras");
        options.setTitle(title);
        
        /*Legend legend = new Legend();
        legend.setDisplay(true);        
        legend.setPosition("top");
        LegendLabel legendLabels = new LegendLabel();
        legendLabels.setFontStyle("bold");
        legendLabels.setFontColor("#2980B9");
        legendLabels.setFontSize(24);
        legend.setLabels(legendLabels);
        options.setLegend(legend);*/
        
        modelBar.setExtender("chartExtender");
        
        Tooltip tooltip = new Tooltip();
        tooltip.setMode("index");
        tooltip.setIntersect(true);        
        options.setTooltip(tooltip);
        options.setOffsetGridLines(false);
        
        modelBar.setOptions(options);
		
		
		
		
		
		
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

	public BarChartModel getModelBar() {
		return modelBar;
	}

	public void setModelBar(BarChartModel modelBar) {
		this.modelBar = modelBar;
	}

	public Planta getPlantaGrafica() {
		return plantaGrafica;
	}

	public void setPlantaGrafica(Planta plantaGrafica) {
		this.plantaGrafica = plantaGrafica;
	}

	public Boolean getSelectPlanta() {
		return selectPlanta;
	}

	public void setSelectPlanta(Boolean selectPlanta) {
		this.selectPlanta = selectPlanta;
	}

	public Boolean getGeneral() {
		return general;
	}

	public void setGeneral(Boolean general) {
		this.general = general;
	}

	public Boolean getSelectCamara() {
		return selectCamara;
	}

	public void setSelectCamara(Boolean selectCamara) {
		this.selectCamara = selectCamara;
	}
	
	
	
	
}
