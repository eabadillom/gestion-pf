package mx.com.ferbo.dao.n;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import mx.com.ferbo.commons.dao.BaseDAO;
import mx.com.ferbo.model.EstadoConstancia;

@Named
@ApplicationScoped
public class EstadoConstanciaDAO extends BaseDAO <EstadoConstancia, Integer>{

    public EstadoConstanciaDAO(){
        super(EstadoConstancia.class);
    }
    
}
