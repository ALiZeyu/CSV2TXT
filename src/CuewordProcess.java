import java.util.ArrayList;
import java.util.List;

public class CuewordProcess {

	public static void main(String[] args) {
		CueProcess();

	}
	
	public static void CueProcess(){
		List<String> words=Util.read_file("E://workspace/GeoScholar/data/templating/cuewords.txt");
		List<String> allwords=new ArrayList<>();
		for(int i=0; i<11; i++)
			allwords.add("");
		for(String line:words){
			String[] t=line.split("\t");
			int index=Integer.parseInt(t[1]);
			allwords.set(index, allwords.get(index)+" "+t[0]);
		}
		for(int i=1; i<11; i++)
			System.out.println(allwords.get(i));
	}
}
