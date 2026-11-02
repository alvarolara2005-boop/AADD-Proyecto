package fuentes;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import modelo.PuntoInteres;

public class FuentePuntosInteresGeoNames implements FuentePuntosInteres {

	private static final String BASE_URL = "http://api.geonames.org/findNearbyWikipedia";

	private final String usuario;

	public FuentePuntosInteresGeoNames(String usuario) {
		this.usuario = usuario;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<PuntoInteres> getPuntosCercanos(double lat, double lng, int max) throws Exception {

		if (max <= 0) {
			throw new IllegalArgumentException("El número máximo de puntos debe ser mayor que cero");
		}

		// Forzar Locale.US para que los decimales usen punto
		String urlStr = String.format(Locale.US, "%s?username=%s&lang=ES&lat=%f&lng=%f&maxRows=%d", BASE_URL,
				URLEncoder.encode(usuario, StandardCharsets.UTF_8.name()), lat, lng, max);

		System.out.println("   [GeoNames] Consultando: " + urlStr);

		URL url = new URL(urlStr);

		try (InputStream is = url.openStream()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			doc.getDocumentElement().normalize();

			// Comprobar error de GeoNames: value y message son ATRIBUTOS
			NodeList statusNodes = doc.getElementsByTagName("status");
			if (statusNodes.getLength() > 0) {
				Element status = (Element) statusNodes.item(0);
				String value = status.getAttribute("value");
				String message = status.getAttribute("message");
				throw new Exception("GeoNames error " + value + ": " + message);
			}

			List<PuntoInteres> resultado = new ArrayList<>();
			NodeList entries = doc.getElementsByTagName("entry");

			for (int i = 0; i < entries.getLength(); i++) {
				Element e = (Element) entries.item(i);

				String titulo = getText(e, "title");
				String resumen = getText(e, "summary");
				String distStr = getText(e, "distance");
				String wiki = getText(e, "wikipediaUrl");

				double distancia = 0.0;
				if (distStr != null && !distStr.isEmpty()) {
					try {
						distancia = Double.parseDouble(distStr);
					} catch (NumberFormatException ignored) {
					}
				}

				String urlWiki = (wiki == null || wiki.isEmpty()) ? "" : "https://" + wiki;

				if (resumen != null && resumen.length() > 900) {
					resumen = resumen.substring(0, 900) + "...";
				}

				resultado.add(new PuntoInteres(titulo, resumen, distancia, urlWiki));
			}

			resultado.sort(Comparator.comparingDouble(PuntoInteres::getDistancia));
			return resultado;
		}
	}

	private String getText(Element parent, String tag) {
		NodeList nl = parent.getElementsByTagName(tag);
		if (nl.getLength() == 0)
			return "";
		return nl.item(0).getTextContent();
	}
}