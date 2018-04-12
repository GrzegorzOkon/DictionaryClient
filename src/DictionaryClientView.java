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
    private ObservableList<String> j�zyki;
    private ComboBox<String> poleJ�zyk�w;
    private TextField polePort�w;
    private TextArea poleWyszukiwania;
    private Button pobierzT�umaczenie;

    Connection po��czenie;
    
    // =============================================================================
    
    private void prepareScene(Stage primaryStage) {    
    	
        kontenerGlowny = new BorderPane();
        kontenerGlowny.setPadding(new Insets(15, 15, 15, 15));  //tworzy odst�p wok� konteneru
        
        j�zyki = FXCollections.observableArrayList("de", "en", "fr");  
        poleJ�zyk�w = new ComboBox(j�zyki);
        
        polePort�w = new TextField();
        polePort�w.setPrefSize(120, polePort�w.getHeight());
        polePort�w.setPromptText("49152 - 65535");
        
        kontenerSiatki = new GridPane();
        kontenerSiatki.setVgap(4);
        kontenerSiatki.setHgap(8);
        kontenerSiatki.setPadding(new Insets(5, 5, 5, 5));
        kontenerSiatki.add(new Label("J�zyk:"), 0, 0);
        kontenerSiatki.add(poleJ�zyk�w, 0, 1);
        kontenerSiatki.add(new Label("Port:"), 1, 0);
        kontenerSiatki.add(polePort�w, 1, 1);     
        kontenerSiatki.setPadding(new Insets(0, 0, 10, 0));  // tworzy odst�p pod kontenerem
        
        kontenerGlowny.setTop(kontenerSiatki);
        
        poleWyszukiwania = new TextArea();
        kontenerGlowny.setCenter(poleWyszukiwania);
        
        konternerDolny = new BorderPane();
        konternerDolny.setPadding(new Insets(10, 0, 0, 0));  // tworzy odst�p nad kontenerem
        
        kontenerPrzyciskow = new HBox(16);
        
        pobierzT�umaczenie = new Button("Pobierz");
        
        kontenerPrzyciskow.getChildren().add(pobierzT�umaczenie);
        
        pobierzT�umaczenie.setOnAction((event) -> {	
        	if (poleJ�zyk�w.getSelectionModel().isEmpty() == false) {
        		
        		if (walidujDane(polePort�w.getText())) {
        			po��czenie = new Connection();
        			
        			po��czenie.wy�lijWiadomo��(poleJ�zyk�w.getSelectionModel().getSelectedItem().toString() + "," + poleWyszukiwania.getText());
        			wy�wietlTekst("");
        			
        			poleWyszukiwania.setText(po��czenie.odbierzWiadomo��());
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
        		
        		wy�wietlTekst("Nale�y poda� zakres portu w przedziale 49152, 65535.");
        	}
    	} catch (Exception ex) {
    		
    		wy�wietlTekst("Nie mo�na pobra� numeru portu.");
    	}

    	return false;
    }
    
	public void wy�wietlTekst(String tekst) {
		
		poleWyszukiwania.clear();
		poleWyszukiwania.appendText(tekst);
	}
}