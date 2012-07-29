package collection_info;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import file_analyzer.FileAnalyzerSettings;

public class Parser {
	private ArrayList<?> tokens_;

	public Parser(String format) {
		this.tokens_ = compile(format);
	}

	public ArrayList<?> getTokens() {
		return this.tokens_;
	}

	public Hashtable<String, Object> parse(String data)
			throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Hashtable<String, Object> parsed = new Hashtable<String, Object>();

		int progress = 0;
		Iterator<?> itr = tokens_.iterator();
		while (itr.hasNext()) {
			Object o = itr.next();
			if (o instanceof ParseToken) {
				ParseToken t = (ParseToken) o;
				String regex = t.getPattern();
				Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(data);
				if (m.find(progress)) {
					int s = m.start();
					int e = m.end();
					parsed.put(t.getToken(), t.getType().getConstructor(
							new Class[] { String.class }).newInstance(
							new Object[] { data.substring(s, e).trim() }));
					progress = e;
				} else {
					//System.out.println("not found");
				}
			} else {
				Pattern p = Pattern.compile("\\Q" + o + "\\E",Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(data);
				System.out.println(o);
				if (m.find(progress)) {
					int e = m.end();
					progress = e;

				} else {
					//System.out.println("not found");
				}
			}
			// System.out.println("remaining: " + data.substring(progress));
		}
		return parsed;
	}

	private ArrayList compile(String match) {
		match = Parser.regexSafe(match);
		ArrayList tokens = new ArrayList();
		boolean read_parse_token = false;
		String parse_token = new String();
		String literal_token = new String();
		for (int i = 0; i < match.length(); i++) {
			Character c = match.charAt(i);
			if (Character.isLetter(c)) {
				if (read_parse_token) {
					parse_token += c;
				} else {
					literal_token += c;
				}
			} else if (c == '%') {
				if (read_parse_token) {
					tokens.add(ParseToken.valueOf(parse_token.toUpperCase()));
				} else {
					if (!literal_token.isEmpty()) {
						tokens.add(literal_token);
					}
					read_parse_token = true;
				}
				literal_token = new String();
				parse_token = new String();
			} else // not letter or %
			{
				if (read_parse_token) {
					tokens.add(ParseToken.valueOf(parse_token.toUpperCase()));
				}
				literal_token += c;
				read_parse_token = false;
			}

		}
		if (read_parse_token) {
			tokens.add(ParseToken.valueOf(parse_token.toUpperCase()));
		} else {
			if (!literal_token.isEmpty()) {
				tokens.add(literal_token);
			}
		}
		return tokens;
	}

	public static String regexSafe(String s) {
		s = s.trim();
		s = s.replace(".", "[\\.]");
		// s = s.replace(" ", "[\\s]");
		return s;
	}

	public static enum ParseToken {
		ARTIST("artist", "[^/]+", true, String.class), TITLE("title", "[^/]+",
				true, String.class), ALBUM("album", "[^/]+", true, String.class), TRACK(
				"track", "[\\d][\\d]*", false, Integer.class), DISC("disc",
				"[\\d][\\d]*", false, Integer.class), ROOT("root", "\\Q"
				+ regexSafe(FileAnalyzerSettings.ROOT) + "\\E", true, String.class);
		// FORMAT("format", "[\\.][[mp3]|[m4a]]", false,String.class);
		private String token_;

		private boolean spaces_;

		private Class class_;

		private String regex_;

		ParseToken(String token, String regex, boolean spaces, Class clazz) {
			token_ = token;
			class_ = clazz;
			spaces_ = spaces;
			regex_ = regex;
		}

		public void setPattern(String pattern) {
			regex_ = pattern;
		}

		public String getPattern() {
			return regex_;
		}

		public boolean mayHaveSpaces() {
			return spaces_;
		}

		public Class<?> getType() {
			return class_;
		}

		public String getToken() {
			return token_;
		}

		public String toString() {
			return token_;
		}
	}

	public static void main(String[] args) throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Parser p = new Parser("%root/Various Artist/%album/%track - %artist - %title");
		for (Object o : p.getTokens()) {
			System.out.println(o);
		}
		Hashtable<String, Object> tokens = p
				.parse("/media/BACKUP/My Documents/My Music/Various Artist/Pay The Devil/13 - Van Morrison - Once A Day.mp3");
		for (Entry<String, Object> e : tokens.entrySet()) {
			System.out.println(e.getKey() + ":" + e.getValue());
		}

	}
}