package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Segment;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

public class BijiaoPosPP {
	
	public static String findFirst(Tree word) {
		Tree t = word.parent;
		if (t.parent.content.equals("NP")) {
			return Print.print(t.parent.child.get(0));
		}
		int index = t.child.indexOf(word);
		while (t.parent.child.indexOf(t) == 0) {
			Tree child = t;
			t = t.parent;
			index = t.child.indexOf(child);
		}
		Tree child = t;
		t = t.parent;
		index = t.child.indexOf(child);
		String s = "";
		for (int i = index - 1; i >= 0 && t.child.get(i).content.equals("NP"); i--)
			s = Print.print(t.child.get(i)) + s;
		if (s.isEmpty() && word.parent.parent.parent.parent != null)
			s = Print.print(word.parent.parent.parent.parent.child.get(0));
		return s;
	}
	
	public static String findSecond(Tree word) {
		Tree t = word.parent.parent;
		if (t.content.equals("NP"))
			return Print.print(t.child.get(t.child.size() - 1));	
		return Print.print(t.child.get(1));
	}
	
	public static String findThird(Tree word, List<String> sentence) {
		//Tree t = word.parent.parent.parent;
		String s = "";
		//if (t.child.size() > 1 && t.child.get(t.child.size() - 2).content.equals("ADVP"))
			//s = Print.print(t.child.get(t.child.size() - 1));
		int i = word.no + Segment.j.seg_postag(findSecond(word)).size();
		List<String> third = new ArrayList<String>();
		List<String> last = new ArrayList<String>();
		if (sentence.get(i).endsWith("VA") && i < sentence.size() - 2) {
			third.add(sentence.get(i - 1).split("_")[0]);
			last.add(sentence.get(i).split("_")[0]);
			i++;
		}
		while (i < sentence.size() - 1) {
			if ((sentence.get(i).endsWith("NN") || sentence.get(i).endsWith("VV")) && (sentence.get(i + 1).endsWith("VA") || sentence.get(i + 1).endsWith("NN"))) {
				third.add(sentence.get(i).split("_")[0]);
				last.add(sentence.get(i + 1).split("_")[0]);
				i++;
			}
			if (i < sentence.size() - 2 && sentence.get(i).endsWith("AD") && sentence.get(i + 1).endsWith("VV") && sentence.get(i + 2).split("_")[1].startsWith("N")) {
				third.add(sentence.get(i).split("_")[0]);
				String temp = sentence.get(i + 1).split("_")[0];
				for (int j = i + 2; j < sentence.size() && sentence.get(j).split("_")[1].startsWith("N"); j++) {
					temp += sentence.get(j).split("_")[0];
					i = j;
				}
				last.add(temp);
				
			}
			i++;
		}
		if (last.isEmpty()) {
			i = word.no + 1;
			while (i < sentence.size()) {
				if (sentence.get(i).endsWith("VA")) {
					last.add(sentence.get(i).split("_")[0]);
					break;
				}
				i++;
			}
		}
		s += findFirst(word) + "@";
		String temp = findSecond(word);
		for (String s1 : third) {
			if (temp.contains(s1)) {
				temp = temp.substring(0, temp.indexOf(s1)) + temp.substring(temp.indexOf(s1) + s1.length(), temp.length()); 
				break;
			}
		}
		s += temp + "@";
		for (String s1 : third)
			s += s1 + " ";
		if (s.endsWith(" ")) s = s.substring(0, s.length() - 1);
		s += "@@";
		for (String s1 : last)
			s += s1 + " ";
		s = s.substring(0, s.length() - 1);
		return s;
	}
	
	public static String print(Tree node, List<String> sentence) {
		return findThird(node, sentence);// + "@@" + findLast(node));
	}
}
