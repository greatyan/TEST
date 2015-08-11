package gmail.greatyan.phonetic.appengine;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGDecoder {

	public PNGDecoder() {
	}

	static private byte read(InputStream in) throws IOException {
		byte b = (byte) in.read();
		return b;
	}

	static private int readInt(InputStream in) throws IOException {
		byte b[] = read(in, 4);
		return (((b[0] & 0xff) << 24) + ((b[1] & 0xff) << 16)
				+ ((b[2] & 0xff) << 8) + ((b[3] & 0xff)));
	}

	static private byte[] read(InputStream in, int count) throws IOException {
		byte[] result = new byte[count];
		for (int i = 0; i < count; i++) {
			result[i] = read(in);
		}
		return result;
	}

	static private boolean compare(byte[] b1, byte[] b2) {
		if (b1.length != b2.length) {
			return false;
		}
		for (int i = 0; i < b1.length; i++) {
			if (b1[i] != b2[i]) {
				return false;
			}
		}
		return (true);
	}

	void checkEquality(byte[] b1, byte[] b2) {
		if (!compare(b1, b2)) {
			throw (new RuntimeException("Format error"));
		}
	}

	/**
	 * Decodes image from an input stream passed into constructor.
	 * 
	 * @return a BufferedImage object
	 * @throws IOException
	 */
	public Image decode(InputStream in) throws IOException {

		byte[] id = read(in, 12);
		checkEquality(id, new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0,
				0, 13 });

		byte[] ihdr = read(in, 4);
		checkEquality(ihdr, "IHDR".getBytes());

		int width = readInt(in);
		int height = readInt(in);

		Image result = new Image(width, height);

		byte[] head = read(in, 5);
		int mode;
		if (compare(head, new byte[] { 1, 0, 0, 0, 0 })) {
			mode = PNGEncoder.BW_MODE;
		} else if (compare(head, new byte[] { 8, 0, 0, 0, 0 })) {
			mode = PNGEncoder.GREYSCALE_MODE;
		} else if (compare(head, new byte[] { 8, 2, 0, 0, 0 })) {
			mode = PNGEncoder.COLOR_MODE;
		} else {
			throw (new RuntimeException("Format error"));
		}

		readInt(in);// !!crc

		int size = readInt(in);

		byte[] idat = read(in, 4);
		checkEquality(idat, "IDAT".getBytes());

		byte[] data = read(in, size);

		Inflater inflater = new Inflater();
		inflater.setInput(data, 0, size);

		int color;

		try {
			switch (mode) {
			case PNGEncoder.BW_MODE: {
				int bytes = (int) (width / 8);
				if ((width % 8) != 0) {
					bytes++;
				}
				byte colorset;
				byte[] row = new byte[bytes];
				for (int y = 0; y < height; y++) {
					inflater.inflate(new byte[1]);
					inflater.inflate(row);
					for (int x = 0; x < bytes; x++) {
						colorset = row[x];
						for (int sh = 0; sh < 8; sh++) {
							if (x * 8 + sh >= width) {
								break;
							}
							if ((colorset & 0x80) == 0x80) {
								result.setRGB(x * 8 + sh, y, 0xFFFFFFFF);
							} else {
								result.setRGB(x * 8 + sh, y, 0);
							}
							colorset <<= 1;
						}
					}
				}
			}
				break;
			case PNGEncoder.GREYSCALE_MODE: {
				byte[] row = new byte[width];
				for (int y = 0; y < height; y++) {
					inflater.inflate(new byte[1]);
					inflater.inflate(row);
					for (int x = 0; x < width; x++) {
						color = row[x];
						result.setRGB(x, y, (color << 16) + (color << 8)
								+ color);
					}
				}
			}
				break;
			case PNGEncoder.COLOR_MODE: {
				byte[] row = new byte[width * 3];
				for (int y = 0; y < height; y++) {
					inflater.inflate(new byte[1]);
					inflater.inflate(row);
					for (int x = 0; x < width; x++) {
						result.setRGB(x, y, ((row[x * 3 + 0] & 0xff) << 16)
								+ ((row[x * 3 + 1] & 0xff) << 8)
								+ ((row[x * 3 + 2] & 0xff)));
					}
				}
			}
			}
		} catch (DataFormatException e) {
			throw (new RuntimeException("ZIP error" + e));
		}

		readInt(in);// !!crc
		readInt(in);// 0

		byte[] iend = read(in, 4);
		checkEquality(iend, "IEND".getBytes());

		readInt(in);// !!crc
		in.close();

		return (result);
	}
}
