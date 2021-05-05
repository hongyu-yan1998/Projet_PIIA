package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class MesureTableViewController implements Initializable{
	
	private Plante selectedPlante;
	
	//configure la table
	@FXML private TableView<Mesure> tableView;
	@FXML private TableColumn<Mesure, String> idCol;
	@FXML private TableColumn<Mesure, String> nomCol;
	@FXML private TableColumn<Mesure, String> valCol;
	@FXML private TableColumn<Mesure, String> uniteCol;
	@FXML private TableColumn<Mesure, LocalDate> dateCol;
	
	//Variables pour creer new Mesure
	@FXML private TextField nomTextField;
	@FXML private TextField valTextField;
	@FXML private TextField uniteTextField;
	@FXML private DatePicker dateDatePicker;
	@FXML private Button newMesureBtn;
	@FXML private Label nomPlante;
	
	//suppression
	@FXML private Button delMesureBtn;
	
	//Recherche
	@FXML private TextField searchField ;
	@FXML private ObservableList<Mesure> mesuresList = FXCollections.observableArrayList(); 
	
	//Enregistre la table
	@FXML private Button updateTableMesureBtn;
	
	//Retourne à la page precedente
	@FXML private Button retourBtn;
	private boolean plante_detail;
	private boolean plante_edit;
	private boolean plante_ajout;
	
	/**
	 * Update the mesures corresponding to the plant
	 * @throws IOException 
	 * */
	public void initMesure(Plante pl) throws IOException {
		this.selectedPlante=pl;
		this.nomPlante.setText(pl.getNom());
		File mesureFile=new File("src/data/mesures.xlsx");
		FileInputStream fis;
		
		fis = new FileInputStream(mesureFile);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (Row r : sheet) {
		       if(r.getCell(4)!=null && pl.getId().equals(r.getCell(4).toString())) {
		    	   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d-MMM-yyyy][yyyy-MM-dd]");
			       String date = r.getCell(3).toString();
			       LocalDate localDate = LocalDate.parse(date, formatter);
			       this.mesuresList.add(new Mesure(r.getCell(5).toString(),r.getCell(0).toString(),r.getCell(1).toString() , r.getCell(2).toString(), localDate));
		       }
		}
		workbook.close();
		fis.close();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//set up the cols in the table
		idCol.setCellValueFactory(new PropertyValueFactory<Mesure, String>("Id"));
		nomCol.setCellValueFactory(new PropertyValueFactory<Mesure, String>("Nom"));
		valCol.setCellValueFactory(new PropertyValueFactory<Mesure, String>("Valeur"));
		uniteCol.setCellValueFactory(new PropertyValueFactory<Mesure, String>("Unite"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Mesure, LocalDate>("Date"));
		
		tableView.setItems(this.mesuresList);
		
        //Update the table to allow for the first and last name fields
        //to be editable
        tableView.setEditable(true);
        nomCol.setCellFactory(TextFieldTableCell.forTableColumn());
        valCol.setCellFactory(TextFieldTableCell.forTableColumn());
        uniteCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        //permet seulement les ints ou floats 
		valTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d+?\\.?\\d*")) {
		        	valTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
        
        //This will allow the table to select multiple rows at once
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
        //Filtre List
        FilteredList<Mesure> filteredData = new FilteredList<Mesure>(mesuresList,b-> true);
        searchField.setOnKeyReleased(e->{
        	searchField.textProperty().addListener((observable,oldValue,newValue)->{
            	filteredData.setPredicate(mesure ->{
            		if (newValue == null || newValue.isEmpty()) {
            			return true;
            		}
            		
            		String lowerCaseFilter = newValue.toLowerCase();
            		
            		if(mesure.getNom().toLowerCase().indexOf(lowerCaseFilter)!= -1) {
            			return true;
            		}else if (mesure.getUnite().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            			return true;
            		}else if(String.valueOf(mesure.getValeur()).indexOf(lowerCaseFilter)!=-1) {
            			return true;
            		}else 
            			return false;
            	});
            });
            tableView.setItems(filteredData);
            SortedList<Mesure> sortedData = new SortedList<Mesure>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);
        });
        

	}
	
	/**
	 * Ajouter une nouvelle mesure
	 * @throws IOException 
	 * */
	public void newMesureButtonClick() throws IOException {
		//Warning s'affiche s'il y a une case est empty
		if (nomTextField.getText().trim().isEmpty()||valTextField.getText().trim().isEmpty()||
				uniteTextField.getText().trim().isEmpty()||dateDatePicker.getValue()==null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Required Fields Empty");
            alert.setContentText("vous n'avez pas rempli tous les cases");
            alert.showAndWait();
		}else {
			String id= String.valueOf(tableView.getItems().size()+1);
			
	        Mesure newMesure = new Mesure(id,nomTextField.getText(),
	                valTextField.getText(),uniteTextField.getText(),
	                dateDatePicker.getValue());
	        tableView.getItems().add(newMesure);
	        File mesureFile=new File("src/data/mesures.xlsx");
	        FileInputStream fis = new FileInputStream(mesureFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

			Row row = sheet.createRow(sheet.getLastRowNum()+1);
			row.createCell(0).setCellValue(nomTextField.getText());
			row.createCell(1).setCellValue(valTextField.getText());
			row.createCell(2).setCellValue(uniteTextField.getText());
			row.createCell(3).setCellValue(dateDatePicker.getValue().toString());
			row.createCell(4).setCellValue(selectedPlante.getId());
			row.createCell(5).setCellValue(id);

			FileOutputStream outputStream = new FileOutputStream("src/data/mesures.xlsx");
	        workbook.write(outputStream);
	        workbook.close();
	        fis.close();
		}
	}
	
	public boolean suppWarning() {
		Alert supp = new Alert(AlertType.CONFIRMATION);
		supp.setTitle("Suppression");
		supp.setHeaderText("êtes-vous sûr de vouloir supprimer ?");
		Optional<ButtonType> result=supp.showAndWait();
		return result.get()==ButtonType.OK;
	}
	
	public void deleteMesureBtn() throws IOException {
		if(suppWarning()) {
	        ObservableList<Mesure> selectedRows, desMesures;
	        desMesures= tableView.getItems();
	        
	        //supprimer dans la base
	        File mesureFile=new File("src/data/mesures.xlsx");
	        FileInputStream fis = new FileInputStream(mesureFile);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = workbook.getSheetAt(0);

	        //this gives us the rows that were selected
	        selectedRows = tableView.getSelectionModel().getSelectedItems();
	        for(Mesure m:selectedRows) {
	    		desMesures.remove(m);
	    		for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
	    			Row r=sheet.getRow(rowIndex);
					if(selectedPlante.getId().equals(r.getCell(4).toString())&&m.getId().equals(r.getCell(5).toString())) {
						sheet.removeRow(r);
	    	    		sheet.shiftRows(rowIndex + 1, rowIndex + 1, -1);
					}
	    		}
	        }

			FileOutputStream outputStream = new FileOutputStream("src/data/mesures.xlsx");
	        workbook.write(outputStream);
	        workbook.close();
	        fis.close();
		}

	}
	
	public void retourPagePreced() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		if(isPlante_detail()) {
	    	loader.setLocation(getClass().getResource("view/DetailPlante.fxml"));
		    Parent detailPlante = loader.load();
	        Stage window = (Stage) retourBtn.getScene().getWindow();
	        setPlante_detail(false);
	        ControlDetail controller = loader.getController();
	        controller.initData(selectedPlante);
	        window.setTitle("Ajout d'une plante");
	        window.setScene(new Scene(detailPlante));
	        window.centerOnScreen();
	        window.show();
		}
		if(isPlante_edit()) {
			loader.setLocation(getClass().getResource("view/EditerPlante.fxml"));
	    	Parent editPlante = loader.load();
	        Stage window = (Stage) retourBtn.getScene().getWindow();
	        setPlante_edit(false);
	        ControlEditer controller = loader.getController();
	        controller.initData(selectedPlante);
	        window.setTitle("Ajout d'une plante");
	        window.setScene(new Scene(editPlante));
	        window.centerOnScreen();
	        window.show();
	        
		}

	}
	
	
	/***************************************************************
	 * Double-cliquer sur une cellule pour changer les valeurs
	 * ************************************************************/
	public void changeNomCellEvent(CellEditEvent edditedCell) {
		Mesure mesureSelected =  tableView.getSelectionModel().getSelectedItem();
        mesureSelected.setNom(edditedCell.getNewValue().toString());
	}
	
	public void changeValCellEvent(CellEditEvent edditedCell) {
		Mesure mesureSelected =  tableView.getSelectionModel().getSelectedItem();
        mesureSelected.setValeur(edditedCell.getNewValue().toString());
	}
	
	public void changeUniteCellEvent(CellEditEvent edditedCell) {
		Mesure mesureSelected =  tableView.getSelectionModel().getSelectedItem();
        mesureSelected.setUnite(edditedCell.getNewValue().toString());
	}

	
	public boolean isPlante_detail() {
		return plante_detail;
	}

	public void setPlante_detail(boolean plante_detail) {
		this.plante_detail = plante_detail;
	}

	public boolean isPlante_edit() {
		return plante_edit;
	}

	public void setPlante_edit(boolean plante_edit) {
		this.plante_edit = plante_edit;
	}

	public boolean isPlante_ajoute() {
		return plante_ajout;
	}

	public void setPlante_ajoute(boolean flag) {
		this.plante_ajout = flag;
	}
	
}
