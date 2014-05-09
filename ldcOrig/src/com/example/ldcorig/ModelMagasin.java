package com.example.ldcorig;

public class ModelMagasin {
	private String nomMagasin;
	private String noMagasin;
	
	public ModelMagasin(String nom, String numero) {
		nomMagasin=nom;
		noMagasin=numero;				
	}
	
	public String getNom() {
		return nomMagasin;
	}
	
	public String getNo() {
		return noMagasin;
	}
	
	public void setNom(String nom) {
		nomMagasin=nom;
	}
	
	public void setNo(String no) {
		noMagasin=no;
	}
}
