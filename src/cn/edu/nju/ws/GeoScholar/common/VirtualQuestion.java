package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;

import cn.edu.nju.ws.GeoScholar.util.ThreeTuple;

/** 虚拟Question类型，追溯某一输出的全部依赖内容
 * @author jwding
 *
 */
public class VirtualQuestion {
	/**
	 * 题干的文本内容
	 */
	public String stemText;
	/**
	 * 选项的文本内容
	 */
	public String optionText;
	/**
	 * NLP模块的输出,内容,置信度,依赖项
	 */
	public ThreeTuple<NlpOutput,Double,ThreeTuple> nlpOut=null;
	/**
	 * SemanticParsing模块的输出,内容,置信度,依赖项
	 */
	public ThreeTuple<ParsingOutput,Double,ThreeTuple> parsingOut=null;
	/**
	 * Solver模块的输出,内容,置信度,依赖项
	 */
	public ThreeTuple<QuestionSolverOutput,Double,ThreeTuple> solverOut=null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
