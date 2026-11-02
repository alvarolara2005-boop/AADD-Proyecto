package servicio;

import fuentes.FuentePuntosInteres;
import modelo.*;
import repositorio.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServicioRestaurantesImpl implements ServicioRestaurantes {

	private final RepositorioRestaurantes repoRestaurantes;
	private final RepositorioPlatos repoPlatos;
	private final FuentePuntosInteres fuentePuntos;

	public ServicioRestaurantesImpl(RepositorioRestaurantes rr, RepositorioPlatos rp, FuentePuntosInteres fp) {
		this.repoRestaurantes = rr;
		this.repoPlatos = rp;
		this.fuentePuntos = fp;
	}

	@Override
	public String altaRestaurante(String nombre, String propietario, String telefono, String direccion, double longitud,
			double latitud, String descripcion, TipoCocina tipoCocina, String horario) throws Exception {
		Restaurante r = new Restaurante(nombre, propietario, telefono, direccion, longitud, latitud, descripcion,
				tipoCocina, horario);
		return repoRestaurantes.add(r);
	}

	@Override
	public void asignarPuntosInteres(String restauranteId, int max) throws Exception {
		if (max <= 0)
			throw new IllegalArgumentException("max debe ser > 0");
		Restaurante r = repoRestaurantes.getById(restauranteId);
		List<PuntoInteres> puntos = fuentePuntos.getPuntosCercanos(r.getLatitud(), r.getLongitud(), max);

		if (puntos.size() > max)
			puntos = puntos.subList(0, max);
		r.setPuntosInteres(new HashSet<>(puntos));
		repoRestaurantes.update(r);
	}

	@Override
	public void modificarRestaurante(String id, String horario, String telefono, String descripcion, String direccion,
			Double longitud, Double latitud) throws Exception {
		Restaurante r = repoRestaurantes.getById(id);

		if (horario != null)
			r.setHorario(horario);
		if (telefono != null)
			r.setTelefono(telefono);
		if (descripcion != null)
			r.setDescripcion(descripcion);
		if (direccion != null)
			r.setDireccion(direccion);

		if (longitud != null && latitud != null) {
			r.setLongitud(longitud);
			r.setLatitud(latitud);
			// Asignamos nueva lista vacía en vez de hacer clear()
			r.setPuntosInteres(new HashSet<PuntoInteres>());
		}
		repoRestaurantes.update(r);
	}

	@Override
	public Restaurante recuperarRestaurante(String id) throws Exception {
		return repoRestaurantes.getById(id);
	}

	@Override
	public String altaPlato(String restauranteId, String nombre, String descripcion, String categoria, double precio)
			throws Exception {
		Restaurante r = repoRestaurantes.getById(restauranteId);
		Plato p = new Plato(nombre, descripcion, precio, categoria);
		p.setRestaurante(r);
		r.getCarta().add(p);
		repoRestaurantes.update(r);
		return p.getId();
	}

	@Override
	public void modificarPlato(String restauranteId, String platoId, String nombre, String descripcion,
			String categoria, Double precio) throws Exception {
		Restaurante r = repoRestaurantes.getById(restauranteId);
		Plato p = r.getCarta().stream().filter(x -> x.getId().equals(platoId)).findFirst()
				.orElseThrow(() -> new RepositorioException("Plato no encontrado: " + platoId));

		if (nombre != null)
			p.setNombre(nombre);
		if (descripcion != null)
			p.setDescripcion(descripcion);
		if (categoria != null)
			p.setCategoria(categoria);
		if (precio != null)
			p.setPrecio(precio);

		repoRestaurantes.update(r);
		// NO llamamos a repoPlatos.update(p): el plato se persiste vía restaurante
	}

	@Override
	public void modificarEstadoPlato(String restauranteId, String platoId, boolean enCarta) throws Exception {
		Restaurante r = repoRestaurantes.getById(restauranteId);
		Plato p = r.getCarta().stream().filter(x -> x.getId().equals(platoId)).findFirst()
				.orElseThrow(() -> new RepositorioException("Plato no encontrado: " + platoId));
		p.setEnCarta(enCarta);
		repoRestaurantes.update(r);
	}

	@Override
	public List<Plato> recuperarPlatosExcluidos(String restauranteId) throws Exception {
		Restaurante r = repoRestaurantes.getById(restauranteId);
		return r.getCarta().stream().filter(p -> !p.isEnCarta()).collect(Collectors.toList());
	}

	@Override
	public List<RestauranteResumen> buscarRestaurantes(String nombre, String tipoCocina, Double precioMax,
			String direccion) throws Exception {
		List<Restaurante> todos = repoRestaurantes.getAll();
		return todos.stream().filter(r -> nombre == null || r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
				.filter(r -> tipoCocina == null || r.getTipoCocina().name().equalsIgnoreCase(tipoCocina))
				.filter(r -> direccion == null || r.getDireccion().toLowerCase().contains(direccion.toLowerCase()))
				.filter(r -> {
					if (precioMax == null)
						return true;
					double pm = r.getPrecioMedio();
					return pm >= 0 && pm <= precioMax;
				}).map(r -> new RestauranteResumen(r.getId(), r.getNombre(), r.getTipoCocina(), r.getDireccion(),
						r.getPrecioMedio()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Plato> consultarCarta(String restauranteId, Double precioMax, String categoria, String texto)
			throws Exception {
		Restaurante r = repoRestaurantes.getById(restauranteId);
		return r.getCarta().stream().filter(Plato::isEnCarta)
				.filter(p -> precioMax == null || p.getPrecio() <= precioMax)
				.filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
				.filter(p -> texto == null || p.getNombre().toLowerCase().contains(texto.toLowerCase())
						|| p.getDescripcion().toLowerCase().contains(texto.toLowerCase()))
				.collect(Collectors.toList());
	}
}