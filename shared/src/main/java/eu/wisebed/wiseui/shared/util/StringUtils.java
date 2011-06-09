package eu.wisebed.wiseui.shared.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class StringUtils {

	/**
	 * Replaces the non-printable ASCII characters with readable counterparts in square brackets, e.g. \0x00 -> [NUL].
	 *
	 * @param str the String in which to replace the characters
	 *
	 * @return a printable String
	 */
	public static String replaceNonPrintableAsciiCharacters(String str) {
		return str
				.replaceAll("\\x00", "[NUL]")
				.replaceAll("\\x01", "[SOH]")
				.replaceAll("\\x02", "[STX]")
				.replaceAll("\\x03", "[ETX]")
				.replaceAll("\\x04", "[EOT]")
				.replaceAll("\\x05", "[ENQ]")
				.replaceAll("\\x06", "[ACK]")
				.replaceAll("\\x07", "[BEL]")
				.replaceAll("\\x08", "[BS]")
				.replaceAll("\\x09", "[TAB]")
				.replaceAll("\\x0a", "[LF]")
				.replaceAll("\\x0b", "[VT]")
				.replaceAll("\\x0c", "[FF]")
				.replaceAll("\\x0d", "[CR]")
				.replaceAll("\\x0e", "[SO]")
				.replaceAll("\\x0f", "[SI]")
				.replaceAll("\\x10", "[DLE]")
				.replaceAll("\\x11", "[DC1]")
				.replaceAll("\\x12", "[DC2]")
				.replaceAll("\\x13", "[DC3]")
				.replaceAll("\\x14", "[DC4]")
				.replaceAll("\\x15", "[NACK]")
				.replaceAll("\\x16", "[SYN]")
				.replaceAll("\\x17", "[ETB]")
				.replaceAll("\\x18", "[CAN]")
				.replaceAll("\\x19", "[EM]")
				.replaceAll("\\x1a", "[SUB]")
				.replaceAll("\\x1b", "[ESC]")
				.replaceAll("\\x1c", "[FS]")
				.replaceAll("\\x1d", "[GS]")
				.replaceAll("\\x1e", "[RS]")
				.replaceAll("\\x1f", "[US]")
				.replaceAll("\\x7f", "[DEL]");
	}



	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexString(char tmp) {
		return toHexString((byte) tmp);
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexString(byte[] tmp) {
		return toHexString(tmp, 0, tmp.length);
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexString(byte tmp) {
		return "0x" + Integer.toHexString(tmp & 0xFF);
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexString(byte[] tmp, int offset) {
		return toHexString(tmp, offset, tmp.length - offset);
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexString(byte[] tmp, int offset, int length) {
		StringBuffer s = new StringBuffer();
		for (int i = offset; i < offset + length; ++i) {
			if (s.length() > 0) {
				s.append(' ');
			}
			s.append("0x");
			s.append(Integer.toHexString(tmp[i] & 0xFF));
		}
		return s.toString();
	}

	// -------------------------------------------------------------------------

	/**
	 * @param tmp
	 *
	 * @return
	 */
	public static String toASCIIString(byte[] tmp) {
		StringBuffer sb = new StringBuffer("");

		for (byte b : tmp) {
			if (b == 0x0D) {
				sb.append("<CR>");
			} else if (b == 0x0A) {
				sb.append("<LF>");
			} else {
				char chr = (char) b;
				sb.append(chr);
			}
		}

		return sb.toString();
	}

	/**
	 *
	 */
	public static String toString(short[] l, int offset, int length) {
		LinkedList<Short> ll = new LinkedList<Short>();
		for (int i = offset; i < offset + length; ++i) {
			ll.add(l[i]);
		}

		return toString(ll, ", ");
	}

	/**
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static String toString(Collection l, String divider) {
		StringBuffer b = new StringBuffer();

		if (l == null) {
			return "<null>";
		}

		for (Object o : l) {
			String t = o != null ? o.toString() : "{null}";
			if (b.length() > 0) {
				b.append(divider);
			}

			b.append(t);
		}

		return b.toString().trim();
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexStringReverseDirection(byte[] tmp) {
		return toHexStringReverseDirection(tmp, 0, tmp.length);
	}

	// -------------------------------------------------------------------------

	/**
	 *
	 */
	public static String toHexStringReverseDirection(byte[] tmp, int offset, int length) {
		byte reverse[] = new byte[length];

		for (int i = 0; i < length; ++i) {
			reverse[i] = tmp[offset + length - i - 1];
		}

		return toHexString(reverse);
	}

	public static List<String> parseLines(String str) {
		return Arrays.asList(str.split("[\\r\\n]+"));
	}

	public static String toString(Collection<? extends Object> list) {
		if (list == null) {
			return "null";
		}

		return Arrays.toString(list.toArray());
	}


	public static Long parseHexOrDecLong(String value) {
		return value.startsWith("0x") ? Long.parseLong(value.substring(2), 16) : Long.parseLong(value, 10);
	}

	public static Long parseHexOrDecLongFromUrn(String urn) {
		String[] arr = urn.split(":");
		String suffix = arr[arr.length - 1];
		return parseHexOrDecLong(suffix);
	}

	public static String parseHexOrDecLongUrnSuffix(String value) {
		String[] valueAsArray = value.split(":");
		String suffix = valueAsArray[valueAsArray.length - 1];
		return getPrefixAsStringFromStringArray(valueAsArray) + ":" + (suffix.startsWith("0x") ?
				Long.parseLong(suffix.substring(2), 16) : Long.parseLong(suffix, 10));
	}

	private static String getPrefixAsStringFromStringArray(String[] value) {
		StringBuffer result = new StringBuffer();
		if (value.length > 0) {
			result.append(value[0]);
			for (int i = 1; i < value.length - 1; i++) {
				result.append(":");
				result.append(value[i]);
			}
		}
		return result.toString();
	}

	public static boolean assertHexOrDecLongValue(String value) {
		try {
			parseHexOrDecLong(value);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static boolean hasHexOrDecLongUrnSuffix(String value) {
		String[] arr = value.split(":");
		String suffix = arr[arr.length - 1];
		return assertHexOrDecLongValue(suffix);
	}

	/**
	 * Asserts that the given String {@code value} is a URN that has a suffix which can be parsed as a long value, either
	 * hex-encoded (starting with 0x) or decimal-encoded.
	 *
	 * @param value
	 *
	 * @throws RuntimeException if suffix can not be parse as long value
	 */
	public static void assertHexOrDecLongUrnSuffix(String value) throws RuntimeException {
		if (!StringUtils.hasHexOrDecLongUrnSuffix(value)) {
			throw new RuntimeException("Suffix of {" + value + "} has to be an integer-value!");
		}
	}

	public static String getUrnSuffix(String urn) {
		String[] arr = urn.split(":");
		return arr[arr.length - 1];
	}

	/**
	 * @param i
	 *
	 * @return
	 */
	public static String toHexString(int i) {
		String tmp = "";
		if (i > 0xFF) {
			tmp += toHexString((byte) (i >> 8 & 0xFF)) + " ";
		} else {
			tmp += "    ";
		}
		tmp += toHexString((byte) (i & 0xFF));
		return tmp;
	}
}
