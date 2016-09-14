package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.nju.ws.GeoScholar.solver.ProblemAnswer;
import cn.edu.nju.ws.GeoScholar.util.ThreeTuple;
import cn.edu.nju.ws.GeoScholar.util.TwoTuple;
/**
 * 一道选择题的顶层结构
 */
/**
 * @author jwding
 *
 */
public class Problem {
	private static final Log log = 
			LogFactory.getLog(Problem.class);
	/**
	 * 题干文本
	 */
	public String stemText;
	/**
	 * 第几题
	 */
	public int index;
	/**
	 * XML试卷中给出的标识不同题目的ID
	 */
	public String id;
	/**
	 * 题干+选项，复杂的表示结构；组合型问题为题干+隐选项
	 */
	public ArrayList<Question> questions;
	/**
	 * 是否为组合型问题,true表示是
	 */
	public boolean isCombined;
	/**
	 * 选项A,表示一些question的组合，比如0,1,3的组合；非组合题默认为{0}
	 */
	public ArrayList<Integer> optionA;
	/**
	 * 选项B,表示一些question的组合，比如0,1,3的组合；非组合题默认为{1}
	 */
	public ArrayList<Integer> optionB;
	/**
	 * 选项C,表示一些question的组合，比如0,1,3的组合；非组合题默认为{2}
	 */
	public ArrayList<Integer> optionC;
	/**
	 * 选项D,表示一些question的组合，比如0,1,3的组合；非组合题默认为{3}
	 */
	public ArrayList<Integer> optionD;
	/**
	 * 选择题背景，基类中只有String，可以通过继承扩充
	 */
	public Background background;
	/**
	 * 题目涉及到的图片编号 
	 */
	public ArrayList<String> images=new ArrayList<String>();;
	/**
	 * 题目涉及到的图片的标注知识
	 */
	public ArrayList<FigKnowledge> figKnowledgeList=new ArrayList<FigKnowledge>();
	/**
	 * 题目涉及到的图表信息
	 */
	public ArrayList<TabKnowledge> tabKnowledgeList;
	/**
	 * 求解器产生的一道题的答案
	 */
	private ArrayList<TwoTuple<ProblemSolverOutput, Double>> problemSolverOutput=new ArrayList<TwoTuple<ProblemSolverOutput, Double>>();
	/**
	 * 一道题的答案，选择题为ABCD选项中的一个
	 */
	public ProblemAnswer problemAnswer;
	
	/** 添加一个Solver的输出,并写日志
	 * @param output Solver输出的答案
	 * @param confidence 置信度
	 */
	public void addSolverOut(ProblemSolverOutput output,double confidence)
	{
		problemSolverOutput.add(new TwoTuple<ProblemSolverOutput, Double>(output, confidence));
		try {
			log.info("选择题第"+(this.index)+"题获得新的求解器输出,置信度"+confidence+"\n"+output.toString());
		} catch (Exception e) {
			// TODO: 写日志异常
		}
	}
	/** 获得Solver输出的迭代器，迭代器指向[内容，置信度，依赖项]的三元组
	 * @return
	 */
	public Iterator<TwoTuple<ProblemSolverOutput,Double>> solverOutIterator()
	{
		return this.problemSolverOutput.iterator();
	}
	/** 获得求解器输出答案的数量
	 * @return
	 */
	public int getSolverOutNum()
	{
		return this.problemSolverOutput.size();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
