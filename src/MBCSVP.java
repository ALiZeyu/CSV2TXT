import java.util.ArrayList;
import java.util.List;

public class MBCSVP {
	//本类处理标上模板结果的试卷，分别是题干\t选项，模板
	public static void main(String[] args) {
		csvToQ();

	}
	//处理csv提出题干+选项以及单独的选项
	public static void csvToQ(){
		String prefix="E://workspace/CSV2TXT/data/";
		String[] files={"2010","2011","2012","2013","2014","2015"};
		//最终的结果，要写入文件当中
		List<String> final_result=new ArrayList<>();
		for(String str:files){
			String path = prefix + str + ".csv";
			List<String> temp = Util.read_file(path);
			temp.remove(0);
			for(String line:temp){
				String[] all = line.split(",");
				String[] content=all[0].split("\t");
				final_result.add(content[0]+content[1]);
				final_result.add(content[1]);
			}
		}
		
		String path = prefix + "2016.txt";
		List<String> temp = Util.read_file(path);
		for(String line:temp){
			String[] content=line.split("@");
			final_result.add(content[0]+content[1]);
			final_result.add(content[1]);
		}
		
		Util.writeFile(final_result, "E://workspace/CSV2TXT/data/10to16A.txt");
	}
}
