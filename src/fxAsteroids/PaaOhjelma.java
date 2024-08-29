package fxAsteroids;

import java.util.HashMap;
import java.util.Map;

import asteroids.AsteroidsPeli;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PaaOhjelma extends Application {
	private AsteroidsPeli peli;
	private Map<KeyCode, Boolean> keys = new HashMap<>();
	private int ruudunLeveys = 800;
	private int ruudunKorkeus = 600;
	private int fpsMax = 100;
	private int askelvali = 1000000000 / fpsMax; // nanosekunteina
	private long edellinenTimeStamp = 0;
	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		peli = new AsteroidsPeli(this.ruudunLeveys, this.ruudunKorkeus);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now - edellinenTimeStamp < askelvali) return;
				edellinenTimeStamp = now;
				//Nappaimet
				if (keys.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.RIGHT);
				}
				if (keys.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.LEFT);
				}
				if (keys.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.UP);
				}
				if (keys.getOrDefault(KeyCode.DOWN, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.DOWN);
				}
				if (keys.getOrDefault(KeyCode.SPACE, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.SPACE);
				}
				if (keys.getOrDefault(KeyCode.SPACE, Boolean.FALSE)) {
					peli.ohjaa(KeyCode.SPACE);
				}
				peli.tick(now);				
			}
		};
		timer.start();
		
		Pane aloitusPane = this.peli.aloitus();
        Scene scene = new Scene(this.peli.kaynnista(), this.ruudunLeveys, this.ruudunKorkeus);
        scene.setOnKeyPressed(event -> {
        	this.keys.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
        	this.keys.put(event.getCode(), Boolean.FALSE);
        });
		primaryStage.setScene(scene);
        primaryStage.setTitle("Asteroids");
        primaryStage.show();
	}
}
