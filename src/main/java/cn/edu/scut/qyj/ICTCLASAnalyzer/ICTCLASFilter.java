package cn.edu.scut.qyj.ICTCLASAnalyzer;

import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.util.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

/**
 * 完全由StopFilter.java拷贝过来
 */
public final class ICTCLASFilter extends FilteringTokenFilter {

	private final CharArraySet stopWords;
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

	/**
	 * Constructs a filter which removes words from the input TokenStream that
	 * are named in the Set.
	 * 
	 * @param matchVersion
	 *            Lucene version to enable correct Unicode 4.0 behavior in the
	 *            stop set if Version > 3.0. See <a href="#version">above</a>
	 *            for details.
	 * @param in
	 *            Input stream
	 * @param stopWords
	 *            A {@link CharArraySet} representing the stopwords.
	 * @see #makeStopSet(Version, java.lang.String...)
	 */
	public ICTCLASFilter(Version matchVersion, TokenStream in,
			CharArraySet stopWords) {
		super(matchVersion, in);
		this.stopWords = stopWords;
	}

	/**
	 * Builds a Set from an array of stop words, appropriate for passing into
	 * the StopFilter constructor. This permits this stopWords construction to
	 * be cached once when an Analyzer is constructed.
	 * 
	 * @param matchVersion
	 *            Lucene version to enable correct Unicode 4.0 behavior in the
	 *            returned set if Version > 3.0
	 * @param stopWords
	 *            An array of stopwords
	 * @see #makeStopSet(Version, java.lang.String[], boolean) passing false to
	 *      ignoreCase
	 */
	public static CharArraySet makeStopSet(Version matchVersion,
			String... stopWords) {
		return makeStopSet(matchVersion, stopWords, false);
	}

	/**
	 * Builds a Set from an array of stop words, appropriate for passing into
	 * the StopFilter constructor. This permits this stopWords construction to
	 * be cached once when an Analyzer is constructed.
	 * 
	 * @param matchVersion
	 *            Lucene version to enable correct Unicode 4.0 behavior in the
	 *            returned set if Version > 3.0
	 * @param stopWords
	 *            A List of Strings or char[] or any other toString()-able list
	 *            representing the stopwords
	 * @return A Set ({@link CharArraySet}) containing the words
	 * @see #makeStopSet(Version, java.lang.String[], boolean) passing false to
	 *      ignoreCase
	 */
	public static CharArraySet makeStopSet(Version matchVersion,
			List<?> stopWords) {
		return makeStopSet(matchVersion, stopWords, false);
	}

	/**
	 * Creates a stopword set from the given stopword array.
	 * 
	 * @param matchVersion
	 *            Lucene version to enable correct Unicode 4.0 behavior in the
	 *            returned set if Version > 3.0
	 * @param stopWords
	 *            An array of stopwords
	 * @param ignoreCase
	 *            If true, all words are lower cased first.
	 * @return a Set containing the words
	 */
	public static CharArraySet makeStopSet(Version matchVersion,
			String[] stopWords, boolean ignoreCase) {
		CharArraySet stopSet = new CharArraySet(matchVersion, stopWords.length,
				ignoreCase);
		stopSet.addAll(Arrays.asList(stopWords));
		return stopSet;
	}

	/**
	 * Creates a stopword set from the given stopword list.
	 * 
	 * @param matchVersion
	 *            Lucene version to enable correct Unicode 4.0 behavior in the
	 *            returned set if Version > 3.0
	 * @param stopWords
	 *            A List of Strings or char[] or any other toString()-able list
	 *            representing the stopwords
	 * @param ignoreCase
	 *            if true, all words are lower cased first
	 * @return A Set ({@link CharArraySet}) containing the words
	 */
	public static CharArraySet makeStopSet(Version matchVersion,
			List<?> stopWords, boolean ignoreCase) {
		CharArraySet stopSet = new CharArraySet(matchVersion, stopWords.size(),
				ignoreCase);
		stopSet.addAll(stopWords);
		return stopSet;
	}

	/**
	 * Returns the next input Token whose term() is not a stop word.
	 */
	@Override
	protected boolean accept() {
		return !stopWords.contains(termAtt.buffer(), 0, termAtt.length());
	}
}