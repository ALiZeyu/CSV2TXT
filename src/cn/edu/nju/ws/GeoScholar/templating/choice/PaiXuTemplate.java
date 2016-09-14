package cn.edu.nju.ws.GeoScholar.templating.choice;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class PaiXuTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(9);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		Set<String> words = new HashSet<String>();
		for (String s : sentence)
			words.add(s.split("_")[0]);
		for (Tree w : word) {
			if ((words.contains("是") || words.contains("为") || words.contains("有")) && 
					(words.contains("至") || words.contains("到")) &&
					(words.contains("从") || words.contains("由")))
				template += w.no + "+" + PaiXuSolution.print(w, sentence) + "\t";
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
