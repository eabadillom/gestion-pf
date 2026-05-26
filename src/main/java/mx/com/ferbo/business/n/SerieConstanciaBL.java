package mx.com.ferbo.business.n;

import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.SerieConstanciaDAO;
import mx.com.ferbo.model.Cliente;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.SerieConstancia;
import mx.com.ferbo.util.FacesUtils;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class SerieConstanciaBL 
{
    private static Logger log = LogManager.getLogger(SerieConstanciaBL.class);
    
    @Inject
    private SerieConstanciaDAO serieConstanciaDAO;
    
    public static final String TIPO_SERIE = "O";
    
    public SerieConstancia buscarSerieConstancia(SerieConstancia serieConstancia) {
        return serieConstanciaDAO.buscarPorClienteAndPlanta(serieConstancia);
    }
    
    public SerieConstancia buscarSeriePorParametros(Integer idCliente, String tipoSerie, Integer idPlanta) throws InventarioException {
        Optional<SerieConstancia> auxSerieConstancia = serieConstanciaDAO.buscarPorClienteTipoSerieAndPlanta(idCliente, tipoSerie, idPlanta);
        SerieConstancia serieConstancia = null;
        
        if(auxSerieConstancia.isPresent())
            serieConstancia = auxSerieConstancia.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return serieConstancia;
    }
    
    public SerieConstancia buscarSerie(Cliente cliente, Planta planta) throws InventarioException {
        return buscarSeriePorParametros(cliente.getCteCve(), TIPO_SERIE, planta.getPlantaCve());
    }
    
    public String generarFolioSalida(SerieConstancia serie, Cliente cliente, Planta planta) throws InventarioException {
        return String.format("%s%s%s%d", serie.getSerieConstanciaPK().getTpSerie(), planta.getPlantaSufijo(), cliente.getCodUnico(), serie.getNuSerie());
    }
    
    public void guardarSerieConstancia(SerieConstancia serieConstancia) throws InventarioException {
        FacesUtils.requireNonNull(serieConstancia, "Error al actualizar la serie constancia");
        
        serieConstancia.setNuSerie(serieConstancia.getNuSerie() + 1);
        serieConstanciaDAO.actualizar(serieConstancia);
    }
    
}
