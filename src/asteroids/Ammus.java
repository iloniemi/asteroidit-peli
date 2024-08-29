package asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import laskentaa.Laskuri2DAvaruuteen;

public class Ammus extends PeliObjekti {
	private static final int MASSA = 1;
	private long elinaika = 200;
	private Polygon muoto = new Polygon();
	private static double VAUHTI= 3.0;
	private Pelaaja omistaja;
	
	
	//TODO: voisi laittaa huomioimaan ampuvan aluksen nopeuden.
	//TODO: ammuksen vasen ylakulma tulee annettuun lahtopisteeseen
	//TODO: Ammukselle rotaatio
	/**
	 * @param lahtopiste
	 * @param kulma johon ammus lahtee liikkumaan. Asteina.
	 */
	public Ammus(Point2D lahtopiste, double kulma, Pelaaja omistaja) {
		super(new Point2D(0.0, 0.0), Ammus.MASSA);
		this.omistaja = omistaja;
		Point2D nopeus = Laskuri2DAvaruuteen.kulmaJaJanaVektoreiksi(kulma, Ammus.VAUHTI);
		this.muutaNopeutta(nopeus);
		this.muoto.getPoints().addAll(new Double[] {
				0.0, 0.0,
				0.0, 3.0,
				3.0, 3.0,
				3.0, 0.0
		});
		this.muoto.setTranslateX(lahtopiste.getX());
		this.muoto.setTranslateY(lahtopiste.getY());
	}

	@Override
	Shape getMuoto() {
		return this.muoto;
	}
	
	@Override
	public void liiku(int maxX, int maxY) {
		super.liiku(maxX, maxY);
		this.elinaika -= 1;
		if (this.elinaika < 0) this.kuole();
	}

	public Pelaaja getOmistaja() {
		return omistaja;
	}
	
}
