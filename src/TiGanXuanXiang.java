import java.util.ArrayList;
import java.util.List;

public class TiGanXuanXiang {

	public static void main(String[] args) {
		TiGanXuanXiangPos();
	}
	
	public static void TiGanXuanXiangPos(){
		List<String> s=Util.read_file("data/posresult.txt");
		List<String> r=new ArrayList<>();
		
		for(int i=0; i<s.size(); i=i+2){
			String line2=s.get(i+1);
			String[] t2=line2.split(" ");
			if(t2.length==1||line2.indexOf("，_PU")==-1)
				continue;
			
			String line1=s.get(i);
			String[] t1=line1.split(" ");
			int l1=t1.length,l2=t2.length;
			String line3="";
			for(int j=l1-l2;j<l1;j++)
				line3+=t1[j]+" ";
			r.add(line1.trim());
			r.add(line3.trim());
		}
		Util.writeFile(r, "data/posresult_2.txt");
	}
}
