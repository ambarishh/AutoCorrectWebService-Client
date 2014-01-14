package com.cdyne.ws;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import com.cdyne.ws.CheckStub.CheckTextBody;
import com.cdyne.ws.CheckStub.CheckTextBodyResponse;
import com.cdyne.ws.CheckStub.Words;

public class SpellChecker {

	public static void main(String[] args) {
	
	}
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
