package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.View;

public class Main extends Application{
	private View myView;

	public static void main(String[] args) {
		Application.launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		myView = new View(primaryStage);
		myView.display();
	}


}
