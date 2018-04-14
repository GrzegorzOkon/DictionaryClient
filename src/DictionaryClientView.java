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
 * Klasa tworzy interfejs u�ytkownika w javafx oraz przeprowadza wst�pn� walidacj� wprowadzonych danych
 * 
 */
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
        kontenerSiatki.setPadding(new Insets(0, 0, 10, 0)); 
        
        kontenerGlowny.setTop(kontenerSiatki);
        
        poleWyszukiwania = new TextArea();
        kontenerGlowny.setCenter(poleWyszukiwania);
        
        konternerDolny = new BorderPane();
        konternerDolny.setPadding(new Insets(10, 0, 0, 0)); 
        
        kontenerPrzyciskow = new HBox(16);
        
        pobierzT�umaczenie = new Button("Pobierz");
        
        kontenerPrzyciskow.getChildren().add(pobierzT�umaczenie);
        
        pobierzT�umaczenie.setOnAction((event) -> {   //przypisuje dzia�anie do przycisku
        	if (poleJ�zyk�w.getSelectionModel().isEmpty() == false) {	//je�eli zosta� dokonany wyb�r j�zyka
        		
        		String portKlienta = polePort�w.getText();
        		
        		if (walidujDaneWej�ciowe(portKlienta)) {	//sprawdza poprawno�� numeru portu na wej�ciu
        			
        			po��czenie = new Connection(this);
        			po��czenie.zarezerwujPort(Integer.valueOf(portKlienta));	//tworzy serwer nasluchuj�cy odpowiedzi
        			po��czenie.start();	  //uruchamia serwer w nowym w�tku
        			
        			po��czenie.wy�lijWiadomo��(poleJ�zyk�w.getSelectionModel().getSelectedItem().toString() + "," + poleWyszukiwania.getText() + "," + portKlienta);
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
     * Waliduje dane wej�ciowe - sprawdza czy wpisano liczb� z zakresu 49152 - 65535
     * 
     * @param portNiezweryfikowany tekst wpisany w polu portu
     * 
     * @return true dla poprawnego numeru portu - liczba w podanym zakresie, w przeciwnym razie false
     */
    private boolean walidujDaneWej�ciowe(String portNiezweryfikowany) {
    	
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
    
    /**
     * Wy�wietla tekst w oknie interfejsu
     * 
     * @param tekst tekst do wy�wietlenia
     */
	public void wy�wietlTekst(String tekst) {
		
		poleWyszukiwania.clear();
		poleWyszukiwania.appendText(tekst);
	}
}