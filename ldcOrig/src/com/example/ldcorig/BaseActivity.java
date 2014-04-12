package com.example.ldcorig;
import com.example.listedecourse.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class BaseActivity extends Activity {
	
	
//quelques propri√©t√©s de la classe:
     //adresse du web service qui fournit les donn√©es
	 protected String url;
	 protected String baseUrl="http://172.16.63.142/coursesOrig/";
	 //protected String baseUrl="http://192.168.1.23/coursesOrig/";
	 //accesseur √† impl√©menter dans chaque classe descendante
	 /* Retourne l'url complËte sans les variables $_GET (le chemin de la page)
	  */
	  abstract String url();
	 //code s'ex√©cutant √† la cr√©ation de l'activit√© (constructeur) 
	protected void onCreate(Bundle savedInstanceState) {
		//appel du constructeur h√©rit√© de la classe Activity
		/*
		 * S'ÈxÈcture automatiquement ‡ l'appel des activitÈs. GÈnÈralement, affiche l'interface et lie les variables avec les items des interfaces.
		 * @param Bundle
		 */
		super.onCreate(savedInstanceState);
		//demande asynchrone de donn√©es par le web
		accessWebService(url());		
	}
	
//chargement des actions du menu
	public boolean onCreateOptionsMenu(Menu monMenu) {
	    // On va charger dans la barre d'action les elements du menu d√©finis dans res/menu/main.xml
	    MenuInflater monInflater = getMenuInflater();
	    monInflater.inflate(R.menu.main, monMenu);
	    //appel de la m√™me m√©thode h√©rit√©e
	    return super.onCreateOptionsMenu(monMenu);
	}
	
//gestion du clic sur les diff√©rentes actions
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId())
		{
		    //faire les courses
			case R.id.action_courses: Intent i = new Intent(this,FaireCourseActivity.class );startActivity(i);finish();break;
			//ranger les rayons dans le magasin
			case R.id.action_magasin: Intent i1 = new Intent(this,MagasinActivity.class );startActivity(i1);finish();break;			
			//remplir la liste de course
			case R.id.action_liste: Intent i2 = new Intent(this,RemplirListe.class );startActivity(i2);finish();break;	
			/*default:
				return super.onOptionsItemSelected(item);*/
		}		
		return true;
	}
	

	// T√¢che Asynchrone qui demande des donn√©es √† un service web √©crit en php

	 
	 public void accessWebService(String adresse) {
	  //instanciation de la classe WebServiceClient
	  WebServiceClient monWebService = new WebServiceClient(new InterfaceDeCallBack() {

          @Override
          public void receptionDonneesTerminee(String result) {
        	  traiterDonneesRecues(result);
          }
      });
	  // on envoie l'adresse du webService √† atteindre
	  monWebService.execute(new String[] { adresse });
	 }
    //Ici on ne sait pas quoi faire des donn√©es mais dans les classes descendantes on le saura
	 /*
	  * Fonction abstraite, traitant les donnÈes de la base de donnÈes renvoyÈes en Json. Les dÈcortique et les gËre.
	  * @param String DonnÈes de la base, au format Json.
	  */
	abstract void  traiterDonneesRecues(String result);
	 
}//fin de la classe BaseActivity
