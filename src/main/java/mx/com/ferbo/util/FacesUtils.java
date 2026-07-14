package mx.com.ferbo.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author alberto
 */
public final class FacesUtils {
    public static void addMessage(FacesMessage.Severity severity, String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, msg));
    }

    // TODO Se reubicara este metodo en paquete de validaciones
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

    public static StreamedContent toPDF(byte[] bytes, String fileName) {
        StreamedContent file;
        InputStream input = new ByteArrayInputStream(bytes);

        file = DefaultStreamedContent.builder()
                .contentType("application/pdf")
                .name(fileName)
                .stream(() -> input).build();

        return file;
    }
    
    public static StreamedContent toXLSX(byte[] bytes, String fileName) {
    	StreamedContent file;
        InputStream input = new ByteArrayInputStream(bytes);

        file = DefaultStreamedContent.builder()
                .contentType("application/vnd.ms-excel")
                .name(fileName)
                .stream(() -> input).build();

        return file;
    }

    @Deprecated
    public static StreamedContent crearStreamedContentDesdeBytes(
            byte[] bytes, String nombreArchivo, String extension) {

        extension = extension.trim().toLowerCase();

        if (!nombreArchivo.endsWith("." + extension)) {
            nombreArchivo += "." + extension;
        }

        return DefaultStreamedContent.builder()
                .contentType(extension) //Implementación incorrecta del atributo contentType
                .name(nombreArchivo)
                .stream(() -> new ByteArrayInputStream(bytes))
                .build();
    }
}
