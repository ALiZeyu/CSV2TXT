package Default;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractTerm {

	public static void main(String[] args) {
		//getTerm();//抽出粗分术语等的细分表示形式并标上@@
		//tagtime();//利用规则给时间自动打点标签，但用处并不大
		//replace();//根据手工标好的术语等去文件中替换
		//getAllSeg();
		getAllTrainAndTest();
		getAllTrainAndTestV_2();
		//get3dic();
		//modifyTagData();
		//processdiff();
		//regex();
		//replaceSegTag();
		//getFour();
		//deal_with_keneng();//处理可能这个词由vv改成ad
		//get_word_base_tag("M");
	}
	//抽出粗分术语等的细分表示形式并标上@@
	public static void getTerm(){
		List<String> all=new ArrayList<>();
		Map<String, String> result=new HashMap<>();
		String[] tag={"time","loc","term","num"};
		List<String> tagList=new ArrayList<>();
		for(int i=0;i<4;i++)
			tagList.add(tag[i]);
		List<String> data=Util.read_file("output/2th.txt");
		for(int tt=0;tt<4;tt++){
			//all.add(tag[tt]);
			all.clear();
			result.clear();
			int index=0;
			while(index<data.size()){
				System.out.println(tag[tt]+" "+data.get(index));
				String rough=data.get(index);
				if(rough.equals("在_P 6月到8月期间_time 正午太阳高度角_term ①_NN 比_P ②_NN 小_VA"))
					System.out.println();
				String detail=data.get(index+1);
				String[] r=rough.split(" ");
				String[] d=detail.split(" ");
				int rlen=r.length;
				int dlen=d.length;
				
				int j = 0, c_len = 0, x_len = 0;
				for (int i = 0; i < rlen; i++) {
					if (r[i].endsWith(tag[tt])) {
						if (r[i].split("_")[0].length()==d[j].split("_")[0].length()) {
							c_len += r[i].split("_")[0].length();
							x_len += d[j].split("_")[0].length();
							addMap(result, r[i], d[j]);
							j++;
						} else {
							c_len += r[i].split("_")[0].length();
							String v="";
							while (j < dlen && c_len >= (x_len+d[j].split("_")[0].length())) {
								x_len += d[j].split("_")[0].length();
								v+=d[j]+" ";
								j++;
							}
							addMap(result, r[i], v.trim());
						}
					} 
					else {
						if (r[i].split("_")[0].length()==d[j].split("_")[0].length()) {
							c_len += r[i].split("_")[0].length();
							x_len += d[j].split("_")[0].length();
							j++;
						} else {
							c_len += r[i].split("_")[0].length();
							while (j < dlen && c_len >= (x_len+d[j].split("_")[0].length())) {
								x_len += d[j].split("_")[0].length();
								j++;
							}
						}
					}
				}
				index+=2;
			}
			for(Map.Entry<String, String> en:result.entrySet()){
				//all.add(en.getKey());
				all.add(en.getValue());
			}
			Util.writeFile(all, "output/"+tag[tt]+".txt");
		}
		//Util.writeFile(all, "output/termetc.txt");
	}
	
	public static void addMap(Map<String, String> result,String key,String value){
		if(result.containsKey(key)&&!result.get(key).equals(value)){
			String v=result.get(key)+"        "+value;
			result.put(key, v);
		}else {
			result.put(key, value);
		}
	}
	//利用规则给时间自动打点标签，但用处并不大
	public static void tagtime(){
		List<String> timelist=Util.read_file("output/time.txt");
		List<String> result=new ArrayList<>();
		for(String str:timelist){
			String[] t=str.split(" ");
			String v="";
			for(int i=0;i<t.length;i++){
				String temp=t[i].split("_")[0];
				if(timeType(temp)!=null)
					v+=temp+"_"+timeType(temp)+" ";
				else {
					v+=t[i]+" ";
				}
			}
			result.add(v.trim());
		}
		Util.writeFile(result, "output/t.txt");
	}
	//判断时间类型
	static String timeType(String str){
		Pattern p1=Pattern.compile("\\d{0,3}[;|：]\\d{0,3}");
		Pattern p2=Pattern.compile("\\d{1,5}");
		Pattern p3=Pattern.compile("\\d{1,5}[年|月|日|月份|时|分|秒]");
		if(p1.matcher(str).matches()||p3.matcher(str).matches())
			return "NT";
		if(p2.matcher(str).matches())
			return "CD";
		return null;
	}
	//根据手工标好的术语等去文件中替换
	public static void replace(){
		List<String> lists=Util.read_file("output/term.txt");
		List<String> data=Util.read_file("output/3th.txt");
		List<String> result=new ArrayList<>();
		Collections.sort(lists, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.length()-o2.length();
			}
		});
		for(String str:data){
			System.out.println(str);
			if(str.indexOf("@@")==-1){
				result.add(str);
			}else {
				for(int i=lists.size()-1;i>=0;i--){
					String sub=getsub(lists.get(i));
					if(str.indexOf(sub)>-1)
						str=str.replace(sub, lists.get(i));
				}
				result.add(str);
			}
		}
		Util.writeFile(result, "output/33th.txt");
	}
	
	//构造segtag语料，将术语合为一词
		public static void replaceSegTag(){
			List<String> lists=Util.read_file("input/4th.txt");
			List<String> terms=Util.read_file("output/term_gold.txt");
			List<String> seg_result=new ArrayList<>();
			List<String> tag_result=new ArrayList<>();
			Collections.sort(terms, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.length()-o2.length();
				}
			});
			for(int i=0;i<lists.size();i++){
				if(i%4==0){
//					if(lists.get(i).equals("若 此 图 表示 大气三圈环流 中 的 低纬环流 ， 则甲 是 副热带高气压带"))
//						System.out.println();
					Map<Integer, String> position=new HashMap<>();
					String[] r_seg=lists.get(i).split(" ");
					int len=0;
					for(String str:r_seg){
						if(terms.contains(str))
							position.put(len, str);
						len+=str.length();
					}
					
					String[] d_seg=lists.get(i+2).split(" ");
					len=0;
					String dd_seg="";
					int index=0;
					Map<Integer, Integer> term_position=new HashMap<>();
					while(index<d_seg.length){
						String str=d_seg[index];
						if(position.containsKey(len)){
							int l=0;
							int b=index;
							while(l<position.get(len).length()){
								l+=d_seg[index].length();
								index++;
							}
							term_position.put(b, index);
							dd_seg+=position.get(len)+" ";
							len+=position.get(len).length();
						}else {
							len+=str.length();
							dd_seg+=str+" ";
							index++;
						}
						
					}
					String[] d_tag=lists.get(i+3).split(" ");
					String dd_tag="";
					int t=0;
					while(t<d_tag.length){
						if(term_position.containsKey(t)){
							List<String> tags=new ArrayList<>();
							for(int tt=t;tt<term_position.get(t);tt++){
								dd_tag+=d_tag[tt].split("_")[0];
								tags.add(d_tag[tt].split("_")[1]);
							}
							if(tags.contains("VV")){
								dd_tag+="_VV ";
								System.out.println(dd_tag);}
							else {
								dd_tag+="_NN ";
							}
							t=term_position.get(t);
						}else {
							dd_tag+=d_tag[t]+" ";
							t++;
						}
					}
					seg_result.add(dd_seg.trim()+" 。");
					tag_result.add(dd_tag.trim()+" 。_PU");
				}
			}
			for(int i=0;i<seg_result.size();i++){
				String[] s1=seg_result.get(i).split(" ");
				String[] t1=tag_result.get(i).split(" ");
				if(s1.length!=t1.length){
					System.out.println(seg_result.get(i));
				}
			}
//			Util.writeFile(seg_result, "output/seg_term.txt");
//			Util.writeFile(tag_result, "output/tag_term.txt");
		}
	
	static String getsub(String str){
		String sub="";
		String[] t=str.split(" ");
		for(String s:t){
			sub+=s.split("_")[0]+"_"+"@@"+" ";
		}
		return sub.trim();
	}
	
	static String getSeg(String str){
		String sub="";
		String[] t=str.split(" ");
		for(String s:t){
			sub+=s.split("_")[0]+" ";
		}
		return sub.trim();
	}
	//从词性标注中把分词抽出来
	static void getAllSeg(){
		List<String> data=Util.read_file("output/33th.txt");
		List<String> result=new ArrayList<>();
		for(String str:data){
			result.add(getSeg(str));
			result.add(str.trim());
		}
		Util.writeFile(result, "output/4th.txt");
	}
	//从4th抽出细分的分词和词性标注结果
	static void getAllTrainAndTest(){
		List<String> data=Util.read_file("output/4th.txt");
		List<String> seg=new ArrayList<>();
		List<String> tag=new ArrayList<>();
		for(int i=0;i<data.size();i++){
			if(i%4==2)
				seg.add(data.get(i));
			if(i%4==3)
				tag.add(data.get(i));
				
		}
		Util.writeFile(seg, "output/seg_gold.txt");
		Util.writeFile(tag, "output/tag_gold.txt");
	}
	
	//抽出所有术语
		static void getAllTrainAndTestV_2(){
			List<String> data=Util.read_file("output/4th.txt");
			Set<String> term=new HashSet<>();
			for(int i=0;i<data.size();i++){
				if(i%4==1){
					String[] sen=data.get(i).split(" ");
					for(String str:sen)
						if(str.endsWith("_term"))
							term.add(str.split("_")[0]);
				}
			}
			List<String> seg=new ArrayList<>();
			seg.addAll(term);
			Util.writeFile(seg, "output/term_gold.txt");
		}
	//抽出文本中长度大于3词语，尝试加入前后处理词典
	static void get3dic(){
		List<String> data=Util.read_file("output/seg.txt");
		List<String> seg=new ArrayList<>();
		Set<String> dic=new HashSet<>();
		for(String str:data){
			String[] words=str.split(" ");
			for(String sub:words)
				if(sub.length()>2&&!hasDigit(sub))
					dic.add(sub);
		}
		seg.addAll(dic);
		Util.writeFile(seg, "output/dic.txt");
	}
	
	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}
	
	static void modifyTagData(){
		int in=1;
		List<String> lines=Util.read_file("output/tag.txt");
		for(String line:lines){
			String[] w = line.split(" ");
			for (int i = 0; i < w.length; i++) {
				String[] t = w[i].split("_");
				if (t.length < 2) {
					System.out.println(in+" "+t[0]+" wrong! " + line);
				} 
			}
			in++;
		}
	}
	//将分开的词对比一下map<mine,gold>
	static void processdiff(){
		List<String> lines=Util.read_file("E://workspace/To94%/data/diff.txt");
		int i=0;String t;
		
		Map<String, String> d=new HashMap<>();
		while(i<lines.size()){
			t=lines.get(i);
			String[] mine=t.split(" ");
			t=lines.get(i+1);
			String[] gold=t.split(" ");
			int m=0,g=0,mlen=0,glen=0;;
			while(m<mine.length||g<gold.length){
				if(mine[m].equals(gold[g])){
					mlen+=mine[m].length();
					glen+=gold[g].length();
					m++;g++;
				}else {
					if(mine[m].length()>gold[g].length()){
						String key=mine[m];
						String value=gold[g]+" ";
						mlen+=mine[m].length();
						glen+=gold[g].length();
						while (glen<mlen) {
							g++;
							glen+=gold[g].length();
							value+=gold[g]+" ";
						}
						if(glen==mlen)
							addMap(d, key, value.trim());
						else {
							 while(glen!=mlen){
								 if(glen<mlen){
									 g++;
									glen+=gold[g].length();
								 }else {
									 m++;
									mlen+=mine[m].length();
								}
							 }
						}
						m++;g++;
					}else {
						String key=mine[m]+" ";
						String value=gold[g];
						mlen+=mine[m].length();
						glen+=gold[g].length();
						while (glen>mlen) {
							m++;
							mlen+=mine[m].length();
							key+=mine[m]+" ";
						}
						if(glen==mlen)
							addMap(d, key.trim(), value);
						else {
							while(glen!=mlen){
								 if(glen<mlen){
									 g++;
									glen+=gold[g].length();
								 }else {
									 m++;
									mlen+=mine[m].length();
								}
							 }
						}
						m++;g++;
					}
				}
			}
			i+=2;
		}
		List<String> result=new ArrayList<>();
		for(Map.Entry<String, String> en:d.entrySet()){
			result.add(en.getKey()+"     "+en.getValue());
		}
		Util.writeFile(result, "output/seperateOrNot.txt");
	}
	//试验各种正则表达式
	static void regex(){
		Pattern p = Pattern.compile("([0-9]{0,3})[:|：]([0-9]{0,3})");
		Matcher m = p.matcher("SENTENCE");
		while (m.find()) {
			int start = m.start();
			int end = m.end();
		}
		System.out.println("20世纪".matches("[A-Za-z0-9\\.\\+\\_]*[A-Za-z0-9][年|世纪|月|日|时|万|亿|%|％|℃]{0,1}[份|代]{0,1}"));
		System.out.println(Pattern.matches("([0-9]{0,3})[:|：]([0-9]{0,3})","6:00" ));
		System.out.println(Pattern.matches("([0-9]{0,3})[:|：]([0-9]{0,3})","23：56" ));
		Pattern p1 = Pattern.compile("([昼|夜|东|西|南|北|中|上|下|左|右|风|雨|雾|雪|人|][间|部|东|西|南|北]*)([长|短|高|低|多|少|大|小])([昼|夜|东|西|南|北|中|上|下|左|右|风|雨|雾|雪][部|东|西|南|北|间]*)([长|短|高|低|多|少|大|小|急])");
		System.out.println(Pattern.matches("([昼|夜|东|西|南|北|中|上|下|左|右|风|雨|雾|雪|人|][间|部|东|西|南|北]*)([长|短|高|低|多|少|大|小])([昼|夜|东|西|南|北|中|上|下|左|右|风|雨|雾|雪][部|东|西|南|北|间]*)([长|短|高|低|多|少|大|小|急])",
		"昼短夜长"));
		System.out.println(Pattern.matches("([图|表])([\\d+|甲|乙|丙|丁])([\\（|\\(][a|b|c|d|A|B|C|D][\\）|\\)])*",
				"图1" ));
	}
	//抽出四个连续单个字的分词
	static void getFour(){
		List<String> lines=Util.read_file("E://workspace/To94%/data/tag_term.txt");
		Set<String> r=new HashSet<>();
		List<String> result=new ArrayList<>();
		for(String line:lines){
			String[] tags=line.split(" ");
			for(int i=0;i<tags.length-4;i++){
				if(IsFour(tags[i], tags[i+1], tags[i+2], tags[i+3]))
					r.add(tags[i]+"  "+tags[i+1]+"  "+tags[i+2]+"  "+tags[i+3]);
			}
		}
		result.addAll(r);
		Util.writeFile(result, "output/four.txt");
	}
	static boolean IsFour(String s1,String s2,String s3,String s4){
//		if(s1.endsWith("_NN")&&s2.endsWith("_VA")&&s3.endsWith("_NN")&&s4.endsWith("_VA"))
//			return true;
//		if(s1.endsWith("_VV")&&s2.endsWith("_NN")&&s3.endsWith("_VV")&&s4.endsWith("_NN"))
//			return true;
		if(s1.endsWith("_P")&&s2.endsWith("_NN")&&s3.endsWith("_P")&&s4.endsWith("_NN"))
			return true;
		return false;
	}
	
	public static void deal_with_keneng(){
		List<String> lines=Util.read_file("E://workspace/To94%/data/tag_term.txt");
		List<String> pus=new ArrayList<>();
		pus.add("VV");pus.add("AD");pus.add("VA");pus.add("VC");pus.add("VE");pus.add("SB");pus.add("P");
		List<String> result=new ArrayList<>();
		Map<String, Integer> s=new HashMap<>();
		for(String line:lines){
			if(line.indexOf("可能")>-1){
				String[] strs=line.split(" ");
				for(int i=0;i<strs.length-1;i++){
					if(strs[i].equals("可能_VV")){
//						if(i==strs.length-1){
////							if(!s.containsKey("endof"))
////								s.put("endof", 1);
////							else {
////								s.put("endof", s.get("endof")+1);
////							}
//						}
						
//						else {
//							if(!s.containsKey(strs[i+1].split("_")[1]))
//								s.put(strs[i+1].split("_")[1], 1);
//							else {
//								s.put(strs[i+1].split("_")[1], s.get(strs[i+1].split("_")[1])+1);
//							}
						if(pus.contains(strs[i+1].split("_")[1])){
							strs[i]="可能_AD";
						}
					}
				}
				String temp="";
				for(String str:strs)
					temp+=str+" ";
				result.add(temp);
				}
			else {
				result.add(line);
			}
		}
		//sysoutmap(s);
		Util.writeFile(result, "E://workspace/To94%/data/tag_term_2.txt");
	}
	//抽出特定词性的单词
	public static void get_word_base_tag(String tag){
		Set<String> s=new HashSet<>();
		List<String> lines=Util.read_file("E://workspace/To94%/data/tag_term_2.txt");
		for(String line:lines){
			String[] strs=line.split(" ");
			for(String str:strs)
				if(str.endsWith(tag))
					s.add(str.split("_")[0]);
		}
		System.out.println(s.size());
		for(String str:s)
			System.out.println(str);
	}
	static void sysoutmap(Map<String,Integer> m){
		for(Map.Entry<String,Integer> en:m.entrySet())
			System.out.println(en.getKey()+"    "+en.getValue());
	}
}
