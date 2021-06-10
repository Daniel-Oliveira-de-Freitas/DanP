package application;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	private static Stage stage;
	private static Scene sc;

	@Override
	public void start(Stage primaryStage) throws Exception {

		stage = primaryStage;
		AnchorPane root = FXMLLoader.load(getClass().getResource("TelaP.fxml"));
	    sc = new Scene(root);
		stage.getIcons().add(new Image("file:imagens/astronauta.jpg"));
		stage.setScene(sc);
		stage.setTitle("DAMP");
		stage.setResizable(false);

		stage.show();
 
	}

	public static void main(String[] args) {
		launch(args);
	}
}
