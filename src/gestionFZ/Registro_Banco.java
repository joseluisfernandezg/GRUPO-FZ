package gestionFZ;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javafx.collections.ObservableList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.Bancos;
import static gestionFZ.Registro_PagosClientes.cargaBancos;
import static gestionFZ.Registro_PagosClientes.regBco;

public class Registro_Banco extends javax.swing.JFrame {

  int indok = 0;
  String nom = "";
  ImageIcon icon;
  DefaultTableModel model;

  // Metodo Constructor - Inicializa valores
  public Registro_Banco() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    model = (DefaultTableModel) tabBco.getModel();
    tabBco.setRowHeight(25);//tamaño de las celdas
    tabBco.setGridColor(new java.awt.Color(0, 0, 0));
    tabBco.setSelectionBackground(new Color(151, 193, 215));
    tabBco.setSelectionForeground(Color.blue);
    tabBco.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas

    TableColumnModel columnModel = tabBco.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(100); // Nombre  

    //Alinea Columnas Header
    AlinCampoH(0, 2); // 0=codigo  2=Center

    //Alinea Columnas Detail
    AlinCampoD(0, 2); // 0=Codigo  2=Center

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        cargaBancos();
        regBco.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        cargaBancos();
        regBco.dispose();
      }
    });

    ListaDatos();
    txtBco.requestFocus();

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    tabBco = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtBco = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    btnEli = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jMsg = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("BANCOS");

    tabBco.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    tabBco.setForeground(new java.awt.Color(51, 51, 51));
    tabBco.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "BANCO"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabBco.setColumnSelectionAllowed(true);
    tabBco.setRowHeight(25);
    tabBco.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabBcoMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabBco);
    tabBco.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (tabBco.getColumnModel().getColumnCount() > 0) {
      tabBco.getColumnModel().getColumn(0).setResizable(false);
    }

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtBco.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtBco.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtBcoMouseClicked(evt);
      }
    });
    txtBco.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtBcoActionPerformed(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("BANCO");

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Bco.png"))); // NOI18N
    jLabel2.setText("   REGISTRO -  BANCOS");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtBco, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(24, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(jLabel2)
        .addGap(18, 18, 18)
        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtBco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(44, Short.MAX_VALUE))
    );

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnEli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/elim.png"))); // NOI18N
    btnEli.setMnemonic('E');
    btnEli.setText("Eliminar");
    btnEli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnEli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnEliActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
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

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jMsg.setForeground(new java.awt.Color(204, 0, 0));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(15, 15, 15)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(31, 31, 31)
            .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnEli, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(18, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnEli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jButton1)
          .addComponent(btnGra)
          .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tabBcoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabBcoMouseClicked
    presenta();
  }//GEN-LAST:event_tabBcoMouseClicked

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed

    int[] sel = tabBco.getSelectedRows();
    jMsg.setText("");
    if (tabBco.getRowCount() > 0) {
      if (tabBco.getSelectedRow() >= 0) {
        icon = new ImageIcon(getClass().getResource("/img/elim.png"));
        String vax = "Desea Eliminar Registro Seleccionado ?";
        if (sel.length > 1) {
          vax = "Desea Eliminar Registros Seleccionados ?";
        }
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          //Borra Seleccionadas
          int numRows = tabBco.getSelectedRows().length;
          for (int i = 0; i < numRows; i++) {
            nom = model.getValueAt(tabBco.getSelectedRow(), 0).toString();
            model.removeRow(tabBco.getSelectedRow());
            Bancos b = new Bancos();
            if (b.eliminarBanco(nom)) {
              jMsg.setText(" - Se elimino Banco  " + nom + " -");
            }
          }
          txtBco.setText("");
          txtBco.requestFocus();
        }
      } else {
        jMsg.setText(" - Debe Seleccionar un registro");
      }
    } else {
      jMsg.setText(" - Tabla Vacia");
    }
  }//GEN-LAST:event_btnEliActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    nom = txtBco.getText().trim();
    if (validaCampos() == true) {
      nom = nom.toUpperCase();
      icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
      String vax = "Desea Grabar \n( " + nom + " ) ?";
      String[] options = {"SI", "NO"};
      int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
      if (opcion == 0) {
        Bancos b = new Bancos(nom);
        if (!b.existeBanco(nom)) {
          if (nom.length() > 20) {
            nom = nom.substring(0, 20);
          }
          // Incluir
          b.insertarBanco();
          model.addRow(new Object[]{nom});
          // Posiciona ultima file tabDoc;
          int i = tabBco.getRowCount() - 1;    // Cantidad Filas
          tabBco.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
          Rectangle oRect = tabBco.getCellRect(i, 0, true);
          tabBco.scrollRectToVisible(oRect);
          jMsg.setText("- Se Grabo registro -");
          txtBco.setText("");
        } else {
          //model.setValueAt(nom, tabBco.getSelectedRow(), 0); // presenta cambio
          txtBco.setSelectionStart(0);
          txtBco.setSelectionEnd(txtBco.getText().length());
          jMsg.setText("- Banco Existe -");
        }
        txtBco.requestFocus();
      }
    }
  }//GEN-LAST:event_btnGraActionPerformed

  public void MarcarColumna() {
    int rows = tabBco.getRowCount();    // Cantidad Filas
    int cols = tabBco.getColumnCount(); // Cantidad Columnas
    String cox = "";
    //Recorre filas (rows)
    for (int i = 0; i < rows; i++) {
      cox = tabBco.getValueAt(i, 0).toString().trim();
      if (cox.equals(nom)) {
        model.setValueAt(nom, i, 0);
        tabBco.getSelectionModel().setSelectionInterval(i, i); // Marca fila como seleccionada 
        System.out.println("spm");
        tabBco.requestFocus();
      }
    }
  }

  private void txtBcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBcoActionPerformed
    nom = txtBco.getText();
    nom = nom.toUpperCase();
    txtBco.setText(nom);
    btnGra.doClick();
  }//GEN-LAST:event_txtBcoActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    cargaBancos();
    regBco.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtBcoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBcoMouseClicked
    txtBco.setSelectionStart(0);
    txtBco.setSelectionEnd(txtBco.getText().length());
    txtBco.requestFocus();
    nom = txtBco.getText().trim();
  }//GEN-LAST:event_txtBcoMouseClicked

  // Alinea Campos Columnas Header
  private void AlinCampoH(int col, int ind) {
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
    if (ind == 0) {
      cellRenderer.setHorizontalAlignment(JLabel.LEFT);
      headerRenderer.setHorizontalAlignment(JLabel.LEFT);
    }
    if (ind == 1) {
      cellRenderer.setHorizontalAlignment(JLabel.RIGHT);
      headerRenderer.setHorizontalAlignment(JLabel.RIGHT);
    }
    if (ind == 2) {
      cellRenderer.setHorizontalAlignment(JLabel.CENTER);
      headerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }
    headerRenderer.setBackground(new java.awt.Color(0, 153, 140));
    headerRenderer.setForeground(Color.WHITE);
    tabBco.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    tabBco.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
  }

  // Alinea Campos Columnas Detalle
  private void AlinCampoD(int col, int ind) {
    DefaultTableCellRenderer Alinea = new DefaultTableCellRenderer();
    if (ind == 1) {
      Alinea.setHorizontalAlignment(SwingConstants.RIGHT);
    }
    if (ind == 2) {
      Alinea.setHorizontalAlignment(SwingConstants.CENTER);
    }
    tabBco.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  // Presenta Datos al pulsar mouse en detalle columna
  public void presenta() {
    jMsg.setText("");
    nom = model.getValueAt(tabBco.getSelectedRow(), 0).toString();
    int[] sel = tabBco.getSelectedRows();
    if (sel.length == 1) {
      txtBco.setText(nom);
    } else {
      IniciaCampos();
    }
  }

  // Inicia Campos
  public void IniciaCampos() {
    nom = "";
    txtBco.setText(nom);
    txtBco.setEditable(true);
    txtBco.requestFocus();
  }

  public void ListaDatos() {
    model.setRowCount(0);
    ObservableList<Bancos> obsBancos;
    Bancos b = new Bancos();
    obsBancos = b.getBancos();
    for (Bancos bco : obsBancos) {
      nom = bco.getBco();
      model.addRow(new Object[]{nom});
    }
    int can = tabBco.getRowCount();
    labCan.setText("" + can);
  }

  public boolean validaCampos() {
    indok = 0;
    jMsg.setText("");
    // Valida Cedula
    if (nom.length() > 0) {
      indok = 1;
    } else {
      jMsg.setText(" - Debe Ingresar Nombre banco");
      txtBco.requestFocus();
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
      java.util.logging.Logger.getLogger(Registro_Banco.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Banco().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jMsg;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labCan;
  private javax.swing.JLabel labojo;
  public javax.swing.JTable tabBco;
  private javax.swing.JTextField txtBco;
  // End of variables declaration//GEN-END:variables
}
