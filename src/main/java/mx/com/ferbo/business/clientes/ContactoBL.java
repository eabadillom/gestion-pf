package mx.com.ferbo.business.clientes;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.n.ContactoDAO;
import mx.com.ferbo.model.Contacto;
import mx.com.ferbo.util.InventarioException;

@Named
@RequestScoped
public class ContactoBL {
	
	@Inject
	private ContactoDAO contactoDAO;
	
	public List<Contacto> buscar(String query)
	throws InventarioException {
		
		if(query == null || "".equalsIgnoreCase(query))
			throw new InventarioException("Debe indicar un término de búsqueda.");
		
		if(query.length() <= 4)
			throw new InventarioException("El término de búsqueda debe tener al menos 4 caracteres.");

		//TODO Falta agregar excepción de símbolos especiales.
		//...
		
		return this.contactoDAO.buscar(query);
	}
}
