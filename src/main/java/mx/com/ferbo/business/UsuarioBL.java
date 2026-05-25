package mx.com.ferbo.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mx.com.ferbo.dao.PlantaDAO;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.Usuario;
import mx.com.ferbo.util.InventarioException;

public class UsuarioBL {
	
	public static synchronized List<Planta> buscarPlantasAutorizadas(Usuario usuario)
	throws InventarioException {
		List<Planta> plantas = null;
		Planta       planta = null;
		
		if(usuario == null)
			throw new InventarioException("Usuario no válido.");
		
		PlantaDAO plantaDAO = new PlantaDAO();
		
		switch(usuario.getPerfil()) {
		
		case 2: //Perfil de facturación
		case 3: //Perfil de administración
			plantas = plantaDAO.buscarTodos();
			break;
		case 1: //Auxiliar de almacen
		case 4: //Gerente de almacen
			plantas = new ArrayList<Planta>();
			
			if(usuario.getIdPlanta() != null) {
				planta = plantaDAO.buscarPorId(usuario.getIdPlanta());
				plantas.add(planta);
			}
			
			break;
		default:
			throw new InventarioException("Perfil no autorizado.");
		}
		
		return plantas;
	}
	
	public static synchronized Optional<Planta> buscarPlantaAsignada(Usuario usuario) {
		Optional<Planta> optional = null;
		Planta planta = null;
		PlantaDAO plantaDAO = null;
		
		try {
			if(usuario.getIdPlanta() == null)
				throw new InventarioException("El usuario no tiene una planta asignada.");
			
			plantaDAO = new PlantaDAO();
			planta = plantaDAO.buscarPorId(usuario.getIdPlanta());
			
			if(planta == null)
				throw new InventarioException("La planta asociada al empleado es incorrecta.");
			
			optional = Optional.of(planta);
		} catch(Exception ex) {
			optional = Optional.empty();
		}
		
		return optional;
	}
}
