package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Print;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class WeiyuTemplate {
	
	Tree word = new Tree();
	
	public static List<String> findNP(Tree p) {
		List<String> l = new ArrayList<String>();
		if (p.content.equals("NP"))
			l.add(Print.print(p));
		else
			for (Tree t : p.child)
				l.addAll(findNP(t));
		return l;
	}
	
	public static String findPriorAndNext(Tree p) {
		Tree node = p;
		Tree position;
		do {
			position = node;
			node = node.parent;
		} while (node.child.size() == 1);
		
		List<String> next = new ArrayList<String>();
		while (node != null) {
			int index = node.child.indexOf(position);
			for (int i = index + 1; i < node.child.size(); i++) {
				Tree t = node.child.get(i);
				next.addAll(findNP(t));	
			}
			position = node;
			node = node.parent;
		}
		if (next.isEmpty()) {
			node = p;
			do {
				position = node;
				node = node.parent;
			} while (node.child.size() == 1);
			next.add(Print.print(node.child.get(1)));
		}
		String prior = "";
		node = p;
		position = null;
		for (int i = 0; i < 2; i++) {
			position = node;
			node = node.parent;
		}
		do {
			position = node;
			node = node.parent;
		} while (!node.content.equals("NP") && !node.content.equals("IP") && !(node.content.equals("VP") && node.child.get(0).content.equals("NP")));
		
		while (node.child.indexOf(position) > 0 && !node.child.get(0).content.equals("NP") && !node.child.get(node.child.indexOf(position) - 1).content.equals("NP")) {
			position = node;
			node = node.parent;
		}
		int k = node.child.indexOf(position) > 0 && node.child.get(node.child.indexOf(position) - 1).content.equals("NP") ? node.child.indexOf(position) - 1 : 0;
		if (node.child.get(k).child.size() > 1 && node.child.get(k).child.get(node.child.get(k).child.size() - 1).content.startsWith("D"))
			prior = Print.print(node.child.get(k).child.get(0));
		else
			prior = Print.print(node.child.get(k));
		return prior + "@" + next.get(0).split("，")[0];
	}
	
	public static String getTemplate(Tree t, List<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(3);
		List<Tree> w = findCueWord.getCueWords(cueword, t);
		String template = "";
		for (Tree word : w)
			if (word.no < sentence.size() && ((word.content.equals("位置") && Tree.findNodeByNo(t, word.no + 1).content.equals("是")) || word.parent.content.startsWith("V") 
					|| (word.parent.parent.parent.parent != null && word.parent.parent.parent.parent.parent != null  && word.content.equals("在") && word.parent.parent.parent.parent.parent.content.equals("ROOT")))) {
				String s = findPriorAndNext(word);
				if (!s.isEmpty() && s.contains("、")) {
					String[] temp = s.split("@")[1].split("、");
					String s1 = s.split("@")[0];
					for (int i = 0; i < temp.length; i++) {
						template += word.no + "+";
						template += s1.contains("、") ? s1.split("、")[i] : s1.charAt(i) + "";
						template += "@" + temp[i] + "\t";
					}
				} else
					template += word.no + "+" + s + "\t";
			}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
