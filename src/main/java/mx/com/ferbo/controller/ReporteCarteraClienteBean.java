package mx.com.ferbo.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.conexion;
import com.ferbo.gestion.reports.jasper.CarteraClienteJR;
import com.ferbo.gestion.reports.util.TipoCarteraCliente;
import mx.com.ferbo.util.InventarioException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@ViewScoped
public class ReporteCarteraClienteBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteCarteraClienteBean.class);
	
	private List<Cliente> listClientesSelect;
        private List<Cliente> listCliente;
	private List<EmisoresCFDIS> emisorList;
	private EmisoresCFDIS emisorSelect;
	private EmisoresCFDISDAO emisoresDAO;
	
	private Date fecha;
	private Date maxDate;
	private String consulta = null;
	
	private FacesContext faceContext;
    private HttpServletRequest request;
        private Usuario usuario;
        
        private CarteraClienteJR carteraClienteJR = null;
        private StreamedContent file;
	
	public ReporteCarteraClienteBean() {
		listClientesSelect = new ArrayList();
		listCliente = new ArrayList<Cliente>();
		emisoresDAO = new EmisoresCFDISDAO();
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(){
		this.faceContext = FacesContext.getCurrentInstance();
        this.request = (HttpServletRequest) faceContext.getExternalContext().getRequest();
                this.usuario = (Usuario) request.getSession(false).getAttribute("usuario");
		
		this.listCliente = (List<Cliente>) request.getSession(false).getAttribute("clientesActivosList");
		this.emisorList = emisoresDAO.buscarTodos();
		this.emisorSelect = new EmisoresCFDIS();
		
		fecha = new Date();
		Date today = new Date();

		maxDate = new Date(today.getTime() );
                log.info("El usuario {} esta ingresando a reporte de cartera de clientes", usuario.getUsuario());
	}

	public void exportarPdf() {
		String filename = null;
		String images = "/images/logo.jpeg";
		String message = null;
		Severity severity = null;
		Connection connection = null;
		
                try {
                    if(consulta == null || consulta.isEmpty()){
                        throw new InventarioException("Debe seleccionar el tipo de filtro de descarga");
                    }
                    
                    List<Integer> listClientes = null;
                    if (this.listClientesSelect != null || !this.listClientesSelect.isEmpty()) {
                        listClientes = new ArrayList();
                        for(Cliente cliente : listClientesSelect) {
                            listClientes.add(cliente.getCteCve());
                        }
                    }

                    String emisorRFC = null;
                    if(emisorSelect != null)
                        emisorRFC = emisorSelect.getNb_rfc();
                    
                    connection = EntityManagerUtil.getConnection();
                    
                    if (consulta.equals("Concentrada")) {
                        log.info("Exportando Cartera de clientes concentrada a pdf...");
                        filename = String.format("Concentrada%s.pdf", fecha);
                        
                        carteraClienteJR = new CarteraClienteJR(connection, images, TipoCarteraCliente.CONCENTRADO);
                        byte[] bytes = carteraClienteJR.getPDF(listClientes, emisorRFC, fecha);
                        InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/pdf")
                            .name(filename)
                            .stream(() -> input)
                            .build();
                        log.info("El usuario {} descargo el reporte de cartera de clientes {}", this.usuario.getUsuario(), filename);
                    } else if(consulta.equals("Desglosado")) {
                        log.info("Exportando Cartera de clientes desglosada a pdf...");
                        filename = String.format("Desglosada%s.pdf", fecha);

                        carteraClienteJR = new CarteraClienteJR(connection, images, TipoCarteraCliente.DESGLOSADO);
                        byte[] bytes = carteraClienteJR.getPDF(listClientes, emisorRFC, fecha);
                        InputStream input = new ByteArrayInputStream(bytes);
                        this.file = DefaultStreamedContent.builder().contentType("application/pdf")
                            .name(filename)
                            .stream(() -> input)
                            .build();
                        log.info("El usuario {} descargo el reporte de cartera de clientes {}", this.usuario.getUsuario(), filename); 
                    }
                } catch (Exception ex) {
                    log.error("Problema general...", ex);
                    message = String.format("No se puede descargar el reporte");
                    severity = FacesMessage.SEVERITY_ERROR;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
                } finally {
                    conexion.close((Connection) connection);
                    PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
                }
	}
	
	public void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
    }
	
	public void exportarExcel() {
			log.info("Exportando cartera de clientes concentrada a excel...");
			String filename = null;
			String images = "/images/logo.jpeg";
			String message = null;
			Severity severity = null;
			Connection connection = null;
                        
                        try {
                            if(consulta == null || consulta.isEmpty()){
                                throw new InventarioException("Debe seleccionar el tipo de filtro de descarga");
                            }
                            
                            List<Integer> listClientes = null;
                            if (this.listClientesSelect != null || !this.listClientesSelect.isEmpty()) {
                                listClientes = new ArrayList();
                                for(Cliente cliente : listClientesSelect) {
                                    listClientes.add(cliente.getCteCve());
                                }
                            }

                            String emisorRFC = null;
                            if(emisorSelect != null)
                                emisorRFC = emisorSelect.getNb_rfc();
                            
                            connection = EntityManagerUtil.getConnection();
                            
                            if (consulta.equals("Concentrada")) {
                                log.info("Exportando cartera de clientes concentrada a excel...");
                                filename = String.format("Concentrada%s.xlsx", fecha);

                                carteraClienteJR = new CarteraClienteJR(connection, images, TipoCarteraCliente.CONCENTRADO);
                                byte[] bytes = carteraClienteJR.getXLSX(listClientes, emisorRFC, fecha);
                                InputStream input = new ByteArrayInputStream(bytes);
                                this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel")
                                    .name(filename)
                                    .stream(() -> input)
                                    .build();
                                log.info("El usuario {} descargo el reporte de cartera de clientes desglosada {}", this.usuario.getUsuario(), filename);
                            } else if(consulta.equals("Desglosado")) {
                                log.info("Exportando cartera de clientes desglosada a excel...");
                                filename = String.format("Desglosada%s.xlsx", fecha);
                                
                                carteraClienteJR = new CarteraClienteJR(connection, images, TipoCarteraCliente.DESGLOSADO);
                                byte[] bytes = carteraClienteJR.getXLSX(listClientes, emisorRFC, fecha);
                                InputStream input = new ByteArrayInputStream(bytes);
                                this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel")
                                    .name(filename)
                                    .stream(() -> input)
                                    .build();
                                log.info("El usuario {} descargo el reporte de cartera de clientes desglosada {}", this.usuario.getUsuario(), filename);
                            }
                        } catch (Exception ex) {
                            log.error("Problema general...", ex);
                            message = String.format("No se puede descargar el reporte");
                            severity = FacesMessage.SEVERITY_ERROR;
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "Error en impresion", message));
                        } finally {
                            conexion.close((Connection) connection);
                            PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
                        }
		    
		}
        
        public String getClientesSeleccionadosLabel()
        {
            if (listClientesSelect == null || listClientesSelect.isEmpty())
            {
                return "Todos los clientes";
            }

            int size = listClientesSelect.size();
            return size == 1 ? "1 cliente seleccionado" : size + " clientes seleccionados";
        }
        
        public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

        public List<Cliente> getListClientesSelect() {
            return listClientesSelect;
        }

        public void setListClientesSelect(List<Cliente> listClientesSelect) {
            this.listClientesSelect = listClientesSelect;
        }

	public List<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(List<Cliente> listCliente) {
		this.listCliente = listCliente;
	}
        
	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public List<EmisoresCFDIS> getEmisorList() {
		return emisorList;
	}

	public void setEmisorList(List<EmisoresCFDIS> emisorList) {
		this.emisorList = emisorList;
	}

	public EmisoresCFDIS getEmisorSelect() {
		return emisorSelect;
	}

	public void setEmisorSelect(EmisoresCFDIS emisorSelect) {
		this.emisorSelect = emisorSelect;
	}

        public StreamedContent getFile() {
            return file;
        }

        public void setFile(StreamedContent file) {
            this.file = file;
        }
	
}
