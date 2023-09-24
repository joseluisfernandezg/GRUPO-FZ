package gestionFZ;

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.isNumeric;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import static gestionFZ.Registro_PedidoCliente.modPrd;

public class Registro_PedidoCliente_Mod extends javax.swing.JFrame {

  int npe = 0;
  double can = 0;
  double cax = 0;
  String cop = "";
  String unm = "";
  String dep = "";
  String unx = "";

  Registro_PedidoCliente ctrlP;

  public Registro_PedidoCliente_Mod(Registro_PedidoCliente ctrl, int npx, String cox, String dex, String unx, double cax) {

    ctrlP = ctrl;
    npe = npx;
    cop = cox;
    dep = dex;
    unm = unx;
    can = cax;
    this.unx = unx;
    this.cax = cax;

    initComponents();

    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes
    cargaUnm();

    //System.out.println("npe=" + npe + ",cop=" + cop + ",dep=" + dep + ",unm=" + unm + ",can=" + can);
    cbUnm.setSelectedItem(unm);

    labCop.setText(cop);
    //labDes.setText("  " + dep);
    txtCan.setText(MtoEs(can, 2).replace(",00", ""));

    txtCan.setSelectionStart(0);
    txtCan.setSelectionEnd(txtCan.getText().length());
    txtCan.requestFocus();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        modPrd.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        modPrd.dispose();
      }
    });

  }

  public void cargaUnm() {
    ConexionSQL mysql = new ConexionSQL();
    Connection con = mysql.Conectar();
    try {
      cbUnm.removeAllItems();
      cbUnm.addItem("");

      Statement st = con.createStatement();
      String sql = "SELECT unm from unidmed "
        + "order by sec";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        cbUnm.addItem(rs.getString("unm"));
      }
      cbUnm.setSelectedIndex(-1);
      rs.close();
      con.close(); // cerramos la conexion
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage(), "**Error SELECT nom from cargo", JOptionPane.ERROR_MESSAGE);
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    labTi1 = new javax.swing.JLabel();
    txtCan = new javax.swing.JTextField();
    labTi4 = new javax.swing.JLabel();
    cbUnm = new javax.swing.JComboBox();
    labTi3 = new javax.swing.JLabel();
    labCop = new javax.swing.JLabel();
    btnSal = new javax.swing.JButton();
    labTit = new javax.swing.JLabel();
    btnGra = new javax.swing.JButton();
    jSeparator1 = new javax.swing.JSeparator();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    labTi1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    labTi1.setForeground(new java.awt.Color(0, 0, 102));
    labTi1.setText("Codigo");
    labTi1.setToolTipText("");
    labTi1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labTi1MouseClicked(evt);
      }
    });

    txtCan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCan.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtCan.setText(" ");
    txtCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtCan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtCan.setPreferredSize(new java.awt.Dimension(7, 30));
    txtCan.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCanMouseClicked(evt);
      }
    });
    txtCan.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCanActionPerformed(evt);
      }
    });
    txtCan.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtCanKeyReleased(evt);
      }
    });

    labTi4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    labTi4.setForeground(new java.awt.Color(51, 51, 102));
    labTi4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTi4.setText("UM");
    labTi4.setToolTipText("Cliente con factura");

    cbUnm.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    cbUnm.setMaximumRowCount(20);
    cbUnm.setPreferredSize(new java.awt.Dimension(35, 30));
    cbUnm.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        cbUnmItemStateChanged(evt);
      }
    });
    cbUnm.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cbUnmActionPerformed(evt);
      }
    });

    labTi3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 15)); // NOI18N
    labTi3.setForeground(new java.awt.Color(0, 0, 102));
    labTi3.setText("Cantidad");
    labTi3.setToolTipText("");
    labTi3.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        labTi3MouseClicked(evt);
      }
    });

    labCop.setBackground(new java.awt.Color(204, 204, 204));
    labCop.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    labCop.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCop.setText(" ");
    labCop.setToolTipText("Cliente con factura");
    labCop.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
    labCop.setOpaque(true);

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    labTit.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    labTit.setForeground(new java.awt.Color(51, 51, 102));
    labTit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labTit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
    labTit.setText("MODIFICAR ");
    labTit.setToolTipText("Cliente con factura");
    labTit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setText("Grabar");
    btnGra.setToolTipText("Actualizar Cambios Pedido");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(labTit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTi3)
                    .addComponent(labTi4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labTi1))
                  .addGap(18, 18, 18)
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labCop, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                      .addComponent(txtCan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                      .addComponent(cbUnm, javax.swing.GroupLayout.Alignment.LEADING, 0, 68, Short.MAX_VALUE)))))))
          .addGroup(layout.createSequentialGroup()
            .addGap(68, 68, 68)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(btnGra))))
        .addContainerGap(22, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(labTit, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labTi1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labCop, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labTi3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labTi4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(cbUnm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void labTi1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labTi1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_labTi1MouseClicked

  private void txtCanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCanMouseClicked
    txtCan.setSelectionStart(0);
    txtCan.setSelectionEnd(txtCan.getText().length());
    txtCan.requestFocus();
  }//GEN-LAST:event_txtCanMouseClicked

  private void txtCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCanActionPerformed
    validaCantidad();
  }//GEN-LAST:event_txtCanActionPerformed


  private void txtCanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCanKeyReleased
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCanKeyReleased

  private void cbUnmItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbUnmItemStateChanged
    int idx = cbUnm.getSelectedIndex();
    if (idx >= 0 && evt.getSource() == cbUnm && evt.getStateChange() == 1) {
      String str = (String) cbUnm.getSelectedItem().toString();
      if (str != null) {
        if (str.length() > 0) {
          str = str.trim();
          unm = str;
          btnGra.requestFocus();
        }
      }
    }
  }//GEN-LAST:event_cbUnmItemStateChanged

  private void cbUnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUnmActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_cbUnmActionPerformed

  private void labTi3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labTi3MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_labTi3MouseClicked

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    modPrd.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    if (validaCampos()) {
      ctrlP.recibeCambiosProducto(npe, cop, unm, can);
      modPrd.dispose();
    } else {
      txtCan.requestFocus();
    }
  }//GEN-LAST:event_btnGraActionPerformed

  public boolean validaCampos() {
    if (validaCantidad()) {
      return true;
    } else {
      return false;
    }
  }

  public boolean validaCantidad() {
    String cax = txtCan.getText();
    if (isNumeric(cax)) {
      cax = GetCurrencyDouble(cax);
      can = GetMtoDouble(cax);
      if (can > 0) {
        btnGra.requestFocus();
        return true;
      } else {
        txtCan.setSelectionStart(0);
        txtCan.setSelectionEnd(txtCan.getText().length());
        txtCan.requestFocus();
        return true;
      }
    } else {
      txtCan.setSelectionStart(0);
      txtCan.setSelectionEnd(txtCan.getText().length());
      txtCan.requestFocus();
      return false;
    }
  }

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
      java.util.logging.Logger.getLogger(Registro_PedidoCliente_Mod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente_Mod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente_Mod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_PedidoCliente_Mod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        Registro_PedidoCliente ctrl = new Registro_PedidoCliente();
        new Registro_PedidoCliente_Mod(ctrl, 0, "", "", "", 0).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnGra;
  private javax.swing.JButton btnSal;
  private javax.swing.JComboBox cbUnm;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JLabel labCop;
  private javax.swing.JLabel labTi1;
  private javax.swing.JLabel labTi3;
  private javax.swing.JLabel labTi4;
  private javax.swing.JLabel labTit;
  private javax.swing.JTextField txtCan;
  // End of variables declaration//GEN-END:variables
}
