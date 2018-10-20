import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

public class Servidor implements ClienteServidor {

    private final boolean PRIORIDADENORMAL = true;
    private boolean [] estaLendo;
    private int [] readCount;
    private Semaphore []leitura, escrita;

    public Servidor() {
        readCount= new int[] {0, 0, 0};
        estaLendo = new boolean[] {false, false, false};

        int PERMISSAOLEITURA = 3;
        leitura = new Semaphore [] {
                    new Semaphore(PERMISSAOLEITURA, true),
                    new Semaphore(PERMISSAOLEITURA, true),
                    new Semaphore(PERMISSAOLEITURA, true)};

        int PERMISSAOESCRITA = 1;
        escrita = new Semaphore[] {
                    new Semaphore(PERMISSAOESCRITA, true),
                    new Semaphore(PERMISSAOESCRITA, true),
                    new Semaphore(PERMISSAOESCRITA, true)};
    }

    private void escritaArquivo(String caminho, String dado) throws IOException {
        FileWriter arquivo = new FileWriter(caminho, StandardCharsets.UTF_8);
        arquivo.append(dado);
        arquivo.close();
    }

    public boolean escrita(String caminho, String dado) {

        long [] ultimoTempo= new long[] {System.currentTimeMillis(), System.currentTimeMillis(), System.currentTimeMillis()};
        try {
            int num = Character.getNumericValue(caminho.charAt(3)) - 1;
            //verifica se o arquivo existe
            if (num <= 2 && num >= 0) {
                //verifica readcount ou define que esta lendo
                if(this.PRIORIDADENORMAL){
                    this.escrita[num].acquire(1);
                    this.leitura[num].acquire(3);
                    //this.estaLendo[num]=true;
                }
                else{
                    int SLEEP_TIME = 2000;
                    do{
						ultimoTempo[num]=System.currentTimeMillis();
                        while(System.currentTimeMillis()<=ultimoTempo[num]+SLEEP_TIME){
                            Thread.sleep(1);
                        }
                        ultimoTempo[num]=System.currentTimeMillis();
                    }while(this.readCount[num]>0);
                    this.escrita[num].acquire(1);
                    this.leitura[num].acquire(3);
                }

                //faz a escrita
                escritaArquivo(caminho, dado);

                //tempo de escrita
                Thread.sleep(1000);
				
                //libera o arquivo 1
                if(this.PRIORIDADENORMAL){
                    this.estaLendo[num]=false;
                }

                this.leitura[num].release(3);
                this.escrita[num].release(1);

                return true;
            }

            return false;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private String leituraArquivo(String caminho) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(caminho));
            return new String(encoded, StandardCharsets.UTF_8);
        }
        catch (Exception e){
            return "Não foi possivel fazer leitura";
        }
    }
    public String leitura(String caminho){
        try {
            int num = Character.getNumericValue(caminho.charAt(3)) - 1;
            if (num <= 2 && num >= 0){
                String saida;
                //trava o arquivo 1 para a leitura
                if(this.PRIORIDADENORMAL){
                    /*while(this.estaLendo[num]){
						Thread.sleep(1);
					}*/
                    this.escrita[num].acquire(1);
                    this.leitura[num].acquire(1);

                }
                else {
                    //verifica readcount
                    this.readCount[num] += 1;
                    if(this.readCount[num] == 1) {
                        this.escrita[num].acquire(1);
                    }
                }
                //faz a leitura
                saida = leituraArquivo(caminho);


                //tempo de leitura
                Thread.sleep(500);
				
                //libera o arquivo 1
                this.leitura[num].release(1);
                if(PRIORIDADENORMAL){
                    this.escrita[num].release(1);

                }
                else {
                    //verifica readcount
                    this.readCount[num] -= 1;
                    if(this.readCount[num] == 0) {
                        this.escrita[num].release(1);
                    }
                }
                return saida;
            } else
                return "Arquivo invalido";
        } catch (Exception e){
            e.printStackTrace();
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
