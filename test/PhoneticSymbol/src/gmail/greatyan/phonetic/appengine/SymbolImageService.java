package gmail.greatyan.phonetic.appengine;

import gmail.greatyan.phonetic.SymbolImageDescription;
import gmail.greatyan.phonetic.Symbols;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SymbolImageService {

	static SymbolImageService service;

	static final int SYMBOLS_PER_LINE = 32;

	public static synchronized SymbolImageService getService()
			throws IOException {
		if (service == null) {
			service = new SymbolImageService();
		}
		return service;
	}

	SymbolImageDescription symbolDesc;
	Image symbolImage;

	protected SymbolImageService() throws IOException {
		symbolDesc = SymbolImageDescription.load();
		symbolImage = loadImage();
	}

	protected Image loadImage() throws IOException {
		InputStream in = SymbolImageService.class
				.getResourceAsStream("/gmail/greatyan/phonetic/phonetic_32.png");
		if (in != null) {
			try {
				Image image = new PNGDecoder().decode(in);
				return image;
			} finally {
				in.close();
			}
		}
		return null;
	}

	public Image getImage(String pronouce) throws IOException {
		char[] symbols = pronouce.toCharArray();
		int charWidth = symbolDesc.maxAdvance;
		int charHeight = symbolDesc.maxAscent + symbolDesc.maxDescent;
		int imageWidth = 0;
		int imageHeight = charHeight;
		for (int i = 0; i < symbols.length; i++) {
			int index = Symbols.getSymbolIndex(symbols[i]);
			imageWidth += symbolDesc.widths[index];
		}
		Image image = new Image(imageWidth, imageHeight);
		int x = 0;
		for (int i = 0; i < symbols.length; i++) {
			int index = Symbols.getSymbolIndex(symbols[i]);
			int row = index / SYMBOLS_PER_LINE;
			int col = index % SYMBOLS_PER_LINE;
			int sx = col * charWidth;
			int sy = row * charHeight;
			image.copy(symbolImage,sx, sy,  x, 0, charWidth,  charHeight);
			x += symbolDesc.widths[index];
		}
		return image;
	}

	public byte[] getImageData(String pronouce) throws IOException {
		Image image = getImage(pronouce);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		return new PNGEncoder(PNGEncoder.BW_MODE).encode(image);
	}

}
