package com.example.ldcorig;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreerFamilleActivity extends BaseActivity {

	//quelques propriétés de la classe:
	private String url = "famille.php";
	private EditText editTextLibelle;
	private Button boutonCreationFamille;
	
	public String url(){return baseUrl+url;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creer_famille);
		editTextLibelle = (EditText) findViewById(R.id.editTextCreerFamilleLibelle);
		boutonCreationFamille = (Button) findViewById(R.id.buttonCreerFamilleCreation);
		boutonCreationFamille.setOnClickListener(listenerBoutonCreation);

	}

	@Override
	void traiterDonneesRecues(String result) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean libelleFamilleOk() {
		boolean saisiOk;
		if(editTextLibelle.getText().toString().isEmpty()) {
			saisiOk = false;
			Toast.makeText(getApplicationContext(), "Veulliez saisir un libelle", Toast.LENGTH_SHORT).show();
		}
		else {
			saisiOk = true;
		}
		return saisiOk;
	}
	
	private OnClickListener listenerBoutonCreation = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String libelle;
			if(libelleFamilleOk()) {
				libelle = editTextLibelle.getText().toString();
				String adresse=url()+"?action=creation&libelle="+libelle;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};
}
