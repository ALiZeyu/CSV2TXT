package cn.edu.nju.ws.GeoScholar.templating.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MyUtil {

	public static List<String> readListFromFile(String path){
		List<String> result=new ArrayList<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf8"));
			String line=br.readLine();
			if(line.startsWith("\uFEFF"))
				line=line.substring(1);
			while(line!=null){
				if(line.trim().length()<2){
					line=br.readLine();
					continue;
				}else {
					result.add(line.trim());
				line=br.readLine();
				}
				
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
