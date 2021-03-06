package org.sfm.csv.impl.cellreader;

import org.sfm.csv.impl.ParsingContext;
import org.sfm.csv.impl.ParsingException;


public final class IntegerCellValueReaderImpl implements IntegerCellValueReader {

	private final static char CZERO = '0';
	private final static char CNINE = '9';
	private final static char CNEGSIGN = '-';

	@Override
	public Integer read(char[] chars, int offset, int length, ParsingContext parsingContext) {
		if (length == 0) return null;
		return readInt(chars, offset, length, parsingContext);
	}

	@Override
	public int readInt(char[] chars, int offset, int length, ParsingContext parsingContext) {
		return parseInt(chars, offset, length);
	}

	public static int parseInt(char[] chars, int offset, int length) {
		int n = 0;
		boolean negative = false;
		for(int i = offset; i < offset + length; i++) {
			char b = chars[i];
			if (b >= CZERO && b <= CNINE) {
				n  = n * 10 +  chars[i] - CZERO;
			} else {
				if (b == CNEGSIGN && i == offset) {
					negative = true;
				} else {
					throw new ParsingException("Cannot parse " + new String(chars, offset, length) + " as an int");
				}
			}
		}
		if (negative) {
			n = 0 - n;
		}
		return n;
	}

    @Override
    public String toString() {
        return "IntegerCellValueReaderImpl{}";
    }
}
