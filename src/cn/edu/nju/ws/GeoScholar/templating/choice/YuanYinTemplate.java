package cn.edu.nju.ws.GeoScholar.templating.choice;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class YuanYinTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		if (sentence.get(0).startsWith("影响")) return "";
		Set<String> cueword = Input.getWord(7);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		Set<String> words = new HashSet<String>();
		for (String s : sentence)
			words.add(s.split("_")[0]);
		String template = "";
		for (Tree w : word) {
			if (w.content.equals("原因") || w.content.equals("优势") || w.content.equals("成因"))
				template += w.no + "+" + YuanYinSolution2.print(w, sentence) + "\t";
			else if ((w.content.equals("因") || w.content.equals("因为")) && (w.no < 2 || !(sentence.get(w.no - 2).startsWith("是") || sentence.get(w.no - 2).startsWith("主要"))))
				template += w.no + "+" + YuanYinSolution1.print(w, sentence) + "\t";
			else if (w.content.equals("由于") || w.content.equals("因为"))
				template += w.no + "+" + YuanYinSolution4.print(w, sentence) + "\t";
			else if (w.no > 1 && w.parent.content.startsWith("V") && !(w.content.equals("形成") || w.content.equals("成")) && (w.no < sentence.size() && !sentence.get(w.no).startsWith("的")))
				template += w.no + "+" + YuanYinSolution5.print(w, sentence) + "\t";
			else if (w.content.equals("如果") && !words.contains("导致") && !words.contains("造成") && words.contains("，"))
				template += w.no + "+" + YuanYinSolution6.print(w, sentence) + "\t";
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
