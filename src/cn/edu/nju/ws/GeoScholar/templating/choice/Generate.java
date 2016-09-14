package cn.edu.nju.ws.GeoScholar.templating.choice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.edu.nju.ws.GeoScholar.common.NlpOutput;
import cn.edu.nju.ws.GeoScholar.templating.common.DepTree;
import cn.edu.nju.ws.GeoScholar.templating.common.Depparser;
import cn.edu.nju.ws.GeoScholar.templating.common.Input;
import cn.edu.nju.ws.GeoScholar.templating.common.MyUtil;
import cn.edu.nju.ws.GeoScholar.templating.common.QuestionTemplateFromNLP;
import cn.edu.nju.ws.GeoScholar.templating.common.Segment;
import cn.edu.nju.ws.GeoScholar.templating.common.SlotStructureFromNLP;
import cn.edu.nju.ws.GeoScholar.templating.common.SyntaxParser;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;

public class Generate {
	private static final Logger LOGGER = Logger.getLogger(Generate.class);
	private static final boolean DEBUG = false;
	
	static class Template {
		int type;
		String template = "";
		
		public Template(int type, String template, String sentence) throws IOException {
			this.type = 0;
			Set<String> cueword = Input.getWord(2);
			if (type < 11)
				this.type = type;
			else {
				String s = template.substring(template.indexOf("@") + 1, template.length());
				if (s.contains("@")) s.replaceAll("@", "");
				List<String> l = Segment.j.seg_postag(s);
				for (String w : cueword)
					for (String t : l)
						if (t.split("_")[0].equals(w)) this.type = 2;
				if (this.type == 0)
					this.type = 11;
			}
			if (this.type == 2) {
				String[] s = template.split("@");
				if (s.length == 3) {
					int i = 2;
					if (!s[1].isEmpty())
						for (String w : cueword)
							if (s[1].contains(w)) {
								i = 1;
								break;
							}
					this.template += s[0] + "@" + s[3 - i] + "@@" + s[i];
				} else {
					int i = 3;
					if (!s[1].isEmpty())
						for (String w : cueword)
							if (s[1].contains(w))  {
								i = 1;
								break;
							}
					if (!s[3].isEmpty())
						for (String w : cueword)
							if (s[3].contains(w))  {
								i = 3;
								break;
							}
					this.template += s[0] + "@" + s[4 - i] + "@" + s[2] + "@" + s[i];
				}
				s = this.template.split("@");
				if (s[2].startsWith("随")) {
					String s1 = s[2].substring(1);
					if (s1.startsWith("着"))
						s1 = s1.substring(1);
					s1 = s[0].split("\\+")[0] + "+" + s1;
					String s2 = s[0].split("\\+")[1] + s[1] + s[3];
					this.template = s1 + "@" +s2;
					this.type = 7;
				} else if (s[3].startsWith("随")) {
					String s1 = "";
					List<String> l = Segment.j.seg_postag(s[3]);
					s1 = s[0].split("\\+")[0] + "+" + l.get(1).split("_")[0];
					String s2 = s[0].split("\\+")[1] + s[1] + s[2];
					for (int i = 2; i < l.size(); i++)
						s2 += l.get(i).split("_")[0]; 
					this.template = s1 + "@" +s2;
					this.type = 7;
				}
			}
			else if (type == 11) {
				String[] s = template.split("@");
				if (s.length > 3)
					if (sentence.contains(s[2] + s[1]))
						this.template = s[0] + "@" + s[2] + s[1] + "@" + s[3];
					else
						this.template = s[0] + "@" + s[1] + "@" + s[2] + s[3];
				else
					this.template = template;
			} else {
				this.template = template;
			}
		}
		
	}
	
	public static void init() throws IOException {
		Segment.init();
		SyntaxParser.init();
		Depparser.init();
	}
	
	public static ArrayList<NlpOutput> GenerateTemplate(String question) throws IOException {
		if (DEBUG) System.out.println(question);
		String[] te = question.split("@");
		if (te[0].endsWith("当日")) te[0] += "，";
		if (te[0].endsWith("时")) te[0] += "，";
		if (te[0].endsWith("相比")) te[0] += "，";
		String q = !te[0].contains("排序") && !te[0].contains("原因") && !te[0].contains("影响") && (te[0].contains("正确的") || te[0].contains("错误的") || te[0].contains("接近实际的") || (te[0].contains("下列") && te[0].endsWith("是") && !te[0].contains("最"))) ? "" : te[0];
		if (te.length > 1) q += "@@@"+te[1];
		while (q.contains("判断"))
			q = q.substring(q.indexOf("判断") + 2, q.length());
		while (q.contains("推断"))
			q = q.substring(q.indexOf("推断") + 2, q.length());
		while (q.contains("可知，"))
			q = q.substring(q.indexOf("可知，") + 3, q.length());
		while (q.contains("可以判断"))
			q = q.substring(q.indexOf("可以推断") + 4, q.length());
		while (q.contains("最有可能"))
			q = q.substring(0, q.indexOf("最有可能")) + q.substring(q.indexOf("最有可能") + 4, q.length());
		while (q.contains("有可能"))
			q = q.substring(0, q.indexOf("有可能")) + q.substring(q.indexOf("有可能") + 3, q.length());
		while (q.contains("最可能"))
			q = q.substring(0, q.indexOf("最可能")) + q.substring(q.indexOf("最可能") + 3, q.length());
		while (q.contains("可能"))
			q = q.substring(0, q.indexOf("可能")) + q.substring(q.indexOf("可能") + 2, q.length());
		while (q.contains("主要"))
			q = q.substring(0, q.indexOf("主要")) + q.substring(q.indexOf("主要") + 2, q.length());
		while (q.startsWith("，"))
			q = q.substring(1);
		if (q.endsWith("。"))
			q = q.substring(0, q.length() - 1);
		if (q.contains("。")) 
			q = q.substring(q.lastIndexOf("。") + 1);
		//int split=q.indexOf("@@@");
		q=q.replace("@@@", "");
		//输出分词词性标注结果是在下一句话的函数中。
		Segment.segmentQuestion(q);
		SyntaxParser.parsing();
		Input input = new Input();
		input.input("data/templating/syntax.txt");
		ArrayList<NlpOutput> l = new ArrayList<NlpOutput>();
		Tree t = input.getRoots();
		//System.out.println(t);
		if (DEBUG) System.out.println(t);
		ArrayList<String> sentence = input.getSentence();
		Set<Integer> type = TemplateClassify.classify(t, sentence);
		List<Template> templates = new ArrayList<Template>();
		for (int i : type) {
			try {
			String s = "";
			switch (i) {
			case 1:
				s = ZuizhiTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 3:
				s = WeiyuTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 4:
				s = BijiaoTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 5:
				s = YingXiangTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 6:
				s = PipeiTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 7:
				s = YuanYinTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 8:
				s = CuoShiTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 9:
				s = PaiXuTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			case 10:
				s = FenbuTemplate.getTemplate(t, sentence);
				if (!s.isEmpty())
					templates.add(new Template(i, s, q));
				break;
			default:
				s = ChenShuTemplate.getTemplate(t, sentence);
				if (!s.isEmpty()) {
					String[] st = s.split("\t");
					for (String st1 : st)
						templates.add(new Template(11, st1, q));
				}
				break;
			}
			} catch(Exception e) {
				LOGGER.warn(String.format("templating error: %s", e.getMessage()));
//				System.err.println("templating error!");
			}
		}
		List<String> time = TimeTemplate.getTemplate(te[0] + (te.length > 1 ? te[1] : ""));
		NlpOutput nl = new NlpOutput();
		nl.pos = new ArrayList<String>();
		nl.pos.addAll(sentence);
		nl.syntaxTree = t;
		nl.dependencyTree = input.depRoots;
		nl.templates = new ArrayList<QuestionTemplateFromNLP>();
		for (String s : time) {
			QuestionTemplateFromNLP geo = new QuestionTemplateFromNLP();
			geo.oriText = te[0] + "@" + (te.length > 1 ? te[1] : "");
			geo.syntaxTreeLeaves = Tree.getLeaves(t);
			geo.templateType = "时间限定";
			geo.slotCount = 1;
			geo.cueWord = -2;
			geo.slots = new ArrayList<SlotStructureFromNLP>();
			SlotStructureFromNLP aslot = new SlotStructureFromNLP();
			//aslot.isTemplate = false;
			aslot.content = s;
			geo.slots.add(aslot);
			nl.templates.add(geo);
		}
		if (templates.isEmpty()) {
			QuestionTemplateFromNLP geo = new QuestionTemplateFromNLP();
			geo.oriText = te[0] + "@" + (te.length > 1 ? te[1] : "");
			geo.syntaxTreeLeaves = Tree.getLeaves(t);
			geo.templateType = "一般陈述";
			geo.slotCount = 1;
			geo.cueWord = -3;
			geo.slots = new ArrayList<SlotStructureFromNLP>();
			SlotStructureFromNLP aslot = new SlotStructureFromNLP();
			//aslot.isTemplate = false;
			aslot.content = geo.oriText;
			aslot.startOffset = 0;
			aslot.endOffset = sentence.size() - 1;
			aslot.syntaxNodes = new ArrayList<Tree>();
			aslot.depNodes = new ArrayList<DepTree>();
			for (int i = aslot.startOffset + 1; i <= aslot.endOffset + 1; i++) {
				aslot.syntaxNodes.add(Tree.findNodeByNo(t, i));
				aslot.depNodes.add(DepTree.findNodeByNo(input.depRoots, i));
			}
			geo.slots.add(aslot);
			nl.templates.add(geo);
		} else 
		for (Template template : templates) {
			String[] temp = template.template.split("\t");
			for (String s : temp) {
				if (s.contains("+@")) continue;
				QuestionTemplateFromNLP geo = new QuestionTemplateFromNLP();
				geo.oriText = te[0] + "@" + (te.length > 1 ? te[1] : "");
				geo.syntaxTreeLeaves = Tree.getLeaves(t);
				switch (template.type) {
				case 1:
					geo.templateType = "最值";
					geo.slotCount = 4;
					break;
				case 2:
					geo.templateType = "趋势";
					geo.slotCount = 4;
					break;
				case 3:
					geo.templateType = "位于";
					geo.slotCount = 2;
					break;
				case 4:
					geo.templateType = "比较";
					geo.slotCount = 5;
					break;
				case 5:
					geo.templateType = "影响";
					geo.slotCount = 3;
					break;
				case 6:
					geo.templateType = "匹配";
					geo.slotCount = 2;
					break;
				case 7:
					geo.templateType = "因果";
					geo.slotCount = 2;
					break;
				case 8:
					geo.templateType = "措施";
					geo.slotCount = 2;
					break;
				case 9:
					geo.templateType = "排序";
					geo.slotCount = 3;
					break;
				case 10:
					geo.templateType = "分布";
					geo.slotCount = 3;
					break;
				default:
					geo.templateType = "实体信息陈述";
					geo.slotCount = 3;
					break;
				}
				geo.cueWord = Integer.parseInt(s.split("\\+")[0]);
				s = s.split("\\+")[1];
				geo.slots = new ArrayList<SlotStructureFromNLP>();
				String[] slots = s.split("@");
				if (template.type == 4 && slots.length > 2) {
					if (slots[0].isEmpty() && !slots[2].isEmpty()) {
						slots[0] = slots[2];
						slots[2] = "";
					}
					if (slots[1].isEmpty() && !slots[3].isEmpty()) {
						slots[1] = slots[3];
						slots[3] = "";
					}
				}
				for (String slot : slots) {
					if (slot.isEmpty()) {
						geo.slots.add(null);
						//System.out.print("null" + " ");
					}
					else {
						SlotStructureFromNLP aslot = new SlotStructureFromNLP();
						//aslot.isTemplate = false;
						aslot.content = slot;
						int start = -1, end = -1;
						for (int i = 0; i < sentence.size(); i++)
							if (slot.startsWith(sentence.get(i).split("_")[0])) {
								start = i;
								int j = i;
								while (j < sentence.size() && !slot.endsWith(sentence.get(j).split("_")[0])) {
									while (j < sentence.size() && slot.contains(sentence.get(j).split("_")[0])) j++;
									j--;
									if (slot.endsWith(sentence.get(j).split("_")[0])) 
										end = j;
									else
										j+=2;
								}
								if (j == sentence.size())
									end = j - 1;
								else
									end = j;
								break;
							}
						aslot.startOffset = start;
						aslot.endOffset = end;
						aslot.syntaxNodes = new ArrayList<Tree>();
						aslot.depNodes = new ArrayList<DepTree>();
						for (int i = start + 1; i <= end + 1; i++) {
							aslot.syntaxNodes.add(Tree.findNodeByNo(t, i));
							aslot.depNodes.add(DepTree.findNodeByNo(input.depRoots, i));
						}
						geo.slots.add(aslot);
						//System.out.print(slot + " ");
					}
				}
				if (slots.length < geo.slotCount)
					for (int i = 0; i < geo.slotCount - slots.length; i++)
						geo.slots.add(null);
				//System.out.println();
				if (geo.slots.get(0) == null && (te[0] != null && te[0].contains("正确的") || te[0].contains("错误的")  || te[0].contains("接近实际的"))) {
					int start = te[0].indexOf("关于");
					int end = te[0].indexOf("的");
					SlotStructureFromNLP aslot = new SlotStructureFromNLP();
					//aslot.isTemplate = false;
					aslot.content = te[0].substring(start + 2, end);
					geo.slots.set(0, aslot);
				}
				nl.templates.add(geo);
			}
		}
		try {
			nl.templates = (ArrayList<QuestionTemplateFromNLP>) Nesting.nest(nl.templates, t, input);
			l = Candidate.chooseCandidate(nl);
		} catch (Exception e) {
			l.add(nl);
		}
		return l;
	}
	
	public static void main(String[] args) throws IOException {
		init();
		
		ArrayList<String> seList = new ArrayList<String>();
//		seList.add("甲处垂直高差可能是1190米");
//		seList.add("乙处岩层中可能找到化石");
//		seList.add("丙处可能发展成为河流");
//		seList.add("丁处山体坡度较缓");
//		seList.add("太阳直射点在赤道与北回归线之间");
//		seList.add("北极圈内有极昼现象");
		seList.add("我喜欢小狗");
		seList.add("我国越往北白昼时间越长");
		seList.add("我国日出时间在6:00之后");
		seList.add("“彩虹”对海南岛的影响有@利于风力发电");
		seList.add("“彩虹”对海南岛的影响是@利于风力发电");
//		seList.add("指示牌在图5(b)中的位置是①");
//		seList.add("太白山又密又高的树木在针叶林带");
		
//		seList.add("气旋③移动方向与地球自转方向相反");
//		seList.add("该河谷岩层①比岩层②形成年代早");
		//seList.add("太白山北坡山中腰降水量比山麓少");
//		seList.add("图中丙村比丁村土层深厚");
//		seList.add("图中丁村比丙村地下水埋藏浅");
//		seList.add("据图可推断，甲地人口迁出比例北方地区比南方地区高");
//		seList.add("据图可推断，甲地人口迁出比例直辖市比省级行政中心高");
//		seList.add("据图可推断，甲地人口迁出比例珠江三角洲比四川盆地高");
//		seList.add("据图可推断，甲地人口迁出比例内陆城市比沿海城市高");
//		seList.add("退耕还林，是治理土壤退化的措施");
//		seList.add("从地形和降水条件分析，最易发生泥石流的网格区是②。");
//		seList.add("本届大会期间，北京@适逢中国农历处暑节气");
//		seList.add("本届大会期间，北京@八达岭长城漫山红叶");
//		seList.add("本届大会期间，北京@比首尔正午太阳高度大");
//		seList.add("本届大会期间，北京@比华盛顿日出时间晚");
//		seList.add("图中举办地所在国家@位于北半球中纬度");
//		seList.add("图中举办地所在国家@地处环太平洋灾害带");
//		seList.add("图中举办地所在国家@人口增长模式不同");
//		seList.add("图中举办地所在国家@南部沿海有寒流经过");
//		seList.add("该日20时@北京大风扬沙，空气污染加重");
//		seList.add("该日20时@东海海域天气晴朗，风大浪高");
//		seList.add("该日20时@低压天气系统中，P强度最强");
//		seList.add("该日20时@Q地位于暖锋锋前，出现降水");
//		seList.add("平顶海山@为褶皱山");
//		seList.add("平顶海山@由沉积岩构成");
//		seList.add("平顶海山@顶部形态由内力作用塑造");
//		seList.add("平顶海山@随着板块的移动没入水下");
//		seList.add("图中@①处冰川融化，湖泊水位升高");
//		seList.add("图中@②处流量稳定，河流的落差小");
//		seList.add("图中@③处谷宽、流速慢，适宜修建水库");
//		seList.add("图中@④处地形平坦，农业生产条件优越");
//		seList.add("若该图示意中国某流域，图中@地物按1:10万比例描绘，特征清晰");
//		seList.add("若该图示意中国某流域，图中@冰川地处新疆，覆盖范围沿山脊延伸");
//		seList.add("若该图示意中国某流域，图中@径流季节变化大，存在不同程度水患");
//		seList.add("若该图示意中国某流域，图中@海域位于北回归线以南，港口数量少");
//		seList.add("气温日较差大的月份是@1月");
//		seList.add("气温日较差大的月份是@4月");
//		seList.add("气温日较差大的月份是@7月");
//		seList.add("气温日较差大的月份是@10月");
//		seList.add("该山地@冬季受副热带高压带控制");
//		seList.add("该山地@因台风暴雨引发的滑坡多");
//		seList.add("该山地@基带的景观为热带雨林");
//		seList.add("该山地@山顶海拔低于1000米");
//		seList.add("芬兰@盛行西风，终年温和多雨");
//		seList.add("芬兰@山区水土流失严重，城镇数量少");
//		seList.add("芬兰@人口稀疏区以大牧场放牧业为主");
//		seList.add("芬兰@森林资源丰富，木材加工业发达");
//		seList.add("燕麦种植北界呈图示走向，主要是由于该国@东部地区的河湖密布，灌溉条件好");
//		seList.add("燕麦种植北界呈图示走向，主要是由于该国@西部受暖流、地形影响，气温偏高");
//		seList.add("燕麦种植北界呈图示走向，主要是由于该国@东南部土层较深厚，耕作技术高");
//		seList.add("燕麦种植北界呈图示走向，主要是由于该国@西北部多晴朗天气，日照时间长");
//		seList.add("据图判断@甲区多公共服务设施，靠近住宅区");
//		seList.add("据图判断@乙是位于郊区的高新技术产业园区");
//		seList.add("据图判断@丙区商业网点等级低，服务半径小");
//		seList.add("据图判断@丁为中心商务区，能耗昼夜差异大");
/*		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("jt.csv")));
		String s = br0.readLine();
		while ((s = br0.readLine()) != null) {
			s = s.split(",")[0];
			//s = s.split("\t")[0] + "@" + s.split("\t")[1];
			seList.add(s);
		}
		br0.close();*/
		List<String> result=MyUtil.readListFromFile("E://workspace/To94%/data/beijing2016.txt");
		for (String tString : result) {
			//System.out.println(tString);
			ArrayList<NlpOutput> tst = Generate.GenerateTemplate(tString);
			//System.out.println(tst.get(0).templates.size());
			//System.out.println(tst.get(0).templates.get(0).oriText);
//			for(int index=0;index<tst.size();index++){
//				for (QuestionTemplateFromNLP template : tst.get(index).templates) {
//					System.out.print(template.templateType + "(");
//					// System.out.println("模板槽个数: " + template.slotCount);
//					// System.out.println("槽列表:");
//					int i = 0;
//					for (SlotStructureFromNLP slot : template.slots) {
//						if (slot == null)
//							System.out.print("#");
//						/*
//						 * else if (slot.isTemplate)
//						 * System.out.println("\t模板槽");
//						 */
//						else
//							System.out.print(slot.content);
//						if (i != template.slotCount - 1)
//							System.out.print("，");
//						else
//							System.out.println(")");
//						i++;
//					}
//
//				}
//			}
			
		}
	}
}
