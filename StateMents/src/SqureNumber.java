import java.util.Scanner;

public class SqureNumber {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the value");
		int x= s.nextInt();
		System.out.println("Enter the value");
		int y= s.nextInt();
		
		if(x>y) {
			
			System.out.println(x+  "  is greater than " +y);
		}else {
			
			System.out.println(y+  "  is greater than " +x);
		}
		

	}

}
