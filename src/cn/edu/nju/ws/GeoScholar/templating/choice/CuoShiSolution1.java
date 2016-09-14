package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 为...，应（应该）...
 */
public class CuoShiSolution1 {

	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		while (k >= 0 && !(sentence.get(k).split("_")[0].equals("，"))) k--;
		if (k == -1) k = word.no - 1;
		int k1 = sentence.get(0).split("_")[0].equals("为") ? 1 : 0;
		for (int i = k1; i < k; i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no;
		Pattern pattern = Pattern.compile("是|为|包括");
		while (k < sentence.size() && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k++;
		if (k == sentence.size()) {
			k = word.no;
			while (k < sentence.size() && !sentence.get(k).split("_")[0].equals("措施")) k++;
			if (k == sentence.size()) k = word.no - 1;
		}
		for (int i = k + 1; i < sentence.size(); i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence);
	}
	
}
