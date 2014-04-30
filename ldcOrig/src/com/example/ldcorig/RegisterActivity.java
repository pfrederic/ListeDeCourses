package com.example.ldcorig;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity {

	//quelques propriétés de la classe:
	private String url = "authentification.php";
	private Boolean creerFamille=false;
	
	private EditText editTextId;
	private EditText editTextMdp;
	private EditText editTextMdpConfirm;
	private EditText editTextMail;
	private EditText editTextNaissanceJour;
	private EditText editTextNaissanceMois;
	private EditText editTextNaissanceAnnee;
	private Button boutonRejoindreFamile;
	private Button boutonCreerFamille;
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
		boutonCreerFamille = (Button) findViewById(R.id.boutonCreerFamille);
		boutonRejoindreFamile.setOnClickListener(listenerNouveauMembre);
		boutonCreerFamille.setOnClickListener(listenerNouveauMembre);
	}

	//Supprime le menu d'option
	public boolean onCreateOptionsMenu (Menu menu) {
		return false;
	}
	
	@Override
	void traiterDonneesRecues(String jsonResult) {
		//le webservice répond et on reçoit sa réponse dans la variable "jsonResult"
				//on garde les bonnes habitudes
				Log.i("ListeDeCourse", "RejoindreFamille::taiterDonneesRecues(String jsonResult)");
				try{
					JSONObject jsonResponse = new JSONObject(jsonResult);				
					JSONArray jsonMainNode = jsonResponse.optJSONArray("register");
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
					if(jsonChildNode.has("erreur")==true) {
						if(jsonChildNode.optString("erreur").equals("id")) {
							Log.i("ListeDeCourse", "Pseudo déja utilisé");
							Toast.makeText(getApplicationContext(), "Ce pseudo est déjà utilisé", Toast.LENGTH_SHORT).show();
						}
						else {
							Log.i("ListeDeCourse", "Adresse mail déja utilisé");
							Toast.makeText(getApplicationContext(), "Ce mail est déjà utilisé", Toast.LENGTH_SHORT).show();
						}
					}
					else {
						String id = editTextId.getText().toString();
						String mdp = editTextMdp.getText().toString();
						getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
						.edit()
						.putString(PREF_ID, id)
						.putString(PREF_MDP, mdp)
						.commit();
						if(creerFamille.equals(true)) {
							//liaison entre les 2 activités
							Intent contexte = new Intent(RegisterActivity.this, CreerFamilleActivity.class);
							//lancement de la seconde activité
							startActivity(contexte);
						}
						else {
							//liaison entre les 2 activités
							Intent contexte = new Intent(RegisterActivity.this, RejoindreFamilleActivity.class);
							//lancement de la seconde activité
							startActivity(contexte);
						}
					}
				}
				catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "Erreur: Réponse du webservice incompréhensible", Toast.LENGTH_SHORT).show();
				}		
	}
	private boolean pseudoOk() {
		Log.i("ListeDeCourse", "pseudoOk()");
		boolean saisiOk=true;
		if(editTextId.getText().toString().isEmpty()){
			Toast.makeText(getApplicationContext(), "Veuliez saisir un identifiant", Toast.LENGTH_SHORT).show();
			saisiOk=false;
		}
		return saisiOk;
	}
	private boolean memeMotDePasse() {
		Log.i("ListeDeCourse", "memeMotDePasse()");
		boolean lesMemes;
		if(editTextMdp.getText().toString().equals(editTextMdpConfirm.getText().toString()))
		{
			lesMemes=true;
		}
		else {
			lesMemes=false;
			Toast.makeText(getApplicationContext(), "Les mots de passe saisis ne sont pas les mêmes", Toast.LENGTH_SHORT).show();
		}
		return lesMemes;
	}
	
	private boolean mailOk() {
		Log.i("ListeDeCourse", "mailOk()");
		boolean saisiOk;
		String masque = "^[a-zA-Z]+[a-zA-Z0-9\\._-]*[a-zA-Z0-9]@[a-zA-Z]+"
                + "[a-zA-Z0-9\\._-]*[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
		Pattern pattern = Pattern.compile(masque);
		Matcher controler = pattern.matcher(editTextMail.getText().toString());
		if (controler.matches()){
			saisiOk=true;
		}
		else {
			saisiOk=false;
			Toast.makeText(getApplicationContext(), "L'adresse mail saisi n'est pas valide", Toast.LENGTH_SHORT).show();
		}		
		return saisiOk;
	}
	
	private boolean anneeNaissanceBisextile() {
		Log.i("ListeDeCourse", "anneNaissanceBisextile()");
		boolean anneeBisextile;
		int annee=Integer.parseInt(editTextNaissanceAnnee.getText().toString());
		if(annee%4==0 && annee%100!=0 || annee%400==0) {
			Log.i("ListeDeCourse", "true");
			anneeBisextile = true;
		}
		else {
			anneeBisextile = false;
		}
		return anneeBisextile;
	}
	
	private boolean dateNaissanceOk() {
		Log.i("ListeDeCourse", "dateNaissanceOk()");
		boolean saisiOk;

		//Création d'une instance calendar
		Calendar cal = Calendar.getInstance();
		//Si les champs mois de naissance, jour de naissance et l'annee de naissance ne sont pas vide et que le mois de naissance est comprit entre 0 et 12
		if(!editTextNaissanceMois.getText().toString().isEmpty() && !editTextNaissanceJour.getText().toString().isEmpty() && !editTextNaissanceAnnee.getText().toString().isEmpty()) {
			int jour = Integer.parseInt(editTextNaissanceJour.getText().toString());
			int mois  = Integer.valueOf(editTextNaissanceMois.getText().toString());
			int annee  = Integer.parseInt(editTextNaissanceAnnee.getText().toString());
			//Si le mois saisi est compri entre 0 et 12 et que l'année soit compri entre 1900 et l'année courante
			if(mois>=0 && mois<=12 && annee<cal.get(Calendar.YEAR) && annee>1900) {
				//Récupération nombre de jour max du mois
				cal.set(Calendar.MONTH, Integer.parseInt(editTextNaissanceMois.getText().toString())-1);
				int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
				//Si le mois de naissance est "février"
				if(editTextNaissanceMois.getText().toString().equals("02")) {
					//Si l'année de naissance est bistextile
					if(anneeNaissanceBisextile()) {
						//Si le jour de naissance est supérieur à 29
						if(jour<0 || jour>29) {
							Toast.makeText(getApplicationContext(), "Date de naissance invalide", Toast.LENGTH_SHORT).show();
							saisiOk=false;
						}
						//Si le jour de naissance est inférieur à 29
						else {
							saisiOk=true;
						}
					}
					//Sinon, c'est pas une année bisextile
					else {
						//Si le jour de naissance est supérieur à 28
						if(jour<0 || jour>28) {
							Toast.makeText(getApplicationContext(), "Date de naissance invalide", Toast.LENGTH_SHORT).show();
							saisiOk=false;
						}
						//Si le jour de naissance est inférieur à 28
						else {
							saisiOk=true;
						}
					}
				}
				//Si ce n'est pas le mois de février
				else {
					//Si le jour saisi NE se trouve PAS dans le mois
					if(jour<0 && jour>maxDay) {
						Toast.makeText(getApplicationContext(), "Date de naissance invalide", Toast.LENGTH_SHORT).show();
						saisiOk=false;
					}
					//Si le jour saisi se trouve bien dans le mois
					else {
						saisiOk=true;
					}
				}
			}
			//Si le mois saisi n'est pas comprit entre 0 et 12 et l'année entre 1900 et l'année courante
			else {
				Toast.makeText(getApplicationContext(), "Saisissez une date de naissance valide", Toast.LENGTH_SHORT).show();
				saisiOk=false;
			}
		}
		//Sinon, si les champs sont vides
		else {
			Toast.makeText(getApplicationContext(), "Saisissez une date de naissance valide", Toast.LENGTH_SHORT).show();
			saisiOk=false;
		}
		return saisiOk;
	}
	
	private OnClickListener listenerNouveauMembre = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i("ListeDeCourse", "onClick(View v)");
			Log.i("ListeDeCourse", v.toString());
			Log.i("ListeDeCourse", String.valueOf(v.getId()));
			String id = "";
			String mdp = "";
			String mail = "";
			String dateNaissance = "";
			if(pseudoOk()) {
				id = editTextId.getText().toString();
			}
			if(memeMotDePasse()) {
				mdp = editTextMdp.getText().toString();
			}
			
			if(mailOk()) {
				mail = editTextMail.getText().toString();
			}			
			if(dateNaissanceOk()){
				dateNaissance = editTextNaissanceAnnee.getText().toString()+"-"+editTextNaissanceMois.getText().toString()+"-"+editTextNaissanceJour.getText().toString();
			}
			if(!mail.isEmpty() && !mdp.isEmpty() && ! dateNaissance.isEmpty()) {
				//Si le bouton cliqué est celui pour ensuite créer un famille
				if(v.getId()==2131099745){
					creerFamille=true;
				}
				
				String adresse=url()+"?action=register&id="+id+"&mdp="+mdp+"&mail="+mail+"&naissance="+dateNaissance;
				Log.i("ListeDeCourse", adresse);
				accessWebService(adresse);
			}
		}
	};
}
