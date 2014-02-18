package cn.edu.scut.patent;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class DiyAnalyzer extends Analyzer {

	private Set stopWords;

	public static final String[] CHINESE_ENGLISH_STOP_WORDS = { "a", "an",
			"and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
			"into", "is", "it", "no", "not", "of", "on", "or", "s", "such",
			"t", "that", "the", "their", "then", "there", "these", "they",
			"this", "to", "was", "will", "with", "我", "我们" };

	public DiyAnalyzer() {
		this.stopWords = StopFilter.makeStopSet(Version.LUCENE_46,
				CHINESE_ENGLISH_STOP_WORDS);
	}

	public DiyAnalyzer(String[] stopWordList) {
		this.stopWords = StopFilter
				.makeStopSet(Version.LUCENE_46, stopWordList);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		// TODO Auto-generated method stub
		Tokenizer source = new StandardTokenizer(Version.LUCENE_46, reader);
		TokenStream filter = new StandardFilter(Version.LUCENE_46, source);
		filter = new LowerCaseFilter(Version.LUCENE_46, filter);
		// filter = new StopFilter(filter, stopWords);
		filter = new PorterStemFilter(filter);
		return new TokenStreamComponents(source, filter);
	}

	/**
	 * 打印分词结果
	 * 
	 * @throws IOException
	 */
	private static void printKeyWords(Analyzer analyzer, String text)
			throws IOException {
		System.out.println("当前使用的分词器：" + analyzer.getClass().getSimpleName());
		// 将一个字符串创建成Token流
		TokenStream tokenStream2 = analyzer.tokenStream("contents",
				new StringReader(text));
		tokenStream2.reset(); // 必须要reset，不然会抛出NullPointerException
		// 保存相应词汇
		CharTermAttribute charTermAttribute2 = tokenStream2
				.addAttribute(CharTermAttribute.class);
		while (tokenStream2.incrementToken()) {
			System.out.print("[" + charTermAttribute2 + "]");
		}
		tokenStream2.close();// 必须要close，不然会报错
		System.out.println();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 好像英文的结束符号标点.,StandardAnalyzer不能识别
		String text = new String("我爱中国,我爱天津大学!I love China!Tianjin is a City");
		Analyzer analyzer = new DiyAnalyzer();
		try {
			printKeyWords(analyzer, text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
