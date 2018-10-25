import java.io.IOException;
import java.rmi.*;

public interface ClienteServidor extends Remote {

    boolean escrita(String caminho, String dado, int id) throws RemoteException;

    String leitura(String caminho, int id) throws RemoteException;

}
