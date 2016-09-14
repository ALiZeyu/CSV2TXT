package cn.edu.nju.ws.GeoScholar.templating.common;

import java.io.IOException;
import java.util.List;

import Joint.Joint;

public class Segment {

	public static Joint j = null;
	/*public static interface CLibrary extends Library {
		CLibrary Instance = (CLibrary) Native.loadLibrary(
				"data/templating/Data/NLPIR.dll", CLibrary.class);
		
		public int NLPIR_Init(String sDataPath, int encoding,
				String sLicenceCode);
				
		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
				boolean bWeightOut);
		public int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
		public int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
		public String NLPIR_GetLastErrorMsg();
		public int NLPIR_SaveTheUsrDic();
		public void NLPIR_Exit();
		public boolean NLPIR_NWI_Start();
		public int NLPIR_NWI_AddFile(String sFilename);
		public boolean NLPIR_NWI_Complete();
		public String NLPIR_NWI_GetResult(boolean bWeightOut);
		public int NLPIR_NWI_Result2UserDict();
	}*/
	
	public static void init() throws IOException {
		/*String argu = "./data/templating";
		if (CLibrary.Instance.NLPIR_Init(argu,1,"0") == 0)	{
			String nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败！fail reason is " + nativeBytes);
			return;
		}
		String s;
		BufferedReader br0 = new BufferedReader(new InputStreamReader(new FileInputStream("data/templating/term.txt")));
		while ((s = br0.readLine()) != null) {
			 CLibrary.Instance.NLPIR_AddUserWord(s);
		}
		br0.close();*/
		j=new Joint("./data/templating/Data/");
		j.loadModel();
	}
	public static void segmentQuestion(String question) throws IOException {
		List<String> l = j.seg_postag(question);
		//l.set(l.indexOf("的_DEG"), "的_DEC");
		if (l.contains("也_AD"))
			l.remove("也_AD");
		if (l.size() > 1 && l.get(0).startsWith("图") && l.get(1).startsWith("中")) {
			l.remove(1);
			l.remove(0);
			if (l.get(0).startsWith("为"))
				l.remove(0);
			if (l.get(0).startsWith("所示"))
				l.remove(0);
			if (l.get(0).startsWith("，"))
				l.remove(0);
		}
		//将风小雾大等分开的四个字合在一起
		for (int i = 0; i < l.size() - 3; i++) {
			if (l.get(i).endsWith("NN") && l.get(i + 1).endsWith("VA") && l.get(i + 2).endsWith("NN") && l.get(i + 3).endsWith("VA")) {
				String s = "";
				for (int j = i; j < i + 4; j++)
					s += l.get(j).split("_")[0];
				s += "_VA";
				for (int j = i + 3; j > i; j--)
					l.remove(j);
				l.set(i, s);
			}
		}
		System.out.println(l);
		Excute.excute(l);
	}
	
	public static void segment(String question) throws IOException {
		List<String> l = j.seg_postag(question);
		Excute.excute(l);
	}

}
