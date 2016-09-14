package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class ZuizhiTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(1);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		for (String s : sentence)
			if (s.split("_")[0].length() < 3 && !s.endsWith("CD") && s.startsWith("最") && !word.contains(Tree.findNodeByNo(t, sentence.indexOf(s) + 1))) word.add(Tree.findNodeByNo(t, sentence.indexOf(s) + 1));
		for (Tree w : word) {
			if (w.no == sentence.size() || (!sentence.get(w.no).split("_")[0].equals("有") && !sentence.get(w.no).contains("主要") && !sentence.get(w.no).contains("能")))
				template += w.no + "+" + ZuizhiSolution.print(w, sentence) + "\t";
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
