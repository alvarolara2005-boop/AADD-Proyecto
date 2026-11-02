package test;

import fuentes.FuentePuntosInteres;
import fuentes.FuentePuntosInteresGeoNames;
import modelo.*;
import repositorio.*;
import servicio.*;

import java.util.List;

public class Programa {

	private static final String GEONAMES_USER = "alvaro.laral";

	public static void main(String[] args) {
		System.out.println("=== PRUEBAS TRICLINIUM ===\n");

		// -------- Inyección de dependencias (AdHoc por defecto) --------
		//RepositorioRestaurantes rr = new RepositorioRestaurantesAdHoc();
		//RepositorioPlatos rp = new RepositorioPlatosAdHoc();
		FuentePuntosInteres fuente = new FuentePuntosInteresGeoNames(GEONAMES_USER);
		//ServicioRestaurantes servicio = new ServicioRestaurantesImpl(rr, rp, fuente);

		//  JPA, si lo queremos lo descmentamos:
		RepositorioRestaurantes rr = new RepositorioRestaurantesJPA();
		RepositorioPlatos rp = new RepositorioPlatosJPA();
		ServicioRestaurantes servicio = new ServicioRestaurantesImpl(rr, rp, fuente);

		try {
			// 1. Alta restaurante
			System.out.println("[1] Alta de restaurante...");
			String idR1 = servicio.altaRestaurante("La Bella Napoli", "Mario Rossi", "968111111",
					"Calle Mayor 10, Murcia", -1.129, 37.984, "Auténtica cocina italiana", TipoCocina.ITALIANA,
					"13:00-16:00, 20:00-23:30");
			System.out.println("   OK -> " + idR1);

			String idR2 = servicio.altaRestaurante("Sushi Tokyo", "Yuki Tanaka", "968222222", "Gran Vía 5, Murcia",
					-1.130, 37.986, "Sushi fresco", TipoCocina.JAPONESA, "12:00-23:00");
			System.out.println("   OK -> " + idR2);

			// 2. Asignar puntos de interés
			System.out.println("\n[2] Asignar puntos de interés (max=3)...");
			try {
				servicio.asignarPuntosInteres(idR1, 3);
				Restaurante r = servicio.recuperarRestaurante(idR1);
				System.out.println("   Puntos: " + r.getPuntosInteres().size());
				r.getPuntosInteres().forEach(p -> System.out.println("     " + p));
			} catch (Exception e) {
				System.out.println("   AVISO (fuente externa): " + e.getMessage());
			}

			// 3. Alta platos
			System.out.println("\n[3] Alta de platos...");
			String p1 = servicio.altaPlato(idR1, "Spaghetti Carbonara", "Pasta con huevo, panceta y parmesano", "Pasta",
					12.50);
			String p2 = servicio.altaPlato(idR1, "Pizza Margherita", "Tomate, mozzarella y albahaca", "Pizza", 9.90);
			String p3 = servicio.altaPlato(idR1, "Tiramisú", "Postre italiano clásico", "Postre", 5.50);
			System.out.println("   Platos: " + p1 + ", " + p2 + ", " + p3);

			// 4. Modificar plato
			System.out.println("\n[4] Modificar plato...");
			servicio.modificarPlato(idR1, p2, "Pizza Margherita XL", null, null, 11.50);
			System.out.println("   Precio actualizado");

			// 5. Excluir plato
			System.out.println("\n[5] Excluir plato...");
			servicio.modificarEstadoPlato(idR1, p3, false);
			System.out.println("   Tiramisú excluido");

			// 6. Recuperar excluidos
			System.out.println("\n[6] Recuperar excluidos...");
			List<Plato> exc = servicio.recuperarPlatosExcluidos(idR1);
			exc.forEach(p -> System.out.println("   - " + p.getNombre()));

			// 7. Modificar restaurante
			System.out.println("\n[7] Modificar restaurante (horario)...");
			servicio.modificarRestaurante(idR1, "12:00-24:00", null, null, null, null, null);
			System.out.println("   OK");

			// 8. Buscar restaurantes
			System.out.println("\n[8] Buscar por tipo 'italiana'...");
			servicio.buscarRestaurantes(null, "italiana", null, null).forEach(r -> System.out.println("   " + r));

			System.out.println("\n[8b] Buscar por nombre 'sushi'...");
			servicio.buscarRestaurantes("sushi", null, null, null).forEach(r -> System.out.println("   " + r));

			System.out.println("\n[8c] Buscar precio medio max 11€...");
			servicio.buscarRestaurantes(null, null, 11.0, null).forEach(r -> System.out.println("   " + r));

			// 9. Consultar carta con filtros
			System.out.println("\n[9] Carta completa...");
			servicio.consultarCarta(idR1, null, null, null).forEach(p -> System.out.println("   " + p));

			System.out.println("\n[9b] Carta filtro precio<=10...");
			servicio.consultarCarta(idR1, 10.0, null, null).forEach(p -> System.out.println("   " + p));

			System.out.println("\n[9c] Carta filtro categoría 'pasta'...");
			servicio.consultarCarta(idR1, null, "pasta", null).forEach(p -> System.out.println("   " + p));

			System.out.println("\n[9d] Carta filtro texto 'tomate'...");
			servicio.consultarCarta(idR1, null, null, "tomate").forEach(p -> System.out.println("   " + p));

			// 10. Casos de error
			System.out.println("\n[10] Casos de error...");

			try {
				servicio.recuperarRestaurante("id-inexistente");
			} catch (Exception e) {
				System.out.println("   OK error restaurante: " + e.getMessage());
			}

			try {
				servicio.asignarPuntosInteres(idR1, 0);
			} catch (Exception e) {
				System.out.println("   OK error max<=0: " + e.getMessage());
			}

			try {
				servicio.modificarPlato(idR1, "plato-inexistente", "x", null, null, null);
			} catch (Exception e) {
				System.out.println("   OK error plato: " + e.getMessage());
			}

		} catch (Exception e) {
			System.err.println("ERROR GLOBAL: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("\n=== FIN DE PRUEBAS ===");
	}
}