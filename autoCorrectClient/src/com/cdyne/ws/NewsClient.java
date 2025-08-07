package com.cdyne.ws;

import java.rmi.RemoteException;

import com.cdyne.ws.SpellCheckerStub.GetCorrect;
import com.cdyne.ws.SpellCheckerStub.GetCorrectResponse;
import java.io.*;

import org.apache.axis2.AxisFault;
import java.util.*;

/**
 * The NewsClient class is the main entry point for the spell-checking application.
 * It reads news articles from a text file, sends the text to a spell-checking web service,
 * and writes the corrected articles to a new file.
 */
public class NewsClient {
	static List<String>date;
	static List<String> title;
	static List<String> text;
	static int count;

	static{
		date = new ArrayList<String>();
		title = new ArrayList<String>();
		text = new ArrayList<String>();
	}
	
	/**
	 * The main method orchestrates the entire process of reading, correcting, and writing news articles.
	 * @param args Command line arguments (not used).
	 * @throws IOException if an I/O error occurs.
	 */
	public static void main(String[] args) throws IOException {
		 System.out.println("Starting...");
		 readNews();
		 getCorrectText();
		 String finalText = join();
		 writeNews(finalText);
		 System.out.println("Spell Correction completed.");
		 System.gc();
		}
	
	/**
	 * Reads news articles from the "news.txt" file, splitting each line into date, title, and text.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void readNews() throws IOException {
		File file = new File("src/news.txt");
		String currentLine = "";
		BufferedReader newsReader = new BufferedReader(new FileReader(file));
	//	System.out.println("Before Correction-");
		while (newsReader.ready()){
			currentLine = newsReader.readLine();
			//System.out.println(currentLine);
			splitDocument(currentLine);
		}
	//	System.out.println("-------------------------------------------------------------------");
		newsReader.close();
	}
	
	/**
	 * Splits a line of text into date, title, and article text, and stores them in respective lists.
	 * @param currentLine The line of text to be split.
	 * @throws AxisFault if an error occurs.
	 */
	private static void splitDocument(String currentLine) throws AxisFault {
		String[] docSplitArray = currentLine.split("\t"); // Split
		date.add(docSplitArray[0]);
		title.add(docSplitArray[1]);
		text.add(docSplitArray[2]);
	}

	/**
	 * Collects all article texts, sends them for spell correction, and updates the text list with corrected versions.
	 * @throws AxisFault if an error occurs during the web service call.
	 */
	public static void getCorrectText() throws AxisFault{
		StringBuilder corpus = new StringBuilder();
		Iterator<String> it = text.iterator();
		while(it.hasNext())
			corpus.append(it.next() + "\t");
		String correctText = corrected(corpus.toString());
		String [] splitText = correctText.split("\t");
		text =  Arrays.asList(splitText);
	}

	/**
	 * Joins the dates, titles, and corrected texts back into a single string, with each article on a new line.
	 * @return A string containing all the corrected news articles.
	 * @throws IOException if an I/O error occurs.
	 */
	public static String join() throws IOException{
		StringBuilder right = new StringBuilder();
		for(int i =0; i< date.size();i++){
			right.append(date.get(i) + "\t" + title.get(i) + "\t"+ text.get(i) + "\n");
		}
		return right.toString();
		
	}

	/**
	 * Sends a string of text to the spell-checking web service and returns the corrected version.
	 * @param text The text to be corrected.
	 * @return The corrected text.
	 * @throws AxisFault if an error occurs during the web service call.
	 */
	public static String corrected(String text) throws AxisFault{
		SpellCheckerStub stub=null;
		GetCorrect gc = null;
		GetCorrectResponse resp = null;
		String b = null;
		try {
			stub = new SpellCheckerStub();
			gc = new GetCorrect();
			gc.setText(text);
			resp = stub.getCorrect(gc);
			b = resp.get_return();
			return b;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		finally{
			stub.cleanup();
			stub=null;
			gc =null;
			resp=null;
			b=null;
		}
		return null;
	}

	/**
	 * Writes the corrected news articles to the "Corrected News.txt" file.
	 * @param text The string containing the corrected news articles.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void writeNews(String text) throws IOException
	{	
//		System.out.println("After Correction-");
//		System.out.println(text);
		BufferedWriter bw = new BufferedWriter(new FileWriter("Corrected News.txt"));
		bw.write(text);
		bw.flush();
		bw.close();
	}
}
