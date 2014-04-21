package com.example.ldcorig;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.example.ldcorig.R;

public class FaireCourseActivity extends BaseActivity {
	
//quelques propriétés de la classe:

	// propriétés
	/*
	 * Complément de l'url complète, indiquant la page qu'il faut appeler
	 */
	 private String url = "faireCourse.php";
	 /*
	  * Adapteur de la listExpandable, contenant des ensembleRayons
	  */
	 AdapterExpandableRayonProduit monAdapteur;
	 /*
	  * Propriété contenant les informations sur les rayons et leurs articles
	  */
	 EnsembleRayons ensRayons;
	 /*
	  * La listExpandable du xml, permettant d'ajouter des données
	  */
	 private ExpandableListView listeViewDesProduitsDeLaListeParRayons;
	 /*
	  * Bouton annuler, qui retire de la liste les articles cochés
	  */
	 private Button annulerAchat;
	 /*
	  * Bouton caddy, qui confirme que les produits cochés se trouve dans le caddy
	  */
	 private Button caddyAchat;
	 /*
	  * Bouton reporter, qui reporte les produits cochés sur la liste de course suivante
	  */
	 private Button reporterAchat;
	 
	 // méthodes
	 /* Retourne www.<ip serveur>/faireCourse.php
	  * @see com.example.ldcorig.BaseActivity#url()
	  */
	 public String url(){return baseUrl+url;};
 //code s'exécutant Ã  la création de l'activité
	@Override
	/* Affiche l'interface, lie la variables avec la liste et de même avec les boutons
	 * @see com.example.ldcorig.BaseActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//on charge la bonne interface (layout)
		setContentView(R.layout.activity_faire_courses);
		listeViewDesProduitsDeLaListeParRayons=(ExpandableListView) findViewById(R.id.expandableListViewProduitDansListe);
		annulerAchat = (Button) findViewById(R.id.buttonAnnulerAchat);
		caddyAchat = (Button) findViewById(R.id.buttonPoserDansCaddy);
		reporterAchat = (Button) findViewById(R.id.buttonReporterAchat);
		annulerAchat.setOnClickListener(listenerBoutonAnnuler);
		caddyAchat.setOnClickListener(listenerBoutonCaddy);
		reporterAchat.setOnClickListener(listenerBoutonReporter);
	}
	/*
	 * Reçoit les données en Json de la base, les décortique, et crée des ensembleRayon (Rayon + Article)
	 * @see com.example.ldcorig.BaseActivity#traiterDonneesRecues(java.lang.String)
	 */
	 public void traiterDonneesRecues(String jsonResult){
		 ensRayons=new EnsembleRayons();
		 try {
			   JSONObject jsonResponse = new JSONObject(jsonResult);
			   JSONArray jsonMainNode = jsonResponse.optJSONArray("coursesAFaire");		   
			   
			   for (int i = 0; i < jsonMainNode.length(); i++) {
			    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
			    String productName = jsonChildNode.optString("produitLib");
			    String productNumber = jsonChildNode.optString("produitId");
			    String rayName = jsonChildNode.optString("rayonLib");
			    String rayNumber = jsonChildNode.optString("rayonId");
			    String listeQte = jsonChildNode.optString("listeQte");

			    ensRayons.addArticle(productNumber, productName, rayNumber, rayName, listeQte);
			   }
			  } catch (JSONException e) {
			   Toast.makeText(getApplicationContext(), "Error" + e.toString(),
			     Toast.LENGTH_SHORT).show();
			  }
		  //création de l'adapteur pour le choix du rayon qd on fait la liste de course
		  monAdapteur = new AdapterExpandableRayonProduit(this);
		  monAdapteur.setEnsRayon(ensRayons);
		  try{ 
			  Log.i("ListeDeCourse", "setAdapter");	
			  //on associe l'adaptateur Ã  la listView
			  listeViewDesProduitsDeLaListeParRayons.setAdapter(monAdapteur);

		 }
		 catch(NullPointerException e){
			 Log.i("ListeDeCourse", listeViewDesProduitsDeLaListeParRayons.toString());		 
		 }
	 }
	 
	 private OnClickListener listenerBoutonAnnuler = new View.OnClickListener() {		
		@Override
		/*
		 * (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		public void onClick(View v) {
			String adresse=url()+"?action=annuler";
			boolean supressionAEffectuer=false;
			for(int i=0;i<monAdapteur.getEnsRayon().getNbRayon(); i++)
			{
				for(int j=0;j<monAdapteur.getEnsRayon().getRayon(i).getNbArticle();j++) {
					if(monAdapteur.getEnsRayon().getRayon(i).getArticle(j).isSelected()) {
						String noDeArticle=monAdapteur.getEnsRayon().getRayon(i).getArticle(j).getNo();
						adresse+="&tabNoArticle[]="+noDeArticle;
						supressionAEffectuer=true;
					}
				}
			}//fin du for
			if(supressionAEffectuer)accessWebService(adresse);
		}
	};
	
	private OnClickListener listenerBoutonCaddy = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String adresse=url()+"?action=acheter";
			boolean achatEffectue=false;
			for(int i=0;i<monAdapteur.getEnsRayon().getNbRayon(); i++)
			{
				for(int j=0;j<monAdapteur.getEnsRayon().getRayon(i).getNbArticle();j++) {
					if(monAdapteur.getEnsRayon().getRayon(i).getArticle(j).isSelected()) {
						String noDeArticle=monAdapteur.getEnsRayon().getRayon(i).getArticle(j).getNo();
						adresse+="&tabNoArticle[]="+noDeArticle;
						achatEffectue=true;
					}
				}
			}
			if(achatEffectue)accessWebService(adresse);
		}
	};
	
	private OnClickListener listenerBoutonReporter = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String adresse=url()+"?action=reporter";
			boolean reportEffectue=false;
			for(int i=0;i<monAdapteur.getEnsRayon().getNbRayon(); i++)
			{
				for(int j=0;j<monAdapteur.getEnsRayon().getRayon(i).getNbArticle();j++) {
					if(monAdapteur.getEnsRayon().getRayon(i).getArticle(j).isSelected()) {
						String noDeArticle=monAdapteur.getEnsRayon().getRayon(i).getArticle(j).getNo();
						adresse+="&tabNoArticle[]="+noDeArticle;
						reportEffectue=true;
					}
				}
			}
			if(reportEffectue)accessWebService(adresse);
		}
	};
	
}
