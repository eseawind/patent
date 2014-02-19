package cn.edu.scut.qyj.ICTCLASAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.Version;

/**
 * 除了stopword加上ICTCLAS的词性标记集外，完全由StopAnalyzer.java拷贝过来
 */
public final class ICTCLASAnalyzer extends StopwordAnalyzerBase {

	/**
	 * An unmodifiable set containing some common English words that are not
	 * usually useful for searching.
	 */
	public static final CharArraySet ENGLISH_STOP_WORDS_SET;

	static {
		final List<String> stopWords = Arrays.asList("a", "an", "and", "are",
				"as", "at", "be", "but", "by", "for", "if", "in", "into", "is",
				"it", "no", "not", "of", "on", "or", "such", "that", "the",
				"their", "then", "there", "these", "they", "this", "to", "was",
				"will", "with", "n", "nr", "nr1", "nr2", "nrj", "nrf", "ns",
				"nsf", "nt", "nz", "nl", "ng", "t", "tg", "s", "f", "v", "vd",
				"vn", "vshi", "vyou", "vf", "vx", "vi", "vl", "vg", "a", "ad",
				"an", "ag", "al", "b", "bg", "bl", "z", "r", "rr", "rz", "rzt",
				"rzs", "rzv", "ry", "ryt", "rys", "ryv", "rg", "m", "mq", "q",
				"qv", "qt", "d", "p", "pba", "pbei", "c", "cc", "u", "uzhe",
				"ule", "uguo", "ude", "ude1", "ude2", "ude3", "usuo", "udeng", "uyy",
				"udh", "uls", "ujl", "uzhi", "ulian", "uqj", "e", "y", "o",
				"h", "k", "x", "xx", "xu", "w", "wkz", "wky", "wyb", "wyz",
				"wyy", "wj", "ww", "wt", "wd", "wf", "wn", "wm", "ws", "wp",
				"wb", "wh");
		final CharArraySet stopSet = new CharArraySet(Version.LUCENE_CURRENT,
				stopWords, false);
		ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
	}

	/**
	 * Builds an analyzer which removes words in {@link #ENGLISH_STOP_WORDS_SET}
	 * .
	 * 
	 * @param matchVersion
	 *            See <a href="#version">above</a>
	 */
	public ICTCLASAnalyzer(Version matchVersion) {
		this(matchVersion, ENGLISH_STOP_WORDS_SET);
	}

	/**
	 * Builds an analyzer with the stop words from the given set.
	 * 
	 * @param matchVersion
	 *            See <a href="#version">above</a>
	 * @param stopWords
	 *            Set of stop words
	 */
	public ICTCLASAnalyzer(Version matchVersion, CharArraySet stopWords) {
		super(matchVersion, stopWords);
	}

	/**
	 * Builds an analyzer with the stop words from the given file.
	 * 
	 * @see WordlistLoader#getWordSet(Reader, Version)
	 * @param matchVersion
	 *            See <a href="#version">above</a>
	 * @param stopwordsFile
	 *            File to load stop words from
	 */
	public ICTCLASAnalyzer(Version matchVersion, File stopwordsFile)
			throws IOException {
		this(matchVersion, loadStopwordSet(stopwordsFile, matchVersion));
	}

	/**
	 * Builds an analyzer with the stop words from the given reader.
	 * 
	 * @see WordlistLoader#getWordSet(Reader, Version)
	 * @param matchVersion
	 *            See <a href="#version">above</a>
	 * @param stopwords
	 *            Reader to load stop words from
	 */
	public ICTCLASAnalyzer(Version matchVersion, Reader stopwords)
			throws IOException {
		this(matchVersion, loadStopwordSet(stopwords, matchVersion));
	}

	/**
	 * Creates {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	 * used to tokenize all the text in the provided {@link Reader}.
	 * 
	 * @return {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	 *         built from a {@link LowerCaseTokenizer} filtered with
	 *         {@link StopFilter}
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		final Tokenizer source = new LowerCaseTokenizer(matchVersion, reader);
		return new TokenStreamComponents(source, new ICTCLASFilter(
				matchVersion, source, stopwords));
	}
}