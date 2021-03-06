import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa obsługuje wysyłanie i odbieranie wiadomości od serwera
 */
public class Connection extends Thread {
	
	DictionaryClientView widokKlientaSłownikowego;
	
    ServerSocket gniazdoSerweraLokalnego;
    ObjectInputStream ois;
    
    public Connection(DictionaryClientView widokKlientaSłownikowego) {
    	
    	this.widokKlientaSłownikowego = widokKlientaSłownikowego;
    }
    
    /**
     * Tworzy serwer na porcie na którym ma być odebrana wiadomość
     * 
     * @param portKlienta numer portu potrzebny do utworzenia gniazda serwera
     */
    public void zarezerwujPort(int portKlienta) {
    	
    	try {
    		
    		gniazdoSerweraLokalnego = new ServerSocket(portKlienta);
    	} catch (IOException ex) {
    		
    	}
    }
    
    /**
     * Wysyła wiadomość do serwera
     * 
     * @param tekst treść wiadomości złożona w GUI w formie np. "en,kot,5555"
     */
    public void wyślijWiadomość(String tekst) {
    	
    	try(Socket gniazdoSerweraZdalnego = new Socket("localhost", 1300); 
    			ObjectOutputStream oos = new ObjectOutputStream(gniazdoSerweraZdalnego.getOutputStream());) {

    		oos.writeObject(tekst);
    		oos.flush();
    	} catch (Exception ex) {
		
    		ex.printStackTrace();
    	} 
    }
    
    /**
     * Służy do uruchomienia gniazda serwera w oddzielnym wątku w celu nie blokowania działania programu podczas odbierania wiadomości
     */
    @Override
    public void run() {
        while (true) {
        	
            try (Socket gniazdo = gniazdoSerweraLokalnego.accept()) {	//metoda blokująca oczekująca na połączenie
            	         	
            	ois = new ObjectInputStream(gniazdo.getInputStream()); 
                String wiadomośćOdebrana = (String) ois.readObject();
                
                widokKlientaSłownikowego.wyświetlTekst(wiadomośćOdebrana);
            } catch (IOException | ClassNotFoundException ex) {
            	
            }
        }
    }  
}
