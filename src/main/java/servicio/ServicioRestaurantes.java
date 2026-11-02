package servicio;

import java.util.List;

import modelo.Plato;
import modelo.Restaurante;
import modelo.TipoCocina;

public interface ServicioRestaurantes {

	String altaRestaurante(String nombre, String propietario, String telefono, String direccion, double longitud,
			double latitud, String descripcion, TipoCocina tipoCocina, String horario) throws Exception;

	void asignarPuntosInteres(String restauranteId, int max) throws Exception;

	void modificarRestaurante(String id, String horario, String telefono, String descripcion, String direccion,
			Double longitud, Double latitud) throws Exception;

	Restaurante recuperarRestaurante(String id) throws Exception;


	String altaPlato(String restauranteId, String nombre, String descripcion, String categoria, double precio)
			throws Exception;

	void modificarPlato(String restauranteId, String platoId, String nombre, String descripcion, String categoria,
			Double precio) throws Exception;

	void modificarEstadoPlato(String restauranteId, String platoId, boolean enCarta) throws Exception;

	List<Plato> recuperarPlatosExcluidos(String restauranteId) throws Exception;


	List<RestauranteResumen> buscarRestaurantes(String nombre, String tipoCocina, Double precioMax, String direccion)
			throws Exception;

	List<Plato> consultarCarta(String restauranteId, Double precioMax, String categoria, String texto) throws Exception;
}
