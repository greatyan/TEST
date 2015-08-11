package gmail.greatyan.phonetic.appengine;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class PNGEncoder {

	/** black and white image mode. */
	public static final byte BW_MODE = 0;
	/** grey scale image mode. */
	public static final byte GREYSCALE_MODE = 1;
	/** full color image mode. */
	public static final byte COLOR_MODE = 2;

	private byte mode;

	/**
	 * public constructor of PNGEncoder class with greyscale mode by default.
	 * 
	 * @param out
	 *            output stream for PNG image format to write into
	 */
	public PNGEncoder() {
		this(GREYSCALE_MODE);
	}

	/**
	 * public constructor of PNGEncoder class.
	 * 
	 * @param out
	 *            output stream for PNG image format to write into
	 * @param mode
	 *            BW_MODE, GREYSCALE_MODE or COLOR_MODE
	 */
	public PNGEncoder(byte mode) {
		if (mode < 0 || mode > 2)
			throw new IllegalArgumentException("Unknown color mode");
		this.mode = mode;
	}

	private class CRCOutputStream extends FilterOutputStream {

		private CRC32 crc;

		public CRCOutputStream(OutputStream output) {
			super(output);
			crc = new CRC32();
		}

		public CRC32 getCRC() {
			return crc;
		}

		public void write(byte[] b, int off, int len) throws IOException{
			out.write(b, off, len);
			crc.update(b, off, len);
		}

		public void write(int b) throws IOException {
			out.write(b);
			crc.update(b);
		}
	}

	private void write(OutputStream out, int i) throws IOException {
		byte b[] = { (byte) ((i >> 24) & 0xff), (byte) ((i >> 16) & 0xff),
				(byte) ((i >> 8) & 0xff), (byte) (i & 0xff) };
		write(out, b);
	}

	private void write(OutputStream out, byte b[]) throws IOException {
		out.write(b);
	}

	/**
	 * main encoding method (stays blocked till encoding is finished).
	 * 
	 * @param image
	 *            BufferedImage to encode
	 * @throws IOException
	 *             IOException
	 */
	public byte[] encode(Image image) throws IOException {

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		CRCOutputStream out = new CRCOutputStream(buffer);
		final byte id[] = { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13 };
		write(out, id);
		out.crc.reset();
		int width = image.getWidth();
		int height = image.getHeight();
		write(out, "IHDR".getBytes());
		write(out, width);
		write(out, height);
		byte head[] = null;
		switch (mode) {
		case BW_MODE:
			head = new byte[] { 1, 0, 0, 0, 0 };
			break;
		case GREYSCALE_MODE:
			head = new byte[] { 8, 0, 0, 0, 0 };
			break;
		case COLOR_MODE:
			head = new byte[] { 8, 2, 0, 0, 0 };
			break;
		}
		write(out, head);
		write(out, (int) out.getCRC().getValue());
		ByteArrayOutputStream compressed = new ByteArrayOutputStream(65536);
		BufferedOutputStream bos = new BufferedOutputStream(
				new DeflaterOutputStream(compressed, new Deflater(9)));
		int pixel;
		int color;
		int colorset;
		switch (mode) {
		case BW_MODE:
			int rest = width % 8;
			int bytes = width / 8;
			for (int y = 0; y < height; y++) {
				bos.write(0);
				for (int x = 0; x < bytes; x++) {
					colorset = 0;
					for (int sh = 0; sh < 8; sh++) {
						pixel = image.getRGB(x * 8 + sh, y);
						color = ((pixel >> 16) & 0xff);
						color += ((pixel >> 8) & 0xff);
						color += (pixel & 0xff);
						colorset <<= 1;
						if (color >= 3 * 128)
							colorset |= 1;
					}
					bos.write((byte) colorset);
				}
				if (rest > 0) {
					colorset = 0;
					for (int sh = 0; sh < width % 8; sh++) {
						pixel = image.getRGB(bytes * 8 + sh, y);
						color = ((pixel >> 16) & 0xff);
						color += ((pixel >> 8) & 0xff);
						color += (pixel & 0xff);
						colorset <<= 1;
						if (color >= 3 * 128)
							colorset |= 1;
					}
					colorset <<= 8 - rest;
					bos.write((byte) colorset);
				}
			}
			break;
		case GREYSCALE_MODE:
			for (int y = 0; y < height; y++) {
				bos.write(0);
				for (int x = 0; x < width; x++) {
					pixel = image.getRGB(x, y);
					color = ((pixel >> 16) & 0xff);
					color += ((pixel >> 8) & 0xff);
					color += (pixel & 0xff);
					bos.write((byte) (color / 3));
				}
			}
			break;
		case COLOR_MODE:
			for (int y = 0; y < height; y++) {
				bos.write(0);
				for (int x = 0; x < width; x++) {
					pixel = image.getRGB(x, y);
					bos.write((byte) ((pixel >> 16) & 0xff));
					bos.write((byte) ((pixel >> 8) & 0xff));
					bos.write((byte) (pixel & 0xff));
				}
			}
			break;
		}
		bos.close();
		write(out, compressed.size());
		out.crc.reset();
		write(out, "IDAT".getBytes());
		write(out, compressed.toByteArray());
		write(out, (int) out.crc.getValue());
		write(out, 0);
		out.crc.reset();
		write(out, "IEND".getBytes());
		write(out, (int) out.crc.getValue());
		out.close();
		return buffer.toByteArray();
	}
}
