package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControlDetail implements Initializable {
	
	private Plante selectedPlante;
	private FileChooser filechooser;
	private File filePath;
	
	@FXML private Button retourBut;
	@FXML private Button ajouterBut;
	@FXML private Button changerPhotoBut;
	@FXML private Button mesureBtn;
	@FXML private Button photoBtn;
	@FXML private Button noteBtn;
	@FXML private Button editerBut;
	
	@FXML private Label id;
	@FXML private Label nom;
	@FXML private Label rempotage;
	@FXML private Label plantation;
	@FXML private Label arronsage;
	@FXML private Label entretien;
	@FXML private Label coupe;
	@FXML private Label recotte;
	@FXML private Label note;
	@FXML private ImageView photo;
	
	@FXML private TableView<Plante> tableView;
	
	/**
	 * This method will allow the user to change the image on the screen
	 * @throws IOException 
	 */
	public void changerPhoto(ActionEvent event) throws IOException {
		Stage stage = (Stage)photo.getScene().getWindow();
		filechooser = new FileChooser();
		filechooser.setTitle("Open image");
		this.filePath = filechooser.showOpenDialog(stage);
		//System.out.println(filePath);
		
		if (filePath != null) {
			String string = filePath.toURI().toString();
			selectedPlante.setImage(new Image(string));
			this.photo.setImage(selectedPlante.getImage());
			selectedPlante.getPhotos().add(string);
			
			int id=Integer.parseInt(selectedPlante.getId());
			
	    	//update photo in plant file
			File plante_File=new File("src/data/plantes.xlsx");
			FileInputStream fis_plante = new FileInputStream(plante_File);
			XSSFWorkbook wb_plante = new XSSFWorkbook(fis_plante);
			XSSFSheet sheet_plante =  wb_plante.getSheetAt(0);
			
			for(Row r:sheet_plante) {
				int id_base=(int)r.getCell(0).getNumericCellValue();
				if(id_base==id) {
					r.getCell(1).setCellValue(string);
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
			r_photo.createCell(1).setCellValue(string);
	        
	    	FileOutputStream outputStream_photo = new FileOutputStream("src/data/photos.xlsx");
	    	wb_photo.write(outputStream_photo );
	    	wb_photo.close();
	    	fis_photo.close();
		}
	}
	
	/**
     * Go to the page of Plante (Liste des plantes)
     * @param event
     * @throws IOException
     */
    @FXML
    public void retour(ActionEvent event) throws IOException {
	     Parent root = FXMLLoader.load(getClass().getResource("view/Plante.fxml"));
	     Stage window = (Stage)retourBut.getScene().getWindow();
         window.setScene(new Scene(root,800,600));
         window.centerOnScreen();
         window.show();
    }

    /**
     * This method accepts a plante to initialize the view
     * @param plante
     */
    public void initData(Plante plante) {
    	selectedPlante = plante;
    	nom.setText(selectedPlante.getNom());
    	id.setText(selectedPlante.getId());
    	
    	if (selectedPlante.getRempotage() != null) {
    		rempotage.setText(selectedPlante.getRempotage().toString());
		}
    	if (selectedPlante.getPlantation() != null) {
    		plantation.setText(selectedPlante.getPlantation().toString());
		}
    	if (selectedPlante.getArronsage() != null) {
    		arronsage.setText(selectedPlante.getArronsage().toString());
		}
    	if (selectedPlante.getEntretien() != null) {
    		entretien.setText(selectedPlante.getEntretien().toString());
		}
    	if (selectedPlante.getCoupe() != null) {
    		coupe.setText(selectedPlante.getCoupe().toString());
		}
    	if (selectedPlante.getRecotte() != null) {
    		recotte.setText(selectedPlante.getRecotte().toString());
		}
    	if (selectedPlante.getNote() != null) {
    		note.setText(selectedPlante.getNote());
		}

    	photo.setImage(selectedPlante.getImage());
    	
    }
    
    /**
     * Go to the page of EditerPlante
     * @param event
     * @throws IOException
     */
    @FXML
    void editerPlante(ActionEvent event) throws IOException {     
        FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/EditerPlante.fxml"));
	    Parent editPlante = loader.load();
	    
	    // access the controller and call a method
	    ControlEditer controller = loader.getController();
	    controller.initData(selectedPlante);
	    
	    Stage window = (Stage)editerBut.getScene().getWindow();
	    window.setTitle("Modifier une plante");
	    window.setScene(new Scene(editPlante));
	    window.centerOnScreen();
        window.show();
    }

	/**
     * Go to the page of mesure (Liste des plantes)
     * @param event
     * @throws IOException
     */
    @FXML
    void pageMesure(ActionEvent event) throws IOException {
     	FXMLLoader loader = new FXMLLoader();
     	loader.setLocation(getClass().getResource("view/PageMesures.fxml"));

 	    Parent pageMesure = loader.load();
	    MesureTableViewController controller = loader.getController();
	    controller.setPlante_detail(true);
	    controller.initMesure(selectedPlante);
	    Stage window = (Stage)mesureBtn.getScene().getWindow();

	 	window.setTitle("Page Mesures");
	 	window.setScene(new Scene(pageMesure));
	 	window.centerOnScreen();
	 	window.show();

    }
    
    /**
     * Go to the page of Photo
     * @param event
     * @throws IOException
     */
    @FXML
    public void pagePhoto(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/PagePhoto.fxml"));
	    Parent pagePhoto = loader.load();
	    
	    // access the controller and call a method
	    ControlPhoto controller = loader.getController();
	    controller.initData(selectedPlante);
	    
	    Stage window = (Stage)photoBtn.getScene().getWindow();
	    window.setTitle("Photo de suivi");
	    window.setScene(new Scene(pagePhoto));
	    window.centerOnScreen();
        window.show();
      
    }
    
    /**
     * Go to the page of Note
     * @param event
     * @throws IOException
     */
    @FXML
    public void pageNote(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/PageNote.fxml"));
	    Parent pageNote = loader.load();
	    
	    // access the controller and call a method
	    ControlNote controller = loader.getController();
	    controller.initData(selectedPlante);
	    
	    Stage window = (Stage)photoBtn.getScene().getWindow();
	    window.setTitle("Note de suivi");
	    window.setScene(new Scene(pageNote));
	    window.centerOnScreen();
        window.show();
      
    }
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
    	
	}
}
