package cn.edu.scut.patent;

import helper.Constants;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import net.paoding.analysis.analyzer.PaodingAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFTextStripper;
import org.wltea.analyzer.lucene.IKAnalyzer;
import ICTCLAS2014.Nlpir;
import cn.edu.scut.qyj.ICTCLASAnalyzer.ICTCLASAnalyzer;
import cn.edu.scut.qyj.ICTCLASAnalyzer.ICTCLASAnalyzer;

/**
 * @author qyj
 * @version February 6, 2014
 */
public class IndexAndSearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			index();
			// search("孙皓", "contents");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * 索引
	 * 
	 * @throws Exception
	 */
	private static void index() throws Exception {
		// 索引文件(夹)的位置
		File fileDir = new File(Constants.FILE_DIR_STRING);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		// 存放索引文件的位置
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, iwc);
		// 索引开始的时间
		long startTime = new Date().getTime();
		Document document = getPDF(fileDir);

		// 打印分词结果
		TextField textfield = (TextField) document.getField("contents");
		String result = textfield.stringValue();
		// printKeyWords(analyzer, result);
		// testing analyzer
		{
			String text = "据悉，质检总局已将       最新 good   有关情况再次G60Q80通报美方，要求美方123142.11加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";
			Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_46);
			printKeyWords(standardAnalyzer, text);
			Analyzer stopAnalyzer = new StopAnalyzer(Version.LUCENE_46);
			printKeyWords(stopAnalyzer, text);
			Analyzer cjkAnalyzer = new CJKAnalyzer(Version.LUCENE_46);
			printKeyWords(cjkAnalyzer, text);
			Analyzer chineseAnalyzer = new ChineseAnalyzer();
			printKeyWords(chineseAnalyzer, text);
			Analyzer paodingAnalyzer = new PaodingAnalyzer();
			printKeyWords(paodingAnalyzer, text);
			Analyzer ikAnalyzer = new IKAnalyzer();
			printKeyWords(ikAnalyzer, text);
			Analyzer smartChineseAnalyzer = new SmartChineseAnalyzer(
					Version.LUCENE_46);
			printKeyWords(smartChineseAnalyzer, text);
			Analyzer ictclasAnalyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
			printKeyWords(ictclasAnalyzer,
					Nlpir.doNlpirString(text, null, null));
		}

		indexWriter.addDocument(document);
		// 提交事务
		indexWriter.commit();
		indexWriter.close();
		// 索引结束的时间
		long endTime = new Date().getTime();
		System.out.println("一共花费了" + (endTime - startTime) + "毫秒完成索引！");
	}

	/**
	 * 打印分词结果
	 * 
	 * @throws IOException
	 */
	private static void printKeyWords(Analyzer analyzer, String text)
			throws IOException {
		System.out.println("当前使用的分词器：" + analyzer.getClass().getSimpleName());
		// TokenStream tokenStream = analyzer.tokenStream("contents",
		// new StringReader(text));
		// tokenStream.reset(); // 必须要reset，不然会抛出NullPointerException
		// tokenStream.addAttribute(CharTermAttribute.class);
		//
		// // 打印方法一
		// System.out.println("打印方法一");
		// while (tokenStream.incrementToken()) {
		// CharTermAttribute charTermAttribute = tokenStream
		// .getAttribute(CharTermAttribute.class);
		// System.out.println(new String(charTermAttribute.buffer()));
		// }
		// tokenStream.close();// 必须要close，不然会报错
		//
		// // 打印方法二
		// System.out.println("打印方法二");
		// // 将一个字符串创建成Token流
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
	 * 查询
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private static void search(String queryString, String field)
			throws IOException {
		TopDocs td = null;
		Query query = null;
		Directory directory = FSDirectory.open(new File(
				Constants.INDEX_DIR_STRING));
		IndexReader indexreader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(indexreader);
		Analyzer analyzer = new ICTCLASAnalyzer(Version.LUCENE_46);
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		try {
			QueryParser queryParser = new QueryParser(Version.LUCENE_46, field,
					analyzer);
			query = queryParser.parse(queryString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (searcher != null) {
			td = searcher.search(query, 1000);
			if (td.totalHits > 0) {
				System.out.println("找到：" + td.totalHits + "个结果！");
				ScoreDoc[] sds = td.scoreDocs;
				for (ScoreDoc sd : sds) {
					Document d = searcher.doc(sd.doc);
					System.out.println(d.get("path") + ":[" + d.get("path")
							+ "]");
				}
			} else {
				System.out.println("没有找到任何结果！");
			}
		}
	}

	/**
	 * 获取PDF文档 请先把已加密的PDF文档解密 采用fontbox-1.8.4和pdfbox-1.8.4支持
	 * 
	 * @throws Exception
	 */
	private static Document getPDF(File pdf) throws Exception {
		// Document document = LucenePDFDocument.getDocument(pdf);
		String pdfpath = pdf.getAbsolutePath();
		// 创建输入流读取pdf文件
		String title = pdf.getName();
		String result = "";
		FileInputStream is = null;
		PDDocument doc = null;
		try {
			is = new FileInputStream(pdf);
			PDFParser parser = new PDFParser(is);
			parser.parse();
			doc = parser.getPDDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			result = stripper.getText(doc);

			// 获取图片
			// getImagesInPDF(doc, title);

			// 转化图片
			transferPDFToImages(doc, title, Constants.TYPE);

			// 测试
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (doc != null) {
				try {
					doc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Document document = new Document();
		document.add(new TextField("title", title, Field.Store.YES));
		document.add(new TextField("contents", result, Field.Store.YES));
		document.add(new TextField("path", pdfpath, Field.Store.YES));
		return document;
	}

	/**
	 * 获取PDF文档当中的图片
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "deprecation", "unused" })
	private static void getImagesInPDF(PDDocument doc, String prefix)
			throws IOException {
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		Iterator<PDPage> iter = pages.iterator();
		while (iter.hasNext()) {
			System.out.println("@@@@@@@@@@");
			PDPage page = (PDPage) iter.next();
			PDResources resources = page.getResources();
			Map<String, PDXObjectImage> images = resources.getImages();
			if (images != null) {
				System.out.println("#########");
				Iterator<String> imageIter = images.keySet().iterator();
				while (imageIter.hasNext()) {
					System.out.println("%%%%%%%%%%");
					String key = (String) imageIter.next();
					PDXObjectImage image = (PDXObjectImage) images.get(key);
					// String name = getUniqueFileName( key,
					// image.getSuffix() );
					// System.out.println( "Writing image:" + name );
					// PDStream pdfstream=image.getPDStream();
					String name = prefix + "_" + key;
					image.write2file(Constants.GET_IMAGES_FROM_PDF_DIR_STRING
							+ "\\" + name);
				}
			}
		}
	}

	/**
	 * 把PDF文档转化为图片
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private static void transferPDFToImages(PDDocument doc, String prefix,
			String type) throws IOException {
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		if (pages.size() > 0) {
			for (int i = 0; i < pages.size(); i++) {
				PDPage page = (PDPage) pages.get(i);
				BufferedImage image = page.convertToImage();
				String name = prefix + "_" + i;
				File file = new File(
						Constants.TRANSFER_PDF_TO_IMAGES_DIR_STRING + "\\"
								+ name + "." + type);
				ImageIO.write(image, type, file);
			}
		}
	}
}
