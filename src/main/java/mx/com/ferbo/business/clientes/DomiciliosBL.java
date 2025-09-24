package mx.com.ferbo.business.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mx.com.ferbo.dao.AsentamientoHumanoDAO;
import mx.com.ferbo.dao.CiudadesDAO;
import mx.com.ferbo.dao.EstadosDAO;
import mx.com.ferbo.dao.MunicipiosDAO;
import mx.com.ferbo.dao.PaisesDAO;

import mx.com.ferbo.model.AsentamientoHumano;
import mx.com.ferbo.model.AsentamientoHumanoPK;
import mx.com.ferbo.model.Ciudades;
import mx.com.ferbo.model.CiudadesPK;
import mx.com.ferbo.model.Estados;
import mx.com.ferbo.model.EstadosPK;
import mx.com.ferbo.model.Municipios;
import mx.com.ferbo.model.MunicipiosPK;
import mx.com.ferbo.model.Paises;
import mx.com.ferbo.util.InventarioException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

/**
 *
 * @author alberto
 */
public class DomiciliosBL implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private static Logger log = LogManager.getLogger(DomiciliosBL.class);

    private List<Paises> lstPaises;
    private List<Paises> lstPaisesFiltered;
    private Paises paisSelected;
    private PaisesDAO paisesDAO;

    private List<Estados> lstEstados;
    private List<Estados> lstEstadosFiltered;
    private Estados estadoSelected;
    private EstadosDAO estadosDAO;

    private List<Municipios> lstMunicipios;
    private List<Municipios> lstMunicipiosFiltered;
    private Municipios municipioSelected;
    private MunicipiosDAO municipiosDAO;

    private List<Ciudades> lstCiudades;
    private List<Ciudades> lstCiudadesFiltered;
    private Ciudades ciudadSelected;
    private CiudadesDAO ciudadesDAO;

    private List<AsentamientoHumano> lstAsentamientos;
    private List<AsentamientoHumano> lstAsentamientoHumanoFiltered;
    private AsentamientoHumano asentamientoSelected;
    private AsentamientoHumanoDAO asentamientoHumanoDAO;
    
    public DomiciliosBL(){
        paisesDAO = new PaisesDAO();
        estadosDAO = new EstadosDAO();
        municipiosDAO = new MunicipiosDAO();
        ciudadesDAO = new CiudadesDAO();
        asentamientoHumanoDAO = new AsentamientoHumanoDAO();
        
        paisSelected = new Paises();
        estadoSelected = new Estados();
        municipioSelected = new Municipios();
        ciudadSelected = new Ciudades();
        asentamientoSelected = new AsentamientoHumano();
        
        lstPaises = paisesDAO.buscarTodos();
        lstEstados = new ArrayList<>();
        lstMunicipios = new ArrayList<>();
        lstCiudades = new ArrayList<>();
        lstAsentamientos = new ArrayList<>();
        
        lstPaisesFiltered = new ArrayList<>();
        lstEstadosFiltered = new ArrayList<>();
        lstMunicipiosFiltered = new ArrayList<>();
        lstCiudadesFiltered = new ArrayList<>();
        lstAsentamientoHumanoFiltered = new ArrayList<>();
    }
    
    public void limpiaDomicilios(){
        ciudadSelected = new Ciudades();
        asentamientoSelected = new AsentamientoHumano();
        paisSelected = new Paises();
        estadoSelected = new Estados();
        municipioSelected = new Municipios();
    }
    
    public void filtraListadoPaises() {
        lstPaisesFiltered.clear();
        lstPaisesFiltered = lstPaises.stream()
            .filter(pa -> paisSelected != null ? (pa.getPaisCve().intValue() == paisSelected.getPaisCve().intValue()) : false)
            .collect(Collectors.toList());
        log.info("Paises: {}", lstPaisesFiltered.toString());
        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion", "form:panel-actClienteDireccion");
    }

    public void filtraListadoEstados() throws InventarioException {
        if(paisSelected != null){
            throw new InventarioException("Debes seleccionar un pais");
        }
        
        lstEstadosFiltered.clear();
        
        EstadosPK estadosPK = new EstadosPK();
        estadosPK.setPaisCve(paisSelected.getPaisCve());
        
        Estados estadoAux = new Estados();
        estadoAux.setPaises(paisSelected);
        
        estadoSelected.setEstadosPK(estadosPK);
        
        lstEstados = estadosDAO.buscarPorCriterios(estadoAux);
        lstEstadosFiltered = lstEstados;
        log.info("Estados: {}", lstEstadosFiltered.toString());
        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion", "form:panel-actClienteDireccion");
    }

    public void filtraListadoMunicipios() throws InventarioException {
        if(estadoSelected != null){
            throw new InventarioException("Debes seleccionar un estado");
        }
        
        lstMunicipiosFiltered.clear();
        
        MunicipiosPK municipiosPK = new MunicipiosPK();
        municipiosPK.setEstadoCve(estadoSelected.getEstadosPK().getEstadoCve());
        municipiosPK.setPaisCve(estadoSelected.getEstadosPK().getPaisCve());
        
        Municipios municipioAux = new Municipios();
        municipioAux.setMunicipiosPK(municipiosPK);
        
        municipioSelected.setMunicipiosPK(municipiosPK);
        
        lstMunicipios = municipiosDAO.buscarPorPaisEstado(municipioAux);
        lstMunicipiosFiltered = lstMunicipios;
        log.info("Municipios: {}", lstMunicipiosFiltered.toString());
        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion", "form:panel-actClienteDireccion");
    }

    public void filtraListadoCiudades() throws InventarioException {
        if(municipioSelected != null){
            throw new InventarioException("Debes seleccionar un municipio");
        }
        
        lstCiudadesFiltered.clear();
        
        CiudadesPK ciudadesPK = new CiudadesPK();
        ciudadesPK.setPaisCve(municipioSelected.getMunicipiosPK().getPaisCve());
        ciudadesPK.setEstadoCve(municipioSelected.getMunicipiosPK().getEstadoCve());
        ciudadesPK.setMunicipioCve(municipioSelected.getMunicipiosPK().getMunicipioCve());
        
        Ciudades ciudadAux = new Ciudades();
        ciudadAux.setCiudadesPK(ciudadesPK);
        
        ciudadSelected.setCiudadesPK(ciudadesPK);
        
        lstCiudades = ciudadesDAO.buscarPorCriteriosCiudades(ciudadAux);
        lstCiudadesFiltered = lstCiudades;
        log.info("Ciudades: {}", lstCiudadesFiltered.toString());
        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion", "form:panel-actClienteDireccion");
    }

    public void filtraListadoAsentamientoHumano() throws InventarioException {
        if(ciudadSelected != null){
            throw new InventarioException("Debes seleccionar una ciudad");
        }
        
        lstAsentamientoHumanoFiltered.clear();
        
        AsentamientoHumanoPK coloniaPKAux = new AsentamientoHumanoPK();
        coloniaPKAux.setCiudades(ciudadSelected);
        
        AsentamientoHumano coloniaAux = new AsentamientoHumano();
        coloniaAux.setAsentamientoHumanoPK(coloniaPKAux);
        
        asentamientoSelected.setAsentamientoHumanoPK(coloniaAux.getAsentamientoHumanoPK());
        
        lstAsentamientos = asentamientoHumanoDAO.buscarPorCriterios(coloniaAux);
        lstAsentamientoHumanoFiltered = lstAsentamientos;
        log.info("Asentamientos: {}", lstAsentamientoHumanoFiltered.toString());
        PrimeFaces.current().ajax().update("form:panel-addClienteDireccion", "form:panel-actClienteDireccion");
    }
    
    public void actualizarAsentamientos(AsentamientoHumano auxAsentamientos){
                
    }

    // Getters & Setters
    public List<Paises> getLstPaises() {
        return lstPaises;
    }

    public void setLstPaises(List<Paises> lstPaises) {
        this.lstPaises = lstPaises;
    }

    public Paises getPaisSelected() {
        return paisSelected;
    }
    
    public void setPaisSelected(Paises paisSelected) {
        this.paisSelected = paisSelected;
    }

    public List<Paises> getLstPaisesFiltered() {
        return lstPaisesFiltered;
    }

    public void setLstPaisesFiltered(List<Paises> lstPaisesFiltered) {
        this.lstPaisesFiltered = lstPaisesFiltered;
    }

    public PaisesDAO getPaisesDAO() {
        return paisesDAO;
    }

    public List<Estados> getLstEstados() {
        return lstEstados;
    }

    public void setLstEstados(List<Estados> lstEstados) {
        this.lstEstados = lstEstados;
    }

    public List<Estados> getLstEstadosFiltered() {
        return lstEstadosFiltered;
    }

    public void setLstEstadosFiltered(List<Estados> lstEstadosFiltered) {
        this.lstEstadosFiltered = lstEstadosFiltered;
    }

    public Estados getEstadoSelected() {
        return estadoSelected;
    }

    public void setEstadoSelected(Estados estadoSelected) {
        this.estadoSelected = estadoSelected;
    }

    public EstadosDAO getEstadosDAO() {
        return estadosDAO;
    }

    public List<Municipios> getLstMunicipios() {
        return lstMunicipios;
    }

    public void setLstMunicipios(List<Municipios> lstMunicipios) {
        this.lstMunicipios = lstMunicipios;
    }

    public List<Municipios> getLstMunicipiosFiltered() {
        return lstMunicipiosFiltered;
    }

    public void setLstMunicipiosFiltered(List<Municipios> lstMunicipiosFiltered) {
        this.lstMunicipiosFiltered = lstMunicipiosFiltered;
    }

    public Municipios getMunicipioSelected() {
        return municipioSelected;
    }

    public void setMunicipioSelected(Municipios municipioSelected) {
        this.municipioSelected = municipioSelected;
    }

    public MunicipiosDAO getMunicipiosDAO() {
        return municipiosDAO;
    }

    public List<Ciudades> getLstCiudades() {
        return lstCiudades;
    }

    public void setLstCiudades(List<Ciudades> lstCiudades) {
        this.lstCiudades = lstCiudades;
    }

    public List<Ciudades> getLstCiudadesFiltered() {
        return lstCiudadesFiltered;
    }

    public void setLstCiudadesFiltered(List<Ciudades> lstCiudadesFiltered) {
        this.lstCiudadesFiltered = lstCiudadesFiltered;
    }

    public Ciudades getCiudadSelected() {
        return ciudadSelected;
    }

    public void setCiudadSelected(Ciudades ciudadSelected) {
        this.ciudadSelected = ciudadSelected;
    }

    public CiudadesDAO getCiudadesDAO() {
        return ciudadesDAO;
    }

    public List<AsentamientoHumano> getLstAsentamientos() {
        return lstAsentamientos;
    }

    public List<AsentamientoHumano> getLstAsentamientoHumanoFiltered() {
        return lstAsentamientoHumanoFiltered;
    }

    public void setLstAsentamientoHumanoFiltered(List<AsentamientoHumano> lstAsentamientoHumanoFiltered) {
        this.lstAsentamientoHumanoFiltered = lstAsentamientoHumanoFiltered;
    }

    public void setLstAsentamientos(List<AsentamientoHumano> lstAsentamientos) {
        this.lstAsentamientos = lstAsentamientos;
    }

    public AsentamientoHumano getAsentamientoSelected() {
        return asentamientoSelected;
    }

    public void setAsentamientoSelected(AsentamientoHumano asentamientoSelected) {
        this.asentamientoSelected = asentamientoSelected;
    }

    public AsentamientoHumanoDAO getAsentamientoHumanoDAO() {
        return asentamientoHumanoDAO;
    }

}
