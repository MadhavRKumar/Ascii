package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.AsciiMaker;

public class View {
	private GridPane gridPane;
	private Stage myStage;
	private boolean isColor = false;
	
	public View(Stage stage){
		gridPane = new GridPane();
		myStage = stage;
		init();
		Scene scene = new Scene(gridPane, 300, 250);
		myStage.setScene(scene);
	}
	
	private void init() {
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(15);
		Button open = new Button("Choose Image");
		myStage.setTitle("Image2ASCII");
		open.setOnAction((event) ->{
			openImage();
		});
		ToggleGroup group = new ToggleGroup();
		RadioButton color = new RadioButton("Color");
		RadioButton bw = new RadioButton("Monochrome");
		color.setToggleGroup(group);
		bw.setToggleGroup(group);
		bw.setSelected(true);
		group.selectedToggleProperty().addListener(event ->{
			isColor = (group.getSelectedToggle()).equals(color);
		});
		gridPane.add(open, 0, 0);
		gridPane.add(bw, 1, 0);
		gridPane.add(color, 1, 1);
	}
	
	private void openImage(){
		FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png", "*.bmp", "*.jpeg","*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
		File file = fc.showOpenDialog(myStage);
		if(file!=null){
			try{
			BufferedImage bim = ImageIO.read(file);
			AsciiMaker maker = new AsciiMaker(bim);
			BufferedImage ascii = isColor ? maker.getColorImage() : maker.getBWImage();
			ImageViewer iv = new ImageViewer(ascii);
			iv.display();
			} catch(IOException e){
				Alert alert = new Alert(AlertType.ERROR, "Error reading image");
				alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> alert.close());
			}
		}
	}
	

	public void display(){
		myStage.show();
	}
}
