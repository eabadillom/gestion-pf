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
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import mx.com.ferbo.business.GestionApiClientBL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.RepInventarioDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.ui.RepInventario;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.util.conexion;
import com.ferbo.gestion.reports.jasper.ReporteInventarioJR;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;

@Named
@ViewScoped
public class ReporteInventarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = LogManager.getLogger(ReporteInventarioBean.class);
        
        @Inject
        private RepInventarioDAO reporteDAO;
        
        @Inject
        private PlantaDAO plantaDAO;

	private Date fecha;

	private Planta plantaSelect;
	private List<Cliente> listClienteSelect;
	private List<Cliente> listaClientes;
	private List<Planta> listaPlanta;
	
	private FacesContext context;
	private HttpServletRequest request;
	private Usuario usuario;
	private List<RepInventario> reporte;
	
        private ReporteInventarioJR reporteInventarioJR = null;
	private StreamedContent file;

	public ReporteInventarioBean() {
		fecha = new Date();

		listaClientes = new ArrayList<Cliente>();
		listaPlanta = new ArrayList<Planta>();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {

		this.context = FacesContext.getCurrentInstance();
		this.request = (HttpServletRequest) context.getExternalContext().getRequest();
		this.usuario = (Usuario) request.getSession(false).getAttribute("usuario");

		this.plantaSelect = new Planta();
		this.listClienteSelect = new ArrayList();
		this.listaClientes = (List<Cliente>) this.request.getSession(false).getAttribute("clientesActivosList");

		if ((this.usuario.getPerfil() == 1) || (this.usuario.getPerfil() == 4)) {
			this.listaPlanta.add(this.plantaDAO.findById(this.usuario.getIdPlanta()));
			this.plantaSelect = this.listaPlanta.get(0);
		} else {
			listaPlanta = plantaDAO.buscarTodos(false);
		}
                
                log.info("El usuario {} esta ingresando a reporte de inventario", usuario.getUsuario());
	}

	public void exportarPdf() throws JRException, IOException, SQLException {
		log.info("Exportando a pdf.....");
                String filename = "InventarioAlmacen" + fecha + ".pdf";
                String images = "/images/logoF.png";
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
			
			if( (this.usuario.getPerfil() == 1 || this.usuario.getPerfil() == 4) && plantaCve == null )
				throw new InventarioException("Debe seleccionar una planta.");

			connection = EntityManagerUtil.getConnection();
			reporteInventarioJR = new ReporteInventarioJR(connection, images);
                        byte[] bytes = reporteInventarioJR.getPDFReporteInventario(fecha, listClientes, plantaCve);
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/pdf").name(filename)
					.stream(() -> input).build();
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

	public void sleep() throws InterruptedException {
		TimeUnit.SECONDS.sleep(5);
	}

	public void exportarExcel() throws JRException, IOException, SQLException {
		log.info("Exportando a excel.....");
		String filename = "InventarioAlmacen" + fecha + ".xlsx";
		String images = "/images/logoF.png";
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
                        reporteInventarioJR = new ReporteInventarioJR(connection, images);
                        byte[] bytes = reporteInventarioJR.getXLSReporteInventario(fecha, listClientes, plantaCve);
			InputStream input = new ByteArrayInputStream(bytes);
			this.file = DefaultStreamedContent.builder().contentType("application/vnd.ms-excel").name(filename)
					.stream(() -> input).build();
			log.info("El usuario {} descargo el reporte de inventario {}", this.usuario.getUsuario(), filename);
		} catch (Exception ex) {
			log.error("Problema general...", ex);
			message = String.format("No se pudo imprimir el reporte");
			severity = FacesMessage.SEVERITY_INFO;
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(severity, "Error en impresion", message));
			PrimeFaces.current().ajax().update("form:messages", "form:dt-facturacionServicios");
		} finally {
			conexion.close((Connection) connection);
		}

	}

	public void generaReporte() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Reporte de Inventario";

		try {
			List<Integer> listClientes = null;
			if (this.listClienteSelect == null) {
                            throw new InventarioException("Debe seleccionar un cliente.");
			} 
                        
                        listClientes = new ArrayList();
                        for(Cliente cliente : listClienteSelect) {
                            listClientes.add(cliente.getCteCve());
                        }

			Integer plantaCve = null;
			if (plantaSelect != null) {
                            plantaCve = plantaSelect.getPlantaCve();
			}

			reporte = reporteDAO.buscar(fecha, listClientes, plantaCve, null);
			log.debug("Registros del reporte: {}", reporte.size());
			log.info("El usuario {} consulta e reporte de inventario.", this.usuario.getUsuario());
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
        
        public void enviarInventario(){
            GestionApiClientBL gestionApi = new GestionApiClientBL();
            String numeroCliente = null;
            String mensaje = null;
            try {
                if (this.listClienteSelect == null || this.listClienteSelect.isEmpty()) {
                    throw new InventarioException("Debe seleccionar un cliente.");
                } 

                for(Cliente cliente : this.listClienteSelect){
                    numeroCliente = cliente.getNumeroCte();
                    gestionApi.enviarCorreoCliente(numeroCliente);
                }
                
                mensaje = (this.listClienteSelect.size() == 1) ? "El reporte se envió al cliente" : "El reporte se envió a los clientes";
                FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Inventario", mensaje);
            } catch (InventarioException ex) {
                log.error("Problema para enviar por correo el reporte: {}", ex.getMessage());
                FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Inventario", ex.getMessage());
            } catch (Exception ex) {
                log.error("Problema para consultar el reporte de salidas...", ex);
                FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Inventario", "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.");
            } finally {
                PrimeFaces.current().ajax().update("form:messages");
            }
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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

	public List<RepInventario> getReporte() {
		return reporte;
	}

	public void setReporte(List<RepInventario> reporte) {
		this.reporte = reporte;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

}
