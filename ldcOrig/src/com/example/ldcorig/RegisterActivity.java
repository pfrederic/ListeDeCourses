package com.example.ldcorig;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {

	//quelques propriétés de la classe:
	private String url = "authentification.php";
	
	private EditText editTextId;
	private EditText editTextMdp;
	private EditText editTextMdpConfirm;
	private EditText editTextMail;
	private EditText editTextNaissance;
	private Button boutonRejoindreFamile;
	public String url(){return baseUrl+url;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		editTextId = (EditText) findViewById(R.id.editTextRegisterIdentifiant);
		editTextMdp = (EditText) findViewById(R.id.editTextRegisterMdp);
		editTextMdpConfirm = (EditText) findViewById(R.id.editTextRegisterMdpConfirm);
		editTextMail = (EditText) findViewById(R.id.editTextRegisterMail);
		editTextNaissance = (EditText) findViewById(R.id.editTextRegisterDateNaissance);
		
		boutonRejoindreFamile = (Button) findViewById(R.id.boutonRejoindreFamille);
		boutonRejoindreFamile.setOnClickListener(listenerRejoindreFamille);
	}

	@Override
	void traiterDonneesRecues(String result) {
		// TODO Auto-generated method stub
		
	}

	private boolean memeMotDePasse() {
		Log.i("ListeDeCourse", "memeMotDePasse()");
		boolean lesMemes=false;
		if(editTextMdp.getText().toString().equals(editTextMdpConfirm.getText().toString()))
		{
			lesMemes=true;
		}		
		return lesMemes;
	}
	
	private OnClickListener listenerRejoindreFamille = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i("ListeDeCourse", "onClick(View v)");
			String id = editTextId.getText().toString();
			String mdp = "";
			if(memeMotDePasse()) {
				mdp = editTextMdp.getText().toString();
			}
			String mail = editTextMail.getText().toString();
			String dateNaissance = editTextNaissance.getText().toString();
			
			if(mdp.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Les mots de passe saisis ne sont pas les mêmes", Toast.LENGTH_SHORT).show();
			}
			else {
				String adresse=url()+"?action=register&id="+id+"&mdp="+mdp+"&mail="+mail+"&naissance="+dateNaissance;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};
}
