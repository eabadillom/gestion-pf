package mx.com.ferbo.util;

import java.util.List;

import mx.com.ferbo.util.funcional.ThrowingConsumer;
import mx.com.ferbo.util.funcional.ThrowingSupplier;
import mx.com.ferbo.util.funcional.ThrowingRunnable;

public interface BaseMGR {

        default public <T> ResultadoOperacion cargar(
                        List<T> listaDestino,
                        ThrowingSupplier<List<T>, InventarioException> proveedorDatos,
                        String titulo,
                        String mensaje) throws InventarioException {

                List<T> nuevaLista = proveedorDatos.get();

                listaDestino.clear();

                int total = 0;

                if (nuevaLista != null) {
                        listaDestino.addAll(nuevaLista);
                        total = nuevaLista.size();
                }

                return new ResultadoOperacion(
                                titulo,
                                mensaje,
                                total,
                                NivelMensaje.INFO);
        }

        default public <T extends Identificable<?>> ResultadoOperacion guardar(
                        T entidad,
                        ThrowingConsumer<T, InventarioException> accionGuardar,
                        String nombreEntidad) throws InventarioException {

                boolean esNuevo = (entidad.getId() == null);

                accionGuardar.accept(entidad);

                String titulo = esNuevo
                                ? "Guardar " + nombreEntidad
                                : "Actualizar " + nombreEntidad;

                String mensaje = esNuevo
                                ? "El " + nombreEntidad + " se guardó correctamente."
                                : "El " + nombreEntidad + " se actualizó correctamente.";

                return new ResultadoOperacion(
                                titulo,
                                mensaje,
                                1,
                                NivelMensaje.INFO);
        }

        default public <T> ResultadoOperacion cambiarEstado(
                        T entidad,
                        ThrowingConsumer<T, InventarioException> accionCambioEstado,
                        String nombreEntidad,
                        String accion) throws InventarioException {

                accionCambioEstado.accept(entidad);

                String titulo = accion + " " + nombreEntidad;

                String mensaje = "El " + nombreEntidad +
                                " se " + accion.toLowerCase() + " correctamente.";

                return new ResultadoOperacion(
                                titulo,
                                mensaje,
                                1,
                                NivelMensaje.INFO);
        }

        default public ResultadoOperacion ejecutarOperacion(
                        ThrowingRunnable<InventarioException> accion,
                        String titulo,
                        String mensaje) throws InventarioException {

                accion.run();

                return new ResultadoOperacion(
                                titulo,
                                mensaje,
                                1,
                                NivelMensaje.INFO);
        }
}