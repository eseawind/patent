package cn.edu.scut.patent;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;

public class DiyTokenizer extends Tokenizer{

	protected DiyTokenizer(AttributeFactory factory, Reader input) {
		super(factory, input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
