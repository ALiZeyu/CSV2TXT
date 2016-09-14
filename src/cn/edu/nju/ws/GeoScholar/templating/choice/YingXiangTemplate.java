package cn.edu.nju.ws.GeoScholar.templating.choice;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class YingXiangTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(5);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		Set<String> words = new HashSet<String>();
		for (String s : sentence)
			words.add(s.split("_")[0]);
		for (Tree w : word) {
			if (w.content.equals("影响")) {
				if (w.no == 1 || sentence.get(w.no - 2).startsWith("，"))
					template += w.no + "+" + YingXiangSolution8.print(w, sentence) + "\t";
				else if (words.contains("受") || words.contains("受到"))
					template += w.no + "+" + YingXiangSolution1.print(w, sentence) + "\t";
				else if (words.contains("对"))
					template += w.no + "+" + YingXiangSolution4.print(w, sentence) + "\t";
				else if (words.contains("给") && words.contains("带来"))
					template += w.no + "+" + YingXiangSolution2.print(w, sentence) + "\t";
				else
					template += w.no + "+" + YingXiangSolution6.print(w, sentence) + "\t";
			} 
			else if ((words.contains("由") || (words.contains("是")))
					&& !words.contains("原因") && !words.contains("成因")
					&& (w.content.equals("形成") || w.content.equals("成"))
					&& w.no > 1
					&& (w.no == sentence.size() || (!sentence.get(w.no - 2).startsWith("的") && !sentence.get(w.no).startsWith("过程")) || (w.no == sentence.size() - 1 && sentence.get(w.no).startsWith("的"))))
				template += w.no + "+" + YuanYinSolution3.print(w, sentence) + "\t";
			else if ((w.no == 1 || !sentence.get(w.no - 2).startsWith("应")) && w.content.equals("控制") || w.content.equals("侵扰"))
				template += w.no + "+" + YingXiangSolution1.print(w, sentence) + "\t";
			else if (w.content.equals("带来") && !words.contains("影响"))
				template += w.no + "+" + YingXiangSolution2.print(w, sentence) + "\t";
			else if (w.no < sentence.size() && !sentence.get(w.no).startsWith("供") && !sentence.get(w.no).startsWith("至") && !sentence.get(w.no).startsWith("达") && (w.no == 1 || !sentence.get(w.no - 2).startsWith("图")) && (w.content.equals("可") || w.content.equals("使") || w.content.contains("利于") || w.content.equals("有助于")))
				template += w.no + "+" + YingXiangSolution3.print(w, sentence) + "\t";
			else if (words.contains("对") && w.content.equals("重要"))
				template += w.no + "+" + YingXiangSolution4.print(w, sentence) + "\t";
			else if ((w.content.equals("有关") || w.content.equals("相关") || w.content.equals("无关")) && words.contains("与"))
				template += w.no + "+" + YingXiangSolution5.print(w, sentence) + "\t";
			else if (w.content.equals("随着") && w.no == 1)
				template += w.no + "+" + YingXiangSolution7.print(w, sentence) + "\t";
			if (template.endsWith("形成@\t"))
				template = template.substring(0, template.length() - 4) + "@形成\t";
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		return template;
	}
}
