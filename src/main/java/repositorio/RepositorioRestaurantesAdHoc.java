package repositorio;

import modelo.Restaurante;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioRestaurantesAdHoc implements RepositorioRestaurantes {
    private Map<String, Restaurante> datos = new HashMap<>();

    @Override
    public String add(Restaurante r) throws RepositorioException {
        datos.put(r.getId(), r);
        return r.getId();
    }

    @Override
    public void update(Restaurante r) throws RepositorioException {
        if (!datos.containsKey(r.getId()))
            throw new RepositorioException("Restaurante no encontrado: " + r.getId());
        datos.put(r.getId(), r);
    }

    @Override
    public void delete(Restaurante r) throws RepositorioException {
        datos.remove(r.getId());
    }

    @Override
    public Restaurante getById(String id) throws RepositorioException {
        Restaurante r = datos.get(id);
        if (r == null) throw new RepositorioException("Restaurante no encontrado: " + id);
        return r;
    }

    @Override
    public List<Restaurante> getAll() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public List<Restaurante> getByNombre(String nombre) {
        String n = nombre.toLowerCase();
        return datos.values().stream()
            .filter(r -> r.getNombre().toLowerCase().contains(n))
            .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> getByTipoCocina(String tipo) {
        return datos.values().stream()
            .filter(r -> r.getTipoCocina().name().equalsIgnoreCase(tipo))
            .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> getByPrecioMedioMax(double precioMax) {
        return datos.values().stream()
            .filter(r -> {
                double pm = r.getPrecioMedio();
                return pm >= 0 && pm <= precioMax;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Restaurante> getByDireccion(String direccion) {
        String d = direccion.toLowerCase();
        return datos.values().stream()
            .filter(r -> r.getDireccion().toLowerCase().contains(d))
            .collect(Collectors.toList());
    }
}