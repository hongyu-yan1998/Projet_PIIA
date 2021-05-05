package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControlEditer {
	private Plante selectedPlante;
	private FileChooser filechooser;
	private File filePath;
	private String path;
	
	@FXML private Button retourBut;
	@FXML private Button ajouterBut;
	@FXML private Button mesuresBut;
	
	@FXML private TextField nom;
	@FXML private DatePicker rempotage;
	@FXML private DatePicker plantation;
	@FXML private DatePicker arronsage;
	@FXML private DatePicker entretien;
	@FXML private DatePicker coupe;
	@FXML private DatePicker recotte;
	@FXML private TextField note;
	@FXML private Text photo;
	@FXML private ImageView img;
	
	/**
     * Go back
     * @param event
     * @throws IOException
     */
    @FXML
    public void retour(ActionEvent event) throws IOException {
	  
        FXMLLoader loader = new FXMLLoader();
     	loader.setLocation(getClass().getResource("view/DetailPlante.fxml"));
 	    Parent detailPlante = loader.load();
 	    
 	    // access the controller and call a method
 	    ControlDetail controller = loader.getController();
 	    controller.initData(selectedPlante);
 	    
 	    Stage window = (Stage)retourBut.getScene().getWindow();
 	    window.setTitle("Details d'une plante");
 	    window.setScene(new Scene(detailPlante));
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
     	FXMLLoader loader = new FXMLLoader();
     	loader.setLocation(getClass().getResource("view/PageMesures.fxml"));

 	    Parent pageMesure = loader.load(); 
	    MesureTableViewController controller = loader.getController();
	    controller.setPlante_edit(true);
	    controller.initMesure(selectedPlante);
	    Stage window = (Stage)mesuresBut.getScene().getWindow();
        window.setScene(new Scene(pageMesure));
        window.centerOnScreen();
        window.show();
    }
    
    
    public void choisirPhoto(MouseEvent event) throws IOException {
		
		Stage stage = (Stage)photo.getScene().getWindow();
		filechooser = new FileChooser();
		filechooser.setTitle("Open image");
		
		this.filePath = filechooser.showOpenDialog(stage);
		if (filePath != null) {
			path = filePath.toURI().toString();
			selectedPlante.setImage(new Image(path));
			this.img.setImage(selectedPlante.getImage());
			selectedPlante.getPhotos().add(path);
			
			int id=Integer.parseInt(selectedPlante.getId());
			
	    	//update photo in plant file
			File plante_File=new File("src/data/plantes.xlsx");
			FileInputStream fis_plante = new FileInputStream(plante_File);
			XSSFWorkbook wb_plante = new XSSFWorkbook(fis_plante);
			XSSFSheet sheet_plante =  wb_plante.getSheetAt(0);
			
			for(Row r:sheet_plante) {
				int id_base=(int)r.getCell(0).getNumericCellValue();
				if(id_base==id) {
					r.getCell(1).setCellValue(path);
				}
			}
			
	    	FileOutputStream outputStream_p = new FileOutputStream("src/data/plantes.xlsx");
	    	wb_plante.write(outputStream_p);
	    	wb_plante.close();
	    	fis_plante.close();
			
			//enregistre photo in file 
	    	File photo_File=new File("src/data/photos.xlsx");
	        FileInputStream fis_photo = new FileInputStream(photo_File);
	        XSSFWorkbook wb_photo = new XSSFWorkbook(fis_photo);
	        XSSFSheet sheet_photo = wb_photo.getSheetAt(0);
			
			Row r_photo=sheet_photo.createRow(sheet_photo.getLastRowNum()+1);
			r_photo.createCell(0).setCellValue(id);
			r_photo.createCell(1).setCellValue(path);
	        
	    	FileOutputStream outputStream_photo = new FileOutputStream("src/data/photos.xlsx");
	    	wb_photo.write(outputStream_photo );
	    	wb_photo.close();
	    	fis_photo.close();
		}
		
		
	}
    
    public void ajouterPlante() throws IOException {
    	selectedPlante.setNom(nom.getText());
    	File mesureFile=new File("src/data/plantes.xlsx");
		FileInputStream fis = new FileInputStream(mesureFile);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int id=Integer.parseInt(selectedPlante.getId());
		
		//ouverture du fichier notes
    	File note_File=new File("src/data/notes.xlsx");
        FileInputStream fis_note = new FileInputStream(note_File);
        XSSFWorkbook wb_note = new XSSFWorkbook(fis_note);
        XSSFSheet sheet_note = wb_note.getSheetAt(0);
		
        for (Row r : sheet) {
			if((int)r.getCell(0).getNumericCellValue()==id) {

				if(rempotage.getValue()!=null) {
					if(r.getCell(3)==null) {
						r.createCell(3);
					}
					r.getCell(3).setCellValue(rempotage.getValue().toString());
					selectedPlante.setRempotage(rempotage.getValue());
					
				}
				if(plantation.getValue()!=null) {
					if(r.getCell(4)==null) {
						r.createCell(4);
					}
					r.getCell(4).setCellValue(plantation.getValue().toString());
					selectedPlante.setPlantation(plantation.getValue());
				}
				if(arronsage.getValue()!=null) {
					if(r.getCell(5)==null) {
						r.createCell(5);
					}
					r.getCell(5).setCellValue(arronsage.getValue().toString());
					selectedPlante.setArronsage(arronsage.getValue());
				}
				if(entretien.getValue()!=null) {
					if(r.getCell(6)==null) {
						r.createCell(6);
					}
					r.getCell(6).setCellValue(entretien.getValue().toString());
					selectedPlante.setEntretien(entretien.getValue());
				}
				if(coupe.getValue()!=null) {
					if(r.getCell(7)==null) {
						r.createCell(7);
					}
					r.getCell(7).setCellValue(coupe.getValue().toString());
					selectedPlante.setCoupe(coupe.getValue());
				}
				if(recotte.getValue()!=null) {
					if(r.getCell(8)==null) {
						r.createCell(8);
					}
					r.getCell(8).setCellValue(recotte.getValue().toString());
					selectedPlante.setRecotte(recotte.getValue());
				}
				if(note.getText()!=null) {
					if(r.getCell(9)==null) {
						r.createCell(9);
					}
					r.getCell(9).setCellValue(note.getText());
					selectedPlante.setNote(note.getText());
					
					Row r_note=sheet_note.createRow(sheet_note.getLastRowNum()+1);
		    		r_note.createCell(0).setCellValue(id);
		    		r_note.createCell(1).setCellValue(note.getText());
				}
			}
		
		}
    	FileOutputStream outputStream_note = new FileOutputStream("src/data/notes.xlsx");
    	wb_note.write(outputStream_note);
    	wb_note.close();
    	fis_note.close();
        
		FileOutputStream outputStream = new FileOutputStream("src/data/plantes.xlsx");
        workbook.write(outputStream);
        workbook.close();
        fis.close();
        
        FXMLLoader loader = new FXMLLoader();
     	loader.setLocation(getClass().getResource("view/DetailPlante.fxml"));
 	    Parent detailPlante = loader.load();
 	    
 	    // access the controller and call a method
 	    ControlDetail controller = loader.getController();
 	    controller.initData(selectedPlante);
 	    
 	    Stage window = (Stage)retourBut.getScene().getWindow();
 	    window.setTitle("Details d'une plante");
 	    window.setScene(new Scene(detailPlante));
 	    window.centerOnScreen();
        window.show();
	    
    }
    
    /**
     * This method accepts a plante to initialize the view
     * @param plante
     */
    public void initData(Plante plante) {
    	selectedPlante = plante;
    	filePath = new File(selectedPlante.getPhotos().get(selectedPlante.getPhotos().size()-1));
    	nom.setText(selectedPlante.getNom());
    
    	if (selectedPlante.getRempotage() != null) {
    		rempotage.setPromptText(selectedPlante.getRempotage().toString());
		}
    	if (selectedPlante.getPlantation() != null) {
    		plantation.setPromptText(selectedPlante.getPlantation().toString());
		}
    	if (selectedPlante.getArronsage() != null) {
    		arronsage.setPromptText(selectedPlante.getArronsage().toString());
		}
    	if (selectedPlante.getEntretien() != null) {
    		entretien.setPromptText(selectedPlante.getEntretien().toString());
		}
    	if (selectedPlante.getCoupe() != null) {
    		coupe.setPromptText(selectedPlante.getCoupe().toString());
		}
    	if (selectedPlante.getRecotte() != null) {
    		recotte.setPromptText(selectedPlante.getRecotte().toString());
		}
    	if (selectedPlante.getNote() != null) {
    		note.setText(selectedPlante.getNote());
		} 
    	
    	img.setImage(selectedPlante.getImage());
    }
}
