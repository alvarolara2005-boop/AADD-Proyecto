package modelo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	@Column(length = 2000)
	private String descripcion;

	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_cocina")
	private TipoCocina tipoCocina;

	private String horario;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "puntos_interes", joinColumns = @JoinColumn(name = "restaurante_id"))
	private Set<PuntoInteres> puntosInteres = new HashSet<>();

	@OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Plato> carta = new HashSet<>();

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
		List<Plato> enCarta = carta.stream().filter(Plato::isEnCarta).collect(Collectors.toList());
		if (enCarta.isEmpty())
			return -1;
		return enCarta.stream().mapToDouble(Plato::getPrecio).average().orElse(-1);
	}

	// Getters/setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String n) {
		this.nombre = n;
	}

	public String getPropietario() {
		return propietario;
	}

	public void setPropietario(String p) {
		this.propietario = p;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String t) {
		this.telefono = t;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String d) {
		this.direccion = d;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double l) {
		this.longitud = l;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double l) {
		this.latitud = l;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String d) {
		this.descripcion = d;
	}

	public TipoCocina getTipoCocina() {
		return tipoCocina;
	}

	public void setTipoCocina(TipoCocina t) {
		this.tipoCocina = t;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String h) {
		this.horario = h;
	}

	public Set<PuntoInteres> getPuntosInteres() {
		return puntosInteres;
	}

	public void setPuntosInteres(Set<PuntoInteres> p) {
		this.puntosInteres = p;
	}

	public Set<Plato> getCarta() {
		return carta;
	}

	public void setCarta(Set<Plato> c) {
		this.carta = c;
	}

	@Override
	public String toString() {
		return "Restaurante[" + id + ", " + nombre + ", " + tipoCocina + ", " + direccion + "]";
	}
}