//
// Calculadora Estandar  Jose Luis Fernandez
//
package comun;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultEditorKit;

public class Calculadora extends javax.swing.JFrame {

  String valor = "", signo = "", signoA = "", cadena = "", historia = "";
  int indMR = 0;
  double tot = 0, tof = 0, val = 0, num = 0, ind = 0, MC = 0, MR = 0, M1 = 0.0, M2 = 0.0;
  DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
  DecimalFormat df = new DecimalFormat("###,##0.00", simbolo);
  private DefaultListModel modelo;

  public Calculadora() {

    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    initComponents();
    modelo = new DefaultListModel();
    modelo.clear();
    jHist.setFont(new java.awt.Font("cambria Bold", 1, 12));
    //jHist.setLayoutOrientation(JList.VERTICAL_WRAP);
    jHist.setSelectionBackground(Color.LIGHT_GRAY);
    jHist.setSelectionForeground(Color.BLUE);
    jHist.setVisibleRowCount(0);
    jHist.getValueIsAdjusting();
    DefaultListCellRenderer renderer = (DefaultListCellRenderer) jHist.getCellRenderer();
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    jHist.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
          jbtTot.doClick();
        }
      }
    });
    setTitle("Calculadora");
    setResizable(false);
    setLocationRelativeTo(null);
    //setLocation(20, 20);
    setAlwaysOnTop(true);
    setBackground((Color) UIManager.getDefaults().get("TextPane.background"));
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/calc.jpg")));
    // Pulsa x cerrar ventana
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent we) {
        dispose();
      }
    });
    // tecla escape - Salir   VK_ESCAPE
    getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(
        javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0), "Cancel");
    getRootPane().getActionMap().put("Cancel", new javax.swing.AbstractAction() {
      @Override
      public void actionPerformed(java.awt.event.ActionEvent e) {
        dispose();
      }
    });
  }

// Calculadora
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jLabel1 = new javax.swing.JLabel();
    jButton20 = new javax.swing.JButton();
    jbt1 = new javax.swing.JButton();
    jbt4 = new javax.swing.JButton();
    jbt7 = new javax.swing.JButton();
    jbt0 = new javax.swing.JButton();
    jbt8 = new javax.swing.JButton();
    jbt5 = new javax.swing.JButton();
    jbt2 = new javax.swing.JButton();
    jbtsig = new javax.swing.JButton();
    jbt9 = new javax.swing.JButton();
    jbt6 = new javax.swing.JButton();
    jbt3 = new javax.swing.JButton();
    jbtPto = new javax.swing.JButton();
    jbtMul = new javax.swing.JButton();
    jbtSum = new javax.swing.JButton();
    jbtRes = new javax.swing.JButton();
    jbtDiv = new javax.swing.JButton();
    jbtmit = new javax.swing.JButton();
    jbtCE = new javax.swing.JButton();
    jbtBack = new javax.swing.JButton();
    jbtTot = new javax.swing.JButton();
    jbtPor = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jMC = new javax.swing.JLabel();
    jMR = new javax.swing.JLabel();
    jM1 = new javax.swing.JLabel();
    jM2 = new javax.swing.JLabel();
    jbtCpy = new JButton( new DefaultEditorKit.CopyAction() )
    ;
    txtMonto = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    jHist = new javax.swing.JList();

    jLabel1.setText("jLabel1");

    jButton20.setBackground(new java.awt.Color(0, 153, 153));
    jButton20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jButton20.setForeground(new java.awt.Color(255, 255, 255));
    jButton20.setText("C");
    jButton20.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton20ActionPerformed(evt);
      }
    });

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    setType(java.awt.Window.Type.POPUP);

    jbt1.setBackground(new java.awt.Color(0, 153, 140));
    jbt1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt1.setForeground(new java.awt.Color(255, 255, 255));
    jbt1.setText("1");
    jbt1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt1.setBorderPainted(false);
    jbt1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jbt1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt1ActionPerformed(evt);
      }
    });

    jbt4.setBackground(new java.awt.Color(0, 153, 140));
    jbt4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt4.setForeground(new java.awt.Color(255, 255, 255));
    jbt4.setText("4");
    jbt4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt4.setBorderPainted(false);
    jbt4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt4ActionPerformed(evt);
      }
    });

    jbt7.setBackground(new java.awt.Color(0, 153, 140));
    jbt7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt7.setForeground(new java.awt.Color(255, 255, 255));
    jbt7.setText("7");
    jbt7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt7.setBorderPainted(false);
    jbt7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt7.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt7ActionPerformed(evt);
      }
    });

    jbt0.setBackground(new java.awt.Color(0, 153, 140));
    jbt0.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt0.setForeground(new java.awt.Color(255, 255, 255));
    jbt0.setText("0");
    jbt0.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt0.setBorderPainted(false);
    jbt0.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt0.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt0ActionPerformed(evt);
      }
    });

    jbt8.setBackground(new java.awt.Color(0, 153, 140));
    jbt8.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt8.setForeground(new java.awt.Color(255, 255, 255));
    jbt8.setText("8");
    jbt8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt8.setBorderPainted(false);
    jbt8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt8ActionPerformed(evt);
      }
    });

    jbt5.setBackground(new java.awt.Color(0, 153, 140));
    jbt5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt5.setForeground(new java.awt.Color(255, 255, 255));
    jbt5.setText("5");
    jbt5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt5.setBorderPainted(false);
    jbt5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt5ActionPerformed(evt);
      }
    });

    jbt2.setBackground(new java.awt.Color(0, 153, 140));
    jbt2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt2.setForeground(new java.awt.Color(255, 255, 255));
    jbt2.setText("2");
    jbt2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt2.setBorderPainted(false);
    jbt2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt2ActionPerformed(evt);
      }
    });

    jbtsig.setBackground(new java.awt.Color(0, 153, 140));
    jbtsig.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N
    jbtsig.setForeground(new java.awt.Color(255, 255, 255));
    jbtsig.setText("+-");
    jbtsig.setToolTipText("Cambiar Signo ( + / - )");
    jbtsig.setActionCommand("");
    jbtsig.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbtsig.setBorderPainted(false);
    jbtsig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtsig.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jbtsig.setPreferredSize(new java.awt.Dimension(11, 29));
    jbtsig.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtsigActionPerformed(evt);
      }
    });

    jbt9.setBackground(new java.awt.Color(0, 153, 140));
    jbt9.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt9.setForeground(new java.awt.Color(255, 255, 255));
    jbt9.setText("9");
    jbt9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt9.setBorderPainted(false);
    jbt9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt9.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt9ActionPerformed(evt);
      }
    });

    jbt6.setBackground(new java.awt.Color(0, 153, 140));
    jbt6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt6.setForeground(new java.awt.Color(255, 255, 255));
    jbt6.setText("6");
    jbt6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt6.setBorderPainted(false);
    jbt6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt6ActionPerformed(evt);
      }
    });

    jbt3.setBackground(new java.awt.Color(0, 153, 140));
    jbt3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbt3.setForeground(new java.awt.Color(255, 255, 255));
    jbt3.setText("3");
    jbt3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbt3.setBorderPainted(false);
    jbt3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbt3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbt3ActionPerformed(evt);
      }
    });

    jbtPto.setBackground(new java.awt.Color(0, 153, 140));
    jbtPto.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jbtPto.setForeground(new java.awt.Color(255, 255, 255));
    jbtPto.setText(".");
    jbtPto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
    jbtPto.setBorderPainted(false);
    jbtPto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtPto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jbtPto.setPreferredSize(new java.awt.Dimension(17, 25));
    jbtPto.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtPtoActionPerformed(evt);
      }
    });

    jbtMul.setBackground(new java.awt.Color(0, 102, 102));
    jbtMul.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbtMul.setForeground(new java.awt.Color(255, 255, 255));
    jbtMul.setText("x");
    jbtMul.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtMul.setBorderPainted(false);
    jbtMul.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtMul.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtMulActionPerformed(evt);
      }
    });

    jbtSum.setBackground(new java.awt.Color(0, 102, 102));
    jbtSum.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
    jbtSum.setForeground(new java.awt.Color(255, 255, 255));
    jbtSum.setText("+");
    jbtSum.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtSum.setBorderPainted(false);
    jbtSum.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtSum.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtSumActionPerformed(evt);
      }
    });

    jbtRes.setBackground(new java.awt.Color(0, 102, 102));
    jbtRes.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 38)); // NOI18N
    jbtRes.setForeground(new java.awt.Color(255, 255, 255));
    jbtRes.setText("-");
    jbtRes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtRes.setBorderPainted(false);
    jbtRes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtRes.setPreferredSize(new java.awt.Dimension(45, 29));
    jbtRes.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtResActionPerformed(evt);
      }
    });

    jbtDiv.setBackground(new java.awt.Color(0, 102, 102));
    jbtDiv.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbtDiv.setForeground(new java.awt.Color(255, 255, 255));
    jbtDiv.setText("÷");
    jbtDiv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtDiv.setBorderPainted(false);
    jbtDiv.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtDiv.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtDivActionPerformed(evt);
      }
    });

    jbtmit.setBackground(new java.awt.Color(0, 153, 153));
    jbtmit.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
    jbtmit.setForeground(new java.awt.Color(255, 255, 255));
    jbtmit.setText("1/x");
    jbtmit.setToolTipText("Fraccion Valor");
    jbtmit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtmit.setBorderPainted(false);
    jbtmit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtmit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtmitActionPerformed(evt);
      }
    });

    jbtCE.setBackground(new java.awt.Color(0, 153, 153));
    jbtCE.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
    jbtCE.setForeground(new java.awt.Color(255, 255, 255));
    jbtCE.setText("CE");
    jbtCE.setToolTipText("Borrar entrada");
    jbtCE.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtCE.setBorderPainted(false);
    jbtCE.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtCE.setPreferredSize(new java.awt.Dimension(51, 23));
    jbtCE.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtCEActionPerformed(evt);
      }
    });

    jbtBack.setBackground(new java.awt.Color(0, 102, 102));
    jbtBack.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jbtBack.setForeground(new java.awt.Color(255, 255, 255));
    jbtBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
    jbtBack.setToolTipText("Borra caracter ");
    jbtBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtBackActionPerformed(evt);
      }
    });

    jbtTot.setBackground(new java.awt.Color(0, 153, 153));
    jbtTot.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 30)); // NOI18N
    jbtTot.setForeground(new java.awt.Color(255, 255, 255));
    jbtTot.setText("=");
    jbtTot.setToolTipText("Totalizar");
    jbtTot.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtTot.setBorderPainted(false);
    jbtTot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtTot.setPreferredSize(new java.awt.Dimension(51, 23));
    jbtTot.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtTotActionPerformed(evt);
      }
    });

    jbtPor.setBackground(new java.awt.Color(0, 153, 153));
    jbtPor.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
    jbtPor.setForeground(new java.awt.Color(255, 255, 255));
    jbtPor.setText("%");
    jbtPor.setToolTipText("Valor Porcentaje");
    jbtPor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    jbtPor.setBorderPainted(false);
    jbtPor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtPor.setPreferredSize(new java.awt.Dimension(51, 23));
    jbtPor.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtPorActionPerformed(evt);
      }
    });

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
    jLabel3.setForeground(new java.awt.Color(51, 0, 204));
    jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel3.setText("JLF");
    jLabel3.setToolTipText("joseluisfernandezg@gmail.com");
    jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jMC.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jMC.setForeground(new java.awt.Color(51, 51, 51));
    jMC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMC.setText("MC");
    jMC.setToolTipText("Borra la Memoria");
    jMC.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jMC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jMC.setPreferredSize(new java.awt.Dimension(24, 17));
    jMC.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jMCMouseClicked(evt);
      }
    });

    jMR.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jMR.setForeground(new java.awt.Color(51, 51, 51));
    jMR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jMR.setText("MR");
    jMR.setToolTipText("Recupera la Memoria");
    jMR.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jMR.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jMR.setPreferredSize(new java.awt.Dimension(24, 17));
    jMR.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jMRMouseClicked(evt);
      }
    });

    jM1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jM1.setForeground(new java.awt.Color(51, 51, 51));
    jM1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jM1.setText("M +");
    jM1.setToolTipText("Suma la Memoria");
    jM1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jM1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jM1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jM1MouseClicked(evt);
      }
    });

    jM2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
    jM2.setForeground(new java.awt.Color(51, 51, 51));
    jM2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jM2.setText("M -");
    jM2.setToolTipText("Resta la Memoria");
    jM2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
    jM2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jM2.setPreferredSize(new java.awt.Dimension(24, 17));
    jM2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jM2MouseClicked(evt);
      }
    });

    jbtCpy.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
    jbtCpy.setForeground(new java.awt.Color(153, 0, 0));
    jbtCpy.setText("Copy");
    jbtCpy.setToolTipText("Copiar  ( Ctrl-V Pegar)");
    jbtCpy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    jbtCpy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jbtCpy.setOpaque(false);
    jbtCpy.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtCpyActionPerformed(evt);
      }
    });

    txtMonto.setBackground(new java.awt.Color(204, 204, 204));
    txtMonto.setFont(new java.awt.Font("SansSerif", 1, 42)); // NOI18N
    txtMonto.setForeground(new java.awt.Color(0, 0, 102));
    txtMonto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtMonto.setAutoscrolls(false);
    txtMonto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    txtMonto.setDisabledTextColor(new java.awt.Color(255, 255, 255));
    txtMonto.setDoubleBuffered(true);
    txtMonto.setOpaque(false);
    txtMonto.setRequestFocusEnabled(false);
    txtMonto.setVerifyInputWhenFocusTarget(false);
    txtMonto.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        txtMontoMouseClicked(evt);
      }
    });
    txtMonto.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        txtMontoActionPerformed(evt);
      }
    });
    txtMonto.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        txtMontoKeyPressed(evt);
      }
      public void keyTyped(java.awt.event.KeyEvent evt) {
        txtMontoKeyTyped(evt);
      }
    });

    jHist.setBackground(new java.awt.Color(229, 226, 226));
    jHist.setFont(new java.awt.Font("Cambria", 2, 14)); // NOI18N
    jHist.setForeground(new java.awt.Color(51, 51, 51));
    jHist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jHist.setToolTipText("Registro del Calculo");
    jHist.setAutoscrolls(false);
    jHist.setFocusable(false);
    jHist.setLayoutOrientation(javax.swing.JList.VERTICAL_WRAP);
    jHist.setRequestFocusEnabled(false);
    jHist.setSelectionBackground(new java.awt.Color(204, 204, 204));
    jHist.setVisibleRowCount(1);
    jScrollPane1.setViewportView(jHist);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(txtMonto)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addComponent(jbtCpy)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3))
          .addComponent(jScrollPane1)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jMC, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jbt0, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addComponent(jbt7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbt4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbt1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jbt5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt8, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
              .addComponent(jMR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbtPto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jbtsig, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jM1, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jbtMul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jbtDiv, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jbtmit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jbtTot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
              .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(jbtSum, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                  .addComponent(jM2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jbtRes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                  .addComponent(jbtPor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jbtCE, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                  .addComponent(jbtBack, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))))
        .addContainerGap(19, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGap(6, 6, 6)
        .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jbtCpy, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jMR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jMC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jM1)
            .addComponent(jM2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jbtBack, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jbtSum, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
              .addComponent(jbtCE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jbtPor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbtRes, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)))
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jbt2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jbt1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jbt3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jbt5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jbt6, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jbt7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbt8, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
              .addComponent(jbtMul, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jbtPto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbt0, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jbtDiv, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jbt9, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jbtmit, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jbtsig, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(jbtTot, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(15, Short.MAX_VALUE))
    );

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

    private void jbt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt1ActionPerformed
      replace_mto("1");
    }//GEN-LAST:event_jbt1ActionPerformed

    private void jbt2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt2ActionPerformed
      replace_mto("2");
    }//GEN-LAST:event_jbt2ActionPerformed

    private void jbt3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt3ActionPerformed
      replace_mto("3");
    }//GEN-LAST:event_jbt3ActionPerformed

    private void jbt4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt4ActionPerformed
      replace_mto("4");
    }//GEN-LAST:event_jbt4ActionPerformed

    private void jbt5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt5ActionPerformed
      replace_mto("5");
    }//GEN-LAST:event_jbt5ActionPerformed

    private void jbt6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt6ActionPerformed
      replace_mto("6");
    }//GEN-LAST:event_jbt6ActionPerformed

    private void jbt7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt7ActionPerformed
      replace_mto("7");
    }//GEN-LAST:event_jbt7ActionPerformed

    private void jbt8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt8ActionPerformed
      replace_mto("8");
    }//GEN-LAST:event_jbt8ActionPerformed

    private void jbt9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt9ActionPerformed
      replace_mto("9");
    }//GEN-LAST:event_jbt9ActionPerformed

    private void jbt0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbt0ActionPerformed
      replace_mto("0");
    }//GEN-LAST:event_jbt0ActionPerformed

    private void jbtCEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCEActionPerformed
      txtMonto.setText("");
      tot = 0;
      tof = 0;
      signo = "";
      signoA = "";
      txtMonto.requestFocus();
      modelo.clear();
      historia = "";
    }//GEN-LAST:event_jbtCEActionPerformed

  // Boton 1/x
    private void jbtmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtmitActionPerformed
      simbolo.setDecimalSeparator(',');
      simbolo.setGroupingSeparator('.');
      df = new DecimalFormat("###,##0.00", simbolo);
      num = 0.00;
      cadena = txtMonto.getText();
      cadena = cadena.replace("/", "").replace("*", "").replace("+", "").replace(" ", "").replace("%", "").replace("÷", "");
      if (cadena.length() > 0) {
        num = GetMtoDouble(cadena);
        if (tof != 0) {
          num = tof;
        }
        if (num < 0.01) {
          //df = new DecimalFormat("#0.000", simbolo);
        }
        num = 1 / num;
        txtMonto.setText(df.format(num).replace(",000000", "").replace(",00", ""));
        //df = new DecimalFormat("#,##0.00", simbolo);
        tof = num;
      }
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtmitActionPerformed

  // Cambio Signo + o -
    private void jbtsigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtsigActionPerformed
      num = 0.00;
      cadena = txtMonto.getText();
      cadena = cadena.replace("/", "").replace("*", "").replace("+", "").replace(" ", "").replace("%", "").replace("÷", "");
      if (cadena.length() > 0) {
        int c = 0, p = 0, d = 0, lp = 0;
        for (int i = 0; i < cadena.length(); i++) {
          if (cadena.substring(i, i + 1).equals(",")) {
            c = c + 1;
          }
          if (cadena.substring(i, i + 1).equals(".")) {
            p = p + 1;
            lp = i;
          }
        }
        if (p == 1) {
          d = cadena.substring(lp + 1, cadena.length()).length();
        }
        if (c > 0 || p > 1 || d == 3) {
          cadena = cadena.replace(".", "");
          cadena = cadena.replace(",", ".");
        }
        num = GetMtoDouble(cadena);
        num = num * -1;
        txtMonto.setText(PrintMtoEsp(num));
      }
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtsigActionPerformed

    private void jbtResActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtResActionPerformed
      valor = txtMonto.getText();
      signo = "-";
      Acumular(signo, valor);
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtResActionPerformed

    private void jbtSumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtSumActionPerformed
      valor = txtMonto.getText();
      signo = "+";
      Acumular(signo, valor);
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtSumActionPerformed

    private void jbtMulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtMulActionPerformed
      valor = txtMonto.getText();
      signo = "x";
      Acumular(signo, valor);
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtMulActionPerformed

    private void jbtDivActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtDivActionPerformed
      valor = txtMonto.getText();
      signo = "÷";
      Acumular(signo, valor);
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtDivActionPerformed

  //Boton Total
    private void jbtTotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtTotActionPerformed
      valor = txtMonto.getText();
      if (historia.length() > 0) {
        Acumular(signo, valor);
        modelo.clear();
        historia = historia.trim();
        historia = historia.substring(0, historia.length() - 1);
        historia = historia + " = " + PrintMtoEsp(tof);
        modelo.addElement(historia);
        jHist.setModel(modelo);
        historia = "";
        //jHist.setSelectedIndex(0);
      }
      valor = PrintMtoEsp(tof);
      txtMonto.setText(valor);
      tot = tof;
      signo = "";
      signoA = "";
      valor = "";
      txtMonto.requestFocus();
    }//GEN-LAST:event_jbtTotActionPerformed

  private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_jButton20ActionPerformed

  // Boton Porcentaje %
  private void jbtPorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPorActionPerformed
    num = 0;
    cadena = txtMonto.getText();
    cadena = cadena.replace(",", "");
    cadena = cadena.replace("/", "").replace("*", "").replace("+", "").replace(" ", "").replace("%", "");
    if (cadena.length() > 0) {
      if (!isNumeric(cadena)) {
        cadena = "0";
      }
      num = GetMtoDouble(cadena);
      num = num / 100;
      val = tot;
      if (val == 0) {
        val = 1;
      }
      txtMonto.setText(PrintMtoEsp(num * val));
      tof = 0;
      tot = 0;
      signoA = "+";
      signo = "%";
      jbtTot.doClick();
    }
    txtMonto.requestFocus();
  }//GEN-LAST:event_jbtPorActionPerformed

  private void jbtPtoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtPtoActionPerformed
    if (ind == 0) {
      cadena = txtMonto.getText();
      if (cadena.length() <= 0) {
        txtMonto.setText("0.");
      } else {
        if (!ExistePunto(txtMonto.getText())) {
          txtMonto.setText(txtMonto.getText() + ".");
        }
      }
      txtMonto.requestFocus();
    }
    ind = 0;
  }//GEN-LAST:event_jbtPtoActionPerformed

  private void jMCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMCMouseClicked
    MC = 0;
    if (jMR.isEnabled()) {
      M1 = 0;
      M2 = 0;
      indMR = 0;
      jMC.setEnabled(false);
      jMR.setEnabled(false);
      jMC.setForeground(new java.awt.Color(255, 51, 51));
      jM1.setForeground(new java.awt.Color(51, 51, 51));
      jM2.setForeground(new java.awt.Color(51, 51, 51));
      jMR.setForeground(new java.awt.Color(51, 51, 51));
    }
  }//GEN-LAST:event_jMCMouseClicked

  private void jMRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMRMouseClicked
    indMR = 0;
    jMR.setForeground(new java.awt.Color(255, 51, 51));
    jM1.setForeground(new java.awt.Color(51, 51, 51));
    jM2.setForeground(new java.awt.Color(51, 51, 51));
    jMC.setForeground(new java.awt.Color(51, 51, 51));
    System.out.println("M1=" + M1 + ",M2=" + M2);
    txtMonto.setText(PrintMtoEsp(M1 - M2));
  }//GEN-LAST:event_jMRMouseClicked

  private void jM1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jM1MouseClicked
    if (!jMR.isEnabled() && txtMonto.getText().length() > 0) {
      jMC.setEnabled(true);
      jMR.setEnabled(true);
    }
    indMR = 1;
    jM1.setForeground(new java.awt.Color(255, 51, 51));
    jM2.setForeground(new java.awt.Color(51, 51, 51));
    jMR.setForeground(new java.awt.Color(51, 51, 51));
    jMC.setForeground(new java.awt.Color(51, 51, 51));
    valor = txtMonto.getText();
    AjuValor();
    M1 = M1 + GetMtoDouble(valor);
    System.out.println("M1=" + M1);
  }//GEN-LAST:event_jM1MouseClicked

  private void jM2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jM2MouseClicked
    if (!jMC.isEnabled() && txtMonto.getText().length() > 0) {
      jMC.setEnabled(true);
      jMR.setEnabled(true);
    }
    indMR = 1;
    jM2.setForeground(new java.awt.Color(255, 51, 51));
    jM1.setForeground(new java.awt.Color(51, 51, 51));
    jMR.setForeground(new java.awt.Color(51, 51, 51));
    jMC.setForeground(new java.awt.Color(51, 51, 51));
    valor = txtMonto.getText();
    AjuValor();
    M2 = M2 + GetMtoDouble(valor);
    System.out.println("M2=" + M2);
  }//GEN-LAST:event_jM2MouseClicked

  private void jbtBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtBackActionPerformed
    cadena = txtMonto.getText();
    if (cadena.length() > 0) {
      cadena = cadena.substring(0, cadena.length() - 1);
    }
    txtMonto.setText(cadena);
    txtMonto.requestFocus();
  }//GEN-LAST:event_jbtBackActionPerformed

  private void jbtCpyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCpyActionPerformed
    valor = txtMonto.getText();
    int c = 0, p = 0, lp = 0, d = 0;
    for (int i = 0; i < valor.length(); i++) {
      if (valor.substring(i, i + 1).equals(",")) {
        c = c + 1;
      }
      if (valor.substring(i, i + 1).equals(".")) {
        p = p + 1;
        lp = i;
      }
    }

    if (p == 1) {
      d = valor.substring(lp + 1, valor.length()).length();
    }

    if (c > 0 || p > 1 || d == 3) {
      valor = valor.replace(".", "");
      valor = valor.replace(",", ".");
    }
    txtMonto.setText(valor);
    txtMonto.setSelectionStart(0);
    txtMonto.setSelectionEnd(txtMonto.getText().length());
    txtMonto.requestFocus();
  }//GEN-LAST:event_jbtCpyActionPerformed

  private void txtMontoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtMontoMouseClicked
    if (evt.getClickCount() == 2) {
      txtMonto.setText(txtMonto.getText().replace(",", ""));
      txtMonto.setSelectionStart(0);
      txtMonto.setSelectionEnd(txtMonto.getText().length());
      jbtCpy.doClick();
    }
  }//GEN-LAST:event_txtMontoMouseClicked

  private void txtMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoActionPerformed

  }//GEN-LAST:event_txtMontoActionPerformed

  private void txtMontoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoKeyPressed
    try {
      char car = evt.getKeyChar();
      if (car == '+') {
        jbtSum.doClick();
      }
      if (car == '-') {
        jbtRes.doClick();
      }
      if (car == '*') {
        jbtMul.doClick();
      }
      if (car == '/' || car == '÷') {
        jbtDiv.doClick();
      }
      if (car == '%') {
        jbtPor.doClick();
      }
      switch (evt.getKeyCode()) {
        case KeyEvent.VK_ENTER:
          jbtTot.doClick();
          break;
      }
    } catch (Exception e) {
      jbtBack.doClick();
    }
  }//GEN-LAST:event_txtMontoKeyPressed

  private void txtMontoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMontoKeyTyped
    valor = txtMonto.getText();
    if (valor.length() == 1 && valor.equals("0")) {
      txtMonto.setSelectionStart(0);
      txtMonto.setSelectionEnd(txtMonto.getText().length());
      txtMonto.requestFocus();
    }
    char car = evt.getKeyChar();
    if (((car < '0') || (car > '9')) && (car != KeyEvent.VK_BACK_SPACE)
        && (car != '.')) {
      if ((car != KeyEvent.VK_BACK_SPACE) && (car != KeyEvent.VK_ENTER) && (car != '+')
          && (car != '-') && (car != '*') && (car != '/')) {
        getToolkit().beep();
      }
      evt.consume();
    } else {
      pulsa_dig(car);
    }
  }//GEN-LAST:event_txtMontoKeyTyped

  //Replace txtMonto
  public void replace_mto(String dig) {
    if (indMR == 1) {
      txtMonto.setText("");
      indMR = 0;
    }
    txtMonto.setText(txtMonto.getText().replace("+", "").replace("-", "").replace("x", "").replace("/", "").replace("÷", ""));
    if (ind == 0) {
      txtMonto.setText(txtMonto.getText() + dig);
      txtMonto.requestFocus();
    }
    ind = 0;
  }

  //Ejecuta Boton Digitos numericos
  public void pulsa_dig(char car) {
    ind = 0;
    if (car == '.') {
      ind = 1;
      jbtPto.doClick();
    }
    if (car == '0') {
      ind = 1;
      jbt0.doClick();
    }
    if (car == '1') {
      ind = 1;
      jbt1.doClick();
    }
    if (car == '2') {
      ind = 1;
      jbt2.doClick();
    }
    if (car == '3') {
      ind = 1;
      jbt3.doClick();
    }
    if (car == '4') {
      ind = 4;
      jbt4.doClick();
    }
    if (car == '5') {
      ind = 5;
      jbt5.doClick();
    }
    if (car == '6') {
      ind = 6;
      jbt6.doClick();
    }
    if (car == '7') {
      ind = 7;
      jbt7.doClick();
    }
    if (car == '8') {
      ind = 8;
      jbt8.doClick();
    }
    if (car == '9') {
      ind = 9;
      jbt9.doClick();
    }
  }

//Acumular valores          
  public void Acumular(String signo, String val) {
    valor = val;
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    df = new DecimalFormat("###,##0.00", simbolo);
    valor = valor.replace("/", "").replace("*", "").replace("+", "").replace(" ", "").replace("%", "").replace("÷", "");
    AjuValor();
    tot = 0.0;
    tot = GetMtoDouble(valor);
    if (signoA.length() > 0) {
      if (signoA.equals("+")) {
        tof = tof + tot;
      }
      if (signoA.equals("-")) {
        if (tot < 0) {
          tof = tof + tot;
        } else {
          tof = tof - tot;
        }
      }
      if (signoA.equals("x")) {
        tof = tof * tot;
      }
      if (signoA.equals("÷")) {
        if (tot != 0) {
          tof = tof / tot;
        }
      }
    } else {
      tof = tot;
    }
    String vax = "";
    if (signo.equals("%")) {
      vax = "%";
      historia = historia + " " + df.format(num * 100).replace(",00", "") + vax + " " + signo;
    } else {
      historia = historia + " " + df.format(tot).replace(",00", "") + vax + " " + signo;
    }
    historia = historia.replace("+ 0 ", "").replace("- 0 ", "").replace("x 0 ", "").replace("/ 0 ", "").replace("÷ 0 ", "");
    modelo.clear();
    historia = historia.trim();
    modelo.addElement(historia);
    jHist.setModel(modelo);
    signoA = signo;
    signo = "";
    txtMonto.setText("");
    txtMonto.setText(signoA);
  }

  // Ajusta valor double 
  public void AjuValor() {
    int c = 0, p = 0, lp = 0, d = 0;
    for (int i = 0; i < valor.length(); i++) {
      if (valor.substring(i, i + 1).equals(",")) {
        c = c + 1;
      }
      if (valor.substring(i, i + 1).equals(".")) {
        p = p + 1;
        lp = i;
      }
    }
    if (p == 1) {
      d = valor.substring(lp + 1, valor.length()).length();
    }
    if (c > 0 || p > 1 || d == 3) {
      valor = valor.replace(".", "");
      valor = valor.replace(",", ".");
    }
  }

  // Verifica si tiene "." en cadena
  public static boolean ExistePunto(String cadena) {
    boolean resultado;
    resultado = false;
    for (int i = 0; i < cadena.length(); i++) {
      if (cadena.substring(i, i + 1).equals(".")) {
        resultado = true;
        break;
      }
    }
    return resultado;
  }

  //Devuelve valor numerico
  private final Double GetMtoDouble(String Imp) {
    Imp = Imp.trim();
    if (Imp == null || Imp.equals("")) {
      Imp = "0.00";
    } else {
      Imp = Imp.replace("'", "");
      String mto = Imp.replace(",", "");
      if (mto.length() == 1) {
        mto = mto + ".00";
      }
      if (Imp.equals("-")) {
        Imp = "0,00";
      }
      if (!isNumeric(mto.trim())) {
        Imp = "0,00";
      }
    }
    return Double.parseDouble(GetFmtoMto(Imp));
  }

  // Valida que sea numerico
  private static boolean isNumeric(String cadena) {  // Valida si es nmerico
    try {
      Double.parseDouble(cadena);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }

  //Convierte formato Ingles a double 
  private final String GetFmtoMto(String sVal) {
    sVal = sVal.replace(",", "");
    if (sVal.equals("")) {
      sVal = "0,00";
    }
    return sVal;
  }

  //Format monto (es = Español  ###.###,##
  private final String PrintMtoEsp(double Imp) {
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    df = new DecimalFormat("###,##0.00", simbolo);
    String MtoF = "0,00";
    if (Imp != 0.0) {
      MtoF = df.format(Imp).trim();
      if (MtoF.indexOf(',') == -1) {
        MtoF = MtoF + ",00";
      } else {
        String N = MtoF.substring(MtoF.indexOf(',') + 1, MtoF.length());
        if (N.length() == 1) {
          MtoF = MtoF + "0";
        }
      }
    }
    MtoF = MtoF.replace("-0,00", "0,00").replace("0,00.00", "0,00").replace(",00", "");
    //System.out.println("MtoF="+MtoF);
    return MtoF;
  }

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
      java.util.logging.Logger.getLogger(Calculadora.class
          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Calculadora.class
          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Calculadora.class
          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Calculadora.class
          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Calculadora.class
          .getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Calculadora().setVisible(true);
      }
    });
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton20;
  private javax.swing.JList jHist;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jM1;
  private javax.swing.JLabel jM2;
  private javax.swing.JLabel jMC;
  private javax.swing.JLabel jMR;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JButton jbt0;
  private javax.swing.JButton jbt1;
  private javax.swing.JButton jbt2;
  private javax.swing.JButton jbt3;
  private javax.swing.JButton jbt4;
  private javax.swing.JButton jbt5;
  private javax.swing.JButton jbt6;
  private javax.swing.JButton jbt7;
  private javax.swing.JButton jbt8;
  private javax.swing.JButton jbt9;
  private javax.swing.JButton jbtBack;
  private javax.swing.JButton jbtCE;
  private javax.swing.JButton jbtCpy;
  private javax.swing.JButton jbtDiv;
  private javax.swing.JButton jbtMul;
  private javax.swing.JButton jbtPor;
  private javax.swing.JButton jbtPto;
  private javax.swing.JButton jbtRes;
  private javax.swing.JButton jbtSum;
  private javax.swing.JButton jbtTot;
  private javax.swing.JButton jbtmit;
  private javax.swing.JButton jbtsig;
  private javax.swing.JTextField txtMonto;
  // End of variables declaration//GEN-END:variables
}
