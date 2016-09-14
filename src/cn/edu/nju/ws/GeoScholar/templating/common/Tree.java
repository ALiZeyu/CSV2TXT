package cn.edu.nju.ws.GeoScholar.templating.common;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
	public String content;
	public Tree parent;
	public int no;
	public List<Tree> child = new ArrayList<Tree>();
	
	public static List<Tree> getLeaves(Tree t) {
		List<Tree> l = new ArrayList<Tree>();
		if (t.child.size() > 0) {
			for (int i = 0; i < t.child.size(); i++)
				l.addAll(getLeaves(t.child.get(i)));
		}
		else 
			l.add(t);
		return l;
	}
	
	private static String traversal(Tree t) {
		String s = "";
		if (t.child.size() > 0) {
			s += "(" + t.content + " ";
			for (int i = 0; i < t.child.size(); i++)
				s += traversal(t.child.get(i));
			s += ")";
		}
		else 
			s += t.content;
		return s;
	}
	
	public static Tree findNodeByNo(Tree t, int no) {
		if (t.no == no) return t;
		else {
			for (int i = 0; i < t.child.size(); i++) {
				Tree temp = findNodeByNo(t.child.get(i), no);
				if (temp != null)
					return temp;
			}
		}
		return null;
	}
	
	public String toString() {
		String s1 = "";
		if (this.child.size() > 0) {
			String s = "(";
			for (int i = 0; i < this.child.size(); i++)
				s += traversal(this.child.get(i));
			s += " )";
			for (int i = 0; i < s.length() - 1; i++) {
				s1 += s.charAt(i);
				if (s.charAt(i) == '(' && s.charAt(i + 1) == '(')
					s1 += " ";
				if (s.charAt(i) == ')' && s.charAt(i + 1) == '(')
					s1 += " ";
			}
			s1 += s.charAt(s.length() - 1);
		}
		else
			s1 = "( )";
		try {
			s1 = new String(s1.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s1;
	}
	
}
