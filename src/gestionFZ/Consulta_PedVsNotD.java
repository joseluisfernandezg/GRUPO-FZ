package gestionFZ;

import static comun.MetodosComunes.MtoEs;
import static comun.MetodosComunes.getdiasFec;
import static comun.MetodosComunes.ymd_dmy;
import static comun.MetodosComunes.ymdhoy;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import modelo.ConexionSQL;
import static gestionFZ.Consulta_PedVsNotR.DetPed;

public class Consulta_PedVsNotD extends javax.swing.JFrame {

  private DefaultListModel modelo;
  String Ref = "";

  String Fed = "", Feh = "";
  String ord = "0";
  String format = " %1$-10s  %2$-10s  %3$-10s  %4$-10s  %5$8s   %6$15s  %7$15s  %8$15s\n";

  int nno = 0, npd = 0;
  String noc = "", coc = "", fno = "", fep = "";

  double tno = 0, tpm = 0, tpd = 0;
  double tgn = 0, tgp = 0;

  public Consulta_PedVsNotD(String cox, String nox, String fdx, String fhx) {

    coc = cox;
    noc = nox;
    Fed = fdx;
    Feh = fhx;

    initComponents();

    txtcli.setText(" " + noc);
    txtFed.setText(ymd_dmy(fdx));
    txtFeh.setText(ymd_dmy(fhx));

    setLocationRelativeTo(null); // centramos la ventana en la pantalla
    setResizable(false);         // hacemos que la ventana no sea redimiensionable 
    setLayout(null);

    String vax = String.format(format,
      "N.Nota", " F.Nota", " N.Pedido", " F.Pedido", "   N.Dias", "  T.Nota $", "  T.Pedido $", "  T.Dif $");
    jTiT.setText(vax);

    modelo = new DefaultListModel();

    jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jList.setSelectionBackground(Color.CYAN);
    jList.setSelectionForeground(Color.BLUE);

    //Seccion Mouse (Doble Click)
    jList.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2 && modelo.size() > 0) {
          //Seleccionar();
        }
      }
    });

    jList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {

        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Consulta_PedVsNotD.jListColor) {
          Consulta_PedVsNotD.jListColor nextRegistro = (Consulta_PedVsNotD.jListColor) value;
          String nam = nextRegistro.name;
          setText(nam);

          if (nextRegistro.loggedIn) {
            setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
          } else {
            if (nam.indexOf("Gastos") > 0) {
              //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
              setForeground(new java.awt.Color(153, 0, 51)); // Rojo (Negativo)
              //setBackground(Color.LIGHT_GRAY);
            } else {
              if (nam.indexOf("------") == -1 && nam.indexOf("───") == -1) {
                //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
                setForeground(Color.BLACK);
              } else {
                setForeground(Color.BLACK);
              }
            }
          }

          if (nam.indexOf("TOTAL") >= 0) {
            setBackground(Color.GRAY);
            //setForeground(new java.awt.Color(0, 0, 102)); // Azul Oscuro
            setForeground(Color.WHITE); // Azul Oscuro
          }
          // setForeground(new java.awt.Color(153, 0, 102)); // Rojo (Negativo)

          if (isSelected) {
            Ref = nam;
            //setBackground(getBackground().darker());
            //setBackground(Color.ORANGE);
          }
        } else {
          setText(null);
        }
        return c;
      }
    });

    ListaDatos();

    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        DetPed.dispose();
      }
    });

    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
      javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        DetPed.dispose();
      }
    });

  }

  //Color Jlist
  static class jListColor {

    String name = "NN";
    boolean loggedIn = false;

    public jListColor(String name, double mto, boolean loggedIn) {
      this.name = name;
      this.loggedIn = loggedIn;
      if (mto < 0) {
        this.loggedIn = true;
      }
    }
  }

  // Lista Datos
  public void ListaDatos() {

    modelo.clear();

    int can = 0;
    int cap = 0;
    int cgn = 0;
    int cgp = 0;

    tno = 0;
    tpm = 0;
    tpd = 0;
    tgn = 0;
    tgp = 0;

    try {
      ConexionSQL mysql = new ConexionSQL();
      Connection con = mysql.Conectar();
      Statement st = con.createStatement();
      ResultSet rs = null;

      String sql = "DECLARE GLOBAL TEMPORARY TABLE SESSION.Saldos ("
        + "nnt  int,"
        + "fno  varchar(10),"
        + "npd  int,"
        + "fep  varchar(10),"
        + "ton  Decimal(15,2),"
        + "tpm  Decimal(15,2),"
        + "tpd  Decimal(15,2)) "
        + "NOT LOGGED ON COMMIT PRESERVE ROWS";
      st.execute(sql);

      sql = "insert into SESSION.Saldos "
        + "SELECT nne nno,fne fno, npe npd,fep,tne tno,0 tpm,0 tpd "
        + "FROM notaent "
        + "where coc = '" + coc + "' "
        + "  and fne between '" + Fed + "' and '" + Feh + "'";
      st.execute(sql);

      sql = "insert into SESSION.Saldos "
        + "SELECT ncr nno,fnc fne,0 npd,'' fep,tne tno,0 tpm,0 tpd "
        + "FROM notaCred "
        + "where coc ='" + coc + "' "
        + "  and fnc between '" + Fed + "' and '" + Feh + "'";
      st.execute(sql);

      // total pedidos clientes mayor
      sql = "update SESSION.Saldos "
        + "set tpm = (select  nullif((sum(can*prm)),0) from pedidoD x,pedidoH y "
        + "where y.tip='0' and SESSION.Saldos.npd=x.npe and x.npe=y.npe and fep between '" + Fed + "' and '" + Feh + "')";
      st.execute(sql);

      // total pedidos clientes detal
      sql = "update SESSION.Saldos "
        + "set tpd = (select  nullif((sum(can*prd)),0) from pedidoD x,pedidoH y "
        + "where y.tip='1' and SESSION.Saldos.npd=x.npe and x.npe=y.npe and fep between '" + Fed + "' and '" + Feh + "')";
      st.execute(sql);

      sql = "Select nnt,fno,npd,fep,ton,tpm,tpd,0 tdf "
        + "from SESSION.Saldos "
        + "Order by ton desc";

      rs = st.executeQuery(sql);
      while (rs.next()) {

        nno = rs.getInt("nnt");
        fno = rs.getString("fno");
        npd = rs.getInt("npd");
        fep = rs.getString("fep");
        tno = rs.getDouble("ton");
        tpm = rs.getDouble("tpm");
        tpd = rs.getDouble("tpd");

        double tpe = tpm + tpd;
        int ndi = 0;

        // Fecha Nota
        String fnx = ymd_dmy(fno);
        String fpx = "";
        // Fecha pedido
        if (fep.length() == 8) {
          fpx = ymd_dmy(fep);
          ndi = getdiasFec(fno, fep);
        } else {
          ndi = getdiasFec(ymdhoy(), fep);
        }

        if (npd == 0) {
          tpe = tno;
        }

        String tx1 = MtoEs(tno, 2);
        String tx2 = MtoEs(tpe, 2);
        String tx3 = MtoEs(tno - tpe, 2);

        String vax = String.format(format, nno, fnx, npd, fpx, ndi, tx1, tx2, tx3);
        modelo.addElement(new Consulta_PedVsNotD.jListColor(vax, 0, false));

        cgn = cgn + can;
        cgp = cgp + cap;

        tgn = tgn + tno;
        tgp = tgp + tpe;

      }

      jList.setModel(modelo);
      if (!modelo.isEmpty()) {
        jList.setSelectedIndex(0);
        labCan.setText("Cant =  " + modelo.size());

        String tx1 = MtoEs(tgn, 2);
        String tx2 = MtoEs(tgp, 2);
        String tx3 = MtoEs(tgn - tgp, 2);

        String vax = String.format(format, "", "", "", "", "TOTAL", tx1, tx2, tx3);
        modelo.addElement(new Consulta_PedVsNotD.jListColor(vax, 1, false));
      }
      rs.close();
      con.close();
    } catch (SQLException ex) {
      Logger.getLogger(Consulta_PedVsNotD.class.getName()).log(Level.SEVERE, null, ex);
    }
    txtcli.requestFocus();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel3 = new javax.swing.JLabel();
    jTiT = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList = new javax.swing.JList();
    jLabel1 = new javax.swing.JLabel();
    txtFed = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    txtFeh = new javax.swing.JTextField();
    Cliente1 = new javax.swing.JLabel();
    txtcli = new javax.swing.JTextField();
    btnSal = new javax.swing.JButton();
    labCan = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/grafic.png"))); // NOI18N
    jLabel3.setText("DETALLE PEDIDOS  Vs NOTAS ENTREGA");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTiT.setBackground(new java.awt.Color(0, 102, 102));
    jTiT.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jTiT.setForeground(new java.awt.Color(255, 255, 255));
    jTiT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
    jTiT.setOpaque(true);

    jList.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    jList.setOpaque(false);
    jList.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jListMouseClicked(evt);
      }
    });
    jScrollPane1.setViewportView(jList);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel1.setForeground(new java.awt.Color(0, 0, 102));
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Del");

    txtFed.setBackground(new java.awt.Color(252, 247, 228));
    txtFed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFed.setForeground(new java.awt.Color(51, 0, 153));
    txtFed.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFed.setText(" ");
    txtFed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtFed.setFocusable(false);
    txtFed.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFedMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFedMouseReleased(evt);
      }
    });
    txtFed.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFedActionPerformed(evt);
      }
    });
    txtFed.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFedKeyReleased(evt);
      }
    });

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jLabel2.setForeground(new java.awt.Color(0, 51, 102));
    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("al");

    txtFeh.setBackground(new java.awt.Color(252, 247, 228));
    txtFeh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtFeh.setForeground(new java.awt.Color(51, 0, 153));
    txtFeh.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtFeh.setText(" ");
    txtFeh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
    txtFeh.setFocusable(false);
    txtFeh.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtFehMouseClicked(evt);
      }
      public void mouseReleased(java.awt.event.MouseEvent evt) {
        txtFehMouseReleased(evt);
      }
    });
    txtFeh.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtFehActionPerformed(evt);
      }
    });
    txtFeh.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtFehKeyReleased(evt);
      }
    });

    Cliente1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 12)); // NOI18N
    Cliente1.setForeground(new java.awt.Color(51, 51, 102));
    Cliente1.setText(" Cliente");

    txtcli.setEditable(false);
    txtcli.setBackground(new java.awt.Color(204, 204, 204));
    txtcli.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    txtcli.setToolTipText("Buscar Clientes");
    txtcli.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    txtcli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    txtcli.setOpaque(false);
    txtcli.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtcliMouseClicked(evt);
      }
    });
    txtcli.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtcliActionPerformed(evt);
      }
    });
    txtcli.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(java.awt.event.KeyEvent evt) {
        txtcliKeyReleased(evt);
      }
    });

    btnSal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    btnSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salir2.png"))); // NOI18N
    btnSal.setText("Salir");
    btnSal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnSalActionPerformed(evt);
      }
    });

    labCan.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11)); // NOI18N
    labCan.setForeground(new java.awt.Color(51, 51, 102));
    labCan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    labCan.setText(" ");
    labCan.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addComponent(btnSal)
            .addGap(36, 36, 36)
            .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jTiT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 932, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txtcli))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(42, 42, 42)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addContainerGap(23, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(txtFed, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jLabel3)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2)
            .addComponent(txtFeh, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(15, 15, 15)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(txtcli, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(Cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTiT, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(labCan, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(btnSal))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void txtFedMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseClicked
    //jDesde.setSelectionStart(0);
    //jDesde.setSelectionEnd(5);
  }//GEN-LAST:event_txtFedMouseClicked

  private void txtFedMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFedMouseReleased

  }//GEN-LAST:event_txtFedMouseReleased

  private void txtFedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFedActionPerformed

  }//GEN-LAST:event_txtFedActionPerformed

  private void txtFedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFedKeyReleased

  }//GEN-LAST:event_txtFedKeyReleased

  private void txtFehMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseClicked
    //jHasta.setSelectionStart(0);
    //jHasta.setSelectionEnd(5);
  }//GEN-LAST:event_txtFehMouseClicked

  private void txtFehMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFehMouseReleased

  }//GEN-LAST:event_txtFehMouseReleased

  private void txtFehActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFehActionPerformed

  }//GEN-LAST:event_txtFehActionPerformed

  private void txtFehKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFehKeyReleased

  }//GEN-LAST:event_txtFehKeyReleased

  private void txtcliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtcliMouseClicked
  }//GEN-LAST:event_txtcliMouseClicked

  private void txtcliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcliActionPerformed
  }//GEN-LAST:event_txtcliActionPerformed

  private void txtcliKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcliKeyReleased
  }//GEN-LAST:event_txtcliKeyReleased

  private void btnSalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalActionPerformed
    DetPed.dispose();
  }//GEN-LAST:event_btnSalActionPerformed

  private void jListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListMouseClicked
    // TODO add your handling code here:
  }//GEN-LAST:event_jListMouseClicked

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
      java.util.logging.Logger.getLogger(Consulta_PedVsNotD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Consulta_PedVsNotD.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Consulta_PedVsNotD("", "", "", "").setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel Cliente1;
  private javax.swing.JButton btnSal;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JList jList;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel jTiT;
  private javax.swing.JLabel labCan;
  private javax.swing.JTextField txtFed;
  private javax.swing.JTextField txtFeh;
  private javax.swing.JTextField txtcli;
  // End of variables declaration//GEN-END:variables
}
