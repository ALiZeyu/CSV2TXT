package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 的...原因（优势）是(有)
 */
public class YuanYinSolution2 {

	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		while (k < sentence.size() && !(sentence.get(k).split("_")[0].equals("是") || sentence.get(k).split("_")[0].equals("有"))) k++;
		if (k == sentence.size()) k = word.no - 1;
		for (int i = k + 1; i < sentence.size(); i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		Pattern pattern = Pattern.compile("成因|原因");
		int k = word.no - 2;
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		if (k == -1) {
			k = word.no - 2;
			while (k >= 0 && !sentence.get(k).split("_")[0].equals("的")) k--;
			if (k == -1) k = word.no - 1;
		} else
			k++;
		if (pattern.matcher(sentence.get(k - 1).split("_")[0]).matches()) {
			for (int i = k + 1; i < word.no - 1; i++)
				s += sentence.get(i).split("_")[0];
		} else {
			pattern = Pattern.compile("影响|导致");
			for (int i = k - 1; i >= 0 && !pattern.matcher(sentence.get(i).split("_")[0]).matches(); i--)
				s = sentence.get(i).split("_")[0] + s;
		}
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence);
	}
}
