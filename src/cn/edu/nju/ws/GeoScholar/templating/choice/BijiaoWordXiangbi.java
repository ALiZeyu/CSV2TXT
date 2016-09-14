package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

public class BijiaoWordXiangbi {
	
	public static String findFirst(ArrayList<String> sentence) {
		int index = sentence.indexOf("相比_VV");
		String s = "";
		for (int i = index - 1; i >= 0 && !sentence.get(i).split("_")[0].equals("，") && !sentence.get(i).split("_")[0].equals("与") && !sentence.get(i).split("_")[0].equals("和"); i--) {
			s = sentence.get(i).split("_")[0] + s;
		}
		return s;
	}
	
	public static String findSecond(ArrayList<String> sentence) {
		int index = sentence.indexOf("相比_VV");
		String s = "";
		int in = index - 1;
		if (sentence.get(index + 1).startsWith("，")) {
			while (in >= 0 && !sentence.get(in).split("_")[0].equals("，") && !sentence.get(in).split("_")[0].equals("与") && !sentence.get(in).split("_")[0].equals("和")) in--;
			in++;
			for (int i = index + 2; i < sentence.size() - index + in; i++) {
				boolean b = true;
				for (int j = 0; j < index - in; j++)
					if (!sentence.get(i + j).split("_")[1].equals(sentence.get(in + j).split("_")[1])) {
						b = false;
						break;
					}
				if (b) {
					for (int j = index + 2; j < i + index - in; j++)
						s += sentence.get(j).split("_")[0];
					return s;
				}
			}
		}
		else {
			for (int i = index + 1; i < sentence.size() && !sentence.get(i).split("_")[0].equals("，"); i++)
				s += sentence.get(i).split("_")[0];
		}
		return s;
	}
	
	private static Tree findFirstIP(Tree t) {
		List<Tree> l = new ArrayList<Tree>();
		if (!t.child.isEmpty()) {
			if (t.content.equals("IP") && t.child.size() > 1)
				return t;
			if (t.child.size() != 0)
				for (int i = 0; i < t.child.size(); i++) {
					Tree w = findFirstIP(t.child.get(i));
					if (w != null) return w;
				}
		}
		return null;
	}
	
	public static String findThird(Tree node, ArrayList<String> sentence) {
		String s = "";
		Tree temp = node.parent.parent.parent.parent.parent;
		int k = 2;
		do {
			while (k < temp.child.size() && !temp.child.get(k - 1).content.equals("PU")) k++;
			if (k >= temp.child.size()) break;
			Tree t = temp.child.get(k);
			t = findFirstIP(t);
			/*while (!t.content.equals("IP") && t.child.size() > 0)
				t = t.child.get(0);*/
			if (t == null || t.child.size() == 0) {
				for (int i = k; i < temp.child.size() && temp.child.get(i).content.equals("NP"); i++)
					s += Print.print(temp.child.get(i));
				s += " ";
				if (k == 2 && s.length() > findSecond(sentence).length()) s = s.substring(findSecond(sentence).length());
			} else {
				if (k == 2 && t.child.get(0).child.size() == 1) {
					t = t.child.get(1);
					s += Print.print(t.child.get(t.child.size() - 1)) + " ";
				} else if (t.child.size() == 1) {
					s += Print.print(t.child.get(0).child.get(1)) + " ";
				} else {
					for (int i = 0; i < t.child.size() - 1; i++)
						s += Print.print(t.child.get(i));
					s += " ";
					if (k == 2 && s.contains(findSecond(sentence))) s = s.substring(findSecond(sentence).length());
				}
			}
			k++;
		} while (k < temp.child.size());
		if (!s.isEmpty()) s = s.substring(0, s.length() - 1);
		return s;
	}
	
	public static String findLast(Tree node, ArrayList<String> sentence) {
		String s = "";
		Tree temp = node.parent.parent.parent.parent.parent;
		int k = 2;
		do {
			while (k < temp.child.size() && !temp.child.get(k - 1).content.equals("PU")) k++;
			if (k >= temp.child.size()) break;
			Tree t = temp.child.get(k);
			t = findFirstIP(t);
			/*while (!t.content.equals("IP") && t.child.size() > 0)
				t = t.child.get(0);*/
			if (t == null || t.child.size() == 0) {
				s += Print.print(temp.child.get(temp.child.size() - 1)) + " ";
			} else {
				if (k == 2 && t.child.get(0).child.size() == 1) {
					s += Print.print(t.child.get(1).child.get(0)) + " ";
				} else if (t.child.size() == 1) {
					s += Print.print(t.child.get(0).child.get(0)) + " ";
				} else {
					s += Print.print(t.child.get(t.child.size() - 1)) + " ";
				}
			}
			k++;
		} while (k < temp.child.size());
		if (!s.isEmpty()) s = s.substring(0, s.length() - 1);
		return s;
	}
	
	public static String print(ArrayList<String> sentence, Tree node) {
		return (findFirst(sentence) + "@" + findSecond(sentence) + "@" + findThird(node, sentence) + "@@" + findLast(node, sentence));
	}
}
