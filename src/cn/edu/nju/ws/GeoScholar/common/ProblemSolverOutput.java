package cn.edu.nju.ws.GeoScholar.common;

/**
 * @author jwding
 * 针对一整个选择题的求解器输出
 */
public class ProblemSolverOutput {
	/**
	 * Problem级别求解器给出的答案，4个-1到1的值，分别为ABCD四个选项的正确性。可以只选出正确的一个(0.8,0,0,0)，也可以用于排除错误答案(-1,-1,0,0)
	 * 注意：及时是组合型选择题，该结构也用于记录ABCD四个选项的正确性，请求解器自行考虑组合事宜
	 */
	public double[] judgement=new double[4];
	/**
	 * 给出判断的求解器名称，通常是求解器所在类的名称
	 */
	public String solverName=new String();
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "求解器"+solverName+"给出解答:A:"+judgement[0]+"B:"+judgement[1]+"C:"+judgement[2]+"D:"+judgement[3];
	}
}
