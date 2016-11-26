package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		Application.launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		FileChooser fc = new FileChooser();
		File file = fc.showOpenDialog(primaryStage);
		if(file!=null){
			BufferedImage bim = ImageIO.read(file);
			AsciiMaker maker = new AsciiMaker(bim);
			BufferedImage ascii = maker.makeImage();
			File save = fc.showSaveDialog(primaryStage);
			if(file !=null){
				try{
					ImageIO.write(ascii, "png", save);
				} catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
		Platform.exit();
	}


}
