// Los Pinos/Jormano
package comun;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Mensaje {

  //--------------------- Mensaje con imagenes ---------------------------
  static JOptionPane option = new JOptionPane("", JOptionPane.INFORMATION_MESSAGE);
  private static JDialog dialogo = null;

  public Mensaje(String texto, String titulo, final long timeout, ImageIcon icon) {
    option.setMessage(texto);
    option.setIcon(icon);
    option.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
    if (null == dialogo) {
      dialogo = option.createDialog(null, titulo);
    } else {
      dialogo.setTitle(titulo);
    }
    dialogo.pack();
    dialogo.setLocationRelativeTo(null);
    Thread hilo = new Thread() {
      public void run() {
        try {
          Thread.sleep(timeout);
          if (dialogo.isVisible()) {
            dialogo.setVisible(false);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };
    hilo.start();
    dialogo.setVisible(true);
    dialogo.pack();
    dialogo.setLocationRelativeTo(null);
    hilo.stop();
  }

}
// Mensaje msg = new Mensaje(vax, tit, tim, icon);

