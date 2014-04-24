package com.example.ldcorig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RejoindreFamilleActivity extends BaseActivity {

	//quelques propri�t�s de la classe:
	private String url = "famille.php";
	private EditText editTextCodeFamille;
	private Button boutonRejoindreFamille;
	
	
	public String url(){return baseUrl+url;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Pour cacher la barre de titre
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		setContentView(R.layout.activity_rejoindre_famille);
		
		editTextCodeFamille = (EditText) findViewById(R.id.editTextRejoindreFamilleCode);
		boutonRejoindreFamille = (Button) findViewById(R.id.buttonRejoindreFamilleRejoindre);
		boutonRejoindreFamille.setOnClickListener(listenerBoutonRejoindre);		
	}

	@Override
	void traiterDonneesRecues(String jsonResult) {
		//le webservice r�pond et on re�oit sa r�ponse dans la variable "jsonResult"
		//on garde les bonnes habitudes
		Log.i("ListeDeCourse", "RejoindreFamille::taiterDonneesRecues(String jsonResult)");
		try{
			JSONObject jsonResponse = new JSONObject(jsonResult);				
			JSONArray jsonMainNode = jsonResponse.optJSONArray("famille");
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			if(jsonChildNode.has("erreur")==true) {
				Log.i("ListeDeCourse", "Aucune famille trouv�");
				Toast.makeText(getApplicationContext(), "Ce code n'existe pas", Toast.LENGTH_SHORT).show();
			}
		}
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Erreur: R�ponse du webservice incompr�hensible", Toast.LENGTH_SHORT).show();
		}		
	}
	
	private boolean saisiOk() {
		boolean saisiOk;
		if(editTextCodeFamille.getText().toString().isEmpty())
		{
			saisiOk=false;
			Toast.makeText(getApplicationContext(), "Veulliez saisir un code", Toast.LENGTH_SHORT).show();
		}
		else {
			saisiOk=true;
		}
		return saisiOk;
	}
	
	private OnClickListener listenerBoutonRejoindre = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String adresse=url()+"?action=rejoindre&code="+editTextCodeFamille.getText().toString();
			if(saisiOk()) {
				accessWebService(adresse);
			}
		}
	};
}
