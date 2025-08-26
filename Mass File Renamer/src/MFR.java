import java.io.File;
import java.io.IOException;

public class MFR {

	public static void main(String[] args) throws IOException {
		String name = "Test.String";
		System.out.println(" ");
		String[] arr = name.split("\\.");
		for(int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
	}

}
