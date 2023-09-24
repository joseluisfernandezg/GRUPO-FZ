package gestionFZ;

public class Registra_Clave extends javax.swing.JFrame {

  public Registra_Clave() {
    initComponents();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel2 = new javax.swing.JLabel();
    jLabNoe1 = new javax.swing.JLabel();
    jPasswordField1 = new javax.swing.JPasswordField();
    jAct = new javax.swing.JLabel();
    jPass = new javax.swing.JPasswordField();
    jLabNoe2 = new javax.swing.JLabel();
    jMsg = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 0, 153));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/usr.png"))); // NOI18N
    jLabel2.setText("CLAVE USUARIO");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    jLabNoe1.setBackground(new java.awt.Color(51, 51, 102));
    jLabNoe1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe1.setForeground(new java.awt.Color(51, 51, 102));
    jLabNoe1.setText("CLAVE");
    jLabNoe1.setToolTipText("");
    jLabNoe1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe1MouseClicked(evt);
      }
    });

    jPasswordField1.setText("jPasswordField1");

    jAct.setBackground(new java.awt.Color(0, 0, 51));
    jAct.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jAct.setForeground(new java.awt.Color(51, 51, 51));
    jAct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    jAct.setText("Cambiar Clave");
    jAct.setToolTipText("Actualizacion Cambios BD");
    jAct.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jAct.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jActMouseClicked(evt);
      }
    });

    jPass.setText("jPasswordField1");
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

    jLabNoe2.setBackground(new java.awt.Color(51, 51, 102));
    jLabNoe2.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 14)); // NOI18N
    jLabNoe2.setForeground(new java.awt.Color(51, 51, 102));
    jLabNoe2.setText("CLAVE");
    jLabNoe2.setToolTipText("");
    jLabNoe2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jLabNoe2MouseClicked(evt);
      }
    });

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jMsg.setForeground(new java.awt.Color(0, 0, 204));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
    jMsg.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jMsgMouseClicked(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabNoe1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(jLabNoe2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jPasswordField1)
                  .addComponent(jPass)))
              .addComponent(jAct, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addContainerGap(24, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(29, 29, 29)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addGap(17, 17, 17)
            .addComponent(jLabNoe1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPass, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
          .addComponent(jLabNoe2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGap(18, 18, 18)
        .addComponent(jAct, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
        .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jLabNoe1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe1MouseClicked

  private void jActMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jActMouseClicked
    if (jPass.isVisible()) {
      jPass.setEnabled(false);
      jPass.setVisible(false);
    } else {
      jPass.setEnabled(true);
      jPass.setVisible(true);
      jPass.setText("");
      jPass.requestFocus();
      jMsg.setText("- Ingrese codigo modificacion -");
    }
  }//GEN-LAST:event_jActMouseClicked

  private void jPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPassMouseClicked
    jPass.setSelectionStart(0);
    jPass.setSelectionEnd(jPass.getText().length());
  }//GEN-LAST:event_jPassMouseClicked

  private void jPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPassActionPerformed
    String pas = jPass.getText();
    System.out.println("pas=" + pas);
  }//GEN-LAST:event_jPassActionPerformed

  private void jLabNoe2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabNoe2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jLabNoe2MouseClicked

  private void jMsgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMsgMouseClicked
    if (evt.getClickCount() == 2) {
      jAct.setVisible(true);
      jPass.setVisible(true);
    }
  }//GEN-LAST:event_jMsgMouseClicked

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
      java.util.logging.Logger.getLogger(Registra_Clave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Registra_Clave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Registra_Clave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registra_Clave.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registra_Clave().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public static javax.swing.JLabel jAct;
  private javax.swing.JLabel jLabNoe1;
  private javax.swing.JLabel jLabNoe2;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jMsg;
  private javax.swing.JPasswordField jPass;
  private javax.swing.JPasswordField jPasswordField1;
  // End of variables declaration//GEN-END:variables
}
