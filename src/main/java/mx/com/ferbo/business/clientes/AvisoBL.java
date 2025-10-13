package mx.com.ferbo.business.clientes;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import mx.com.ferbo.dao.n.CategoriaDAO;
import mx.com.ferbo.dao.n.CuotaMinimaDAO;
import mx.com.ferbo.dao.n.PlantaDAO;
import mx.com.ferbo.dao.n.UdCobroDAO;
import mx.com.ferbo.model.Categoria;
import mx.com.ferbo.model.Planta;
import mx.com.ferbo.model.UdCobro;

@Named
@RequestScoped
public class AvisoBL {

    @Inject
    private CategoriaDAO categoriaDAO;

    @Inject
    private UdCobroDAO udCobroDAO;

    @Inject
    private PlantaDAO plantaDAO;

    @Inject
    private CuotaMinimaDAO cuotaMinimaDAO;

    public List<Categoria> obtenerCategorias(){
        List<Categoria> list = categoriaDAO.buscarTodos();

        if (list == null){
            return new ArrayList<>();
        }

        return list;
    }

    public List<UdCobro> obtenerUnidadesCobro(){
        List<UdCobro> list = udCobroDAO.buscarTodos();

        if (list == null){
            return new ArrayList<>();
        }

        return list;
    }

    public List<Planta> obtenerPlantas(){
        List<Planta> list = plantaDAO.buscarTodos();

        if (list == null) {
            return new ArrayList<>();
        }

        return list;
    }

}
