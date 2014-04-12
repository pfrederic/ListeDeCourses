package com.example.ldcorig;

import java.util.ArrayList;
import java.util.List;

public class EnsembleRayons {
	private List <ModelRayonGarni> lesRayons;
	
	public EnsembleRayons() {
		lesRayons=new ArrayList<ModelRayonGarni>();
	}
	int getNbRayon() {
		return lesRayons.size();
	}
	
	void add (ModelRayonGarni leRayon) {
		lesRayons.add(leRayon);
	}
	
	ModelRayonGarni getRayon(int indice) {
		return lesRayons.get(indice);
	}
	
	/**
	 * Cette méthode renvoie null si le rayon n'est pas trouvé dans la liste sinon elle renvoi le rayon trouvé.
	 * @param String noRayon Numéro du rayon demandé
	 * @return ModelRayonGarni Le rayon voulu, null si il n'est pas trouvé
	 */
	ModelRayonGarni getRayonById(String noRayon) {
		ModelRayonGarni leRayon=null;
		int indiceRayon=0;
		while(!(indiceRayon==lesRayons.size() || getRayon(indiceRayon).getNo().equals(noRayon)))
		{
			indiceRayon++;				
		}
		if(!(indiceRayon==lesRayons.size()))
		{
		  leRayon=lesRayons.get(indiceRayon);
		}
		return leRayon;
	}
	
	/**
	 * Ajoute un article et un rayon ou à un rayon spécifié
	 * @param String no			Numéro de l'article
	 * @param String libelle	Libellé de l'article
	 * @param String noR		Numéro de rayon
	 * @param String libR		Libellé du rayon
	 * @param String qte		Quantité de l'artcile
	 */
	public void addArticle(String no, String libelle, String noR,String libR, String qte) {
		ModelRayonGarni leRayon=getRayonById(noR);
		if(leRayon==null)
		{
			leRayon=new ModelRayonGarni(noR, libR);
			leRayon.add(new ModelArticle(no, libelle, qte));
			lesRayons.add(leRayon);
		}
		else {
			leRayon.add(new ModelArticle(no, libelle, qte));
		}
	}
}
