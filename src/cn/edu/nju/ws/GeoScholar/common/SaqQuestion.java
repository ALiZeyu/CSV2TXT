package cn.edu.nju.ws.GeoScholar.common;

import java.util.*;

/**
 * 
 * @author guyu
 *
 */

public class SaqQuestion{
	String id;//问题题号
	
	String score;//问题分值
	
	int index;//第几题
	
	ArrayList<String> picList = new ArrayList<String>();//需要用到的图片的编号
	
	/**
	 *拆分的类型。
	 *0 表示不拆分
	 *1表示按逗号或句号拆分
	 *2表示按及或以及拆分
	 *3表示按及其拆分
	 *4表示按并拆分
	 *5表示按问号拆分
	 *6表示按和、与拆分 
	 */
	int splitType = 0;
	
	/**
	 * 问题原始文本，预处理模块输入的数据域
	 */
	SaqText text;
	
	/**
	 * 预处理部分需要修改的数据域
	 */
	ArrayList<SaqQuestionProcessed> sqps = new ArrayList<SaqQuestionProcessed>();
	
	/**
	 * 图标注知识的存储
	 */
	public ArrayList<FigKnowledge> figKnowledgeList = new ArrayList<FigKnowledge>(); 
	
	public SaqQuestion(){
		id = null;
		score = null;
		text = null;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public SaqText getText() {
		return text;
	}

	public void setText(SaqText text) {
		this.text = text;
	}


	public ArrayList<SaqQuestionProcessed> getSqps() {
		return sqps;
	}

	/**
	 * 该函数由nlp在预处理阶段调用
	 * @param problemlist应为预处理模块的输出
	 */
	public void setSqps(ArrayList<String> problemlist) {
		sqps = new ArrayList<SaqQuestionProcessed>();
		Iterator<String> it = problemlist.iterator();
		for(; it.hasNext();){
			String problem = it.next();
			SaqQuestionProcessed sqp = new SaqQuestionProcessed();
			sqp.setProblem(problem);
			sqps.add(sqp);
		}
	}


	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}


	public ArrayList<String> getPicList() {
		return picList;
	}


	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}


	public ArrayList<FigKnowledge> getFigKnowledge() {
		return figKnowledgeList;
	}


	public void setFigKnowledge(ArrayList<FigKnowledge> figKnowledgeList) {
		this.figKnowledgeList = figKnowledgeList;
	}


	public int getSplitType() {
		return splitType;
	}


	public void setSplitType(int splitType) {
		this.splitType = splitType;
	}



	
}