package asteroids;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class AsteroidsPeli {
	private List<Asteroidi> asteroidit;
	private List<Ammus> ammukset;
	
	public int ruudunLeveys;
	public int ruudunKorkeus;
	private Pane pane = new Pane();
	private Label infoText = new Label("Debug");
	private Pelaaja pelaaja1;
	
	public AsteroidsPeli(int ruudunLeveys, int ruudunKorkeus) {
		this.ruudunLeveys = ruudunLeveys;
		this.ruudunKorkeus = ruudunKorkeus;
	}

	public Pane kaynnista() {		
		this.uusiPeli();
        return this.pane;
	}
	
	/** Alustaa pelin */
	public void uusiPeli() {		
		this.pelaaja1 = new Pelaaja(400, 300);
		
		ammukset = new LinkedList<Ammus>();
		
		this.asteroidit = new ArrayList<Asteroidi>();
		this.asteroidit.add(new Asteroidi(new Point2D(0.2,0.0), new Point2D(100.0, 100.0), 2,30));
		this.asteroidit.add(new Asteroidi(new Point2D(0.4,0.0), new Point2D(200.0, 100.0), 2,30));
		this.asteroidit.add(new Asteroidi(new Point2D(0.4,0.0), new Point2D(100.0, 200.0), 2,30));
		this.asteroidit.add(new Asteroidi(new Point2D(-0.4,0.0), new Point2D(250.0, 260.0), 2,30));
		this.asteroidit.add(new Asteroidi(new Point2D(0.5,2.0), new Point2D(700.0, 100.0), 2,10));
		this.asteroidit.add(new Asteroidi(new Point2D(-0.1,-0.05), new Point2D(700.0, 450.0), 0.2,80));
		
		this.pane.getChildren().clear();
        //Lisataan naytettavat
		this.infoText = new Label();
        this.pane.getChildren().add(infoText);
        this.pane.getChildren().add(pelaaja1.getMuoto());
        this.asteroidit.forEach(asteroidi -> {
        	this.pane.getChildren().add(asteroidi.getMuoto());
        });
	}

	/** Peliloopin toiminnat.
	 * @param timeStamp Kyseisen framen aikaleima.
	 */
	public void tick(long time) {
		StringBuilder infoString = new StringBuilder();
		
		//TODO yhdeksi loopiksi?
        //Objektien liikkuminen
		pelaaja1.liiku(ruudunLeveys, ruudunKorkeus);
		this.asteroidit.forEach(asteroidi -> {
        	asteroidi.liiku(ruudunLeveys, ruudunKorkeus);
        });
		this.ammukset.forEach(ammus -> {
        	ammus.liiku(ruudunLeveys, ruudunKorkeus);
        });
		
		
		//Tormays asteroideihin
		for (int i = 0; i < this.asteroidit.size(); i++) {
			Asteroidi asteroidi = this.asteroidit.get(i);
			//Pelaajaan
			if (pelaaja1.tormaa(asteroidi)) {
				pelaaja1.kimpoaa(asteroidi);
			}
			//Toisiin asteroideihin
			for (int j = i+1; j < this.asteroidit.size(); j++) {
				if (asteroidi.tormaa(this.asteroidit.get(j))) {
					asteroidi.kimpoaa(this.asteroidit.get(j));
				}
			}
		}
		
		Collection<Asteroidi> lisattavatAsteroidit = new LinkedList<Asteroidi>();
		//Tormays ammuksiin
		this.ammukset.forEach(ammus -> {
			this.asteroidit.forEach(asteroidi -> {
	        	if (ammus.tormaa(asteroidi)) {
	        		lisattavatAsteroidit.addAll(asteroidi.osuma(ammus));
	        		ammus.kuole();
	        	}
	        });
        });
		
		//Kuolleiden poistaminen
		this.poistaKuolleet(this.asteroidit);
		this.poistaKuolleet(this.ammukset);
		
		//Lisataan uudet asteroidit
		this.asteroidit.addAll(lisattavatAsteroidit);
		lisattavatAsteroidit.forEach(asteroidi -> {
			this.pane.getChildren().add(asteroidi.getMuoto());			
		});
		
		//PeliInfo
		infoString.append("Pisteet: ").append(this.pelaaja1.getPisteet());
		this.infoText.setText(infoString.toString());
		
		//Voitto
		if (this.asteroidit.size() == 0) {
			this.uusiPeli();
		}
	}

	/** Poistaa kuolleet pelin UI Pane elementista ja annetulta listalta.
	 * @param kokoelma jolta poistetaan kuolleet.
	 */
	private void poistaKuolleet(Collection<? extends PeliObjekti> kokoelma) {
		Iterator<? extends PeliObjekti> iteraattori = kokoelma.iterator();
		PeliObjekti objekti; 
		while (iteraattori.hasNext()) {
			objekti = iteraattori.next();
			if (!objekti.elossa()) {
				this.pane.getChildren().remove(objekti.getMuoto());
				iteraattori.remove();
			}
		}
	}

	/** Lisaa ammuksen listalle ja 
	 * @param ammus lisattava ammus.
	 */
	private void lisaaAmmus(Collection<Ammus> ammuksia) {
		ammuksia.forEach(ammus -> {
			this.ammukset.add(ammus);
			this.pane.getChildren().add(ammus.getMuoto());
		});
	}

	/** Suorittaa pelaajan komentoa vastaavan toiminnon.
	 * @param komento joka halutaan suoritettavan.
	 */
	public void ohjaa(KeyCode komento) {
		Pelaaja pelaaja = this.pelaaja1;
		
		switch (komento) {
			case RIGHT:
				pelaaja.kaannaPelaajaa(true);
				break;
			case LEFT:
				pelaaja.kaannaPelaajaa(false);
				break;
			case UP:
				pelaaja.kiihdyta();
				break;
			case DOWN:
				pelaaja.jarruta();
				break;
			case SPACE:
				lisaaAmmus(pelaaja.ammu());
				break;
			default:				
		}
	}

	/** Luo aloitusruudun pelia varten.
	 * @return
	 */
	public Pane aloitus() {
		Pane palautettava = new Pane();
		Button kaynnistaNappi = new Button();
		
		return null;
	}
	
	
}
