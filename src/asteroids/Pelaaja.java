package asteroids;

import java.util.Collection;
import java.util.LinkedList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import laskentaa.Laskuri2DAvaruuteen;

//TODO Laita aluksen karki aluksi oikealle.
public class Pelaaja extends PeliObjekti {
	private Polygon muoto = new Polygon();
	private double kiihtyvyys = 0.1;
	private static double ALUKSEN_PITUUS = 25.0;
	private static double ALUKSEN_LEVEYS = 20.0;
	private static double KAANTONOPEUS = 5.0;
	private static double MASSA = 100.0;
	private int pisteet = 0;
	private int ammusCooldown = 0;
	
	/** Luo pelaajan ja asettaa sen annettuun pisteeseen.
	 * @param x -koordinaatti.
	 * @param y -koordinaatti.
	 */
	public Pelaaja(int x, int y) {
		super(new Point2D(0,0), Pelaaja.MASSA);
		this.muoto.setFill(Color.CRIMSON);
		this.muoto.getPoints().addAll(new Double[]{
			    ALUKSEN_LEVEYS/2, 0.0,
			    0.0, ALUKSEN_PITUUS,
			    ALUKSEN_LEVEYS, ALUKSEN_PITUUS });
		this.muoto.setTranslateX(x);
		this.muoto.setTranslateY(y);
	}
	
	/** Lisaa PeliObjektin liiku -metodiin ampumisviiveen toiminnallisuuden. */
	@Override
	public void liiku(int maxX, int maxY) {
		super.liiku(maxX, maxY);
		if (this.ammusCooldown > 0) {
			this.ammusCooldown -= 1;
		}
	}
	
	public void kiihdyta() {
		Point2D muutos = Laskuri2DAvaruuteen.kulmaJaJanaVektoreiksi(
								this.muoto.getRotate(), 
								this.kiihtyvyys
						);
		this.muutaNopeutta(muutos);
	}
	
	/** Kaantaa pelaajaa oikealle tai vasemmalle sen kaantonopeuden verran.
	 * @param oikealle kaannetaanko oikealle tai vasemmalle.
	 */
	public void kaannaPelaajaa(boolean oikealle) {
		this.kaanna((oikealle) ? Pelaaja.KAANTONOPEUS : -Pelaaja.KAANTONOPEUS);
	}
	
	/**
	 * Pienentaa aluksen nopeutta vahitellen nollaan saakka.
	 */
	public void jarruta() {
		double muutosX;
		double muutosY;
		if (Math.abs(this.getNopeus().getX()) < this.kiihtyvyys) {
			muutosX = -this.getNopeus().getX();
		} else if (this.getNopeus().getX() > 0) {
			muutosX = -0.5*this.kiihtyvyys;
		} else {
			muutosX= 0.5*this.kiihtyvyys;
		}
		
		if (Math.abs(this.getNopeus().getY()) < this.kiihtyvyys) {
			muutosY = -this.getNopeus().getY();
		} else if (this.getNopeus().getY() > 0) {
			muutosY = -0.5*this.kiihtyvyys;
		} else {
			muutosY = 0.5*this.kiihtyvyys;
		}
		
		muutaNopeutta(new Point2D(muutosX, muutosY));
	}

	@Override
	public Shape getMuoto() {
		return this.muoto;
	}

	/** Luo ammuksen, joka lahtee aluksen karjesta poispain.
	 * @return luotu ammus.
	 */
	public Collection<Ammus> ammu() {
		Collection<Ammus> palautettava = new LinkedList<Ammus>();
		if (this.ammusCooldown != 0) {
			return palautettava;
		}
		
		this.ammusCooldown = 20;
		double kulma = this.getMuoto().getRotate();
		double x = this.muoto.getTranslateX() + ALUKSEN_LEVEYS/2;
		double y = this.muoto.getTranslateY() + ALUKSEN_PITUUS/2;
		
		Point2D karkiKeskipisteesta = Laskuri2DAvaruuteen
				.pisteYmpyranKehalta(ALUKSEN_PITUUS/2, kulma - 90.0); 
		/** -90, koska aluksen karki on 0 asteen tapauksessa ylospain vs yksikkoympyra
		 * jossa 0 astetta olisi vaakatasossa. **/
		
		Point2D karki = new Point2D(x, y).add(karkiKeskipisteesta);
		palautettava.add(new Ammus(karki, kulma, this));
		return palautettava;
	}

	public int getPisteet() {
		return pisteet;
	}

	/** Muuttaa pelaajan pisteita annetun luvun verran.
	 * @param muutos
	 */
	public void muutaPisteita(int muutos) {
		this.pisteet += muutos;
	}
}
