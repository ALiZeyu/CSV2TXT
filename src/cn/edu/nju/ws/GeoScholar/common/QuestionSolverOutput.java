package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;

/**
 * @author jwding
 * 针对一个选项的求解器输出
 */
public class QuestionSolverOutput {
	/**
	 * 求解器给出的答案，取值为-1到1的double，-1表示一定错误，1表示一定正确，0表示不知对错
	 */
	public double judgement;
	/**
	 * 给出判断的求解器名称，通常是求解器所在类的名称
	 */
	public String solverName=new String();
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "求解器"+solverName+"给出正确度:"+judgement;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * QuestionSolverOutput类的默认构造函数
	 * 初始化solverName 为 new String()
	 */
	public QuestionSolverOutput() {

	}

	/**
	 * QuestionSolverOutput类构造函数
	 * 由solverName及judgement构造
	 * @param solverName 求解器名称
	 * @param judgement 求解器给出的答案
     */
	public QuestionSolverOutput(String solverName, double judgement) {
		this.solverName = solverName;
		this.judgement = judgement;
	}
}
