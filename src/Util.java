import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Util {
	
	public static void diff(String out , String ref , String diff){
		List<String> candidates=read_file(out);
		List<String> references=read_file(ref);
		List<String> result=new ArrayList<>();
		if(candidates.size()!=references.size()){
			System.out.println("the reference and the candidate consists of different number of lines!");
			return;
		}
		for(int i=0;i<candidates.size();i++){
			if(!candidates.get(i).equals(references.get(i))){
				result.add(candidates.get(i));
				result.add(references.get(i));
			}
		}
		writeFile(result, diff);
	}
	
	public static List<String> read_file(String path){
		List<String> result = new ArrayList<>();
		try {
			BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			String line = br1.readLine();
			if (line.startsWith("\uFEFF")) {
				line = line.substring(1);
			}
			while (line != null) {
				result.add(line.trim());
				line = br1.readLine();
			}
			br1.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		return result;
	}
	
	public static void writeFile(List<String> data,String file){
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			for(String str:data)
				writer.write(str.trim()+"\n");
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<String> getFileList(String path){
		File file = new File(path);
		File[] list = file.listFiles();
		List<String> result = new ArrayList<>();
		for(File f:list){
			if(f.isFile())
				result.add(f.getAbsolutePath());
		}
		return result;
	}
}
