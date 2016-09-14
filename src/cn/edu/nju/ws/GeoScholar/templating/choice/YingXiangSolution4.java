package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 对...影响（重要）...
 */

public class YingXiangSolution4 {

	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		Pattern pattern = Pattern.compile("对");
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		if (k == -1) k = word.no - 1;
		for (int i = k - 1; i >= 0 && !sentence.get(i).split("_")[0].equals("，"); i--)
			s = sentence.get(i).split("_")[0] + s;
		return s;
	}
	
	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		Pattern pattern = Pattern.compile("对");
		while (k >= 0 && !pattern.matcher(sentence.get(k).split("_")[0]).matches()) k--;
		if (k == -1) k = word.no - 3;
		int k1 = word.no - 2;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("的")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("明显")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("至关")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("很")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("非常")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("特别")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("十分")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("没有")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("不")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("无")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("产生")) k1--;
		if (k1 >= 0 && sentence.get(k1).split("_")[0].equals("的")) k1--;
		for (int i = k + 1; i <= k1; i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findLast(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no;
		if (k < sentence.size() && sentence.get(k).split("_")[0].equals("是")) k++;
		for (int i = k; i < sentence.size() && !sentence.get(k).split("_")[0].equals("，"); i++)
			s += sentence.get(i).split("_")[0];
		Pattern pattern = Pattern.compile("无|没有|不");
		if (s.isEmpty() && (pattern.matcher(sentence.get(k - 2).split("_")[0]).matches() || pattern.matcher(sentence.get(k - 3).split("_")[0]).matches()))
			s = "没有";
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence) + "@" + findLast(node, sentence);
	}
}
