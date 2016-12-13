package Default;
import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		new Main().run();
	}
	
	public void run(){
		//ISBN
//		Scanner fin = new Scanner(System.in);
//		String str=fin.nextLine();
//		String num=str.replace("-", "");
//		int sum=0;
//		for(int i=0;i<9;i++){
//			sum+=(i+1)*(num.charAt(i)-'0');
//		}
//		String s=String.valueOf(num.charAt(9));
//		String mod=sum%11==10?"X":String.valueOf(sum%11);
//		if(s.equals(mod))
//			System.out.println("Right");
//		else {
//			System.out.println(str.substring(0, str.length()-1)+mod);
//		}
		//max upside down
//		Scanner fin = new Scanner(System.in);
//		int n=fin.nextInt();
//		int max=-1;
//		int[] array=new int[n];
//		for(int i=0;i<n;i++){
//			array[i]=fin.nextInt();
//			if(i>0){
//				max=Math.abs(array[i]-array[i-1])>max?Math.abs(array[i]-array[i-1]):max;
//			}
//		}
//		System.out.println(max);
		//火车座位安排
//		Scanner fin = new Scanner(System.in);
//		int[][] array=new int[20][5];
//		int n=fin.nextInt();
//		for(int i=0;i<n;i++){
//			int num
//		}
		//出现次数最多的数
		Scanner fin=new Scanner(System.in);
		int[] num=new int[10001];
		int n=fin.nextInt();
		int max=-1,min_num=10000;
		for(int i=0;i<n;i++){
			int t=fin.nextInt();
			num[t]=num[t]+1;
			if(max<num[t]){
//				if(max==num[t] && min_num>t)
//					min_num=t;
				max=num[t];
			}
		}
		for(int i=1;i<=10000;i++)
			if(num[i]==max){
				System.out.println(i);
				break;
			}
	}
	
	public int empty(int[] a,int num){
		int f=-1;
		return f;
	}
}
