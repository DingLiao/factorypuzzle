package ding.demo.manager;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ding.demo.model.Customer;
import ding.demo.model.PaintType;
import ding.demo.model.TestCase;

public class ParseFileManager {	
	List<TestCase> testCaseList = new ArrayList<TestCase>();
	int testCaseCount = 0;
	String sCurrentLine;
	MyBufferedReader bufferedReader = null;
	
	public static ParseFileManager getInstance(){
		return new ParseFileManager();
	}
	
	public List<TestCase> parseInputFile(String inputPath) throws Exception {
		System.out.println("******** parseInputFile *********");
		try {
			initializeReader(inputPath);
			return getTestCases();
		} catch(FileNotFoundException e) {
			throw new Exception("Input file is not found.");
		} catch(Exception e) {
			throw new Exception("At "+ bufferedReader.getLineContent() + "\n" + e.getMessage());
		} finally {
			try {
				closeReader();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void initializeReader(String inputFilePath) throws FileNotFoundException{
		bufferedReader = new MyBufferedReader(new FileReader(inputFilePath));
	}
	
	private void closeReader() throws IOException{
		bufferedReader.close();
	}
	
	private List<TestCase> getTestCases() throws Exception{
		getTotalTestCaseCount();
		for(int testCaseId=1; testCaseId<=testCaseCount;testCaseId++) {
			TestCase tc = getTestCase(testCaseId);
			testCaseList.add(tc);
		}
		return testCaseList;
	}
	
	private int getTotalTestCaseCount() throws NumberFormatException, IOException{
		sCurrentLine = bufferedReader.readLine();
		testCaseCount = Integer.parseInt(sCurrentLine.trim());
		return testCaseCount;
	}
	
	private int getColorCountForTestCase() throws NumberFormatException, IOException{
		sCurrentLine = bufferedReader.readLine();
		int	colorCount = Integer.parseInt(sCurrentLine.trim());
		return colorCount;
	}
	
	private void insertCustomerIntoCurrentTestCase(TestCase currentTestCase) throws Exception {
		sCurrentLine = bufferedReader.readLine();
		int	customerCount = Integer.parseInt(sCurrentLine.trim());
		for(int i=1; i<=customerCount; i++){
			sCurrentLine = bufferedReader.readLine();
			Customer c = getCustomer(sCurrentLine.trim());
			c.setId(i);
			currentTestCase.insetNewCustomer(c);
		}
	}
	
	private TestCase getTestCase(int testCaseId) throws Exception{
		TestCase tc = new TestCase();
		tc.setId(testCaseId);
		tc.setColorCount(getColorCountForTestCase());
		insertCustomerIntoCurrentTestCase(tc);
		return tc;
	}
	
	private static Customer getCustomer(String input) throws Exception{
		Customer c = new Customer();
		String[] values = input.split(" ");
		int index = 0;
		int paintCount = Integer.parseInt(values[index]);
		for(int i=0;i<paintCount;i++) {
			int color = Integer.parseInt(values[++index]);
			int colorType = Integer.parseInt(values[++index]);
			c.addPreferPaintType(new PaintType(color, colorType));
		}
		if(!validateCustomer(c))
			throw new Exception("Input customer info is incorrect.");
		return c;
	}
	
	private static boolean validateCustomer(Customer c){
		Set<PaintType> paints = new HashSet<PaintType>(c.getPreferedGlossyPaints());
		return paints.size()==c.getPreferedGlossyPaints().size();
	}
}
