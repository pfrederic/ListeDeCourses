package com.example.ldcorig;


import com.example.ldcorig.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AdapterExpandableRayonProduit extends BaseExpandableListAdapter {

	private EnsembleRayons lesRayons;
	private final Activity context;
	
	public AdapterExpandableRayonProduit(Activity context) {
		lesRayons = new EnsembleRayons();
		this.context = context;
	}
	
	public void setEnsRayon(EnsembleRayons lesJolisRayons){
		lesRayons=lesJolisRayons;
	}

	@Override
	/**
	 * Renvoi le nombre de rayon
	 */
	public int getGroupCount() {
		return lesRayons.getNbRayon();
	}

	@Override
	/**
	 * Renvoi le nombre d'article du rayon à l'indice groupPosition
	 */
	public int getChildrenCount(int groupPosition) {
		ModelRayonGarni leRayonChoisi=lesRayons.getRayon(groupPosition);
		return leRayonChoisi.getNbArticle();
	}

	@Override
	/**
	 * Renvoi le rayon à l'indice groupPosition
	 */
	public Object getGroup(int groupPosition) {
		return lesRayons.getRayon(groupPosition);
	}

	@Override
	/**
	 * Renvoi l'article 
	 */
	public Object getChild(int groupPosition, int childPosition) {
		return lesRayons.getRayon(groupPosition).getArticle(childPosition);
	}

	@Override
	/**
	 * 
	 */
	public long getGroupId(int groupPosition) {
		return Long.parseLong(lesRayons.getRayon(groupPosition).getNo());
	}

	@Override
	/**
	 * 
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return Long.parseLong(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNo());
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class ViewHolder {
		protected TextView textNumeroRayon;
	    protected TextView textLibelleRayon;
	    protected TextView textNumeroArticle;
	    protected TextView textLibelleArticle;
	    protected TextView textQuantiteArticle;
	    protected CheckBox checkbox;
	  }

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		//Log.i("ListeDeCourse", "GROUP");
		View view = null;
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.produit_layout, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.textNumeroRayon = (TextView) view.findViewById(R.id.itemNumeroRayon);
	      viewHolder.textLibelleRayon = (TextView) view.findViewById(R.id.itemLibelleRayon);
	      view.setTag(viewHolder);
	    } else {
	      view = convertView;
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.textNumeroRayon.setText(lesRayons.getRayon(groupPosition).getNo());
	    holder.textLibelleRayon.setText(lesRayons.getRayon(groupPosition).getNom());
	    return view;
	  }

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//Log.i("ListeDeCourse", "CHILD");		
		View view = null;
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.produit_layout_checkable, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.textNumeroArticle = (TextView) view.findViewById(R.id.itemNumeroProduitDsListe);
	      viewHolder.textLibelleArticle = (TextView) view.findViewById(R.id.itemLibelleProduitDsListe);
	      viewHolder.textQuantiteArticle = (TextView) view.findViewById(R.id.itemQuantite);
	      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.itemCheckBoxProdDsListe);
	      viewHolder.checkbox
	          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            	ModelArticle element = (ModelArticle) viewHolder.checkbox.getTag();
	            	element.setSelected(isChecked);
	            }
	          });
	      view.setTag(viewHolder);
	      viewHolder.checkbox.setTag(lesRayons.getRayon(groupPosition).getArticle(childPosition));
	    } else {
	      view = convertView;
	      ((ViewHolder) view.getTag()).checkbox.setTag(lesRayons.getRayon(groupPosition).getArticle(childPosition));
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.textNumeroArticle.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNo());
	    holder.textLibelleArticle.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNom());
	    holder.textQuantiteArticle.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getQte());
	    holder.checkbox.setChecked(lesRayons.getRayon(groupPosition).getArticle(childPosition).isSelected());
	    return view;
	    }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Renvoi la liste des rayons
	 * @return EnsembleRayons lesRayons Propriété de cette classe
	 */
	public EnsembleRayons getEnsRayon() {
		return lesRayons;
	}
}
