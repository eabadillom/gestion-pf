package mx.com.ferbo.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author alberto
 */
public final class FacesUtils 
{
    public static void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }
    
    public static void requireNonNull(Object obj, String mensaje) throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
    }
    
    public static <T> T requireNonNullWithReturn(T obj, String mensaje) throws InventarioException {
        if (obj == null) {
            throw new InventarioException(mensaje);
        }
        return obj;
    }

    public static String normalizar(String valor) {
        return valor == null ? "" : valor.trim().toLowerCase();
    }
    
}
