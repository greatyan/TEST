package gmail.greatyan.phonetic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class SymbolImage {

	static final int SYMBOLS_PER_LINE = 32;
	static final int FONT_SIZE = 32;
	static final String FONT_NAME = "DejaVu Sans";

	static public void generateImage(String fileName) throws IOException {

		Font font = new Font(FONT_NAME, Font.PLAIN, FONT_SIZE);
		SymbolImageDescription desc = createSymbolDescription(font);
		BufferedImage image = drawPhoneticSymbols(desc, font);
		ImageIO.write(image, "png", new File(fileName + ".png"));
		FileOutputStream out = new FileOutputStream(fileName + ".dat");
		try {
			desc.write(out);
		} finally {
			out.close();
		}
	}

	static private SymbolImageDescription createSymbolDescription(Font font) {
		char[] symbols = Symbols.getSymbols();
		BufferedImage bufferedImage = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		SymbolImageDescription desc = new SymbolImageDescription();
		desc.ascent = fontMetrics.getAscent();
		desc.descent = fontMetrics.getDescent();
		desc.leading = fontMetrics.getLeading();
		desc.maxAscent = fontMetrics.getMaxAscent();
		desc.maxDescent = fontMetrics.getMaxDescent();
		desc.maxAdvance = fontMetrics.getMaxAdvance();
		desc.widths = new int[symbols.length];
		for (int i = 0; i < symbols.length; i++) {
			desc.widths[i] = fontMetrics.charWidth(symbols[i]);
		}
		g2d.dispose();
		return desc;
	}

	static private BufferedImage drawPhoneticSymbols(SymbolImageDescription desc,
			Font font) {
		// g2d always be 72dpi, so 1pt equals to 1px
		char[] symbols = Symbols.getSymbols();
		int charHeight = desc.maxAscent + desc.maxDescent;
		int charWidth = desc.maxAdvance;

		int lines = (symbols.length + SYMBOLS_PER_LINE - 1) / SYMBOLS_PER_LINE;
		int imageHeight = charHeight * lines;
		int imageWidth = charWidth * SYMBOLS_PER_LINE;

		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		BufferedImage bufferedImage = new BufferedImage(imageWidth,
				imageHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = bufferedImage.createGraphics();
		// Draw graphics
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, imageWidth, imageHeight);
		g2d.setFont(font);
		// int ascent = fontMetrics.getAscent();
		int y = 0;
		int x = 0;
		int charIndex = 0;
		for (int i = 0; i < lines; i++) {
			y += charHeight;
			x = 0;
			for (int j = 0; j < SYMBOLS_PER_LINE; j++) {
				if (charIndex < symbols.length) {
//					g2d.setColor(Color.RED);
//					g2d.drawRect(x, y - charHeight, charWidth, charHeight);
					g2d.setColor(Color.BLACK);
					g2d.drawChars(symbols, charIndex, 1, x, y - desc.maxDescent);
				}
				charIndex++;
				x += charWidth;
			}
		}

		// Graphics context no longer needed so dispose it
		g2d.dispose();

		return bufferedImage;
	}

	private SymbolImageDescription symbolDesc;
	private BufferedImage symbolImage;

	public SymbolImage() throws IOException {
		symbolDesc = loadDescription();
		symbolImage = loadImage();
	}

	public void createImage(String pronouce, String fileName)
			throws IOException {
		RenderedImage image = getImage(pronouce);
		ImageIO.write(image, "png", new File(fileName));
	}

	public RenderedImage getImage(String pronouce) throws IOException {
		char[] symbols = pronouce.toCharArray();
		int charWidth = symbolDesc.maxAdvance;
		int charHeight = symbolDesc.maxAscent + symbolDesc.maxDescent;
		int imageWidth = 0;
		int imageHeight = charHeight;
		for (int i = 0; i < symbols.length; i++) {
			int index = Symbols.getSymbolIndex(symbols[i]);
			imageWidth += symbolDesc.widths[index];
		}
		BufferedImage image = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setBackground(Color.WHITE);
		g2d.fillRect(0, 0, imageWidth, imageHeight);

		g2d.setColor(Color.BLACK);
		int x = 0;
		for (int i = 0; i < symbols.length; i++) {
			int index = Symbols.getSymbolIndex(symbols[i]);
			int row = index / SYMBOLS_PER_LINE;
			int col = index % SYMBOLS_PER_LINE;
			int sx = col * charWidth;
			int sy = row * charHeight;
			g2d.drawImage(symbolImage, x, 0, x + charWidth, charHeight, sx, sy,
					sx + charWidth, sy + charHeight, null);
			x += symbolDesc.widths[index];
		}
		g2d.dispose();
		return image;
	}

	public byte[] getImageData(String pronouce) throws IOException {
		RenderedImage image = getImage(pronouce);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		return out.toByteArray();
	}

	private BufferedImage loadImage() throws IOException {
		InputStream stream = SymbolImage.class
				.getResourceAsStream("phonetic_32.png");
		if (stream != null) {
			try {
				BufferedImage image = ImageIO.read(stream);
				return image;
			} finally {
				stream.close();
			}
		}
		throw new IOException("can't load phonetic_32.png");
	}

	private SymbolImageDescription loadDescription() throws IOException {
		InputStream stream = SymbolImage.class
				.getResourceAsStream("phonetic_32.dat");
		if (stream != null) {
			try {
				SymbolImageDescription desc = new SymbolImageDescription();
				desc.read(stream);
				return desc;
			} finally {
				stream.close();
			}
		}
		throw new IOException("can't load phonetic_32.dat");
	}

	public static void main(String[] args) throws IOException {

//		SymbolImage.generateImage("phonetic_32");

		SymbolImage image = new SymbolImage();
		image.createImage("opqaue /əʊˈpeɪk//oʊ-/", "opaque.png");
	}

}
