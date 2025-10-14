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

import mx.com.ferbo.dao.n.PaisesDAO;
import mx.com.ferbo.dao.n.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.n.CiudadesDAO;
import mx.com.ferbo.dao.n.EntidadPostalDAO;
import mx.com.ferbo.dao.n.EstadosDAO;
import mx.com.ferbo.dao.n.MunicipiosDAO;
import mx.com.ferbo.dao.n.TipoAsentamientoDAO;

import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.EntidadPostal;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.model.TipoAsentamiento;

import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
@Named(value = "domiciliosBean")
@ViewScoped
public class DomiciliosBean implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(DomiciliosBean.class);

    @Inject
    private AsentamientoHumanoDAO asentamientoDAO;
    
    @Inject
    private EntidadPostalDAO entidadPostalDAO;
    
    @Inject
    private TipoAsentamientoDAO tipoAsentamientoDAO;

    @Inject
    private CiudadesDAO ciudadesDAO;

    @Inject
    private MunicipiosDAO municipiosDAO;

    @Inject
    private EstadosDAO estadosDAO;

    @Inject
    private PaisesDAO paisesDAO;

    /**
     * Objetos para Asentamientos
     */
    private List<AsentamientoHumano> listAsentamientoHumano;
    private List<AsentamientoHumano> listAsentamientoHumanoFiltered;
    private List<AsentamientoHumano> listAsentamientoHumanoCP;
    private AsentamientoHumano asentamientoHumanoSelected;
    
    /**
     * Objetos para EntidadPostal
     */
    private List<EntidadPostal> listEntidadPostal;
    private EntidadPostal entidadPostalSelected;
    
    /**
     * Objetos para TipoAsentamiento
     */
    private List<TipoAsentamiento> listTipoAsentamiento;
    private TipoAsentamiento tipoAsentamientoSelected;
    
    /**
     * Objetos para Ciudades
     */
    private List<Ciudades> listCiudadesFiltered;
    private Ciudades ciudadSelected;
    
    /**
     * Objetos para Municipios
     */
    private List<Municipios> listMunicipiosFiltered;
    private Municipios municipioSelected;
    
    /**
     * Objetos para Estados
     */
    private List<Estados> listEstadosFiltered;
    private Estados estadoSelected;
    
    /**
     * Objetos para Paises
     */
    private List<Paises> listPaises;
    private Paises paisSelected;
    
    private String codigoPostal = "";
    
    public DomiciliosBean() {
        this.asentamientoHumanoSelected = this.nuevoAsentamiento();
        this.listEstadosFiltered = new ArrayList<>();
        this.listMunicipiosFiltered = new ArrayList<>();
        this.listCiudadesFiltered = new ArrayList<>();
        this.listAsentamientoHumanoFiltered = new ArrayList<>();
        this.listAsentamientoHumanoCP = new ArrayList<>();
        this.inicializaPais();
        this.estadoSelected = null;
        this.municipioSelected = null;
        this.ciudadSelected = null;
        this.asentamientoHumanoSelected = null;
        this.entidadPostalSelected = null;
        this.tipoAsentamientoSelected = null;
    }

    @PostConstruct
    public void init() {
        this.listPaises = paisesDAO.buscarTodos();
        this.listEntidadPostal = entidadPostalDAO.buscarTodos();
        this.listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
    }

    public void filtraListadoEstados()
    {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Estados";
        try {
            this.listEstadosFiltered.clear();
            this.listMunicipiosFiltered.clear();
            this.listCiudadesFiltered.clear();
            this.listAsentamientoHumanoFiltered.clear();
            
            if(this.paisSelected == null){
                throw new InventarioException("No hay pais seleccionado.");
            }
            
            EstadosPK auxEstadosPK = new EstadosPK();
            auxEstadosPK.setPais(this.paisSelected);
            
            this.estadoSelected = new Estados();
            this.estadoSelected.setEstadosPK(auxEstadosPK);
            log.debug("Info estado: {}", this.estadoSelected.getEstadosPK().toString());
            
            this.listEstadosFiltered = estadosDAO.buscarPorCriteriosEstados(this.estadoSelected);
            
            mensaje = "Estado encontrado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un estado...", ex.getMessage());
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar un estado...", ex);
            mensaje = "Problema para buscar un estado.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoMunicipios(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Municipios";
        try {
            this.listMunicipiosFiltered.clear();
            this.listCiudadesFiltered.clear();
            this.listAsentamientoHumanoFiltered.clear();
            
            if(this.estadoSelected == null){
                throw new InventarioException("No hay estado seleccionado.");
            }
            
            MunicipiosPK municipiosPK = new MunicipiosPK();
            municipiosPK.setEstados(this.estadoSelected);
            
            this.municipioSelected = new Municipios();
            this.municipioSelected.setMunicipiosPK(municipiosPK);
            log.debug("Info municipio: {}", this.municipioSelected.getMunicipiosPK().toString());
            
            this.listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(this.municipioSelected);
            
            mensaje = "Municipio encontrado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un municipio...", ex.getMessage());
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar un municipio...", ex);
            mensaje = "Problema para buscar un municipio.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoCiudades(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Ciudades";
        try {
            log.debug("Municipio selected: {}", this.municipioSelected.toString());
            this.listCiudadesFiltered.clear();
            this.listAsentamientoHumanoFiltered.clear();
            
            if(this.municipioSelected == null){
                throw new InventarioException("No hay municipio seleccionado.");
            }
            
            CiudadesPK ciudadPK = new CiudadesPK();
            ciudadPK.setMunicipios(this.municipioSelected);
            
            this.ciudadSelected = new Ciudades();
            this.ciudadSelected.setCiudadesPK(ciudadPK);
            log.debug("Info ciudad: {}", this.ciudadSelected.getCiudadesPK().toString());
            
            this.listCiudadesFiltered = this.ciudadesDAO.buscarPorCriteriosCiudades(this.ciudadSelected);
            
            mensaje = "Ciudad encontrado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.warn("Problema para buscar una ciudad...", ex.getMessage());
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar una ciudad...", ex);
            mensaje = "Problema para buscar una ciudad.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoAsentamiento(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Asentamiento Humano";
        try {
            this.listAsentamientoHumanoFiltered.clear();
            
            if(this.ciudadSelected == null){
                throw new InventarioException("No hay ciudad seleccionado.");
            }
            
            AsentamientoHumanoPK asentamientoPK = new AsentamientoHumanoPK();
            asentamientoPK.setCiudades(this.ciudadSelected);
            
            this.asentamientoHumanoSelected = new AsentamientoHumano();
            this.asentamientoHumanoSelected.setAsentamientoHumanoPK(asentamientoPK);
            log.debug("Info asentamiento: {}", this.asentamientoHumanoSelected.getAsentamientoHumanoPK().toString());
            
            this.listAsentamientoHumanoFiltered = this.asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
            
            mensaje = "Asentamiento encontrado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un asentamiento...", ex.getMessage());
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            mensaje = "Problema para buscar un asentamiento.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraEntidadPostalTipoAsentamiento(){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Asentamiento";
        try {
            if(this.asentamientoHumanoSelected == null){
                throw new InventarioException("No hay un asentamiento seleccionado.");
            }
            
            this.entidadPostalSelected = this.asentamientoHumanoSelected.getEntidadPostal();
            this.tipoAsentamientoSelected = this.asentamientoHumanoSelected.getTipoAsentamiento();
            
            mensaje = "Entidad postal y Tipo asentamiento encontrado";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.warn("Problema para buscar los elementos...", ex.getMessage());
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar los elementos...", ex);
            mensaje = "Problema para buscar los elementos.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void guardar(T entidad){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = null;
        try{
            log.info("Iniciando el proceso de guardar");
            
            if(entidad instanceof Paises) {
                titulo = "Pais";
                
                Paises auxPais = paisesDAO.buscarUltimoPais();
                int numPais = auxPais.getPaisCve();
                numPais++;
                
                Paises pais = (Paises) entidad;
                ((Paises) entidad).setPaisCve(numPais);
                this.validarPais(pais);
                
                paisesDAO.guardar(pais);
                log.info("Guardando un pais");
                
                this.listPaises = paisesDAO.buscarTodos();
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
            } else if(entidad instanceof Estados){
                titulo = "Estado";
                
                Estados auxEstado = estadosDAO.buscarUltimoEstado(this.paisSelected);
                int numEstado = (auxEstado != null) ? auxEstado.getEstadosPK().getEstadoCve() : 0;
                numEstado++;
                
                Estados estado = (Estados) entidad;
                estado.getEstadosPK().setEstadoCve(numEstado);
                this.validarEstado(estado);
                
                estadosDAO.guardar(estado);
                log.info("Guardando un estado");
                
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
                this.listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(this.estadoSelected);
            } else if(entidad instanceof Municipios){
                titulo = "Municipio";
                
                Municipios auxMunicipio = municipiosDAO.buscarUltimoMunicipio(this.estadoSelected);
                int numMunicipio = (auxMunicipio != null) ? auxMunicipio.getMunicipiosPK().getMunicipioCve() : 0;
                numMunicipio++;
                
                Municipios municipio = (Municipios) entidad;
                ((Municipios) entidad).getMunicipiosPK().setMunicipioCve(numMunicipio);
                this.validarMunicipio(municipio);
                
                municipiosDAO.guardar(municipio);
                log.info("Guardando un municipio");
                
                this.listMunicipiosFiltered = municipiosDAO.buscarPorCriterios(this.municipioSelected);
                this.listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(this.municipioSelected);
            } else if(entidad instanceof Ciudades) {
                titulo = "Ciudad";
                
                Ciudades auxCiudad = ciudadesDAO.buscarUltimaCidad(this.municipioSelected);
                int numCiudad = (auxCiudad != null) ? auxCiudad.getCiudadesPK().getCiudadCve() : 0;
                numCiudad++;
                
                Ciudades ciudad = (Ciudades) entidad;
                ((Ciudades) entidad).getCiudadesPK().setCiudadCve(numCiudad);
                this.validarCiudad(ciudad);
                
                ciudadesDAO.guardar(ciudad);
                log.info("Guardando una ciudad");
                
                this.listCiudadesFiltered = ciudadesDAO.buscarPorCriteriosCiudades(this.ciudadSelected);
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
            } else if(entidad instanceof AsentamientoHumano) {
                titulo = "Asentamiento Humano";
                
                AsentamientoHumano auxAsentamiento = asentamientoDAO.buscarUltimoAsentamiento();
                int numAsentamiento = (auxAsentamiento != null) ? auxAsentamiento.getAsentamientoHumanoPK().getAsentamientoCve() : 0;
                numAsentamiento++;
                
                AsentamientoHumano asentamiento = (AsentamientoHumano) entidad;
                asentamiento.getAsentamientoHumanoPK().setAsentamientoCve(numAsentamiento);
                asentamiento.setEntidadPostal(this.entidadPostalSelected);
                asentamiento.setTipoAsentamiento(this.tipoAsentamientoSelected);
                this.validarAsentamiento(asentamiento);
                
                asentamientoDAO.guardar(asentamiento);
                log.info("Guardando un asentamiento");
                
                this.codigoPostal = asentamiento.getCp();
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCriterios(this.asentamientoHumanoSelected);
                this.listAsentamientoHumanoCP = asentamientoDAO.buscaPorCP(((AsentamientoHumano) entidad).getCp());
            } else if (entidad instanceof EntidadPostal) { 
                titulo = "Entidad Postal";
                
                EntidadPostal auxEntidadPostal = entidadPostalDAO.buscarUltimaEntidadPostal();
                int numEntidadPostal = auxEntidadPostal.getEntidadpostalCve();
                numEntidadPostal++;
                
                EntidadPostal entidadPostal = (EntidadPostal) entidad;
                ((EntidadPostal) entidad).setEntidadpostalCve(numEntidadPostal);
                this.validarEntidadPostal(entidadPostal);
                
                entidadPostalDAO.guardar(entidadPostal);
                log.info("Guardando una entidad postal");
                
                this.listEntidadPostal = entidadPostalDAO.buscarTodos();
            } else if (entidad instanceof TipoAsentamiento) {
                titulo = "Tipo de Asentamiento";
                
                TipoAsentamiento auxTipoAsentamiento = tipoAsentamientoDAO.buscarUltimoTipoAsentamiento();
                int numTipoAsentamiento = auxTipoAsentamiento.getTipoasntmntoCve();
                numTipoAsentamiento++;
                
                TipoAsentamiento tipoAsentamiento = (TipoAsentamiento) entidad;
                tipoAsentamiento.setTipoasntmntoCve((short) numTipoAsentamiento);
                this.validarTipoAsentamiento(tipoAsentamiento);
                
                tipoAsentamientoDAO.guardar(tipoAsentamiento);
                log.info("Guardando un tipo de asentamiento");
                
                this.listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } else {
                throw new IllegalArgumentException("Tipo de entidad no soportado: " + entidad.getClass());
            }
            
            mensaje = "Se guardo con exito";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Se encontro un problema para guardar...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Se encontro un problema para guardar...", ex);
            mensaje = "Problema para guardar";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void actualizar(T entidad){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = entidad.getClass().getSimpleName();
        try{
            log.info("Iniciando el proceso de actualizar");
            
            if(entidad instanceof Paises){
                titulo = "Pais";
                
                Paises pais = (Paises) entidad;
                validarPais(pais);
                
                paisesDAO.actualizar(pais);
                log.info("Actualizando un pais ");
                
                this.listPaises = paisesDAO.buscarTodos();
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
            } else if(entidad instanceof Estados){
                titulo = "Estado";
                
                Estados estado = (Estados) entidad;
                validarEstado(estado);
                
                estadosDAO.actualizar(estado);
                log.info("Actualizando un estado");
                
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
                this.listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(this.estadoSelected);
            } else if(entidad instanceof Municipios){
                titulo = "Municipio";
                
                Municipios municipio = (Municipios) entidad;
                this.validarMunicipio(municipio);
                
                municipiosDAO.actualizar(municipio);
                log.info("Actualizando un municipio");
                
                this.listMunicipiosFiltered = municipiosDAO.buscarPorCriterios(this.municipioSelected);
                this.listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(this.municipioSelected);
            } else if(entidad instanceof Ciudades) {
                titulo = "Ciudad";
                
                Ciudades ciudad = (Ciudades) entidad;
                this.validarCiudad(ciudad);
                
                ciudadesDAO.actualizar(ciudad);
                log.info("Actualizando una ciudad");
                
                this.listCiudadesFiltered = ciudadesDAO.buscarPorCriteriosCiudades(this.ciudadSelected);
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
            } else if (entidad instanceof AsentamientoHumano) {
                titulo = "Asentamiento Humano";
                
                AsentamientoHumano asentamiento = (AsentamientoHumano) entidad;
                this.validarAsentamiento(asentamiento);
                
                asentamientoDAO.actualizar(asentamiento);
                log.info("Actualizando un asentamiento");
                
                this.codigoPostal = asentamiento.getCp();
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCriterios(this.asentamientoHumanoSelected);
                this.listAsentamientoHumanoCP = asentamientoDAO.buscaPorCP(((AsentamientoHumano) entidad).getCp());
            } else if (entidad instanceof EntidadPostal) {
                titulo = "Entidad Postal";
                
                EntidadPostal entidadPostal = (EntidadPostal) entidad;
                this.validarEntidadPostal(entidadPostal);
                
                entidadPostalDAO.actualizar(entidadPostal);
                log.info("Actualizando una entidad postal");
                
                this.listEntidadPostal = entidadPostalDAO.buscarTodos();
            } else if (entidad instanceof TipoAsentamiento) {
                titulo = "Tipo Asentamiento";
                
                TipoAsentamiento tipoAsentamiento = (TipoAsentamiento) entidad;
                this.validarTipoAsentamiento(tipoAsentamiento);
                
                tipoAsentamientoDAO.actualizar(tipoAsentamiento);
                log.info("Actualizando un tipo de asentamiento");
                
                this.listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } else {
                throw new IllegalArgumentException("Tipo de entidad no soportado: " + entidad.getClass());
            }
            
            mensaje = "Se actualizo con exito";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Se encontro un problema para actualizar...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Se encontro un problema para actualizar...", ex);
            mensaje = "Problema para actualizar";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void eliminarObjeto(T entidad){
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = null;
        try {
            log.info("Iniciando el proceso de eliminar");
            
            if(entidad instanceof Paises){
                titulo = "Pais";
                log.info("Eliminando el pais: {}", ((Paises) entidad).getPaisDesc());
                
                this.validarPais((Paises) entidad);
                
                paisesDAO.eliminar((Paises) entidad);
                this.inicializaPais();
                this.listPaises = paisesDAO.buscarTodos();
            } else if (entidad instanceof Estados) {
                titulo = "Estado";
                log.info("Eliminando el estado: {}", ((Estados) entidad).getEstadoDesc());
                
                this.validarEstado((Estados) entidad);
                
                estadosDAO.eliminar((Estados) entidad);
                
                this.inicializaEstado();
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
                this.listMunicipiosFiltered.clear();
            } else if (entidad instanceof Municipios) {
                titulo = "Municipio";
                log.info("Eliminando el municipio: {}", ((Municipios) entidad).getMunicipioDs());
                
                this.validarMunicipio((Municipios) entidad);
                
                municipiosDAO.eliminar((Municipios) entidad);
                
                this.inicializaMunicipio();
                this.listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(this.estadoSelected);
                this.listCiudadesFiltered.clear();
            } else if (entidad instanceof Ciudades) {
                titulo = "Ciudad";
                log.info("Eliminando la ciudad: {}", ((Ciudades) entidad).getCiudadDs());
                
                this.validarCiudad((Ciudades) entidad);
                
                ciudadesDAO.eliminar((Ciudades) entidad);
                
                this.inicializaCiudad();
                this.listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(this.municipioSelected);
                this.listAsentamientoHumanoFiltered.clear();
            } else if (entidad instanceof AsentamientoHumano) {
                titulo = "Asentamiento Humano";
                log.info("Eliminando el asentamiento humano: {}", ((AsentamientoHumano) entidad).getAsentamientoDs());
                
                this.validarAsentamiento((AsentamientoHumano) entidad);
                
                asentamientoDAO.eliminar((AsentamientoHumano) entidad);
                
                this.inicializaAsentamiento();
                this.inicializaEntidadPostal();
                this.inicializaTipoAsentamiento();
                
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
                this.limpiar();
            } else if (entidad instanceof EntidadPostal) {
                titulo = "Entidad Postal";
                log.info("Eliminando el asentamiento humano: {}", ((EntidadPostal) entidad).getEntidadpostalDs());
                
                this.validarEntidadPostal((EntidadPostal) entidad);
                
                entidadPostalDAO.eliminar((EntidadPostal) entidad);
                
                this.inicializaEntidadPostal();
                this.listEntidadPostal = entidadPostalDAO.buscarTodos();
            } else if (entidad instanceof TipoAsentamiento) {
                titulo = "Tipo Asentamiento";
                log.info("Eliminando el asentamiento humano: {}", ((TipoAsentamiento) entidad).getTipoasntmntoDs());
                
                this.validarTipoAsentamiento((TipoAsentamiento) entidad);
                
                tipoAsentamientoDAO.eliminar((TipoAsentamiento) entidad);
                
                
                this.inicializaTipoAsentamiento();
                this.listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } else {
                titulo = "Error";
                throw new IllegalArgumentException("Tipo de entidad no soportado: " + entidad.getClass());
            }
            
            mensaje = "Se elimino correctamente";
            severity = FacesMessage.SEVERITY_INFO;
        } catch (InventarioException ex) {
            log.error("Problema para eliminar...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para eliminar...", ex);
            mensaje = "Problema para eliminar.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    private void validarPais(Paises pais) throws InventarioException {
        if (pais.getPaisDesc() == null || pais.getPaisDesc().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción del país.");
        if (pais.getPaisDsCorta() == null || pais.getPaisDsCorta().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción corta del país.");
    }

    private void validarEstado(Estados estado) throws InventarioException {
        if (estado.getEstadoDesc() == null || estado.getEstadoDesc().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción del estado.");
        if (estado.getEstadoDsCorta() == null || estado.getEstadoDsCorta().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción corta del estado.");
    }
    
    private void validarMunicipio(Municipios municipio) throws InventarioException {
        if (municipio.getMunicipioDs() == null || municipio.getMunicipioDs().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción del municipio.");
    }
    
    private void validarCiudad(Ciudades ciudad) throws InventarioException {
        if (ciudad.getCiudadDs() == null || ciudad.getCiudadDs().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción de la ciudad.");
    }
    
    private void validarAsentamiento(AsentamientoHumano asentamiento) throws InventarioException {
        if (asentamiento.getAsentamientoDs() == null || asentamiento.getAsentamientoDs().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción del asentamiento.");
        if (asentamiento.getCp() == null || asentamiento.getCp().trim().isEmpty()) throw new InventarioException("Debes agregar un codigo postal del asentamiento");
        if (asentamiento.getEntidadPostal() == null || asentamiento.getEntidadPostal().getEntidadpostalCve() == null) throw new InventarioException("Debes agregar una entidad postal del asentamiento");
        if (asentamiento.getTipoAsentamiento() == null || asentamiento.getTipoAsentamiento().getTipoasntmntoCve() == null) throw new InventarioException("Debes agregar una tipo de asentamiento");
    }
    
    private void validarEntidadPostal(EntidadPostal entidadPostal) throws InventarioException {
        if (entidadPostal.getEntidadpostalDs() == null || entidadPostal.getEntidadpostalDs().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción de la entidad postal");
    }
    
    private void validarTipoAsentamiento(TipoAsentamiento tipoAsentamiento) throws InventarioException {
        if (tipoAsentamiento.getTipoasntmntoDsCorta() == null || tipoAsentamiento.getTipoasntmntoDsCorta().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción corta del tipo de asentamiento");
        if (tipoAsentamiento.getTipoasntmntoDs() == null || tipoAsentamiento.getTipoasntmntoDs().trim().isEmpty()) throw new InventarioException("Debes agregar una descripción del tipo de asentamiento");
    }
    
    public void inicializaPais(){
        this.paisSelected = new Paises();
    }
    
    public void inicializaEstado(){
        EstadosPK estadosPK = new EstadosPK();
        estadosPK.setPais(this.paisSelected);
        
        this.estadoSelected = new Estados();
        this.estadoSelected.setEstadosPK(estadosPK);
    }
    
    public void inicializaMunicipio(){
        MunicipiosPK municipiosPK = new MunicipiosPK();
        municipiosPK.setEstados(this.estadoSelected);
        
        this.municipioSelected = new Municipios();
        this.municipioSelected.setMunicipiosPK(municipiosPK);
    }
    
    public void inicializaCiudad(){
        CiudadesPK ciudadesPK = new CiudadesPK();
        ciudadesPK.setMunicipios(this.municipioSelected);
        
        this.ciudadSelected = new Ciudades();
        this.ciudadSelected.setCiudadesPK(ciudadesPK);
    }
    
    public void inicializaAsentamiento(){
        AsentamientoHumanoPK asentamientoPK = new AsentamientoHumanoPK();
        asentamientoPK.setCiudades(this.ciudadSelected);
        
        this.asentamientoHumanoSelected = new AsentamientoHumano();
        this.asentamientoHumanoSelected.setAsentamientoHumanoPK(asentamientoPK);
    }
    
    public void inicializaEntidadPostal(){
        this.entidadPostalSelected = new EntidadPostal();
    }
    
    public void inicializaTipoAsentamiento(){
        this.tipoAsentamientoSelected = new TipoAsentamiento();
    }
    
    public void asignarAsentamiento(AsentamientoHumano asentamiento) {
        FacesMessage message = null;
        Severity severity = null;
        String mensaje = null;
        String titulo = "Asentamiento Humano";
        try {
            this.asentamientoHumanoSelected = asentamiento;
            
            if (this.asentamientoHumanoSelected == null) {
                throw new InventarioException("No hay objeto asentamiento seleccionado.");
            }

            log.info("Buscando información del asentamiento");
            log.debug("Domicilio seleccionado: {}", this.asentamientoHumanoSelected.toString());
            
            this.entidadPostalSelected = this.asentamientoHumanoSelected.getEntidadPostal();
            this.tipoAsentamientoSelected = this.asentamientoHumanoSelected.getTipoAsentamiento();
            this.paisSelected = this.asentamientoHumanoSelected.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().getPais();
            this.estadoSelected = this.asentamientoHumanoSelected.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados();
            this.municipioSelected = this.asentamientoHumanoSelected.getAsentamientoHumanoPK().getCiudades().getCiudadesPK().getMunicipios();
            this.ciudadSelected = this.asentamientoHumanoSelected.getAsentamientoHumanoPK().getCiudades();
            this.codigoPostal = this.asentamientoHumanoSelected.getCp();
            
            this.limpiarListasFiltradas();
            
            this.listEstadosFiltered = estadosDAO.buscarPorCriteriosEstados(this.estadoSelected);
            this.listMunicipiosFiltered = municipiosDAO.buscarPorCriterios(this.municipioSelected);
            this.listCiudadesFiltered = this.ciudadesDAO.buscarPorCriteriosCiudades(this.ciudadSelected);
            this.listAsentamientoHumanoFiltered = this.asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
            
            mensaje = "Domicilio seleccionado.";
            severity = FacesMessage.SEVERITY_INFO;

        } catch (InventarioException ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            mensaje = ex.getMessage();
            severity = FacesMessage.SEVERITY_WARN;
        } catch (Exception ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            mensaje = "Problema para buscar un asentamiento.";
            severity = FacesMessage.SEVERITY_ERROR;
        } finally {
            this.codigoPostal = "";
            message = new FacesMessage(severity, titulo, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, message);
            PrimeFaces.current().ajax().update("form:messages", "form:pais", "form:estado", "form:municipio", "form:ciudad", "form:asentamiento");
        }
    }
    
    public void sugerenciasCodigoPostal() {
        this.listAsentamientoHumanoCP = asentamientoDAO.buscaPorCP(this.codigoPostal.trim());
    }
    
    public AsentamientoHumano nuevoAsentamiento(){
        AsentamientoHumano asentamiento = new AsentamientoHumano();
        
        AsentamientoHumanoPK pk = new AsentamientoHumanoPK();
        pk.setCiudades(new Ciudades());
        pk.getCiudades().setCiudadesPK(new CiudadesPK());
        pk.getCiudades().getCiudadesPK().setMunicipios(new Municipios());
        pk.getCiudades().getCiudadesPK().getMunicipios().setMunicipiosPK(new MunicipiosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().setEstados(new Estados());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().setEstadosPK(new EstadosPK());
        pk.getCiudades().getCiudadesPK().getMunicipios().getMunicipiosPK().getEstados().getEstadosPK().setPais(new Paises());
        
        asentamiento.setAsentamientoHumanoPK(pk);
        
        return asentamiento;
    }
    
    public void limpiar(){
        this.codigoPostal = "";
        this.asentamientoHumanoSelected = this.nuevoAsentamiento();
        this.listAsentamientoHumanoCP.clear();
    }
    
    public void limpiarAsentamiento(){
        this.asentamientoHumanoSelected = this.nuevoAsentamiento();
        this.entidadPostalSelected = new EntidadPostal();
        this.tipoAsentamientoSelected = new TipoAsentamiento();
        this.ciudadSelected = new Ciudades();
        this.municipioSelected = new Municipios();
        this.estadoSelected = new Estados();
        this.paisSelected = new Paises();
        this.limpiarListasFiltradas();
    }
    
    public void limpiarListasFiltradas(){
        this.listEstadosFiltered.clear();
        this.listMunicipiosFiltered.clear();
        this.listCiudadesFiltered.clear();
        this.listAsentamientoHumanoFiltered.clear();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters&Setters">
    public List<AsentamientoHumano> getListAsentamientoHumano() {
        return listAsentamientoHumano;
    }

    public void setListAsentamientoHumano(List<AsentamientoHumano> listAsentamientoHumano) {
        this.listAsentamientoHumano = listAsentamientoHumano;
    }

    public List<AsentamientoHumano> getListAsentamientoHumanoFiltered() {
        return listAsentamientoHumanoFiltered;
    }

    public void setListAsentamientoHumanoFiltered(List<AsentamientoHumano> listAsentamientoHumanoFiltered) {
        this.listAsentamientoHumanoFiltered = listAsentamientoHumanoFiltered;
    }

    public List<AsentamientoHumano> getListAsentamientoHumanoCP() {
        return listAsentamientoHumanoCP;
    }

    public void setListAsentamientoHumanoCP(List<AsentamientoHumano> listAsentamientoHumanoCP) {
        this.listAsentamientoHumanoCP = listAsentamientoHumanoCP;
    }

    public AsentamientoHumano getAsentamientoHumanoSelected() {
        return asentamientoHumanoSelected;
    }

    public void setAsentamientoHumanoSelected(AsentamientoHumano asentamientoHumanoSelected) {
        this.asentamientoHumanoSelected = asentamientoHumanoSelected;
    }

    public List<EntidadPostal> getListEntidadPostal() {
        return listEntidadPostal;
    }

    public void setListEntidadPostal(List<EntidadPostal> listEntidadPostal) {
        this.listEntidadPostal = listEntidadPostal;
    }

    public EntidadPostal getEntidadPostalSelected() {
        return entidadPostalSelected;
    }

    public void setEntidadPostalSelected(EntidadPostal entidadPostalSelected) {
        this.entidadPostalSelected = entidadPostalSelected;
    }

    public List<TipoAsentamiento> getListTipoAsentamiento() {
        return listTipoAsentamiento;
    }

    public void setListTipoAsentamiento(List<TipoAsentamiento> listTipoAsentamiento) {
        this.listTipoAsentamiento = listTipoAsentamiento;
    }

    public TipoAsentamiento getTipoAsentamientoSelected() {
        return tipoAsentamientoSelected;
    }

    public void setTipoAsentamientoSelected(TipoAsentamiento tipoAsentamientoSelected) {
        this.tipoAsentamientoSelected = tipoAsentamientoSelected;
    }

    public List<Ciudades> getListCiudadesFiltered() {
        return listCiudadesFiltered;
    }

    public void setListCiudadesFiltered(List<Ciudades> listCiudadesFiltered) {
        this.listCiudadesFiltered = listCiudadesFiltered;
    }

    public Ciudades getCiudadSelected() {
        return ciudadSelected;
    }

    public void setCiudadSelected(Ciudades ciudadSelected) {
        this.ciudadSelected = ciudadSelected;
    }

    public List<Municipios> getListMunicipiosFiltered() {
        return listMunicipiosFiltered;
    }

    public void setListMunicipiosFiltered(List<Municipios> listMunicipiosFiltered) {
        this.listMunicipiosFiltered = listMunicipiosFiltered;
    }

    public Municipios getMunicipioSelected() {
        return municipioSelected;
    }

    public void setMunicipioSelected(Municipios municipioSelected) {
        this.municipioSelected = municipioSelected;
    }

    public List<Estados> getListEstadosFiltered() {
        return listEstadosFiltered;
    }

    public void setListEstadosFiltered(List<Estados> listEstadosFiltered) {
        this.listEstadosFiltered = listEstadosFiltered;
    }

    public Estados getEstadoSelected() {
        return estadoSelected;
    }

    public void setEstadoSelected(Estados estadoSelected) {
        this.estadoSelected = estadoSelected;
    }

    public List<Paises> getListPaises() {
        return listPaises;
    }

    public void setListPaises(List<Paises> listPaises) {
        this.listPaises = listPaises;
    }

    public Paises getPaisSelected() {
        return paisSelected;
    }

    public void setPaisSelected(Paises paisSelected) {
        this.paisSelected = paisSelected;
    }
    
    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    //</editor-fold>

}
