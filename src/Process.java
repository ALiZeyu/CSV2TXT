import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Process {
	//对文件夹中csv文件进行处理，处理为一句粗粉一句细分
	//时间,地点,术语,数量词,（粗粒度）分词,（细粒度）分词,词性
	public static void main(String[] args) {
		//List<String> csvList=new ArrayList<>();
		List<String> files=getFileNames("F:/csv_geo_paper/wendati");
		writeIntoAFile(files);
//		String[] tag={"time","loc","term","num"};
//		List<String> tagList=new ArrayList<>();
//		for(int i=0;i<4;i++)
//			tagList.add(tag[i]);
//		String[] inputfile={"2003-北京.choice.tag.csv","2004-北京.choice.tag.csv","2005-北京.choice.tag.csv","2006-北京.choice.tag.csv","2007-北京.choice.tag.csv","2008-北京.choice.tag.csv","2009-北京.choice.tag.csv","2010-北京.choice.tag.csv"};
//		String[] outputfile={"2003-北京.txt","2004-北京.txt","2005-北京.txt","2006-北京.txt","2007-北京.txt","2008-北京.txt","2009-北京.txt","2010-北京.txt",};
//		for(int sj=0;sj<inputfile.length;sj++){
//			try {
//				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("input/"+inputfile[sj]), "utf-8"));
//				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output/"+outputfile[sj]), "utf-8"));
//				String line = br.readLine();
//				if (line.startsWith("\uFEFF")) {
//					line = line.substring(1);
//				}
//				line=br.readLine();
//				int num=1;
//				while (line != null) {
//					String[] t = line.split(",");
//					if(t.length!=7){
//						System.out.println(line);
//						return;
//					}
//					String[] c_seg=t[4].split(" ");
//					String[] x_seg=t[5].split(" ");
//					String[] pos=t[6].split(" ");
//					for (int i = 0; i < 4; i++){
//						if(t[i].length()>0){
//							String[] index=t[i].split(" ");
//							for(int j=0;j<index.length;j++)
//								if(!pos[Integer.parseInt(index[j])].equals(tag[i])){
//									System.out.println(num+" "+line);
//									pos[Integer.parseInt(index[j])]=tag[i];
//								}
//						}
//					}
//					int j=0,c_len=0,x_len=0;
//					for(int i=0;i<c_seg.length;i++){
//						if(!tagList.contains(pos[i])){
//							c_len+=c_seg[i].length();
//							x_len+=x_seg[j].length();
//							String ctemp=c_seg[i];
//							String xtemp=x_seg[j];
//							c_seg[i]=ctemp+"_"+pos[i];
//							x_seg[j]=xtemp+"_"+pos[i];
//							j++;
//						}else{
//							if(c_seg[i].length()==x_seg[j].length()){
//								c_len+=c_seg[i].length();
//								x_len+=x_seg[j].length();
//								String ctemp=c_seg[i];
//								String xtemp=x_seg[j];
//								c_seg[i]=ctemp+"_"+pos[i];
//								//x_seg[j]=xtemp+"_@@";
//								x_seg[j]=xtemp+"_"+pos[i];
//								j++;
//							}else {
//								c_len+=c_seg[i].length();
//								String ctemp=c_seg[i];
//								c_seg[i]=ctemp+"_"+pos[i];
//								while(j<x_seg.length&&c_len>=(x_len+x_seg[j].length())){
//									x_len+=x_seg[j].length();
//									String xtemp=x_seg[j];
//									//x_seg[j]=xtemp+"_@@";
//									x_seg[j]=xtemp+"_"+pos[i];
//									j++;
//								}
//							}
//						}
//					}
//					for(String str:c_seg)
//						writer.write(str+" ");
//					writer.write("\n");
//					for(String str:x_seg)
//						writer.write(str+" ");
//					writer.write("\n");
//					//writer.write("\n");
//					line = br.readLine();
//					num++;
//				}
//				br.close();
//				writer.flush();
//				writer.close();
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public static List<String> getFileNames(String path){
		List<String> result=new ArrayList<>();
		File folder=new File(path);
		File[] files=folder.listFiles();
		for(File f:files){
			result.add(f.getAbsolutePath());
		}
		return result;
	}
	public static void writeIntoAFile(List<String> inputfile){
		List<String> miss=new ArrayList<>();
		String[] tag={"time","loc","term","num"};
		List<String> tagList=new ArrayList<>();
		for(int i=0;i<4;i++)
			tagList.add(tag[i]);
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:/csv_geo_paper/wendati/2th.txt"), "utf-8"));
			for (int sj = 0; sj < inputfile.size(); sj++) {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(new FileInputStream(inputfile.get(sj)), "utf-8"));
				String line = br.readLine();
				if (line.startsWith("\uFEFF")) {
					line = line.substring(1);
				}
				line = br.readLine();
				int num = 1;
				while (line != null) {
					System.out.println(inputfile.get(sj)+" "+num+" "+line);
					String[] t = line.split(",");
					if (t.length != 7) {
						System.out.println(inputfile.get(sj)+" "+line);
						return;
					}
					String[] c_seg = t[4].split(" ");//粗分分词结果
					String[] x_seg = t[5].split(" ");//细分分词结果
					String[] pos = t[6].split(" ");//词性结果
					//miss是为了处理标了时间地点但是没有标上标成NR的情况
					for (int i = 0; i < 4; i++) {
						if (t[i].length() > 0) {
							String[] index = t[i].split(" ");
							for (int j = 0; j < index.length; j++)
								if (!pos[Integer.parseInt(index[j])].equals(tag[i])) {
									miss.add(inputfile.get(sj));
									miss.add(num + " " + line);
									pos[Integer.parseInt(index[j])] = tag[i];
								}
						}
					}
					int j = 0, c_len = 0, x_len = 0;
					for (int i = 0; i < c_seg.length; i++) {
						if (!tagList.contains(pos[i])) {
							c_len += c_seg[i].length();
							x_len += x_seg[j].length();
							String ctemp = c_seg[i];
							String xtemp = x_seg[j];
							c_seg[i] = ctemp + "_" + pos[i];
							x_seg[j] = xtemp + "_" + pos[i];
							j++;
						} else {
							if (c_seg[i].length() == x_seg[j].length()) {
								c_len += c_seg[i].length();
								x_len += x_seg[j].length();
								String ctemp = c_seg[i];
								String xtemp = x_seg[j];
								c_seg[i] = ctemp + "_" + pos[i];
								x_seg[j]=xtemp+"_@@";
								// x_seg[j] = xtemp + "_" + pos[i];
								j++;
							} else {
								c_len += c_seg[i].length();
								String ctemp = c_seg[i];
								c_seg[i] = ctemp + "_" + pos[i];
								while (j < x_seg.length && c_len >= (x_len + x_seg[j].length())) {
									x_len += x_seg[j].length();
									String xtemp = x_seg[j];
									x_seg[j]=xtemp+"_@@";
									// x_seg[j] = xtemp + "_" + pos[i];
									j++;
								}
							}
						}
					}
					for (String str : c_seg)
						writer.write(str + " ");
					writer.write("\n");
					for (String str : x_seg)
						writer.write(str + " ");
					writer.write("\n");
					// writer.write("\n");
					line = br.readLine();
					num++;
				}
				br.close();
			}
			writer.flush();
			writer.close();
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("F:/csv_geo_paper/wendati/miss.txt"), "utf-8"));
			for(String str:miss)
				bw.write(str);
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
