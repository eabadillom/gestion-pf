package mx.com.ferbo.bitacora.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.StreamedContent;

import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.util.date.DateFormatter;

import mx.com.ferbo.bitacora.business.BitacoraBL;
import mx.com.ferbo.bitacora.dto.BitacoraDTO;
import mx.com.ferbo.bitacora.enums.NombrePantalla;
import mx.com.ferbo.bitacora.enums.TipoPantalla;
import mx.com.ferbo.bitacora.model.EventoBitacora;
import mx.com.ferbo.bitacora.model.FiltroBitacora;
import mx.com.ferbo.business.UsuarioBL;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.utils.ToolException;

@Named
@ViewScoped
public class BitacoraBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LogManager.getLogger(BitacoraBean.class);

    private List<EventoBitacora> eventos;

    private List<BitacoraDTO> grupos;

    private FiltroBitacora filtros;

    private NombrePantalla nombrePantallaSeleccionado;

    private TipoPantalla tipoPantallaSeleccionado;

    private final String title = "Bitácora";

    private String message = "";

    private List<LocalDate> rangoFechas;

    @Inject
    private BitacoraBL bitacoraBL;

    private List<Usuario> usuarios;

    private Boolean usuariosActivos = Boolean.TRUE;

    private BitacoraDTO grupoSelected;

    private StreamedContent bitacora;

    public BitacoraBean() {
        LocalDate hoy = LocalDate.now();
        filtros = new FiltroBitacora();
        filtros.setInicio(hoy);
        filtros.setFin(hoy);
        eventos = new ArrayList<>();
        rangoFechas = new ArrayList<>();
        rangoFechas.add(hoy);
        rangoFechas.add(hoy);
        usuarios = new ArrayList<>();
        grupoSelected = new BitacoraDTO();
    }

    @PostConstruct
    public void init() {

        try {
            log.info("Inicia proceso para cargar los grupos de la bitacora del dia de hoy");
            grupos = bitacoraBL.obtenerGruposPorFiltros(filtros);
            obtenerUsuariosActivosOInactivos();
            grupoSelected.setMomento(new Date());
            log.info("Finaliza proceso para cargar bitacora del dia de hoy");
        } catch (SystemException ex) {
            log.warn("Hubo un problema al carga la bitacora del dia de hoy. {}", ex.getMessage(), ex);
        }
    }

    public void filtrarGrupos() {
        try {
            if (rangoFechas != null && rangoFechas.size() == 2) {
                filtros.setInicio(rangoFechas.get(0));
                filtros.setFin(rangoFechas.get(1));
            }
            grupos = bitacoraBL.obtenerGruposPorFiltros(filtros);
            message = "La bitacora se filtro exitosamente";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, title, message);
        } catch (SystemException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
            message = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, title, message);
        } finally {
            actualizarMensajes();
        }
    }

    public void detalleGrupo(BitacoraDTO grupo) {
        try {
            grupoSelected = grupo;
            eventos = bitacoraBL.obtenerPorFiltros(grupo);
            message = "La bitacora se filtro exitosamente";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, title, message);
        } catch (SystemException ex) {
            log.warn("Error: {}", ex.getMessage(), ex);
        } finally {
            actualizarMensajes();
        }
    }

    public void obtenerUsuariosActivosOInactivos() {
        String estado = (usuariosActivos) ? "A" : "B";
        try {
            this.usuarios = UsuarioBL.obtenerUsuariosPorStatus(estado);
            this.message = "Los usuarios se cargaron con exito";
            FacesUtils.addMessage(FacesMessage.SEVERITY_INFO, title, message);
        } catch (SystemException ex) {
            log.warn("Error...}", ex);
        } finally {
            actualizarMensajes();
        }
    }

    public void imprimirBitacora(String extension) {
        try {
            String inicioFecha = DateFormatter.format(this.filtros.getInicio(), "dd-MM-yyyy");
            String finFecha = DateFormatter.format(this.filtros.getFin(), "dd-MM-yyyy");
            String filename = String.format("Bitacora" + inicioFecha + "al" + finFecha);
            byte[] bytes = this.bitacoraBL.exportToFile(this.filtros, extension)
                    .orElseThrow(() -> new ToolException("Hubo un problema para generar el PDF de la bitácora"));
            if("pdf".equalsIgnoreCase(extension)) {
            	this.bitacora = FacesUtils.toPDF(bytes, filename);
            	return;
            }
            
            if("xlsx".equalsIgnoreCase(extension)) {
            	this.bitacora = FacesUtils.toXLSX(bytes, filename);
            	return;
            }
            
        } catch (ToolException ex) {
            log.warn("{}", ex.getMessage(), ex);
            message = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, title, message);
        } catch (BusinessException ex) {
            log.warn("{}", ex.getMessage(), ex);
            message = ex.getMessage();
            FacesUtils.addMessage(FacesMessage.SEVERITY_WARN, title, message);
        } catch (Exception ex) {
            log.error("Error al generar PDF de bitácora. {}", ex.getMessage(), ex);
            message = "Error al generar PDF de la bitácora. \nContacte con el administrador del sistema";
            FacesUtils.addMessage(FacesMessage.SEVERITY_ERROR, title, message);
        } finally {
            actualizarMensajes();
        }
    }

    public String nombreCompleto(BitacoraDTO bitacoraDTO) {
        return bitacoraDTO.getNombreUsuario() + " " + bitacoraDTO.getApellido1Usuario() + " "
                + bitacoraDTO.getApellido2Usuario();
    }

    private void actualizarMensajes() {
        PrimeFaces.current().ajax().update("form:messages");
    }

    public List<LocalDate> getRangoFechas() {
        return rangoFechas;
    }

    public void setRangoFechas(List<LocalDate> rangoFechas) {
        this.rangoFechas = rangoFechas;
    }

    public List<EventoBitacora> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoBitacora> eventos) {
        this.eventos = eventos;
    }

    public List<NombrePantalla> getNombrePantallas() {
        return bitacoraBL.nombresPantallaEnumToList();
    }

    public List<TipoPantalla> getTipoPantallas() {
        return bitacoraBL.tiposPantallaEnumToList();
    }

    public FiltroBitacora getFiltros() {
        return filtros;
    }

    public void setFiltros(FiltroBitacora filtros) {
        this.filtros = filtros;
    }

    public NombrePantalla getNombrePantallaSeleccionado() {
        return nombrePantallaSeleccionado;
    }

    public void setNombrePantallaSeleccionado(NombrePantalla nombrePantallaSeleccionado) {
        this.nombrePantallaSeleccionado = nombrePantallaSeleccionado;
    }

    public TipoPantalla getTipoPantallaSeleccionado() {
        return tipoPantallaSeleccionado;
    }

    public void setTipoPantallaSeleccionado(TipoPantalla tipoPantallaSeleccionado) {
        this.tipoPantallaSeleccionado = tipoPantallaSeleccionado;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getUsuariosActivos() {
        return usuariosActivos;
    }

    public void setUsuariosActivos(Boolean usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public List<BitacoraDTO> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<BitacoraDTO> grupos) {
        this.grupos = grupos;
    }

    public BitacoraDTO getGrupoSelected() {
        return grupoSelected;
    }

    public void setGrupoSelected(BitacoraDTO grupoSelected) {
        this.grupoSelected = grupoSelected;
    }

    public StreamedContent getBitacora() {
        return bitacora;
    }

    public void setBitacora(StreamedContent bitacora) {
        this.bitacora = bitacora;
    }

}