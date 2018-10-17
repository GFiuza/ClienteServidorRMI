import java.rmi.*;

public interface ClienteServidor extends Remote {

    boolean escrita(String caminho, String dado) throws RemoteException;

    String leitura(String caminho) throws RemoteException;

}
