package appli;

import java.time.LocalDate;

public class Mesure {
	private String id;
	private String nom,unite;
	private String valeur;
	private LocalDate date;
	
	public Mesure(String id,String nom,String val,String unite,LocalDate date) {
		this.setId(id);
		this.setNom(nom);
		this.setValeur(val);
		this.setUnite(unite);
		this.setDate(date);
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getUnite() {
		return unite;
	}

	public void setUnite(String unite) {
		this.unite = unite;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
