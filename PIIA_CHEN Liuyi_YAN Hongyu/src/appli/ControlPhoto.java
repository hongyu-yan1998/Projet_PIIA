package appli;

import java.io.File;
import java.io.FileInputStream;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ControlPhoto {
	private ObservableList<PlanteImage> imgList = FXCollections.observableArrayList();
	private Plante selectedPlante;
	
	@FXML private TableView<PlanteImage> tableView;
	@FXML private TableColumn<PlanteImage, ImageView> imageColumn;
	
	@FXML private Button retourBut;
	
	
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
     * Nous mettons à jour les histoires des photos
     * */
    public void initData(Plante plante) throws IOException {
    	selectedPlante = plante;
    	File File=new File("src/data/photos.xlsx");
        FileInputStream fis = new FileInputStream(File);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        int id=Integer.parseInt(selectedPlante.getId());
        
        for(Row r:sheet) {
        	if((int)r.getCell(0).getNumericCellValue()==id) {
        		String str=r.getCell(1).toString();
            	imgList.add(new PlanteImage(new ImageView(new Image(str))));
        	}
        }
        
	    imageColumn.setCellValueFactory(new PropertyValueFactory<PlanteImage, ImageView>("image"));
		tableView.setItems(imgList);
		
		workbook.close();
        fis.close();
	}
	
}