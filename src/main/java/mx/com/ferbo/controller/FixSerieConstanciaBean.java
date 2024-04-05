package mx.com.ferbo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

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

import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieConstanciaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class FixSerieConstanciaBean implements Serializable {

	private static final long serialVersionUID = 2075180134318824059L;
	private static Logger log = LogManager.getLogger(PlantaBean.class);

	private Usuario usuario;
	private FacesContext faceContext;
	private HttpServletRequest httpServletRequest;

	private ClienteDAO clienteDAO;
	private PlantaDAO plantaDAO;
	private SerieConstanciaDAO serieConstanciaDAO;

	private Cliente clienteSelect;
	private List<Cliente> clienteList;
	private List<SerieConstancia> serieConstanciaList;
	private Planta plantaSelect;
	private List<Planta> plantaList;
	private Integer numeroEntrada;
	private Integer numeroSalida;
	private Integer numeroTraspaso;
	private Integer numeroServicio;
	private SerieConstancia serieConstanciaSelected;

	public FixSerieConstanciaBean() {
		clienteDAO = new ClienteDAO();
		plantaDAO = new PlantaDAO();
		serieConstanciaDAO = new SerieConstanciaDAO();
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		this.faceContext = FacesContext.getCurrentInstance();
		this.httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
		this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");

		log.info("El usuario {} entra a la corrección de Serie-Constancia.", this.usuario.getUsuario());

		this.clienteList = (List<Cliente>) httpServletRequest.getSession(false).getAttribute("clientesTodosList");
		this.plantaList = plantaDAO.buscarTodos();

		this.serieConstanciaSelected = new SerieConstancia();
		this.serieConstanciaSelected.setSerieConstanciaPK(new SerieConstanciaPK());

		if (usuario.getPerfil() != 3) {
			try {
				faceContext.getExternalContext().redirect("dashboard.xhtml");
			} catch (IOException e) {
				log.error("Problema para redigir a la página...", e);
			}
		}
	}

	public void cargaInfo() {
		log.info("Cargando información de las series de constancias...");
		if (this.clienteSelect == null) {
			log.info("No hay cliente seleccionado.");
			return;
		}

		if (this.plantaSelect == null) {
			serieConstanciaList = serieConstanciaDAO.buscarPorIdCliente(this.clienteSelect.getCteCve());
		} else {
			serieConstanciaList = serieConstanciaDAO.buscarPorClienteAndPlanta(this.clienteSelect.getCteCve(),
					this.plantaSelect.getPlantaCve());
		}

		log.info("Información de las series de constancias cargadas.");
	}

	public void resetNumeros() {
		log.info("Reiniciando numeros...");
		this.numeroEntrada = null;
		this.numeroSalida = null;
		this.numeroTraspaso = null;
		this.numeroServicio = null;
	}

	public void crearSeries() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Crear serie";

		Cliente cliente = null;
		Planta planta = null;

		try {
			if (this.clienteSelect == null)
				throw new InventarioException("Debe seleccionar un cliente.");

			if (this.plantaSelect == null)
				throw new InventarioException("Debe seleccionar una planta.");

			cliente = clienteDAO.buscarDetalleSerieConstancia(this.clienteSelect.getCteCve());
			planta = plantaDAO.buscarDetalleSerieConstancia(this.plantaSelect.getPlantaCve());

			SerieConstancia sc_I = new SerieConstancia();
			SerieConstanciaPK scPK_I = new SerieConstanciaPK();
			scPK_I.setCliente(cliente);
			scPK_I.setPlanta(planta);
			scPK_I.setTpSerie("I");
			sc_I.setSerieConstanciaPK(scPK_I);
			sc_I.setNuSerie(this.numeroEntrada);
			if (this.serieConstanciaList.contains(sc_I) == false) {
				cliente.addSerieConstancia(sc_I);
				planta.add(sc_I);
				this.serieConstanciaList.add(sc_I);
				log.info("Agregando nueva serie-constancia: {}", scPK_I);
			}

			SerieConstancia sc_O = new SerieConstancia();
			SerieConstanciaPK scPK_O = new SerieConstanciaPK();
			scPK_O.setCliente(cliente);
			scPK_O.setPlanta(planta);
			scPK_O.setTpSerie("O");
			sc_O.setSerieConstanciaPK(scPK_O);
			sc_O.setNuSerie(this.numeroSalida);
			if (this.serieConstanciaList.contains(sc_O) == false) {
				cliente.addSerieConstancia(sc_O);
				planta.add(sc_O);
				this.serieConstanciaList.add(sc_O);
				log.info("Agregando nueva serie-constancia: {}", scPK_O);
			}

			SerieConstancia sc_T = new SerieConstancia();
			SerieConstanciaPK scPK_T = new SerieConstanciaPK();
			scPK_T.setCliente(cliente);
			scPK_T.setPlanta(planta);
			scPK_T.setTpSerie("T");
			sc_T.setSerieConstanciaPK(scPK_T);
			sc_T.setNuSerie(this.numeroTraspaso);
			if (this.serieConstanciaList.contains(sc_T) == false) {
				cliente.addSerieConstancia(sc_T);
				planta.add(sc_T);
				this.serieConstanciaList.add(sc_T);
				log.info("Agregando nueva serie-constancia: {}", scPK_T);
			}

			SerieConstancia sc_S = new SerieConstancia();
			SerieConstanciaPK scPK_S = new SerieConstanciaPK();
			scPK_S.setCliente(cliente);
			scPK_S.setPlanta(planta);
			scPK_S.setTpSerie("S");
			sc_S.setSerieConstanciaPK(scPK_S);
			sc_S.setNuSerie(this.numeroServicio);
			if (this.serieConstanciaList.contains(sc_S) == false) {
				cliente.addSerieConstancia(sc_S);
				planta.add(sc_S);
				this.serieConstanciaList.add(sc_S);
				log.info("Agregando nueva serie-constancia: {}", scPK_S);
			}

			for (SerieConstancia sc : this.serieConstanciaList) {
				serieConstanciaDAO.guardar(sc);
				log.info("Almacenamiento de serie constancia {} procesado.", sc.getSerieConstanciaPK());
			}
			this.cargaInfo();
			log.info("Creación de series de constancias terminó correctamente.");

			PrimeFaces.current().executeScript("PF('dg-serie').hide()");
			mensaje = "Series agregadas correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages", ":form:dtSerieConstancia");
		}
	}

	public void cargaSerie() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Editar serie";

		try {
			log.info("Cargando serie: {}", this.serieConstanciaSelected);
			mensaje = "Series cargada";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages", ":form:dtSerieConstancia");
		}
	}

	public void actualizarSerie() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Crear serie";

		try {
			log.info("Cargando serie: {}", this.serieConstanciaSelected);
			if (this.serieConstanciaSelected.getNuSerie() < 1)
				throw new InventarioException("Debe indicar un numero de serie válido.");
			this.serieConstanciaDAO.actualizar(this.serieConstanciaSelected);
			this.cargaInfo();
			log.info("Serie constancia actualizada correctamente: {}",
					this.serieConstanciaSelected.getSerieConstanciaPK());
			PrimeFaces.current().executeScript("PF('dgEditSerie').hide()");
			mensaje = "La serie constancia se actualizó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para generar el folio de entrada...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages", ":form:dtSerieConstancia");
		}

	}

	public void cloneAll() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Crear serie";

		try {
			for (Cliente cliente : this.clienteList) {
				this.fixCliente(cliente);
			}
			this.cargaInfo();
			mensaje = "La serie-constancia se clonó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para procesar el listado de Serie-constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages", ":form:dtSerieConstancia");
		}
	}

	public void fix() {
		FacesMessage message = null;
		Severity severity = null;
		String mensaje = null;
		String titulo = "Crear serie";

		try {
			this.fixCliente(clienteSelect);
			this.cargaInfo();
			mensaje = "La serie-constancia se clonó correctamente.";
			severity = FacesMessage.SEVERITY_INFO;
		} catch (InventarioException ex) {
			mensaje = ex.getMessage();
			severity = FacesMessage.SEVERITY_WARN;
		} catch (Exception ex) {
			log.error("Problema para procesar el listado de Serie-constancia...", ex);
			mensaje = "Ha ocurrido un error en el sistema. Intente nuevamente.\nSi el problema persiste, por favor comuniquese con su administrador del sistema.";
			severity = FacesMessage.SEVERITY_ERROR;
		} finally {
			message = new FacesMessage(severity, titulo, mensaje);
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().update(":form:messages", ":form:dtSerieConstancia");
		}
	}

	public void fixCliente(Cliente cliente) throws InventarioException {
		List<Planta> plantaList = null;
		List<SerieConstancia> serieConstanciaList = null;
		Integer numeroEntrada = null;
		Integer numeroSalida = null;
		Integer numeroTraspaso = null;
		Integer numeroServicio = null;

		log.info("Cliente: {}", cliente);
		if (cliente == null)
			throw new InventarioException("Debe seleccionar un cliente");

		cliente = clienteDAO.buscarDetalleSerieConstancia(cliente.getCteCve());
		plantaList = plantaDAO.buscarTodosSerieConstancia();
		serieConstanciaList = cliente.getSerieConstanciaList();

		for (Planta planta : plantaList) {
			SerieConstancia sc_I = new SerieConstancia();
			SerieConstanciaPK scPK_I = new SerieConstanciaPK();
			scPK_I.setCliente(cliente);
			scPK_I.setPlanta(planta);
			scPK_I.setTpSerie("I");
			sc_I.setSerieConstanciaPK(scPK_I);
			if (serieConstanciaList.contains(sc_I)) {
				numeroEntrada = serieConstanciaDAO.buscarPorId(scPK_I).getNuSerie();
			} else {
				sc_I.setNuSerie(numeroEntrada == null ? 0 : numeroEntrada.intValue());
				cliente.addSerieConstancia(sc_I);
				planta.add(sc_I);
				log.info("Agregando nueva serie-constancia: {}, {}", scPK_I, sc_I.getNuSerie());
			}

			SerieConstancia sc_O = new SerieConstancia();
			SerieConstanciaPK scPK_O = new SerieConstanciaPK();
			scPK_O.setCliente(cliente);
			scPK_O.setPlanta(planta);
			scPK_O.setTpSerie("O");
			sc_O.setSerieConstanciaPK(scPK_O);
			if (serieConstanciaList.contains(sc_O)) {
				numeroSalida = serieConstanciaDAO.buscarPorId(scPK_O).getNuSerie();
			} else {
				sc_O.setNuSerie(numeroSalida == null ? 0 : numeroSalida.intValue());
				cliente.addSerieConstancia(sc_O);
				planta.add(sc_O);
				log.info("Agregando nueva serie-constancia: {}, {}", scPK_O, sc_O.getNuSerie());
			}

			SerieConstancia sc_T = new SerieConstancia();
			SerieConstanciaPK scPK_T = new SerieConstanciaPK();
			scPK_T.setCliente(cliente);
			scPK_T.setPlanta(planta);
			scPK_T.setTpSerie("T");
			sc_T.setSerieConstanciaPK(scPK_T);
			if (serieConstanciaList.contains(sc_T)) {
				numeroTraspaso = serieConstanciaDAO.buscarPorId(scPK_T).getNuSerie();
				;
			} else {
				sc_T.setNuSerie(numeroTraspaso == null ? 0 : numeroTraspaso.intValue());
				cliente.addSerieConstancia(sc_T);
				planta.add(sc_T);
				log.info("Agregando nueva serie-constancia: {}, {}", scPK_T, sc_T.getNuSerie());
			}

			SerieConstancia sc_S = new SerieConstancia();
			SerieConstanciaPK scPK_S = new SerieConstanciaPK();
			scPK_S.setCliente(cliente);
			scPK_S.setPlanta(planta);
			scPK_S.setTpSerie("S");
			sc_S.setSerieConstanciaPK(scPK_S);
			if (serieConstanciaList.contains(sc_S)) {
				numeroServicio = serieConstanciaDAO.buscarPorId(scPK_S).getNuSerie();
			} else {
				sc_S.setNuSerie(numeroServicio == null ? 0 : numeroServicio.intValue());
				cliente.addSerieConstancia(sc_S);
				planta.add(sc_S);
				log.info("Agregando nueva serie-constancia: {}, {}", scPK_S, sc_S.getNuSerie());
			}
		}

		for (SerieConstancia sc : serieConstanciaList) {
			serieConstanciaDAO.actualizar(sc);
		}
	}

	public List<Cliente> getClienteList() {
		return clienteList;
	}

	public void setClienteList(List<Cliente> clienteList) {
		this.clienteList = clienteList;
	}

	public Cliente getClienteSelect() {
		return clienteSelect;
	}

	public void setClienteSelect(Cliente clienteSelect) {
		this.clienteSelect = clienteSelect;
	}

	public List<SerieConstancia> getSerieConstanciaList() {
		return serieConstanciaList;
	}

	public void setSerieConstanciaList(List<SerieConstancia> serieConstanciaList) {
		this.serieConstanciaList = serieConstanciaList;
	}

	public Planta getPlantaSelect() {
		return plantaSelect;
	}

	public void setPlantaSelect(Planta plantaSelect) {
		this.plantaSelect = plantaSelect;
	}

	public List<Planta> getPlantaList() {
		return plantaList;
	}

	public void setPlantaList(List<Planta> plantaList) {
		this.plantaList = plantaList;
	}

	public Integer getNumeroEntrada() {
		return numeroEntrada;
	}

	public void setNumeroEntrada(Integer numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}

	public Integer getNumeroSalida() {
		return numeroSalida;
	}

	public void setNumeroSalida(Integer numeroSalida) {
		this.numeroSalida = numeroSalida;
	}

	public Integer getNumeroTraspaso() {
		return numeroTraspaso;
	}

	public void setNumeroTraspaso(Integer numeroTraspaso) {
		this.numeroTraspaso = numeroTraspaso;
	}

	public Integer getNumeroServicio() {
		return numeroServicio;
	}

	public void setNumeroServicio(Integer numeroServicio) {
		this.numeroServicio = numeroServicio;
	}

	public SerieConstancia getSerieConstanciaSelected() {
		return serieConstanciaSelected;
	}

	public void setSerieConstanciaSelected(SerieConstancia serieConstanciaSelected) {
		this.serieConstanciaSelected = serieConstanciaSelected;
	}

}
