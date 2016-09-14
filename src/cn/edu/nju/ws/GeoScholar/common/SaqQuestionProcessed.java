package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;
import java.util.Iterator;

import cn.edu.nju.ws.GeoScholar.basis.LinkedItem;
import cn.edu.nju.ws.GeoScholar.util.ThreeTuple;

/**
 * 
 * @author guyu
 *
 */

public class SaqQuestionProcessed{
	/**
	 * 预处理后的文本，模板化与解题模块的输入部分
	 */
	String problem;
	
	/**
	 * 分类得到的问题类型
	 */
	String type;
	
	public SaqQuestionProcessed(){
		problem = null;
	}
	
	/**
	 * 相关术语的uri列表
	 */
	private ArrayList<LinkedItem> geoEntity = new ArrayList<LinkedItem>();
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
	private ArrayList<ThreeTuple<SaqSolverOutput,Double,ThreeTuple>> saqsolverOut=new ArrayList<ThreeTuple<SaqSolverOutput,Double,ThreeTuple>>();
	
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
	public Iterator<ThreeTuple<SaqSolverOutput,Double,ThreeTuple>> solverOutIterator()
	{
		return this.saqsolverOut.iterator();
	}

	/** 获得求解器输出答案的数量
	 * @return
	 */
	public int getSolverOutNum()
	{
		return this.saqsolverOut.size();
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
	/** 添加一个NLP输出
	 * @param output NLP输出的内容
	 * @param confidence 置信度
	 */
	public void addNlpOut(NlpOutput output,double confidence)
	{
		ThreeTuple<NlpOutput,Double,ThreeTuple> a=new ThreeTuple<NlpOutput,Double,ThreeTuple>(output,confidence,null);
		this.nlpOut.add(a);
	}
	
	/** 添加一个Parsing的输出
	 * @param output Parsing输出的内容
	 * @param confidence 置信度
	 * @param dependency 依赖关系，可能是一个NLP的输出，没有依赖填写null
	 */
	public void addParsingOut(ParsingOutput output,double confidence,ThreeTuple dependency)
	{
		ThreeTuple<ParsingOutput,Double,ThreeTuple> a=new ThreeTuple<ParsingOutput,Double,ThreeTuple>(output,confidence,dependency);
		this.parsingOut.add(a);
	}
	
	/** 添加一个Solver的输出
	 * @param output Solver输出的答案
	 * @param confidence 置信度
	 * @param dependency 依赖关系，可能是Parsing的输出，没有依赖填写null
	 */
	public void addSolverOut(SaqSolverOutput output,double confidence,ThreeTuple dependency)
	{
		ThreeTuple<SaqSolverOutput,Double,ThreeTuple> a=new ThreeTuple<SaqSolverOutput,Double,ThreeTuple>(output,confidence,dependency);
		this.saqsolverOut.add(a);
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<LinkedItem> getGeoEntity() {
		return geoEntity;
	}

	public void setGeoEntity(ArrayList<LinkedItem> geoEntity) {
		this.geoEntity = geoEntity;
	}


	
}