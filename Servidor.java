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

    private int readCount1,readCount2,readCount3;
    private boolean estaLendo1,estaLendo2,estaLendo3;
    private final int PERMISSAOLEITURA = 3;
    private final int PERMISSAOESCRITA = 1;
    private final boolean PRIORIDADENORMAL = true;
    private Semaphore leitura1 = new Semaphore(PERMISSAOLEITURA, true) ,escrita1 = new Semaphore(PERMISSAOESCRITA, true) ;
    private Semaphore leitura2 = new Semaphore(PERMISSAOLEITURA, true) ,escrita2 = new Semaphore(PERMISSAOESCRITA, true) ;
    private Semaphore leitura3 = new Semaphore(PERMISSAOLEITURA, true) ,escrita3 = new Semaphore(PERMISSAOESCRITA, true) ;

    public Servidor() {
        readCount1=0;
        readCount2=0;
        readCount3=0;
        estaLendo1=false;
        estaLendo2=false;
        estaLendo3=false;
    }




    public void escritaArquivo(String caminho, String dado) throws IOException {
        FileWriter arquivo = new FileWriter(caminho, StandardCharsets.UTF_8);
        arquivo.append(dado);
        arquivo.close();
    }
    public boolean escrita(String caminho, String dado){
        try {
            //verifica o arquivo
            if(caminho.charAt(3) == '1'){
                //verifica readcount ou define que esta lendo
                if(PRIORIDADENORMAL){
                    escrita1.acquire(1);
                    estaLendo1=true;
                    leitura1.acquire(3);
                }
                else{
                    while(readCount1!=0){}
                    leitura1.acquire(3);
                    escrita1.acquire(1);
                }


                //faz a escrita
                escritaArquivo(caminho, dado);

                //libera o arquivo 1
                if(PRIORIDADENORMAL){
                    estaLendo1=false;
                }
                leitura1.release(3);
                escrita1.release(1);


                return true;
            }
            if(caminho.charAt(3) == '2'){
                //verifica readcount ou define que esta lendo
                if(PRIORIDADENORMAL){
                    escrita2.acquire(1);
                    estaLendo2=true;
                    leitura2.acquire(3);
                }
                else{
                    while(readCount2!=0){}
                    leitura2.acquire(3);
                    escrita2.acquire(1);
                }


                //faz a escrita
                escritaArquivo(caminho, dado);

                //libera o arquivo 2
                if(PRIORIDADENORMAL){
                    estaLendo2=false;
                }
                leitura2.release(3);
                escrita2.release(1);

                return true;

            }
            if(caminho.charAt(3) == '3'){
                //verifica readcount ou define que esta lendo
                if(PRIORIDADENORMAL){
                    escrita3.acquire(1);
                    estaLendo3=true;
                    leitura3.acquire(3);
                }
                else{
                    while(readCount3!=0){}
                    leitura3.acquire(3);
                    escrita3.acquire(1);
                }


                //faz a escrita
                escritaArquivo(caminho, dado);

                //libera o arquivo 3
                if(PRIORIDADENORMAL){
                    estaLendo3=false;
                }
                leitura3.release(3);
                escrita3.release(1);

                return true;

            }
            return false;

        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }



    public String leituraArquivo(String caminho) throws IOException {
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
            String saida;//verifica o arquivo
            if(caminho.charAt(3) == '1'){
                //trava o arquivo 1 para a leitura
                if(PRIORIDADENORMAL){
                    while(estaLendo1){}
                    leitura1.acquire(1);
                    escrita1.acquire(1);

                }
                else {
                    //verifica readcount
                    readCount1+=1;
                    if(readCount1==1) {
                        escrita1.acquire(1);
                    }
                }
                //faz a leitura
                saida = leituraArquivo(caminho);


                //libera o arquivo 1
                leitura1.release(1);
                if(PRIORIDADENORMAL){
                    escrita1.release(1);

                }
                else {
                    //verifica readcount
                    readCount1-=1;
                    if(readCount1==0) {
                        escrita1.release(1);
                    }
                }
                return saida;
            }
            if(caminho.charAt(3) == '2'){
                //trava o arquivo 2 para a leitura
                if(PRIORIDADENORMAL){
                    while(estaLendo2){}
                    leitura2.acquire(1);
                    escrita2.acquire(1);

                }
                else {
                    //verifica readcount
                    readCount2+=1;
                    if(readCount2==1) {
                        escrita2.acquire(1);
                    }
                }

                //faz a leitura
                saida = leituraArquivo(caminho);


                //libera o arquivo 2
                leitura2.release(1);
                if(PRIORIDADENORMAL){
                    escrita2.release(1);

                }
                else {
                    //verifica readcount
                    readCount2-=1;
                    if(readCount2==0) {
                        escrita2.release(1);
                    }
                }


                return saida;

            }
            if(caminho.charAt(3) == '3'){
                //trava o arquivo 3 para a leitura
                if(PRIORIDADENORMAL){
                    while(estaLendo3){}
                    leitura3.acquire(1);
                    escrita3.acquire(1);

                }
                else {
                    //verifica readcount
                    readCount3+=1;
                    if(readCount3==1) {
                        escrita3.acquire(1);
                    }
                }


                //faz a leitura
                saida = leituraArquivo(caminho);


                //libera o arquivo 3
                leitura3.release(1);
                if(PRIORIDADENORMAL){
                    escrita3.release(1);

                }
                else {
                    //verifica readcount
                    readCount3-=1;
                    if(readCount3==0) {
                        escrita3.release(1);
                    }
                }



                return saida;

            }
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
