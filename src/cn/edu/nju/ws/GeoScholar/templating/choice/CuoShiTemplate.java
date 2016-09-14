package cn.edu.nju.ws.GeoScholar.templating.choice;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class CuoShiTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(8);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		Set<String> words = new HashSet<String>();
		for (String s : sentence)
			words.add(s.split("_")[0]);
		for (Tree w : word) {
			if (w.content.startsWith("应") && !sentence.get(w.no).equals("为") && !sentence.get(w.no).equals("是"))
				template += w.no + "+" + CuoShiSolution1.print(w, sentence) + "\t";
			else if (!words.contains("应") && !words.contains("应该"))
				template += w.no + "+" + CuoShiSolution2.print(w, sentence) + "\t";
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
