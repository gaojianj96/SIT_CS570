package lab;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lab2 {

	public static void main(String[] args) {
		System.out.print("Name of file?: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bFileReader;
		BufferedWriter bWriter;
		try {
			String fileName = bReader.readLine().trim();
			if(fileName.length()<1){
				System.out.println("File Name Not Entered! Program Exit");
				System.exit(0);
			}
			bFileReader = new BufferedReader(new FileReader(fileName));
			String writerCode = "";
			String fileData;
			while ((fileData = bFileReader.readLine()) != null) {
				writerCode += decipher(fileData);
			}
			bWriter = new BufferedWriter(new FileWriter("solution.txt"));
			bWriter.write(writerCode);
			bWriter.flush();
			bWriter.close();

		} catch (FileNotFoundException e) {
			System.out.println("No such file! Program exit!");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Read file Error! Program exit!");
			System.exit(0);
		}
	}

	public static String decipher(String text) {
		String decipher = "";
		int stat = 0, key = 5;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (!Character.isLetter(c)) {
				decipher += c;
				stat++;
				continue;
			}
			while (stat >= 3) {// increases by 2 after every 3
				key += 2;
				stat -= 3;
				if (key > 26)
					key -= 26;
			}
			char c_new = (char) (c - key);// decipher
			if (Character.isUpperCase(c) && !Character.isUpperCase(c_new)) {
				c_new = (char) (c_new + 26);
			} else if (Character.isLowerCase(c) && !Character.isLowerCase(c_new)) {
				c_new = (char) (c_new + 26);
			}
			decipher += c_new;
			stat++;
		}

		return decipher;
	}

}
