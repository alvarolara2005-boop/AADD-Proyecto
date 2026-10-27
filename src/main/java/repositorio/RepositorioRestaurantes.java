package repositorio;

import modelo.Restaurante;
import java.util.List;

public interface RepositorioRestaurantes {
	String add(Restaurante r) throws RepositorioException;

	void update(Restaurante r) throws RepositorioException;

	void delete(Restaurante r) throws RepositorioException;

	Restaurante getById(String id) throws RepositorioException;

	List<Restaurante> getAll() throws RepositorioException;

	// Búsquedas AdHoc
	List<Restaurante> getByNombre(String nombre) throws RepositorioException;

	List<Restaurante> getByTipoCocina(String tipo) throws RepositorioException;

	List<Restaurante> getByPrecioMedioMax(double precioMax) throws RepositorioException;

	List<Restaurante> getByDireccion(String direccion) throws RepositorioException;
}