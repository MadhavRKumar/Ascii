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
	private Color[][] colorArray;
	public AsciiMaker(BufferedImage image){
		originalImage = resize(image);
		asciiImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
		colorArray = makePixelArray();
		makeAscii();
	}
	
	private BufferedImage resize(BufferedImage image){
		double ratio = (double)(image.getHeight())/image.getWidth();
		int newHeight = (int)(WIDTH*ratio);
		BufferedImage newImage = new BufferedImage(WIDTH, newHeight, image.getType());
		Graphics2D g2d = newImage.createGraphics();
		g2d.drawImage(image, 0, 0, WIDTH, newHeight, null);
		return newImage;
	}
	
	private Color[][] makePixelArray() {
		byte[] pixels = ((DataBufferByte)originalImage.getRaster().getDataBuffer()).getData();
		boolean hasAlpha = originalImage.getAlphaRaster() != null;
		int pixelLength = hasAlpha ? 4 : 3;
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		Color[][] pixArray = new Color[height][width];
		int argb;
		for (int pixel=0, row=0, col=0; pixel<pixels.length; pixel+=pixelLength){
			int pos = pixel;
			argb = hasAlpha ? (((int)pixels[pos++]&0xff)<<24) : -16777216;
			argb += ((int) pixels[pos++]&0xff);
			argb += (((int) pixels[pos++]&0xff)<<8);
			argb += (((int) pixels[pos++]&0xff)<<16);
			Color c = new Color(argb);
			pixArray[row][col] = c;
		    col++;
		    if (col == width) {
		    	col = 0;
		    	row++;
		    }
		}
		return pixArray;
	}
	
	private void makeAscii(){
		for(int x=0; x<colorArray.length; x++){
			for(int y=0; y<colorArray[x].length; y++){
				Color c = colorArray[x][y];
				double average = (c.getRed()+c.getGreen()+c.getBlue())/3.0;
				double val = (average/255.0 );
				int index = (int)(val * (SYMBOLS.length-1));
				ascii.append(SYMBOLS[index]);
			}
			ascii.append("\n");
		}
	}
	
	public BufferedImage getBWImage(){
		return makeImage(false);
	}
	
	public BufferedImage getColorImage(){
		return makeImage(true);
	}
	
	private BufferedImage makeImage(boolean isColor){
		String[] lines = ascii.toString().split("\n");
		Graphics2D g2d = asciiImage.createGraphics();
		Font font = new Font("Courier",Font.PLAIN, 20);
		g2d.setFont(font);
		FontMetrics fm = g2d.getFontMetrics();
		int width = fm.stringWidth(lines[0]);
		int height = (int)(fm.getHeight()*0.8);
		int charWidth = fm.stringWidth("A");
		g2d.dispose();
		asciiImage = new BufferedImage(width,height*(lines.length-1), originalImage.getType());
		g2d = asciiImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, asciiImage.getHeight());
		g2d.setFont(font);
		g2d.setColor(Color.BLACK);
		for(int x=0; x<lines.length; x++){
			String[] stringArr = lines[x].split("");
			int xPos = 0;
			int yPos = (int)(x*height);
			for(int y=0; y<stringArr.length; y++){
				if(isColor){
					g2d.setColor(colorArray[x][y]);
				}
				g2d.drawString(stringArr[y], xPos, yPos);
				xPos += charWidth;
			}	
		}
		g2d.dispose();

		return asciiImage;
	}
}
