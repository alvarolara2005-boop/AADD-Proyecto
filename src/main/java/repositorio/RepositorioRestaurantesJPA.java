package repositorio;

import modelo.Restaurante;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioRestaurantesJPA implements RepositorioRestaurantes {

	private EntityManagerFactory emf;

	public RepositorioRestaurantesJPA() {
		this.emf = Persistence.createEntityManagerFactory("tricliniumPU");
	}

	private EntityManager getEM() {
		return emf.createEntityManager();
	}

	@Override
	public String add(Restaurante r) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			em.persist(r);
			em.getTransaction().commit();
			return r.getId();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al añadir restaurante", e);
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Restaurante r) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			em.merge(r);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al actualizar", e);
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(Restaurante r) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			Restaurante managed = em.find(Restaurante.class, r.getId());
			if (managed != null)
				em.remove(managed);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al eliminar", e);
		} finally {
			em.close();
		}
	}

	@Override
	public Restaurante getById(String id) throws RepositorioException {
		EntityManager em = getEM();
		try {
			Restaurante r = em.find(Restaurante.class, id);
			if (r == null)
				throw new RepositorioException("Restaurante no encontrado: " + id);
			// Forzar inicialización
			r.getPuntosInteres().size();
			r.getCarta().size();
			return r;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Restaurante> getAll() throws RepositorioException {
		EntityManager em = getEM();
		try {
			List<Restaurante> lista = em.createQuery("SELECT DISTINCT r FROM Restaurante r", Restaurante.class)
					.getResultList();
			lista.forEach(r -> {
				r.getPuntosInteres().size();
				r.getCarta().size();
			});
			return lista;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Restaurante> getByNombre(String nombre) throws RepositorioException {
		return getAll().stream().filter(r -> r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Restaurante> getByTipoCocina(String tipo) throws RepositorioException {
		return getAll().stream().filter(r -> r.getTipoCocina().name().equalsIgnoreCase(tipo))
				.collect(Collectors.toList());
	}

	@Override
	public List<Restaurante> getByPrecioMedioMax(double precioMax) throws RepositorioException {
		return getAll().stream().filter(r -> {
			double pm = r.getPrecioMedio();
			return pm >= 0 && pm <= precioMax;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Restaurante> getByDireccion(String direccion) throws RepositorioException {
		return getAll().stream().filter(r -> r.getDireccion().toLowerCase().contains(direccion.toLowerCase()))
				.collect(Collectors.toList());
	}

	public void close() {
		if (emf != null && emf.isOpen())
			emf.close();
	}
}