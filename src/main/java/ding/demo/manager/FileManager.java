package ding.demo.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FileManager {
	private static FileManager instance = null;
	private String format = "UTF-8";
	
	public static FileManager getInstance(){
		if(instance == null) {
			synchronized(FileManager.class) {
				if(instance == null) {
					instance = new FileManager();
				}
			}
		}
		return instance;
	}
	
	public void writeToFile(InputStream uploadedInputStream,
		String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	public void generateResultFile(String filePath, List<String> fileContent){
		System.out.println("******** generateResultFile *********");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePath, format);
			for(int i=0; i< fileContent.size(); i++) {
				System.out.println(fileContent.get(i));
				writer.println(fileContent.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
			
		}
	}
	
	public void deleteFile(File f) throws IOException {
		if(!f.exists())
			return;
		
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteFile(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}
	
	public void deleteFile(String path) throws IOException {
		File f = new File(path);
		deleteFile(f);
	}
	
}
