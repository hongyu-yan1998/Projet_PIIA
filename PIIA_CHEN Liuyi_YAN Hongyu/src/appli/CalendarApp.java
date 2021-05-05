package appli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.scene.layout.HBox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


import javafx.event.Event;
import javafx.event.EventHandler;


public class CalendarApp extends Application {
	
	private static Stage guiStage;
	private static Scene guiScene;
	private static Calendar guiGardins;
	
    /**
     * lancement du program
     **/
    public static void main(String[] args) {
    	disableAccessWarnings();
        launch(args);
    }

    public static Stage getStage() {
        return guiStage;
    }
    
    public static Scene getScene() {
        return guiScene;
    }
    
    public static Calendar getJardins() {
        return guiGardins;
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
    	guiStage = primaryStage;
    	BorderPane root = new BorderPane();
        CalendarView calendarView = new CalendarView(); 
        calendarView.setShowAddCalendarButton(false);
        root.setCenter(calendarView);
        
        File file = new File("src/image/plante.png");
		String string = file.toURI().toString();
        ImageView imgPlante = new ImageView(new Image(string));
        ImageView imgPlanteR = new ImageView(new Image(string));
        Button changeListePlante = new Button("Liste de plantes");
        imgPlante.setFitWidth(48);
        imgPlante.setFitHeight(48);
        imgPlanteR.setFitWidth(48);
        imgPlanteR.setFitHeight(48);
        
        HBox buttonHBox = new HBox(imgPlante, changeListePlante, imgPlanteR);
        buttonHBox.setAlignment(Pos.CENTER);
		root.setTop(buttonHBox);
        
        //Deux categories de Calendar
        Calendar ecoles = new Calendar("Evenements scolaires"); 
        Calendar jardins = new Calendar("Evenements jardiniers");

        ecoles.setStyle(Style.STYLE2); 
        jardins.setStyle(Style.STYLE1);
        guiGardins = jardins;

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); 
        myCalendarSource.getCalendars().addAll(ecoles, jardins);
                
        loadBase(jardins);

        calendarView.getCalendarSources().addAll(myCalendarSource); 

        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        
        
        changeListePlante.setOnAction((e)->{
			Parent plante;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("view/Plante.fxml"));
				plante = loader.load();
		        
		        primaryStage.setScene(new Scene(plante));
		        primaryStage.centerOnScreen();
		        primaryStage.show();
		        //primaryStage.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	     
		});
		
        
        /*
        changeListePlante.setPickOnBounds(true);
        changeListePlante.setOnMouseClicked(new EventHandler() {
			@Override
			public void handle(Event arg0) {
				// TODO Auto-generated method stub
				Parent plante;
				try {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("view/Plante.fxml"));
					plante = loader.load();
			        
			        primaryStage.setScene(new Scene(plante));
			        primaryStage.centerOnScreen();
			        primaryStage.show();
			        //primaryStage.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}	     
			}
        });
        */
        
  
    	//Parent root = FXMLLoader.load(getClass().getResource("view/Plante.fxml"));

        Scene scene = new Scene(root);
        guiScene = scene;
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
    
	/**
	 * creation d'un evenement dans le calendar jardin
	 * */
    public static void createEvent(Calendar jardins,LocalDate date,String str) {
        Entry<String> entry = new Entry<>(str);
        entry.setInterval(LocalDate.now());
        entry.changeStartDate(date);
        entry.changeEndDate(date);
        Random rand=new Random();
        int randomNum = rand.nextInt(8) + 9;
        entry.changeStartTime(LocalTime.of(randomNum,30));
        entry.changeEndTime(LocalTime.of(randomNum+1,30));
        jardins.addEntry(entry);
    }
	
    /**
     * Nous importons les informations stockees dans notre base dans le calendrier
     * */
	public static void loadBase(Calendar jardins) throws IOException {
		File mesureFile=new File("src/data/plantes.xlsx");
        FileInputStream fis = new FileInputStream(mesureFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        
        for(Row r:sheet) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d-MMM-yyyy][yyyy-MM-dd]");
            String plant_nom=r.getCell(2).toString();
        	
            if (r.getCell(3) != null) {
            	String date = r.getCell(3).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Rempotage");
	        }
            
            if (r.getCell(4) != null) {
	            String date = r.getCell(4).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Plantation");
            }
            
            if (r.getCell(5)!= null) {
                String date = r.getCell(5).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Arronsage");
            }
            
            if (r.getCell(6) != null) {
	            String date = r.getCell(6).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Entretien");
            }
            
            if (r.getCell(7) != null) {
	            String date = r.getCell(7).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Coupe");
            }
            
            if (r.getCell(8) != null) {
	            String date = r.getCell(8).toString();
	            LocalDate localDate = LocalDate.parse(date, formatter);
	            createEvent(jardins,localDate,plant_nom+": Recotte");
            }
        	
        }
        
        workbook.close();
        fis.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void disableAccessWarnings() {
       try {
    	   Class unsafeClass = Class.forName("sun.misc.Unsafe");
	       Field field = unsafeClass.getDeclaredField("theUnsafe");
           field.setAccessible(true);
           Object unsafe = field.get(null);

           Method putObjectVolatile = unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
           Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);

           Class loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
           Field loggerField = loggerClass.getDeclaredField("logger");
           Long offset = (Long) staticFieldOffset.invoke(unsafe, loggerField);
           putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
       } catch (Exception ignored) {}
	  }

}