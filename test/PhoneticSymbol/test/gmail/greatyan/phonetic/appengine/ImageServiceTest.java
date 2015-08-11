package gmail.greatyan.phonetic.appengine;

import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class ImageServiceTest extends TestCase {

	public void testService() throws IOException {
		SymbolImageService service = SymbolImageService.getService();
		byte[] bytes = service
				.getImageData("opaque /əʊˈpeɪk/ DJ /oʊ-/ DJ US /o'pek/ KK US");
		FileOutputStream out = new FileOutputStream("./output.png");
		try {
			out.write(bytes);
		} finally {
			out.close();
		}
	}
}
