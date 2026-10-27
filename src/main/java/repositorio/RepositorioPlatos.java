package repositorio;

import modelo.Plato;
import java.util.List;

public interface RepositorioPlatos {
	String add(Plato p) throws RepositorioException;

	void update(Plato p) throws RepositorioException;

	void delete(Plato p) throws RepositorioException;

	Plato getById(String id) throws RepositorioException;

	List<Plato> getByRestaurante(String restauranteId) throws RepositorioException;

	List<Plato> getExcluidosByRestaurante(String restauranteId) throws RepositorioException;
}