package modelo;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "platos")
public class Plato {
	@Id
	private String id;
	private String nombre;
	private String descripcion;
	private double precio;
	private String categoria;
	@Column(name = "en_carta")
	private boolean enCarta;

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

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public boolean isEnCarta() {
		return enCarta;
	}

	public void setEnCarta(boolean enCarta) {
		this.enCarta = enCarta;
	}

}
