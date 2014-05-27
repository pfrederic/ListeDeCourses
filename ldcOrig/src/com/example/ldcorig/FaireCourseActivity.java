package com.example.ldcorig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
	 //private String url = "faireCourse.php";
	 private String url = "listeMagasins.php";
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
	 /*
	  * Liste déroulante magasin, qui contient les magasins de la base
	  */
	 private Spinner spinnerMagasin;
	 /*
	  * La liste des données des magasins de la base
	  */
	 public List<Map<String, String>> listeDesMapsMagasin = new ArrayList<Map<String, String>>();
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
		spinnerMagasin=(Spinner) findViewById(R.id.spinnerMagasin);
		annulerAchat = (Button) findViewById(R.id.buttonAnnulerAchat);
		caddyAchat = (Button) findViewById(R.id.buttonPoserDansCaddy);
		reporterAchat = (Button) findViewById(R.id.buttonReporterAchat);
		annulerAchat.setOnClickListener(listenerBoutonAnnuler);
		caddyAchat.setOnClickListener(listenerBoutonCaddy);
		reporterAchat.setOnClickListener(listenerBoutonReporter);
		
		spinnerMagasin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Log.i("ListeDeCourse", "onItemSelected(AdapterView<?> parent, View view, int position, long id)");
				//Changement de valeur de la propriété
				url="faireCourse.php";
				String idDuMagasin=((HashMap<String,String>)(spinnerMagasin.getSelectedItem())).get("magasinId");
				Log.i("ListeDeCourse", idDuMagasin);
				//on va chercher la liste de course en cours
				String adresse=url()+"?action=liste&magasin="+idDuMagasin;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/*
	 * Reçoit les données en Json de la base, les décortique, et crée des ensembleRayon (Rayon + Article)
	 * @see com.example.ldcorig.BaseActivity#traiterDonneesRecues(java.lang.String)
	 */
	 public void traiterDonneesRecues(String jsonResult) {
		 try {
			 JSONObject jsonResponse = new JSONObject(jsonResult);
			 JSONArray jsonMainNode = jsonResponse.optJSONArray("magasinInfos");
			 if(jsonMainNode!=null) {
				 //for de parcours du json
				 for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String name = jsonChildNode.optString("magasinLib");
					String number = jsonChildNode.optString("magasinId");
					listeDesMapsMagasin.add(creerMapMagasin(name, number));
				 }//fin du for de parcours du json
				 //création de l'adapteur pour le choix du rayon
				 SimpleAdapter sAMagasin = new SimpleAdapter(this, listeDesMapsMagasin,R.layout.magasin_layout,new String[] { "magasinLib" }, new int[] { R.id.itemLibelle});
				 //on essaye de donner l'adapteur créé au spinner
				 try{ 
					 //on associe l'adaptateur au spinner des rayons
					 spinnerMagasin.setAdapter(sAMagasin);
				 }
				 catch(NullPointerException e){
					 Log.i("ListeDeCourse",listeDesMapsMagasin.toString());				
				 }
			}
			else {
				ensRayons=new EnsembleRayons();
				jsonMainNode = jsonResponse.optJSONArray("coursesAFaire");		   
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String productName = jsonChildNode.optString("produitLib");
					String productNumber = jsonChildNode.optString("produitId");
					String listeQte = jsonChildNode.optString("listeQte");
					String rayName = jsonChildNode.optString("rayonLib");
					String rayNumber = jsonChildNode.optString("rayonId");
					if(rayName.equals("") && rayNumber.equals("")) {
						rayName="Ne se trouve pas dans le magasin";
						rayNumber="0";
					}

					ensRayons.addArticle(productNumber, productName, rayNumber, rayName, listeQte);
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
		 }
		 catch (JSONException e) {
			 Log.e("ListeDeCourse", "Error" + e.toString());
			 listeViewDesProduitsDeLaListeParRayons.removeAllViewsInLayout();
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
			Log.i("ListeDeCourse", adresse);
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
			Log.i("ListeDeCourse", adresse);
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
			Log.i("ListeDeCourse", adresse);
			if(reportEffectue)accessWebService(adresse);
		}
	};
	//cree une map à partir des paramètres	
	private HashMap<String, String> creerMapMagasin(String libMagasin, String idMagasin) {
		HashMap<String, String> mapMagasin = new HashMap<String, String>();
		mapMagasin.put("magasinLib", libMagasin);
		mapMagasin.put("magasinId", idMagasin);
		
		return mapMagasin;
	}
}
