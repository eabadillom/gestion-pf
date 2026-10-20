
package mx.com.ferbo.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ServerEndpoint("/notificacion-componentes")
public class NotificacionWS {
    
    private static final Logger log = LogManager.getLogger(NotificacionWS.class);
    
    private static final Set<Session> sessions =
            Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        log.info("Cliente WebSocket conectado: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        log.info("Cliente WebSocket desconectado: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
        log.warn("Error en WebSocket: " + throwable.getMessage());
    }

    /**
     * Llamado desde el Servlet para enviar el mensaje a todos los navegadores conectados.
     */
    public static void broadcast(String mensaje) {
        synchronized (sessions) {
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getAsyncRemote().sendText(mensaje);
                }
            }
        }
    }
    
}
