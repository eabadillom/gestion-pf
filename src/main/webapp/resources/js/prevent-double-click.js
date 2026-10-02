/**
 * Prevención global de doble clic para botones de PrimeFaces
 */
$(document).ready(function() {
    // 1. Deshabilita el botón al iniciar cualquier petición AJAX
    $(document).on("pfAjaxSend", function(e, xhr, settings) {
        if (settings && settings.source) {
            // Se usa $.escapeSelector para evitar fallos con IDs de JSF que contienen dos puntos (ej: "miForm:btnGuardar")
            var buttonId = $.escapeSelector(settings.source);
            var $btn = $("#" + buttonId);

            if ($btn.hasClass("ui-button")) {
                $btn.addClass("ui-state-disabled").prop("disabled", true);
            }
        }
    });

    // 2. Vuelve a habilitar el botón al terminar la petición AJAX
    $(document).on("pfAjaxComplete", function(e, xhr, settings) {
        if (settings && settings.source) {
            var buttonId = $.escapeSelector(settings.source);
            var $btn = $("#" + buttonId);

            if ($btn.hasClass("ui-button")) {
                $btn.removeClass("ui-state-disabled").prop("disabled", false);
            }
        }
    });
});
