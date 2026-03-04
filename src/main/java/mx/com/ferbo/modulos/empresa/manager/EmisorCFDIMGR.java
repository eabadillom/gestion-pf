package mx.com.ferbo.modulos.empresa.manager;

import java.util.List;

import javax.inject.Inject;

import mx.com.ferbo.modulos.empresa.bussines.EmisorCFDIBL;
import mx.com.ferbo.modulos.empresa.model.EmisorCFDI;
import mx.com.ferbo.util.InventarioException;

public class EmisorCFDIMGR {

    @Inject
    private EmisorCFDIBL nEmisoresCFDISBL;

    public String[] cargarEmisoresCFDIs(List<EmisorCFDI> lst) throws InventarioException {
        List<EmisorCFDI> nuevaLista = nEmisoresCFDISBL.obtenerTodos();
        lst.clear();
        lst.addAll(nuevaLista);
        
        String mensaje = "Se han cagado exitosamente los emisores de CFDI de la empresa.";
        String titulo = "Cargar emisores de CFDIs";

        return new String[]{titulo, mensaje};
    }
}
