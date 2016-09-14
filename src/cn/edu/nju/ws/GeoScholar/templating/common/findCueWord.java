package cn.edu.nju.ws.GeoScholar.templating.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class findCueWord {
	
	static List<Tree> word = new ArrayList<Tree>();
	
	private static void find(Set<String> s, Tree p) {
		if (!p.child.isEmpty())
			for (Tree q : p.child)
				find(s, q);	
		else
		if (s.contains(p.content))
			word.add(p);
	}
	
	private static void findCueWords(Set<String> s, Tree p) {
		word = new ArrayList<Tree>();
		find(s, p);
	}
	
	public static ArrayList<Tree> getCueWords(Set<String> s, Tree p) {
		findCueWords(s, p);
		return (ArrayList<Tree>) word;
	}
	
	public static Tree findFirstNP(Tree p, List<String> sentence) {
		if (p.content.equals("NP") || p.content.equals("DP")) 
			return p;
		else {
			List<Tree> l = new ArrayList<Tree>();
			l.addAll(p.child);
			while (!l.isEmpty()) {
				List<Tree> list = new ArrayList<Tree>();
				for (Tree t : l)
					if (t.content.equals("NP") || t.content.equals("DP")) 
						return t;
					else
						list.addAll(t.child);
				l = list;
			}
		}
		for (int i = p.no - 2; i >= 0; i++)
			if (sentence.get(i).split("_")[1].startsWith("N"))
				return Tree.findNodeByNo(p, i + 1);
		return null;
	}
	
	public static Tree findFirstLCP(Tree p, List<String> sentence) {
		if (p.content.equals("LCP")) 
			return p;
		else {
			List<Tree> l = new ArrayList<Tree>();
			l.addAll(p.child);
			while (!l.isEmpty()) {
				List<Tree> list = new ArrayList<Tree>();
				for (Tree t : l)
					if (t.content.equals("LCP")) 
						return t;
					else
						list.addAll(t.child);
				l = list;
			}
		}
		return null;
	}
}
