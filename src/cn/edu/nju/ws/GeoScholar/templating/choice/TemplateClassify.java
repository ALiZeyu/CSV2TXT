package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class TemplateClassify {

	private static boolean checksenzui(List<String> sentence) {
		for (String s : sentence)
			if (s.startsWith("最"))
				return true;
		return false;
	}
	
	public static Set<Integer> classify(Tree t, List<String> sentence) throws IOException {
		Set<Integer> set = new HashSet<Integer>();
		Set<String> s1 = Input.getWord(1);
		Set<String> s3 = Input.getWord(3);
		Set<String> s4 = Input.getWord(4);
		Set<String> s5 = Input.getWord(5);
		Set<String> s6 = Input.getWord(6);
		Set<String> s7 = Input.getWord(7);
		Set<String> s8 = Input.getWord(8);
		Set<String> s9 = Input.getWord(9);
		Set<String> s10 = Input.getWord(10);
		if (!findCueWord.getCueWords(s1, t).isEmpty() || checksenzui(sentence))
			set.add(1);
		if (!findCueWord.getCueWords(s3, t).isEmpty())
			set.add(3);
		if (!findCueWord.getCueWords(s4, t).isEmpty())
			set.add(4);
		if (!findCueWord.getCueWords(s5, t).isEmpty())
			set.add(5);
		if (!findCueWord.getCueWords(s6, t).isEmpty() && 
				(findCueWord.getCueWords(s6, t).size() > 1 || !Tree.findNodeByNo(t, 1).content.equals("为")))
			set.add(6);
		if (!findCueWord.getCueWords(s7, t).isEmpty())
			set.add(7);
		if (!findCueWord.getCueWords(s8, t).isEmpty())
			set.add(8);
		if (!findCueWord.getCueWords(s9, t).isEmpty())
			set.add(9);
		if (!findCueWord.getCueWords(s10, t).isEmpty())
			set.add(10);
		set.add(11);
		return set;
	}
}
