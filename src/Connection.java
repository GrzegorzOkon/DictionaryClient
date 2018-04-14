import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa obs³uguje wysy³anie i odbieranie wiadomoœci od serwera
 */
public class Connection extends Thread {
	
	DictionaryClientView widokKlientaS³ownikowego;
	
    ServerSocket gniazdoSerweraLokalnego;
    ObjectInputStream ois;
    
    public Connection(DictionaryClientView widokKlientaS³ownikowego) {
    	
    	this.widokKlientaS³ownikowego = widokKlientaS³ownikowego;
    }
    
    /**
     * Tworzy serwer na porcie na którym ma byæ odebrana wiadomoœæ
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
     * Wysy³a wiadomoœæ do serwera
     * 
     * @param tekst treœæ wiadomoœci z³o¿ona w GUI w formie np. "en,kot,5555"
     */
    public void wyœlijWiadomoœæ(String tekst) {
    	
    	try(Socket gniazdoSerweraZdalnego = new Socket("localhost", 1300); 
    			ObjectOutputStream oos = new ObjectOutputStream(gniazdoSerweraZdalnego.getOutputStream());) {

    		oos.writeObject(tekst);
    		oos.flush();
    	} catch (Exception ex) {
		
    		ex.printStackTrace();
    	} 
    }
    
    /**
     * S³u¿y do uruchomienia gniazda serwera w oddzielnym w¹tku w celu nie blokowania dzia³ania programu podczas odbierania wiadomoœci
     */
    @Override
    public void run() {
        while (true) {
        	
            try (Socket gniazdo = gniazdoSerweraLokalnego.accept()) {	//metoda blokuj¹ca oczekuj¹ca na po³¹czenie
            	         	
            	ois = new ObjectInputStream(gniazdo.getInputStream()); 
                String wiadomoœæOdebrana = (String) ois.readObject();
                
                widokKlientaS³ownikowego.wyœwietlTekst(wiadomoœæOdebrana);
            } catch (IOException | ClassNotFoundException ex) {
            	
            }
        }
    }  
}
