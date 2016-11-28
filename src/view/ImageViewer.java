package view;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageViewer {
	private static final double SCALE_FACTOR = 1.1;
	private static final int DISPLAY = 800;
	private BufferedImage myImage;
	private Stage myStage;
	
	public ImageViewer(BufferedImage image){
		myImage = image;
		myStage = new Stage();
		init();
	}
	
	private void init() {
		Group root = new Group();
		Image image = SwingFXUtils.toFXImage(myImage, null);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(DISPLAY);
		imageView.setPreserveRatio(true);
		root.getChildren().add(imageView);
		Scene scene = new Scene(root, DISPLAY, DISPLAY);
		root.setOnScroll((event)-> {
			if(event.getDeltaY()==0){
				return;
			}
			double scale = event.getDeltaY()<0 ? 1/SCALE_FACTOR : SCALE_FACTOR;
			imageView.setScaleX(imageView.getScaleX()*scale);
			imageView.setScaleY(imageView.getScaleY()*scale);

		});
		
		MenuBar menuBar = initMenu(scene);
		root.getChildren().add(menuBar);
		myStage.setTitle("ASCII Art");
		myStage.setScene(scene);
	}

	private MenuBar initMenu(Scene scene) {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem save = new MenuItem("Save");
		save.setOnAction(event ->{
			System.out.println("doing");
			FileChooser fc = new FileChooser();
			File saved = fc.showSaveDialog(myStage);
			try {
				ImageIO.write(myImage, "png", saved);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR, "Error saving image");
				alert.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> alert.close());
			}
		});
		file.getItems().add(save);
		menuBar.getMenus().add(file);
		menuBar.setPrefWidth(scene.getWidth());
		return menuBar;
	}

	public void display(){
		myStage.show();
	}
}
