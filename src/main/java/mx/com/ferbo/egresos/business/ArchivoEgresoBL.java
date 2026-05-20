package mx.com.ferbo.egresos.business;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;

import com.ferbo.tools.exception.SystemException;

import mx.com.ferbo.egresos.dao.ArchivoEgresoDAO;
import mx.com.ferbo.egresos.model.ArchivoEgreso;
import mx.com.ferbo.egresos.model.Egreso;
import mx.com.ferbo.model.Certificado;
import mx.com.ferbo.util.DataSourceManager;
import mx.com.ferbo.util.InventarioException;
import mx.com.ferbo.utils.IOUtil;

@Named
@RequestScoped
public class ArchivoEgresoBL {

    private static final Logger log = LogManager.getLogger(ArchivoEgresoBL.class);

    @Inject
    private ArchivoEgresoDAO dao;

    private String direccion;

    private String carpeta = "egresos";

    private String parametroEgresos = "gestion/egresos";

    public ArchivoEgreso obtenerPorEgreso(Egreso egreso) {
        try {
            return dao.buscarPorEgreso(egreso);
        } catch (SystemException ex) {
            log.warn("Error al obtener el achivo por egreso. {}", ex);
            throw ex;
        }
    }

    public void guardarOActualizarArchivo(ArchivoEgreso archivoEgreso) {

        String estado = "";

        Long id = archivoEgreso.getId();

        try {
            if (id == null) {
                estado = "guardar";
                dao.guardar(archivoEgreso);
            } else {
                estado = "actualizar";
                dao.actualizar(archivoEgreso);
            }
        } catch (InventarioException ex) {
            log.error("Error al momento de {} el archivo de egreso. {}", estado, ex);
            throw new SystemException("Hubo un problema al momento de " + estado + " el archivo de egreso.");
        }
    }

    public void crearArchivoEgresoSistema() {

    }

    public ArchivoEgreso crearRegistroArchivoEgreso(UploadedFile archivo, Egreso egreso) {

        if (archivo == null) {
            throw new ValidationException("El archivo no se subio correctamente.");
        }
        ArchivoEgreso archivoEgreso = new ArchivoEgreso();
        try {
            archivoEgreso.setNombre(archivo.getFileName());
            archivoEgreso.setTipoArchivo(archivo.getContentType());
            archivoEgreso.setEgreso(egreso);
            archivoEgreso.setTamanio(archivo.getSize());
            direccion = DataSourceManager.getJndiParameter(parametroEgresos);
            direccion = direccion.concat(File.separator);
            direccion = direccion.concat(carpeta);
            archivoEgreso.setUrl(direccion);
        } catch (IOException ex) {

        }
 
    }
    
}


    private File buscaImagen(File directorio, String imagen) {

        String imegencompleta = imagen + ".jpg";
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    File buscado = buscaImagen(archivo, imegencompleta);
                    if (buscado != null) {
                        return buscado;
                    }
                } else if (archivo.getName().equalsIgnoreCase(imegencompleta)) {
                    return archivo;
                }
            }
        }
        return null;
    }

    public void guardarImagen() {
        log.info("Entrando a guardarImagen...");

        byte[] contenidoimagen = null;
        String nombreimagen = this.prenda.getDescripcion();

        String ruta = this.getDireccion() + File.separator + "uniformes";
        File directorio = new File(ruta);
        File buscado = this.buscaImagen(directorio, nombreimagen);

        if (imagen != null) {

            if (directorio.exists() && directorio.isDirectory()) {

                if (buscado != null) {
                    buscado.delete();
                }

            } else {
                log.error("Problema al encontrar el directorio.");
                return;
            }

            try {
                log.info("Longitud del archivo: {}", this.imagen.getSize());
                contenidoimagen = this.imagen.getContent();
                contenidoimagen = IOUtil.read(this.imagen.getInputStream());
            } catch (IOException ex) {
                log.error("Hubo algun problema al momento de convertir la imagen a un arreglo de bytes", ex);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                pf.ajax().update("frm:message");
            }

            ruta = this.getDireccion() + File.separator + "uniformes" + File.separator + nombreimagen + ".jpg";

            try (FileOutputStream fos = new FileOutputStream(ruta)) {
                fos.write(contenidoimagen);
                fos.flush();
            } catch (IOException ex) {
                log.error("Hubo algun problema al momento de guardar la imagen en el servidor", ex);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                pf.ajax().update("frm:message");
            }
        } else if (imagen == null && buscado == null) {

            String sourcePath = this.getDireccion() + "sinimagen.jpg";
            String newFileName = "prendas/" + this.prenda.getDescripcion() + ".jpg";

            File sourceFile = new File(sourcePath);
            String directory = sourceFile.getParent();
            String destinationPath = directory + File.separator + newFileName;

            try {
                Path source = Paths.get(sourcePath);
                Path destination = Paths.get(destinationPath);
                Files.copy(source, destination);
            } catch (IOException ex) {

                log.error("Problema al guardar la imagen por defecto en el servidor", ex);
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: problema al momento de guardar la imagen", null));
                pf.ajax().update("frm:message");

            }
        }
        this.listar(true);
    }

    private void borrarImagen() throws IOException {
        try {
            String nombreimagen = this.prenda.getDescripcion();

            String ruta = this.getDireccion() + "prendas/";
            File directorio = new File(ruta);
            File buscado = this.buscaImagen(directorio, nombreimagen);

            if (directorio.exists() && directorio.isDirectory()) {

                if (buscado != null) {
                    buscado.delete();
                }

            } else {
                log.error("Problema al encontrar el directorio.");

            }
        } catch (Exception ex) {
            throw new IOException("Problema al borrar la imagen del articulo.");
        }
    }