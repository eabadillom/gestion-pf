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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.optionconfig.legend.LegendLabel;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

import mx.com.ferbo.dao.n.ClienteDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RepOcupacionCamaraDAO;
import mx.com.ferbo.graficas.OcupacionChart;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.OcupacionCamara;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.JasperReportUtil;
import mx.com.ferbo.util.conexion;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.optionconfig.title.Title;

@Named
@ViewScoped
public class ReporteInventarioOcupacionCamaraBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteInventarioOcupacionCamaraBean.class);
        
        @Inject
        private ClienteDAO clientaDAO;
        
        @Inject
        private PlantaDAO plantaDAO;
        
        @Inject
        private RepOcupacionCamaraDAO ocupacionCamaraDAO;

	private PieChartModel pieModel;
	private PieChartModel model;
	
	private Cliente clienteSelect;
	private List<Cliente> listaClientes;
	
        private Planta plantaSelect;
        private List<Planta> listaPlanta;
        
	private List<OcupacionCamara> listaOcupacionCamara;

	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;
	private Usuario usuario;
        
        private final Locale localeMx = new Locale("es", "MX");
        private StreamedContent file;
	private Date fecha;
        private BigDecimal totalPosicionesPorCliente;
        
	public ReporteInventarioOcupacionCamaraBean() {
		fecha = new Date();

		listaClientes = new ArrayList<Cliente>();
		listaPlanta = new ArrayList<Planta>();
		listaOcupacionCamara = new ArrayList<OcupacionCamara>();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		faceContext = FacesContext.getCurrentInstance();
		httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		usuario = (Usuario) httpServletRequest.getSession(false).getAttribute("usuario");
                log.info("El usuario {} ha ingresado a reporte de ocupación de posiciones", usuario.getUsuario());
		
		//listaClientes = clienteDAO.buscarHabilitados(true);
		listaClientes = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesActivosList");
		
		if((usuario.getPerfil()==1)||(usuario.getPerfil()==4)) {
                    Planta auxPlanta = plantaDAO.buscarPorId(usuario.getIdPlanta()).get();
                    listaPlanta.add(auxPlanta);
                    log.info("Entro el usuario {} con perfil de almacen", usuario.getUsuario());
		}else {
                    listaPlanta = plantaDAO.buscarTodos(Boolean.FALSE);
		}
	}
	
	public void ocupacionCamara() {
		Integer idCliente = null;
		Integer idPlanta = null;
		
		try {
			
			if(clienteSelect != null) {
                            idCliente = clienteSelect.getCteCve();
                            log.info("El usuario {} ha seleccionado al cliente {}", this.usuario.getUsuario(), this.clienteSelect.getNombre());
			}else {
                            log.info("El usuario {} no seleccionó un cliente", this.usuario.getUsuario());
                            //throw new InventarioException("Debe seleccionar un cliente");
			}
			
			if(plantaSelect != null) {
                            idPlanta = plantaSelect.getPlantaCve();
                            log.info("El usuario {} ha seleccionado a la planta {}", this.usuario.getUsuario(), this.plantaSelect.getPlantaDs());
			}else {
                            log.info("El usuario {} no seleccionó una planta", this.usuario.getUsuario());
                            //throw new InventarioException("Debe seleccionar una planta");
			}
			
			listaOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, idCliente, idPlanta);
		} /*catch(InventarioException ex) {
                        log.error("Problema para descargar el reporte...", ex);
			FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Reporte de camaras", ex.getMessage());
		}*/ catch(Exception e) {
                        log.error("Problema para descargar el reporte...", e);
			FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Reporte de camaras", "Error al generar el reporte, consulte con su administrados de sistemas");
		} finally {	
			PrimeFaces.current().ajax().update("form:dt-OcupacionCamara", "form:messages");
		}
		
		//createPieModel();
		//System.out.println(listaOcupacionCamara.get(0).getTarima());
	}
        
        public List<String> getClientesUnicos() {
            return listaOcupacionCamara.stream()
                .map(OcupacionCamara::getCte_nombre)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        }
	
        public List<OcupacionCamara> plantasPorCliente(String cliente) {
            return listaOcupacionCamara.stream()
                .filter(o -> o.getCte_nombre().equals(cliente))
                .collect(Collectors.toList());
        }
        
        public BigDecimal totalPorCliente(String cliente) {
            return listaOcupacionCamara.stream()
                .filter(o -> o.getCte_nombre().equals(cliente))
                .map(OcupacionCamara::getTarima)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
	
	public void exportarPdf() throws JRException, IOException, SQLException{
                        log.info("Exportando a pdf.....");
			String jasperPath = "/jasper/OcupacionCamara.jrxml";
			String filename = "Ocupacion Camaras" +fecha+".pdf";
			String images = "/images/logo.jpeg";
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
                        Integer idCliente = null;
                        Integer idPlanta = null;
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				if(clienteSelect == null) {
					idCliente = null;
				}else {
					idCliente = clienteSelect.getCteCve();
			}
				
				if(plantaSelect == null) {
                                    idPlanta = null;
				}else {
                                    idPlanta = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
                                parameters.put("REPORT_LOCALE", localeMx);
				parameters.put("idCliente", idCliente);
				parameters.put("idPlanta", idPlanta);
				parameters.put("fecha", fecha);
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
                                InputStream input = new ByteArrayInputStream(bytes);
                                this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
                                    .stream(() -> input).build();
                                log.info("El usuario {} descargo el reporte de domicilios por cliente {}", this.usuario.getUsuario(), filename);
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Reporte de camaras", "Error al generar el reporte, consulte con su administrados de sistemas");
				PrimeFaces.current().ajax().update("form:messages");
			} finally {
				conexion.close((Connection) connection);
			}
			}

		
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	
	
	public void exportarExcel() throws JRException, IOException, SQLException{
                        log.info("Exportando a pdf.....");
			String jasperPath = "/jasper/OcupacionCamara.jrxml";
			String filename = "Ocupacion Camaras" +fecha+".xlsx";
			String images = "/images/logo.jpeg";
			File reportFile = new File(jasperPath);
			File imgfile = null;
			JasperReportUtil jasperReportUtil = new JasperReportUtil();
			Map<String, Object> parameters = new HashMap<String, Object>();
			Connection connection = null;
			parameters = new HashMap<String, Object>();
                        Integer idCliente = null;
                        Integer idPlanta = null;
			
			try {
			
				URL resource = getClass().getResource(jasperPath);
				URL resourceimg = getClass().getResource(images);
				String file = resource.getFile();
				String img = resourceimg.getFile();
				reportFile = new File(file);
				imgfile = new File(img);
				log.info(reportFile.getPath());
			
				if(clienteSelect == null) {
					idCliente = null;
				}else {
					idCliente = clienteSelect.getCteCve();
                                }
				
				if(plantaSelect == null) {
                                    idPlanta = null;
				}else {
                                    idPlanta = plantaSelect.getPlantaCve();
				}
			
				connection = EntityManagerUtil.getConnection();
				parameters.put("REPORT_CONNECTION", connection);
                                parameters.put("REPORT_LOCALE", localeMx);
				parameters.put("idCliente", idCliente);
				parameters.put("idPlanta", idPlanta);
				parameters.put("fecha", fecha);
				parameters.put("imagen", imgfile.getPath());
				log.info("Parametros: " + parameters.toString());
				byte[] bytes = jasperReportUtil.createPDF(parameters, reportFile.getPath());
                                InputStream input = new ByteArrayInputStream(bytes);
                                this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename)
                                    .stream(() -> input).build();
                                log.info("El usuario {} descargo el reporte de domicilios por cliente {}", this.usuario.getUsuario(), filename);
			} catch (Exception ex) {
				log.error("Problema general...", ex);
				FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Reporte de camaras", "Error al generar el reporte, consulte con su administrados de sistemas");
                                PrimeFaces.current().ajax().update("form:messages");
			} finally {
				conexion.close((Connection) connection);
			}
			}
	
	
    public void createPieModel(String nombreCliente) {
        log.info("Generando la grafica de las ocupaciones por cliente");
        List<OcupacionCamara> listOcupacionCamara = null;
        
        try {
            Cliente cliente = clientaDAO.buscarPorNombre(nombreCliente);

            listOcupacionCamara = ocupacionCamaraDAO.ocupacionCamara(fecha, cliente.getCteCve(), plantaSelect != null ? plantaSelect.getPlantaCve() : null);
            
            pieModel =  OcupacionChart.build(listOcupacionCamara, nombreCliente);
            
            totalPosicionesPorCliente = listOcupacionCamara.stream()
                .map(OcupacionCamara::getTarima)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception ex) {
            log.error("Problema general...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Reporte de camaras", "Error al generar la grafica, consulte con su administrados de sistemas");
            PrimeFaces.current().ajax().update("form:messages");
        }
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

        public BigDecimal getTotalPosicionesPorCliente() {
            return totalPosicionesPorCliente;
        }

        public void setTotalPosicionesPorCliente(BigDecimal totalPosicionesPorCliente) {
            this.totalPosicionesPorCliente = totalPosicionesPorCliente;
        }

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }
	
	
}
