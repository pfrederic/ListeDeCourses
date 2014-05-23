package com.example.ldcorig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {

	private String url = "famille.php?action=code";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

	@Override
	public String url(){return baseUrl+url;};

	@Override
	void traiterDonneesRecues(String jsonResult) {
		Log.i("ListeDeCourse", "SettingFamille::taiterDonneesRecues(String jsonResult)");
		try{
			TextView outPutCode = (TextView) findViewById(R.id.textViewSettingCodeFamille);
			JSONObject jsonResponse = new JSONObject(jsonResult);				
			JSONArray jsonMainNode = jsonResponse.optJSONArray("famille");
			JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
			if(jsonChildNode.has("erreur")==true) {
				outPutCode.setText("Vous n'etes pas responsable de famille");
			}
			else {
				String codeFamille = jsonChildNode.optString("familleCode");
				outPutCode.setText(codeFamille);
			}
		}
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Erreur: Réponse du webservice incompréhensible", Toast.LENGTH_SHORT).show();
		}	
	}
}
