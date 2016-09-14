package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.nju.ws.GeoScholar.common.NlpOutput;
import cn.edu.nju.ws.GeoScholar.templating.common.QuestionTemplateFromNLP;
import cn.edu.nju.ws.GeoScholar.templating.common.SlotStructureFromNLP;

public class Candidate {

	private static class Check {
		int start;
		int end;
		
		public Check(QuestionTemplateFromNLP qt) {
			start = 99;
			end = -1;
			for (SlotStructureFromNLP ss : qt.slots) 
				if (ss != null) {
					if (ss.startOffset < start)
						start = ss.startOffset;
					if (ss.endOffset > end)
						end = ss.endOffset;
				}
		}
	}
	
	private static class Type {
		String type;
		String first;
		int cueword;
		
		public Type(String t, String f, int c) {
			type = t;
			first = f;
			cueword = c;
		}
		
		public boolean equals(Type t) {
			return type.equals(t.type) && (first.equals(t.type) || (cueword > 0 && cueword == t.cueword));
		}
		
	}
	
	public static ArrayList<NlpOutput> chooseCandidate(NlpOutput nl) {
		List<Check> l = new ArrayList<Check>();
		for (QuestionTemplateFromNLP qt : nl.templates)
			l.add(new Check(qt));
		for (int i = 0; i < nl.templates.size() - 1; i++)
			for (int j = i; j < nl.templates.size(); j++)
				if (l.get(i).start > l.get(j).start) {
					Check ci = l.get(i);
					QuestionTemplateFromNLP qt = nl.templates.get(i);
					l.set(i, l.get(j));
					nl.templates.set(i, nl.templates.get(j));
					l.set(j, ci);
					nl.templates.set(j, qt);
				}
		List<List<QuestionTemplateFromNLP>> list = new ArrayList<List<QuestionTemplateFromNLP>>();
		for (int i = 0; i < l.size() - 1; i++)  {
			Check ci = l.get(i);
			for (int j = i + 1; j < l.size(); j++) {
				Check cj = l.get(j);
				if (!(nl.templates.get(i).templateType.equals("时间限定") || nl.templates.get(j).templateType.equals("时间限定"))
					&& !(cj.start > ci.end || ci.start > cj.end) && nl.templates.get(i).cueWord != nl.templates.get(j).cueWord) {
					boolean b = true;
					for (int k = 0; k < list.size(); k++) {
						if (list.get(k).contains(nl.templates.get(i)) && !list.get(k).contains(nl.templates.get(j))) { 
							list.get(k).add(nl.templates.get(j));
							b = false;
						} else if (!list.get(k).contains(nl.templates.get(i)) && list.get(k).contains(nl.templates.get(j))) { 
							list.get(k).add(nl.templates.get(i));
							b = false;
						} else if (list.get(k).contains(nl.templates.get(i)) && list.get(k).contains(nl.templates.get(j))) {
							b = false;
						}
					}
					if (b) {
						List<QuestionTemplateFromNLP> temp = new ArrayList<QuestionTemplateFromNLP>();
						temp.add(nl.templates.get(i));
						temp.add(nl.templates.get(j));
						list.add(temp);
					}
				}
			}
		}
		ArrayList<NlpOutput> nllist = new ArrayList<NlpOutput>();
		if (list.isEmpty())
			nllist.add(nl);
		else {
			List<QuestionTemplateFromNLP> common = new ArrayList<QuestionTemplateFromNLP>();
			Set<QuestionTemplateFromNLP> same = new HashSet<QuestionTemplateFromNLP>();
			for (List<QuestionTemplateFromNLP> temp : list)
				for (QuestionTemplateFromNLP qt : nl.templates)
					if (temp.contains(qt))
						same.add(qt);
			for (QuestionTemplateFromNLP qt : nl.templates)
				if (!same.contains(qt))
					common.add(qt);
			Map<Integer, Type> typelist = new HashMap<Integer, Type>();
			for (QuestionTemplateFromNLP qt : list.get(0)) {
				if (qt.slots.get(0) == null) {
					list.get(0).remove(qt);
					continue;
				}
				Type type = new Type(qt.templateType, qt.slots.get(0).content, qt.cueWord);
				boolean b = true;
				for (int i : typelist.keySet())
					if (typelist.get(i).equals(type)) {
						nllist.get(i).templates.add(qt);
						b = false;
						break;
					}
				if (b) {
					NlpOutput nlt = new NlpOutput();
					nlt.pos = nl.pos;
					nlt.syntaxTree = nl.syntaxTree;
					nlt.dependencyTree = nl.dependencyTree;
					nlt.templates = new ArrayList<QuestionTemplateFromNLP>();
					nlt.templates.add(qt);
					nllist.add(nlt);
					typelist.put(nllist.indexOf(nlt), new Type(qt.templateType, qt.slots.get(0).content, qt.cueWord));
				}
			}
			for (int i = 1; i < list.size(); i++) {
				ArrayList<NlpOutput> tempnllist = new ArrayList<NlpOutput>();
				typelist = new HashMap<Integer, Type>();
				for (QuestionTemplateFromNLP qt : list.get(i)) {
					if (qt.slots.get(i) == null) {
						list.get(i).remove(qt);
						continue;
					}
					Type type = new Type(qt.templateType, qt.slots.get(0).content, qt.cueWord);
					boolean b = true;
					for (int j : typelist.keySet())
						if (typelist.get(j).equals(type)) {
							tempnllist.get(j).templates.add(qt);
							b = false;
							break;
						}
					if (b) {
						NlpOutput nlt = new NlpOutput();
						nlt.templates = new ArrayList<QuestionTemplateFromNLP>();
						nlt.templates.add(qt);
						tempnllist.add(nlt);
						typelist.put(tempnllist.indexOf(nlt), new Type(qt.templateType, qt.slots.get(0).content, qt.cueWord));
					}
				}
				int size = nllist.size();
				for (int j = 0; j < tempnllist.size() - 1; j++)
					for (int k = 0; k < size; k++) {
						NlpOutput no = nllist.get(k);
						NlpOutput nlt = new NlpOutput();
						nlt.pos = no.pos;
						nlt.syntaxTree = no.syntaxTree;
						nlt.dependencyTree = no.dependencyTree;
						nlt.templates = new ArrayList<QuestionTemplateFromNLP>();
						nlt.templates.addAll(no.templates);
						nllist.add(nlt);
					}
				for (int j = 0; j < nllist.size(); j += size)
					for (int k = j; k < j + size; k++)
						nllist.get(k).templates.addAll(tempnllist.get(k / size).templates);
			}
			for (NlpOutput no : nllist)
				no.templates.addAll(common);
		}
		for (NlpOutput no : nllist) {
			for (int i = 1; i < no.templates.size(); i++) {
				QuestionTemplateFromNLP qt  = no.templates.get(i);
				if (qt.templateType.equals("实体信息陈述") && qt.slots.get(1) == null && !no.templates.get(i - 1).templateType.equals("时间限定")) {
					qt.slots.set(1, qt.slots.get(0));
					qt.slots.set(0, no.templates.get(i - 1).slots.get(0));
				}
			}
		}
		/*for (NlpOutput no : nllist) {
			for (QuestionTemplateFromNLP qt : no.templates)
				for (SlotStructureFromNLP ss : qt.slots)
					checkRepeat(ss);
		}*/
		if (nllist.get(0).templates.get(0).templateType.equals("匹配")) {
			NlpOutput no = nllist.get(0);
			for (int i = 0; i < nllist.size() - 1; i++)
				nllist.set(i, nllist.get(i + 1));
			nllist.set(nllist.size() - 1, no);
		}
		if (nllist.get(0).templates.get(0).templateType.equals("实体信息陈述")) {
			NlpOutput no = nllist.get(0);
			for (int i = 0; i < nllist.size() - 1; i++)
				nllist.set(i, nllist.get(i + 1));
			nllist.set(nllist.size() - 1, no);
		}
		for (NlpOutput no : nllist) {
			if (no.templates.get(0).templateType.equals("因果")) {
				NlpOutput nlp = nllist.get(0);
				int i = nllist.indexOf(no);
				nllist.set(0, no);
				nllist.set(i, nlp);
				
			}
		}
		for (NlpOutput no : nllist)
			for (int j = 1; j < no.templates.size(); j++) {
			//for (QuestionTemplateFromNLP qt : no.templates) {
				QuestionTemplateFromNLP qt = no.templates.get(j);
				if (qt.templateType.equals("时间限定")) {
					int i = 0;
					while (no.templates.get(i).templateType.equals("时间限定")) i++;
					if (i < j) {
						QuestionTemplateFromNLP qt1 = no.templates.get(i);
						no.templates.set(i, qt);
						no.templates.set(j, qt1);
					}
				}
		}
		return nllist;
	}
	
	/*@SuppressWarnings("unchecked")
	private static void checkRepeat(SlotStructureFromNLP ss) {
		if (ss == null || !ss.isTemplate)
			return;
		if (((ArrayList<QuestionTemplateFromNLP>)ss.content).size() > 1) {
			List<QuestionTemplateFromNLP> l = new ArrayList<QuestionTemplateFromNLP>();
			for (QuestionTemplateFromNLP qt : ((ArrayList<QuestionTemplateFromNLP>)ss.content))
				if (qt.templateType.equals("实体信息陈述"))
					l.add(qt);
			if (l.size() < ((ArrayList<QuestionTemplateFromNLP>)ss.content).size())
				((ArrayList<QuestionTemplateFromNLP>)ss.content).removeAll(l);
			for (QuestionTemplateFromNLP qt : ((ArrayList<QuestionTemplateFromNLP>)ss.content))
				for (SlotStructureFromNLP ssf : qt.slots)
					checkRepeat(ssf);
		}
	}*/
}
