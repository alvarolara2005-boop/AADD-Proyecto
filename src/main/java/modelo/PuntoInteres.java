package modelo;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PuntoInteres {
	private String nombre;
	@Column(length = 1000)
	private String descripcion;
	private double distancia;
	@Column(name = "url_wikipedia")
	private String urlWikipedia;

	public PuntoInteres() {
	}

	public PuntoInteres(String nombre, String descripcion, double distancia, String urlWikipedia) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.distancia = distancia;
		this.urlWikipedia = urlWikipedia;
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

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double d) {
		this.distancia = d;
	}

	public String getUrlWikipedia() {
		return urlWikipedia;
	}

	public void setUrlWikipedia(String u) {
		this.urlWikipedia = u;
	}

	@Override
	public String toString() {
		return nombre + " (" + distancia + " km)";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PuntoInteres))
			return false;
		PuntoInteres that = (PuntoInteres) o;
		return Objects.equals(nombre, that.nombre) && Objects.equals(urlWikipedia, that.urlWikipedia);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre, urlWikipedia);
	}
}