package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.pintarImagen;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import static gestionFZ.Registro_CatalogoProductos.ImgPrd;

public class Consulta_ImagenProd extends javax.swing.JFrame {

  String dep = "";   // departamento
  String nom = "";   // descripcion producto
  double pum = 0;    // precio mayor producto
  double pud = 0;    // Precio detal prpducto
  double stk = 0;    // Precio detal prpducto

  public Consulta_ImagenProd(String rut, String cop) {

    initComponents();

    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    pintarImagen(labImg, rut);
    if (!buscaProducto(cop)) {
      labNom.setText("- no existe -");
    }
    buscaEmpaquPrd(cop);

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        ImgPrd.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        ImgPrd.dispose();
      }
    });
  }

  public boolean buscaProducto(String cop) {
    nom = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT dep,nom,pud,pum,stk "
        + "FROM listaprecios "
        + "where cop =  '" + cop + "'";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        dep = rs.getString("dep");
        nom = rs.getString("nom");
        pum = rs.getDouble("pum");
        pud = rs.getDouble("pud");
        stk = rs.getDouble("stk");
      }
      if (stk < 0) {
        stk = 0;
      }
      labDep.setText(dep);
      labNom.setText(nom);
      labCod.setText(cop);
      labPum.setText(MtoEs(pum, 2));
      labPud.setText(MtoEs(pud, 2));
      labStk.setText(MtoEs(stk, 2).replace(",00", ""));
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (nom.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  public boolean buscaEmpaquPrd(String cop) {
    String emp = "", ref = "";
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      String sql = "SELECT tx1,unm,ref,can  from empaqueproducto "
        + "Where cop='" + cop + "'";
      ResultSet rs = st.executeQuery(sql);
      while (rs.next()) {
        emp = rs.getString("tx1");
        ref = rs.getString("ref");
        labEmp.setText(emp.trim() + "    -    " + ref);
      }

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Clientes.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (nom.length() > 0) {
      return true;
    } else {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    labc0 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    labNom = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    labImg = new javax.swing.JLabel();
    labDep = new javax.swing.JLabel();
    labCod = new javax.swing.JLabel();
    labPum = new javax.swing.JLabel();
    labStk = new javax.swing.JLabel();
    labc1 = new javax.swing.JLabel();
    labc2 = new javax.swing.JLabel();
    labPud = new javax.swing.JLabel();
    labc3 = new javax.swing.JLabel();
    labEmp = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    labc0.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labc0.setForeground(new java.awt.Color(102, 0, 0));
    labc0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labc0.setText("Codigo");

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    labNom.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labNom.setForeground(new java.awt.Color(0, 0, 153));
    labNom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labNom.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labImg.setText(" ");
    labImg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

    labDep.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labDep.setForeground(new java.awt.Color(102, 0, 0));
    labDep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labDep.setText(" ");
    labDep.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(67, 67, 67)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(labImg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labDep, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE))
        .addContainerGap(67, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(labDep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(labImg, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    labCod.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labCod.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCod.setText(" ");
    labCod.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labPum.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labPum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labPum.setText(" ");
    labPum.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labStk.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labStk.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labStk.setText(" ");
    labStk.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labStk.setPreferredSize(new java.awt.Dimension(8, 25));

    labc1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labc1.setForeground(new java.awt.Color(102, 0, 0));
    labc1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labc1.setText("Precio Mayor");

    labc2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labc2.setForeground(new java.awt.Color(102, 0, 0));
    labc2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labc2.setText("Existencia");

    labPud.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    labPud.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labPud.setText(" ");
    labPud.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    labPud.setPreferredSize(new java.awt.Dimension(8, 25));

    labc3.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labc3.setForeground(new java.awt.Color(102, 0, 0));
    labc3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labc3.setText("Precio Detal");

    labEmp.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
    labEmp.setForeground(new java.awt.Color(51, 51, 51));
    labEmp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labEmp.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(13, 13, 13)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButton1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(labc0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addComponent(labCod, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(labc1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(labPum, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(labc3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(23, 23, 23))
              .addGroup(layout.createSequentialGroup()
                .addComponent(labPud, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(labc2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(92, 92, 92))
              .addGroup(layout.createSequentialGroup()
                .addComponent(labStk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(51, 51, 51))))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(labNom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(labEmp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap())))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(labNom, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(labEmp, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labc0)
          .addComponent(labc1)
          .addComponent(labc2)
          .addComponent(labc3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labCod, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labPum, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labPud, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labStk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jButton1))
        .addContainerGap(17, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    ImgPrd.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

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
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_ImagenProd.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Consulta_ImagenProd("", "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JLabel labCod;
  private javax.swing.JLabel labDep;
  private javax.swing.JLabel labEmp;
  private javax.swing.JLabel labImg;
  private javax.swing.JLabel labNom;
  private javax.swing.JLabel labPud;
  private javax.swing.JLabel labPum;
  private javax.swing.JLabel labStk;
  private javax.swing.JLabel labc0;
  private javax.swing.JLabel labc1;
  private javax.swing.JLabel labc2;
  private javax.swing.JLabel labc3;
  // End of variables declaration//GEN-END:variables
}
