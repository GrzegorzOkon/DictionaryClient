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

/**
 * Klasa tworzy interfejs użytkownika w javafx oraz przeprowadza wstępną walidację wprowadzonych danych
 * 
 */
public class DictionaryClientView extends Application { 
	
    private Scene scena; 
    private BorderPane kontenerGlowny, konternerDolny;
    private GridPane kontenerSiatki;
    private HBox kontenerPrzyciskow; 
    private ObservableList<String> języki;
    private ComboBox<String> poleJęzyków;
    private TextField polePortów;
    private TextArea poleWyszukiwania;
    private Button pobierzTłumaczenie;

    Connection połączenie;
    
    // =============================================================================
    
    private void prepareScene(Stage primaryStage) {    
    	
        kontenerGlowny = new BorderPane();
        kontenerGlowny.setPadding(new Insets(15, 15, 15, 15));  //tworzy odstęp wokół konteneru
        
        języki = FXCollections.observableArrayList("de", "en", "fr");  
        poleJęzyków = new ComboBox(języki);
        
        polePortów = new TextField();
        polePortów.setPrefSize(120, polePortów.getHeight());
        polePortów.setPromptText("49152 - 65535");
        
        kontenerSiatki = new GridPane();
        kontenerSiatki.setVgap(4);
        kontenerSiatki.setHgap(8);
        kontenerSiatki.setPadding(new Insets(5, 5, 5, 5));
        kontenerSiatki.add(new Label("Język:"), 0, 0);
        kontenerSiatki.add(poleJęzyków, 0, 1);
        kontenerSiatki.add(new Label("Port:"), 1, 0);
        kontenerSiatki.add(polePortów, 1, 1);     
        kontenerSiatki.setPadding(new Insets(0, 0, 10, 0)); 
        
        kontenerGlowny.setTop(kontenerSiatki);
        
        poleWyszukiwania = new TextArea();
        kontenerGlowny.setCenter(poleWyszukiwania);
        
        konternerDolny = new BorderPane();
        konternerDolny.setPadding(new Insets(10, 0, 0, 0)); 
        
        kontenerPrzyciskow = new HBox(16);
        
        pobierzTłumaczenie = new Button("Pobierz");
        
        kontenerPrzyciskow.getChildren().add(pobierzTłumaczenie);
        
        pobierzTłumaczenie.setOnAction((event) -> {   //przypisuje działanie do przycisku
        	if (poleJęzyków.getSelectionModel().isEmpty() == false) {	//jeżeli został dokonany wybór języka
        		
        		String portKlienta = polePortów.getText();
        		
        		if (walidujDaneWejściowe(portKlienta)) {	//sprawdza poprawność numeru portu na wejściu
        			
        			połączenie = new Connection(this);
        			połączenie.zarezerwujPort(Integer.valueOf(portKlienta));	//tworzy serwer nasluchujący odpowiedzi
        			połączenie.start();	  //uruchamia serwer w nowym wątku
        			
        			połączenie.wyślijWiadomość(poleJęzyków.getSelectionModel().getSelectedItem().toString() + "," + poleWyszukiwania.getText() + "," + portKlienta);
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

    /**
     * Waliduje dane wejściowe - sprawdza czy wpisano liczbę z zakresu 49152 - 65535
     * 
     * @param portNiezweryfikowany tekst wpisany w polu portu
     * 
     * @return true dla poprawnego numeru portu - liczba w podanym zakresie, w przeciwnym razie false
     */
    private boolean walidujDaneWejściowe(String portNiezweryfikowany) {
    	
    	try {
    		
    		Integer port = Integer.valueOf(portNiezweryfikowany);
    		
        	if (port >= 49152 && port <= 65535) {
        		
        		return true;
        	} else {
        		
        		wyświetlTekst("Należy podać zakres portu w przedziale 49152, 65535.");
        	}
    	} catch (Exception ex) {
    		
    		wyświetlTekst("Nie można pobrać numeru portu.");
    	}

    	return false;
    }
    
    /**
     * Wyświetla tekst w oknie interfejsu
     * 
     * @param tekst tekst do wyświetlenia
     */
	public void wyświetlTekst(String tekst) {
		
		poleWyszukiwania.clear();
		poleWyszukiwania.appendText(tekst);
	}
}