package gestionFZ;
//
// Registro Importadora
//

import static comun.MetodosComunes.GetCurrencyDouble;
import static comun.MetodosComunes.GetMtoDouble;
import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.validaEmail;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import static gestionFZ.Menu.RegVen;

public class Registro_Vendedor extends javax.swing.JFrame {

  int indok = 0;
  String cod = "", nom = "", car = "", eml = "";
  double por = 0;

  ImageIcon icon;

  // Metodo Constructor - Inicializa valores
  public Registro_Vendedor() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegVen.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegVen.dispose();
      }
    });
    presentaDatos();
  }

  public void presentaDatos() {
    try {

      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT cod,nom,car,eml,poc "
        + "FROM Vendedor";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        cod = rs.getString("cod");
        nom = rs.getString("nom");
        car = rs.getString("car");
        eml = rs.getString("eml");
        por = rs.getDouble("poc");
        txtCod.setText(cod);
        txtNom.setText(nom);
        txtCar.setText(car);
        txtEml.setText(eml);
        txtPor.setText(MtoEs(por, 2));
      }
      txtCod.requestFocus();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Vendedor.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    txtPor = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jMsg = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    jLabel8 = new javax.swing.JLabel();
    txtCod = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    txtEml = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    txtCar = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Vendedor");

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtPor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtPor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtPor.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtPorMouseClicked(evt);
      }
    });
    txtPor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtPorActionPerformed(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("NOMBRE");

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jMsg.setForeground(new java.awt.Color(204, 0, 0));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Vendedor.png"))); // NOI18N
    jLabel2.setText("   REGISTRO - VENDEDOR");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    txtNom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNom.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNomMouseClicked(evt);
      }
    });
    txtNom.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNomActionPerformed(evt);
      }
    });

    jLabel6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(51, 51, 102));
    jLabel6.setText("% COMISION");

    jLabel8.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(51, 51, 102));
    jLabel8.setText("CODIGO");

    txtCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCod.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCodMouseClicked(evt);
      }
    });
    txtCod.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCodActionPerformed(evt);
      }
    });

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("EMAIL");

    txtEml.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtEml.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtEmlMouseClicked(evt);
      }
    });
    txtEml.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtEmlActionPerformed(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("CARGO");

    txtCar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCar.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtCarMouseClicked(evt);
      }
    });
    txtCar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtCarActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(20, 20, 20)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
              .addComponent(txtPor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel4)
              .addComponent(txtNom)
              .addComponent(txtEml)
              .addComponent(txtCar)
              .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(0, 16, Short.MAX_VALUE))))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtCar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(1, 1, 1)
        .addComponent(txtEml, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtPor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
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
        .addGap(25, 25, 25)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnGra)))
        .addContainerGap(13, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jButton1)
          .addComponent(btnGra))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
    String vax = "Desea Grabar Registro ?";
    String[] options = {"SI", "NO"};
    int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
    if (opcion == 0) {
      cod = txtCod.getText().toUpperCase().trim();
      nom = txtNom.getText().toUpperCase().trim();
      car = txtCar.getText().toUpperCase().trim();
      eml = txtEml.getText().toLowerCase().trim();
      String mox = txtPor.getText();
      mox = GetCurrencyDouble(mox);
      por = GetMtoDouble(mox);

      if (validaCampos()) {

        try {
          ConexionSQL bdsql = new ConexionSQL();
          Connection con = bdsql.Conectar();
          Statement st = con.createStatement();
          String sql = "DELETE FROM Vendedor";
          st.execute(sql);
          if (cod.length() > 10) {
            cod = cod.substring(0, 10);
          }
          if (nom.length() > 30) {
            nom = nom.substring(0, 30);
          }
          if (car.length() > 30) {
            car = car.substring(0, 30);
          }
          if (eml.length() > 50) {
            eml = eml.substring(0, 40);
          }
          sql = "Insert into Vendedor "
            + "(cod,nom,car,eml,poc) "
            + "VALUES ('" + cod + "','" + nom + "','" + car + "','" + eml + "'," + por + ")";
          st.execute(sql);
          jMsg.setText(" - Se grabo con Exito");
          con.close();
        } catch (SQLException ex) {
          Logger.getLogger(Registro_Vendedor.class.getName()).log(Level.SEVERE, null, ex);
        }

      }
    }
  }//GEN-LAST:event_btnGraActionPerformed


  private void txtPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorActionPerformed
    btnGra.requestFocus();
  }//GEN-LAST:event_txtPorActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegVen.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtPorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPorMouseClicked
  }//GEN-LAST:event_txtPorMouseClicked

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    txtCar.requestFocus();
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCodMouseClicked

  private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
    txtNom.requestFocus();
  }//GEN-LAST:event_txtCodActionPerformed

  private void txtEmlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtEmlMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtEmlMouseClicked

  private void txtEmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmlActionPerformed
    txtPor.requestFocus();    // TODO add your handling code here:
  }//GEN-LAST:event_txtEmlActionPerformed

  private void txtCarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCarMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtCarMouseClicked

  private void txtCarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCarActionPerformed
    txtEml.requestFocus();
  }//GEN-LAST:event_txtCarActionPerformed

  public boolean validaCampos() {
    indok = 0;
    jMsg.setText("");
    // Valida Codigo
    if (cod.length() > 0) {
      indok = 1;
    } else {
      jMsg.setText(" - Debe Ingresar Codigo");
      txtCod.requestFocus();
    }
    //Valida Nombre
    if (indok == 1) {
      indok = 0;
      if (nom.length() > 0) {
        indok = 1;
      } else {
        jMsg.setText(" - Debe ingresar Nombre");
        txtNom.requestFocus();
      }
      //Valida Email
      if (eml.length() > 0) {
        indok = 0;
        if (validaEmail(eml)) {
          indok = 1;
        } else {
          jMsg.setText(" - Email Invalido");
          txtEml.requestFocus();
        }
      }
    }
    if (indok == 1) {
      return true;
    } else {
      return false;
    }
  }

// Metodo Principal
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
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Registro_Vendedor.class
        .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Vendedor().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnGra;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jMsg;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JTextField txtCar;
  private javax.swing.JTextField txtCod;
  private javax.swing.JTextField txtEml;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtPor;
  // End of variables declaration//GEN-END:variables
}
