package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class AsciiMaker {
	private StringBuilder ascii = new StringBuilder();
	private BufferedImage originalImage;
	private BufferedImage asciiImage;
	private final String[] SYMBOLS = {"@", "%", "#", "x", "+", "=", ":", "-", ".", " "};
	private final int WIDTH = 200;
	public AsciiMaker(BufferedImage image){
		originalImage = resize(image);
		asciiImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
		int[][] arr = makePixelArray();
		makeAscii(arr);
	}
	
	private BufferedImage resize(BufferedImage image){
		double ratio = (double)(image.getHeight())/image.getWidth();
		int newHeight = (int)(WIDTH*ratio);
		BufferedImage newImage = new BufferedImage(WIDTH, newHeight, image.getType());
		Graphics2D g2d = newImage.createGraphics();
		g2d.drawImage(image, 0, 0, WIDTH, newHeight, null);
		return newImage;
	}

	private int[][] makePixelArray() {
		byte[] pixels = ((DataBufferByte)originalImage.getRaster().getDataBuffer()).getData();
		boolean hasAlpha = originalImage.getAlphaRaster() != null;
		int pixelLength = hasAlpha ? 4 : 3;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		int[][] pixArray = new int[height][width];
		int argb;
		for (int pixel=0, row=0, col=0; pixel<pixels.length; pixel+=pixelLength){
			int pos = pixel;
			argb = hasAlpha ? (((int)pixels[pos++]&0xff)<<24) : -16777216;
			argb += ((int) pixels[pos++]&0xff);
			argb += (((int) pixels[pos++]&0xff)<<8);
			argb += (((int) pixels[pos++]&0xff)<<16);
			Color c = new Color(argb);
			pixArray[row][col] = (int)((c.getBlue()+c.getRed() + c.getGreen())/3.0);
		    col++;
		    if (col == width) {
		    	col = 0;
		    	row++;
		    }
		}
		return pixArray;
	}
	
	private void makeAscii(int[][] arr){
		for(int x=0; x<arr.length; x++){
			for(int y=0; y<arr[x].length; y++){
				int index = (int)(arr[x][y]/255.0 * (SYMBOLS.length-1));
				ascii.append(SYMBOLS[index]);
			}
			ascii.append("\n");
		}
	}
	
	public BufferedImage makeImage(){
		String[] lines = ascii.toString().split("\n");
		Graphics2D g2d = asciiImage.createGraphics();
		Font font = new Font("Courier",Font.PLAIN, 20);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(lines[0]);
		int height = (int)(fm.getHeight()*0.8);
		g2d.dispose();
		asciiImage = new BufferedImage(width,height*lines.length, originalImage.getType());
		g2d = asciiImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, asciiImage.getHeight());
		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		for(int x=0; x<lines.length; x++){
			int xPos = 0;
			int yPos = (int)(x*height);
			g2d.drawString(lines[x], xPos, yPos);	
		}
		g2d.dispose();

		return asciiImage;
	}
}
