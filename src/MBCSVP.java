import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MBCSVP {
	//本类处理标上模板结果的试卷，分别是题干\t选项，模板
	public static void main(String[] args) {
		//csvToQ();
		//getAllTigan("F://地理相关/所有选择题及其填槽结果/");
		bianHuaYunDong();

	}
	//处理csv提出题干+选项以及单独的选项
	public static void csvToQ(){
		String prefix="E://workspace/CSV2TXT/data/";
//		String[] files={"2010","2011","2012","2013","2014","2015"};
		//最终的结果，要写入文件当中
		List<String> final_result=new ArrayList<>();
//		for(String str:files){
//			String path = prefix + str + ".csv";
//			List<String> temp = Util.read_file(path);
//			temp.remove(0);
//			for(String line:temp){
//				String[] all = line.split(",");
//				String[] content=all[0].split("\t");
//				final_result.add(content[0]+content[1]);
//				final_result.add(content[1]);
//			}
//		}
		
		String path = prefix + "位于.txt";
		List<String> temp = Util.read_file(path);
		for(String line:temp){
			System.out.println(line);
			String[] all = line.split(",");
			String[] content=all[0].split("\t");
			final_result.add(content[0]+"@"+content[1]);
		}
		
		Util.writeFile(final_result, "E://workspace/CSV2TXT/data/weiyu.txt");
	}
	
	public static void getAllTigan(String path){
		//最终的结果，要写入文件当中
		List<String> final_result=new ArrayList<>();
		List<String> fileList=Util.getFileList(path);
		for(String str : fileList){
			List<String> temp = Util.read_file(str);
			temp.remove(0);
			for(String line:temp){
				System.out.println(line);
				String[] all = line.split(",");
				String[] content=all[0].split("\t");
				final_result.add(content[0]+"@"+content[1]);
			}
		}

		Util.writeFile(final_result, "E://workspace/CSV2TXT/data/选择题汇总.txt");
	}
	
	public static void bianHuaYunDong(){
		List<String> words = Util.read_file("F://地理相关/所有选择题及其填槽结果按模板分类/tempcue.txt");
		Set<String> bianhua = new HashSet<>();
		Set<String> yundong = new HashSet<>();
		for(String str : words){
			String[] t=str.split("\t");
			if(t[1].equals("9"))
				yundong.add(t[0]);
			else
				bianhua.add(t[0]);
		}
		List<String> temp = Util.read_file("F://地理相关/所有选择题及其填槽结果按模板分类/趋势.csv");
		Set<String> bh = new HashSet<>();
		Set<String> yd = new HashSet<>();
		for(String line:temp){
			System.out.println(line);
			String[] all = line.split(",");
			String content=all[0].split("\t")[0]+"@"+all[0].split("\t")[1];
			if(haswords(yundong, content))
				yd.add(content);
			else if(haswords(bianhua, content))
				bh.add(content);
		}
		List<String> bbh=new ArrayList<>(bh);
		List<String> yyd=new ArrayList<>(yd);
		Util.writeFile(bbh, "F://地理相关/所有选择题及其填槽结果按模板分类/bianhua.txt");
		Util.writeFile(yyd, "F://地理相关/所有选择题及其填槽结果按模板分类/yundong.txt");
	}
	
	public static boolean haswords(Set<String> words, String sentence){
		for(String str : words){
			if(sentence.indexOf(str)!=-1)
				return true;
		}
		return false;
	}
}
