package gestionFZ;

import static comun.MetodosComunes.isNumeric;
import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.ConexionSQL;
import static gestionFZ.Menu.RegCop;

public class Registro_Copa extends javax.swing.JFrame {

  int indok = 0;
  String cod = "";
  String nom = "";
  String ndi = "";

  ImageIcon icon;

  // Metodo Constructor - Inicializa valores
  public Registro_Copa() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    tabCop.setRowHeight(25);//tamaño de las celdas
    tabCop.setGridColor(new java.awt.Color(0, 0, 0));
    tabCop.setSelectionBackground(new Color(151, 193, 215));
    tabCop.setSelectionForeground(Color.blue);
    tabCop.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = tabCop.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(10); // Codigo 
    columnModel.getColumn(0).setPreferredWidth(60); // descripcion 
    columnModel.getColumn(0).setPreferredWidth(10); // dias

    //Alinea Columnas Header
    AlinCampoH(0, 2); //  codigo
    AlinCampoH(1, 0); //  descripcion 
    AlinCampoH(2, 2); //  dias

    //Alinea Columnas Detail
    AlinCampoD(0, 2); //  codigo
    AlinCampoD(1, 0); //  descripcion 
    AlinCampoD(2, 2); //  dias  

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        RegCop.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        RegCop.dispose();
      }
    });

    txtCod.setEditable(true);
    txtCod.requestFocus();
    ListaDatos();

    tabCop.requestFocus();
    tabCop.getSelectionModel().setSelectionInterval(1, 1); // Marca fila como seleccionada

    txtCod.requestFocus();

  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    tabCop = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtCod = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jMsg = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    txtNom = new javax.swing.JTextField();
    txtDia = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    btnEli = new javax.swing.JButton();
    btnGra = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("CONDICION DE PAGO");

    tabCop.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    tabCop.setForeground(new java.awt.Color(51, 51, 51));
    tabCop.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "CODIGO", "DESCRIPCION", "DIAS"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    tabCop.setRowHeight(25);
    tabCop.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        tabCopMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(tabCop);
    tabCop.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (tabCop.getColumnModel().getColumnCount() > 0) {
      tabCop.getColumnModel().getColumn(0).setResizable(false);
      tabCop.getColumnModel().getColumn(0).setPreferredWidth(10);
      tabCop.getColumnModel().getColumn(1).setResizable(false);
      tabCop.getColumnModel().getColumn(1).setPreferredWidth(80);
      tabCop.getColumnModel().getColumn(2).setResizable(false);
      tabCop.getColumnModel().getColumn(2).setPreferredWidth(20);
    }

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtCod.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
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

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("DIAS CREDITO");

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jMsg.setForeground(new java.awt.Color(204, 0, 0));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Copa.png"))); // NOI18N
    jLabel2.setText("   REGISTRO - C. PAGO");
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

    txtDia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtDia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtDia.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtDiaMouseClicked(evt);
      }
    });
    txtDia.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtDiaActionPerformed(evt);
      }
    });

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("CODIGO");

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("DESCRIPCION");

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(jLabel2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(4, 4, 4)
        .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtDia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(labojo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );

    jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    jButton1.setText("Salir");
    jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    btnEli.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
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

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34)
            .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnEli, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btnEli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jButton1)
          .addComponent(btnGra))
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void tabCopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabCopMouseClicked

  }//GEN-LAST:event_tabCopMouseClicked

  private void btnEliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliActionPerformed
    DefaultTableModel model = (DefaultTableModel) tabCop.getModel();
    int[] sel = tabCop.getSelectedRows();
    jMsg.setText("");
    if (tabCop.getRowCount() > 0) {
      if (tabCop.getSelectedRow() >= 0) {
        icon = new ImageIcon(getClass().getResource("/img/elim.png"));
        String vax = "Desea Eliminar Registro Seleccionado ?";
        if (sel.length > 1) {
          vax = "Desea Eliminar Registros Seleccionados ?";
        }
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          //Borra Seleccionadas
          try {
            ConexionSQL bdsql = new ConexionSQL();
            Connection con = bdsql.Conectar();
            Statement st = con.createStatement();
            int numRows = tabCop.getSelectedRows().length;
            for (int i = 0; i < numRows; i++) {
              cod = model.getValueAt(tabCop.getSelectedRow(), 0).toString();
              model.removeRow(tabCop.getSelectedRow());
              String sql = "delete from Copa "
                + "where cop = '" + cod + "' ";
              st.execute(sql);
            }
            jMsg.setText(" - Se elimino Banco  " + nom + " -");
            txtCod.requestFocus();
            st.close();
            con.close();
          } catch (SQLException ex) {
            Logger.getLogger(Registro_Copa.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } else {
        jMsg.setText(" - Debe Seleccionar un registro");
      }
    } else {
      jMsg.setText(" - Tabla Vacia");
    }
  }//GEN-LAST:event_btnEliActionPerformed

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed
    DefaultTableModel model = (DefaultTableModel) tabCop.getModel();

    if (validaCampos() == true) {
      try {
        String vax = "";
        //Incluir / Modificar
        ConexionSQL bdsql = new ConexionSQL();
        Connection con = bdsql.Conectar();
        Statement st = con.createStatement();
        icon = new ImageIcon(getClass().getResource("/img/salvar.png"));
        vax = "Desea Grabar \n( " + nom + " ) ?";
        String[] options = {"SI", "NO"};
        int opcion = JOptionPane.showOptionDialog(null, vax, "* Aviso *", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon, options, options[1]);
        if (opcion == 0) {
          if (nom.length() > 20) {
            nom = nom.substring(0, 20);
          }
          String sql = "Delete from Copa "
            + "where cop =  '" + cod + "' ";
          st.execute(sql);
          // Incluir
          sql = "Insert into Copa "
            + "(cop,nom,ndi) "
            + "VALUES ('" + cod + "','" + nom + "','" + ndi + "')";
          st.execute(sql);
          jMsg.setText("- Se Grabo registro -");
          txtCod.setText("");
          txtCod.requestFocus();
          ListaDatos();
          con.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(Registro_Copa.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else {
      jMsg.setText("- Hay errores -");
    }
  }//GEN-LAST:event_btnGraActionPerformed

  private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
    cod = txtCod.getText();
    cod = cod.toUpperCase();
    txtCod.setText(cod);
    txtNom.requestFocus();
  }//GEN-LAST:event_txtCodActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    RegCop.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtCodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCodMouseClicked
    nom = txtCod.getText().trim();
  }//GEN-LAST:event_txtCodMouseClicked

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
    nom = txtNom.getText();
    nom = nom.toUpperCase();
    txtNom.setText(nom);
    txtDia.requestFocus();
  }//GEN-LAST:event_txtNomActionPerformed

  private void txtDiaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDiaMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtDiaMouseClicked

  private void txtDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiaActionPerformed
    String dix = txtDia.getText().trim();
    if (!isNumeric(dix)) {
      txtDia.requestFocus();
    } else {
      btnGra.requestFocus();
    }
  }//GEN-LAST:event_txtDiaActionPerformed

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
    tabCop.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    tabCop.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
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
    tabCop.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  public void ListaDatos() {
    DefaultTableModel model = (DefaultTableModel) tabCop.getModel();
    model.setRowCount(0);
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT cop,nom,ndi  "
        + "FROM Copa "
        + "order by cop ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        cod = rs.getString("cop");
        nom = rs.getString("nom");
        ndi = rs.getString("ndi");
        model.addRow(new Object[]{cod, nom, ndi});
      }
      txtCod.requestFocus();

      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Copa.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public boolean validaCampos() {
    indok = 0;
    jMsg.setText("");

    cod = txtCod.getText();
    if (!isNumeric(cod) || cod.length() > 1) {
      jMsg.setText(" - Permitido 1 Digito numerico");
      txtCod.requestFocus();
      indok = 1;
    }

    nom = txtNom.getText().toUpperCase().trim();
    if (nom.length() == 0 || nom.length() > 20) {
      jMsg.setText(" - maximo 20 Caracteres");
      txtNom.requestFocus();
      indok = 1;
    }

    String dix = txtDia.getText();
    if (!isNumeric(dix)) {
      jMsg.setText(" - dias debe ser numerico");
      txtDia.requestFocus();
      indok = 1;
    } else {
      ndi = dix;
    }

    if (indok == 0) {
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
      java.util.logging.Logger.getLogger(Registro_Copa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Copa().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btnEli;
  private javax.swing.JButton btnGra;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jMsg;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel labojo;
  public javax.swing.JTable tabCop;
  private javax.swing.JTextField txtCod;
  private javax.swing.JTextField txtDia;
  private javax.swing.JTextField txtNom;
  // End of variables declaration//GEN-END:variables
}
