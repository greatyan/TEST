package gmail.greatyan.phonetic.appengine;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class ImageTest extends TestCase {

	public void testImage() throws IOException {
		Image image = new Image(10, 10);
		for (int i = 0; i < 10; i++) {
			image.setRGB(i, i, 0xFFFFFF);
			image.setRGB(9 - i, i, 0xFFFFFF);
		}
		PNGEncoder encoder = new PNGEncoder(PNGEncoder.BW_MODE);
		byte[] bytes = encoder.encode(image);

		FileOutputStream out = new FileOutputStream("./output.png");
		try {
			out.write(bytes);
		} finally {
			out.close();
		}
		PNGDecoder decoder = new PNGDecoder();
		image = decoder.decode(new ByteArrayInputStream(bytes));

		for (int i = 0; i < 10; i++) {
			int color = image.getRGB(i, i);
			assertEquals(color, 0xFFFFFF);
			color = image.getRGB(10 - i, i);
			assertEquals(color, 0xFFFFFF);
		}

	}
}
