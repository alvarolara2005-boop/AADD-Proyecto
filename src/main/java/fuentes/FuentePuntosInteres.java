package fuentes;

import modelo.PuntoInteres;
import java.util.List;

public interface FuentePuntosInteres {
	/**
	 * Devuelve los puntos de interés cercanos a las coordenadas indicadas,
	 * ordenados de menor a mayor distancia.
	 *
	 * @param lat latitud
	 * @param lng longitud
	 * @param max número máximo de puntos a devolver (debe ser > 0)
	 */
	List<PuntoInteres> getPuntosCercanos(double lat, double lng, int max) throws Exception;
}