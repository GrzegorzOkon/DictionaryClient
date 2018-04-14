import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa obs�uguje wysy�anie i odbieranie wiadomo�ci od serwera
 */
public class Connection extends Thread {
	
	DictionaryClientView widokKlientaS�ownikowego;
	
    ServerSocket gniazdoSerweraLokalnego;
    ObjectInputStream ois;
    
    public Connection(DictionaryClientView widokKlientaS�ownikowego) {
    	
    	this.widokKlientaS�ownikowego = widokKlientaS�ownikowego;
    }
    
    /**
     * Tworzy serwer na porcie na kt�rym ma by� odebrana wiadomo��
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
     * Wysy�a wiadomo�� do serwera
     * 
     * @param tekst tre�� wiadomo�ci z�o�ona w GUI w formie np. "en,kot,5555"
     */
    public void wy�lijWiadomo��(String tekst) {
    	
    	try(Socket gniazdoSerweraZdalnego = new Socket("localhost", 1300); 
    			ObjectOutputStream oos = new ObjectOutputStream(gniazdoSerweraZdalnego.getOutputStream());) {

    		oos.writeObject(tekst);
    		oos.flush();
    	} catch (Exception ex) {
		
    		ex.printStackTrace();
    	} 
    }
    
    /**
     * S�u�y do uruchomienia gniazda serwera w oddzielnym w�tku w celu nie blokowania dzia�ania programu podczas odbierania wiadomo�ci
     */
    @Override
    public void run() {
        while (true) {
        	
            try (Socket gniazdo = gniazdoSerweraLokalnego.accept()) {	//metoda blokuj�ca oczekuj�ca na po��czenie
            	         	
            	ois = new ObjectInputStream(gniazdo.getInputStream()); 
                String wiadomo��Odebrana = (String) ois.readObject();
                
                widokKlientaS�ownikowego.wy�wietlTekst(wiadomo��Odebrana);
            } catch (IOException | ClassNotFoundException ex) {
            	
            }
        }
    }  
}
