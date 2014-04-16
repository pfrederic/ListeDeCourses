package com.example.ldcorig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;


public class WebServiceClient extends AsyncTask<String , Void, String> {
	final InterfaceDeCallBack monInterfaceDeCallBack;
	//Ajout ICI
	private static HttpClient httpclient = new DefaultHttpClient();
	
    public WebServiceClient(InterfaceDeCallBack monInterface) {
    	//constructeur 
        monInterfaceDeCallBack= monInterface;
    }
	
	  
	  protected String doInBackground(String... params) {
	   //HttpClient httpclient = new DefaultHttpClient();
	   //params[0] contient l'adresse du webservice
	   
	   HttpPost httppost = new HttpPost(params[0]);
	   try {
		Log.i("ListeDeCourse", httpclient.toString());
	    HttpResponse response = httpclient.execute(httppost);
	    String reponse = inputStreamToString(
	      response.getEntity().getContent()).toString();
	    return reponse;
	   }
	 
	   catch (ClientProtocolException e) {
		Log.i("ListeDeCourse","Erreur http: serveur inaccessible ou adresse erronée");
	    e.printStackTrace();
	   } 
	   catch (IOException e) {
		   Log.i("ListeDeCourse","Erreur i/o: Le service met trop de temps à répondre");   
	    e.printStackTrace();
	   }
	   return null;
	  }//fin du doInBackground
	 
	  private StringBuilder inputStreamToString(InputStream is) {
	   String ligneLue = "";
	   StringBuilder reponse = new StringBuilder();
	   BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	 
	   try {
	    while ((ligneLue = rd.readLine()) != null) {
	     reponse.append(ligneLue);
	    }
	   }
	 
	   catch (IOException e) {
	    Log.i("ListeDeCourse","Erreur: Session http interrompue prématurément.");
	   }
	   return reponse;
	  }
	 
	  @Override
	  protected void onPostExecute(String resultat){
	//Quand on a fini de recevoir la chaine resultat contient la reponse du webservice il faut exécuter le code prévu
	   monInterfaceDeCallBack.receptionDonneesTerminee(resultat);
	  }
	 }// fin du  WebServiceClient