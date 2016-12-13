package com.Muban;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Default.Util;

public class MergeMuban {

	public static void main(String[] args) {
		//mergeAll("F://地理相关/500题审核/final/data/muban");
		//extract("data/paper/2011-北京.data", "data/paper/2011-北京.data.txt");
		//compareType("data/paper/科大讯飞-地理测试01.data.txt", "data/paper/科大讯飞-地理测试01.data.type");
		//covert();
		//getOriginal();
		count("E://workspace/CSV2TXT/data/paper/goldAll.txt");
	}
	/**将分开的模板文件合在一起
	 * 输入格式：文件格式是一行文本、一行模板
	 * 输出：所有文本加模板
	 * */
	public static void mergeAll(String path){
		List<String> files=Default.Util.getFileList(path);
		Iterator<String> it = files.iterator();
		while(it.hasNext()){
			String s=it.next();
			if(!s.endsWith("_.txt"))
				it.remove();
		}
		List<String> all=new ArrayList<>();
		for(String s : files)
			all.addAll(Default.Util.read_file(s));
		List<String> text=new ArrayList<>();
		List<String> muban=new ArrayList<>();
		for(int i=0;i<all.size();i+=2){
			String t=all.get(i);
			if(text.contains(t)){
				int index=text.indexOf(t);
				if(!muban.get(index).contains(all.get(i+1))){
					String s=muban.get(index)+"\t"+all.get(i+1);
					muban.set(index, s);
				}
			}else {
				text.add(t);
				muban.add(all.get(i+1));
			}
		}
		all.clear();
		for(int i=0;i<text.size();i++){
			all.add(text.get(i));
			all.add(muban.get(i));
		}
		Default.Util.writeFile(all, "data/500template.txt");
	}
	
	//根据试题文本从模板记录中抽出模板最终结果
	public static void extract(String path, String rp){
		List<String> text=Default.Util.read_file(path);
		List<String> all=Default.Util.read_file("data/500template.txt");
		List<String> result=new ArrayList<>();
		for(String str:text){
			str=del(str);
			if(all.contains(str)){
				if(str.equals("在图3所示的山区自然灾害链中，①②③④依次是	滑坡、泥石流、地震、崩塌"))
					System.out.println();
				int index=all.indexOf(str);
				result.add(str);
				String m=all.get(index+1);
				String[] mb=m.split("\t");
				//抽出模板类型
				String type="";
				for(String ss:mb){
					type+=ss.split("\\(")[0]+" ";
				}
				type=type.trim();
				result.add(type);
				result.add(all.get(index+1));
			}else {
				result.add(str);
				System.out.println(str);
			}
		}
		Default.Util.writeFile(result, rp);
	}
	
	public static void compareType(String gpath, String mypath){
		List<String> gold=Util.read_file(gpath);
		List<String> mine=Util.read_file(mypath);
		float right=0,all=0,oracle=0;
		int g=1,m=1;
		while(g<gold.size()&&m<mine.size()){
			gold.set(g-1, del(gold.get(g-1)));
			mine.set(m-1, del(mine.get(m-1)));
			if(!gold.get(g-1).equals(mine.get(m-1))){
				System.out.println("句子不一致了 "+gold.get(g-1));
				break;
			}
			List<String> glist=Util.strToList(gold.get(g));
			List<String> mlist=Util.strToList(mine.get(m));
			all+=mlist.size();oracle+=glist.size();
			boolean flag=true;
			for(String type:mlist){
				if(glist.contains(type)){
					int index=glist.indexOf(type);
					glist.remove(index);
					right++;
				}else if(flag){
					System.out.println(gold.get(g-1));
					System.out.println(mine.get(m+1));
					System.out.println(gold.get(g+1));
					flag=false;
				}
			}
			if(flag && (glist.size()>0)){
				System.out.println(gold.get(g-1));
				System.out.println(mine.get(m+1));
				System.out.println(gold.get(g+1));
			}
			g+=3;
			m+=3;
		}
		float p=right/all;
		float r=right/oracle;
		System.out.println("正确率："+p+"\n召回率："+r);
		System.out.println("F-score："+2.0/(1.0/p+1.0/r));
		
	}
	
	public static String del(String question){
		String[] del = {"最有可能", "有可能", "最可能", "可能", "主要","图中反映"};
		for (String s : del) {
			while (question.contains(s))
				question = question.substring(0, question.indexOf(s)) + question.substring(question.indexOf(s) + s.length(), question.length());
		}
		return question;
	}
	//文件格式是0文本!@#1高阶模板!@#2高阶模板类型!@#3二阶模板!@#4二阶模板类型
	public static void covert(){
		String[] path={"data/paper/16-全国.data","data/paper/科大讯飞-地理测试01.data","data/paper/2016-丰台区期末.data"};
		for(String p : path){
			List<String> strs=Util.read_file(p);
			List<String> result=new ArrayList<>();
			for(String str : strs){
				if(str.equals("促使佛山陶瓷产业向外转移的主要原因是佛山	产业结构调整!@#原因_0(促使佛山陶瓷产业向外转移,佛山产业结构调整)!@#原因!@#其他陈述_0(佛山,产业结构,调整)!@#其他陈述"))
					System.out.println(str);
				String[] temp=new String[5];
				for(int i=0;i<5;i++)
					temp[i]="";
				for(int i=0;i<str.split("!@#").length;i++)
					temp[i]=str.split("!@#")[i];
				result.add(temp[0]);
				String sec=temp[2]+" "+temp[4]+" ";
				sec=sec.replace("时间限定", "");
				sec=sec.replaceAll("  ", " ");
				result.add(sec.trim());
				String thi=temp[1]+temp[3];
				thi=thi.replaceAll(" ", "");
				thi=thi.replaceAll("_[0|1|2|3|4|5]\\(", "\\(");
				if(thi.indexOf(")")==thi.lastIndexOf(")"))
						result.add(thi);
				else {
					List<String> t=new ArrayList<>();
					while(thi.length()>0){
						int index=0;
						while(!(index<thi.length()&&thi.charAt(index)==')' && (index==thi.length()-1||strBeginWith(thi.substring(index+1))))){
							index++;
						}
						String s=thi.substring(0, index+1);
						t.add(s);
						thi=thi.replace(s, "");
					}
					for(String ss:t)
						thi+=ss+" ";
					result.add(thi.trim());
				}
			}
			Util.writeFile(result, p+".txt");
		}
	}
	
	public static void getOriginal(){
		String[] path={"data/paper/16-全国.data","data/paper/科大讯飞-地理测试01.data","data/paper/2016-丰台区期末.data"};
		for(String p : path){
			List<String> strs=Util.read_file(p);
			List<String> result=new ArrayList<>();
			for(String str : strs){
				String[] temp=new String[5];
				for(int i=0;i<5;i++)
					temp[i]="";
				for(int i=0;i<str.split("!@#").length;i++)
					temp[i]=str.split("!@#")[i];
				result.add(temp[0]);
			}
				
			Util.writeFile(result, p);
		}
		
	}
	
	public static boolean strBeginWith(String s){
		String[] b={"时间限定","原因","后果","影响","对策","其他关联","指示","比较","变化","因素关联","运动","构成","分布","其他陈述"};
		for(String bb:b)
			if(s.startsWith(bb))
				return true;
		return false;
	}
	//统计模板类型的分布
	public static void count(String p){
		List<String> type=new ArrayList<>();
		int[] count=new int[13];
		String[] b={"原因","后果","影响","对策","其他关联","指示","比较","变化","因果关联","运动","构成","分布","其他陈述"};
		for(String bb:b)
			type.add(bb);
		List<String> gold=Util.read_file(p);
		int g=1;
		int sum=0;
		while(g<gold.size()){
			System.out.println(gold.get(g-1));
			List<String> glist=Util.strToList(gold.get(g).trim());
			for(String str:glist){
				sum++;
				int index=type.indexOf(str);
				count[index]++;
			}
			g+=3;
		}
		for(int i=0;i<13;i++)
			System.out.println((float)count[i]/sum);
	}

}
