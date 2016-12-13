package Default;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	/**本类是针对所有的试题进行正则表达式测试*/
	public static void main(String[] args) {
		List<String> all = Util.read_file("data/所有试题");
		List<String> result = new ArrayList<>();
		/**
		 * "([图|表])([\\d|甲|乙|丙|丁])+([\\（|\\(][a|b|c|d|A|B|C|D][\\）|\\)])*",
					"(\\d+)°[ewsnEWSN]?((\\d+)′[ewsnEWSN]?)?",
					"[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]]{1,2}(类|图|国|河流|河|市|县|镇|村|点|线|地|区域|区|阶段)",
					"[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]][城省处]",
					"(洋流|气流|环节)[甲|乙|丙|丁|戊|①|②|③|④|⑤|Ⅰ|Ⅱ|Ⅲ|Ⅳ|Ⅴ|[A-Z]|[a-z]]",
					"([0|1|2|3|4|5|6|7|8|9|０|１|２|３|４|５|６|７|８|９|一|二|三|四|五|六|七|八|九|零|十|百|千|万|亿|〇|两]{0,3})[:|：]([0|1|2|3|4|5|6|7|8|9|０|１|２|３|４|５|６|７|８|９|一|二|三|四|五|六|七|八|九|零|十|百|千|万|亿|〇|两]{0,3})"
					"(\\d|\\.)+°[ewsnEWSN]?((\\d|\\.)+′[ewsnEWSN]?)?""1[:|︰|：]\\d{1,10}"
					*/
		String[] pattern_str = { 
				"[A-Za-z0-9\\.\\+\\_\\/]*[A-Za-z0-9](分钟|分|秒|世纪|年代|年|月份|月|日|时刻|时|万|亿|%|％|℃){0,1}"
					};
		for(String str : all){
			for (int j = 0; j < pattern_str.length; j++) {
				Pattern p = Pattern.compile(pattern_str[j]);
				Matcher m = p.matcher(str);
				while (m.find()) {
					int start = m.start();
					int end = m.end();
					result.add(str.substring(start, end));
				}
			}
		}
		System.out.println(result);
//		Iterator<String> it=result.iterator();
//		while(it.hasNext()){
//			String s=it.next();
//			if(hasDigitLetter(s))
//				System.out.println(s);
//		}
		//listPrint(result);

	}
	
	@Deprecated
	public static void getAllShiti(String path){
		List<String> result = Util.getAllFileContentList(path);
		Util.writeFile(result, "data/所有试题");
	}
	
	public static void listPrint(List<String> result){
		for(String str : result)
			System.out.println(str);
	}
	
	public static boolean hasDigitLetter(String str){
		boolean digit=false,letter=false;
		for(int i=0;i<str.length();i++){
			if((str.charAt(i)>='a'&&str.charAt(i)<='z')||(str.charAt(i)>='A'&&str.charAt(i)<='Z'))
				letter=true;
			else if (Character.isDigit(str.charAt(i))) {
				digit=true;
			}
		}
		return digit&letter;
	}

}
