package mx.com.ferbo.business.n;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.com.ferbo.dao.n.ProductoDAO;
import mx.com.ferbo.model.Producto;
import mx.com.ferbo.util.InventarioException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author alberto
 */
@Named
@RequestScoped
public class ProductoBL 
{
    private static final Logger log = LogManager.getLogger(ProductoBL.class);
    
    @Inject
    private ProductoDAO productoDAO;
    
    public Producto buscarProductoPorId(Integer idProducto) throws InventarioException
    {
        if(idProducto == null)
            throw new InventarioException("El producto no puede ser vacía");
        
        Optional<Producto> producto = productoDAO.buscarPorId(idProducto);
        Producto auxProducto = null;
        
        if(producto.isPresent())
            auxProducto = producto.get();
        else
            throw new InventarioException("No se encontro registro con ese identificador");
        
        return auxProducto;
    }
    
    public List<Producto> buscarPorCliente(Integer idCliente) throws InventarioException
    {
        if(idCliente == null)
            throw new InventarioException("El producto no puede ser vacía");
        
        return productoDAO.buscarPorCliente(idCliente);
    }
    
}
