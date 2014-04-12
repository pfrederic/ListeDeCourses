package com.example.ldcorig;

public class ModelRayon {
	private String nomRayon;
	private String noRayon;
	private boolean selected;
	public ModelRayon(String no,String nom){
	  nomRayon=nom;
	  noRayon=no;
	  selected=false;
	}
	public String getNom() {
	    return nomRayon;
	  }

	  public void setNom(String nom) {
	    nomRayon = nom;
	  }
	  public String getNo() {
		    return noRayon;
		  }

		  public void setNo(String no) {
		    noRayon = no;
		  }
	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean selectionne) {
	    this.selected = selectionne;
	  }

}
