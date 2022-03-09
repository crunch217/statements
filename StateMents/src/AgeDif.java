import java.util.Scanner;

public class AgeDif {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("enter age");
		 int x = sc.nextInt();
		 
		 if(x<10) {
			  System.out.println("I am child");
		 }else if ((x<=30)&&(x>=20)){
			 System.out.println("I am Young");
		 }else if ((x<=100)&&(x>=50)){
			 System.out.println("I am old");

		 }else {
			 
			 System.out.println("age is out of range");
		 }
	}

}
