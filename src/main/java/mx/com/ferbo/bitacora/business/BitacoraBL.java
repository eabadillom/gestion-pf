package mx.com.ferbo.bitacora.business;

import java.io.File;
import java.sql.Connection;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferbo.gestion.reports.jasper.BitacoraJR;
import com.ferbo.gestion.tools.GestionException;
import com.ferbo.tools.exception.BusinessException;
import com.ferbo.tools.exception.SystemException;
import com.ferbo.tools.util.date.DateFormatter;
import com.ferbo.tools.validation.ObjectValidator;

import mx.com.ferbo.bitacora.dao.BitacoraDAO;
import mx.com.ferbo.bitacora.dto.BitacoraDTO;
import mx.com.ferbo.bitacora.enums.NombrePantalla;
import mx.com.ferbo.bitacora.enums.TipoPantalla;
import mx.com.ferbo.bitacora.model.ContextoBitacora;
import mx.com.ferbo.bitacora.model.EventoBitacora;
import mx.com.ferbo.bitacora.model.FiltroBitacora;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.utils.ToolException;

@Named
@RequestScoped
public class BitacoraBL {

    private static final Logger log = LogManager.getLogger(BitacoraBL.class);

    @Inject
    private BitacoraDAO dao;

    public void registrarEnBitacora(List<EventoBitacora> eventos) {

        Integer numeroEvento = null;

        try {
            log.info("Inicia el proceso para registrar los eventos en la bitacora.");
            for (int i = 0; i < eventos.size(); i++) {
                numeroEvento = i;
                dao.guardar(eventos.get(i));
                log.info("Se registro el evento: {}", eventos.get(i));
            }
            log.info("Finaliza el proceso para registrar los eventos en la bitacora.");
        } catch (InventarioException ex) {
            throw new SystemException("Error al momento de guardar el evento : "
                    + eventos.get(numeroEvento).getDescripcion() + ". En la bitacora.");
        }
    }

    public List<EventoBitacora> obtenerPorFiltros(BitacoraDTO dto) {
        try {
            ObjectValidator.notNull(dto, "el grupo seleccionado");
            FiltroBitacora filtros = dtoToFiltro(dto);
            return dao.buscarPorFiltros(filtros);
        } catch (SystemException ex) {
            log.warn("{}", ex);
            throw ex;
        }
    }

    public List<BitacoraDTO> obtenerGruposPorFiltros(FiltroBitacora filtros) {
        List<BitacoraDTO> grupos = new ArrayList<>();
        try {
            List<Object[]> resulsados = dao.buscarGruposPorFiltros(filtros);
            if (resulsados.isEmpty()) {
                return grupos;
            }
            return listResultSetToDTO(resulsados);
        } catch (SystemException ex) {
            log.warn("{}", ex);
            throw ex;
        }
    }

    public void agregarORemplazarSiExiste(
			List<EventoBitacora> eventos,
			ContextoBitacora context,
			String descripcion,
			String documento) {

		int index = 0;

		EventoBitacora evento = EventoBitacora.of(context)
				.documento(documento)
				.descripcion(descripcion)
				.build();

		if (eventos.isEmpty()) {
			eventos.add(evento);
		} else {
			eventos.set(index, evento);
		}
	}

    public List<BitacoraDTO> listResultSetToDTO(List<Object[]> resultados) {
        List<BitacoraDTO> eventos = new ArrayList<>();
         for (Object[] resultado : resultados) {
                BitacoraDTO evento = new BitacoraDTO();
                evento.setDocumento((String) resultado[0]);
                evento.setIdUsuario(((Number) resultado[1]).intValue());
                evento.setNombreUsuario((String) resultado[2]);
                evento.setApellido1Usuario((String) resultado[3]);
                evento.setApellido2Usuario((String) resultado[4]);
                evento.setNombrePantalla((String) resultado[5]);
                evento.setTipoPantalla((String) resultado[6]);
                evento.setMomento((Date) resultado[7]);

                eventos.add(evento);
            }
            return eventos;
    }

    public FiltroBitacora dtoToFiltro(BitacoraDTO dto) {
        FiltroBitacora filtros = new FiltroBitacora();

        Date momentoDate = dto.getMomento();
        String monetoString = DateFormatter.format(momentoDate, "yyyy-MM-dd");
        LocalDate momento = DateFormatter.parseToLocalDate(monetoString, "yyyy-MM-dd");
        
        filtros.setInicio(momento);
        filtros.setFin(momento);
        
        filtros.setIdUsuario(dto.getIdUsuario());
        filtros.setDocumento(dto.getDocumento());
        
        List<TipoPantalla> tiposPantalla = tiposPantallaEnumToList();
        List<NombrePantalla> nombresPantalla = nombresPantallaEnumToList();
        
        for (TipoPantalla tipoPantalla : tiposPantalla) {
            if (dto.getTipoPantalla().equalsIgnoreCase(tipoPantalla.toString())){
                filtros.setTipoPantalla(tipoPantalla);
            }
        }

        for(NombrePantalla nombrePantalla : nombresPantalla) {
            if (dto.getNombrePantalla().equalsIgnoreCase(nombrePantalla.toString())){
                filtros.setNombrePantalla(nombrePantalla);
            }
        }
        
        return filtros;
    }

    public List<TipoPantalla> tiposPantallaEnumToList(){
        return Arrays.asList(TipoPantalla.values());
    }

    public List<NombrePantalla> nombresPantallaEnumToList() {
        return Arrays.asList(NombrePantalla.values());
    }

    public Optional<byte[]> exportToFile(FiltroBitacora filtros, String extension) throws GestionException, ToolException {
        Optional<byte[]> response = null;
		byte[] bytes = null;
		String sLogoPath = "/images/logoF.png";
		File logoFile = new File(getClass().getResource(sLogoPath).getFile());
		log.info("Imagen: {}", logoFile.getPath());
		
		Connection conn = null;

        try {
            conn = EntityManagerUtil.getConnection();

            String inicioString = DateFormatter.format(filtros.getInicio(), "yyyy-MM-dd");
            Date inicio = DateFormatter.parseToDate(inicioString, "yyyy-MM-dd");
            String finString = DateFormatter.format(filtros.getFin(), "yyyy-MM-dd");
            Date fin = DateFormatter.parseToDate(finString, "yyyy-MM-dd");

            String tipo = (filtros.getTipoPantalla() == null) ? null : filtros.getTipoPantalla().toString();
            String nombre = (filtros.getNombrePantalla() == null) ? null : filtros.getNombrePantalla().toString(); 

            Integer usuario = filtros.getIdUsuario();

            switch (extension) {
                case "PDF":
                    bytes = new BitacoraJR(conn, sLogoPath).getPDF(inicio, fin, usuario, tipo, nombre);
                    break;
                
                case "XLSX":
                    bytes = new BitacoraJR(conn, sLogoPath).getXLSX(inicio, fin, usuario, tipo, nombre);
                    break;
                    
                default:
                    throw new BusinessException("El sistema no soporta exportación en el formato");
            }

            response = Optional.of(bytes);
        } catch (GestionException ex) {
            log.warn("{}", ex.getMessage(), ex);
            throw ex;
        } catch (ParseException ex) {
            log.warn("{}", ex.getMessage(), ex);
            throw new ToolException("Error al convertir una de las fechas seleccionadas");
        }

        return response;        
    }


}
