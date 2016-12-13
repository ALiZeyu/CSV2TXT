import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	public static void main(String[] args){
		new Main().run();
	}
	
	public void run(){
//		Scanner fin=new Scanner(System.in);
//		int n=fin.nextInt();
//		int m=fin.nextInt();
////		//fin.nextLine();
////		int[][] edges=new int[n][n];
////		for(int i=0;i<n;i++){
////			int a=fin.nextInt();
////			int b=fin.nextInt();
////			edges[a][b]=1;
////		}
//		String[] array = new String[n];
//		fin.nextLine();
//		for(int i=0;i<n;i++)
//			array[i]=fin.nextLine();
//		Map<String, String> token=new HashMap<>();
//		for(int i=0;i<m;i++){
//			String str=fin.nextLine();
//			String[] t = str.split(" ",2);
//			token.put(t[0], t[1].substring(1, t[1].length()-1));
//		}
//		for(int i=0;i<n;i++){
//			String str = array[i];
//			if(!str.contains("{{ ") || !str.contains(" }}"))
//				continue;
//			for(Map.Entry<String, String> entry : token.entrySet()){
//				String p= "\\{\\{ "+entry.getKey()+" \\}\\}";
//				String r=entry.getValue();
//				str=str.replaceAll(p, r);
//			}
//			if(str.contains("{{ ") && str.contains(" }}"))
//				str=str.replaceAll("\\{\\{ .* \\}\\}", "");
//			array[i]=str;
//		}
//		for(String str:array)
//			System.out.println(str);
		
//		Scanner fin = new Scanner(System.in);
//		int m=fin.nextInt(),n=fin.nextInt();
//		int[][] array=new int[m][n];
//		int[][] result=new int[n][m];
//		
//		for(int i=0;i<m;i++)
//			for(int j=0;j<n;j++)
//				array[i][j]=fin.nextInt();
//		int r=0;
//		for(int i=n-1;i>=0;i--){
//			int c=0;
//			for(int j=0;j<m;j++){
//				result[r][c]=array[j][i];
//				c++;
//			}
//			r++;
//		}
//		for(int i=0;i<n;i++){
//			String s="";
//			for(int j=0;j<m;j++)
//				s+=result[i][j]+" ";
//			System.out.println(s.trim());
//		}
	
//		Scanner fin = new Scanner(System.in);
//		int n=fin.nextInt();
//		int[] array = new int[n];
//		Map<Integer, Integer> map = new HashMap<>();
//		for(int i=0;i<n;i++) array[i] = fin.nextInt();
//		String s="";
//		for(int i=0;i<n;i++){
//			if(map.containsKey(array[i])){
//				map.put(array[i], map.get(array[i])+1);
//				s+=map.get(array[i])+" ";
//			}else {
//				map.put(array[i], 1);
//				s+=map.get(array[i])+" ";
//			}
//		}
//		System.out.println(s.trim());
		
//		Scanner fin = new Scanner(System.in);
//		int n=fin.nextInt();
//		int[][] a = new int[n][n];
//		for(int i=0;i<n;i++)
//			for(int j=0;j<n;j++)
//				a[i][j]=fin.nextInt();
//		String s="";
//		int num=0,total=n*n;
//		int i=0,j=0;
//		boolean up=true;
//		while(num<=total){
//			s+=a[i][j];
//			num++;
//		}
//		Scanner fin = new Scanner(System.in);
//		int n=fin.nextInt();
//		Map<Integer, Integer> map = new HashMap<>();
//		for(int i=0;i<n;i++){
//			int in=fin.nextInt();
//			if(map.containsKey(in)){
//				map.put(in, map.get(in)+1);
//			}else
//				map.put(in, 1);
//		}
//		List<Map.Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
//		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
//			@Override
//			public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
//				if(o1.getValue()<o2.getValue())
//					return 1;
//				else if (o1.getValue()==o2.getValue()) {
//					if(o1.getKey()>o2.getKey())
//						return 1;
//					else
//						return -1;
//				}
//				return -1;
//			}
//			
//		});
//		for(Map.Entry<Integer, Integer> m:list)
//			System.out.println(m.getKey()+" "+m.getValue());
		
//		Scanner fin = new Scanner(System.in);
//		final int right=1,down=2,leftdown=3,rightup=4;
//		int n=fin.nextInt();
//		int[][] a = new int[n][n];
//		for(int i=0;i<n;i++)
//			for(int j=0;j<n;j++)
//				a[i][j]=fin.nextInt();
//		int direction=0;
//		int r=0,c=0;
//		String s=a[r][c]+" ";
//		//System.out.print(a[r][c]);
//		while(!(r==n-1 && c==n-1)){
//			if(direction==0)
//				direction=right;
//			else if(direction==right) {
//				if(r+1<n && c-1>=0)
//					direction=leftdown;
//				else
//					direction=rightup;
//			}else if(direction==down){
//				if(r-1>=0 && c+1<n)
//					direction=rightup;
//				else
//					direction=leftdown;
//			}else if (direction==leftdown) {
//				if(r+1<n && c-1>=0)
//					direction=leftdown;
//				else if(r+1<n)
//					direction=down;
//				else
//					direction=right;
//			}else {
//				if(r-1>=0 && c+1<n)
//					direction=rightup;
//				else if(c+1<n)
//					direction=right;
//				else
//					direction=down;
//			}
//			switch (direction) {
//			case right:
//				c++;
//				break;
//			case down:
//				r++;
//				break;
//			case leftdown:
//				r++;c--;
//				break;
//			default:
//				c++;r--;
//				break;
//			}
//			//System.out.print(a[r][c]);
//			s+=a[r][c]+" ";
//		}
//		System.out.println(s.trim());
//		Scanner fin=new Scanner(System.in);
//		int n=fin.nextInt();
//		int[][] a=new int[100][100];
//		int sum=0;
//		for(int k=0;k<n;k++){
//			int x1=fin.nextInt(),y1=fin.nextInt(),x2=fin.nextInt(),y2=fin.nextInt();
//			for(int i=y1;i<y2;i++)
//				for(int j=x1;j<x2;j++){
//					if(a[i][j]!=1){
//						sum++;
//						a[i][j]=1;
//					}
//				}
//		}
//		System.out.println(sum);
		
		Scanner fin=new Scanner(System.in);
		int n=fin.nextInt(),m=fin.nextInt();
		int[][] edges=new int[n+1][n+1];
		for(int i=1;i<=n;i++)
			for(int j=1;j<=n;j++)
				edges[i][j]=-1;
		for(int i=0;i<n;i++){
			int b=fin.nextInt(),e=fin.nextInt(),v=fin.nextInt();
			edges[b][e]=v;
			edges[e][b]=v;
		}
		int[] lowcost=new int[n+1];
		for(int i=1;i<=n;i++) lowcost[i]=-1;
		boolean[] visited=new boolean[n+1];
		visited[1]=true;
		for(int i=2;i<=n;i++){
			lowcost[i]=edges[i][1];
			visited[i]=false;
		}
		int sum=0;
		for(int i=2;i<=n;i++){
			int min=Integer.MAX_VALUE;
			int node=0;
			for(int j=2;j<=n;j++){
				if(visited[j]==false && lowcost[j]!=-1 && lowcost[j]<min){
					min=lowcost[j];
					node=j;
				}
			}
			visited[node]=true;
			sum+=min;
			for(int j=2;j<=n;j++){
				if(visited[j]==false && edges[j][node]>-1 && (lowcost[j]==-1 || lowcost[j]>edges[j][node])){
					lowcost[j]=edges[j][node];
				}
			}	
		}
		System.out.println(sum);
	}
}

