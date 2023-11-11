/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import cadastroserver.controller.ProdutoJpaController;
import cadastroserver.controller.UsuarioJpaController;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andre
 */
public class CadastroThread extends Thread {

    private final UsuarioJpaController ctrlUsu;

    private final ProdutoJpaController ctrl;

    private final Socket s1;

    public CadastroThread(UsuarioJpaController ctrlUsu, ProdutoJpaController ctrl, Socket s1) {
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
        this.ctrl = ctrl;
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream()); ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();
            String mensagem = (String) in.readObject();

            Date dataAtual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = formato.format(dataAtual);
            System.out.println(">> Nova Comunicação em " + dataFormatada);

            boolean usuarioValido = validar(login, senha);

            List<String> produtoList = ctrl.findProdutoNames();

            if(usuarioValido){
                //logado no sistema
                out.writeObject(usuarioValido);
                out.writeObject(produtoList);
            System.out.println(produtoList.size());
                
            }else{
                 out.writeObject(usuarioValido);
                 out.writeObject(null);
                   
            }
           out.flush();

//         List<Usuario> usuariosList = ctrlUsu.findUsuarioEntities();
//
//            int tamanhoLista = usuariosList.size();
//            System.out.println("tamnho=" + tamanhoLista);
//            for (Usuario usuario : usuariosList) {
//                System.out.println("login=" + usuario.getLogin());
//            }
//
//            System.out.println("GRAVANDO NO BANCO - login=" + login + " senha=" + senha);
//            Usuario userTeste = new Usuario(null);
//            userTeste.setLogin(login);
//            userTeste.setSenha(senha);
//
//            ctrlUsu.create(userTeste);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(CadastroThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validar(String login, String senha) {
        return ctrlUsu.validarUsuario(login, senha);
    }

}
