package com.example.ldcorig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
	private EditText editTextNaissanceJour;
	private EditText editTextNaissanceMois;
	private EditText editTextNaissanceAnnee;
	private Button boutonRejoindreFamile;
	public String url(){return baseUrl+url;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Pour cacher la barre de titre
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);		
		
		editTextId = (EditText) findViewById(R.id.editTextRegisterIdentifiant);
		editTextMdp = (EditText) findViewById(R.id.editTextRegisterMdp);
		editTextMdpConfirm = (EditText) findViewById(R.id.editTextRegisterMdpConfirm);
		editTextMail = (EditText) findViewById(R.id.editTextRegisterMail);
		editTextNaissanceJour = (EditText) findViewById(R.id.editTextRegisterNaissanceJour);
		editTextNaissanceMois = (EditText) findViewById(R.id.editTextRegisterNaissanceMois);
		editTextNaissanceAnnee = (EditText) findViewById(R.id.editTextRegisterNaissanceAnnee);
		
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
	
	private boolean mailOk() {
		Log.i("ListeDeCourse", "mailOk()");
		boolean saisiOk=false;
		String masque = "^[a-zA-Z]+[a-zA-Z0-9\\._-]*[a-zA-Z0-9]@[a-zA-Z]+"
                + "[a-zA-Z0-9\\._-]*[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
		Pattern pattern = Pattern.compile(masque);
		Matcher controler = pattern.matcher(editTextMail.getText().toString());
		if (controler.matches()){
			saisiOk=true;
		}
		
		return saisiOk;
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
			String mail = "";
			if(mailOk()) {
				mail = editTextMail.getText().toString();
			}
			String dateNaissance = editTextNaissanceAnnee.getText().toString()+"-"+editTextNaissanceMois.getText().toString()+"-"+editTextNaissanceJour.getText().toString();
			
			if(mdp.isEmpty()) {
				Toast.makeText(getApplicationContext(), "Les mots de passe saisis ne sont pas les mêmes", Toast.LENGTH_SHORT).show();
			}
			else if(mail.isEmpty()) {
				Toast.makeText(getApplicationContext(), "L'adresse mail saisi n'est pas valide", Toast.LENGTH_SHORT).show();
			}
			else {
				String adresse=url()+"?action=register&id="+id+"&mdp="+mdp+"&mail="+mail+"&naissance="+dateNaissance;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};
}
