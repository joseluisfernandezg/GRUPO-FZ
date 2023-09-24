package gestionFZ;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.collections.ObservableList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.Clientes;
import static gestionFZ.Registro_NotaEntrega.conCli;

public class Consulta_Clientes extends javax.swing.JFrame {

  private DefaultListModel modelo;

  String coc = "", noc = "", rif = "", tlf = "";
  String fil = "";
  String format = " %1$-10s  %2$-35s  %3$-12s  %4$-20s\n";

  Registro_NotaEntrega ctrlN;

  ObservableList<Clientes> obsClientes;  // ObservableList modelo tabla Clientes

  public Consulta_Clientes(Registro_NotaEntrega ctrl) {
    ctrlN = ctrl;
    ConsultaClientes();
  }

  public void ConsultaClientes() {

    initComponents();
    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    String vax = String.format(format, " CODIGO", " NOMBRE CLIENTE", " RIF", " TLF").toUpperCase();
    jTiT.setText(vax);

    btnSel.setVisible(false);
    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          Seleccionar();
        }
      }
    });

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        conCli.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        conCli.dispose();
      }
    });

    listaObsList();
    txtBus.setText(" ");
    txtBus.requestFocus();
  }

  // Selecciona Registro
  public void Seleccionar() {
    int[] x = jList.getSelectedIndices();
    int idx = x.length;
    idx = idx - 1;
    String Ref = String.valueOf(modelo.getElementAt(x[idx]));
    Ref = Ref.trim();
    if (Ref.length() >= 20) {
      coc = Ref.substring(0, 10).trim();
      ctrlN.recibeCodigoCte(coc);
      conCli.dispose();
    }
  }

  // Lista Datos
  public void listaObsList() {
    modelo.clear();
    Clientes c = new Clientes();
    obsClientes = c.getObsListClientes(fil, "", "0");
    for (Clientes cte : obsClientes) {
      coc = cte.getCoc();
      noc = cte.getNom();
      rif = cte.getRif();
      tlf = cte.getTlf();
      if (noc.length() > 35) {
        noc = noc.substring(0, 35);
      }
      if (tlf.length() > 20) {
        noc = noc.substring(0, 20);
      }
      noc = noc.toUpperCase();
      String vax = String.format(format, coc, noc, rif, tlf);
      modelo.addElement(vax);
    }
    jList.setModel(modelo);
    btnSel.setVisible(false);
    if (!modelo.isEmpty()) {
      btnSel.setVisible(true);
      //jList.setSelectedIndex(0);
      lanCan.setText("Cant =  " + modelo.size());
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
    btnBus = new javax.swing.JButton();
    btnSel = new javax.swing.JButton();
    txtBus = new javax.swing.JTextField();
    Cliente = new javax.swing.JLabel();
    lanCan = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Clte.png"))); // NOI18N
    jLabel3.setText("CONSULTA CLIENTES");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jList.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jList.setOpaque(false);
    jScrollPane1.setViewportView(jList);

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    jTiT.setOpaque(true);

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    btnBus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
    btnBus.setText("Buscar");
    btnBus.setPreferredSize(new java.awt.Dimension(77, 30));
    btnBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBusActionPerformed(evt);
      }
    });

    btnSel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ok2.png"))); // NOI18N
    btnSel.setText("Seleccionar");
    btnSel.setPreferredSize(new java.awt.Dimension(77, 25));
    btnSel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSelActionPerformed(evt);
      }
    });

    txtBus.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBus.setToolTipText("Buscar Clientes");
    txtBus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtBus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtBus.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtBusMouseClicked(evt);
      }
    });
    txtBus.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtBusActionPerformed(evt);
      }
    });
    txtBus.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtBusKeyReleased(evt);
      }
    });

    Cliente.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    Cliente.setForeground(new java.awt.Color(51, 51, 102));
    Cliente.setText(" Cliente");

    lanCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    lanCan.setForeground(new java.awt.Color(51, 51, 102));
    lanCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lanCan.setText(" ");
    lanCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addGap(23, 23, 23)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addComponent(btnSal)
                    .addGap(38, 38, 38)
                    .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                  .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addContainerGap(19, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(Cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtBus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(18, 18, 18)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnSal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(lanCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    conCli.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
    listaObsList();
  }//GEN-LAST:event_btnBusActionPerformed

  private void btnSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelActionPerformed
    Seleccionar();
  }//GEN-LAST:event_btnSelActionPerformed

  private void txtBusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBusMouseClicked
    if (txtBus.getText().length() == 1) {
      fil = "";
      listaObsList();
    }
    txtBus.setText(" ");
    txtBus.requestFocus();
  }//GEN-LAST:event_txtBusMouseClicked

  private void txtBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusActionPerformed
    if (txtBus.getText().length() == 0) {
      fil = "";
    }
  }//GEN-LAST:event_txtBusActionPerformed

  private void txtBusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBusKeyReleased
    fil = txtBus.getText().toUpperCase().trim();
    listaObsList();
  }//GEN-LAST:event_txtBusKeyReleased

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
      java.util.logging.Logger.getLogger(Consulta_Clientes.class
              .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      Registro_NotaEntrega ctrl = new Registro_NotaEntrega();

      public void run() {
        new Consulta_Clientes(ctrl).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente;
  private javax.swing.JButton btnBus;
  private javax.swing.JButton btnSal;
  private javax.swing.JButton btnSel;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel lanCan;
  private javax.swing.JTextField txtBus;
  // End of variables declaration//GEN-END:variables
}
