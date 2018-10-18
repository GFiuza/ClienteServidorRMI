import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente {

    private Cliente() {}

    public static void main(String[] args) {
        System.out.println(args[0]);
        String host = (args.length < 1) ? null : null;//args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");

            Scanner entrada = new Scanner(System.in);

            String arquivo;

            BufferedReader arquivoComandos = new BufferedReader(new FileReader(args[0]));
            String linha=arquivoComandos.readLine();
            while(linha!=null){
                //define o arquivo que vai ler
                arquivo = "arq" + linha + ".txt";

                linha=arquivoComandos.readLine();

                //define o comando que vai ser feito
                //leitura
                if(linha.equals("2")){
                    String sresposta = stub.leitura(arquivo);
                    System.out.println(sresposta);

                }
                //escrita
                else{
                    //le os dados a serem escritos
                    linha=arquivoComandos.readLine();

                    boolean resposta = stub.escrita(arquivo, linha);

                    if(resposta){
                        System.out.println("Escrita bem sucedida");
                    }
                }

                linha=arquivoComandos.readLine();
            }

            entrada.nextInt();
            /*int ainda = 1;
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
            }*/
        } catch (Exception e) {
            System.err.println("Capturando a exceção no Cliente: " + e.toString());
            e.printStackTrace();
        }
    }
}
