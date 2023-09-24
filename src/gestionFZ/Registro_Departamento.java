package gestionFZ;

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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import modelo.ConexionSQL;
import static gestionFZ.Registro_CatalogoProductos.comDep;

public class Registro_Departamento extends javax.swing.JFrame {

  int indok = 0;
  String nom = "";  // Nombre Departamento Grupo fz
  String tx0 = "";  // Nombre departamento correcto
  String tx1 = "";  // Comnentario 1 
  String tx2 = "";  // Comnentario 1 
  ImageIcon icon;

  DefaultTableModel model;

  // Metodo Constructor - Inicializa valores
  public Registro_Departamento() {
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/logosysven.jpg")));
    initComponents();
    this.setLocationRelativeTo(null); // centramos la ventana en la pantalla
    this.setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    this.setLayout(null);             // no usamos ningun layout para dar posiciones a los componentes

    model = (DefaultTableModel) jTabla.getModel();

    jTabla.setRowHeight(25);//tamaño de las celdas
    jTabla.setGridColor(new java.awt.Color(0, 0, 0));
    jTabla.setSelectionBackground(new Color(151, 193, 215));
    jTabla.setSelectionForeground(Color.blue);
    jTabla.getTableHeader().setReorderingAllowed(true);  // activa movimiefnto columnas
    jTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Ajusta tamaño Columnas
    TableColumnModel columnModel = jTabla.getColumnModel();
    columnModel.getColumn(0).setPreferredWidth(100); // Nombre  

    //Alinea Columnas Header
    AlinCampoH(0, 0); // 0=Izq
    AlinCampoH(1, 0); // 2=Center
    AlinCampoH(2, 0); // 2=Center

    //Alinea Columnas Detail
    AlinCampoD(0, 0); // 0=Izq
    AlinCampoD(1, 0); // =Center
    AlinCampoD(2, 0); // =Center

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        comDep.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        comDep.dispose();
      }
    });
    ListaDatos();
    bloqueaCampos();
  }

  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jTabla = new javax.swing.JTable();
    jPanel1 = new javax.swing.JPanel();
    txtNom = new javax.swing.JTextField();
    jLabel3 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    txtNom2 = new javax.swing.JTextField();
    jLabel5 = new javax.swing.JLabel();
    txtOb2 = new javax.swing.JTextField();
    btnGra = new javax.swing.JButton();
    txtOb1 = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();
    jMsg = new javax.swing.JLabel();
    labojo = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("DEPARTAMENTOS");

    jTabla.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jTabla.setForeground(new java.awt.Color(51, 51, 51));
    jTabla.setModel(new javax.swing.table.DefaultTableModel(
      new Object [][] {

      },
      new String [] {
        "DEPARTAMENTO ", "TEXTO A", "TEXTO B"
      }
    ) {
      boolean[] canEdit = new boolean [] {
        false, false, false
      };

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
      }
    });
    jTabla.setRowHeight(25);
    jTabla.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jTablaMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(jTabla);
    jTabla.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    if (jTabla.getColumnModel().getColumnCount() > 0) {
      jTabla.getColumnModel().getColumn(0).setResizable(false);
      jTabla.getColumnModel().getColumn(0).setPreferredWidth(150);
      jTabla.getColumnModel().getColumn(1).setResizable(false);
      jTabla.getColumnModel().getColumn(1).setPreferredWidth(60);
      jTabla.getColumnModel().getColumn(2).setResizable(false);
      jTabla.getColumnModel().getColumn(2).setPreferredWidth(60);
    }

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    txtNom.setEditable(false);
    txtNom.setBackground(new java.awt.Color(249, 249, 249));
    txtNom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNom.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

    jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 51, 102));
    jLabel3.setText("DEPARTAMENTO");

    jLabel2.setBackground(new java.awt.Color(217, 226, 226));
    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/editar.png"))); // NOI18N
    jLabel2.setText("   COMENTARIOS DEPARTAMENTO  ( Catalogo de Precios )");
    jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jLabel2.setOpaque(true);

    jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel4.setForeground(new java.awt.Color(51, 51, 102));
    jLabel4.setText("TEXTO B");

    txtNom2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtNom2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtNom2MouseClicked(evt);
      }
    });
    txtNom2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtNom2ActionPerformed(evt);
      }
    });

    jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel5.setForeground(new java.awt.Color(51, 51, 102));
    jLabel5.setText("NOMBRE DEPARTAMENTO CORRECTO ");

    txtOb2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtOb2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtOb2MouseClicked(evt);
      }
    });
    txtOb2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtOb2ActionPerformed(evt);
      }
    });

    btnGra.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnGra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar.png"))); // NOI18N
    btnGra.setMnemonic('A');
    btnGra.setText("Grabar");
    btnGra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnGra.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnGraActionPerformed(evt);
      }
    });

    txtOb1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtOb1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtOb1MouseClicked(evt);
      }
    });
    txtOb1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtOb1ActionPerformed(evt);
      }
    });

    jLabel6.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(51, 51, 102));
    jLabel6.setText("TEXTO A");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtOb2)
          .addComponent(txtOb1)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(txtNom, javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(txtNom2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 72, Short.MAX_VALUE)))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtNom, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtNom2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnGra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtOb1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(txtOb2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    jMsg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
    jMsg.setForeground(new java.awt.Color(204, 0, 0));
    jMsg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMsg.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    labojo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    labojo.setForeground(new java.awt.Color(102, 102, 102));
    labojo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ojop.png"))); // NOI18N
    labojo.setToolTipText("Notificacion de Mensaje");
    labojo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap(16, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(111, 111, 111)
            .addComponent(labojo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jMsg, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(14, 14, 14))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
          .addComponent(jMsg, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(labojo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaMouseClicked
    presenta();
  }//GEN-LAST:event_jTablaMouseClicked

  private void btnGraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraActionPerformed

    nom = txtNom.getText().toUpperCase().trim();
    tx0 = txtNom2.getText().toUpperCase().trim();
    tx1 = txtOb1.getText().toUpperCase().trim();
    tx2 = txtOb2.getText().toUpperCase().trim();

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
          // Borra registro
          String sql = "Update Departamento "
            + "set "
            + "nom='" + tx0 + "',"
            + "ob1='" + tx1 + "',"
            + "ob2='" + tx2 + "' "
            + "where dep='" + nom + "'";
          st.execute(sql);

          int row = jTabla.getSelectedRow();    // Fila Selecciuonada
          int col = jTabla.getSelectedColumn(); // Columna Seleccionada
          if ((row != -1) && (col != -1)) {
            model.setValueAt(tx1, jTabla.getSelectedRow(), 1);
            model.setValueAt(tx2, jTabla.getSelectedRow(), 2);
          }
          jMsg.setText("- Se Grabo registro -");
          bloqueaCampos();
          con.close();
        }
      } catch (SQLException ex) {
        Logger.getLogger(Registro_Departamento.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
  }//GEN-LAST:event_btnGraActionPerformed


  private void txtNomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomActionPerformed
  }//GEN-LAST:event_txtNomActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    comDep.dispose();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void txtNomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNomMouseClicked
    nom = txtNom.getText().trim();
  }//GEN-LAST:event_txtNomMouseClicked

  private void txtNom2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNom2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtNom2MouseClicked

  private void txtNom2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNom2ActionPerformed
    txtOb1.requestFocus();
  }//GEN-LAST:event_txtNom2ActionPerformed

  private void txtOb2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOb2MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtOb2MouseClicked

  private void txtOb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOb2ActionPerformed
    btnGra.requestFocus();
  }//GEN-LAST:event_txtOb2ActionPerformed

  private void txtOb1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOb1MouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_txtOb1MouseClicked

  private void txtOb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOb1ActionPerformed
    txtOb2.requestFocus();
  }//GEN-LAST:event_txtOb1ActionPerformed

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
    jTabla.getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
    jTabla.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
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
    jTabla.getColumnModel().getColumn(col).setCellRenderer(Alinea);
  }

  public void bloqueaCampos() {
    txtNom2.setEnabled(false);
    txtOb1.setEnabled(false);
    txtOb2.setEnabled(false);
    txtNom.setText("");
    txtNom2.setText("");
    txtOb1.setText("");
    txtOb2.setText("");
  }

  public void desbloqueaCampos() {
    txtNom2.setEnabled(true);
    txtOb1.setEnabled(true);
    txtOb2.setEnabled(true);
    txtNom.setText(nom);
    txtNom2.setText(tx0);
    txtOb1.setText(tx1);
    txtOb2.setText(tx2);
  }

  // Presenta Datos al pulsar mouse en detalle columna
  public void presenta() {
    jMsg.setText("");
    nom = model.getValueAt(jTabla.getSelectedRow(), 0).toString();
    int[] sel = jTabla.getSelectedRows();
    if (sel.length == 1) {
      BuscaDatos(nom);
    }
  }

  public void ListaDatos() {
    model.setRowCount(0);
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT dep,nom,ob1,ob2  "
        + "FROM Departamento "
        + "order by dep ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nom = rs.getString("dep");
        tx1 = rs.getString("ob1");
        tx2 = rs.getString("ob2");
        model.addRow(new Object[]{nom, tx1, tx2});
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Departamento.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  // Busca Datos  
  public boolean BuscaDatos(String cod) {
    int can = 0;
    try {
      ConexionSQL bdsql = new ConexionSQL();
      Connection con = bdsql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;
      String sql = "SELECT dep,nom,dep,ob1,ob2 "
        + "FROM Departamento "
        + "where dep = '" + cod + "' ";
      rs = st.executeQuery(sql);
      while (rs.next()) {
        nom = rs.getString("dep");
        tx0 = rs.getString("nom");
        tx1 = rs.getString("ob1");
        tx2 = rs.getString("ob2");
        can = 1;
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Registro_Departamento.class.getName()).log(Level.SEVERE, null, ex);
    }
    if (can > 0) {
      desbloqueaCampos();
      txtNom2.requestFocus();
      return true;
    } else {
      return false;
    }
  }

  public boolean validaCampos() {
    indok = 0;
    jMsg.setText("");
    // Valida observacion 1
    if (tx0.length() <= 50) {
      indok = 1;
      // Valida observacion 2 
      if (tx1.length() <= 60) {
        indok = 1;
        if (tx2.length() <= 60) {
          indok = 1;
        } else {
          jMsg.setText(" - Maximo 60 Caracteres");
          txtOb2.requestFocus();
          indok = 0;
        }
      } else {
        jMsg.setText(" - Maximo 60 Caracteres");
        txtOb1.requestFocus();
        indok = 0;
      }
    } else {
      jMsg.setText(" - Maximo 50 Caracteres");
      txtNom2.requestFocus();
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
      java.util.logging.Logger.getLogger(Registro_Departamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    /*
     /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Registro_Departamento().setVisible(true);
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
  private javax.swing.JLabel jMsg;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  public javax.swing.JTable jTabla;
  private javax.swing.JLabel labojo;
  private javax.swing.JTextField txtNom;
  private javax.swing.JTextField txtNom2;
  private javax.swing.JTextField txtOb1;
  private javax.swing.JTextField txtOb2;
  // End of variables declaration//GEN-END:variables
}
