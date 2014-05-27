package com.example.ldcorig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ldcorig.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class RemplirListe extends BaseActivity{
	
	//propriétés
	private String url = "listeRayons.php";
	public String url(){return baseUrl+url;};
	final Context context = this;
	
	//widgets manipulÃ©s
	private Spinner spinnerRayon;	
	private Spinner spinnerProduit;
	private Button boutonAjouter;
	private ListView listViewProduits;
	//les listes des donnÃ©es
	public List<Map<String, String>> listeDesMapsRayon = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> listeDesMapsProduit = new ArrayList<Map<String, String>>();
	public List<Map<String, String>> listeDesMapsProduitDsListe = new ArrayList<Map<String, String>>();
	//les mÃ©thodes de la classe
	//code s'exÃ©cutant Ã  la crÃ©ation de l'activitÃ©	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//on choisi la vue correspondant Ã  l'activitÃ©
		setContentView(R.layout.activity_remplir_liste);
	
		//obtention de la liste view des produits Ã  acheter
		listViewProduits=(ListView)findViewById(R.id.listViewProduits);
		listViewProduits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("ListeDeCourse", "RemplirListe::onItemClick");
				Log.i("ListeDeCourse", listeDesMapsProduitDsListe.get(position).toString());
				final String noProduit = listeDesMapsProduitDsListe.get(position).get("produitId").toString();
				String quantite = listeDesMapsProduitDsListe.get(position).get("listeQte").toString();
				String nomProduit = listeDesMapsProduitDsListe.get(position).get("produitLib").toString();
				final EditText inputQuantite = new EditText(context);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setCancelable(true);
				builder.setTitle("Modification de la quantité");
				builder.setMessage(nomProduit+" : ");
				builder.setView(inputQuantite);
				inputQuantite.setText(quantite);
				inputQuantite.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputQuantite.selectAll();
				builder.setPositiveButton("Modifier", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String nouvelleQuantite=inputQuantite.getText().toString();
						String adresse=baseUrl+"listeCourse.php?action=modification&produitId="+noProduit+"&qte="+nouvelleQuantite;
						Log.i("ListeDeCourse", adresse);
						accessWebService(adresse);
					}
				});
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}					
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}			
		});
		listViewProduits.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i("ListeDeCourse", "RemplirListe::onItemLongClick");
				Log.i("ListeDeCourse", listeDesMapsProduitDsListe.get(position).toString());
				final String noProduit = listeDesMapsProduitDsListe.get(position).get("produitId").toString();
				String nomProduit = listeDesMapsProduitDsListe.get(position).get("produitLib").toString();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setCancelable(true);
				builder.setTitle("Suppression");
				builder.setMessage("Etes-vous sûr de vouloir supprimer le produit "+nomProduit+" ?");
				builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String adresse=baseUrl+"listeCourse.php?action=suppression&produitId="+noProduit;
						Log.i("ListeDeCourse", adresse);
						accessWebService(adresse);
					}
				});
				builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}					
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
		});
		//obtention du bouton ajouter et codage du click sur le bouton ajouter
		boutonAjouter=(Button) findViewById(R.id.buttonAjouterALaListe);
		boutonAjouter.setOnClickListener(new AdapterView.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// ajout d'un produit à la liste
				spinnerProduit= (Spinner)findViewById(R.id.SpinnerProduit);
				String noProduit=((HashMap<String,String>)(spinnerProduit.getSelectedItem())).get("produitId");
				String qte=((EditText)findViewById(R.id.editTextQuantite)).getText().toString();
				String adresse=baseUrl+"listeCourse.php?action=ajout&produitId="+noProduit+"&qte="+qte;
				Log.i("ListeDeCourse",adresse);
				accessWebService(adresse);	
				((EditText)findViewById(R.id.editTextQuantite)).getText().clear();
			}});
		//obtention du spinner des rayons dans la layout listeDeCourse		
		spinnerRayon= (Spinner)findViewById(R.id.spinnerRayon);
	
		spinnerRayon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {               

		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    
		    	String nomDuRayon=((HashMap<String,String>)(spinnerRayon.getSelectedItem())).get("rayonLib");
		    	Log.i("ListeDeCourse",nomDuRayon);
	            accessWebService(baseUrl+"listeProduits.php?rayon="+nomDuRayon);	            
		    }

		    public void onNothingSelected(AdapterView<?> parent) {
		    // Another interface callback
		    }
		});
	
		//obtention du spinner des produits dans la layout listeDeCourse
		spinnerProduit=(Spinner) findViewById(R.id.SpinnerProduit);
		
	}
	//charge les donnÃ©es de la fenÃªtre: liste des rayons et liste des produits du rayon 
	@Override
	public void traiterDonneesRecues(String jsonResult){
		Log.i("ListeDeCourse", jsonResult.toString());
			try{
				JSONObject jsonResponse = new JSONObject(jsonResult);				
				JSONArray jsonMainNode = jsonResponse.optJSONArray("rayonInfos");
				if(jsonMainNode!=null){//si c'est la liste des rayons
					//for de parcours du json
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						String name = jsonChildNode.optString("rayonLib");
						String number = jsonChildNode.optString("rayonId");
						listeDesMapsRayon.add(creerMapRayon(name, number));
					}//fin du for de parcours du json
					//crÃ©ation de l'adapteur pour le choix du rayon
					SimpleAdapter sARayon = new SimpleAdapter(this,listeDesMapsRayon,R.layout.rayon_layout,new String[] { "rayonLib" },new int[] { R.id.itemLibelle});
					//on essaye de donner l'adapteur crÃ©Ã© au spinner
					
					try{ 
						//on associe l'adaptateur au spinner des rayons
						spinnerRayon.setAdapter(sARayon);
						//on va chercher la liste de course en cours
						String adresse=baseUrl+"listeCourse.php";
						accessWebService(adresse);
					}
					catch(NullPointerException e){
						Log.i("ListeDeCourse",listeDesMapsRayon.toString());
					}
				}//fin du alors
				else //ce n'est pas la liste des rayons
				{
					jsonMainNode = jsonResponse.optJSONArray("produitsDuRayon");
					if(jsonMainNode!=null){//si c'est la liste des produits
						//vidons la map
						listeDesMapsProduit.clear();
						//for de parcours du json
						for (int i = 0; i < jsonMainNode.length(); i++) {
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							String name = jsonChildNode.optString("produitLib");
							String number = jsonChildNode.optString("produitId");
							listeDesMapsProduit.add(creerMapProduit(name, number));
						}//fin du for de parcours du json
						//création de l'adapteur pour le choix du rayon
						SimpleAdapter saProduit = new SimpleAdapter(this,listeDesMapsProduit,R.layout.produit_layout,new String[] { "produitLib" },new int[] { R.id.itemLibelleRayon});
						//on essaye de donner l'adapteur créé au spinner
						
						try{ 
							//on associe l'adaptateur au spinner des rayons
							spinnerProduit.setAdapter(saProduit);
						}
						catch(NullPointerException e){
							
							Log.i("ListeDeCourse",listeDesMapsRayon.toString());
						}
					}
					else//ce n'est pas la liste des produits non plus
					{
						jsonMainNode = jsonResponse.optJSONArray("listeDeCourse");
						Log.i("ListeDeCourse",jsonMainNode.toString());
						if(jsonMainNode!=null){//si c'est la liste de course
							//vidons la map
							listeDesMapsProduitDsListe.clear();
							//for de parcours du json
							for (int i = 0; i < jsonMainNode.length(); i++) {
								JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
								String idProduit = jsonChildNode.optString("produitId");
								String qte = jsonChildNode.optString("listeQte");
								String libelle=jsonChildNode.optString("produitLib");
								listeDesMapsProduitDsListe.add(creerMapProduitAAcheter(idProduit, qte, libelle));
							}//fin du for de parcours du json
							//création de l'adapteur pour le remplissage de la liste de course
							SimpleAdapter saListe = new SimpleAdapter(this,listeDesMapsProduitDsListe,R.layout.produit_a_acheter_layout,new String[] { "produitId","produitLib","listeQte" },new int[] { R.id.itemNumeroProduit,R.id.itemLibelleProduit,R.id.itemQuantite});
							//on essaye de donner l'adapteur créé à la liste de course
							
							try{ 
								//on associe l'adaptateur à la liste des produits à acheter
								listViewProduits.setAdapter(saListe);
							}
							catch(NullPointerException e){
								
								Log.i("ListeDeCourse",listeDesMapsProduitDsListe.toString());
							}
						}
						else//ce n'est pas la liste de course non plus
						{
							
						}//fin de "ce n'est pas la liste de course non plus"
					}//fin de "ce n'est pas la liste des produits non plus"
				}//fin de "ce n'est pas la liste des rayons"
			} //fin du try
			catch (JSONException e) {
				Log.e("ListeDeCourse", "Error " + e.toString());
			}

	}
	//cree une map Ã  partir des paramÃ¨tres	
	private HashMap<String, String> creerMapProduitAAcheter(String idProduit,
			String qte,String libProduit) {
		HashMap<String, String> mapProduitAAcheter = new HashMap<String, String>();
		mapProduitAAcheter.put("produitId", idProduit);
		mapProduitAAcheter.put("listeQte", qte);
		mapProduitAAcheter.put("produitLib", libProduit);
		
		return mapProduitAAcheter;
	}
	//cree une map à partir des paraètres
	private HashMap<String, String> creerMapRayon(String name, String number) {
		HashMap<String, String> mapRayon = new HashMap<String, String>();
		mapRayon.put("rayonId", number);
		mapRayon.put("rayonLib", name);
		return mapRayon;
	}
	//cree une map à partir des paramètres
	private HashMap<String, String> creerMapProduit(String name, String number) {
		HashMap<String, String> mapRayon = new HashMap<String, String>();
		mapRayon.put("produitId", number);
		mapRayon.put("produitLib", name);
		return mapRayon;
	} 
}//fin de la classe RemplirListe
