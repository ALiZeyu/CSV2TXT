package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 因...而（，）...
 */
public class YuanYinSolution1 {

	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no;
		while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("而"))) k++;
		if (k == sentence.size()) {
			k = word.no;
			while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("，"))) k++;
			if (k == sentence.size()) 
				return sentence.get(word.no).split("_")[0];
		}
		for (int i = word.no; i < k; i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no;
		while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("而"))) k++;
		if (k == sentence.size()) {
			k = word.no;
			while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("，"))) k++;
			if (k == sentence.size()) 
				k = word.no;
		}
		for (int i = word.no - 2; i >= 0 && sentence.get(i).split("_")[1].startsWith("N"); i--)
			s = sentence.get(i).split("_")[0] + s;
		for (int i = k + 1; i < sentence.size(); i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence);
	}
}
