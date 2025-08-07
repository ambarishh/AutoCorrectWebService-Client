package com.cdyne.ws;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.cdyne.ws.CheckStub.CheckTextBody;
import com.cdyne.ws.CheckStub.CheckTextBodyResponse;
import com.cdyne.ws.CheckStub.Words;

/**
 * The SpellChecker class provides functionality to check and correct spelling in a given text.
 * It uses a web service to identify misspelled words and find suggestions for corrections.
 */
public class SpellChecker {

	/**
	 * The main method is the entry point of the program.
	 * It is currently empty and does not perform any operations.
	 * @param args Command line arguments (not used).
	 */
	public static void main(String[] args) {
	
	}

	/**
	 * Corrects the spelling of the given text by replacing misspelled words with the first suggestion from a web service.
	 * @param text The text to be spell-checked and corrected.
	 * @return The corrected text.
	 * @throws AxisFault if an error occurs during the web service call.
	 */
	public static String getCorrect(String text) throws AxisFault{
		CheckStub stub=null;
		CheckTextBody tb =null;
		CheckTextBodyResponse resp=null;
		String body=null;

		try {
		
			stub = new CheckStub();
			tb = new CheckTextBody();
			tb.setBodyText(text);

			resp = stub.checkTextBody(tb);
			body = resp.getDocumentSummary().getBody();
			Words [] misSpelledWords = resp.getDocumentSummary().getMisspelledWord();
			for(int i=0; i<misSpelledWords.length;i++){
				String [] sug = misSpelledWords[i].getSuggestions();
				body = body.replace(misSpelledWords[i].getWord(), sug[0]);
				//System.out.println(misSpelledWords[i].getWord() + ": "+sug[0]);
			}
			return body;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		finally{
			stub.cleanup();
			tb=null;
			resp=null;
			stub=null;
			body=null;
		}
		return null;
	}
}
