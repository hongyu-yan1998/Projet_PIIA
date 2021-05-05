package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControlAjouter {
	private FileChooser filechooser;
	private File filePath = new File("src/image/add.png");
	private String path  = filePath.toURI().toString();
	
	@FXML private Button retourBut;
	@FXML private Button ajouterBut;
	@FXML private Button mesuresBut;
	
	// These instance variablea are used to create new Plante Objects
	@FXML private TextField nom;
	@FXML private DatePicker rempotage;
	@FXML private DatePicker plantation;
	@FXML private DatePicker arronsage;
	@FXML private DatePicker entretien;
	@FXML private DatePicker coupe;
	@FXML private DatePicker recotte;
	@FXML private TextField note;
	@FXML private Text photo;
	
	/**
     * Go to the page of Plante (Liste des plantes)
     * @param event
     * @throws IOException
     */
    @FXML
    void retour(ActionEvent event) throws IOException {
	     Parent root = FXMLLoader.load(getClass().getResource("view/Plante.fxml"));
	     Stage window = (Stage)retourBut.getScene().getWindow();
         window.setScene(new Scene(root,800,600));
         window.centerOnScreen();
         window.show();
    }
    
    /**
     * Go to the page of Mesures
     * @param event
     * @throws IOException
     */
    @FXML
    void ChangePageMesures(ActionEvent event) throws IOException {
	     Parent root = FXMLLoader.load(getClass().getResource("view/PageMesures.fxml"));
	     Stage window = (Stage)retourBut.getScene().getWindow();
         window.setScene(new Scene(root));
         window.centerOnScreen();
         window.show();
    }
  
    public void ajouterPlante() throws IOException {
    	// Get all the items from the table as a list, then add the new plante to the list
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/Plante.fxml"));
	    Parent Plante = loader.load();
    	
	    //get the last id in the table
	    TableView<Plante> tableView =  (TableView<Plante>)Plante.lookup("#tableView");
	    Integer newId;
	    if (tableView.getItems().size() != 0) {
	    	newId = Integer.parseInt(tableView.getItems().get(tableView.getItems().size()-1).getId())+1;
		}
	    else {
	    	newId = 1;
		}
	    
    	//create a new plant
    	Plante newPlante = new Plante(newId,nom.getText());
		File plante_File=new File("src/data/plantes.xlsx");
		FileInputStream fis_plante = new FileInputStream(plante_File);
		XSSFWorkbook wb_plante = new XSSFWorkbook(fis_plante);
		XSSFSheet sheet_plante =  wb_plante.getSheetAt(0);
		
		//create a photo
    	File photo_File=new File("src/data/photos.xlsx");
        FileInputStream fis_photo = new FileInputStream(photo_File);
        XSSFWorkbook wb_photo = new XSSFWorkbook(fis_photo);
        XSSFSheet sheet_photo = wb_photo.getSheetAt(0);
		
		//update the dataset
		Row row = sheet_plante.createRow(sheet_plante.getLastRowNum()+1);
		
		row.createCell(0).setCellValue(newId);
		newPlante.setImage(new Image(path));
		//update photo
		Row r_photo=sheet_photo.createRow(sheet_photo.getLastRowNum()+1);
		newPlante.getPhotos().add(path);
		r_photo.createCell(0).setCellValue(newId);
		r_photo.createCell(1).setCellValue(path);
		row.createCell(1).setCellValue(path);
		
		row.createCell(2).setCellValue(nom.getText());
		
		//ouverture du fichier notes
    	File note_File=new File("src/data/notes.xlsx");
        FileInputStream fis_note = new FileInputStream(note_File);
        XSSFWorkbook wb_note = new XSSFWorkbook(fis_note);
        XSSFSheet sheet_note = wb_note.getSheetAt(0);
    	
    	if (rempotage.getValue() != null) {
    		newPlante.setRempotage(rempotage.getValue());
    		row.createCell(3).setCellValue(rempotage.getValue().toString());
		}
    	if (plantation.getValue() != null) {
    		newPlante.setPlantation(plantation.getValue());
    		row.createCell(4).setCellValue(plantation.getValue().toString());
		}
    	if (arronsage.getValue() != null) {
    		newPlante.setArronsage(arronsage.getValue());
    		row.createCell(5).setCellValue(arronsage.getValue().toString());
		}
    	if (entretien.getValue() != null) {
    		newPlante.setEntretien(entretien.getValue());
    		row.createCell(6).setCellValue(entretien.getValue().toString());
		}
    	if (coupe.getValue() != null) {
    		newPlante.setCoupe(coupe.getValue());
    		row.createCell(7).setCellValue(coupe.getValue().toString());
		}
    	if (recotte.getValue() != null) {
    		newPlante.setRecotte(recotte.getValue());
    		row.createCell(8).setCellValue(recotte.getValue().toString());
		}
    	if (note.getText() != null) {
    		newPlante.setNote(note.getText());
    		row.createCell(9).setCellValue(note.getText());
    		
    		Row r_note=sheet_note.createRow(sheet_note.getLastRowNum()+1);
    		r_note.createCell(0).setCellValue(newId);
    		r_note.createCell(1).setCellValue(note.getText());
		}

    	FileOutputStream outputStream_note = new FileOutputStream("src/data/notes.xlsx");
    	wb_note.write(outputStream_note);
    	wb_note.close();
    	fis_note.close();
    	
    	FileOutputStream outputStream_photo = new FileOutputStream("src/data/photos.xlsx");
    	wb_photo.write(outputStream_photo );
    	wb_photo.close();
    	fis_photo.close();
    	
		FileOutputStream outputStream = new FileOutputStream("src/data/plantes.xlsx");
		wb_plante.write(outputStream);
		wb_plante.close();
        fis_plante.close();
	    
        tableView.getItems().add(newPlante);
	    Stage window = (Stage)ajouterBut.getScene().getWindow();
	    window.setTitle("Details d'une plante");
	    window.setScene(new Scene(Plante));
	    window.centerOnScreen();
        window.show();
	    
    }
   
    
    public void choisirPhoto(MouseEvent event) {
    	Stage stage = new Stage();
		filechooser = new FileChooser();
		filechooser.setTitle("Open image");
		this.filePath = filechooser.showOpenDialog(stage);
		if (filePath != null) {
			path = filePath.toURI().toString();
		}
	}
}
