package cn.edu.nju.ws.GeoScholar.templating.common;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.TypedDependency;

/**
 * NLP输出结果的存储结构
 * @author lfshi
 *
 */

public class QuestionTemplateFromNLP {
	/**
	 * 原始文本, 题干+选项, 中间用'$'分隔
	 */
	public String oriText ;							
	/**
	 * 模板类型
	 */
	public String templateType ;				
	/**
	 * 模板槽的个数
	 */
	public int slotCount ;						
	/**
	 * 槽信息列表
	 */
	public ArrayList<SlotStructureFromNLP> slots ;	
	/**
	 * 句法树
	 */
	public List<Tree> syntaxTreeLeaves;
	/**
	 * 线索词ID
	 */
	public int cueWord;
	/**
	 * toString
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(templateType + "(");
		boolean first = false;
		for (SlotStructureFromNLP slot : slots) {
			if (!first) {
				result.append(slot);
				first = true;
			} else 
				result.append("," + slot);
		}
		result.append(")");
		return result.toString();
	}
}
