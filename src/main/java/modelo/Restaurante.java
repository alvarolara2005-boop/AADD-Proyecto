package modelo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "restaurantes")
public class Restaurante {

	@Id
	private String id;
	private String nombre;
	private String propietario;
	private String telefono;
	private String direccion;
	private double longitud;
	private double latitud;
	private String descripcion;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cocina")
	private TipoCocina tipoCocina;

	private String horario;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "puntos_interes", joinColumns = @JoinColumn(name = "restaurante_id"))
	private List<PuntoInteres> puntosInteres = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurante_id")
	private List<Plato> carta = new ArrayList<>();

	public Restaurante() {
		this.id = UUID.randomUUID().toString();
	}

	public Restaurante(String nombre, String propietario, String telefono, String direccion, double longitud,
			double latitud, String descripcion, TipoCocina tipoCocina, String horario) {
		this();
		this.nombre = nombre;
		this.propietario = propietario;
		this.telefono = telefono;
		this.direccion = direccion;
		this.longitud = longitud;
		this.latitud = latitud;
		this.descripcion = descripcion;
		this.tipoCocina = tipoCocina;
		this.horario = horario;
	}

	public double getPrecioMedio() {
		List<Plato> enCarta = carta.stream()
				.filter(Plato::isEnCarta)
				.collect(Collectors.toList());
		if (enCarta.isEmpty())
			return -1;
		return enCarta.stream().mapToDouble(Plato::getPrecio).average().orElse(-1);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String propietario) {
		this.propietario = propietario;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoCocina getTipoCocina() {
		return tipoCocina;
	}

	public void setTipoCocina(TipoCocina tipoCocina) {
		this.tipoCocina = tipoCocina;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public List<PuntoInteres> getPuntosInteres() {
		return puntosInteres;
	}

	public void setPuntosInteres(List<PuntoInteres> puntosInteres) {
		this.puntosInteres = puntosInteres;
	}

	public List<Plato> getCarta() {
		return carta;
	}

	public void setCarta(List<Plato> carta) {
		this.carta = carta;
	}

}
