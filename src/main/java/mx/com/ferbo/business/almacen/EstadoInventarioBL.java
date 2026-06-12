package mx.com.ferbo.business.almacen;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.n.EstadoInventarioDAO;
import mx.com.ferbo.model.EstadoInventario;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class EstadoInventarioBL {
    
    @Inject
    private EstadoInventarioDAO estadoInventarioDAO;
    
    public EstadoInventario buscarPorId(Integer idEstadoInventario) throws InventarioException{
        return estadoInventarioDAO.buscarPorId(idEstadoInventario)
        		.orElseThrow(() -> new InventarioException("No se encontro registro con ese identificador"));
    }
    
    public List<EstadoInventario> buscarTodos() {
        return estadoInventarioDAO.buscarTodos();
    }
    
    public EstadoInventario actual()
    throws InventarioException {
    	return estadoInventarioDAO.buscarPorId(1)
    			.orElseThrow(() -> new InventarioException("Estado no encontrado"));
    }
    
    public EstadoInventario historico() throws InventarioException {
    	return estadoInventarioDAO.buscarPorId(2)
    			.orElseThrow(() -> new InventarioException("Estado no encontrado"));
    }
}
