import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Random;

public class Cliente {

    private Cliente() {}

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : null;//args[0];

        Scanner entrada = new Scanner(System.in);
            
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClienteServidor stub = (ClienteServidor) registry.lookup("ClienteServidor");


			String[] tiposCaracteres = new String[3];
			tiposCaracteres[0] = "abcdefghijklmnopqrstuvwxyz";
			tiposCaracteres[1] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			tiposCaracteres[2] = "0123456789";
			
			String escrever = new String();
			
			String arquivo;
			
			Random gerador = new Random();
			
			int id = Integer.parseInt(args[0]);
			
			for(int i=0;i<10;i++) {
				int escolha = gerador.nextInt(3) + 1;

				arquivo = "arq" + escolha + ".txt";
				
				escolha = gerador.nextInt(2) + 1;
				
				if(escolha == 1) {
					int tamanho = gerador.nextInt(100) + 1;
					for(int j=0;j<tamanho;j++) {
						int casa = gerador.nextInt(3);
						int index;
						if(casa == 2){
							index = gerador.nextInt(10);
						}
						else {
							index = gerador.nextInt(26);
						}
						escrever += tiposCaracteres[casa].charAt(index);
					}
					System.out.println("Palavra a ser escrita: " + escrever);
						
	                boolean resposta = stub.escrita(arquivo, escrever,id);

	                if(resposta){
	                    System.out.println("Escrita bem sucedida");
	                }
				}
				else {
					String sresposta = stub.leitura(arquivo,id);
                    System.out.println(sresposta);
				}
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
			entrada.nextInt();
        }
    }
}
