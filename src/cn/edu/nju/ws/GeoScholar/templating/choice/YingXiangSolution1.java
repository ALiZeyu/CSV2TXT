package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 受（受到）...控制（影响，侵扰），...
 */

//to do 影响...的有....
public class YingXiangSolution1 {

	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		Pattern pattern = Pattern.compile("受到|受");
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		if (k == -1) k = word.no - 1;
		pattern = Pattern.compile("是|，");
		if (k > 0) {
			if (pattern.matcher(sentence.get(k - 1).split("_")[0]).matches()) {
				Tree t = word.parent;
				while (t.parent != null) t = t.parent;
				while (t.child.size() == 1) t = t.child.get(0);
				s = Print.print(t.child.get(0));
			} else {
				if (k > 0 && sentence.get(k - 1).split("_")[0].equals("易")) k--;
				if (k > 0 && sentence.get(k - 1).split("_")[0].equals("少")) k--;
				if (k > 0 && sentence.get(k - 1).split("_")[0].equals("较")) k--;
				if (k > 0 && sentence.get(k - 1).split("_")[0].equals("主要")) k--;
				for (int i = k - 1; i >= 0 && !sentence.get(i).split("_")[0].equals("，") && !sentence.get(i).split("_")[1].equals("LC"); i--)
					s = sentence.get(i).split("_")[0] + s;
			}
			s += "@" + findLast(word, sentence);
		} else {
			Tree t = word.parent;
			while (!t.parent.content.equals("ROOT")) t = t.parent;
			if (t.child.get(t.child.size() - 1).content.equals("IP")) {
				t = t.child.get(t.child.size() - 1);
				s = Print.print(t.child.get(0)) + "@" + Print.print(t.child.get(t.child.size() - 1)); 
			}
		}
		return s;
	}
	
	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		Pattern pattern = Pattern.compile("受到|受");
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		if (k == -1) k = Math.max(word.no - 3, 0);
		for (int i = k + 1; i < word.no - 1 && !(i > 0 && i == word.no - 2 && sentence.get(i).split("_")[0].equals("的")); i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findLast(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("，"))) k++;
		if (k == sentence.size()) k = word.no - 1;
		for (int i = k + 1; i < sentence.size(); i++)
			s += sentence.get(i).split("_")[0];
		Pattern pattern = Pattern.compile("受到|受");
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		pattern = Pattern.compile("无|没有|少");
		if (s.isEmpty() && pattern.matcher(sentence.get(k - 1).split("_")[0]).matches())
			s = sentence.get(k - 1).split("_")[0];
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence);
	}
}
