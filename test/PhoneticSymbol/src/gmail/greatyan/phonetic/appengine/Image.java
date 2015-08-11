package gmail.greatyan.phonetic.appengine;

public class Image {

	int width;
	int height;
	byte[] data;

	Image(int width, int height) {
		this.width = width;
		this.height = height;
		this.data = new byte[width * 4 * height];
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[] getImageData() {
		return data;
	}

	public void setRGB(int x, int y, int color) {
		int index = (y * width + x) * 4;
		data[index++] = (byte) ((color & 0xFF000000) >> 24);
		data[index++] = (byte) ((color & 0x00FF0000) >> 16);
		data[index++] = (byte) ((color & 0x0000FF00) >> 8);
		data[index++] = (byte) (color & 0x000000FF);
	}

	public int getRGB(int x, int y) {
		int index = (y * width + x) * 4;
		return (((int) data[index++]) >> 24) | (((int) data[index++]) >> 16)
				| (((int) data[index++]) >> 8) | ((int) data[index++]);
	}

	public void copy(Image src, int sx, int sy, int dx, int dy, int w, int h) {

		w = Math.min(w, width - dx);
		w = Math.min(w, src.getWidth() - sx);
		h = Math.min(h, height - dy);
		h = Math.min(h, src.getHeight() - sy);
		if (w > 0 && h > 0) {
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					int color = src.getRGB(sx + x, sy + y);
					setRGB(dx + x, dy + y, color);
				}
			}
		}
	}
}
