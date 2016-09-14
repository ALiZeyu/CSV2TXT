package cn.edu.nju.ws.GeoScholar.templating.common;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.nju.ws.GeoScholar.common.ExamPaper;
import cn.edu.nju.ws.GeoScholar.common.NlpOutput;
import cn.edu.nju.ws.GeoScholar.common.Problem;
import cn.edu.nju.ws.GeoScholar.common.Question;
import cn.edu.nju.ws.GeoScholar.common.SaqQuestion;
import cn.edu.nju.ws.GeoScholar.common.SaqQuestionProcessed;
import cn.edu.nju.ws.GeoScholar.common.SaqQuestions;
import cn.edu.nju.ws.GeoScholar.log.LogExamples;
import cn.edu.nju.ws.GeoScholar.saqSolver.BasicSolver;
import cn.edu.nju.ws.GeoScholar.templating.choice.Generate;
import cn.edu.nju.ws.GeoScholar.templating.saq.*;

/**
 * 从题目到nlp模板的过程, 即对nlp的调用, 
 * 结果就是填充ExamPaper中相应的内容
 * @author lfshi
 * 
 */
public class ToTemplating {
	private static final Log log = 
			LogFactory.getLog(LogExamples.class);
	
	public static void toTemplating(ExamPaper examPaper) {
		for (Problem problem : examPaper.problemList) {
			for (Question question : problem.questions) {
				String qt = question.stemText + "@" + question.optionText;
				ArrayList<NlpOutput> nlpOutputs = null;
				try {
					nlpOutputs = Generate.GenerateTemplate(qt);
					for (NlpOutput nlpOutput : nlpOutputs)
						question.addNlpOut(nlpOutput, 1);
				} catch (Exception e) {
					try {
						log.fatal("第" + problem.index + "题,选项" + 
							question.questionIndex + ": " + qt + ", 模板化错误");
					} catch (Exception exception) {
						
					}
				}
			}
		}
		
		/**
		 * TODO 添加简答题部分
		 */
		ArrayList<SaqQuestion> saqlist = new ArrayList<SaqQuestion>();
		
		Iterator<SaqQuestions> qsit = examPaper.saqs.getQslist().iterator();
		for(; qsit.hasNext();){
			saqlist.addAll(qsit.next().getQlist());//将所有问题取出
		}
		
		Iterator<SaqQuestion> qit = saqlist.iterator();
		for(; qit.hasNext();){
			SaqQuestion sq = qit.next();
			ArrayList<SaqQuestionProcessed> sqplist = sq.getSqps();
			if(sqplist.size() > 0){
				if(sq.getSplitType() == 0){//不拆分
					SaqQuestionProcessed sqp = sqplist.get(0);
					
					try{
						//System.out.println(sqp.getProblem());
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp.getProblem());
						sqp.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(sqp.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
				}
				else if(sq.getSplitType() == 1 || sq.getSplitType() == 5){//逗号或句号或问号拆分
					SaqQuestionProcessed sqp0 = sqplist.get(0);
					
					try{
						//System.out.println(sqp0.getProblem());
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem());
						sqp0.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(sqp0.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					
					SaqQuestionProcessed sqp1 = sqplist.get(1);
					
					try{
						if(!sqp1.getProblem().contains("其") && !sqp1.getProblem().contains("分析原因") && !sqp1.getProblem().contains("对策") && !sqp1.getProblem().contains("措施")){
							//System.out.println(sqp1.getProblem());
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp1.getProblem());
							sqp1.addNlpOut(nlpoutput, 1);
							//System.out.println(nlpoutput.templates.toString());
						}
						else{
							//System.out.println(sqp0.getProblem() + sqp1.getProblem());
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + sqp1.getProblem());
							sqp1.setProblem(sqp0.getProblem() + sqp1.getProblem());
							sqp1.addNlpOut(nlpoutput, 1);
							//System.out.println(nlpoutput.templates.toString());
						}
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
				}
				else if(sq.getSplitType() == 2){//及或以及拆分
					SaqQuestionProcessed sqp0 = sqplist.get(0);
					
					try{
						//System.out.println(sqp0.getProblem());
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem());
						sqp0.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(sqp0.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					
					SaqQuestionProcessed sqp1 = sqplist.get(1);
					String newsqp1;
					if(!sqp1.getProblem().contains("对"))
						newsqp1 = sqp0.getProblem() + "的" + sqp1.getProblem();
					else
						newsqp1 = sqp0.getProblem() + sqp1.getProblem();
					try{
						//System.out.println(newsqp1);
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(newsqp1);
						sqp1.setProblem(newsqp1);
						sqp1.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(newsqp1 + ", 模板化错误");
							} catch (Exception exception) {
									
							}
						}
					
				}
				else if(sq.getSplitType() == 3){//及其拆分
					SaqQuestionProcessed sqp0 = sqplist.get(0);
					
					try{
						//System.out.println(sqp0.getProblem());
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem());
						sqp0.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(sqp0.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					
					SaqQuestionProcessed sqp1 = sqplist.get(1);
					if(sqp1.getProblem().contains("成因") || sqp1.getProblem().contains("形成原因")){//成因
						try{
							//System.out.println(sqp0.getProblem() + "的成因");
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的成因");
							sqp1.setProblem(sqp0.getProblem() + "的成因");
							sqp1.addNlpOut(nlpoutput, 1);
							//System.out.println(nlpoutput.templates.toString());
							
							//模板嵌套的方式先不考虑
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "因果";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = "成因";
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
						
					}
					else if(sqp1.getProblem().contains("原因") && !sqp1.getProblem().contains("形成")){//一般原因
						try{
							//System.out.println(sqp0.getProblem() + "的原因");
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的原因");
							sqp1.addNlpOut(nlpoutput, 1);
							sqp1.setProblem(sqp0.getProblem() + "的原因");
							//System.out.println(nlpoutput.templates.toString());
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "因果";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = null;
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
					else if(sqp1.getProblem().contains("优势") ){//优势类
						try{
							//System.out.println(sqp0.getProblem() + "的优势");
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的优势");
							sqp1.addNlpOut(nlpoutput, 1);
							sqp1.setProblem(sqp0.getProblem() + "的优势");
							//System.out.println(nlpoutput.templates.toString());
							
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "优势";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 4;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = false;
							slot0.content = null;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = true;
							slot1.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot1);
							SlotStructureFromNLP slot2 = new SlotStructureFromNLP();
							slot2.isTemplate = false;
							slot2.content = null;
							slots.add(slot2);
							SlotStructureFromNLP slot3 = new SlotStructureFromNLP();
							slot3.isTemplate = false;
							slot3.content = null;
							slots.add(slot3);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
					else if(sqp1.getProblem().contains("措施") || sqp1.getProblem().contains("对策")){//对策措施类
						try{
							
							//System.out.println(sqp0.getProblem() + "的对策");
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的对策");
							sqp1.addNlpOut(nlpoutput, 1);
							sqp1.setProblem(sqp0.getProblem() + "的对策");
							//System.out.println(nlpoutput.templates.toString());
							
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "对策措施";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = null;
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
					else{//其他
						try{
							
							//System.out.println(sqp0.getProblem() + sqp1.getProblem());
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + sqp1.getProblem());
							sqp1.addNlpOut(nlpoutput, 1);
							sqp1.setProblem(sqp0.getProblem() + sqp1.getProblem());
							//System.out.println(nlpoutput.templates.toString());
							
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "实体信息陈述";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = sqp1.getProblem();
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
							
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
				}
				else if(sq.getSplitType() == 4){//并拆分
					SaqQuestionProcessed sqp0 = sqplist.get(0);
					
					try{
						//System.out.println(sqp0.getProblem());
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem());
						sqp0.addNlpOut(nlpoutput, 1);
						//System.out.println(nlpoutput.templates.toString());
						}catch (Exception e){
							try {
								log.fatal(sqp0.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					
					SaqQuestionProcessed sqp1 = sqplist.get(1);
					////System.out.println(sqp1.getProblem());
					if((sqp1.getProblem().contains("原因") && !sqp1.getProblem().contains("形成")) || sqp1.getProblem().contains("理由")){//一般原因
						try{
							
							if(sqp1.getProblem().length() < 10){
								//System.out.println(sqp0.getProblem() + "的原因");
								NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的原因");
								sqp1.addNlpOut(nlpoutput, 1);
								sqp1.setProblem(sqp0.getProblem() + "的原因");
								//System.out.println(nlpoutput.templates.toString());
							}
							
							else{
								//System.out.println(sqp1.getProblem());
								NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp1.getProblem());
								sqp1.addNlpOut(nlpoutput, 1);
								//System.out.println(nlpoutput.templates.toString());
							}
							
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "因果";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = null;
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
					else if(sqp1.getProblem().contains("成因") || sqp1.getProblem().contains("形成原因")){//成因类
						//try{
							
						
						//System.out.println(sqp0.getProblem() + "的成因");
						NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的成因");
						sqp1.addNlpOut(nlpoutput, 1);
						sqp1.setProblem(sqp0.getProblem() + "的成因");
						//System.out.println(nlpoutput.templates.toString());	
						
						/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "因果";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = "成因";
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
							////System.out.println(nlpoutput.templates.toString());
						/*}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}*/
					}
					else if(sqp1.getProblem().contains("对策") || sqp1.getProblem().contains("措施")){//对策措施类
						try{
							
							//System.out.println(sqp0.getProblem() + "的对策");
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的对策");
							sqp1.addNlpOut(nlpoutput, 1);
							sqp1.setProblem(sqp0.getProblem() + "的对策");
							//System.out.println(nlpoutput.templates.toString());
							
							/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
							qtfnlp.templateType = "对策措施";
							qtfnlp.oriText = sqp1.getProblem();
							qtfnlp.slotCount = 2;
							ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
							
							SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
							slot0.isTemplate = true;
							slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
							slots.add(slot0);
							SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
							slot1.isTemplate = false;
							slot1.content = null;
							slots.add(slot1);
							
							qtfnlp.slots = slots;
							
							NlpOutput nlpoutput = new NlpOutput();
							nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
							nlpoutput.templates.add(qtfnlp);
							
							sqp1.addNlpOut(nlpoutput, 1);*/
						}catch (Exception e){
							try {
								log.fatal(sqp1.getProblem() + ", 模板化错误");
							} catch (Exception exception) {
								
							}
						}
					}
					else if(sqp1.getProblem().contains("其")){
						if(sqp1.getProblem().contains("作用") || sqp1.getProblem().contains("意义") || sqp1.getProblem().contains("价值")){//意义作用类
							try{
								//System.out.println(sqp0.getProblem() + "的意义");
								NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp0.getProblem() + "的意义");
								sqp1.addNlpOut(nlpoutput, 1);
								sqp1.setProblem(sqp0.getProblem() + "的意义");
								//System.out.println(nlpoutput.templates.toString());
								
								/*QuestionTemplateFromNLP qtfnlp = new QuestionTemplateFromNLP();
								qtfnlp.templateType = "意义";
								qtfnlp.oriText = sqp1.getProblem();
								qtfnlp.slotCount = 2;
								ArrayList<SlotStructureFromNLP> slots = new ArrayList<SlotStructureFromNLP>();
								
								SlotStructureFromNLP slot0 = new SlotStructureFromNLP();
								slot0.isTemplate = true;
								slot0.content = sqp0.nlpOutIterator().next().getFirst().templates;
								slots.add(slot0);
								SlotStructureFromNLP slot1 = new SlotStructureFromNLP();
								slot1.isTemplate = false;
								slot1.content = null;
								slots.add(slot1);
								
								qtfnlp.slots = slots;
								
								NlpOutput nlpoutput = new NlpOutput();
								nlpoutput.templates = new ArrayList<QuestionTemplateFromNLP>();
								nlpoutput.templates.add(qtfnlp);
								
								sqp1.addNlpOut(nlpoutput, 1);*/
								////System.out.println(nlpoutput.templates.toString());
							}catch (Exception e){
								try {
									log.fatal(sqp1.getProblem() + ", 模板化错误");
								} catch (Exception exception) {
									
								}
							}
						}
						else{
							
						}
					}
					else{
						try{
							//System.out.println(sqp1.getProblem());
							NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp1.getProblem());
							sqp1.addNlpOut(nlpoutput, 1);
							//System.out.println(nlpoutput.templates.toString());
							}catch (Exception e){
								try {
									log.fatal(sqp1.getProblem() + ", 模板化错误");
								} catch (Exception exception) {
									
								}
							}
					}
					
				}
				else if(sq.getSplitType() == 6){//和与拆分
					
				}
			}
			/*if(sqplist != null){
				Iterator<SaqQuestionProcessed> itsqp = sqplist.iterator();
				for(;itsqp.hasNext();){
					SaqQuestionProcessed sqp = itsqp.next();
					
					
					try{
					//System.out.println(sqp.getProblem());
					NlpOutput nlpoutput = SaqTemplating.templateSAQ(sqp.getProblem());
					sqp.addNlpOut(nlpoutput, 1);
					//System.out.println(nlpoutput.templates.toString());
					}catch (Exception e){
						try {
							log.fatal(sqp.getProblem() + ", 模板化错误");
						} catch (Exception exception) {
							
						}
					}
				}
			}*/
		}
	}
}
