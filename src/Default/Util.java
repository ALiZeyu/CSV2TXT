package Default;
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
import java.util.Arrays;
import java.util.List;

/**
 * 1、比较两个文本文件的不同
 * 2、读文件
 * 3、写文件
 * 4、获取当前文件夹下的所有文件*/
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
	/**获取当前路径下所有的文件名*/
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
	/**获取当前文件夹下所有文件的内容List。*/
	public static List<String> getAllFileContentList(String path){
		List<String> final_result=new ArrayList<>();
		List<String> fileList=Util.getFileList(path);
		for(String str : fileList){
			List<String> temp = Util.read_file(str);
			temp.remove(0);
			final_result.addAll(temp);
		}
		return final_result;
	}
	/**这个是训练berkeley parser之前，把训练数据改成要求的格式*/
	public static void modifyBracket(String path){
		List<String> txt=read_file(path);
		List<String> result=new ArrayList<>();
		for(String str:txt){
			result.add("( "+str+" )");
		}
		writeFile(result, "E://workspace/BerkelyParser/lib/traindata.txt");
	}
	
	public static void posFileToSegFile(String path){
		List<String> txt=read_file(path);
		List<String> result=new ArrayList<>();
		for(String str:txt){
			String[] temp=str.split(" ");
			StringBuffer s=new StringBuffer();
			for(String t:temp){
				if(t.split("_").length!=2){
					System.out.println(str);
					return;
				}
				s.append(t.split("_")[0]+" ");
			}
			result.add(s.toString().trim());
		}
		writeFile(result, "E://workspace/BerkelyParser/lib/test.txt");
	}
	
	/**将有空格做间隔的句子切分成list*/
	public static List<String> strToList(String sentence){
		String[] a=sentence.split(" ");
		List<String> list=new ArrayList<>(Arrays.asList(a));
		return list;
	}
	
	public static void main(String[] args) {
		//modifyBracket("E://workspace/BerkelyParser/lib/BRACKETED.txt");
		posFileToSegFile("E://分词/分词数据/CTB5.0 auto-pos/ctb_test.data");
	}
}
