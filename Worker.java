
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Worker {
	
	public void Worker() {
	}

	public static void main(String[] args) {
		//System.out.println(args[0]);
		Socket client=null;
		try {
			client = new Socket(InetAddress.getLocalHost(), Integer.parseInt(args[0]));
			
			BufferedReader dis= new BufferedReader(new InputStreamReader(client.getInputStream()));
			String fileString = dis.readLine().trim();
			String[] files= fileString.split("\\s+");
			String key = "output_"+files[files.length-1]+".txt";
			File file = new File(key);
			//System.out.println("worker start");
			String output="";
			for(int i=0; i<files.length-1;i++) {
				BufferedReader reader = new BufferedReader(new FileReader(files[i]));
				String line = null;
				ArrayList<String> a = new ArrayList<String>();
				try {
					line=reader.readLine();
					while(line!=null) {
						String[] s= line.trim().split("\\s+");
						for(int j = 0; j<s.length; j++) {
							a.add(s[j]);
						}
						try {
							line=reader.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					FileWriter f0 = new FileWriter(file,true);

					for(int j=0;j<a.size();j++)
					{
						f0.write(a.get(j));
						f0.write('\n');
					}
					f0.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//output = .fileParse(files[i],file);
			}
			//System.out.println("worker done");
			
			BufferedWriter dos = new BufferedWriter(
	                new OutputStreamWriter(client.getOutputStream()));
			dos.write(key);
			dos.newLine(); //HERE!!!!!!
			dos.flush();
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		// TODO Auto-generated method stub

	}
	public String fileParse(String filename, File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			ArrayList<String> a = new ArrayList<String>();
			try {
				line=reader.readLine();
				while(line!=null) {
					String[] s= line.trim().split("\\s+");
					for(int i = 0; i<s.length; i++) {
						a.add(s[i]);
					}
					try {
						line=reader.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileWriter f0 = new FileWriter(file,true);

				for(int i=0;i<a.size();i++)
				{
					f0.write(a.get(i));
				    //f0.println(a.get(i));
					f0.write('\n');
				}
				f0.close();
				return file.getAbsolutePath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
