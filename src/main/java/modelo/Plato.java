package modelo;

import javax.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "platos")
public class Plato {
	@Id
	private String id;
	private String nombre;
	@Column(length = 1000)
	private String descripcion;
	private double precio;
	private String categoria;
	@Column(name = "en_carta")
	private boolean enCarta;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;

	public Plato() {
		this.id = UUID.randomUUID().toString();
		this.enCarta = true;
	}

	public Plato(String nombre, String descripcion, double precio, String categoria) {
		this();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.categoria = categoria;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String d) {
		this.descripcion = d;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double p) {
		this.precio = p;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String c) {
		this.categoria = c;
	}

	public boolean isEnCarta() {
		return enCarta;
	}

	public void setEnCarta(boolean e) {
		this.enCarta = e;
	}

	public Restaurante getRestaurante() {
		return restaurante;
	}

	public void setRestaurante(Restaurante r) {
		this.restaurante = r;
	}

	@Override
	public String toString() {
		return "Plato[" + id + ", " + nombre + ", " + precio + "€, " + categoria + ", enCarta=" + enCarta + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Plato))
			return false;
		Plato that = (Plato) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}