package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.nju.ws.GeoScholar.log.LogExamples;
import cn.edu.nju.ws.GeoScholar.solver.SemiStructuredQuestion;
import cn.edu.nju.ws.GeoScholar.solver.StructuredQuestion;
import cn.edu.nju.ws.GeoScholar.templating.common.DepTree;
import cn.edu.nju.ws.GeoScholar.templating.common.QuestionTemplateFromNLP;
import cn.edu.nju.ws.GeoScholar.templating.common.Tree;
import cn.edu.nju.ws.GeoScholar.util.ThreeTuple;
/**
 * 题干和某个选项
 * @author jwding
 *
 */
public class Question {
	/**
	 * 日志
	 */
	private static final Log log = 
			LogFactory.getLog(Question.class);
	/**
	 * 指向包含当前选项的问题
	 */
	public Problem problem;
	/**
	 * 指示该选项是这道题的第几个选项
	 */
	public int questionIndex;
	/**
	 * 题干的文本内容
	 */
	public String stemText;
	/**
	 * 选项的文本内容
	 */
	public String optionText;
	/**
	 * 题目涉及到的图片编号 
	 */
	public ArrayList<String> images=new ArrayList<String>();
	/**
	 * 题目涉及到的图片的标注知识
	 */
	public ArrayList<FigKnowledge> figKnowledgeList=new ArrayList<FigKnowledge>();
	/**
	 * NLP模块的输出,内容,置信度,依赖项
	 */
	private ArrayList<ThreeTuple<NlpOutput,Double,ThreeTuple>> nlpOut=new ArrayList<ThreeTuple<NlpOutput,Double,ThreeTuple>>();
	/**
	 * SemanticParsing模块的输出,内容,置信度,依赖项
	 */
	private ArrayList<ThreeTuple<ParsingOutput,Double,ThreeTuple>> parsingOut=new ArrayList<ThreeTuple<ParsingOutput,Double,ThreeTuple>>();
	/**
	 * Solver模块的输出,内容,置信度,依赖项
	 */
	private ArrayList<ThreeTuple<QuestionSolverOutput,Double,ThreeTuple>> solverOut=new ArrayList<ThreeTuple<QuestionSolverOutput,Double,ThreeTuple>>();
	
	/** 获得NLP输出的迭代器，迭代器指向[内容，置信度，依赖项]的三元组
	 * @return 
	 */
	public Iterator<ThreeTuple<NlpOutput,Double,ThreeTuple>> nlpOutIterator()
	{
		return this.nlpOut.iterator();
	}
	
	/** 获得Parsing输出的迭代器，迭代器指向[内容，置信度，依赖项]的三元组
	 * @return
	 */
	public Iterator<ThreeTuple<ParsingOutput,Double,ThreeTuple>> parsingOutIterator()
	{
		return this.parsingOut.iterator();
	}
	
	/** 获得Solver输出的迭代器，迭代器指向[内容，置信度，依赖项]的三元组
	 * @return
	 */
	public Iterator<ThreeTuple<QuestionSolverOutput,Double,ThreeTuple>> solverOutIterator()
	{
		return this.solverOut.iterator();
	}

	/** 获得求解器输出答案的数量
	 * @return
	 */
	public int getSolverOutNum()
	{
		return this.solverOut.size();
	}
	
	/** 获得Parsing输出的数量
	 * @return
	 */
	public int getParsingOutNum()
	{
		return this.parsingOut.size();
	}

	/** 获得NLP输出的数量
	 * @return 
	 */
	public int getNlpOutNum()
	{
		return this.nlpOut.size();
	}
	/** 添加一个NLP输出,并写日志
	 * @param output NLP输出的内容
	 * @param confidence 置信度
	 */
	public void addNlpOut(NlpOutput output,double confidence)
	{
		ThreeTuple<NlpOutput,Double,ThreeTuple> a=new ThreeTuple<NlpOutput,Double,ThreeTuple>(output,confidence,null);
		this.nlpOut.add(a);
		try {
			log.info("选择题第"+(this.problem.index)+"题第"+(this.questionIndex)+"个选项-获得新的NLP输出,置信度"+confidence+"\n"+output.toString());
		} catch (Exception e) {
			// TODO: 写日志异常
		}
	}
	
	/** 添加一个Parsing的输出,并写日志
	 * @param output Parsing输出的内容
	 * @param confidence 置信度
	 * @param dependency 依赖关系，可能是一个NLP的输出，没有依赖填写null
	 */
	public void addParsingOut(ParsingOutput output,double confidence,ThreeTuple dependency)
	{
		ThreeTuple<ParsingOutput,Double,ThreeTuple> a=new ThreeTuple<ParsingOutput,Double,ThreeTuple>(output,confidence,dependency);
		this.parsingOut.add(a);
		try {
			log.info("选择题第"+(this.problem.index)+"题第"+(this.questionIndex)+"个选项-获得新的实体链接输出,置信度"+confidence+"\n"+output.toString()
					+"\n依赖于:"+dependency);
			
		} catch (Exception e) {
			// TODO: 写日志异常
		}
	}
	
	/** 添加一个Solver的输出,并写日志
	 * @param output Solver输出的答案
	 * @param confidence 置信度
	 * @param dependency 依赖关系，可能是Parsing的输出，没有依赖填写null
	 */
	public void addSolverOut(QuestionSolverOutput output,double confidence,ThreeTuple dependency)
	{
		ThreeTuple<QuestionSolverOutput,Double,ThreeTuple> a=new ThreeTuple<QuestionSolverOutput,Double,ThreeTuple>(output,confidence,dependency);
		this.solverOut.add(a);
		try {
			log.info("选择题第"+(this.problem.index)+"题第"+(this.questionIndex)+"个选项-获得新的求解器输出,置信度"+confidence+"\n"+output.toString()
					+"\n依赖于:"+dependency);
			
		} catch (Exception e) {
			// TODO: 写日志异常
		}
	}
	
	/** 获得一条求解路径构成的Question
	 * @param a 某个步骤的输出对应的三元组
	 * @return 一个虚拟Question，仅包含到当前步骤为止所依赖的所有内容
	 */
	public VirtualQuestion getVirtualQuestion(ThreeTuple a)
	{
		VirtualQuestion ts=new VirtualQuestion();
		ts.stemText=this.stemText;
		ts.optionText=this.optionText;
		if(a==null)
			return ts;
		if(a.getFirst() instanceof NlpOutput)
		{
			ts.nlpOut=a;
			return ts;
		}
		if(a.getFirst() instanceof ParsingOutput)
		{
			ts.parsingOut=a;
			if(a.getThird()!=null&&((ThreeTuple)a.getThird()).getFirst() instanceof NlpOutput)
			{
				ts.nlpOut=(ThreeTuple)a.getThird();
			}
			return ts;
		}
		if(a.getFirst() instanceof QuestionSolverOutput)
		{
			ts.solverOut=a;
			if(a.getThird()!=null&&((ThreeTuple)a.getThird()).getFirst() instanceof ParsingOutput)
			{
				ts.parsingOut=(ThreeTuple)a.getThird();
			}
			else if(a.getThird()!=null&&((ThreeTuple)a.getThird()).getFirst() instanceof NlpOutput)
			{
				ts.nlpOut=(ThreeTuple)a.getThird();
			}
			if(ts.parsingOut!=null&&ts.parsingOut.getThird()!=null&&ts.parsingOut.getThird().getFirst() instanceof NlpOutput)
			{
				ts.nlpOut=ts.parsingOut.getThird();
			}
			return ts;
		}
		return ts;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
