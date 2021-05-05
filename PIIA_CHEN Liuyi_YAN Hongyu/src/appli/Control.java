package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Control implements Initializable {

	private ObservableList<Plante> plante = FXCollections.observableArrayList();
	private ObservableList<PlanteImage> imgList = FXCollections.observableArrayList(); 
	
    @FXML private Button ajouterBut;
    @FXML private Button detailBut;
    @FXML private Button supprimerBut;
    
    
    //configure the table
    @FXML private TableView<Plante> tableView;
    @FXML private TableColumn<Plante, LocalDate> plantationColumn;
    @FXML private TableColumn<Plante, String> nomColumn;
    @FXML private TableColumn<Plante, String> idColumn;
    
    //calendar
    @FXML private Button calendarBtn;
    
    public void pageCalendar(ActionEvent event) throws IOException {
    	CalendarApp.loadBase(CalendarApp.getJardins());
	    Stage window = (Stage)calendarBtn.getScene().getWindow();
        window.setScene(CalendarApp.getScene());
        window.centerOnScreen();
        window.show();
      
    }
    

	public void changeNomCellEvent(CellEditEvent editedCell) {
    	Plante planteSelected = tableView.getSelectionModel().getSelectedItem();
    	planteSelected.setNom(editedCell.getNewValue().toString());
    }
    
    public void userClickedOnTable() {
		this.detailBut.setDisable(false);
	}

    /**
     * Go to the page of AjouterPlante
     * @param event
     * @throws IOException
     */
    @FXML
    void ajouterPlante(ActionEvent event) throws IOException {
    	Parent ajoutPlante = FXMLLoader.load(getClass().getResource("view/AjouterPlante.fxml"));
        Stage window = (Stage) ajouterBut.getScene().getWindow();
        window.setTitle("Ajout d'une plante");
        window.setScene(new Scene(ajoutPlante));
        window.centerOnScreen();
        window.show();
    }
    
    
    /**
     * Go to the page of DetailPlante
     * @param event
     * @throws IOException
     */
    @FXML
    void detailPlante(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("view/DetailPlante.fxml"));
	    Parent detailPlante = loader.load();
	    
	    // access the controller and call a method
	    ControlDetail controller = loader.getController();
	    controller.initData(tableView.getSelectionModel().getSelectedItem());
	    
	    Stage window = (Stage)detailBut.getScene().getWindow();
	    window.setTitle("Details d'une plante");
	    window.setScene(new Scene(detailPlante));
	    window.centerOnScreen();
        window.show();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			getPlante();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		nomColumn.setCellValueFactory(new PropertyValueFactory<Plante, String>("Nom"));
	    idColumn.setCellValueFactory(new PropertyValueFactory<Plante, String>("id"));
	    plantationColumn.setCellValueFactory(new PropertyValueFactory<Plante, LocalDate>("plantation"));
	    
		tableView.setItems(plante);
		
		// Update the table to allow for the nom field
		tableView.setEditable(true);
		nomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		
		// This will allow the table to select multiple rows at once
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		// Disable the detailBut until a row is selected
		this.detailBut.setDisable(true);
	 }
	    
	/**
	 * This method will return an ObservableList of Plante objects
	 * @return
	 * @throws IOException 
	 */
    public void getPlante() throws IOException {     
    	
        File mesureFile = new File("src/data/plantes.xlsx");
        FileInputStream fis = new FileInputStream(mesureFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        for (Row r : sheet) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d-MMM-yyyy][yyyy-MM-dd]");
            Plante p = new Plante((int) r.getCell(0).getNumericCellValue(),r.getCell(2).toString());
            p.setImage(new Image(r.getCell(1).toString()));
            
            if (r.getCell(3) != null) {
            	String date = r.getCell(3).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setRempotage(localDate);
	        }
            
            if (r.getCell(4) != null) {
	            String date = r.getCell(4).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setPlantation(localDate);
            }
            
            if (r.getCell(5)!= null) {
                String date = r.getCell(5).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setArronsage(localDate);
            }
            
            if (r.getCell(6) != null) {
	            String date = r.getCell(6).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setEntretien(localDate);
            }
            
            if (r.getCell(7) != null) {
	            String date = r.getCell(7).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setCoupe(localDate);
            }
            
            if (r.getCell(8) != null) {
	            String date = r.getCell(8).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            p.setRecotte(localDate);
            }
            
            if (r.getCell(9) != null) {
	            String str = r.getCell(9).toString();
	            p.setNote(str);
            }

            /*ajouter image */
            
            plante.add(p);
            imgList.add(new PlanteImage(new ImageView(p.getImage())));
        }

        workbook.close();
        fis.close();

    }
    
    public boolean suppWarning() {
		Alert supp = new Alert(AlertType.CONFIRMATION);
		supp.setTitle("Suppression");
		supp.setHeaderText("êtes-vous sûr de vouloir supprimer ?");
		Optional<ButtonType> result=supp.showAndWait();
		return result.get()==ButtonType.OK;
	}
  
    
    public void supprimerPlante() throws IOException {
    	if(suppWarning()) {
	    	ObservableList<Plante> selectedRows, allPlante;
	    	allPlante = tableView.getItems();
	    	
	        //supprimer dans la base
	        File planteFile=new File("src/data/plantes.xlsx");
	        FileInputStream fis_plante = new FileInputStream(planteFile);
			XSSFWorkbook wb_plante = new XSSFWorkbook(fis_plante);
			XSSFSheet sh_plante = wb_plante.getSheetAt(0);
	    	
	        File mesureFile=new File("src/data/mesures.xlsx");
	        FileInputStream fis_mesure = new FileInputStream(mesureFile);
			XSSFWorkbook wb_mesure= new XSSFWorkbook(fis_mesure);
			XSSFSheet sh_mesure = wb_mesure.getSheetAt(0);
	    	
	    	// this gives us the rows that are selected
	    	selectedRows = tableView.getSelectionModel().getSelectedItems();
	    	
	    	// loop over the selected rows and remove the Plante objects from the table
	    	for (Plante plante: selectedRows) {
	    		allPlante.remove(plante);
	    		for (int rowIndex = 1; rowIndex <= sh_plante.getLastRowNum(); rowIndex++) {
	    			Row r=sh_plante.getRow(rowIndex);
					int id_base=(int)r.getCell(0).getNumericCellValue();
					int id_cur=Integer.parseInt(plante.getId());
	    			if(id_cur==id_base) {
						sh_plante.removeRow(r);
						sh_plante.shiftRows(rowIndex + 1, rowIndex + 1, -1);
					}
	    		}
	    	}
	    	
	    	FileOutputStream outputStream_m = new FileOutputStream("src/data/mesures.xlsx");
	    	wb_mesure.write(outputStream_m);
	    	wb_mesure.close();
	    	fis_mesure.close();
	    	
	    	FileOutputStream outputStream_p = new FileOutputStream("src/data/plantes.xlsx");
	    	wb_plante.write(outputStream_p);
	    	wb_plante.close();
	    	fis_plante.close();
    	}
    }



}
