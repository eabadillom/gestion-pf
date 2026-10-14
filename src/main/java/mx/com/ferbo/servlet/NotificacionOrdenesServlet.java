package mx.com.ferbo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

import mx.com.ferbo.business.NotificacionBL;
import mx.com.ferbo.dto.NotificacionDTO;
import mx.com.ferbo.util.SistemaOrigen;
import mx.com.ferbo.util.StatusNotificacion;
import mx.com.ferbo.util.TipoNotificacion;
import mx.com.ferbo.websocket.NotificacionWS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet ("/notificacion-ordenes")
public class NotificacionOrdenesServlet extends HttpServlet {
    
    private static final Logger log = LogManager.getLogger(NotificacionOrdenesServlet.class);

    @Inject 
    NotificacionBL notificacionBL;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
            new JsonDeserializer<LocalDateTime>() {
        @Override
        public LocalDateTime deserialize(
                JsonElement json,
                Type type,
                JsonDeserializationContext context) {

            return LocalDateTime.parse(json.getAsString());
        }
    }).registerTypeAdapter(
                    LocalDateTime.class,
                    new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(
                        LocalDateTime fecha,
                        Type type,
                        JsonSerializationContext context) {

                    return new JsonPrimitive(fecha.toString());
                }
            }).create();

    @Override
    public void init() throws ServletException {

    }

    @Override 
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La notificación recibida no contiene el formato necesario");
            return;
        }

        NotificacionDTO notificacion = null;

        try (BufferedReader reader = request.getReader()) {
            notificacion = gson.fromJson(reader, NotificacionDTO.class);
        } catch(JsonIOException ex) {
            log.warn("Error al momento de convertir el json recibido: {}", ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No fue posible decodificar correctamente la notificación. Por favor, contacte con el administrador del sistema para recibir asistencia.");
            return;
        } catch (Exception ex) {
            log.error("Error al momento de obtener la solicitud del cliente: {}", ex.getMessage(), ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error interno. Intente nuevamente y, si el problema persiste, contacte con el administrador del sistema.");
            return;
        }

        if (notificacion == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "La notificicación recibida está vacía. Contacte con el administrador del sistema");
            return;
        }

        if (!SistemaOrigen.GESTION_CLIENTES.equals(notificacion.getSistemaOrigen())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "El sistema de origen no tiene autorización para notificar este recurso");
            return;
        }

        if (!TipoNotificacion.NUEVA_ORDEN_RETIRO.equals(notificacion.getTipo())) {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "El tipo de operación a notificar aún no está implementada");
            return;
        }

        if (StatusNotificacion.RECIVIDA.equals(notificacion.getStatus())) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "La notificación no puede cambiar de status por que se encuentra como recibida");
            return;
        }

        NotificacionWS.broadcast(gson.toJson(notificacion));
        
        NotificacionDTO acuseRecibido = notificacionBL.obtenerRespuesta(notificacion.getTipo());

        String acuseRecibidoString = gson.toJson(acuseRecibido);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(acuseRecibidoString);

    }

    @Override
    public void destroy() {

    }
}
