package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import static gestionFZ.Registro_PedidoCliente.regPrm;

public class Consulta_CanPromoc extends javax.swing.JFrame {

  private DefaultListModel modelo;
  String format = " %1$-10s  %2$-5s  %3$-10s  %4$-10s\n";

  List<String> veclis;

  public Consulta_CanPromoc(List<String> names) {

    veclis = names;

    initComponents();

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    String vax = String.format(format, "GRUPO", "%DESC", "A PARTIR", "CANT PEDIDO").toUpperCase();
    jTiT.setText(vax);

    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        regPrm.dispose();;
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        regPrm.dispose();
      }
    });

    ListaDatos();

  }

  // Lista Datos
  public void ListaDatos() {

    modelo.clear();

    int i = 1;
    for (String name : veclis) {

      i++;
      String[] ln = name.split(";");

      String grp = "";
      String pox = "";
      String cxt = "";
      String cxn = "";

      for (int j = 0; j < ln.length; j++) {
        if (j == 0) {
          grp = ln[j].trim();
        }
        if (j == 1) {
          pox = ln[j].trim();
        }
        if (j == 2) {
          cxt = ln[j].trim();
        }
        if (j == 3) {
          cxn = ln[j].trim();
        }
      }

      double por = 0;
      double can = 0;
      double cat = 0;

      if (isNumeric(pox)) {
        por = Double.parseDouble(pox);
      }

      if (isNumeric(cxt)) {
        cat = Double.parseDouble(cxt);
        if (isNumeric(cxn)) {
          can = Double.parseDouble(cxn);
          String vax = String.format(format, grp, MtoEs(por, 0).replace(",00", "") + "%", MtoEs(cat, 0).replace(",00", ""), MtoEs(can, 0).replace(",00", ""));
          modelo.addElement(vax);
        }
      }
    }

    jList.setModel(modelo);

    if (!modelo.isEmpty()) {
      jList.setSelectedIndex(0);
    }

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    btnSal = new javax.swing.JButton();
    jTiT = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exis.png"))); // NOI18N
    jLabel3.setText("  PROMOCIONES X CANTIDAD");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jList.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    jTiT.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jTiT.setForeground(new java.awt.Color(102, 0, 0));
    jTiT.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(btnSal)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(18, 18, 18)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(13, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel3)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 175, Short.MAX_VALUE)
        .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
      .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
          .addGap(81, 81, 81)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addContainerGap(56, Short.MAX_VALUE)))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    regPrm.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

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
      java.util.logging.Logger.getLogger(Consulta_CanPromoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_CanPromoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_CanPromoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_CanPromoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        List<String> prods = new ArrayList<>();
        new Consulta_CanPromoc(prods).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  // End of variables declaration//GEN-END:variables
}
