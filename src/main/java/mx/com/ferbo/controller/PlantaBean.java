package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
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

import mx.com.ferbo.dao.n.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.ClienteDAO;
import mx.com.ferbo.dao.EmisoresCFDISDAO;
import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.dao.SerieFacturaDAO;

import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.EmisoresCFDIS;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.model.SerieConstanciaPK;
import mx.com.ferbo.model.SerieFactura;
import mx.com.ferbo.model.Usuario;

import mx.com.ferbo.util.InventarioException;

@Named
@ViewScoped
public class PlantaBean implements Serializable {

    private static Logger log = LogManager.getLogger(PlantaBean.class);
    private static final long serialVersionUID = 1L;

    @Inject
    private AsentamientoHumanoDAO asentamientoHumanoDAO;

    private PlantaDAO daoPlanta;
    private ClienteDAO clienteDAO;
    private SerieFacturaDAO sfDAO;
    private EmisoresCFDISDAO emisorDAO;

    private List<Planta> list;
    private List<Usuario> usuarios;
    private List<EmisoresCFDIS> listaEmisores;
    private List<SerieFactura> listaSerieFactura;

    private AsentamientoHumano asentamientoHumanoSelect;
    
    private EmisoresCFDIS idEmisoresCFDIS;
    private String PlantaDs;
    private String PlantaAbrev;
    private String PlantaSufijo;
    private int IdUsuario;
    private Planta planta;
    private Planta seleccion;
    private String uuid;

    private Usuario usuario;

    private FacesContext faceContext;
    private HttpServletRequest httpServletRequest;

    public PlantaBean() {
        daoPlanta = new PlantaDAO();
        clienteDAO = new ClienteDAO();
        sfDAO = new SerieFacturaDAO();
        emisorDAO = new EmisoresCFDISDAO();

        listaEmisores = new ArrayList<EmisoresCFDIS>();

        planta = new Planta();
        seleccion = new Planta();
    }

    @PostConstruct
    public synchronized void init() {
        this.faceContext = FacesContext.getCurrentInstance();
        this.httpServletRequest = (HttpServletRequest) faceContext.getExternalContext().getRequest();
        this.usuario = (Usuario) httpServletRequest.getSession(true).getAttribute("usuario");

        log.info("El usuario {} ingresa al catálogo de plantas (sucursales).", this.usuario.getUsuario());

        list = daoPlanta.findall(false);
        listaEmisores = emisorDAO.buscarTodos(true);
        usuarios = daoPlanta.getUsuarios();
        this.asentamientoHumanoSelect = this.nuevoAsentamiento();
        this.idEmisoresCFDIS = new EmisoresCFDIS();
    }

    public void openNew() {
        this.planta = new Planta();

        this.asentamientoHumanoSelect = this.nuevoAsentamiento();
    }

    public void datosPlanta(Planta planta) {
        this.planta = daoPlanta.buscarPorId(planta.getPlantaCve(), true);
        cargaSeries();
        //Carga domicilio de la planta
        cargaDireccion();

        PrimeFaces.current().ajax().update("form:pnlPlanta");
    }

    public void cargaSeries() {
        if (this.planta == null) {
            log.warn("No hay una planta seleccionada");
            return;
        }
        
        if (this.idEmisoresCFDIS == null) {
            log.warn("No hay un emisor seleccionado");
            return;
        }

        log.info("Emisor seleccionado: {}", this.planta.getIdEmisoresCFDIS());

        this.listaSerieFactura = sfDAO.buscarPorEmisor(this.planta.getIdEmisoresCFDIS());
        log.info("Emisor: {}, Total de series de facturas: {}", this.planta.getIdEmisoresCFDIS(), this.listaSerieFactura.size());
    }

    public void cargaDireccion() {
        if (this.planta == null) {
            return;
        }

        if (this.planta.getPlantaCve() == null) {
            return;
        }
        
        AsentamientoHumano auxAsentamiento = asentamientoHumanoDAO.buscarPorAsentamiento(this.planta.getIdPais(), this.planta.getIdEstado(), this.planta.getIdMunicipio(), this.planta.getIdCiudad(), this.planta.getIdAsentamiento());
        
        if(auxAsentamiento == null){
            return;
        }
        
        this.asentamientoHumanoSelect = auxAsentamiento;
    }
    
    public AsentamientoHumano nuevoAsentamiento(){
        AsentamientoHumanoPK pk = new AsentamientoHumanoPK();
        pk.setCiudades(new Ciudades());
        pk.getCiudades().setCiudadesPK(new CiudadesPK());
        pk.getCiudades().getCiudadesPK().setMunicipios(new Municipios());
        pk.getCiudades().getCiudadesPK().getMunicipios().setMunicipiosPK(new MunicipiosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().setEstados(new Estados());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().setEstadosPK(new EstadosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().setPais(new Paises());
        
        AsentamientoHumano auxAsentamiento = new AsentamientoHumano();
        auxAsentamiento.setAsentamientoHumanoPK(pk);
        
        return auxAsentamiento;
    }

    public void update() {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Guardar planta";

        String result = null;

        try {
            if (this.asentamientoHumanoSelect == null) {
                throw new InventarioException("Debe seleccionar un asentamiento (colonia)");
            }
            
            if (this.planta.getPlantaDs() == null || "".equalsIgnoreCase(this.planta.getPlantaDs().trim())) {
                throw new InventarioException("Debe indicar el nombre de la planta");
            }

            if (this.planta.getPlantaAbrev() == null || "".equalsIgnoreCase(this.planta.getPlantaAbrev().trim())) {
                throw new InventarioException("Debe indicar el número de la planta");
            }

            if (this.planta.getPlantaSufijo() == null || "".equalsIgnoreCase(this.planta.getPlantaSufijo().trim())) {
                throw new InventarioException("Debe indicar el sufijo (código) de la planta");
            }

            if (this.planta.getIdUsuario() == null) {
                throw new InventarioException("Debe indicar un usuario responsable de la planta");
            }

            if (this.planta.getIdEmisoresCFDIS() == null) {
                throw new InventarioException("Debe indicar un emisor de CFDI para la planta");
            }

            if (this.planta.getCalle() == null || "".equalsIgnoreCase(this.planta.getCalle().trim())) {
                throw new InventarioException("Debe indicar la calle del domicilio de la planta");
            }

            if (this.planta.getNumexterior() == null || "".equalsIgnoreCase(this.planta.getNumexterior().trim())) {
                throw new InventarioException("Debe indicar el número exterior del domicilio de la planta.");
            }

            planta.setIdPais(asentamientoHumanoSelect.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais().getPaisCve());
            planta.setIdEstado(asentamientoHumanoSelect.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getEstadoCve());
            planta.setIdMunicipio(asentamientoHumanoSelect.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getMunicipioCve());
            planta.setIdCiudad(asentamientoHumanoSelect.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getCiudadCve());
            planta.setIdAsentamiento(asentamientoHumanoSelect.getAsentamientoHumanoPK().getAsentamientoCve());
            planta.setTipoasentamiento(asentamientoHumanoSelect.getTipoAsentamiento().getTipoasntmntoCve().intValue());
            planta.setCodigopostal(asentamientoHumanoSelect.getCp());
            planta.setIdEmisoresCFDIS(planta.getIdEmisoresCFDIS());

            if (planta.getPlantaCve() == null) {
                log.info("Guardando información de la planta");
                this.processSerieConstancia(planta);
                result = daoPlanta.save(this.planta);
                if (result != null) {
                    throw new InventarioException("Ocurrió un problema al guardar la planta.");
                }
                list = daoPlanta.findall(false);
                log.info("El usuario {} ha registrado la planta {}.", this.usuario.getUsuario(), this.planta);
                mensaje = "La planta se registró correctamente";

            } else {
                result = daoPlanta.update(planta);
                if (result != null) {
                    throw new InventarioException("Ocurrió un problema al guardar la planta.");
                }
                log.info("El usuario {} ha actualizado la planta {}.", this.usuario.getUsuario(), this.planta);
                list = daoPlanta.findall(false);
                mensaje = "La planta se actualizó correctamente";
            }

            this.planta = new Planta();
            
            PrimeFaces.current().executeScript("PF('dg-agrega').hide()");
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_ERROR;
        } catch (Exception ex) {
            log.error("Problema para registrar información de la planta...", ex);
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
        }
    }

    public void processSerieConstancia(Planta planta) {
        List<Cliente> clienteList = null;

        try {
            clienteList = clienteDAO.buscarTodos(true);

            for (Cliente cliente : clienteList) {
                SerieConstanciaPK serieConstanciaPK_I = new SerieConstanciaPK();
                serieConstanciaPK_I.setCliente(cliente);
                serieConstanciaPK_I.setPlanta(planta);
                serieConstanciaPK_I.setTpSerie("I");
                SerieConstancia serieConstanciaI = new SerieConstancia();
                serieConstanciaI.setSerieConstanciaPK(serieConstanciaPK_I);
                serieConstanciaI.setNuSerie(1);
                cliente.addSerieConstancia(serieConstanciaI);
                planta.add(serieConstanciaI);

                SerieConstanciaPK serieConstanciaPK_O = new SerieConstanciaPK();
                serieConstanciaPK_O.setCliente(cliente);
                serieConstanciaPK_O.setPlanta(planta);
                serieConstanciaPK_O.setTpSerie("O");
                SerieConstancia serieConstanciaO = new SerieConstancia();
                serieConstanciaO.setSerieConstanciaPK(serieConstanciaPK_O);
                serieConstanciaO.setNuSerie(1);
                cliente.addSerieConstancia(serieConstanciaO);
                planta.add(serieConstanciaO);

                SerieConstanciaPK serieConstanciaPK_T = new SerieConstanciaPK();
                serieConstanciaPK_T.setCliente(cliente);
                serieConstanciaPK_T.setPlanta(planta);
                serieConstanciaPK_T.setTpSerie("T");
                SerieConstancia serieConstanciaT = new SerieConstancia();
                serieConstanciaT.setSerieConstanciaPK(serieConstanciaPK_T);
                serieConstanciaT.setNuSerie(1);
                cliente.addSerieConstancia(serieConstanciaT);
                planta.add(serieConstanciaT);

                SerieConstanciaPK serieConstanciaPK_S = new SerieConstanciaPK();
                serieConstanciaPK_S.setCliente(cliente);
                serieConstanciaPK_S.setPlanta(planta);
                serieConstanciaPK_S.setTpSerie("S");
                SerieConstancia serieConstanciaS = new SerieConstancia();
                serieConstanciaS.setSerieConstanciaPK(serieConstanciaPK_S);
                serieConstanciaS.setNuSerie(1);
                cliente.addSerieConstancia(serieConstanciaS);
                planta.add(serieConstanciaS);
            }

        } catch (Exception ex) {

        } finally {

        }
    }

    public void delete() {
        PrimeFaces.current().executeScript("PF('dg-delete').hide()");

        String message = daoPlanta.delete(planta);

        if (message == null) {

            list.remove(this.planta);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Planta Eliminado " + planta.getPlantaDs(), null));
            PrimeFaces.current().ajax().update("form:messages", "form:dt-planta");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar " + planta.getPlantaDs(), message));
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void asignarDomicilio() {
    	FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Domicilio";
    	try {
            if(this.asentamientoHumanoSelect == null){
                throw new InventarioException("No hay objeto Domicilio asignado al cliente.");
            }
            
            log.info("Agregando / Actualizando información al cliente");
            log.debug("Domicilio seleccionado: {}", this.asentamientoHumanoSelect.toString());
            
            mensaje = "Nuevo domicilio seleccionado.";
            severity = FacesMessage.SEVERITY_INFO;
        } catch(InventarioException ex) {
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para seleccionar un domicilio...", ex);
            mensaje = "Problema para seleccionar un domicilio.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:pnlPlanta");
        }
    }
    
    public List<AsentamientoHumano> sugerenciasCodigoPostal(String consulta){
        List<AsentamientoHumano> listaSugerencias = asentamientoHumanoDAO.buscaPorCP(consulta);
        return listaSugerencias;
    }

    public void muestraplanta() {
        log.info("Planta" + planta);
        log.info("Planta" + seleccion);
    }

    public void limpiaPlanta() {
        this.planta = new Planta();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlantaDs() {
        return PlantaDs;
    }

    public void setPlantaDs(String plantaDs) {
        PlantaDs = plantaDs;
    }

    public String getPlantaAbrev() {
        return PlantaAbrev;
    }

    public void setPlantaAbrev(String plantaAbrev) {
        PlantaAbrev = plantaAbrev;
    }

    public String getPlantaSufijo() {
        return PlantaSufijo;
    }

    public void setPlantaSufijo(String plantaSufijo) {
        PlantaSufijo = plantaSufijo;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }

    public AsentamientoHumano getAsentamientoHumanoSelect() {
        return asentamientoHumanoSelect;
    }

    public void setAsentamientoHumanoSelect(AsentamientoHumano asentamientoHumanoSelect) {
        this.asentamientoHumanoSelect = asentamientoHumanoSelect;
    }

    public List<Planta> getList() {
        return list;
    }

    public void setList(List<Planta> list) {
        this.list = list;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public Planta getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Planta seleccion) {
        this.seleccion = seleccion;
    }

    public List<EmisoresCFDIS> getListaEmisores() {
        return listaEmisores;
    }

    public void setListaEmisores(List<EmisoresCFDIS> listaEmisores) {
        this.listaEmisores = listaEmisores;
    }

    public EmisoresCFDIS getIdEmisoresCFDIS() {
        return idEmisoresCFDIS;
    }

    public void setIdEmisoresCFDIS(EmisoresCFDIS idEmisoresCFDIS) {
        this.idEmisoresCFDIS = idEmisoresCFDIS;
    }

    public List<SerieFactura> getListaSerieFactura() {
        return listaSerieFactura;
    }

    public void setListaSerieFactura(List<SerieFactura> listaSerieFactura) {
        this.listaSerieFactura = listaSerieFactura;
    }

}
