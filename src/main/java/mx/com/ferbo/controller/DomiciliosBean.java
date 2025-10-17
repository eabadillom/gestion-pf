package mx.com.ferbo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
import mx.com.ferbo.util.FacesUtils;

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
    
    private final Map<Class<?>, Consumer<Object>> guardarObjetos = new HashMap<>();
    private final Map<Class<?>, Consumer<Object>> actualizarObjetos = new HashMap<>();
    private final Map<Class<?>, Consumer<Object>> eliminarObjetos = new HashMap<>();
    
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
        this.initGuardarObjetos();
        this.initActualizarObjetos();
        this.initEliminarObjetos();
    }

    public void filtraListadoEstados()
    {
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
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Estados", "Elemento encontrado");
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un estado...", ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Estados", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar un estado...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Estados", "Problema para buscar un estado.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoMunicipios(){
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
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Municipios", "Elemento encontrado");
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un municipio...", ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Municipios", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar un municipio...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Municipios", "Problema para buscar un municipio.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoCiudades(){
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
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Ciudades", "Elemento encontrado");
        } catch (InventarioException ex) {
            log.warn("Problema para buscar una ciudad...", ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Ciudades", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar una ciudad...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Ciudades", "Problema para buscar una ciudad.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraListadoAsentamiento(){
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
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Asentamiento Humano", "Elemento encontrado");
        } catch (InventarioException ex) {
            log.warn("Problema para buscar un asentamiento...", ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Asentamiento Humano", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Asentamiento Humano", "Problema para buscar un asentamiento.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void filtraEntidadPostalTipoAsentamiento(){
        try {
            if(this.asentamientoHumanoSelected == null){
                throw new InventarioException("No hay un asentamiento seleccionado.");
            }
            
            this.entidadPostalSelected = this.asentamientoHumanoSelected.getEntidadPostal();
            this.tipoAsentamientoSelected = this.asentamientoHumanoSelected.getTipoAsentamiento();
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Asentamiento", "Elemento encontrado");
        } catch (InventarioException ex) {
            log.warn("Problema para buscar los elementos...", ex.getMessage());
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Asentamiento", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar los elementos...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Asentamiento", "Problema para buscar los elementos.");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void guardar(T entidad){
        String titulo = entidad.getClass().getSimpleName();
        try{
            log.info("Iniciando el proceso de guardar");
            
            Consumer<Object> accion = guardarObjetos.get(entidad.getClass());
            if (accion == null) {
                log.warn("Entidad no soportado: {}" + entidad.getClass());
                throw new IllegalArgumentException("Problema para guardar, favor de contactar al administrador");
            }

            accion.accept(entidad);
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, "El elemento se guardo con exito");
        } catch (RuntimeException ex) {
            log.error("Se encontro un problema para guardar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, ex.getMessage());
        } catch (Exception ex) {
            log.error("Se encontro un problema para guardar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, "Se encontro un problema para guardar el elemento, favor de contactar al administrador de sistemas");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void actualizar(T entidad){
        String titulo = entidad.getClass().getSimpleName();
        try{
            log.info("Iniciando el proceso de actualizar");
            
            Consumer<Object> actualizador = actualizarObjetos.get(entidad.getClass());
            if (actualizador == null) {
                log.warn("Entidad no soportado: {}" + entidad.getClass());
                throw new IllegalArgumentException("Problema para guardar, favor de contactar al administrador");
            }

            actualizador.accept(entidad);
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, "El elemento se actualizo con exito");
        } catch (RuntimeException ex) {
            log.error("Se encontro un problema para actualizar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, ex.getMessage());
        } catch (Exception ex) {
            log.error("Se encontro un problema para actualizar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, titulo, "Se encontro un problema para actualizar el elemento, favor de contactar al administrador de sistemas");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public <T> void eliminarObjeto(T entidad){
        String titulo = entidad.getClass().getSimpleName();
        try {
            log.info("Iniciando el proceso de eliminar");
            
            Consumer<Object> eliminador = eliminarObjetos.get(entidad.getClass());
            if (eliminador == null) {
                log.warn("Entidad no soportado: {}" + entidad.getClass());
                throw new IllegalArgumentException("Problema para guardar, favor de contactar al administrador");
            }

            eliminador.accept(entidad);
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, "El elemento se elimino correctamente");
        } catch (RuntimeException ex) {
            log.error("Problema para eliminar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, titulo, ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para eliminar...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, titulo, "Se encontro un problema para eliminar el elemento, favor de contactar al administrador de sistemas");
        } finally {
            PrimeFaces.current().ajax().update("form:messages");
        }
    }
    
    public void initGuardarObjetos(){
        guardarObjetos.put(Paises.class, o -> {
            Paises entidad = (Paises) o;
            try {
                Paises auxPais = paisesDAO.buscarUltimoPais();
                int numPais = auxPais.getPaisCve();
                numPais++;
                
                entidad.setPaisCve(numPais);
                this.validarPais(entidad);
                
                paisesDAO.guardar(entidad);
                log.info("Guardando un pais");
                
                this.listPaises = paisesDAO.buscarTodos();
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(Estados.class, o -> {
            Estados entidad = (Estados) o;
            try {
                Estados auxEstado = estadosDAO.buscarUltimoEstado(this.paisSelected);
                int numEstado = (auxEstado != null) ? auxEstado.getEstadosPK().getEstadoCve() : 0;
                numEstado++;
                
                entidad.getEstadosPK().setEstadoCve(numEstado);
                this.validarEstado(entidad);
                
                estadosDAO.guardar(entidad);
                log.info("Guardando un estado");
                
                this.listEstadosFiltered = estadosDAO.buscarPorPais(this.paisSelected);
                this.listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(this.estadoSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(Municipios.class, o -> {
            Municipios entidad = (Municipios) o;
            try {
                Municipios auxMunicipio = municipiosDAO.buscarUltimoMunicipio(this.estadoSelected);
                int numMunicipio = (auxMunicipio != null) ? auxMunicipio.getMunicipiosPK().getMunicipioCve() : 0;
                numMunicipio++;
                
                entidad.getMunicipiosPK().setMunicipioCve(numMunicipio);
                this.validarMunicipio(entidad);
                
                municipiosDAO.guardar(entidad);
                log.info("Guardando un municipio");
                
                this.listMunicipiosFiltered = municipiosDAO.buscarPorCriterios(this.municipioSelected);
                this.listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(this.municipioSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(Ciudades.class, o -> {
            Ciudades entidad = (Ciudades) o;
            try {
                Ciudades auxCiudad = ciudadesDAO.buscarUltimaCidad(this.municipioSelected);
                int numCiudad = (auxCiudad != null) ? auxCiudad.getCiudadesPK().getCiudadCve() : 0;
                numCiudad++;
                
                entidad.getCiudadesPK().setCiudadCve(numCiudad);
                this.validarCiudad(entidad);
                
                ciudadesDAO.guardar(entidad);
                log.info("Guardando una ciudad");
                
                this.listCiudadesFiltered = ciudadesDAO.buscarPorCriteriosCiudades(this.ciudadSelected);
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(this.ciudadSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(AsentamientoHumano.class, o -> {
            AsentamientoHumano entidad = (AsentamientoHumano) o;
            try {
                AsentamientoHumano auxAsentamiento = asentamientoDAO.buscarUltimoAsentamiento();
                int numAsentamiento = (auxAsentamiento != null) ? auxAsentamiento.getAsentamientoHumanoPK().getAsentamientoCve() : 0;
                numAsentamiento++;
                
                entidad.getAsentamientoHumanoPK().setAsentamientoCve(numAsentamiento);
                entidad.setEntidadPostal(this.entidadPostalSelected);
                entidad.setTipoAsentamiento(this.tipoAsentamientoSelected);
                this.validarAsentamiento(entidad);
                
                asentamientoDAO.guardar(entidad);
                log.info("Guardando un asentamiento");
                
                this.codigoPostal = entidad.getCp();
                this.listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCriterios(this.asentamientoHumanoSelected);
                this.listAsentamientoHumanoCP = asentamientoDAO.buscaPorCP(((AsentamientoHumano) entidad).getCp());
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(EntidadPostal.class, o -> {
            EntidadPostal entidad = (EntidadPostal) o;
            try {
                EntidadPostal auxEntidadPostal = entidadPostalDAO.buscarUltimaEntidadPostal();
                int numEntidadPostal = auxEntidadPostal.getEntidadpostalCve();
                numEntidadPostal++;
                
                entidad.setEntidadpostalCve(numEntidadPostal);
                this.validarEntidadPostal(entidad);
                
                entidadPostalDAO.guardar(entidad);
                log.info("Guardando una entidad postal");
                
                this.listEntidadPostal = entidadPostalDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
        
        guardarObjetos.put(TipoAsentamiento.class, o -> {
            TipoAsentamiento entidad = (TipoAsentamiento) o;
            try {
                TipoAsentamiento auxTipoAsentamiento = tipoAsentamientoDAO.buscarUltimoTipoAsentamiento();
                int numTipoAsentamiento = auxTipoAsentamiento.getTipoasntmntoCve();
                numTipoAsentamiento++;
                
                entidad.setTipoasntmntoCve((short) numTipoAsentamiento);
                this.validarTipoAsentamiento(entidad);
                
                tipoAsentamientoDAO.guardar(entidad);
                log.info("Guardando un tipo de asentamiento");
                
                this.listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void initActualizarObjetos() {
        actualizarObjetos.put(Paises.class, o -> {
            Paises entidad = (Paises) o;
            try {
                validarPais(entidad);
                paisesDAO.actualizar(entidad);
                log.info("Actualizando un país");
                listPaises = paisesDAO.buscarTodos();
                listEstadosFiltered = estadosDAO.buscarPorPais(paisSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(Estados.class, o -> {
            Estados entidad = (Estados) o;
            try {
                validarEstado(entidad);
                estadosDAO.actualizar(entidad);
                log.info("Actualizando un estado");
                listEstadosFiltered = estadosDAO.buscarPorPais(paisSelected);
                listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(estadoSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(Municipios.class, o -> {
            Municipios entidad = (Municipios) o;
            try {
                validarMunicipio(entidad);
                municipiosDAO.actualizar(entidad);
                log.info("Actualizando un municipio");
                listMunicipiosFiltered = municipiosDAO.buscarPorCriterios(municipioSelected);
                listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(municipioSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(Ciudades.class, o -> {
            Ciudades entidad = (Ciudades) o;
            try {
                validarCiudad(entidad);
                ciudadesDAO.actualizar(entidad);
                log.info("Actualizando una ciudad");
                listCiudadesFiltered = ciudadesDAO.buscarPorCriteriosCiudades(ciudadSelected);
                listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(ciudadSelected);
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(AsentamientoHumano.class, o -> {
            AsentamientoHumano entidad = (AsentamientoHumano) o;
            try {
                validarAsentamiento(entidad);
                asentamientoDAO.actualizar(entidad);
                log.info("Actualizando un asentamiento humano");
                codigoPostal = entidad.getCp();
                listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCriterios(asentamientoHumanoSelected);
                listAsentamientoHumanoCP = asentamientoDAO.buscaPorCP(entidad.getCp());
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(EntidadPostal.class, o -> {
            EntidadPostal entidad = (EntidadPostal) o;
            try {
                validarEntidadPostal(entidad);
                entidadPostalDAO.actualizar(entidad);
                log.info("Actualizando una entidad postal");
                listEntidadPostal = entidadPostalDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });

        actualizarObjetos.put(TipoAsentamiento.class, o -> {
            TipoAsentamiento entidad = (TipoAsentamiento) o;
            try {
                validarTipoAsentamiento(entidad);
                tipoAsentamientoDAO.actualizar(entidad);
                log.info("Actualizando un tipo de asentamiento");
                listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void initEliminarObjetos() 
    {
        eliminarObjetos.put(Paises.class, o -> {
            Paises entidad = (Paises) o;
            log.info("Eliminando el país: {}", entidad.getPaisDesc());
            try {
                validarPais(entidad);
                paisesDAO.eliminar(entidad);
                inicializaPais();
                listPaises = paisesDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(Estados.class, o -> {
            Estados entidad = (Estados) o;
            log.info("Eliminando el estado: {}", entidad.getEstadoDesc());
            try {
                validarEstado(entidad);
                estadosDAO.eliminar(entidad);
                inicializaEstado();
                listEstadosFiltered = estadosDAO.buscarPorPais(paisSelected);
                listMunicipiosFiltered.clear();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(Municipios.class, o -> {
            Municipios entidad = (Municipios) o;
            log.info("Eliminando el municipio: {}", entidad.getMunicipioDs());
            try{
                validarMunicipio(entidad);
                municipiosDAO.eliminar(entidad);
                inicializaMunicipio();
                listMunicipiosFiltered = municipiosDAO.buscarPorPaisEstado(estadoSelected);
                listCiudadesFiltered.clear();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(Ciudades.class, o -> {
            Ciudades entidad = (Ciudades) o;
            log.info("Eliminando la ciudad: {}", entidad.getCiudadDs());
            try {
                validarCiudad(entidad);
                ciudadesDAO.eliminar(entidad);
                inicializaCiudad();
                listCiudadesFiltered = ciudadesDAO.buscarPorPaisEstadoMunicipio(municipioSelected);
                listAsentamientoHumanoFiltered.clear();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(AsentamientoHumano.class, o -> {
            AsentamientoHumano entidad = (AsentamientoHumano) o;
            log.info("Eliminando el asentamiento humano: {}", entidad.getAsentamientoDs());
            try {
                validarAsentamiento(entidad);
                asentamientoDAO.eliminar(entidad);
                inicializaAsentamiento();
                inicializaEntidadPostal();
                inicializaTipoAsentamiento();
                listAsentamientoHumanoFiltered = asentamientoDAO.buscarPorCiudad(ciudadSelected);
                limpiar();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(EntidadPostal.class, o -> {
            EntidadPostal entidad = (EntidadPostal) o;
            log.info("Eliminando la entidad postal: {}", entidad.getEntidadpostalDs());
            try {
                validarEntidadPostal(entidad);
                entidadPostalDAO.eliminar(entidad);
                inicializaEntidadPostal();
                listEntidadPostal = entidadPostalDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        eliminarObjetos.put(TipoAsentamiento.class, o -> {
            TipoAsentamiento entidad = (TipoAsentamiento) o;
            log.info("Eliminando tipo de asentamiento: {}", entidad.getTipoasntmntoDs());
            try {
                validarTipoAsentamiento(entidad);
                tipoAsentamientoDAO.eliminar(entidad);
                inicializaTipoAsentamiento();
                listTipoAsentamiento = tipoAsentamientoDAO.buscarTodos();
            } catch (InventarioException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
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
            
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, "Asentamiento Humano", "Domicilio seleccionado");
        } catch (InventarioException ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, "Asentamiento Humano", ex.getMessage());
        } catch (Exception ex) {
            log.error("Problema para buscar un asentamiento...", ex);
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, "Asentamiento Humano", "Problema para buscar un asentamiento");
        } finally {
            this.codigoPostal = "";
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
