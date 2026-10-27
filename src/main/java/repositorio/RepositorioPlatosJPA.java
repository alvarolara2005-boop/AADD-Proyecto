package repositorio;

import modelo.Plato;

import javax.persistence.*;
import java.util.List;

public class RepositorioPlatosJPA implements RepositorioPlatos {

	private EntityManagerFactory emf;

	public RepositorioPlatosJPA() {
		this.emf = Persistence.createEntityManagerFactory("tricliniumPU");
	}

	private EntityManager getEM() {
		return emf.createEntityManager();
	}

	@Override
	public String add(Plato p) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			em.persist(p);
			em.getTransaction().commit();
			return p.getId();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al añadir plato", e);
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Plato p) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			em.merge(p);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al actualizar plato", e);
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(Plato p) throws RepositorioException {
		EntityManager em = getEM();
		try {
			em.getTransaction().begin();
			Plato managed = em.find(Plato.class, p.getId());
			if (managed != null)
				em.remove(managed);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			throw new RepositorioException("Error al eliminar plato", e);
		} finally {
			em.close();
		}
	}

	@Override
	public Plato getById(String id) throws RepositorioException {
		EntityManager em = getEM();
		try {
			Plato p = em.find(Plato.class, id);
			if (p == null)
				throw new RepositorioException("Plato no encontrado: " + id);
			return p;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Plato> getByRestaurante(String restauranteId) throws RepositorioException {
		EntityManager em = getEM();
		try {
			return em.createQuery("SELECT p FROM Plato p WHERE p.restauranteId = :rid", Plato.class)
					.setParameter("rid", restauranteId).getResultList();
		} catch (Exception e) {
			// Si no existe la columna, devolvemos vacío
			return java.util.Collections.emptyList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Plato> getExcluidosByRestaurante(String restauranteId) throws RepositorioException {
		EntityManager em = getEM();
		try {
			return em.createQuery("SELECT p FROM Plato p WHERE p.restauranteId = :rid " + "AND p.enCarta = false",
					Plato.class).setParameter("rid", restauranteId).getResultList();
		} catch (Exception e) {
			return java.util.Collections.emptyList();
		} finally {
			em.close();
		}
	}

	public void close() {
		if (emf != null && emf.isOpen())
			emf.close();
	}
}