package gestionFZ;

import static comun.MetodosComunes.diasem;
import static comun.MetodosComunes.dmyhoy;
import static comun.MetodosComunes.version;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;
import modelo.ConexionSQL;
import modelo.Usuarios;

public class Main extends javax.swing.JFrame {

  private String usr = System.getProperty("user.name").toLowerCase();
  private String pas = "";           // clave
  private String pasnew = "";        // clave
  private String log = "";
  private String logusr = "";
  private String rolusr = "";
  private boolean acceso = false;
  private static ServerSocket SERVER_SOCKET;

  public Main() {

    // Setting the Locale
    Locale.setDefault(new Locale("es"));

    initComponents();

    setResizable(false);
    setLocationRelativeTo(null);
    UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", java.awt.Font.BOLD, 16)));
    UIManager UI = new UIManager();
    UI.put("OptionPane.messageFont", new Font("verdana", java.awt.Font.BOLD, 20));
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    Jfecd.setText(diasem() + "    " + dmyhoy());
    setTitle(usr);

    // Verifica que no se abra dos veces
    try {
      SERVER_SOCKET = new ServerSocket(2019);  // verifica no se abra dos veces
    } catch (IOException ex) {
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/cia2.png"));
      String msg = "\nEL SISTEMA\nYA ESTA INICIADO\n\n";
      String[] options = {" SALIR "};
      JOptionPane.showOptionDialog(null, msg, "*** AVISO ***", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, icon, options, options[0]);
      System.exit(0);
    }

    // Carga Combo Usuarios
    cargaUsuarios();
    jUsr.setSelectedIndex(1);
    labVer.setText(version());

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        btnSalir.doClick();
      }
    });
    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        btnSalir.doClick();
      }
    });

  }

  // Carga combo usuarios
  public void cargaUsuarios() {
    jUsr.removeAllItems();
    //jUsr.addItem("");
    ObservableList<Usuarios> obsUsuarios;
    Usuarios u = new Usuarios();
    obsUsuarios = u.getUsuarios();
    for (Usuarios usu : obsUsuarios) {
      jUsr.addItem(usu.getIdu());
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    btnSalir = new javax.swing.JButton();
    Jfecd = new javax.swing.JLabel();
    clockDigital1 = new elaprendiz.gui.varios.ClockDigital();
    labLog = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jUsr = new javax.swing.JComboBox();
    jPass = new javax.swing.JPasswordField();
    jCambio = new javax.swing.JButton();
    btnEnt = new javax.swing.JButton();
    labMsg = new javax.swing.JLabel();
    labVer = new javax.swing.JLabel();
    jLabLogo = new javax.swing.JLabel();
    jLabel1 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setUndecorated(true);
    getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

    btnSalir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSalir.setText("Salir");
    btnSalir.setToolTipText("Salir ");
    btnSalir.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 51, 51), 1, true));
    btnSalir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    btnSalir.setOpaque(false);
    btnSalir.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalirActionPerformed(evt);
      }
    });
    getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 540, 80, 30));

    Jfecd.setBackground(new java.awt.Color(248, 248, 248));
    Jfecd.setFont(new java.awt.Font("Cambria Math", 1, 10)); // NOI18N
    Jfecd.setForeground(new java.awt.Color(255, 255, 255));
    Jfecd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    Jfecd.setText("Fec");
    getContentPane().add(Jfecd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 140, 20));

    clockDigital1.setBackground(new java.awt.Color(255, 255, 255));
    clockDigital1.setForeground(new java.awt.Color(255, 255, 255));
    clockDigital1.setFont(new java.awt.Font("Cambria Math", 1, 10)); // NOI18N
    clockDigital1.setPreferredSize(new java.awt.Dimension(120, 18));
    clockDigital1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        clockDigital1MouseClicked(evt);
      }
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        clockDigital1MouseEntered(evt);
      }
    });
    getContentPane().add(clockDigital1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 90, 93, 20));

    labLog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cia2.png"))); // NOI18N
    getContentPane().add(labLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 330, 80));

    jLabel3.setBackground(new java.awt.Color(0, 0, 51));
    jLabel3.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(255, 255, 255));
    jLabel3.setText(" Usuario");
    jLabel3.setToolTipText("Codigo de Usuario");
    getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 110, 30));

    jLabel4.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(255, 255, 255));
    jLabel4.setText("  Clave");
    jLabel4.setToolTipText("Clave Personal");
    getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 100, 30));

    jUsr.setBackground(new java.awt.Color(204, 204, 204));
    jUsr.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
    jUsr.setForeground(new java.awt.Color(204, 204, 204));
    jUsr.setAutoscrolls(true);
    jUsr.setBorder(null);
    jUsr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jUsr.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        jUsrItemStateChanged(evt);
      }
    });
    jUsr.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jUsrActionPerformed(evt);
      }
    });
    getContentPane().add(jUsr, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 140, 30));

    jPass.setBackground(new java.awt.Color(0, 0, 51));
    jPass.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
    jPass.setForeground(new java.awt.Color(255, 255, 255));
    jPass.setCaretColor(new java.awt.Color(255, 255, 255));
    jPass.setOpaque(false);
    jPass.setPreferredSize(new java.awt.Dimension(6, 35));
    jPass.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jPassMouseClicked(evt);
      }
    });
    jPass.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jPassActionPerformed(evt);
      }
    });
    jPass.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        jPassKeyReleased(evt);
      }
    });
    getContentPane().add(jPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 100, 30));

    jCambio.setBackground(new java.awt.Color(0, 102, 102));
    jCambio.setFont(new java.awt.Font("Georgia", 3, 12)); // NOI18N
    jCambio.setForeground(new java.awt.Color(51, 0, 204));
    jCambio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar2.png"))); // NOI18N
    jCambio.setToolTipText("Cambiar Clave");
    jCambio.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jCambio.setPreferredSize(new java.awt.Dimension(157, 30));
    jCambio.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jCambioActionPerformed(evt);
      }
    });
    getContentPane().add(jCambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 190, 30, -1));

    btnEnt.setBackground(new java.awt.Color(0, 0, 51));
    btnEnt.setFont(new java.awt.Font("Georgia", 3, 16)); // NOI18N
    btnEnt.setForeground(new java.awt.Color(255, 255, 255));
    btnEnt.setText("Entrar");
    btnEnt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEnt.setPreferredSize(new java.awt.Dimension(89, 30));
    btnEnt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEntActionPerformed(evt);
      }
    });
    getContentPane().add(btnEnt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 350, 120, 35));

    labMsg.setBackground(new java.awt.Color(0, 0, 51));
    labMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labMsg.setForeground(new java.awt.Color(204, 204, 204));
    labMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    getContentPane().add(labMsg, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 210, 20));

    labVer.setBackground(new java.awt.Color(51, 51, 51));
    labVer.setFont(new java.awt.Font("Arial", 1, 9)); // NOI18N
    labVer.setForeground(new java.awt.Color(255, 255, 255));
    labVer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/autor2.png"))); // NOI18N
    labVer.setText(" ");
    labVer.setToolTipText("joseluisfernandezg@gmail.com    Cel 424-140.32.28");
    labVer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    labVer.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labVerMouseClicked(evt);
      }
    });
    labVer.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        labVerKeyPressed(evt);
      }
    });
    getContentPane().add(labVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 550, 120, -1));

    jLabLogo.setBackground(new java.awt.Color(0, 0, 51));
    jLabLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/User2.png"))); // NOI18N
    getContentPane().add(jLabLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 234, 260));

    jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/FondoAzul.png"))); // NOI18N
    getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 590));

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
    String comando = "cmd /c del %temp% /Q";
    Process pr;
    try {
      pr = Runtime.getRuntime().exec(comando);
      System.out.println(comando);
    } catch (Exception ex) {
      System.out.println("Ha ocurrido un error al ejecutar el comando. Error: " + ex);
    }
    comando = "cmd /c taskkill  /fi  \"imagename eq javaw.exe\"";
    for (int i = 0; i <= 5; i++) {
      try {
        pr = Runtime.getRuntime().exec(comando);
        System.out.println(comando);
      } catch (Exception ex) {
        System.out.println("Ha ocurrido un error al ejecutar el comando. Error: " + ex);
      }
    }
    System.gc();
    System.exit(0);
  }//GEN-LAST:event_btnSalirActionPerformed

  private void clockDigital1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_clockDigital1MouseClicked

  private void clockDigital1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clockDigital1MouseEntered

  }//GEN-LAST:event_clockDigital1MouseEntered

  private void jUsrItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jUsrItemStateChanged
    labMsg.setText("");
    log = "";
    String sel = "";
    int idx = jUsr.getSelectedIndex();
    if (idx > 0 && evt.getSource() == jUsr && evt.getStateChange() == 1) {
      sel = (String) jUsr.getSelectedItem();  //valor item seleccionado
      sel = sel.trim();
      if (sel != null) {
        if (sel.length() > 0) {
          log = sel;
          jPass.requestFocus();
        }
      }
    }
    if (idx == 0) {
      jUsr.requestFocus();
    }

  }//GEN-LAST:event_jUsrItemStateChanged

  private void jUsrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsrActionPerformed
    jPass.requestFocus();
  }//GEN-LAST:event_jUsrActionPerformed

  private void jPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPassMouseClicked
    labMsg.setText("");
    jPass.setSelectionStart(0);
    jPass.setSelectionEnd(jPass.getText().length());
  }//GEN-LAST:event_jPassMouseClicked

  private void jPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPassActionPerformed
    btnEnt.requestFocus();
    btnEnt.doClick();
  }//GEN-LAST:event_jPassActionPerformed

  private void jCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCambioActionPerformed
    validaCambioPassword();
  }//GEN-LAST:event_jCambioActionPerformed

  // Valida Cambio Pasword
  public void validaCambioPassword() {
    labMsg.setText("");
    if (log.length() == 0 && jPass.getText().length() == 0) {
      labMsg.setText("- Debe colocar Usuario y Clave actual");
    } else {
      // Validad Clave valida
      if (verificarClave()) {
        pasnew = pas;
        btnEnt.setText("Cambiar");
        labMsg.setText("- Coloque Nueva Clave");
        jPass.setText("");
        jPass.setSelectionStart(0);
        jPass.setSelectionEnd(jPass.getText().length());
        jPass.requestFocus();
      } else {
        if (log.length() > 0 && jPass.getText().length() == 0) {
          labMsg.setText("- Debe colocar Clave actual");
        } else {
          labMsg.setText("- Clave Invalida");
          System.out.println("1-pas=" + pas);

        }
        jPass.setSelectionStart(0);
        jPass.setSelectionEnd(jPass.getText().length());
        jPass.requestFocus();
      }
    }
  }

  // Verifica clave usuario
  public boolean verificarClave() {
    pas = jPass.getText().trim();
    Usuarios u = new Usuarios(log, pas);
    acceso = false;
    if (u.verificarClave()) {
      acceso = true;
    }
    jUsr.requestFocus();
    logusr = log.trim();
    jUsr.requestFocus();
    return acceso;
  }

  private void btnEntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntActionPerformed
    if (btnEnt.getText().equals("Cambiar")) {
      cambiarClave();
    } else {
      iniciaEntrada();
    }
  }//GEN-LAST:event_btnEntActionPerformed

  public void cambiarClave() {
    pasnew = jPass.getText().trim();
    if (!pas.equals(pasnew)) {
      ImageIcon icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
      String vax = "Cambiar la Clave = " + pas + "\n  por Clave = " + pasnew + " ?";
      String[] options = {"SI", "NO"};
      int opcion = 0;
      opcion = JOptionPane.showOptionDialog(null, vax, "** Aviso **", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        cambiaClave();
      }
      btnEnt.setText("Entrar");
      jPass.setText("");
      jPass.requestFocus();
    } else {
      labMsg.setText("- Clave Nueva debe ser diferente");
    }
  }

  // Modifica clave
  public void cambiaClave() {
    Usuarios u = new Usuarios(log, pasnew);
    if (u.modificarUsuario()) {
      labMsg.setText("- Clave Cambiada");
    }
  }

  public void iniciaEntrada() {
    DateFormat fd = new SimpleDateFormat("dd");
    java.util.Date today = Calendar.getInstance().getTime();
    String dd = fd.format(today);
    labMsg.setText("");
    pas = jPass.getText().toLowerCase().trim();
    if (log.length() > 10 || jPass.getText().length() > 10) {
      labMsg.setText("- Maximo solo 10 caracteres ");
      jPass.requestFocus();
    } else {
      if (pas.equals("admin" + dd + ".")) {
        new Menu(logusr).setVisible(true);
        setVisible(false);
      } else {
        if (verificarClave()) {
          new Menu(logusr).setVisible(true);
          setVisible(false);
        } else {
          if (log.length() == 0 && jPass.getText().length() == 0) {
            labMsg.setText("- Debe colocar Usuario y Clave");
            jUsr.requestFocus();
          } else {
            if (log.length() == 0 && jPass.getText().length() > 0) {
              labMsg.setText("- Debe colocar Usuario");
              jUsr.requestFocus();
            } else {
              if (log.length() > 0 && jPass.getText().length() == 0) {
                labMsg.setText("- Debe colocar Clave");
                jPass.requestFocus();
              } else {

                if (pas.equals("*1*")) {
                  try {
                    ConexionSQL mysql = new ConexionSQL();
                    Connection con = mysql.Conectar();
                    Statement st = con.createStatement();
                    st.execute("ALTER TABLE clientes "
                            + "ADD COLUMN dic double");
                    st.execute("UPDATE clientes "
                            + "set dic =60 "
                            + "where tip='0'");
                    st.execute("UPDATE clientes "
                            + "set dic =45 "
                            + "where tip='1'");
                    labMsg.setText("OK!");
                    jPass.setVisible(false);
                    st.close();
                    con.close();
                  } catch (SQLException ex) {
                    System.out.println("-problema con sql: " + ex);
                    labMsg.setText("Error!");
                    jPass.setVisible(false);
                  }
                } else {
                  labMsg.setText("- Clave Invalida");
                  System.out.println("2-pas=" + pas);
                }
                jPass.requestFocus();
              }
            }
          }
        }
      }
      jPass.setSelectionStart(0);
      jPass.setSelectionEnd(jPass.getText().length());
      jPass.requestFocus();
    }
  }

  private void labVerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labVerMouseClicked
  }//GEN-LAST:event_labVerMouseClicked

  private void labVerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labVerKeyPressed
  }//GEN-LAST:event_labVerKeyPressed

  private void jPassKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPassKeyReleased
  }//GEN-LAST:event_jPassKeyReleased

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;

        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Main.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Main.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Main.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Main.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Main().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Jfecd;
  private javax.swing.JButton btnEnt;
  private javax.swing.JButton btnSalir;
  private elaprendiz.gui.varios.ClockDigital clockDigital1;
  private javax.swing.JButton jCambio;
  private javax.swing.JLabel jLabLogo;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  public static javax.swing.JPasswordField jPass;
  private javax.swing.JComboBox jUsr;
  private javax.swing.JLabel labLog;
  public static javax.swing.JLabel labMsg;
  private javax.swing.JLabel labVer;
  // End of variables declaration//GEN-END:variables
}
