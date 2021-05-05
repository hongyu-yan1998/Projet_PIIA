package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Plante {
	private Integer id;
	private SimpleStringProperty nom;
	private LocalDate rempotage;
	private LocalDate plantation;
	private LocalDate arronsage;
	private LocalDate entretien;
	private LocalDate coupe;
	private LocalDate recotte;
	//private String note;
	private SimpleStringProperty note;
	private Image photo;
	private ArrayList<Mesure> list_mesures;
	private ArrayList<String> list_photos = new ArrayList<>();
	private ArrayList<String> list_notes = new ArrayList<>();
	
	public Plante(Integer id,String nom) {
		this.id=id;
		this.nom = new SimpleStringProperty(nom);
		File file = new File("src/image/add.png");
		String string = file.toURI().toString();
		photo = new Image(string);
		list_photos.add(string);
	}
	
	public String getNom() {
		return nom.get();
	}

	public void setNom(String nom) {
		this.nom = new SimpleStringProperty(nom);
	}
	
	public LocalDate getRempotage() {
		return rempotage;
	}

	public void setRempotage(LocalDate rempotage) {
		this.rempotage = rempotage;
	}

	public LocalDate getPlantation() {
		return plantation;
	}

	public void setPlantation(LocalDate plantation) {
		this.plantation = plantation;
	}

	public LocalDate getArronsage() {
		return arronsage;
	}

	public void setArronsage(LocalDate arronsage) {
		this.arronsage = arronsage;
	}

	public LocalDate getEntretien() {
		return entretien;
	}

	public void setEntretien(LocalDate entretien) {
		this.entretien = entretien;
	}

	public LocalDate getCoupe() {
		return coupe;
	}

	public void setCoupe(LocalDate coupe) {
		this.coupe = coupe;
	}

	public LocalDate getRecotte() {
		return recotte;
	}

	public void setRecotte(LocalDate recotte) {
		this.recotte = recotte;
	}
	
	public String getNote() {
		return note.get();
	}

	public void setNote(String note) {
		this.note = new SimpleStringProperty(note);
		this.list_notes.add(note);
	}

	public Image getImage() {
		return photo;
	}

	public void setImage(Image photo) {
		this.photo = photo;
	}
	
	public String getId() {
		return id.toString();
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<Mesure> getMesures() {
		return list_mesures;
	}

	public void setMesures(ArrayList<Mesure> list_mesures) {
		this.list_mesures = list_mesures;
	}

	public ArrayList<String> getPhotos() {
		return list_photos;
	}

	public void setList_photos(ArrayList<String> list_photos) {
		this.list_photos = list_photos;
	}

	public ArrayList<String> getNotes() {
		return list_notes;
	}

	public void setList_notes(ArrayList<String> list_notes) {
		this.list_notes = list_notes;
	}
	
	
}
