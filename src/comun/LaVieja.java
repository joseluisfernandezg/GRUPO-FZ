// JUEGO LA VIEJA - JLF *
package comun;

import comun.Mensaje;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

public class LaVieja extends javax.swing.JFrame {

  String use = "";
  int v1, v2, v3, v4, v5, v6, v7, v8, v9;
  boolean done = false;
  boolean fin = false;
  private ImageIcon icon;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JButton p1;
  private javax.swing.JButton p2;
  private javax.swing.JButton p3;
  private javax.swing.JButton p4;
  private javax.swing.JButton p5;
  private javax.swing.JButton p6;
  private javax.swing.JButton p7;
  private javax.swing.JButton p8;
  private javax.swing.JButton p9;

  void reset() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ex) {
    }
    v1 = 0;
    v2 = 0;
    v3 = 0;
    v4 = 0;
    v5 = 0;
    v6 = 0;
    v7 = 0;
    v8 = 0;
    v9 = 0;
    p1.setText("");
    p1.setEnabled(true);
    p2.setText("");
    p2.setEnabled(true);
    p3.setText("");
    p3.setEnabled(true);
    p4.setText("");
    p4.setEnabled(true);
    p5.setText("");
    p5.setEnabled(true);
    p6.setText("");
    p6.setEnabled(true);
    p7.setText("");
    p7.setEnabled(true);
    p8.setText("");
    p8.setEnabled(true);
    p9.setText("");
    p9.setEnabled(true);
    done = false;
    fin = false;
  }

  void LaVieja(int player, int poss) {
    //System.out.println("puta maquina player=" + player + ",poss=" + poss);
    switch (player) {
      case 1:
        switch (poss) {
          case 1:
            v1 = 1;
            p1.setText("O");
            p1.setEnabled(false);
            break;
          case 2:
            v2 = 1;
            p2.setText("O");
            p2.setEnabled(false);
            break;
          case 3:
            v3 = 1;
            p3.setText("O");
            p3.setEnabled(false);
            break;
          case 4:
            v4 = 1;
            p4.setText("O");
            p4.setEnabled(false);
            break;
          case 5:
            v5 = 1;
            p5.setText("O");
            p5.setEnabled(false);
            break;
          case 6:
            v6 = 1;
            p6.setText("O");
            p6.setEnabled(false);
            break;
          case 7:
            v7 = 1;
            p7.setText("O");
            p7.setEnabled(false);
            break;
          case 8:
            v8 = 1;
            p8.setText("O");
            p8.setEnabled(false);
            break;
          case 9:
            v9 = 1;
            p9.setText("O");
            p9.setEnabled(false);
            break;
        }
        break;

      case 2:
        switch (poss) {
          case 1:
            v1 = 2;
            p1.setText("X");
            p1.setEnabled(false);
            break;
          case 2:
            v2 = 2;
            p2.setText("X");
            p2.setEnabled(false);
            break;
          case 3:
            v3 = 2;
            p3.setText("X");
            p3.setEnabled(false);
            break;
          case 4:
            v4 = 2;
            p4.setText("X");
            p4.setEnabled(false);
            break;
          case 5:
            v5 = 2;
            p5.setText("X");
            p5.setEnabled(false);
            break;
          case 6:
            v6 = 2;
            p6.setText("X");
            p6.setEnabled(false);
            break;
          case 7:
            v7 = 2;
            p7.setText("X");
            p7.setEnabled(false);
            break;
          case 8:
            v8 = 2;
            p8.setText("X");
            p8.setEnabled(false);
            break;
          case 9:
            v9 = 2;
            p9.setText("X");
            p9.setEnabled(false);
            break;
        }
        break;
    }

    if (player == 2) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {
      }
    }

  }

  boolean winner(int player) {

    boolean gano = false;

    switch (player) {
      case 1:
        if (v1 == 1 && v2 == 1 && v3 == 1) {
          gano = true;
        }
        if (v4 == 1 && v5 == 1 && v6 == 1) {
          gano = true;
        }
        if (v7 == 1 && v8 == 1 && v9 == 1) {
          gano = true;
        }
        if (v1 == 1 && v4 == 1 && v7 == 1) {
          gano = true;
        }
        if (v2 == 1 && v5 == 1 && v8 == 1) {
          gano = true;
        }
        if (v3 == 1 && v6 == 1 && v9 == 1) {
          gano = true;
        }
        if (v1 == 1 && v5 == 1 && v9 == 1) {
          gano = true;
        }
        if (v3 == 1 && v5 == 1 && v7 == 1) {
          gano = true;
        }
        break;

      case 2:
        if (v1 == 2 && v2 == 2 && v3 == 2) {
          gano = true;
        }
        if (v4 == 2 && v5 == 2 && v6 == 2) {
          gano = true;
        }
        if (v7 == 2 && v8 == 2 && v9 == 2) {
          gano = true;
        }
        if (v1 == 2 && v4 == 2 && v7 == 2) {
          gano = true;
        }
        if (v2 == 2 && v5 == 2 && v8 == 2) {
          gano = true;
        }
        if (v3 == 2 && v6 == 2 && v9 == 2) {
          gano = true;
        }
        if (v1 == 2 && v5 == 2 && v9 == 2) {
          gano = true;
        }
        if (v3 == 2 && v5 == 2 && v7 == 2) {
          gano = true;
        }
        break;
    }
    return gano;
  }

  void bloqueo() {
    //primera fila horizontal
    if (v1 == 2 && v2 == 2 && v3 == 0 && !done) {
      LaVieja(1, 3);
      done = true;
    }
    if (v1 == 2 && v2 == 0 && v3 == 2 && !done) {
      LaVieja(1, 2);
      done = true;
    }
    if (v1 == 0 && v2 == 2 && v3 == 2 && !done) {
      LaVieja(1, 1);
      done = true;
    }

    //segunda fila horizontal
    if (v4 == 2 && v5 == 2 && v6 == 0 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v4 == 2 && v5 == 0 && v6 == 2 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v4 == 0 && v5 == 2 && v6 == 2 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    //tercera fila horizontal
    if (v7 == 2 && v8 == 2 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v7 == 2 && v8 == 0 && v9 == 2 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v7 == 0 && v8 == 2 && v9 == 2 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    //primera vertical
    if (v1 == 2 && v4 == 2 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v1 == 2 && v4 == 0 && v7 == 2 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    if (v1 == 0 && v4 == 2 && v7 == 2 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    //segunda vertical
    if (v2 == 2 && v5 == 2 && v8 == 0 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v2 == 2 && v5 == 0 && v8 == 2 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v2 == 0 && v5 == 2 && v8 == 2 && !done) {
      LaVieja(1, 2);
      done = true;
    }
    //tercera vertical
    if (v3 == 2 && v6 == 2 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v3 == 2 && v6 == 0 && v9 == 2 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v3 == 0 && v6 == 2 && v9 == 2 && !done) {
      LaVieja(1, 3);
      done = true;
    }
    //primera diagonal
    if (v1 == 2 && v5 == 2 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v1 == 2 && v5 == 0 && v9 == 2 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v1 == 0 && v5 == 2 && v9 == 2 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    //segunda diagonal
    if (v3 == 2 && v5 == 2 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v3 == 2 && v5 == 0 && v7 == 2 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v3 == 0 && v5 == 2 && v7 == 2 && !done) {
      LaVieja(1, 3);
      done = true;
    }
  }

  //pegar la funcion de bloqueo y cambiarle los "2" por "1" en las condiciones
  void ganar() {
    //
    if (v1 == 1 && v2 == 1 && v3 == 0 && !done) {
      LaVieja(1, 3);
      done = true;
    }
    if (v1 == 1 && v2 == 0 && v3 == 1 && !done) {
      LaVieja(1, 2);
      done = true;
    }
    if (v1 == 0 && v2 == 1 && v3 == 1 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    //segunda fila horizontal
    if (v4 == 1 && v5 == 1 && v6 == 0 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v4 == 1 && v5 == 0 && v6 == 1 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v4 == 0 && v5 == 1 && v6 == 1 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    //tercera fila horizontal
    if (v7 == 1 && v8 == 1 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v7 == 1 && v8 == 0 && v9 == 1 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v7 == 0 && v8 == 1 && v9 == 1 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    //primera vertical
    if (v1 == 1 && v4 == 1 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v1 == 1 && v4 == 0 && v7 == 1 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    if (v1 == 0 && v4 == 1 && v7 == 1 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    //segunda vertical
    if (v2 == 1 && v5 == 1 && v8 == 0 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v2 == 1 && v5 == 0 && v8 == 1 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v2 == 0 && v5 == 1 && v8 == 1 && !done) {
      LaVieja(1, 2);
      done = true;
    }
    //tercera vertical
    if (v3 == 1 && v6 == 1 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v3 == 1 && v6 == 0 && v9 == 1 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v3 == 0 && v6 == 1 && v9 == 1 && !done) {
      LaVieja(1, 3);
      done = true;
    }
    //primera diagonal
    if (v1 == 1 && v5 == 1 && v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
    if (v1 == 1 && v5 == 0 && v9 == 1 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v1 == 0 && v5 == 1 && v9 == 1 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    //segunda diagonal
    if (v3 == 1 && v5 == 1 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v3 == 1 && v5 == 0 && v7 == 1 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v3 == 0 && v5 == 1 && v7 == 1 && !done) {
      LaVieja(1, 3);
      done = true;
    }
  }

  void centro() {

    if (v5 == 0 && !done) {
      LaVieja(1, 5);
      done = true;
    }
  }

  void recorrer() {
    if (v1 == 0 && !done) {
      LaVieja(1, 1);
      done = true;
    }
    if (v2 == 0 && !done) {
      LaVieja(1, 2);
      done = true;
      //System.out.println("2");
    }
    if (v3 == 0 && !done) {
      LaVieja(1, 3);
      done = true;
      //System.out.println("3");
    }
    if (v4 == 0 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    if (v5 == 0 && !done) {
      LaVieja(1, 5);
      done = true;
    }
    if (v6 == 0 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v8 == 0 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v9 == 0 && !done) {
      LaVieja(1, 9);
      done = true;
    }
  }

  void allBloqueo() {
    if (v4 == 2 && v5 == 1 && v9 == 2 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
    if (v1 == 2 && v5 == 1 && v9 == 2 && v8 == 0 && !done) {
      LaVieja(1, 8);
      done = true;
    }
    if (v3 == 2 && v5 == 1 && v7 == 2 && v4 == 0 && !done) {
      LaVieja(1, 4);
      done = true;
    }
    if (v3 == 2 && v5 == 1 && v8 == 2 && v6 == 0 && !done) {
      LaVieja(1, 6);
      done = true;
    }
    if (v1 == 2 && v5 == 1 && v8 == 2 && v7 == 0 && !done) {
      LaVieja(1, 7);
      done = true;
    }
  }

  void machine() {
    //se marca que no hay ningun tiro realizado por parte de la makina
    //done cambia a true cuando la makina tire
    done = false;
    fin = false;
    //Mensaje msg = new Mensaje(vax, tit, tim, icon);
    //se almacena en gano el valor que recibe winner
    boolean gano = winner(2);
    //comprueba si el jugador gano
    if (gano) {
      icon = new ImageIcon(getClass().getResource("/img/comp.png"));
      String tit = "* LA VIEJA *";
      long tim = 3000;
      Toolkit.getDefaultToolkit().beep();
      String vax = use + "\nGANASTE\nPara la próxima\nno creo que ganes";
      Mensaje msg = new Mensaje(vax, tit, tim, icon);
      //JOptionPane.showMessageDialog(null, "GANASTE DE VAINA", "LA VIEJA", JOptionPane.PLAIN_MESSAGE);
      reset();
      fin = true;
    }
    //esta funcion comprueba si puede ganar
    if (!done && !fin) {
      //System.out.println("ganar");
      ganar();
    }
    //intenta bloquear los tiros del ususario en caso de uqe se pueda
    if (!done && !fin) {
      bloqueo();
      //System.out.println("bloqueo");
    }
    //tira en el centro en caso de que este vacio
    if (!done && !fin) {
      centro();
    }
    if (!done && !fin) {
      allBloqueo();
    }
    //si no puedo hacer nada de lo anterior
    if (!done && !fin && v3 == 0) {
      LaVieja(1, 3);
      done = true;
    }
    //funcion  de comprobacion de casillas vacias
    if (!done && !fin) {
      recorrer();
    }

    //comprueba si yo(makina) gane
    if (done && !fin) {
      gano = winner(1);
      if (gano) {
        String tit = "* LA VIEJA *";
        long tim = 3000;
        Toolkit.getDefaultToolkit().beep();
        String vax = "GANA COMPUTADORA\nTRATA DE MEJORAR\nPARA LA PROXIMA";
        icon = new ImageIcon(getClass().getResource("/img/comp.png"));
        Mensaje msg = new Mensaje(vax, tit, tim, icon);
        //JOptionPane.showMessageDialog(null, "SOY EL MAS ARRECHO", "LA VIEJA", JOptionPane.PLAIN_MESSAGE);
        reset();
        fin = true;
      }

    }

    if (v1 != 0 && v2 != 0 && v3 != 0 && v4 != 0 && v5 != 0 && v6 != 0 && v7 != 0 && v8 != 0 && v9 != 0) {
      String tit = "* LA VIEJA *";
      long tim = 3000;
      Toolkit.getDefaultToolkit().beep();
      String vax = use + "\nNUNCA ME GANARAS";
      icon = new ImageIcon(getClass().getResource("/img/comp.png"));
      Mensaje msg = new Mensaje(vax, tit, tim, icon);
      //JOptionPane.showMessageDialog(null, "NUNCA GANARAS", "LA VIEJA", JOptionPane.PLAIN_MESSAGE);
      reset();
    }
  }

  public LaVieja(String usr) {
    use = usr;
    initComponents();
    setResizable(false);
    setLocationRelativeTo(null);
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/lavieja.png")));
  }

  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    p2 = new javax.swing.JButton();
    p1 = new javax.swing.JButton();
    p3 = new javax.swing.JButton();
    p6 = new javax.swing.JButton();
    p5 = new javax.swing.JButton();
    p4 = new javax.swing.JButton();
    p9 = new javax.swing.JButton();
    p8 = new javax.swing.JButton();
    p7 = new javax.swing.JButton();

    //setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
         dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
         dispose();
      }
    });

    setTitle("3 EN RAYA  ( " + use + " )");

    p1.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p1ActionPerformed(evt);
      }
    });

    p2.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p2ActionPerformed(evt);
      }
    });

    p3.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p3ActionPerformed(evt);
      }
    });

    p6.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p6ActionPerformed(evt);
      }
    });

    p5.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p5ActionPerformed(evt);
      }
    });

    p4.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p4ActionPerformed(evt);
      }
    });

    p9.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p9.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p9ActionPerformed(evt);
      }
    });

    p8.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p8ActionPerformed(evt);
      }
    });

    p7.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
    p7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        p7ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(p4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(p7, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(p3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(p9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(p3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(p6, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(p9, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(p5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(p4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(p8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(p7, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap())
    );
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void p1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p1ActionPerformed
    LaVieja(2, 1);
    machine();
  }//GEN-LAST:event_p1ActionPerformed

  private void p2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p2ActionPerformed
    LaVieja(2, 2);
    machine();
  }//GEN-LAST:event_p2ActionPerformed

  private void p3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p3ActionPerformed
    LaVieja(2, 3);
    machine();
  }//GEN-LAST:event_p3ActionPerformed

  private void p4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p4ActionPerformed
    LaVieja(2, 4);
    machine();
  }//GEN-LAST:event_p4ActionPerformed

  private void p5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p5ActionPerformed
    LaVieja(2, 5);
    machine();
  }//GEN-LAST:event_p5ActionPerformed

  private void p6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p6ActionPerformed
    LaVieja(2, 6);
    machine();
  }//GEN-LAST:event_p6ActionPerformed

  private void p7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p7ActionPerformed
    LaVieja(2, 7);
    machine();
  }//GEN-LAST:event_p7ActionPerformed

  private void p8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p8ActionPerformed
    LaVieja(2, 8);
    machine();
  }//GEN-LAST:event_p8ActionPerformed

  private void p9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p9ActionPerformed
    LaVieja(2, 9);
    machine();
  }//GEN-LAST:event_p9ActionPerformed

  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new LaVieja("TU").setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}
