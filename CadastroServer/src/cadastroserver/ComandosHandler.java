package cadastroserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ComandosHandler {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ComandosHandler(ObjectOutputStream out, ObjectInputStream in) {
        this.out = out;
        this.in = in;
    }

    public void executarComandos() throws IOException, ClassNotFoundException {
        while (true) {
            String comando = (String) in.readObject();

            if ("X".equalsIgnoreCase(comando)) {
                break; // finalizar o loop
            } else if ("E".equalsIgnoreCase(comando) || "S".equalsIgnoreCase(comando)) {
                // lógica para entrada (E) e saída (S)
                String idPessoa = (String) in.readObject();
                String idProduto = (String) in.readObject();
                String quantidade = (String) in.readObject();
                String valorUnitario = (String) in.readObject();
                
                System.out.println("idPessoa: "+ idPessoa);

                // Aqui você pode processar os dados como necessário
                // Exemplo: chamar métodos do seu controlador (ctrl) para manipular os dados
               
                
            }
        }
    }
}
