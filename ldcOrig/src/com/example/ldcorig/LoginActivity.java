package com.example.ldcorig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	private EditText editTextId;
	private EditText editTextMdp;
	private Button boutonSeConnecter;
	private Button boutonInscription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Pour cacher la barre de titre
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		setContentView(R.layout.activity_login);
		
		editTextId = (EditText) findViewById(R.id.editTextId);
		editTextMdp = (EditText) findViewById(R.id.editTextMdp);
		boutonSeConnecter = (Button) findViewById(R.id.buttonSeConnecter);
		boutonInscription = (Button) findViewById(R.id.buttonInscription);
		boutonSeConnecter.setOnClickListener(listenerConnexion);
		boutonInscription.setOnClickListener(listenerInscription);
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		final String id = pref.getString(PREF_ID, null);
		final String mdp = pref.getString(PREF_MDP, null);
		if(id!=null && mdp!=null) {
			String adresse = url()+"?action=login&id="+id+"&mdp="+mdp;
			Log.i("ListeDeCourse", adresse);
			accessWebService(adresse);
		}
	}

	//Supprime le menu d'option
	public boolean onCreateOptionsMenu (Menu menu) {
		return false;
	}
	
	//quelques propri�t�s de la classe:
	private String url = "authentification.php";
	public String url(){return baseUrl+url;};

	@Override
	void traiterDonneesRecues(String jsonResult) {
	    //le webservice r�pond et on re�oit sa r�ponse dans la variable "jsonResult"
		//on garde les bonnes habitudes
		Log.i("ListeDeCourse", "LoginActivity::taiterDonneesRecues(String jsonResult)");
		Log.i("ListeDeCourse", jsonResult.toString());
		try{
			JSONObject jsonResponse = new JSONObject(jsonResult);				
			JSONArray jsonMainNode = jsonResponse.optJSONArray("authentification");
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			//Si le retour du webservice contient l'objet erreur
			if(jsonChildNode.has("erreur")==true) {
				Log.i("ListeDeCourse", "Connection failed");
				Toast.makeText(getApplicationContext(), "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
			}
			//Si le retour du webservice contient autre chose
			else {
				Log.i("ListeDeCourse", "Connection success");
				SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
				final String idShared = pref.getString(PREF_ID, null);
				final String mdpShared = pref.getString(PREF_MDP, null);
				//Si les infos stock�es dans le t�l�phone sont diff�rents de null
				if(idShared!=null || mdpShared!=null) {
				    String id = jsonChildNode.optString("membreId");
					String mdp = jsonChildNode.optString("membreMdp");
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
					.edit()
					.putString(PREF_ID, id)
					.putString(PREF_MDP, mdp)
					.commit();
				}
				//Si il n'y a pas d'identifiant de la famille
				if(jsonChildNode.optString("familleId").equals("null")) {
					//liaison entre les 2 activit�s
					Intent contexte = new Intent(LoginActivity.this, CreerFamilleActivity.class);
					//lancement de la seconde activit�
					startActivity(contexte);
				}
				//Si le retour du webservice contient aute chose (les informations de la base de donn�es)
				else {
					//liaison entre les 2 activit�s
					Intent contexte = new Intent(LoginActivity.this, RemplirListe.class);
					//lancement de la seconde activit�
					startActivity(contexte);
				}
			}				
		} 
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Erreur: R�ponse du webservice incompr�hensible", Toast.LENGTH_SHORT).show();
		}
	}
	
	private OnClickListener listenerConnexion = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i("ListeDeCourse", "Demande de connexion");
			String id = editTextId.getText().toString();
			String mdp = editTextMdp.getText().toString();
			
			if(id.isEmpty() || mdp.isEmpty()){
				Toast.makeText(getApplicationContext(), "Veulliez renseign� les champs", Toast.LENGTH_SHORT).show();
			}
			else {
				String adresse = url()+"?action=login&id="+id+"&mdp="+mdp;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};
	
	private OnClickListener listenerInscription = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//liaison entre les 2 activit�s
			Intent contexte = new Intent(LoginActivity.this, RegisterActivity.class);
			//lancement de la seconde activit�
			startActivity(contexte);			
		}
	};
}