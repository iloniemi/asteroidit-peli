package laskentaa;

import javafx.geometry.Point2D;

public class Laskuri2DAvaruuteen {
	/** Muuntaa kulman avulla janan komponenttivektoreiksi. Koska on tama on tarkoittu 
	 * tietokonegrafiikkaan, annetaan kaanteisarvo pystysuoran akselin suuntaisesta
	 *  komponentista. 
	 * @param kulma asteina.
	 * @param jana janan pituus.
	 * @return Komponenttivektorit.
	 */
	public static Point2D kulmaJaJanaVektoreiksi(double kulma, double jana) {
		double x = Math.sin(Math.toRadians(kulma))*jana;
		double y = -Math.cos(Math.toRadians(kulma))*jana;
		return new Point2D(x, y);
	}
	
	
	/** Laskee koordinaatit pisteelle ympyran kehalta.
	 * @param r sade.
	 * @param kulma asteina.
	 * @return pisteen koordinaatit.
	 */
	public static Point2D pisteYmpyranKehalta(double r, double kulma) {
		double kulmaRad = Math.toRadians(kulma);
		double y = Math.sin(kulmaRad) * r;
		double x = Math.cos(kulmaRad) * r;
		return new Point2D(x, y);
	}
	
	/** Palauttaa vektorista toisen vektorin kanssa yhdensuuntaisen komponettivektorin.
	 * @param projektoitava vektori, jonka komponentti palautetaan.
	 * @param kohde vektori, jonka kanssa komponenttivektori on yhdensuuntainen.
	 * @return komponenttivektori.
	 */
	public static Point2D vektorinProjektio(Point2D projektoitava, Point2D kohde) {
		Point2D projektio = kohde.multiply(projektoitava.dotProduct(kohde)/kohde.dotProduct(kohde));
		double kulma = projektoitava.angle(kohde);
		if (45.0 < kulma && kulma < 135) projektio.multiply(-1);
		return projektio;
	}

}
