package mx.com.ferbo.business.catalogos;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.catalogos.CatConceptoEgresoDAO;
import mx.com.ferbo.model.n.catalogos.CatConceptoEgreso;
import mx.com.ferbo.model.n.catalogos.CategoriaEgreso;
import mx.com.ferbo.util.DAOException;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class CatConceptoEgresoBL extends BaseCatalogosBL<CatConceptoEgreso> {

    private static final Logger log = LogManager.getLogger(CatConceptoEgresoBL.class);

    @Inject
    private CatConceptoEgresoDAO dao;

    @PostConstruct
    public void init(){
        setDao(dao);
    }

    @Override
    protected void validarEspecifico(CatConceptoEgreso model) throws InventarioException {
        if ("".equalsIgnoreCase(model.getCodigoSAT())) {
            throw new InventarioException("El concepto no tiene un código de SAT asignado.");
        }

        if (model.getTieneIVA() == null){
            throw new InventarioException("No se puede determinar si el concepto incluye IVA o no.");
        }

        if (model.getTieneIEPS() == null) {
            throw new InventarioException("No se puede determinar si el concepto incluye IEPSE o no.");
        }
    }

    public List<CatConceptoEgreso> obtenerPorCategoria(CategoriaEgreso categoria) throws InventarioException{
        try {
            return dao.buscarPorCategoriaEgreso(categoria.getId());
        } catch (DAOException ex) {
            log.warn("Error al obtener conceptos asociados con la categoria {}. {}", categoria.getNombre(), ex);
            throw new InventarioException("Hubo un problema al obtener los conceptos asociados con la categoria " + categoria.getNombre());
        }
    }

    public List<CatConceptoEgreso> obtenerPorCategoriaYVigencia (CategoriaEgreso categoria, Boolean vigencia) throws InventarioException{
        try {
            return dao.buscarPorCategoriaEgreso(categoria.getId());
        } catch (DAOException ex) {
            String estado = vigencia ? "vigentes" : "no vigentes";
            log.warn("Error al obtener conceptos asociados con la categoria {} y {}. {}", categoria.getNombre(), estado, ex);
            throw new InventarioException("Hubo un problema al obtener los conceptos asociados con la categoria " + categoria.getNombre() + " y " + estado);
        }
    }
}
