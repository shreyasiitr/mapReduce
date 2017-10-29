

import java.io.IOException;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String arg[] = {"random.txt"};
		try {
			WordCount w = new WordCount(1,arg);
			w.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
