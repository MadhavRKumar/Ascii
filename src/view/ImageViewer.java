package view;

import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
		myStage.setScene(scene);
	}

	public void display(){
		myStage.show();
	}
}
