package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class FenbuTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(10);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		for (Tree w : word) {
			if (w.parent.content.startsWith("V") && w.no < sentence.size() && !sentence.get(w.no).startsWith("上限") && !sentence.get(w.no).startsWith("范围") && !sentence.get(w.no).startsWith("特点") && !sentence.get(w.no).startsWith("特征")) {
				if (w.content.equals("出现") && !sentence.get(w.no).startsWith("在"))
					continue;
				String s = FenbuSolution.print(w, sentence);
				if (!s.startsWith("@")) template +=  w.no + "+" + s + "\t";
			}
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
