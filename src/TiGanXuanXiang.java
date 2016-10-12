import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TiGanXuanXiang {

	public static void main(String[] args) {
		//TiGanXuanXiangPos();
	//	TiGanXuanXiangfenli();
		//getPureAndTag();
		Util.diff("E://workspace/BerkeleySource/data/未修改结果.txt", "E://workspace/BerkeleySource/data/修改结果.txt", "E://workspace/BerkeleySource/data/对比结果.txt");
	}
	
	public static void TiGanXuanXiangPos(){
		List<String> s=Util.read_file("data/posresult.txt");
		List<String> r=new ArrayList<>();
		List<String> tigan=new ArrayList<>();
		List<String> xuanxiang = new ArrayList<>();
		
		for (int i = 0; i < s.size(); i = i + 2) {
			//选项
			String line2 = s.get(i + 1);
			String[] t2 = line2.split(" ");
			//题干+选项
			String line1 = s.get(i);
			String[] t1 = line1.split(" ");
			int l1 = t1.length, l2 = t2.length;
			String line3 = "";
			line1 = "";
			//题干核心
			for (int j = l1 - l2 - 1; j >= 0 && !t1[j].startsWith("，"); j--)
				line1 = t1[j] + " " + line1;
			// for(int j=0;j<l1-l2;j++)
			// line1+=t1[j]+" ";
			for (int j = l1 - l2; j < l1; j++)
				line3 += t1[j] + " ";
			// 题干防止重复，和最后存入题干不同再存入
			if (tigan.size() == 0 || !tigan.get(tigan.size() - 1).equals(line1.trim())) {
				r.add(line1.trim());
				tigan.add(line1.trim());
			}
			if(xuanxiang.isEmpty()||!isRepeat(xuanxiang.get(xuanxiang.size()-1), line3.trim())){
				r.add(s.get(i));
				r.add(line3.trim());
				xuanxiang.add(line3.trim());
			}
		}
		Util.writeFile(r, "data/posresult_3.txt");
	}
	//判断两个选项是不是类型类似(词数和对应词性一致)
	public static boolean isRepeat(String p, String s){
		if(p.contains(s))
			return true;
		String[] peek = p.split(" ");
		String[] str = s.split(" ");
		if(peek.length!=str.length)
			return false;
		for(int i=0;i<peek.length;i++)
			if(!peek[i].split("_")[1].equals(str[i].split("_")[1]))
				return false;
		return true;
	}
	
	public static void TiGanXuanXiangfenli(){
		List<String> s=Util.read_file("data/10to16A.txt");
		List<String> r=new ArrayList<>();
		List<String> tigan=new ArrayList<>();
		
		for(int i=0; i<s.size(); i=i+2){
			String line1=s.get(i);
			String line2=s.get(i+1);
			String line3=line1.replace(line2, "");
			if(tigan.size()==0||!tigan.get(tigan.size()-1).equals(line3.trim())){
				r.add("");
				r.add(line3);
				tigan.add(line3);
			}
			r.add(line2.trim());
		}
		Util.writeFile(r, "data/10to16_pure.txt");
	}
	
	//从题干、选项的词法分析结果中提取题目和题目(题干+选项)的全部结果
	public static void getPureAndTag(){
		List<String> all = Util.read_file("data/10to16out.txt");
		List<String> pure = new ArrayList<>();
		List<String> tag = new ArrayList<>();
		for(int i=0;i<all.size();i=i+2){
			if(!tag.isEmpty() && tag.get(tag.size()-1).contains(all.get(i+1)))
				continue;
			tag.add(all.get(i));
			String t = all.get(i).replace(all.get(i+1), "");
			pure.add(listToString(t)+"@"+listToString(all.get(i+1)));
		}
		Util.writeFile(pure, "data/10to16Apure.txt");
		Util.writeFile(tag, "data/10to16Atag.txt");
	}
	
	public static String listToString(String sentence){
		String[] list=sentence.split(" ");
		StringBuffer str = new StringBuffer();
		for(String line : list)
			str.append(line.split("_")[0]);
		return str.toString();
	}
}
