import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class DictionaryClientView extends Application { 
	
    private Scene scena; 
    private BorderPane kontenerGlowny, konternerDolny;
    private GridPane kontenerSiatki;
    private HBox kontenerPrzyciskow; 
    private ObservableList<String> jêzyki;
    private ComboBox<String> poleJêzyków;
    private TextField polePortów;
    private TextArea poleWyszukiwania;
    private Button pobierzT³umaczenie;

    Connection po³¹czenie;
    
    // =============================================================================
    
    private void prepareScene(Stage primaryStage) {    
    	
        kontenerGlowny = new BorderPane();
        kontenerGlowny.setPadding(new Insets(15, 15, 15, 15));  //tworzy odstêp wokó³ konteneru
        
        jêzyki = FXCollections.observableArrayList("de", "en", "fr");  
        poleJêzyków = new ComboBox(jêzyki);
        
        polePortów = new TextField();
        polePortów.setPrefSize(120, polePortów.getHeight());
        polePortów.setPromptText("49152 - 65535");
        
        kontenerSiatki = new GridPane();
        kontenerSiatki.setVgap(4);
        kontenerSiatki.setHgap(8);
        kontenerSiatki.setPadding(new Insets(5, 5, 5, 5));
        kontenerSiatki.add(new Label("Jêzyk:"), 0, 0);
        kontenerSiatki.add(poleJêzyków, 0, 1);
        kontenerSiatki.add(new Label("Port:"), 1, 0);
        kontenerSiatki.add(polePortów, 1, 1);     
        kontenerSiatki.setPadding(new Insets(0, 0, 10, 0));  // tworzy odstêp pod kontenerem
        
        kontenerGlowny.setTop(kontenerSiatki);
        
        poleWyszukiwania = new TextArea();
        kontenerGlowny.setCenter(poleWyszukiwania);
        
        konternerDolny = new BorderPane();
        konternerDolny.setPadding(new Insets(10, 0, 0, 0));  // tworzy odstêp nad kontenerem
        
        kontenerPrzyciskow = new HBox(16);
        
        pobierzT³umaczenie = new Button("Pobierz");
        
        kontenerPrzyciskow.getChildren().add(pobierzT³umaczenie);
        
        pobierzT³umaczenie.setOnAction((event) -> {	
        	if (poleJêzyków.getSelectionModel().isEmpty() == false) {
        		
        		if (walidujDane(polePortów.getText())) {
        			po³¹czenie = new Connection();
        			
        			po³¹czenie.wyœlijWiadomoœæ(poleJêzyków.getSelectionModel().getSelectedItem().toString() + "," + poleWyszukiwania.getText());
        			wyœwietlTekst("");
        			
        			poleWyszukiwania.setText(po³¹czenie.odbierzWiadomoœæ());
        		}
        	}
		});
         
        konternerDolny.setRight(kontenerPrzyciskow);
        kontenerGlowny.setBottom(konternerDolny);
        
        scena = new Scene(kontenerGlowny, 450, 300);
    }
    
    // =============================================================================
    
    @Override
    public void start(Stage primaryStage) {   
    	
        prepareScene(primaryStage);
        
        primaryStage.setTitle("Dictionary Client");
        primaryStage.setScene(scena);
        primaryStage.show();
    }

    // =============================================================================

    public static void main(String[] args) { 
    	
          launch(args);
    }

    // =============================================================================
    
    private boolean walidujDane(String portNiezweryfikowany) {
    	
    	try {
    		
    		Integer port = Integer.valueOf(portNiezweryfikowany);
    		
        	if (port >= 49152 && port <= 65535) {
        		
        		return true;
        	} else {
        		
        		wyœwietlTekst("Nale¿y podaæ zakres portu w przedziale 49152, 65535.");
        	}
    	} catch (Exception ex) {
    		
    		wyœwietlTekst("Nie mo¿na pobraæ numeru portu.");
    	}

    	return false;
    }
    
	public void wyœwietlTekst(String tekst) {
		
		poleWyszukiwania.clear();
		poleWyszukiwania.appendText(tekst);
	}
}