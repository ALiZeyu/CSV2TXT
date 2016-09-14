package cn.edu.nju.ws.GeoScholar.common;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
 * @author guyu
 *
 */
public class SAQs{
	ArrayList<SaqQuestions> qslist;
	
	public SAQs(){
		
	}

	public ArrayList<SaqQuestions> getQslist() {
		return qslist;
	}

	public void setQslist(ArrayList<SaqQuestions> qslist) {
		this.qslist = qslist;
	}
	
	public void printPaper(){
		Iterator<SaqQuestions> it0 = qslist.iterator();
		for(; it0.hasNext();){
			SaqQuestions qs = it0.next();
			
			ArrayList<SaqQuestion> qlist = qs.getQlist();
			for(Iterator<SaqQuestion> it1 = qlist.iterator(); it1.hasNext();){
				SaqQuestion q = it1.next();
				SaqText txt = q.getText();
				if(txt != null)
					System.out.println(txt.getTxt());
			}
		}
	}
}