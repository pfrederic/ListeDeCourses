package com.example.ldcorig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
		boutonSeConnecter.setOnClickListener(listernerConnexion);
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		final String id = pref.getString(PREF_ID, null);
		final String mdp = pref.getString(PREF_MDP, null);
		if(id!=null || mdp!=null) {
			//liaison entre les 2 activités
			Intent contexte = new Intent(LoginActivity.this, RemplirListe.class);
			//lancement de la seconde activité
			startActivity(contexte);
		}
	}

	//quelques propriétés de la classe:
	private String url = "authentification.php";
	public String url(){return baseUrl+url;};

	@Override
	void traiterDonneesRecues(String jsonResult) {
	    //le webservice répond et on reçoit sa réponse dans la variable "jsonResult"
			//on garde les bonnes habitudes
			Log.i("ListeDeCourse", "LoginActivity::taiterDonneesRecues(String jsonResult)");
			try{
				JSONObject jsonResponse = new JSONObject(jsonResult);				
				JSONArray jsonMainNode = jsonResponse.optJSONArray("authentification");
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
				Log.i("ListeDeCourse", jsonChildNode.toString());
				if(jsonChildNode.has("erreur")==true) {
					Log.i("ListeDeCourse", "Connection failed");
					Toast.makeText(getApplicationContext(), "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
				}
				else {
					Log.i("ListeDeCourse", "Connection success");				    
				    String id = jsonChildNode.optString("membreId");
					String mdp = jsonChildNode.optString("membreMdp");
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
					.edit()
					.putString(PREF_ID, id)
					.putString(PREF_MDP, mdp)
					.commit();
					
					//liaison entre les 2 activités
					Intent contexte = new Intent(LoginActivity.this, RemplirListe.class);
					//lancement de la seconde activité
					startActivity(contexte);
				}
			} 
			catch (JSONException e) {
				Toast.makeText(getApplicationContext(), "Erreur: Réponse du webservice incompréhensible", Toast.LENGTH_SHORT).show();
			}
		
	}
	
	private OnClickListener listernerConnexion = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i("ListeDeCourse", "Demande de connexion");
			String id = editTextId.getText().toString();
			String mdp = editTextMdp.getText().toString();
			
			if(id.isEmpty() || mdp.isEmpty()){
				Toast.makeText(getApplicationContext(), "Veulliez renseigné les champs", Toast.LENGTH_SHORT).show();
			}
			else {
				String adresse = url()+"?action=login&id="+id+"&mdp="+mdp;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};

}