import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {

    private Cliente() {}

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");

            Scanner entrada = new Scanner(System.in);
            int ainda = 1;
            while (ainda == 1)
            {
                System.out.println("Deseja ler do arquivo 1, 2 ou 3?");
                int arq = entrada.nextInt();

                String arquivo = "arq" + arq + ".txt";

                System.out.println("1 para escrita, 2 para leitura");
                int operacao = entrada.nextInt();
                if(operacao == 1){
                    System.out.println("Dado de entrada:");
                    String dado = entrada.next();

                    boolean resposta = stub.escrita(arquivo, dado);

                    if(resposta){
                        System.out.println("Escrita bem sucedida");
                    }
                } else if (operacao== 2){
                    String sresposta = stub.leitura(arquivo);
                    System.out.println(sresposta);
                }
                System.out.println("Ainda deseja fazer mais operações? 1 sim, 0 não.");
                ainda = entrada.nextInt();
            }
        } catch (Exception e) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}
