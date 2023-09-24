package comun;

import static comun.MetodosComunes.isvalidFec;

public class validaFecYMD {

  public validaFecYMD(String fec) {
    
    System.out.println("v=" + isvalidFec(fec,0));
    
  }

  public static void main(String args[]) {
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new validaFecYMD("20220121");
      }
    });
  }

}
