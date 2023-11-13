package cadastrocliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ThreadClient extends Thread {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ThreadClient(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        try {
            Boolean validate = (Boolean) in.readObject();
            Integer idUsuario = (Integer) in.readObject();
           

            if (validate) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String comando;

                do {
                    System.out.print("Comando (L - Listar, X - Finalizar, E - Entrada, S - Saída): ");
                    comando = reader.readLine().toLowerCase();

                    switch (comando) {
                        case "e":
                            //Enviar tipo de comando
                            out.writeObject("e");

                            System.out.print("ID Pessoa: ");
                            String idPessoa = reader.readLine();
                            out.writeObject(idPessoa);

                            System.out.print("ID Produto: ");
                            String idProduto = reader.readLine();
                            out.writeObject(idProduto);
                            
                            System.out.print("ID Usuario: "+ idUsuario);
                            out.writeObject(idUsuario);
                            System.out.println("");

                            System.out.print("Quantidade: ");
                            String quantidade = reader.readLine();
                            out.writeObject(quantidade);

                            System.out.print("Valor Unitário: ");
                            String valorUnitario = reader.readLine();
                            out.writeObject(valorUnitario);

                            break;

                        case "x":
                            System.out.println("Programa finalizado.");
                            break;

                        default:
                            System.out.println("Opção inválida. Escolha novamente.");
                            break;
                    }

                } while (!"x".equalsIgnoreCase(comando));

            } else {
                System.out.println("Usuario e senha não confere!");
            }

        } catch (Exception e) {
            // Verifique se o erro ocorreu devido ao fechamento do programa
            if (!(e instanceof java.io.EOFException)) {
                System.out.println("Thread Finalizada ");
            }
        }
    }
}

