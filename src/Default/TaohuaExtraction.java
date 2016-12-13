package Default;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaohuaExtraction {
	/**
	 * 本类是对套话进行处理：
	 * getAllTaohua()是从标注网站上的数据中抽出标注的套话
	 * deleteDup()是删除一道题四句话套话一样的情况，留一个就行了
	 * matchTaohua()是从句子中根据正则表达式匹配出套话*/
	public static void main(String[] args) {
		//getAllTaohua("F://installer/shijuan/上下文");
		//deleteDup("data/AllTaohua.txt");
		//matchTaohua();
		String s="aaaa。bbbb";
		System.out.println(s.indexOf("。")+"。".length());
		System.out.println(s.substring(s.indexOf("。")+"。".length()));
		
	}
	
	public static void matchTaohua(){
		List<String> list = Util.read_file("data/套话138");
		String[] regex={"(根据|据|由|结合|从).*(分析|推测|推断|判断|可知)(，|出){0,1}(下列|关于|有关).*(正确|符合实际|可信).(是)",
				"(根据|据|由|结合|从).*(分析|推测|推断|判断|可知)(，|出){0,1}",
				"(下列|关于|有关).*(正确|符合实际|可信).{1,3}(是)"};
		//有一些不符合上述表达式的套话，特别处理一下
		String[] special={"依据图11与图12，","图中显示","按此理解，","根据图示信息，","图7中","图2中①②③三地自然条件相比较","图3中①、②、③、④四地自然条件相比","文中描述的现象可以解释为","根据所学知识，你认为","结合图4，"};
		Set<String> spSet=new HashSet<>();
		for(String str : special)
			spSet.add(str);
		for(int i=0;i<list.size();i+=2){
//			String standard=list.get(i+1).split("\t")[0];
			String standard=list.get(i+1);
			String mine="";
			String sentence=list.get(i).substring(list.get(i).indexOf("。")+1, list.get(i).length());
			List<String> match = matchStr(regex, sentence);
			if(match.size()==0){
				for(String ss:spSet)
					if(sentence.contains(ss)){
						int ssindex=sentence.indexOf(ss);
						if(ssindex==0||sentence.charAt(ssindex-1)=='，'){
							mine=ss;
							break;
						}
					}
				if(mine.equals("")){
					System.out.println("没有匹配结果   "+list.get(i));
					System.out.println(standard);
				}
			}
			else if(match.size()==1){
				mine=match.get(0);
			}
			else 
				mine=match.get(0)+"\t"+match.get(1);
			
			if(!mine.equals("") && !mine.equals(standard)){
				System.out.println("结果不相同   "+sentence);
				System.out.println(mine+"\t"+standard);
			}
		}
	}
	//输入一句话，返回list中含有套话和上下文
	public static List<String> matchStr(String[] regs, String str){
		List<String> result = new ArrayList<>();
		int i=0;
		String taohua="";
		String sxw=null;
		for(String reg:regs){
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(str);
			int start=0,end=0;
			while (m.find()) {
				start = m.start();
				end = m.end();
				taohua=str.substring(start, end);
			}
			if(!taohua.equals("")){
				result.add(taohua);
				sxw = getSXW(i + 1, taohua);
				if (!(sxw == null) && sxw.length() > 0)
					result.add(sxw);
				break;
			}
//			if(i==0&&result.size()>0)
//				break;
			//正确的是前面还有文字，看例子处理
//			if(i==2&&result.size()>0){
//				if()
//			}
			i++;
		}
		return result;
	}
	
	//从套话中抽出上下文信息
	public static String getSXW(int type, String taohua){
		String sxw=null;
		String[]regs=new String[2];
		switch (type) {
		case 1:
			regs[0]="(根据|据|由|结合|从).*(分析|推测|推断|判断|可知)(，|出){0,1}(下列|关于|有关)(对|关于|有关){0,1}";
			regs[1]="(正确|符合实际|可信).(是)";
			break;
		case 2:
			regs[0]="(根据|据|由|结合|从)";
			regs[1]="(分析|推测|推断|判断|可知)(，|出){0,1}";
			break;
		case 3:
			regs[0]="(下列|关于|有关)(对|关于|有关){0,1}";
			regs[1]="(正确|符合实际|可信).(是)";
			break;
		default:
			break;
		}
		for(String reg:regs){
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(taohua);
			String temp ="";
			int start=0,end=0;
			while (m.find()) {
				start = m.start();
				end = m.end();
				temp = taohua.substring(start, end);
			}
			taohua=taohua.replace(temp, "");
		}
		sxw=simplifysxw(taohua,type);
		return sxw;
	}
	//进一步处理上下文，去除【，|叙述|描述】之类的
	public static String simplifysxw(String sxw,int type){
		if(type==1||type==3){
			//关于。。正确的是
			if(sxw.indexOf("，")!=-1)
				sxw=sxw.substring(0, sxw.indexOf("，"));
			String[] tail={"的叙述","的描述","的说法","说法","叙述","描述","最"};
			for(String s : tail){
				if(sxw.contains(s))
					sxw=sxw.replace(s, "");
			}
		}
		else if (type==2) {
			//据图可以判断
			if(sxw.indexOf("，")!=-1)
				sxw=sxw.substring(0, sxw.indexOf("，"));
			
			String[] tail={"可以","可"};
			for(String s : tail){
				if(sxw.endsWith(s))
					sxw=sxw.substring(0,sxw.length()-s.length());
			}
			//
			if(sxw.matches("图.{0,2}|此")||((sxw.contains("图")||sxw.contains("表")||sxw.contains("材料"))&&sxw.contains("信息")))
				sxw="";
		}
		return sxw;
	}
	
	/**
	 * 输入：文本!@#（粗粒度）分词!@#可删除的套话!@#上下文信息
	 * 输出：文本+套话+上下文信息
	 * 1、在句号之前的不要
	 * 2、图中、可能、最可能这些不要
	 * */
	public static void getAllTaohua(String path){
		String[] strings={"图中","最有可能","有可能","最可能","可能","主要"};
		Set<String> set=new HashSet<>();
		for(String str:strings) set.add(str);
		
		List<String> result=new ArrayList<>();
		List<String> list = Util.getAllFileContentList(path);
		for(String str : list){
			System.out.println(str);
			String taohua="",shangxiawen="";
			String[] temp = str.split("!@#");
			List<String> tag = new ArrayList<>(Arrays.asList(temp[1].split(" ")));
			
			if(temp.length > 2 && !temp[2].equals("")){
				String[] temp2=temp[2].split(" ");
				if(temp2.length==1 && !temp2[0].contains("-"))
					continue;
				if(temp.length==4 && !temp[3].equals("")){
					String[] temp3=temp[3].split(" ");
					merge(temp2, temp3);
				}
				for(String s:temp2){
					String sss=getwords(s, tag);
					if(!set.contains(sss))
						taohua+=sss;
				}
			}
			
			if(temp.length==4 && !temp[3].equals("")){
				String[] temp3=temp[3].split(" ");
				for(String s:temp3){
					shangxiawen+=getwords(s, tag);
				}
			}
			if(!taohua.equals("")||!shangxiawen.equals(""))
				result.add(temp[0]+"\n"+taohua+"\t"+shangxiawen);
		}
		Util.writeFile(result, "data/AllTaohua.txt");
	}
	
	public static String getwords(String s, List<String> tags){
		int index=tags.indexOf("。");
		int b=0,e=0;
		String result="";
		if(s.contains("-")&&!s.startsWith("-")){
			String[] list = s.split("-");
			b=Integer.parseInt(list[0]);
			e=Integer.parseInt(list[1]);
		}else{
			b=e=Integer.parseInt(s);
		}
		if(b<index)
			return "";
		else {
			for(int i=b;i<=e;i++)
				result+=tags.get(i);
		}
		return result;
	}
	
	public static void merge(String[] th, String[] sxw){
		if(th.length<2)
			return;
		String shangxiawen="";
		for(String str:sxw) shangxiawen+=str+" ";
		int b=getEnd(th[0]);
		int e=getBegin(th[1]);
		if(shangxiawen.contains(String.valueOf(b+1))&&shangxiawen.contains(String.valueOf(e-1))){
			th[0]=mergestr(th[0], th[1]);
			th[1]=String.valueOf(-2);
		}
	}
	
	public static int getEnd(String str){
		if(!str.contains("-"))
			return Integer.parseInt(str);
		String[] array = str.split("-");
		return Integer.parseInt(array[1]);
	}
	
	public static int getBegin(String str){
		if(!str.contains("-"))
			return Integer.parseInt(str);
		String[] array = str.split("-");
		return Integer.parseInt(array[0]);
	}
	
	public static String mergestr(String str1, String str2){
		int b=getBegin(str1);
		int e=getEnd(str2);
		return b+"-"+e;
	}
	
	public static void deleteDup(String path){
		List<String> list=Util.read_file("data/AllTaohua.txt");
		List<String> result=new ArrayList<>();
		List<String> taohua=new ArrayList<>();
		result.add(list.get(0));
		result.add(list.get(1));
		taohua.add(list.get(1).split("\t")[0]);
		for(int i=0;i<list.size();i+=2){
			if(list.get(i+1).split("\t")[0].equals(taohua.get(taohua.size()-1)))
				continue;
			if(list.get(i+1).split("\t")[0].length()<3)
				continue;
			result.add(list.get(i));
			result.add(list.get(i+1));
			taohua.add(list.get(i+1).split("\t")[0]);
		}
		Util.writeFile(result, "data/MTaohua.txt");
	}

}
