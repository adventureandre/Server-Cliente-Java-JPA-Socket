package cadastrocliente;

import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Cliente de Thread
 *
 * @author andre
 */
public class ThreadClient extends Thread {

    private ObjectInputStream in;
    private final JTextArea textArea;
    private JFrame frame;

    public ThreadClient(ObjectInputStream in) {
        this.in = in;
        frame = new JFrame("Mensagens do Servidor");
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea));
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Boolean validate =  (Boolean) in.readObject();
                List<String> produtoList = (List<String>) in.readObject();
              

                SwingUtilities.invokeLater(() -> {  
                    
                    if(validate ){
                        
                       textArea.append("Usuario conectado com sucesso \n");
                         textArea.append("Lista de Itens:\n");
                         
                         
                         //logado no sistema
                         
                    for (String item : produtoList) {
                        textArea.append(item + "\n");
                    }
                    
                    }else{
                        textArea.append("Usuario inválido! \n");
                    }


                    textArea.setCaretPosition(textArea.getDocument().getLength());
                });
            }

        } catch (Exception e) {
            System.out.println("Thread Finalizada1 ");
        }
    }
}
