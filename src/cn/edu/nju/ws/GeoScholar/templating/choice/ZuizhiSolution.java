package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.util.ArrayList;

import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

/**
 * 
 * @author wang
 * NP NP VP
 */

public class ZuizhiSolution {
	
	//TO DEBUG
	public static String findFirst(Tree word, ArrayList<String> sentence) {
		Tree t = word.parent; 
		String s = "";
		while (t.parent != null && !(t.parent.content.equals("IP") && !t.parent.parent.content.equals("CP")) && !t.parent.content.equals("NP") && !(t.parent.content.equals("VP") && t.parent.child.indexOf(t) > 0 && !t.parent.child.get(0).content.startsWith("AD"))) 
			t = t.parent;
		while (t.parent.child.indexOf(t) == 0 && t.parent.child.size() == 1) t = t.parent;
		if (t.parent.child.indexOf(t) == 0 || sentence.contains("是_VC")) {
			int k = word.no;
			while (k >= 0 &&!sentence.get(k).split("_")[0].equals("是")) k--;
			if (k == -1) {
				k = word.no;
				while (k < sentence.size() &&!sentence.get(k).split("_")[0].equals("是")) k++;
				if (k == sentence.size()) {
					k = word.no;
					while (k < sentence.size() && !sentence.get(k).split("_")[0].equals("的")) k++;
				}
				for (int i = k + 1; i < sentence.size() && !sentence.get(i).split("_")[0].equals("，"); i++)
					s += sentence.get(i).split("_")[0];
			} else {
				if (sentence.get(k - 1).split("_")[0].startsWith("可能")) k--;
				for (int i = k - 1; i >= 0 && sentence.get(i).split("_")[1].startsWith("N") || (i > 0 && sentence.get(i).split("_")[1].startsWith("C") && sentence.get(i - 1).split("_")[1].startsWith("N")); i--)
					s = sentence.get(i).split("_")[0] + s;
			}
		} else
		if (t.parent.parent.content.equals("ROOT") || t.parent.parent.parent.content.equals("ROOT")) {
			if (t.parent.child.indexOf(t) == 1) {
				t = t.parent.child.get(0);
				if (t.content.equals("PP") && t.parent.parent.child.get(0).content.equals("NP"))
					s = Print.print(t.parent.parent.child.get(0));
				else
				if (t.child.size() > 2 && t.child.get(t.child.size() - 2).content.equals("CC"))
					s = Print.print(t);
				else {
					if (t.content.equals("NN"))
						t = t.parent.parent;
					if (!t.child.get(0).content.equals("NN")) {
						t = t.child.get(0);
						if (t.content.equals("CP"))
							t = t.child.get(0);
						s = Print.print(t);
					} else {
						while (t.child.size() > 0) t = t.child.get(0);
						int i = word.no - 2;
						while (i >= 0 && sentence.get(i).split("_")[1].startsWith("N")) {
							s = sentence.get(i).split("_")[0] + s;
							i--;
						}
					}
				}
			} else  {
				if (t.content.equals("CP"))
					t = t.child.get(0);
				if (t.parent.parent.content.equals("ROOT")) {
					int i = t.parent.child.indexOf(t) - 1;
					int index = i;
					while (i >= 0) {
						if (t.parent.child.get(i).content.equals("NP"))
							index = i;
						else if (t.parent.child.get(i).content.equals("PU") && t.parent.child.get(i).child.get(0).content.equals("，"))
							break;
						i--;
					}
					t = t.parent.child.get(index);
				}
				else
					t = t.parent.child.get(0);
				s = Print.print(t);
			}
		} else {
			while (!t.parent.parent.content.equals("ROOT")) t = t.parent;
			if (t.parent.child.indexOf(t) == 0 || t.parent.child.get(t.parent.child.size() - 1).content.equals("VP"))
				s = Print.print(findCueWord.findFirstNP(t.parent.child.get(t.parent.child.size() - 1), sentence));
			else
				s = Print.print(findCueWord.findFirstNP(t.parent.child.get(t.parent.child.indexOf(t) - 1), sentence));
		}
		return s;
	}
	
	public static String findSecond(Tree word) {
		Tree t = word.parent;
		String s = "";
		while (t.parent.parent != null && !t.parent.content.equals("IP") && !(t.parent.content.equals("NP") && (!word.parent.content.startsWith("N") || t.parent.child.indexOf(t) > 0)) && !(t.parent.content.equals("VP") && t.parent.child.indexOf(t) > 0 && !t.parent.child.get(0).content.startsWith("AD"))) 
			t = t.parent;
		if (t.parent.child.indexOf(t) == 0) {
			while (t.parent.child.size() == 1)
				t = t.child.get(0);
			t = t.parent.child.get(1);
			if (t.child.size() > 1)
				s = Print.print(t.child.get(1));
		} else
		if (t.parent.child.indexOf(t) == 1) {
			t = t.parent.child.get(0);
			if (t.content.equals("PP") && t.parent.parent.child.get(0).content.equals("NP"))
				s = Print.print(t);
			else
			if (t.child.size() > 2 && t.child.get(t.child.size() - 2).content.equals("CC"))
				s = "";
			else {
				if (!t.content.equals("NP"))
					if (!t.child.get(0).content.equals("NN") && !t.child.get(0).content.equals("JJ"))
						t = t.child.get(t.child.size() - 1);
				s = Print.print(t);
			}
		/*} else if (t.parent.child.indexOf(t) == 2) {
			t = t.parent.child.get(1);
			s = Print.print(t);*/
		} else {
			if (t.parent.parent.content.equals("ROOT")) {
				int i = t.parent.child.indexOf(t) - 1;
				int index = i;
				while (i >= 0) {
					if (t.parent.child.get(i).content.equals("NP"))
						index = i;
					else if (t.parent.child.get(i).content.equals("PU") && t.parent.child.get(i).child.get(0).content.equals("，"))
						break;
					i--;
				}
				index++;
				for (; index < t.parent.child.indexOf(t); index++)
					s += Print.print(t.parent.child.get(index));
			}
			else
				for (int i = 1; i < t.parent.child.indexOf(t); i++)
					s += Print.print(t.parent.child.get(i));
			//t = t.parent.child.get(t.parent.child.indexOf(t) - 1);
			//s = Print.print(t);
		}
		return s;
	}
	
	public static String findThird(Tree word) {
		if (word.content.endsWith("最") || word.content.endsWith("为"))
			if (word.parent.parent.parent.child.get(word.parent.parent.parent.child.size() - 1).child.size() == 1)
				return Print.print(word.parent.parent.parent.child.get(word.parent.parent.parent.child.size() - 1));
			else
				return Print.print(word.parent.parent.parent.child.get(word.parent.parent.parent.child.size() - 1).child.get(0));
		else return word.content.split("最")[1];
	}
	
	public static String findLast(Tree word, ArrayList<String> sentence) {
		String s = "";
		Tree t = word.parent;
		while (!t.parent.content.equals("ROOT") && !t.parent.content.equals("NP"))
			t = t.parent;
		if (t.parent.content.equals("ROOT") || t.parent.child.get(0).equals(t)) {
			t = word.parent;
			while (t.parent != null) t = t.parent;
			s = Print.print(findCueWord.findFirstLCP(t, sentence));
			if (s.isEmpty()) {
				int k = word.no;
				while (k < sentence.size() && !sentence.get(k).split("_")[0].equals("是")) k++;
				if (sentence.get(k - 1).startsWith("可能")) k--;
				int k1 = word.no;
				while (k1 < sentence.size() && !sentence.get(k1).split("_")[0].equals("的")) k1++;
				for (int i = k1 + 1; i < k && i < sentence.size(); i++)
					s += sentence.get(i).split("_")[0];
			}
		} else
			s = Print.print(t.parent.child.get(0));
		return s;
	}
	
	public static String print(Tree node, ArrayList<String> sentence) {
		String first = findFirst(node, sentence);
		String second = findSecond(node);
		if (second.startsWith(first)) {
			second = second.substring(first.length());
			if (second.startsWith("的"))
				second = second.substring(1);
		} else {
			if (first.startsWith(second)) {
				first = first.substring(second.length());
				if (first.startsWith("的"))
					first = first.substring(1);
			}
		}
		return first + "@" + second + "@" + findThird(node) + "@" + findLast(node, sentence);
	}
}
