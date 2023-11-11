/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastrocliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class CadastroCliente {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
    try {
        Socket socket = new Socket("localhost", 4321);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("------------------------------");
        System.out.print("Login: ");
        String login = reader.readLine();
        System.out.print("Senha: ");
        String senha = reader.readLine();

        out.writeObject(login);
        out.writeObject(senha);
        out.writeObject("Mensagem do cliente para o servidor.");
        out.flush();

        
             ThreadClient threadClient = new ThreadClient(in);
        threadClient.start();
            
        

       
    } catch (IOException ex) {
        Logger.getLogger(CadastroCliente.class.getName()).log(Level.SEVERE, null, ex);
    }
}


}
