package gmail.greatyan.phonetic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SymbolImageDescription {
	
	static final int SYMBOLS_PER_LINE = 32;
	
	public int leading;
	public int ascent;
	public int descent;
	public int maxAscent;
	public int maxDescent;
	public int maxAdvance;
	public int[] widths;
	
	public void write(OutputStream stream) throws IOException {
		DataOutputStream out = new DataOutputStream(stream);
		out.writeInt(maxAscent);

		out.writeInt(maxDescent);
		out.writeInt(maxAdvance);
		out.writeInt(ascent);
		out.writeInt(descent);
		out.writeInt(leading);
		out.writeInt(widths.length);
		for (int i = 0; i < widths.length; i++) {
			out.writeInt(widths[i]);
		}
		out.flush();
	}

	public void read(InputStream stream) throws IOException {
		DataInputStream in = new DataInputStream(stream);
		maxAscent = in.readInt();
		maxDescent = in.readInt();
		maxAdvance = in.readInt();
		ascent = in.readInt();
		descent = in.readInt();
		leading = in.readInt();
		int size = in.readInt();
		widths = new int[size];
		for (int i = 0; i < size; i++) {
			widths[i] = in.readInt();
		}
	}

	static public SymbolImageDescription load() throws IOException {
		InputStream stream = SymbolImageDescription.class
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

}