package cn.edu.nju.ws.GeoScholar.common;

import cn.edu.nju.ws.GeoScholar.solver.SemiStructuredQuestion;
import cn.edu.nju.ws.GeoScholar.solver.StructuredQuestion;

/**
 * SemanticParsing产生的输出
 * @author jwding
 *
 */
public class ParsingOutput {
	/**
	 * 半结构化问句，施张
	 */
	public SemiStructuredQuestion semiStructuredQuestion;
	/**
	 * 结构化问句，暂缓
	 */
	public StructuredQuestion structuredQuestion;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "半结构化问句:"+semiStructuredQuestion.toString();
	}
}
