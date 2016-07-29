import java.util.ArrayList;
import java.util.List;

public class DeleteStop {

	public static void main(String[] args) {
		List<String> t=Util.read_file("E://workspace/To94%/data/pos_train.txt");
		List<String> result=new ArrayList<>();
		for(String str:t){
			if(str.endsWith("。_PU")||str.endsWith("？_PU")||str.endsWith("！_PU"))
				result.add(str.substring(0, str.length()-4));
			else {
				result.add(str);
			}
		}
		System.out.println(result.size());
		Util.writeFile(result, "E://workspace/To94%/data/tag_train_1.txt");
	}
	
	void deleteseg(){
		List<String> t=Util.read_file("E://workspace/To94%/data/pos_train.txt");
		List<String> result=new ArrayList<>();
		for(String str:t){
			if(str.endsWith("。")||str.endsWith("？")||str.endsWith("！"))
				result.add(str.substring(0, str.length()-1));
			else {
				result.add(str);
			}
		}
		System.out.println(result.size());
		Util.writeFile(result, "E://workspace/To94%/data/seg_train_1.txt");
	}

}
