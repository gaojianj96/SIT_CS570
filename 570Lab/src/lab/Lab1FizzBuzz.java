package lab;

public class Lab1FizzBuzz {

	public static void main(String[] args) {
		for(int num=74;num<=291;num++){
			processNum(num);
		}
	}

	public static void processNum(int num){
		if(num<74||num>291){
			System.out.println("Error");
		}
		if(num%3==0){
			if(num%5==0){
				System.out.println("BuzzFizz");
			}
			else{
				System.out.println("Buzz");
			}
		}
		else if(num%5==0){
			System.out.println("Fizz");
		}
		else
			System.out.println(num);
	}
	
}
