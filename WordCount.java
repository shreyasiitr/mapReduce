

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Comparator;

import javax.swing.text.html.HTMLDocument.Iterator;


public class WordCount implements Master {
	
	private Map<Integer,ArrayList<String>> m = new HashMap<Integer,ArrayList<String>>();
	private Map<String,Integer> res = new HashMap<String,Integer>();
	private ArrayList<Integer> a = new ArrayList<Integer>();
	private ArrayList<String> files = new ArrayList<String>();
	private PrintStream ps = null;
	private int sum = 0;
	private int counter = 0;
	private int port = 0;
	private Map<Integer,Integer> ports = new HashMap<Integer,Integer>();
	
    public WordCount(int workerNum, String[] filenames) throws IOException {
    	
    		for(int i = 0; i<filenames.length; i++) {
				int key = i%workerNum;
				if(m.containsKey(key)) {
					ArrayList<String> temp = new ArrayList<String>();
					temp = m.get(key);
					temp.add(filenames[i]);
					m.put(key, temp);
				}
				else {
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(filenames[i]);
					m.put(key, temp);
				}
			}
			for(int i = 0; i<workerNum; i++) {
				a.add(0);
			}
    		
    }

    public void setOutputStream(PrintStream out) {
    		ps = out;
    		//System.setOut(ps);
    }

    public static void main(String[] args) throws Exception {
    	    	
    }

    public void run() {
    	
    			ServerSocket serverSocket=null;
			try {
				serverSocket = new ServerSocket(0);
				//ports.put(9000, 1);
			} catch (IOException e1) {
				//System.out.println("stack trace 1");
				//e1.printStackTrace();
			}
			port = serverSocket.getLocalPort();
			Socket clientSocket=null;
			int index = 0;
			while(index<a.size()) {
				
				//System.out.println("index : "+index);
				try {
					createWorker();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			new Thread(new Runnable() {
				private Socket client = null;
				private ServerSocket server = null;
    				public Runnable init(Socket clientSocket, ServerSocket serverSocket) {
			        this.client = clientSocket;
			        this.server = serverSocket;
			        return this;
			    }
	    			@Override
	    			public void run() {
	    				try {	synchronized(this) {
	    						while(sum<a.size() && server!=null&& !server.isClosed()) {
	    							client = server.accept();
	    							//System.out.println("connection");
	    							int temp = counter;
	    							counter++;
	    							ArrayList<String> arg;
	    							if(m.containsKey(temp)) {
	    								arg = m.get(temp);
	    							}
	    							else {
	    								arg=new ArrayList<>();
	    							}
	    							String fileString = "";
									for(int i =0; i<arg.size();i++) {
										fileString += arg.get(i) + " ";
									}
									fileString += temp;
									//System.out.println(fileString);
									BufferedWriter dos;
									dos = new BufferedWriter(
									        new OutputStreamWriter(client.getOutputStream()));
									
									dos.write(fileString);
									dos.newLine(); //HERE!!!!!!
	    			    					dos.flush();
	    			    					BufferedReader dis= new BufferedReader(new InputStreamReader(client.getInputStream()));
	    			    					files.add(dis.readLine());
	    			    					sum++;
//	    							new Thread(new Runnable() {
//	    								private ArrayList<String> list = new ArrayList<String>();
//	    								private Integer key = 0;
//										@Override
//										public void run() {
//											
//											BufferedWriter dos;
//											try {
//												String fileString = "";
//												for(int i =0; i<list.size();i++) {
//													fileString += list.get(i) + " ";
//												}
//												fileString += key;
//												System.out.println(fileString);
//												dos = new BufferedWriter(
//												        new OutputStreamWriter(client.getOutputStream()));
//												
//												dos.write(fileString);
//												dos.newLine(); //HERE!!!!!!
//				    			    					dos.flush();
//				    			    					BufferedReader dis= new BufferedReader(new InputStreamReader(client.getInputStream()));
//				    			    					files.add(dis.readLine());
//				    			    					sum++;
//				    			    					
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//					    			    			
//										}
//
//										public Runnable init(ArrayList<String> param, Integer param2) {
//											this.list = param;
//											this.key = param2;
//											return this;
//										}
//	    								
//	    							}.init(arg,temp)).start();
	    							
	    						}
	    						client.close();
	    				}
						} catch (Exception e) {
							//System.out.println("stack trace 1");
							// TODO Auto-generated catch block
							//System.out.println(e.getMessage());
							e.printStackTrace();
							
						}
	    			}
	    		}.init(clientSocket,serverSocket)).start();
			
//			new Thread(new Runnable() {
//				
//	    			public void run() {
//	    				try {
//	    					int index = 0;
//	    					while(index<a.size()) {
//	    						
//	    						System.out.println("index : "+index);
//	    						createWorker();
//	    						index++;
//	    					}
//	    						
//	    				} catch (IOException e) {
//	    					System.out.println("stack trace 2");
//	    					e.printStackTrace();
//	    				}
//	    				
//	    			}
//	    		}).start();
			
			new Thread(new Runnable() {
				private ServerSocket server = null;
	    			public void run() {
	    				int index = 0;
	    				File space = new File("space.txt");
	    				FileWriter f=null;
	    				try {
							f = new FileWriter(space);
							while(sum<a.size()) {
			    					f.write("");
		    						//System.out.println("sum");
		    					}
							BufferedReader readerSpace = new BufferedReader(new FileReader("space.txt"));
							//readerSpace.close();
						} catch (IOException e1) {
							//System.out.println("stack trace 3");
							e1.printStackTrace();
						}
	    				
	    				for(int i = 0; i<files.size(); i++) {
	    					try {
								BufferedReader reader = new BufferedReader(new FileReader(files.get(i)));
								String line = null;
								try {
									line = reader.readLine();
									while(line!=null) {
										line = line.trim();
										if(res.containsKey(line)) {
											int t = res.get(line);
											t++;
											res.put(line, t);
										}
										else {
											res.put(line, 1);
										}
										line=reader.readLine();
									}
									reader.close();
								} catch (IOException e) {
									//System.out.println("stack trace 4");
									e.printStackTrace();
								}
								
							} catch (FileNotFoundException e) {
								//System.out.println("stack trace 5");
								e.printStackTrace();
							}
	    					
	    				}
	    				
	    				try {
	    						File file = new File("outputEnd.txt");
							FileWriter filewriter = new FileWriter(file);
							Object[] as = res.entrySet().toArray();
							Arrays.sort(as, new Comparator() {
							    public int compare(Object obj1, Object obj2) {
							    	if(((Map.Entry<String, Integer>) obj2).getValue().compareTo(
							               ((Map.Entry<String, Integer>) obj1).getValue())==0) {
							    		return ((Map.Entry<String, Integer>) obj1).getKey().compareTo(
									               ((Map.Entry<String, Integer>) obj2).getKey());
							    	}
							        return ((Map.Entry<String, Integer>) obj2).getValue().compareTo(
							               ((Map.Entry<String, Integer>) obj1).getValue());
							    }
							});       
							for (Object e : as) {
			    				    String p = ((Map.Entry<String, Integer>) e).getValue() + " : "
						                     + ((Map.Entry<String, Integer>) e).getKey();
			    				    if(!((Map.Entry<String, Integer>) e).getKey().trim().isEmpty()) {
			    				    		p+='\n';
				    				    filewriter.write(p);
				    				    ps.write(p.getBytes());
			    				    	}
			    				    //System.out.println(p);
			    				}
							filewriter.close();
							server.close();
							
						} catch (IOException e) {
							//System.out.println("stack trace 6");
							e.printStackTrace();
						}
	    			}

					public Runnable init(ServerSocket serverSocket) {
						server = serverSocket;
						return this;
					}
	    		}.init(serverSocket)).start();		
		
    }

    public Collection<Process> getActiveProcess() {
        return new LinkedList<>();
    }

    public void createWorker() throws IOException {
	
	    ProcessBuilder builder = new ProcessBuilder("java","-cp","bin","Worker",""+port);
	    builder.redirectOutput(new File("a.txt"));
	    Process process = builder.start();
	    
    }
}

