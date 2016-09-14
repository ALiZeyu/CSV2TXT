package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * ，是......的...措施
 */
public class CuoShiSolution2 {

	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		while (k >= 0 && !(sentence.get(k).split("_")[0].equals("，"))) k--;
		if (k == -1) {
			k = word.no - 2;
			while (k >= 0 && !(sentence.get(k).split("_")[0].equals("是"))) k--;
			if (k == -1) {
				k = word.no - 2;
				while (k >= 0 && !(sentence.get(k).split("_")[0].equals("的"))) k--;
				if (k == -1) k = word.no - 1;
			}
		}
		for (int i = 0; i < k; i++)
			s += sentence.get(i).split("_")[0];
		return s;
	}
	
	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		while (k >= 0 && !(sentence.get(k).split("_")[0].equals("的"))) k--;
		if (k == -1) k = word.no - 1;
		int k1 = word.no - 2;
		while (k1 >= 0 && !(sentence.get(k1).split("_")[0].equals("是") || sentence.get(k1).split("_")[0].equals("有"))) k1--;
		if (k1 == -1) k1 = word.no - 1;
		for (int i = k1 + 1; i < k; i++)
			s += sentence.get(i).split("_")[0];
		if (s.isEmpty()) {
			int i = word.no;
			if (i < sentence.size() && sentence.get(i).split("_")[0].equals("可行")) i++;
			if (i < sentence.size() && sentence.get(i).split("_")[0].equals("的")) i++;
			if (i < sentence.size() && sentence.get(i).split("_")[0].equals("是")) i++;
			if (i < sentence.size() && sentence.get(i).split("_")[0].equals("有")) i++;
			for (; i < sentence.size(); i++)
				s += sentence.get(i).split("_")[0];
		}
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		if (node.no >= sentence.size() || !sentence.get(node.no).startsWith("是") || !sentence.get(node.no).startsWith("有"))
			return findFirst(node, sentence) + "@" + findSecond(node, sentence);
		else
			return findSecond(node, sentence) + "@" + findFirst(node, sentence);
	}
	
}
