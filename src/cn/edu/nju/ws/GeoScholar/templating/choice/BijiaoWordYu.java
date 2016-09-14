package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.util.ArrayList;
import java.util.HashSet;

import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class BijiaoWordYu {

	public static String findFirst(ArrayList<String> sentence, Tree word) {
		String s = "";
		if (word.parent.parent.parent.content.equals("VP") && word.parent.parent.parent.child.get(0).content.equals("PP")) {
			Tree t = word.parent.parent.parent.parent.child.get(0);
			s = Print.print(findCueWord.findFirstNP(t, sentence));
		} else {
			int i = word.no - 2;
			if (sentence.get(i).split("_")[0].equals("远")) i--;
			if (sentence.get(i).split("_")[0].equals("略")) i--;
			while (sentence.get(i).split("_")[1].startsWith("N") || sentence.get(i).split("_")[1].startsWith("D")) {
				if (sentence.get(i).split("_")[1].equals("DT"))
					s = sentence.get(i).split("_")[0] + sentence.get(i + 1).split("_")[0];
				else if (!sentence.get(i).split("_")[1].startsWith("D"))
					s = sentence.get(i).split("_")[0];
				i--;
				if (i < 0) break;
			}
			i += 2;
			HashSet<String> set = new HashSet<String>();
			set.add("①");
			set.add("②");
			set.add("④");
			set.add("③");
			if (set.contains(sentence.get(i).split("_")[0])) s += sentence.get(i).split("_")[0];
		}
		return s;
	}
	
	public static String findSecond(ArrayList<String> sentence, Tree word) {
		String s = "";
		if (word.parent.parent.child.get(1).content.equals("NP")) {
			s = Print.print(word.parent.parent.child.get(1));
		} else {
			String cueWord = word.content + "_" + word.parent.content;
			int index = sentence.indexOf(cueWord);
			int i = index + 1;
			while (sentence.get(i).split("_")[1].startsWith("N") || sentence.get(i).split("_")[1].startsWith("D")) {
				s += sentence.get(i).split("_")[0];
				i++;
				if (i >= sentence.size()) break;
			}
		}
		return s;
	}
	
	public static String findThird(ArrayList<String> sentence, Tree word) {
		String s = "";
		int i = word.no - 2;
		if (sentence.get(i).split("_")[0].equals("远")) i--;
		if (sentence.get(i).split("_")[0].equals("略")) i--;
		HashSet<String> set = new HashSet<String>();
		set.add("①");
		set.add("②");
		set.add("④");
		set.add("③");
		while (sentence.get(i - 1).split("_")[1].startsWith("N") || (i >= 2 && sentence.get(i - 1).split("_")[1].startsWith("D") && sentence.get(i - 2).split("_")[1].startsWith("N"))) {
			if (set.contains(sentence.get(i).split("_")[0])) break;
			s = sentence.get(i).split("_")[0] + s;
			i--;
			if (i <= 0) break;
		}
		return s;
	}
	
	public static String findLast(Tree word) {
		return word.content.substring(0, word.content.length() - 1);
	}
	
	public static String print(ArrayList<String> sentence, Tree node) {
		return (findFirst(sentence, node) + "@" + findSecond(sentence, node) + "@" + findThird(sentence, node) + "@@" + findLast(node));
	}
}
