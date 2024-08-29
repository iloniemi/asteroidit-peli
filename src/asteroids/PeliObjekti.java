package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import laskentaa.Laskuri2DAvaruuteen;

public abstract class PeliObjekti {
	private Point2D nopeus;
	private double massa;
	private boolean elossa = true;
	
	public PeliObjekti(Point2D nopeus, double massa) {
		super();
		this.nopeus = nopeus;
		this.massa = massa;
	}
	
	public double getMassa() {
		return massa;
	}

	abstract Shape getMuoto();
	

	public Point2D getNopeus() {
		return nopeus;
	}

	/** Liikuttaa peliobjektia annettujen rajojen sisalla.
	 * @param maxX Suurin sallitty x-koordinaatin arvo.
	 * @param maxY Suurin sallitty y-koordinaatin arvo.
	 */
	public void liiku(int maxX, int maxY) {
		this.siirry(nopeus);
		
		//Rajojen sisassa pysyminen
		Shape muoto = getMuoto();
		if (muoto.getTranslateX() > maxX) {
			muoto.setTranslateX(0);
		} else if (muoto.getTranslateX() < 0) {
			muoto.setTranslateX(maxX);
		}
		if (muoto.getTranslateY() > maxY) {
			muoto.setTranslateY(0);
		} else if (muoto.getTranslateY() < 0) {
			muoto.setTranslateY(maxY);
		}
	}
	
	/** Siirtaa muodon sijaintia annetun siirtymaa kuvaavan vektorin verran.
	 * @param siirtyma
	 */
	private void siirry(Point2D siirtyma) {
		Shape muoto = getMuoto();
		muoto.setTranslateX(muoto.getTranslateX() + siirtyma.getX());
		muoto.setTranslateY(muoto.getTranslateY() + siirtyma.getY());
	}
	
	public void muutaNopeutta(Point2D muutos) {
		this.nopeus = this.nopeus.add(muutos);
	}
	
	public boolean tormaa(PeliObjekti objekti) {
		Shape yhteinenAla = Shape.intersect(this.getMuoto(), objekti.getMuoto());
		return yhteinenAla.getBoundsInLocal().getHeight() != -1;
	}
	
	public void kuole() {
		this.elossa = false;
	}
	
	/** Kaantaa objektia xy-tasossa.
	 * @param asteet minka verran on tarkoitus kaantaa.
	 * @return Nykyinen kulma
	 */
	protected double kaanna(double asteet) {
		Shape muoto = this.getMuoto();
		muoto.setRotate(muoto.getRotate() + asteet);
		return muoto.getRotate();
	}
	
	public Point2D getSijainti() {
		double x = this.getMuoto().getTranslateX();
		double y = this.getMuoto().getTranslateY();
		return new Point2D(x, y);
	}

	public boolean elossa() {
		return elossa;
	}
	
	//TODO myohemmin liikemaarilla?
	/** Kahden PeliObjektin tormayksessa kappaleet erotetaan toisistaan ja toteutetaan 
	 * kimmoisan tormayksen vaatimat muutokset niiden kohtisuorissa nopeuden komponenteissa.
	 * @param objekti jonka kanssa tormataan.
	 */
	public void kimpoaa(PeliObjekti objekti) {
		//Lasketaan yksikkovektori, jonka suunta on talta toiselle objektille.
		Point2D yksikkovektori = objekti.getSijainti().subtract(this.getSijainti()).normalize();
		
		//Erotetaan tormaavat kappaleet, jotta ne eivat juuttuisi.
		double siirtyma = 0.2;
		double toisenOsuus = this.getMassa()/(this.getMassa() + objekti.getMassa()) * siirtyma;
		double tamanOsuus = toisenOsuus - siirtyma;
		while (this.tormaa(objekti)) {
			this.siirry(yksikkovektori.multiply(tamanOsuus));
			objekti.siirry(yksikkovektori.multiply(toisenOsuus));
		}
		
		//Lasketaan nopeuksien kappaleiden kohtisuorat komponentit
		Point2D tamanKomponentti = Laskuri2DAvaruuteen
				                     .vektorinProjektio(this.getNopeus(), yksikkovektori);
		Point2D toisenKomponentti = Laskuri2DAvaruuteen
                .vektorinProjektio(objekti.getNopeus(), yksikkovektori);
		
		
		// Jatetaan nopeudet ennalleen, jos objektit eivat tormanneet nopeuden takia. 
		boolean toinenNopeampi = toisenKomponentti.magnitude() > tamanKomponentti.magnitude();
		if (toinenNopeampi) {
			boolean toinenPoispainTasta = toisenKomponentti.angle(yksikkovektori) < 90 
					|| 315 < toisenKomponentti.angle(yksikkovektori);
			if (toinenPoispainTasta) return; 
		} else {
			boolean tamaPoispainToisesta = 90 < tamanKomponentti.angle(yksikkovektori) 
					&& tamanKomponentti.angle(yksikkovektori) < 315;
			if (tamaPoispainToisesta) return; 
		}
		
		/* Kokonaisliikemaara sailyy tormayksessa ja kimmoisassa tormayksessa
		 * vanhan ja uuden nopeuden summa on yhta suuri kummallakin. */		
		Point2D tamanUusiKomponentti = toisenKomponentti.multiply(2)
				.subtract(tamanKomponentti)
				.multiply(objekti.getMassa())
				.add(tamanKomponentti.multiply(this.getMassa()))
				.multiply(1.0/(this.getMassa()+objekti.getMassa()));
		Point2D toisenUusiKomponentti = tamanKomponentti
									.add(tamanUusiKomponentti)
									.subtract(toisenKomponentti);
		//Korvataan vanha nopeuden komponentti uudella.
		this.muutaNopeutta(tamanKomponentti.multiply(-1));
		this.muutaNopeutta(tamanUusiKomponentti);
		
		objekti.muutaNopeutta(toisenKomponentti.multiply(-1));
		objekti.muutaNopeutta(toisenUusiKomponentti);
	}
}
