package repositorio;

import modelo.Plato;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioPlatosAdHoc implements RepositorioPlatos {
    private Map<String, Plato> datos = new HashMap<>();
    private Map<String, String> platoRestaurante = new HashMap<>();

    @Override
    public String add(Plato p) throws RepositorioException {
        datos.put(p.getId(), p);
        return p.getId();
    }

    public String add(Plato p, String restauranteId) throws RepositorioException {
        datos.put(p.getId(), p);
        platoRestaurante.put(p.getId(), restauranteId);
        return p.getId();
    }

    @Override
    public void update(Plato p) throws RepositorioException {
        if (!datos.containsKey(p.getId()))
            throw new RepositorioException("Plato no encontrado: " + p.getId());
        datos.put(p.getId(), p);
    }

    @Override
    public void delete(Plato p) throws RepositorioException {
        datos.remove(p.getId());
    }

    @Override
    public Plato getById(String id) throws RepositorioException {
        Plato p = datos.get(id);
        if (p == null) throw new RepositorioException("Plato no encontrado: " + id);
        return p;
    }

    @Override
    public List<Plato> getByRestaurante(String restauranteId) {
        return datos.values().stream()
            .filter(p -> restauranteId.equals(platoRestaurante.get(p.getId())))
            .collect(Collectors.toList());
    }

    @Override
    public List<Plato> getExcluidosByRestaurante(String restauranteId) {
        return getByRestaurante(restauranteId).stream()
            .filter(p -> !p.isEnCarta())
            .collect(Collectors.toList());
    }
}