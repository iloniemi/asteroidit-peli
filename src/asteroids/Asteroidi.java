package asteroids;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import laskentaa.Laskuri2DAvaruuteen;

public class Asteroidi extends PeliObjekti {
	private Polygon muoto;
	private double kulmanopeus;
	private final double sade;
	private static double MIN_HAJOAVA_SADE = 20.0;
	private static double TIHEYS = 0.01;
	
	//TODO kulmanopeuden voisi laittaa riippumaan koosta
	
	/** Luo asteroidin, jolla on 5 - 7 kulmaa.
	 * @param nopeus Asteroidin nopeus.
	 * @param sijainti johon asteroidi sijoitetaan.
	 * @param kulmanopeus jolla asteroidi pyorii.
	 * @param sade ympyralle, jonka kehalle asteroidin kulmat asetetaan.
	 */
	public Asteroidi(Point2D nopeus, Point2D sijainti, double kulmanopeus, double sade) {
		super(nopeus, 4.18879020478639 * sade * sade * sade * Asteroidi.TIHEYS);
		this.kulmanopeus = kulmanopeus;
		this.sade = sade;
		int kulmia = 5 + new Random().nextInt(3);
		Double[] kulmat = new Double[kulmia*2];
		for (int i = 0; i<kulmia; i++) {
			double kulma = 360.0 / kulmia * (i + 0.5 * (0.5 - Math.random()));
			Point2D piste = Laskuri2DAvaruuteen.pisteYmpyranKehalta(sade, kulma);
			kulmat[i*2] = piste.getX();
			kulmat[i*2+1] = piste.getY();
		}
		this.muoto = new Polygon();
		this.muoto.getPoints().addAll(kulmat);
		this.muoto.setTranslateX(sijainti.getX());
		this.muoto.setTranslateY(sijainti.getY());
	}

	@Override
	public Shape getMuoto() {
		return this.muoto;
	}
	
	public double getSade() {
		return sade;
	}
	
	/** Ylaluokan liiku -metodin lisaksi kaannetaan asteroidia kulmanopeuden verran. */
	@Override
	public void liiku(int maxX, int maxY) {
		super.liiku(maxX, maxY);
		this.kaanna(this.kulmanopeus);
	}

	/** Kun ammus osuu asteroidiin, iso asteroidi halkeaa uusiksi asteroideiksi 
	 * ja pieni vain haviaa. 
	 */
	public Collection<Asteroidi> osuma(Ammus ammus) {
		//Pilkkoutuminen, jos asteroidi on tarpeeksi suuri.
		Collection<Asteroidi> uudet = new LinkedList<Asteroidi>();
		if (this.sade > Asteroidi.MIN_HAJOAVA_SADE) {
			for (int i = 0; i < 3; i++) {
				uudet.add(luoPala(120.0 * i));
			}
		}
		
		//Tuhoajalle piste
		ammus.getOmistaja().muutaPisteita(1);
		
		this.kuole();
		return uudet;
	}
	
	/** Luo uuden alkuperaista asteroidia pienemman asteroidin. 
	 * @param kulma jonka suuntaan pala luodaan 
	 * ja johon suuntaan palalle annetaan lisavauhtia.
	 * @return luotu asteroidin pala.
	 */
	private Asteroidi luoPala(double kulma) {
		Point2D nopeudenLisays = Laskuri2DAvaruuteen.kulmaJaJanaVektoreiksi(kulma, 0.5 * Math.random());
		Point2D uudenNopeus = this.getNopeus().add(nopeudenLisays);
		double uudenKulmanopeus = this.kulmanopeus * (0.5 - Math.random()) * (1.7 - Math.random());
		double uudenSade = 0.464 * this.sade - Math.random() * 5.0;
		//Ympyran sisalle mahtuu kolme samankokoista ympyraa, joiden sade voi olla maksimissaan noin 0,464.
		Point2D uudenSijaintiVrtVanhaan = Laskuri2DAvaruuteen.kulmaJaJanaVektoreiksi(kulma, this.sade-uudenSade);
		Point2D uudenSijainti = this.getSijainti().add(uudenSijaintiVrtVanhaan);
		return new Asteroidi(uudenNopeus, uudenSijainti, uudenKulmanopeus, uudenSade);
	}
}
