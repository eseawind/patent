package ICTCLAS2014;

import helper.Constants;
import java.io.UnsupportedEncodingException;
import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 引用自中科院ICTCLAS分词系统（20140106103837_ICTCLAS2014u0105）-sample-JnaTest_NLPIR
 */
public class Nlpir {

	/**
	 * 定义接口CLibrary，继承自com.sun.jna.Library
	 */
	public interface CLibrary extends Library {
		// 定义并初始化接口的静态变量
		// 这一个语句是来加载dll的，注意dll文件的路径可以是绝对路径也可以是相对路径，只需要填写dll的文件名，不能加后缀。
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				Constants.NLPIR_DLL_STRING, CLibrary.class);

		/**
		 * 初始化函数声明
		 */
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);

		/**
		 * 执行分词函数声明
		 */
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		/**
		 * 提取关键词函数声明
		 */
		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);

		/**
		 * 增加用户词典
		 * 
		 * add by qp 2008.11.10
		 * 
		 */
		public int NLPIR_AddUserWord(String sWord);

		/**
		 * 删除用户词典
		 * 
		 * add by qp 2008.11.10
		 * 
		 */
		public int NLPIR_DelUsrWord(String sWord);

		/**
		 * 退出函数声明
		 */
		public void NLPIR_Exit();
	}

	/**
	 * 编码转换
	 */
	public static String transString(String aidString, String ori_encoding,
			String new_encoding) {
		try {
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 供外界调用的静态方法，执行ICTCLAS分词
	 */
	public static void doNlpir(String inputString, String[] addWord,
			String[] deleteWord, String MaxKeyLimit) {
		// ICTCLAS词库library(即Data文件夹)的相对路径
		String library = Constants.ICTCLAS_LIBRARY_STRING;
		// String system_charset = "GBK";//GBK----0
		// String system_charset = "UTF-8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance
				.NLPIR_Init(library, charset_type, "0");
		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return;
		}
		String nativeBytes = null;
		try {
			System.out.println("输入的文本为： " + inputString);
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(inputString,
					1);
			System.out.println("分词结果为： " + nativeBytes);

			// 如果用户需要增加词典，则显示增加词典的效果
			if (addWord != null) {
				for (int i = 0; i < addWord.length; i++) {
					CLibrary.Instance.NLPIR_AddUserWord(addWord[i]);
				}
				nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
						inputString, 1);
				System.out.println("增加用户词典后分词结果为： " + nativeBytes);
			}

			// 如果用户需要删除词典，则显示删除词典的效果
			if (deleteWord != null) {
				for (int i = 0; i < deleteWord.length; i++) {
					CLibrary.Instance.NLPIR_DelUsrWord(deleteWord[i]);
				}
				nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
						inputString, 1);
				System.out.println("删除用户词典后分词结果为： " + nativeBytes);
			}

			// 如果用户需要提取关键词，则显示提取的关键词
			if (MaxKeyLimit != null) {
				String nativeByte = CLibrary.Instance.NLPIR_GetKeyWords(
						inputString, Integer.parseInt(MaxKeyLimit), false);
				String[] keys = nativeByte.split("#");
				int nCountKey = keys.length;
				System.out.println("提取的关键词个数为：" + nCountKey);
				System.out.println("关键词提取结果是：" + nativeByte);
			}
			CLibrary.Instance.NLPIR_Exit();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	public static String doNlpirString(String inputString, String[] addWord,
			String[] deleteWord) {
		// ICTCLAS词库library(即Data文件夹)的相对路径
		String library = Constants.ICTCLAS_LIBRARY_STRING;
		// String system_charset = "GBK";//GBK----0
		// String system_charset = "UTF-8";
		int charset_type = 1;
		int init_flag = CLibrary.Instance
				.NLPIR_Init(library, charset_type, "0");
		if (0 == init_flag) {
			System.err.println("初始化失败！");
			return null;
		}
		String nativeBytes = null;
		try {
			System.out.println("输入的文本为： " + inputString);
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(inputString,
					1);
			System.out.println("分词结果为： " + nativeBytes);

			// 如果用户需要增加词典，则显示增加词典的效果
			if (addWord != null) {
				for (int i = 0; i < addWord.length; i++) {
					CLibrary.Instance.NLPIR_AddUserWord(addWord[i]);
				}
				nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
						inputString, 1);
				System.out.println("增加用户词典后分词结果为： " + nativeBytes);
			}

			// 如果用户需要删除词典，则显示删除词典的效果
			if (deleteWord != null) {
				for (int i = 0; i < deleteWord.length; i++) {
					CLibrary.Instance.NLPIR_DelUsrWord(deleteWord[i]);
				}
				nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(
						inputString, 1);
				System.out.println("删除用户词典后分词结果为： " + nativeBytes);
			}
			CLibrary.Instance.NLPIR_Exit();
		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return nativeBytes;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String inputString = "据悉，质检总局已将最新有关情况再次通报美方，要求美方加强对输华玉米的产地来源、运输及仓储等环节的管控措施，有效避免输华玉米被未经我国农业部安全评估并批准的转基因品系污染。";
		String[] addWord = { "要求美方加强对输 n", "华玉米的产地来源 n" };
		String[] delWord = { "要求美方加强对输 n" };
		String maxKey = "10";
		doNlpir(inputString, addWord, delWord, maxKey);
	}
}
