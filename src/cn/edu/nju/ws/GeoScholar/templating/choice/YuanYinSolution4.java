package cn.edu.nju.ws.GeoScholar.templating.choice;
import java.util.ArrayList;

import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

/**
 * 
 * @author wang
 * 由于,因为
 */
public class YuanYinSolution4 {

	public static String findFirst(Tree word, ArrayList<String> sentence) {
		String s = "";
		if (word.no > 1)
			for (int i = word.no; i < sentence.size(); i++)
				s += sentence.get(i).split("_")[0];
		else {
			int k = word.no;
			while (k < sentence.size() && !sentence.get(k).startsWith("，"))
				k++;
			for (int i = word.no; i < k; i++)
				s += sentence.get(i).split("_")[0];
		}
		return s;
	}
	
	public static String findSecond(Tree word, ArrayList<String> sentence) {
		String s = "";
		int k = word.no - 2;
		if (k >= 0) {
			if (sentence.get(k).split("_")[0].equals("是")) k--;
			if (k >= 0 && sentence.get(k).split("_")[0].equals("主要")) k--;
			if (k >= 0 && sentence.get(k).split("_")[0].equals("或许")) k--;
			if (k >= 0 && sentence.get(k).split("_")[0].equals("应该")) k--;
			if (k >= 0 && sentence.get(k).split("_")[0].equals("可能")) k--;
			if (k >= 0 && sentence.get(k).split("_")[0].equals("，")) k--;
			for (int i = 0; i <= k; i++)
				s += sentence.get(i).split("_")[0];
		} else {
			k = word.no;
			while (k < sentence.size() && !sentence.get(k).startsWith("，")) k++;
			if (sentence.get(k).split("_")[0].equals("，")) k++;
			if (sentence.get(k).split("_")[0].equals("所以")) k++;
			for (int i = k; i < sentence.size(); i++)
				s += sentence.get(i).split("_")[0];
			
		}
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		return findFirst(node, sentence) + "@" + findSecond(node, sentence);
	}
}

