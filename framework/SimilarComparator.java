package framework;

import java.util.ArrayList;

import com.wcohen.ss.Jaro;
import com.wcohen.ss.MongeElkan;

public class SimilarComparator {

	private static final int NEITHER = 0;
	private static final int UP = 1;
	private static final int LEFT = 2;
	private static final int UP_AND_LEFT = 3;

	public static String getLongestCommonSubstring(String a, String b) {
		int n = a.length();
		int m = b.length();
		int S[][] = new int[n + 1][m + 1];
		int R[][] = new int[n + 1][m + 1];
		int ii, jj;

		// It is important to use <=, not <. The next two for-loops are
		// initialization
		for (ii = 0; ii <= n; ++ii) {
			S[ii][0] = 0;
			R[ii][0] = UP;
		}
		for (jj = 0; jj <= m; ++jj) {
			S[0][jj] = 0;
			R[0][jj] = LEFT;
		}

		// This is the main dynamic programming loop that computes the score and
		// backtracking arrays.
		for (ii = 1; ii <= n; ++ii) {
			for (jj = 1; jj <= m; ++jj) {

				if (a.charAt(ii - 1) == b.charAt(jj - 1)) {
					S[ii][jj] = S[ii - 1][jj - 1] + 1;
					R[ii][jj] = UP_AND_LEFT;
				}

				else {
					S[ii][jj] = S[ii - 1][jj - 1] + 0;
					R[ii][jj] = NEITHER;
				}

				if (S[ii - 1][jj] >= S[ii][jj]) {
					S[ii][jj] = S[ii - 1][jj];
					R[ii][jj] = UP;
				}

				if (S[ii][jj - 1] >= S[ii][jj]) {
					S[ii][jj] = S[ii][jj - 1];
					R[ii][jj] = LEFT;
				}
			}
		}

		// The length of the longest substring is S[n][m]
		ii = n;
		jj = m;
		int pos = S[ii][jj] - 1;
		char lcs[] = new char[pos + 1];

		// Trace the backtracking matrix.
		while (ii > 0 || jj > 0) {
			if (R[ii][jj] == UP_AND_LEFT) {
				ii--;
				jj--;
				lcs[pos--] = a.charAt(ii);
			}

			else if (R[ii][jj] == UP) {
				ii--;
			}

			else if (R[ii][jj] == LEFT) {
				jj--;
			}
		}

		return new String(lcs);
	}

	public static boolean isSimilar(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2))
			return true;
		s1 = strip(s1);
		s2 = strip(s2);
		return s1.equals(s2) || s1.contains(s2) || s2.contains(s1);
	}

	// *****************************
	// Compute Levenshtein distance
	// *****************************

	public static int computeLD(String s, String t) {
		int d[][]; // matrix
		int n; // length of s
		int m; // length of t
		int i; // iterates through s
		int j; // iterates through t
		char s_i; // ith character of s
		char t_j; // jth character of t
		int cost; // cost

		// Step 1

		n = s.length();
		m = t.length();
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];

		// Step 2

		for (i = 0; i <= n; i++) {
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) {
			d[0][j] = j;
		}

		// Step 3

		for (i = 1; i <= n; i++) {

			s_i = s.charAt(i - 1);

			// Step 4

			for (j = 1; j <= m; j++) {

				t_j = t.charAt(j - 1);

				// Step 5

				if (s_i == t_j) {
					cost = 0;
				} else {
					cost = 1;
				}

				// Step 6

				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1]
						+ cost);

			}

		}

		// Step 7

		return d[n][m];

	}

	public static String strip(String s1) {
		// trim
		s1 = s1.trim();

		// remove comments [xxx] or (xxx)
		if (s1.matches(".*\\(.*\\)$")) {
			s1 = s1.substring(0, s1.lastIndexOf("("));
		}
		if (s1.matches(".*\\[.*\\]$")) {
			s1 = s1.substring(0, s1.lastIndexOf("["));
		}

		s1 = s1.toLowerCase();

		// rock 'n' roll = rock & roll
		s1 = s1.replace("'n'", "&");
		// rock and roll = rock & roll
		s1 = s1.replace(" and ", " & ");
		// part = pt
		s1 = s1.replace("part", "pt");
		// volume=vol
		s1 = s1.replace("volume", "vol");
		s1 = s1.replace("vol.", "vol");
		// ignore ' a ' and the
		s1 = s1.replace(" a ", "");
		if (s1.startsWith("a "))
			s1 = s1.substring(1);
		s1 = s1.replace(" the ", "");
		if (s1.startsWith("the "))
			s1 = s1.substring(3);
		s1 = s1.replace("!", "").replace("'", "").replace(",", "").replace(".",
				"").replace("?", "").replace("-", "").replace("(", "").replace(
				")", "").replace(" ", "").replace("/", "").replace("\\", "");
		return s1;
	}

	public static double getSimilarityRating(String a, String b) {
		if (a.equalsIgnoreCase(b) || isSimilar(a, b))
			return 1.0;
		a = strip(a);
		b = strip(b);

		ArrayList<Double> scores = new ArrayList<Double>();
		scores.add(new MongeElkan().score(a, b));
		scores.add(new Jaro().score(a, b));
		scores.add(getLongestCommonSubstring(a,b).length()/((a.length() + b.length())/2.0));
		double sumscores = 0;
		for (Double score : scores) {
			sumscores += score;
		}
		double average = sumscores / scores.size();
		return average;
	}

	/**
	 * Computes the minimum of three numbers
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static int min(int a, int b, int c) {
		int mi;

		mi = a;
		if (b < mi) {
			mi = b;
		}
		if (c < mi) {
			mi = c;
		}
		return mi;

	}
}
