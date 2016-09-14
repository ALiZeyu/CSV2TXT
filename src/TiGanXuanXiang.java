import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TiGanXuanXiang {

	public static void main(String[] args) {
		//TiGanXuanXiangPos();
		TiGanXuanXiangfenli();
	}
	
	public static void TiGanXuanXiangPos(){
		List<String> s=Util.read_file("data/posresult.txt");
		List<String> r=new ArrayList<>();
		List<String> tigan=new ArrayList<>();
		
		for(int i=0; i<s.size(); i=i+2){
			String line2=s.get(i+1);
			String[] t2=line2.split(" ");
//			if(t2.length==1||line2.indexOf("，_PU")==-1)//只要选项中有逗号的
//				continue;
			
			String line1=s.get(i);
			String[] t1=line1.split(" ");
			int l1=t1.length,l2=t2.length;
			String line3="";
			line1="";
			for(int j=0;j<l1-l2;j++)
				line1+=t1[j]+" ";
			for(int j=l1-l2;j<l1;j++)
				line3+=t1[j]+" ";
			if(tigan.size()==0||!tigan.get(tigan.size()-1).equals(line1.trim())){
				r.add(line1.trim());
				tigan.add(line1.trim());
			}
			
			r.add(line3.trim());
		}
		Util.writeFile(r, "data/posresult_2.txt");
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
}
