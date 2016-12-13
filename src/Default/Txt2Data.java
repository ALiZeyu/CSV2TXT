package Default;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Txt2Data {
	//将处理过的地理试卷处理成分词词性标注的6个文件
	public static void main(String[] args) {
		String[] inputfile={"TRAIN.txt","TEST.txt"};
		String[] outputfile={"seg_train.txt","pos_train.txt","seg_test.txt","pos_test.txt","seg_gold.txt","pos_gold.txt"};
			try {
				List<String> seg_train=new ArrayList<>();
				List<String> pos_train=new ArrayList<>();
				List<String> seg_test=new ArrayList<>();
				List<String> seg_gold=new ArrayList<>();
				List<String> pos_test=new ArrayList<>();
				List<String> pos_gold=new ArrayList<>();
				List<String> all_train=new ArrayList<>();
				List<String> all_test=new ArrayList<>();
				
				BufferedReader br1 = new BufferedReader(new InputStreamReader(new FileInputStream("input/TRAIN.txt"), "utf-8"));
				String line = br1.readLine();
				if (line.startsWith("\uFEFF")) {
					line = line.substring(1);
				}
				int num=1;
				while (line != null) {
					if(num%2==0){
						all_train.add(line.trim());
					}
					num++;
					line = br1.readLine();
				}
				br1.close();
				writeFile(all_train, "pos_train.txt");
				
				for(String str:all_train){
					String temp="";
					String[] array=str.split(" ");
					for(String str1:array){
						temp+=str1.split("_")[0]+" ";
					}
					seg_train.add(temp.trim());
				}
				writeFile(seg_train, "seg_train.txt");
				
				BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("input/TEST.txt"), "utf-8"));
				line = br2.readLine();
				if (line.startsWith("\uFEFF")) {
					line = line.substring(1);
				}
				num=1;
				while (line != null) {
					if(num%2==0){
						all_test.add(line.trim());
					}
					num++;
					line = br2.readLine();
				}
				br2.close();
				writeFile(all_test, "pos_gold.txt");
				for(String str:all_test){
					String temp="";
					String temp1="";
					String[] array=str.split(" ");
					for(String str1:array){
						temp+=str1.split("_")[0]+" ";
						temp1+=str1.split("_")[0];
					}
					seg_gold.add(temp.trim());
					seg_test.add(temp1.trim());
				}
				writeFile(seg_gold, "seg_gold.txt");
				writeFile(seg_gold, "pos_test.txt");
				writeFile(seg_test, "seg_test.txt");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void writeFile(List<String> data,String file){
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output/"+file), "utf-8"));
			for(String str:data)
				writer.write(str+"\n");
			writer.flush();
			writer.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
