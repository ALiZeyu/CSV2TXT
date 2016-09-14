package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;

/**
 * 整张试卷
 * @author jwding
 * 
 */
public class ExamPaper {
	/**
	 * XML文件中的试卷名称
	 */
	public String examName;
	/**
	 * XML文件中的试卷ID
	 */
	public String examID;
	/**
	 * XML文件中的试卷科目
	 */
	public String examSubject;
	/**
	 * 题目的列表
	 */
	public ArrayList<Problem> problemList;
	/**
	 * 题目中表格的列表
	 */
	public ArrayList<TabKnowledge> tableList;
	
	/**
	 * 简答题部分
	 */
	public SAQs saqs;
	
	/** 根据id查找图表信息
	 * @param id 图表自带的ID
	 * @return 找到的图表信息，或者null
	 */
	
	public TabKnowledge findTabKnowledge(String id)
	{
		for(int i=0;i<this.tableList.size();i++)
		{
			if(this.tableList.get(i).id.equals(id))
				return this.tableList.get(i);
		}
		return null;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
