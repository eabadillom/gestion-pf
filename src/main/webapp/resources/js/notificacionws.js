(function () {
    "use strict";

    let socket = null;
    let reconnectTimer = null;
    let reconnectDelay = 1000;
    const maxReconnectDelay = 30000;
    
    function getContextPath() {
        return window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1));
    }

    function connect() {
        const protocol =
            window.location.protocol === "https:" ? "wss:" : "ws:";

        const url =
            protocol + "//" + window.location.host + getContextPath() + "/notificacion-componentes";

        socket = new WebSocket(url);

        socket.onopen = function () {
            console.info("Tunel de comunicación conectado");
            reconnectDelay = 1000;
        };

        socket.onmessage = function (event) {
            console.log("Notificiación recibida.");

            let mensaje;

            try {
                mensaje = JSON.parse(event.data);
            } catch (error) {
                console.error("Notificación no válida:", error);
                return;
            }

            document.dispatchEvent(
                new CustomEvent("websocket-message", {
                    detail: mensaje
                })
            );
        };

        socket.onerror = function (error) {
            console.error("Error en el tunel de comunicación:", error);
        };

        socket.onclose = function () {
            console.warn("Tunel de comunicación desconectado");

            clearTimeout(reconnectTimer);

            reconnectTimer = setTimeout(function () {
                reconnectDelay = Math.min(
                    reconnectDelay * 2,
                    maxReconnectDelay
                );

                connect();
            }, reconnectDelay);
        };
    }

    window.WebSocketClient = {
        connect: connect
    };

    connect();
})();

document.addEventListener("websocket-message", function (event) {
    const mensaje = event.detail;

    if (mensaje.tipo === "NUEVA_ORDEN_RETIRO") {
         if (typeof actualizarNotificaciones === "function") {
            actualizarNotificaciones();
        }
    }
});
