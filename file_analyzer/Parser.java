package file_analyzer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class Parser {
	private ArrayList<String> tokens_;

	public Parser(String format) {
		this.tokens_ = compile(format);
	}

	public ArrayList<String> getTokens() {
		return this.tokens_;
	}

	public Hashtable<String, Object> parse(String data)
			throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {

		data = data.replaceFirst(FileAnalyzerSettings.ROOT + "/", "");
		Hashtable<String, Object> parsed = new Hashtable<String, Object>();
		Iterator<String> itr = tokens_.iterator();
		ParserVariable variable = null;
		int progress = 0;
		while (itr.hasNext()) {
			String next_token = itr.next();
			if (variable != null && isLiteralToken(next_token)) {
				int end = data.indexOf(next_token, progress);
				String var_val = data.substring(progress, end);
				parsed.put(variable.toString(), variable.getType()
						.getConstructor(new Class[] { String.class })
						.newInstance(new Object[] { var_val.trim() }));
				variable = null;
				progress = end + next_token.length();
			} else if (variable == null) {
				// skip '%'
				variable = ParserVariable.valueOf(next_token.substring(1)
						.toUpperCase());
			} else {
				// error - can't have two variables in a row not separated by a
				// delimiter
				System.out.println("error");
			}
		}
		if (variable != null && progress < data.length()) {
			parsed.put(variable.toString(), variable.getType().getConstructor(
					new Class[] { String.class }).newInstance(
					new Object[] { data.substring(progress).trim() }));
		}
		return parsed;
	}

	private boolean isVariableToken(int i) {
		return this.tokens_.get(i).startsWith("%");
	}

	private boolean isVariableToken(String t) {
		return t.startsWith("%");
	}

	private boolean isLiteralToken(int i) {
		return !this.isVariableToken(i);
	}

	private boolean isLiteralToken(String t) {
		return !this.isVariableToken(t);
	}

	private ArrayList<String> compile(String match) {
		ArrayList<String> tokens = new ArrayList<String>();
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
					tokens.add(parse_token);
				} else {
					if (!literal_token.isEmpty()) {
						tokens.add(literal_token);
					}
					read_parse_token = true;
				}
				literal_token = new String();
				parse_token = "%";
			} else // not letter or %
			{
				if (read_parse_token) {
					tokens.add(parse_token);
				}
				literal_token += c;
				read_parse_token = false;
			}

		}
		if (read_parse_token) {
			tokens.add(parse_token);
		} else {
			if (!literal_token.isEmpty()) {
				tokens.add(literal_token);
			}
		}
		return tokens;
	}

	private static enum ParserVariable {
		ARTIST(String.class), ALBUM(String.class), TRACK(Integer.class), TITLE(
				String.class), DISC(Integer.class);
		private Class class_;

		private ParserVariable(Class clazz) {
			this.class_ = clazz;
		}

		public Class<?> getType() {
			return this.class_;
		}

		public String toString() {
			return this.name().toLowerCase();
		}
	}

	public static void main(String[] args) throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Parser p = new Parser(
				"%root/Various Artist/%album/%track - %artist - %title");
		p
				.parse("/media/BACKUP/My Documents/My Music/Various Artist/Pay The Devil/13 - Van Morrison - Once A Day.mp3");
		/*
		 * for (Object o : p.getTokens()) { System.out.println(o); } Hashtable<String,
		 * Object> tokens = p .parse("/media/BACKUP/My Documents/My
		 * Music/Various Artist/Pay The Devil/13 - Van Morrison - Once A
		 * Day.mp3"); for (Entry<String, Object> e : tokens.entrySet()) {
		 * System.out.println(e.getKey() + ":" + e.getValue()); }
		 */
	}
}