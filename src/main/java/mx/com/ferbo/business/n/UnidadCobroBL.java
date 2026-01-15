package mx.com.ferbo.business.n;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dao.n.UdCobroDAO;
import mx.com.ferbo.model.UdCobro;

public class UnidadCobroBL {

    private static final Logger log = LogManager.getLogger(UnidadCobroBL.class);

    @Inject
    private UdCobroDAO udCobroDAO;

    public List<UdCobro> obtenerUnidadesCobro() {
        log.info("Inicia proceso para obtener todas las unidades de cobro");
        List<UdCobro> list = udCobroDAO.buscarTodos();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

}
