package Default;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MubanExtraction {
/**
 * 获取所有试题的模板结果，文件每一行分别是“编号,文本,（粗粒度）分词,高阶模板类型,高阶模板线索词,二阶模板,二阶模板线索词”
 * 提取之后的模式是(文本|一级模板|一级模板线索词|二级模板|二级模板线索词)
 * */
	public static void main(String[] args) {
		//List<String> all=getAllList("F://installer/shijuan/147627542969.byPaper.choice");
//		List<String> all=getAllList("F://地理相关/500题审核/final/txt");
//		List<String> muban=extraction("F://地理相关/500题审核/final/txt",all);
//		getMubanType("F://地理相关/500题审核/final",muban);
		
//		String[] mubanArray = {"二阶_变化","二阶_分布","二阶_构成","二阶_因素关联","二阶_影响","二阶_运动","二阶_指示","一阶_对策","一阶_后果","一阶_影响","一阶_原因"};
//		for(String str : mubanArray)
//			cuewordExtraction("F://地理相关/500题审核/final", str);
		//mergeCueword("F://地理相关/500题审核/final/data/muban");
		//writeIntoSegFile("F://installer/shijuan/最新分词");
		//check("E://workspace/NewTemplate/data/result.txt");
		replaceGoldByMine("F:/地理相关/500题审核/final/data/muban/二阶_指示_.txt", "E://workspace/NewTemplate/data/result.txt");
	}
	
	//获取文件夹下所有文件的内容List
	public static List<String> getAllList(String path){
		List<String> final_result=new ArrayList<>();
		List<String> fileList=Util.getFileList(path);
		for(String str : fileList){
			List<String> temp = Util.read_file(str);
			temp.remove(0);
			for(String line:temp){
				//System.out.println(line);
				String[] all = line.split("!@#");
				//保证一道题一行通过看题号保证
				if(Pattern.matches("[0-9]{1,3}-[A|B|C|D|①|②|③|④|⑤|⑥](-[0-9])*", all[0])){
					//有时候由于回车被删除导致两句话合成一句，所以专门根据题号再分割一次
					Pattern p = Pattern.compile("[0-9]{1,3}-[A|B|C|D|①|②|③|④|⑤|⑥](-[0-9])*");
					Matcher m = p.matcher(line);
					List<String> matchStr=new ArrayList<>();
					while (m.find()) {
						int start = m.start();
						int end = m.end();
						matchStr.add(line.substring(start, end+1));
					}
					if(matchStr.size()==2){
						String[] s1=matchStr.get(0).split("-");
						String[] s2=matchStr.get(1).split("-");
						if(s1[0].length()==s2[0].length()){
							final_result.add(line.substring(0,line.indexOf(matchStr.get(1))));
							final_result.add(line.substring(line.indexOf(matchStr.get(1)),line.length()));
						
						}
						else{
							matchStr.set(1, matchStr.get(1).substring(matchStr.get(1).indexOf("-")));
							matchStr.set(1, s1[0]+matchStr.get(1));
	//						System.out.println();
	//						System.out.println();
							final_result.add(line.substring(0,line.indexOf(matchStr.get(1))));
							final_result.add(line.substring(line.indexOf(matchStr.get(1)),line.length()));
						}
						
					}
					else
						final_result.add(line);
					
				}
				else {
					//System.out.println(line+"111111");
					String t=final_result.get(final_result.size()-1);
					final_result.remove(final_result.size()-1);
					final_result.add(t+" "+line);
				}
			}
		}
		Util.writeFile(final_result, path.substring(0, path.lastIndexOf("/"))+"/data/所有模板.txt");
		return final_result;
	}
	
	//获取所有模板结果
	public static List<String> extraction(String path, List<String> result){
		List<String> final_result=new ArrayList<>();
		for(String line : result){
			//System.out.println(line);
//			if(line.equals("17-C!@#该段河流进行水利工程建设后	河流支流数量增加，流域面积增大!@#该 段 河流 进行 水利 工程 建设 后 河流 支流 数量 增加 ， 流域 面积 增大!@#!@#!@#变化_0(河流支流数量,,增加)变化_1(流域面积,,增大)时间限定_2(该段河流进行水利工程建设后,0-7)!@#0_11 1_15"))
//				System.out.println();
			if(line.endsWith(",,,,"))
				continue;
			System.out.println(line);
			//String[] temp=line.split(",");
			String[] temp=new String[7];
			String[] p=line.split("!@#");
			for(int i=0;i<p.length;i++)
				temp[i]=p[i];
			for(int i=p.length;i<7;i++)
				temp[i]="";

			if(temp.length!=7&&temp.length!=6){
				System.out.println(line);
				return null;
			}
			StringBuffer sb=new StringBuffer();
			sb.append(temp[1]+"|");
			String[] segmentation=temp[2].split(" ");
			if(temp[3].equals("")){
				sb.append("null|null|");
			}
			else {
				sb.append(temp[3]+"|");
				if(temp[4].equals(""))
					sb.append("null|");
				else {
					//sb.append(getword(segmentation, temp[4])+"|");
					if(temp[4].indexOf(" ")==-1)
						sb.append(getword(segmentation, temp[4])+"|");
					else {
						String[] tt=temp[4].split(" ");
						String cue="";
						for(String ss:tt)
							cue+=getword(segmentation, ss)+" ";
						sb.append(cue.trim()+"|");
					}
				}
			}
			
			if(temp[5].equals("")){
				sb.append("null|null");
			}
			else {
				sb.append(temp[5]+"|");
				if(temp.length==6||temp[6].equals(""))
					sb.append("null");
				else {
					if(temp[6].indexOf(" ")==-1)
						sb.append(getword(segmentation, temp[6]));
					else {
						String[] tt=temp[6].split(" ");
						String cue="";
						for(String ss:tt)
							cue+=getword(segmentation, ss)+" ";
						sb.append(cue.trim());
					}
				}
			}
			final_result.add(sb.toString());
		}
		Util.writeFile(final_result, path.substring(0, path.lastIndexOf("/"))+"/data/所有模板_1.txt");
		return final_result;
	}
	
	//根据下标在分词结果中找到触发词
	public static String getword(String[] seg,String str){
		str=str.substring(2);
		String result="";
		if(str.indexOf("-")==-1)
			return seg[Integer.valueOf(str)];
		else {
			String[] temp=str.split("-");
			try {
				for(String s:temp)
				result+=seg[Integer.valueOf(s)]+"-";
			} catch (ArrayIndexOutOfBoundsException e) {
				for(String s:seg)
					System.out.print(s);
			}
			
			return result.trim();
		}
	}
	//首先从所有的模板中中抽出所有的一阶、二阶类型，然后自动从中抽出文本写入相应的txt文件
	public static void getMubanType(String path, List<String> content){
		Set<String> first = new HashSet<>();
		Set<String> second = new HashSet<>();
		for(String line : content){
			String[] temp = line.split("\\|");
			if(!temp[1].equals("null")){
				String[] mt=temp[1].split(" ");
				String[] t=mt[0].split("_");
				first.add(t[0].trim());
			}
			if(!temp[3].equals("null")){
				String[] mt=temp[3].split(" ");
				String[] t=mt[0].split("_");
				if(t[0].trim().equals(" "))
					System.out.println(content);
				second.add(t[0].trim());
			}
		}
		List<String> errorlist = Arrays.asList(""," ","()原因","对策(抵御严寒,#,");
		Iterator<String> it=first.iterator();
		while(it.hasNext()){
			String checkWork = it.next();  
            if(errorlist.contains(checkWork)||checkWork.indexOf("(")!=-1||checkWork.indexOf("!")!=-1){  
                it.remove();  
            }  
		}
		it=second.iterator();
		while(it.hasNext()){
			String checkWork = it.next();  
            if(errorlist.contains(checkWork)||checkWork.indexOf("(")!=-1||checkWork.indexOf("!")!=-1){  
                it.remove();  
            }  
		}
		System.out.println(first);
		System.out.println(second);
		for(String str:first)
			writeBy1Type(path, content, str+"_");
		for(String str:second)
			writeBy2Type(path, content, str+"_");
	}
	
	public static void writeBy1Type(String path,List<String> content,String type){
		List<String> list = new ArrayList<>();
		for(String line : content){
			String[] temp = line.split("\\|");
			if(temp[1].contains(type)){
				list.add(temp[0]);
				list.add(temp[1]+" "+temp[2]);
			}
		}
		Util.writeFile(list, path+"/data/muban/一阶_"+type+".txt");
	}
	
	public static void writeBy2Type(String path,List<String> content,String type){
		List<String> list = new ArrayList<>();
		for(String line : content){
			String[] temp = line.split("\\|");
			if(temp[3].contains(type)){
				list.add(temp[0]);
				list.add(temp[3]+" "+temp[4]);
			}
		}
		Util.writeFile(list, path+"/data/muban/二阶_"+type+".txt");
	}
	
	//传入模板的txt文件路径，提取触发词以及含有这个触发词的句子
	public static void cuewordExtraction(String path, String type){
		List<String> result = new ArrayList<>();
		List<String> txt = Util.read_file(path+"/data/muban/"+type+"_.txt");
		Set<String> words = new HashSet<>();
		for(int i=0;i<txt.size();i++){
			if(i%2==1){
				String[] t = txt.get(i).split("\\) ");
				if(t.length>1&&!words.contains(t[1])){
					words.add(t[1]);
					result.add(txt.get(i-1)+"\t"+t[1]);
				}
			}
		}
		Util.writeFile(result, path+"/data/muban/"+type+"_cueword.txt");
	}
	//将上一步获取的【句子-触发词】格式的文件汇总起来，获取所有模板的
	public static void mergeCueword(String path){
		List<String> fileList=Util.getFileList(path);
		Iterator<String> it = fileList.iterator();
		while(it.hasNext()){
			String t = it.next();
			if(!t.endsWith("cueword_m.txt"))
				it.remove();
		}
//		System.out.println(fileList);
//		System.out.println(fileList.get(1).substring(fileList.get(1).lastIndexOf("\\")+1, fileList.get(1).indexOf("_cueword_m.txt")));
		List<String> result = new ArrayList<>();
		for(String p : fileList){
			List<String> temp = Util.read_file(p);
			result.add(p.substring(p.lastIndexOf("\\"),p.indexOf("_cueword_m.txt"))+"：");
			StringBuffer wordStr = new StringBuffer();
			for(String ss:temp){
				if(ss.length()==0)
					break;
				String[] t = ss.split("\t");
				wordStr.append(t[t.length-1]+"，");
			}
			result.add(wordStr.toString().substring(0, wordStr.toString().length()-1));
		}
		Util.writeFile(result, path+"/mergecueword.txt");
	}
	
	//从数据标注网站上下载的细粒度分词文件，把文件夹里的文件全部合在一起放在一个文件里
	public static void writeIntoSegFile(String path){
		List<String> filelist = Util.getFileList(path);
		List<String> result = new ArrayList<>();
		for(String p : filelist){
			List<String> temp = Util.read_file(p);
			temp.remove(0);
			result.addAll(temp);
		}
		Util.writeFile(result, path+"/newseg.txt");
	}
	/**由于一些标注的模板是错误的，因此需要把result.txt中的mine批量替换gold
	 * mine的格式：三行：文本，mine，gold
	 * gold的格式：两行：文本，gold*/
	public static void replaceGoldByMine(String path, String mypath){
		List<String> gold=Util.read_file(path);
		List<String> mine=Util.read_file(mypath);
		for(int i=0;i<mine.size();i+=3){
			String str=mine.get(i);
			int index=gold.indexOf(str);
			if(index!=-1){
				String myMine=mine.get(i+1);
				gold.set(index+1, myMine);
			}else {
				System.out.println(str);
			}
		}
		Util.writeFile(gold, "F://地理相关/500题审核/final/data/muban/二阶_指示.txt");
	}
	public static void check(String path){
		List<String> gold=Util.read_file(path);
		int i=0;
		for(String s:gold){
			if(i%3==0&&(s.startsWith("mine：")||s.startsWith("gold："))){
				System.out.println(i);
				break;
			}
			else if (i%3==1&&!s.startsWith("mine：")) {
				System.out.println(i);
				break;
			}
			else if (i%3==2&&!s.startsWith("gold：")) {
				System.out.println(i);
				break;
			}
			i++;
		}
	}

}
