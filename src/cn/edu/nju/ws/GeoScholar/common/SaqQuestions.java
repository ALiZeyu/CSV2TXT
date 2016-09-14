package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;

/**
 * 
 * @author guyu
 *
 */
public class SaqQuestions{
	SaqHeadtext htxt;
	
	ArrayList<SaqText> txts;
	
	ArrayList<SaqTable> tables;
	
	ArrayList<String> picList = new ArrayList<String>();//图片标号列表
	
	ArrayList<SaqQuestion> qlist;
	
	public SaqQuestions(){
		
	}

	public SaqHeadtext getHtxt() {
		return htxt;
	}

	public void setHtxt(SaqHeadtext htxt) {
		this.htxt = htxt;
	}

	public ArrayList<SaqQuestion> getQlist() {
		return qlist;
	}

	public void setQlist(ArrayList<SaqQuestion> qlist) {
		this.qlist = qlist;
	}

	public ArrayList<SaqText> getTxts() {
		return txts;
	}

	public void setTxts(ArrayList<SaqText> txts) {
		this.txts = txts;
	}

	public ArrayList<SaqTable> getTables() {
		return tables;
	}

	public void setTables(ArrayList<SaqTable> tables) {
		this.tables = tables;
	}

	public ArrayList<String> getPicList() {
		return picList;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	
	public void addPicList(){//将saqQuestions中的图片列表传给隶属于其的saqQuestion
		for(SaqQuestion q:qlist){
			ArrayList<String> imgl = q.getPicList();
			imgl.addAll(getPicList());
			q.setPicList(imgl);
		}
	}
	
}