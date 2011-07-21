package gmail.greatyan.phonetic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class SymbolGenerator {

	public static void main(String[] args) throws IOException {

		SymbolGenerator generator = new SymbolGenerator();
		generator.createImage("./phonetic_12.png", 12);
		// generator.createImage("./phonetic_16.png", 16);
		// generator.createImage("./phonetic_18.png", 28);
	}

	protected void createImage(String fileName, int fontSize)
			throws IOException {
		RenderedImage image = drawPhoneticSymbols(fontSize);
		File file = new File(fileName);
		ImageIO.write(image, "png", file);
	}

	RenderedImage drawPhoneticSymbols(int fontSize) {
		char[] symbols = Symbols.getSymbols();

		Font font = new Font("Lucida Sans", Font.PLAIN, fontSize);
		int charWidth = (int)(fontSize/72.0*96)/2;
		int charHeight = (int)(fontSize/72.0*96);
		

		// Create a graphics contents on the buffered image
		// Graphics2D g2d = bufferedImage.createGraphics();
		JFrame jframe = new JFrame();
		jframe.setSize(600, 600);
		jframe.setVisible(true);
		Graphics2D g2d = (Graphics2D) jframe.getContentPane().getGraphics();
		FontRenderContext frc = g2d.getFontRenderContext();
		
		int imageWidth = charWidth;
		int imageHeight = charHeight * symbols.length;
		
		// Draw graphics
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, imageWidth, imageHeight);
		g2d.setColor(Color.black);
		g2d.setFont(font);
		int y = charHeight;
		int x = charWidth;
		for (int i = 0; i < symbols.length; i++) {
			y += charHeight;
			g2d.drawChars(symbols, i, 1, x, y);
		}

		// Graphics context no longer needed so dispose it
		g2d.dispose();

		jframe.dispose();
		return null;
	}
}
