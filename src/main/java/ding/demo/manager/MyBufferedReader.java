package ding.demo.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {
	static int currentLine = 0;
	private String lineContent = "";
	
	public MyBufferedReader(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String readLine() throws IOException {
		++currentLine;
		lineContent = super.readLine();
		return lineContent;
	}

	public String getLineContent(){
		return "currentLint #" + +currentLine + ": " + lineContent;
	}
}
