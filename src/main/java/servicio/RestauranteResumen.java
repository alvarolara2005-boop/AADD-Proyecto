package servicio;

import modelo.TipoCocina;

public class RestauranteResumen {
	private String id;
	private String nombre;
	private TipoCocina tipoCocina;
	private String direccion;
	private double precioMedio;

	public RestauranteResumen(String id, String nombre, TipoCocina tipoCocina, String direccion, double precioMedio) {
		this.id = id;
		this.nombre = nombre;
		this.tipoCocina = tipoCocina;
		this.direccion = direccion;
		this.precioMedio = precioMedio;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public TipoCocina getTipoCocina() {
		return tipoCocina;
	}

	public String getDireccion() {
		return direccion;
	}

	public double getPrecioMedio() {
		return precioMedio;
	}

	@Override
	public String toString() {
		return nombre + " | " + tipoCocina + " | " + direccion + " | precio medio: "
				+ (precioMedio < 0 ? "N/A" : precioMedio + "€");
	}
}