package web;

import modelo.Restaurante;
import modelo.Plato;
import modelo.TipoCocina;
import servicio.RestauranteResumen;
import servicio.ServicioRestaurantes;
import servicio.ServicioRestaurantesImpl;
import repositorio.*;
import fuentes.FuentePuntosInteresGeoNames;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@ManagedBean
@ViewScoped
public class RestauranteWebBean implements Serializable {

	// Inyección del Bean de Sesión
	@ManagedProperty(value = "#{sesionBean}")
	private SesionBean sesionBean;

	// Instanciamos el servicio usando JPA (Ejercicio 1)
	private ServicioRestaurantes servicio;

	// Propiedades para las vistas
	private List<Restaurante> misRestaurantes;
	private Restaurante nuevoRestaurante = new Restaurante();

	private String restauranteSeleccionadoId;
	private Plato nuevoPlato = new Plato();

	// Variables para buscadores
	private String busquedaTexto;
	private TipoCocina busquedaTipoCocina;
	private Double busquedaPrecioMax;
	private List<RestauranteResumen> resultadosBusqueda;

	// Variables para buscador de platos
	private List<Plato> cartaRestaurante;
	private String filtroTextoPlato;
	private String filtroCategoriaPlato;
	private Double filtroPrecioMaxPlato;
	private List<Plato> resultadosPlatos;

	@PostConstruct
	public void init() {
		// Inicializamos el servicio con las implementaciones JPA del Ejercicio 1
		RepositorioRestaurantes rr = new RepositorioRestaurantesJPA();
		RepositorioPlatos rp = new RepositorioPlatosJPA();
		// Usamos una fuente dummy si no queremos saturar GeoNames en la web,
		// o la real si tienes la cuenta activa.
		servicio = new ServicioRestaurantesImpl(rr, new FuentePuntosInteresGeoNames("alvaro.laral"));
	}

	// --- MÉTODOS DEL PROPIETARIO ---

	public void cargarMisRestaurantes() {
		try {
			misRestaurantes = servicio.getRestaurantesPropietario(sesionBean.getUsuario());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String crearRestaurante() {
		try {
			nuevoRestaurante.setPropietario(sesionBean.getUsuario());
			servicio.altaRestaurante(nuevoRestaurante.getNombre(), nuevoRestaurante.getPropietario(),
					nuevoRestaurante.getTelefono(), nuevoRestaurante.getDireccion(), nuevoRestaurante.getLongitud(),
					nuevoRestaurante.getLatitud(), nuevoRestaurante.getDescripcion(), nuevoRestaurante.getTipoCocina(),
					nuevoRestaurante.getHorario());
			nuevoRestaurante = new Restaurante(); // Limpiar formulario
			cargarMisRestaurantes();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Restaurante creado"));
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			return null;
		}
	}

	public String añadirPlato() {
		try {
			servicio.altaPlato(restauranteSeleccionadoId, nuevoPlato.getNombre(), nuevoPlato.getDescripcion(),
					nuevoPlato.getCategoria(), nuevoPlato.getPrecio());
			nuevoPlato = new Plato(); // Limpiar
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Plato añadido"));
			return null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
			return null;
		}
	}

	// --- MÉTODOS DEL CLIENTE ---

	public void buscarRestaurantes() {
		try {
			resultadosBusqueda = servicio.buscarRestaurantes(busquedaTexto,
					busquedaTipoCocina != null ? busquedaTipoCocina.name() : null, busquedaPrecioMax, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpiarBusquedaPlatos() {
		this.filtroTextoPlato = null;
		this.filtroCategoriaPlato = null;
		this.filtroPrecioMaxPlato = null;
		this.resultadosPlatos = null;
	}

	public void buscarPlatos() {
		if (restauranteSeleccionadoId == null || restauranteSeleccionadoId.trim().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debes seleccionar un restaurante"));
			return;
		}

		try {
			resultadosPlatos = servicio.consultarCarta(restauranteSeleccionadoId, filtroPrecioMaxPlato,
					filtroCategoriaPlato, filtroTextoPlato);
			System.out.println("DEBUG: Platos encontrados = " + resultadosPlatos.size());

			if (resultadosPlatos.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso",
						"No se encontraron platos para este restaurante con esos filtros."));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
		}
	}

	// Array de tipos de cocina para los combos
	public TipoCocina[] getTiposCocina() {
		return TipoCocina.values();
	}

	// Getters y Setters (Generados)
	public void setSesionBean(SesionBean sesionBean) {
		this.sesionBean = sesionBean;
	}

	public List<Restaurante> getMisRestaurantes() {
		return misRestaurantes;
	}

	public Restaurante getNuevoRestaurante() {
		return nuevoRestaurante;
	}

	public void setNuevoRestaurante(Restaurante r) {
		this.nuevoRestaurante = r;
	}

	public String getRestauranteSeleccionadoId() {
		return restauranteSeleccionadoId;
	}

	public void setRestauranteSeleccionadoId(String id) {
		this.restauranteSeleccionadoId = id;
	}

	public Plato getNuevoPlato() {
		return nuevoPlato;
	}

	public void setNuevoPlato(Plato p) {
		this.nuevoPlato = p;
	}

	public String getBusquedaTexto() {
		return busquedaTexto;
	}

	public void setBusquedaTexto(String b) {
		this.busquedaTexto = b;
	}

	public TipoCocina getBusquedaTipoCocina() {
		return busquedaTipoCocina;
	}

	public void setBusquedaTipoCocina(TipoCocina t) {
		this.busquedaTipoCocina = t;
	}

	public Double getBusquedaPrecioMax() {
		return busquedaPrecioMax;
	}

	public void setBusquedaPrecioMax(Double p) {
		this.busquedaPrecioMax = p;
	}

	public List<RestauranteResumen> getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	public List<Plato> getResultadosPlatos() {
		return resultadosPlatos;
	}

	public void setResultadosPlatos(List<Plato> r) {
		this.resultadosPlatos = r;
	}

	public String getFiltroTextoPlato() {
		return filtroTextoPlato;
	}

	public void setFiltroTextoPlato(String f) {
		this.filtroTextoPlato = f;
	}

	public String getFiltroCategoriaPlato() {
		return filtroCategoriaPlato;
	}

	public void setFiltroCategoriaPlato(String f) {
		this.filtroCategoriaPlato = f;
	}

	public Double getFiltroPrecioMaxPlato() {
		return filtroPrecioMaxPlato;
	}

	public void setFiltroPrecioMaxPlato(Double f) {
		this.filtroPrecioMaxPlato = f;
	}

	// Método para cargar todos los restaurantes en el desplegable
	public List<RestauranteResumen> getListaRestaurantesDisponibles() {
		try {
			// Llamamos al método de búsqueda sin filtros para que devuelva todos
			return servicio.buscarRestaurantes(null, null, null, null);
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}
}