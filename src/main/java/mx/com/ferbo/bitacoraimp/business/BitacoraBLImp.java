package mx.com.ferbo.bitacoraimp.business;

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

import com.ferbo.bitacora.business.BitacoraBL;
import com.ferbo.bitacora.dto.BitacoraDTO;
import com.ferbo.bitacora.exception.BitacoraException;
import com.ferbo.bitacora.model.Bitacora;
import com.ferbo.bitacora.model.FiltroBitacora;
import com.ferbo.gestion.reports.jasper.BitacoraJR;
import com.ferbo.gestion.tools.GestionException;

import com.ferbo.tools.util.date.DateFormatter;

import mx.com.ferbo.bitacoraimp.dao.BitacoraDAOImp;
import mx.com.ferbo.bitacoraimp.enums.NombrePantalla;
import mx.com.ferbo.bitacoraimp.enums.TipoPantalla;
import mx.com.ferbo.util.EntityManagerUtil;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class BitacoraBLImp implements BitacoraBL{

    private static final Logger log = LogManager.getLogger(BitacoraBL.class);

    @Inject
    private BitacoraDAOImp dao;

    @Override
    public void mostrarInfo(String mensaje) {
        log.info(mensaje);
    }

    @Override
    public void mostrarWaring(String mensaje, Exception ex) {
        log.warn("{}: {}", mensaje, ex.getMessage(), ex);
    }

    @Override
    public void mostrarError(String mensaje, Exception ex) {
        log.error("{}: {}", mensaje, ex.getMessage(), ex);
    }
    
    @Override
    public List<String> tiposPantallaEnumToList(){
        List<TipoPantalla> lista = Arrays.asList(TipoPantalla.values());
        List<String> tiposDePantalla = new ArrayList<>();
        for (TipoPantalla tipoPantalla : lista) {
            tiposDePantalla.add(tipoPantalla.toString());
        }
        return tiposDePantalla;
    }

    @Override
    public List<String> nombresPantallaEnumToList() {
        List<NombrePantalla> lista = Arrays.asList(NombrePantalla.values());
        List<String> nombresDePantallas = new ArrayList<>();
        for (NombrePantalla nombrePantalla : lista) {
            nombresDePantallas.add(nombrePantalla.toString());
        }
        return nombresDePantallas;
    }

   
    @Override
    public List<Object[]> buscarGruposPorFiltrosImp(FiltroBitacora filtros) throws BitacoraException {
        return dao.buscarGruposPorFiltros(filtros);
    }

    @Override
    public List<Bitacora> buscarPorFiltrosImp(FiltroBitacora filtros) throws BitacoraException {
        return dao.buscarPorFiltros(filtros);
    }

    @Override
    public void guardarEvento(Bitacora evento) throws BitacoraException{
        try {
            dao.guardar(evento);
        } catch (InventarioException ex) {
            throw new BitacoraException("Persistencia", ex.getMessage());
        }
    }

    @Override
    public LocalDate obtenerMomentoBicatoraFormateado(BitacoraDTO dto) {
        Date momentoDate = dto.getMomento();
        String monetoString = DateFormatter.format(momentoDate, "yyyy-MM-dd");
        LocalDate momento = DateFormatter.parseToLocalDate(monetoString, "yyyy-MM-dd");
        return momento;
    }


    @Override
    public Optional<byte[]> exportToFile(FiltroBitacora filtros, String extension) throws BitacoraException {
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

            String tipo = ("".equalsIgnoreCase(filtros. getTipoPantalla())) ? null : filtros.getTipoPantalla();
            String nombre = ("".equalsIgnoreCase(filtros.getNombrePantalla())) ? null : filtros.getNombrePantalla(); 

            Integer usuario = filtros.getIdUsuario();

            switch (extension) {
                case "PDF":
                    bytes = new BitacoraJR(conn, sLogoPath).getPDF(inicio, fin, usuario, tipo, nombre);
                    break;
                
                case "XLSX":
                    bytes = new BitacoraJR(conn, sLogoPath).getXLSX(inicio, fin, usuario, tipo, nombre);
                    break;
                    
                default:
                    throw new BitacoraException("La bitacora no soporta exportación en el formato seleccionado");
            }

            response = Optional.of(bytes);
        } catch (GestionException ex) {
            log.warn("Error de gestion: {}", ex.getMessage(), ex);
            throw new BitacoraException("Archivo bitácora", "Hubo un problema al momento de generar la bitácora");
        } catch (ParseException ex) {
            log.warn("Error de parseo: {}", ex.getMessage(), ex);
            throw new BitacoraException("Conversión de datos" ,"Hubo un problema al convertir una de las fechas seleccionadas");
        }

        return response;        
    }

}
