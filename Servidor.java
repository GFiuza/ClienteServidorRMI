import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Servidor implements ClienteServidor {

    public Servidor() {}

    public boolean escrita(String caminho, String dado){
        try {
            FileWriter arquivo = new FileWriter(caminho, StandardCharsets.UTF_8);
            arquivo.append(dado);
            arquivo.close();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public String leitura(String caminho, int caracteres, int offset){
        try {
            FileReader arquivo = new FileReader(caminho, StandardCharsets.UTF_8);
            char[] retornochar = new char[caracteres];
            int stat = arquivo.read(retornochar, offset, caracteres);
            arquivo.close();
            if(stat == -1){
                return "Não foi possível fazer leitura";
            } else {
                return new String(retornochar);
            }
        } catch (Exception e){
            return "Exceção levantada";
        }
    }

    public static void main(String args[]) {
        try {
            Servidor obj = new Servidor();
            ClienteServidor stub = (ClienteServidor) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("ClienteServidor", stub);
            System.out.println("Servidor pronto!");
        } catch (Exception e) {
            System.err.println("Capturando exceção no Servidor: " + e.toString());
            e.printStackTrace();
        }
    }

}
