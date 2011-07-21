package gmail.greatyan.phonetic;

import java.util.Arrays;

public class Symbols {

	static int[][] CODE_RANGES = new int[][] { new int[] { 0x1D00, 0x1D7F }, // Phonetic
																				// Extensions
			new int[] { 0x1D80, 0x1DBF }, // Phonetic Extensions Supplement
			new int[] { 0x0250, 0x02AF }, // IPA Extensions
			new int[] { 0x0270, 0x209F }, // Superscripts and Subscripts
			new int[] { 0x02B0, 0x02FF }, // Spacing Modifier Letters
			new int[] { 0xA701, 0xA71F } // Modifier Tone Letters
	};

	static int SYMBOL_COUNT;
	static char[] SYMBOLS;
	static {
		for (int[] range : CODE_RANGES) {
			SYMBOL_COUNT += range[1] - range[0];
		}
		SYMBOLS = new char[SYMBOL_COUNT];
		int index = 0;
		for (int[] range : CODE_RANGES) {
			for (int i = range[0]; i < range[1]; i++) {
				SYMBOLS[index] = (char)i;
				index++;
			}
		}
	}

	static public char[] getSymbols() {
		return SYMBOLS;
	}

	static public int getSymbolCount() {
		return SYMBOLS.length;
	}

	static public int getSymbolIndex(char symbol) {
		int index = Arrays.binarySearch(SYMBOLS, symbol);
		if (index < 0) {
			return -1;
		}
		return index;
	}

	static public char getSymbolCode(int index) {
		if (index < 0 && index > SYMBOLS.length) {
			return ' ';
		}
		return SYMBOLS[index];
	}
}
