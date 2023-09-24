package gestionFZ;

import comun.Mensaje;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import modelo.Importadora;
import static gestionFZ.Menu.RegStk;

public class Registra_Existencias extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String format = " %1$-10s  %2$-40s  %3$10s\n";
  String cop = "";
  String dep = "";
  String cax = "";
  double can = 0;

  List<String> veclis;

  public Registra_Existencias(List<String> names, String fil) {

    veclis = names;

    initComponents();

    labTit.setText(fil);

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    String vax = String.format(format, " CODIGO", " DESCRIPCION", " EXISTENCIA").toUpperCase();
    jTiT.setText(vax);

    ListaDatos();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegStk.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegStk.dispose();
      }
    });

  }

  public void ListaDatos() {
    // Lista vector
    modelo.clear();

    int i = 1;
    for (String name : veclis) {
      //System.out.println("- i=" + i + ". " + name);
      i++;
      String[] ln = name.split(";");
      cop = "";
      dep = "";
      can = 0;
      for (int j = 0; j < ln.length; j++) {
        if (j == 0) {
          cop = ln[j].trim();
        }
        if (j == 1) {
          dep = ln[j].trim();
        }
        if (j == 2) {
          cax = ln[j].trim();
        }
      }
      //System.out.println("cop=" + cop + ",dep=" + dep + ",cax=" + cax);
      if (isNumeric(cax)) {
        can = Double.parseDouble(cax);
        String vax = String.format(format, cop, dep, MtoEs(can, 0).replace(",00", ""));
        modelo.addElement(vax);
      }
    }

    jList.setModel(modelo);

    if (!modelo.isEmpty()) {
      //jList.setSelectedIndex(0);
      labCan.setText("Cant =  " + modelo.size());

    }

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jTiT = new javax.swing.JLabel();
    btnSal = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    labMsg = new javax.swing.JLabel();
    labTit = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(0, 0, 153));
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exis.png"))); // NOI18N
    jLabel3.setText("  ACTUALIZAR EXISTENCIAS");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jList.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jList.setFocusable(false);
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    jTiT.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jTiT.setForeground(new java.awt.Color(102, 0, 0));
    jTiT.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Actualizar");
    btnGra.setToolTipText("Actualizar Stock en  Catalogo de Productos");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });

    labCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(51, 51, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labMsg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    labMsg.setForeground(new java.awt.Color(153, 0, 0));
    labMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labMsg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labMsg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labMsgMouseClicked(evt);
      }
    });

    labTit.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    labTit.setForeground(new java.awt.Color(204, 0, 0));
    labTit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTit.setText(" ");
    labTit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(23, 23, 23)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
          .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btnSal)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(btnGra)
            .addGap(18, 18, 18)
            .addComponent(labCan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGap(18, 18, 18)
            .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(labMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(27, 27, 27)
            .addComponent(labTit, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(0, 16, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labTit, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(labMsg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    RegStk.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    ImageIcon icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
    String vax = "Desea Actualizar Existencias  ?";

    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {

      try {

        vax = "";
        //Incluir / Modificar

        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();

        int i = 1;
        for (String name : veclis) {
          //System.out.println("- i=" + i + ". " + name);
          i++;
          String[] ln = name.split(";");
          cop = "";
          dep = "";
          can = 0;
          for (int j = 0; j < ln.length; j++) {
            if (j == 0) {
              cop = ln[j].trim();
            }
            if (j == 1) {
              dep = ln[j].trim();
            }
            if (j == 2) {
              cax = ln[j].trim();
            }
          }
          //System.out.println("cop=" + cop + ",dep=" + dep + ",cax=" + cax);
          if (isNumeric(cax)) {
            can = GetMtoDouble(cax);
            // Modifica
            String sql = "update Listaprecios "
                    + "set stk =" + can + " "
                    + "where cop =  '" + cop + "' ";
            st.execute(sql);
            //System.out.println("sql=" + sql);
          }
        }

        con.commit();
        con.close();

        // Genera reporte Excel
        Importadora imp = new Importadora();
        String fel = imp.getFecLis();
        new Reporte_ExcelListaPrecios(fel.replace("/", "").replace("-", ""), 0);

        labMsg.setText("- Se Actualizaron Existencias - Revise ");
        String tit = "* AVISO *";
        icon = new ImageIcon(getClass().getResource("/img/ok.png"));
        long tim = 3000;
        Toolkit.getDefaultToolkit().beep();
        vax = "- Se Actualizaron Existencias - Revise ";
        Mensaje msg = new Mensaje(vax, tit, tim, icon);
        btnSal.requestFocus();

      } catch (SQLException ex) {
        Logger.getLogger(Registra_Existencias.class.getName()).log(Level.SEVERE, null, ex);
      }

    }

  }//GEN-LAST:event_btnGraActionPerformed

  private void labMsgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labMsgMouseClicked

  }//GEN-LAST:event_labMsgMouseClicked

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
      java.util.logging.Logger.getLogger(Registra_Existencias.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registra_Existencias.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registra_Existencias.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);

    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registra_Existencias.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        List<String> prods = new ArrayList<>();
        new Registra_Existencias(prods, "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel labCan;
  private javax.swing.JLabel labMsg;
  private javax.swing.JLabel labTit;
  private javax.swing.JLabel labojo;
  // End of variables declaration//GEN-END:variables
}
