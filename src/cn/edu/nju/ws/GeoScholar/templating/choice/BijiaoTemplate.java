package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.Segment;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.templating.common.findCueWord;

public class BijiaoTemplate {

	public static String getTemplate(Tree t, ArrayList<String> sentence) throws IOException {
		Set<String> cueword = Input.getWord(4);
		List<Tree> word = findCueWord.getCueWords(cueword, t);
		String template = "";
		Set<String> words = new HashSet<String>();
		for (String s : sentence)
			words.add(s.split("_")[0]);
		for (Tree w : word) {
			if (w.content.equals("相比")) {
				String s = BijiaoWordXiangbi.print(sentence, w);
				if (s.contains(" ")) {
					String[] temp = s.split("@");
					String[] temp1 = temp[2].split(" ");
					String[] temp2 = temp[4].split(" ");
					int max = temp1.length > temp2.length ? temp1.length : temp2.length;
					for (int i = 0; i < max; i++) {
						template += w.no + "+";
						template += temp[0] + "@" + temp[1] + "@";
						template += i < temp1.length ? temp1[i] : temp1[temp1.length - 1];
						template += "@@";
						template += i < temp2.length ? temp2[i] : temp2[temp2.length - 1];
						template += "\t";
					}
				}
				else
					template += template += w.no + "+" + s + "\t";
			}
			else
			if (words.contains("与") && (w.content.equals("相当") || w.content.equals("一致") || w.content.equals("相同") || w.content.equals("不同") || w.content.equals("相反")))
				template += w.no + "+" + BijiaoWordXiangtong.print(sentence, w) + "\t";
			else
			if (w.content.equals("同时"))
				template += w.no + "+" + BijiaoWordTongshi.print(sentence, w) + "\t";
			else
			if (w.content.endsWith("于"))
				template += w.no + "+" + BijiaoWordYu.print(sentence, w) + "\t";
			else
			if (w.parent.content.equals("CC"))
				template += w.no + "+" + BijiaoPosCC.print(w) + "\t";
			else
			if (word.contains("在") && (w.no == sentence.size() || sentence.get(w.no).endsWith("PU"))&& w.parent.content.equals("LC"))
				template += w.no + "+" + BijiaoPosLC.print(w) + "\t";
			else
			if (w.parent.content.equals("P")) {
				String s = BijiaoPosPP.print(w, sentence);
				if (s.contains(" ")) {
					String[] temp = s.split("@");
					String[] temp1 = temp[2].split(" ");
					String[] temp2 = temp[4].split(" ");
					int max = temp1.length > temp2.length ? temp1.length : temp2.length;
					for (int i = 0; i < max; i++) {
						template += w.no + "+"; 
						template += temp[0] + "@" + temp[1] + "@";
						template += i < temp1.length ? temp1[i] : temp1[temp1.length - 1];
						template += "@@";
						template += i < temp2.length ? temp2[i] : temp2[temp2.length - 1];
						template += "\t";
					}
				}
				else
					template += w.no + "+" + s + "\t";
			}
		}
		if (!template.isEmpty()) template = template.substring(0, template.length() - 1);
		String[] list = template.split("\t");
		template = "";
		for (String s : list)
			if (s.contains("@@@")) {
				String no = s.split("\\+")[0];
				s = s.substring(s.indexOf("+") + 1);
				List<String> l1 = Segment.j.seg_postag(s.split("@")[0]);
				List<String> l2 = Segment.j.seg_postag(s.split("@")[1]);
				if (l1.size() > l2.size()) {
				//	System.out.println(sentence);
					template += no + "+";
					List<String> split_result=splitTemplate(l1, l2);
//					for (int i = l1.size() - l2.size(); i < l1.size(); i++)
//						template += l1.get(i).split("_")[0];
					template +=split_result.get(0)+"@";
					template += s.split("@")[1] + "@";
//					for (int i = 0; i < l1.size() - l2.size(); i++)
//						template += l1.get(i).split("_")[0];
					template +=split_result.get(1);
					template += "@@" + s.substring(s.lastIndexOf("@") + 1) + "\t";
				} else if (l1.size() < l2.size()) {
					template += no + "+";
					template += s.split("@")[0] + "@";
					for (int i = 0; i < l1.size(); i++)
						template += l2.get(i).split("_")[0];
					template += "@";
					for (int i = l1.size(); i < l2.size(); i++)
						template += l2.get(i).split("_")[0];
					template += "@@" + s.substring(s.lastIndexOf("@") + 1) + "\t";
				} else {
					int k1 = 0, k2 = 0;
					for (int i = 0; i < l1.size(); i++)
						for (int j = 0; j < l2.size(); j++)
							if (l1.get(i).equals(l2.get(j))) {
								k1 = i;
								k2 = j;
								break;
							}
					while (l1.get(k1).equals(l2.get(k2))) {
						k1++;
						k2++;
					}
					if (k1 == l1.size() - 1) {
						template += no + "+";
						template += s.split("@")[0] + "@";
						for (int i = 0; i <= k2; i++)
							template += l2.get(i).split("_")[0];
						template += "@";
						for (int i = k2 + 1; i < l2.size(); i++)
							template += l2.get(i).split("_")[0];
						template += "@@" + s.substring(s.lastIndexOf("@") + 1) + "\t";
					} else {
						template += no + "+";
						for (int i = 0; i <= k1; i++)
							template += l1.get(i).split("_")[0];
						template += "@";
						template += s.split("@")[1] + "@";
						for (int i = k1 + 1; i < l1.size(); i++)
							template += l1.get(i).split("_")[0];
						template += "@@" + s.substring(s.lastIndexOf("@") + 1) + "\t";
					}
				}
			} else
				template += s + "\t";
		return template;
	}
	/*
	 * 用于处理“该线路北京游客的规模较宁夏的小”这样的句子中提出北京和宁夏这样对应的
	 * 1、如果槽二NR：“北极村自转线速度比大连大”，区槽一找对应的NR
	 * 2、如果含有的“①的水温变化比②大”按“的分开”
	 * 3、槽二含有：可以利用规则匹配：“图中甲地1月份降水量比7月份的多”
	 * 4、利用词的相似性，比如第一个字、最后一个字这样非常不靠谱的
	 * 局限性：甲地人口迁出比例直辖市比省级行政中心高
	 * excuse me？直辖市省级中心怎么匹配
	 */
	public static List<String> splitTemplate(List<String> l1, List<String> l2){
		//为了特殊处理：则E处气温比H处高，多了一个则
		if(l1.size()>1 && l1.get(0).split("_")[1].equals("AD"))
			l1.remove(0);
		
		List<String> result = new ArrayList<String>();
		String first="",second="";
	//	int de_index = -1;
		List<String> l2rule=new ArrayList<String>();
		List<Integer> indexes= new ArrayList<Integer>();
		int l2nr=isNR(l2);
		
		if (l2nr!=-1) {
			int i;
			for(i = 0; i < l1.size() ; i++){
				if(l1.get(i).split("_")[1].equals("NR"))
					break;
			}
			if(i < l1.size()){
				int gap = i - l2nr;
				for (int j = 0; j < l2.size(); j++) {
					if (gap + j >= 0 && gap + j < l1.size())
						first += l1.get(j + gap).split("_")[0];
				}

				String s = listPrint(l1);
				second = s.replace(first, "");
				if(second.startsWith("的"))
					second=second.substring(1, second.length());
				result.add(first);
				result.add(second);
			}
		}
//		else if((l1.contains("的_DEG")||l1.contains("的_DEC")) && (de_index=get_de_index(l1, l2))!=-1){
//			if(de_index<l1.size()-1){
//				for(int i=0;i<de_index;i++)
//					first+=l1.get(i).split("_")[0];
//				for(int i=de_index+1;i<l1.size();i++)
//					second+=l1.get(i).split("_")[0];
//				result.add(first);
//				result.add(second);
//			}
//		}
		else if ((l2rule=isRule(l2))!=null) {
			String re=l2rule.get(0);
			Pattern p=Pattern.compile(re);
			int index=Integer.parseInt(l2rule.get(1));
			int i;
			for(i = 0; i < l1.size() ; i++){
				if(p.matcher(l1.get(i).split("_")[0]).matches())
					break;
			}
			if(i < l1.size()){
				int gap = i - index;
				for (int j = 0; j < l2.size(); j++) {
					if (gap + j >= 0 && gap + j < l1.size())
						first += l1.get(j + gap).split("_")[0];
				}

				String s = listPrint(l1);
				second = s.replace(first, "");
				result.add(first);
				if(second.startsWith("的"))
					second=second.substring(1, second.length());
				result.add(second);
			}
		}else if((indexes = get_similar_words(l1, l2))!=null){
			int w2 = indexes.get(0);
			int w1 = indexes.get(1);
			int gap = w1 - w2;
			for (int j = 0; j < l2.size(); j++) {
				if (gap + j >= 0 && gap + j < l1.size())
					first += l1.get(j + gap).split("_")[0];
			}

			String s = listPrint(l1);
			second = s.replace(first, "");
			if(second.startsWith("的"))
				second=second.substring(1, second.length());
			result.add(first);
			result.add(second);
		}
		if(result.size()==0){
			for (int i = l1.size() - l2.size(); i < l1.size(); i++)
				first += l1.get(i).split("_")[0];
			for (int i = 0; i < l1.size() - l2.size(); i++)
				second += l1.get(i).split("_")[0];
			result.add(first);
			result.add(second);
		}
		return result;
	}
	
//	public static int get_de_index(List<String> l1, List<String> l2){
//		int index=l1.contains("的_DEG")?l1.indexOf("的_DEG"):l1.indexOf("的_DEC");
//		if(index!=l2.size())
//			return -1;
//		return index;
//	}
	
	public static int isNR(List<String> l2){
		int flag=-1;
		int i;
		for(i=0;i<l2.size();i++){
			if(l2.get(i).split("_")[1].equals("NR")){
				flag=i;
				break;
			}
		}
		if(i<l2.size()-1 && l2.get(i+1).split("_")[0].equals("的"))
			l2.remove(i+1);
		return flag;
	}
	
	public static List<String> isRule(List<String> l2){
		List<String> result=new ArrayList<String>();
		String[] pattern_str = { 
				"①|②|③|④|⑤",
				"[A-Za-z0-9\\.\\+\\_]*[A-Za-z0-9][分|秒|世|年|月|日|时|万|亿|%|％|℃]{0,1}[纪|份|代]{0,1}",
				"([图|表])([\\d|甲|乙|丙|丁])+([\\（|\\(][a|b|c|d|A|B|C|D][\\）|\\)])*",
				"[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]]{1,2}(类|图|国|河流|河|市|县|镇|村|点|线|地|区域|区|阶段)",
				"[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]][城省处]",
				"(洋流|气流|环节)[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]]"
				};
		int i;
		
		for(i=0;i<l2.size();i++){
			for(String str:pattern_str){
				Pattern p=Pattern.compile(str);
				if(p.matcher(l2.get(i).split("_")[0]).matches()){
					result.add(str);
					result.add(String.valueOf(i));
					if(i<l2.size()-1 && l2.get(i+1).split("_")[0].equals("的"))
						l2.remove(i+1);
					return result;
				}
			}
		}
		return null;
	}
	
	public static String listPrint(List<String> l){
		String result="";
		for(int i=0;i<l.size();i++)
			result+=l.get(i).split("_")[0];
		return result;
	}
	//衡量两个词语的相似程度，按照完全相同，前后缀两个字相同、前后缀一个字相同的顺序
	public static List<Integer> get_similar_words(List<String> l1, List<String> l2){
		List<Integer> result=new ArrayList<Integer>();
		for(int i=0;i<l2.size();i++)
			for(int j=0;j<l1.size();j++){
				if(l2.get(i).split("_")[0].equals(l1.get(j).split("_")[0])){
					result.add(i);
					if(i<l2.size()-1 && l2.get(i+1).split("_")[0].equals("的"))
						l2.remove(i+1);
					result.add(j);
					return result;
				}
			}
		for(int i=0;i<l2.size();i++)
			for(int j=0;j<l1.size();j++)
				for(int len=2;len>0;len--){
					if(word_similarity(len, l2.get(i).split("_")[0], l1.get(j).split("_")[0])){
						result.add(i);
						if(i<l2.size()-1 && l2.get(i+1).split("_")[0].equals("的"))
							l2.remove(i+1);
						result.add(j);
						return result;
					}
			}
		return null;
	}
	public static boolean word_similarity(int len,String word1, String word2){
		if(!(word1.length()>=len)||!(word2.length()>=len))
			return false;
		String w1=word1.substring(0, len);
		String w2=word2.substring(0, len);
		if(w1.equals(w2))
			return true;
		w1=word1.substring(word1.length()-len, word1.length());
		w2=word2.substring(word2.length()-len, word2.length());
		if(w1.equals(w2))
			return true;
		return false;
	}
}
