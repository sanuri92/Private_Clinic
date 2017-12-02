/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package private_clinic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sanuri Isara
 */
public class PrescriptionEntry extends javax.swing.JFrame {

    /**
     * Creates new form PrescriptionEntry
     */
    public PrescriptionEntry() {
        initComponents();
        showTime();
        //genratePrescriptionId();
        LoadDrugsToThe();
        btnAddPatient.setVisible(false);
       this.setLocationRelativeTo(null);
        lblPatientName.setText("");
        LoadDocterToThecmb();
        lbldocternic.setText("");
        
        
    }
   public void genratePrescriptionId() {
        
        try {
             Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
             Statement stmt=con.createStatement();    
             ResultSet rs=stmt.executeQuery("SELECT MAX(PrescriptionID) FROM prescriptionrecord");
            while ( rs.next() ){
            int maxID = rs.getInt(1);
            maxID++;
            txtPrescriptionId.setText(maxID+"");
    }

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    SimpleDateFormat sdf;
    Timer t;
    
   public void showTime(){
   t=new Timer(0, new ActionListener() {

       @Override
       public void actionPerformed(ActionEvent e) {
          Date d=new Date();
             sdf   =  new SimpleDateFormat("hh:mm:ssa");
             String time=sdf.format(d);
             jLabel10.setText(time);
          
       }
   });
   t.start();
      }
   
  
    
   public void LoadDrugsToThe(){
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
           ResultSet rs=stmt.executeQuery("SELECT DrugName FROM drug");
            while (rs.next()) {
                cmbDrugName.addItem(rs.getString(1));        
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
   
   
   }
    public void LoadDocterToThecmb(){
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
           ResultSet rs=stmt.executeQuery("SELECT Name FROM docter");
            while (rs.next()) {
                cmbDocterName.addItem(rs.getString(1));        
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
   
   
   }
    int error=0;
    public void ChekMedicineRaction(){
     DefaultTableModel dtm=(DefaultTableModel) tblePres.getModel();
     int rwc=dtm.getRowCount();
      String s1="";
        for (int rw = 0; rw < rwc; rw++) {
            s1=dtm.getValueAt(rw, 0).toString();
           
            
        }
        try {
             String dname = null;
           Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
           Statement stmt=con.createStatement();    
           ResultSet rs=stmt.executeQuery("SELECT DrugName FROM drug where DrugId='"+s1+"'");
            while (rs.next()) {
                dname=rs.getString(1).toString();
   
            } 
            if (dtm.getRowCount()!=0) {
            if (dname.equals(cmbDrugName.getSelectedItem().toString())) {
            error++;
            JOptionPane.showMessageDialog(PrescriptionEntry.this, "Error The Drug is already in the list");
            }
            // check incompatibility
            Statement stmt1=con.createStatement();    
            ResultSet rs1=stmt1.executeQuery("SELECT IncompatibleDrugId FROM incompatibledrug where DrugId='"+s1+"'");
            Vector v=new Vector();
            String incomp = null;
            while (rs1.next()) {
                incomp=rs1.getString(1).toString();
                v.add(incomp);
                
            }
            Statement stmt2=con.createStatement();    
            ResultSet rs2=stmt2.executeQuery("SELECT DrugName FROM drug where DrugId='"+incomp+"'");
            String drugInName=null;
            while (rs2.next()) {
                drugInName=rs2.getString(1).toString();
                 
            }
            if (drugInName.equals(cmbDrugName.getSelectedItem().toString())) {
                   JFrame patient=new JFrame();
      JOptionPane optionPane= new JOptionPane("The drug you added can be incompatible, Do You",JOptionPane.INFORMATION_MESSAGE,JOptionPane.YES_NO_OPTION);
      JDialog dialog=optionPane.createDialog(PrescriptionEntry.this,"Add Medicine");
      dialog.setVisible(true);
      
             if ((int)optionPane.getValue()==0) {
                 
          
              
            }else if((int)optionPane.getValue()==1){
                  error=10;
                  System.out.println("Value is one");
            }   
                }
   
            }
           
       
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
    }
   public void addToPrescription(){
   genratePrescriptionId();
         try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT DrugID from drug WHERE drugName='"+cmbDrugName.getSelectedItem().toString()+"'");
            String drugId=""; 
            while (rs.next()) {                 
             drugId=rs.getString(1);
             }
            Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt1=c.createStatement();
            stmt1.executeUpdate("INSERT INTO prescriptionrecord(PrescriptionID,DrugId,Duration,Dosage,TimesPerDay,TimeTaken,IncompatibleFlag)VALUES('"+txtPrescriptionId.getText()+"','"+drugId+"','"+txtDuration.getText()+"','"+txtDos.getText()+"','"+cmbPerDay.getSelectedItem().toString()+"','"+cmdTimeTaken.getSelectedItem().toString()+"','"+0+"')");
           
            JOptionPane.showMessageDialog(PrescriptionEntry.this, "Sucesfully Drug add to the system");
            
            Connection connec=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt2=connec.createStatement();
            
            stmt2.executeUpdate("INSERT INTO prescription(PrescriptionID,PatientNIC,DocterNIC,Date)VALUES('"+txtPrescriptionId.getText()+"','"+txtPNIC.getText()+"','"+lbldocternic.getText()+"','"+jLabel10.getText()+"')");
            
            Statement stmt3=connec.createStatement();
            ResultSet result=stmt3.executeQuery("SELECT * from prescriptionrecord WHERE PrescriptionID='"+txtPrescriptionId.getText()+"'");
              DefaultTableModel dtm=(DefaultTableModel) tblePres.getModel();
            while (result.next()) { 
                Vector v=new Vector();
                v.add(result.getString(2));
                v.add(result.getString(3));
                v.add(result.getString(4));
                v.add(result.getString(5));
                v.add(result.getString(6));
                
               dtm.addRow(v);
            
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
   
   
   
   }
    
         
    
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbDrugName = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtDuration = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDos = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbPerDay = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmdTimeTaken = new javax.swing.JComboBox();
        btnAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPNIC = new javax.swing.JTextField();
        lblPatientName = new javax.swing.JLabel();
        lblPNICMessage = new javax.swing.JLabel();
        btnAddPatient = new javax.swing.JButton();
        btnCheck = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblePres = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtPrescriptionId = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cmbDocterName = new javax.swing.JComboBox();
        lbldocternic = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prescription Entry");
        setAlwaysOnTop(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Medication"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Drug Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 31, 110, -1));

        cmbDrugName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbDrugName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDrugNameActionPerformed(evt);
            }
        });
        jPanel1.add(cmbDrugName, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 27, 213, 29));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Duration");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 78, 107, -1));
        jPanel1.add(txtDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 74, 210, 29));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Days");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(361, 78, 103, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Dosage");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 126, 107, -1));
        jPanel1.add(txtDos, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 121, 84, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("mg");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(227, 126, 34, -1));

        cmbPerDay.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbPerDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        jPanel1.add(cmbPerDay, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 121, 78, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Times Per Day");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(361, 121, 103, 26));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Time Taken");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 174, 87, -1));

        cmdTimeTaken.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmdTimeTaken.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "After Meal", "Before Meal" }));
        cmdTimeTaken.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmdTimeTakenFocusLost(evt);
            }
        });
        jPanel1.add(cmdTimeTaken, new org.netbeans.lib.awtextra.AbsoluteConstraints(133, 169, 210, 30));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel1.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, 90, 30));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Patient"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Patient's NIC");

        txtPNIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPNICActionPerformed(evt);
            }
        });
        txtPNIC.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPNICFocusLost(evt);
            }
        });
        txtPNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPNICKeyTyped(evt);
            }
        });

        lblPatientName.setText("jLabel9");

        btnAddPatient.setText("ADD");
        btnAddPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPatientActionPerformed(evt);
            }
        });

        btnCheck.setText("Check Patient Details");
        btnCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPNIC, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblPatientName, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddPatient, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPNICMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPNICMessage)
                    .addComponent(btnCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPatientName, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddPatient))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Description"));

        tblePres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Drug ID", "Duration", "Dosage", "Times Per Day", "Time Taken"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblePres);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Prescription ID");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txtPrescriptionId, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 321, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPrescriptionId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("jLabel10");

        cmbDocterName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cmbDocterNameFocusLost(evt);
            }
        });

        lbldocternic.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lbldocternic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbldocternic.setText("jLabel11");

        jLabel11.setText("Docter Name");

        jLabel12.setText("Docter NIC");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 573, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbldocternic, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(118, 118, 118))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbDocterName, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDocterName, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbldocternic, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPNICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPNICActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPNICActionPerformed

    private void txtPNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPNICKeyTyped
      
    }//GEN-LAST:event_txtPNICKeyTyped

    private void txtPNICFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPNICFocusLost
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
           ResultSet rs=stmt.executeQuery("SELECT * FROM patient where PatientNIC='"+txtPNIC.getText()+"'");
            while (rs.next()) {
                String name=rs.getString(3).toString();
                String ststus=rs.getString(2).toString();
                
                lblPatientName.setText(ststus+" "+name);
               
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }//GEN-LAST:event_txtPNICFocusLost

    private void cmbDrugNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDrugNameActionPerformed
       
        
    }//GEN-LAST:event_cmbDrugNameActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

          ChekMedicineRaction();
        if (error==0) {
            addToPrescription();
        
        }else if(error==10){
        
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckActionPerformed
     lblPatientName.setText("");
         try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
            ResultSet rs=stmt.executeQuery("SELECT * FROM patient where PatientNIC='"+txtPNIC.getText()+"'");
            while (rs.next()) {
                String name=rs.getString(3).toString();
                String ststus=rs.getString(2).toString();
                
                lblPatientName.setText(ststus+" "+name);
               
                
            }
             if (lblPatientName.getText().equals("")) {
                 btnAddPatient.setVisible(true);
                 
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnCheckActionPerformed

    private void btnAddPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPatientActionPerformed
    new PatientDetails().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnAddPatientActionPerformed

    private void cmbDocterNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbDocterNameFocusLost
       
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
            ResultSet rs=stmt.executeQuery("SELECT NIC FROM docter where Name='"+cmbDocterName.getSelectedItem().toString()+"'");
            while (rs.next()) {
                String nic=rs.getString(1).toString();
                
                
                lbldocternic.setText(nic);
               
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmbDocterNameFocusLost

    private void cmdTimeTakenFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmdTimeTakenFocusLost
       
       
    }//GEN-LAST:event_cmdTimeTakenFocusLost

    private void txtPNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPNICKeyPressed
     if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
      lblPatientName.setText("");
         try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/dbclinic","root","");
            Statement stmt=con.createStatement();    
            ResultSet rs=stmt.executeQuery("SELECT * FROM patient where PatientNIC='"+txtPNIC.getText()+"'");
            while (rs.next()) {
                String name=rs.getString(3).toString();
                String ststus=rs.getString(2).toString();
                
                lblPatientName.setText(ststus+" "+name);
               
                
            }
             if (lblPatientName.getText().equals("")) {
                 btnAddPatient.setVisible(true);
                 
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
    }//GEN-LAST:event_txtPNICKeyPressed

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
            java.util.logging.Logger.getLogger(PrescriptionEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrescriptionEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrescriptionEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrescriptionEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrescriptionEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddPatient;
    private javax.swing.JButton btnCheck;
    private javax.swing.JComboBox cmbDocterName;
    private javax.swing.JComboBox cmbDrugName;
    private javax.swing.JComboBox cmbPerDay;
    private javax.swing.JComboBox cmdTimeTaken;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblPNICMessage;
    private javax.swing.JLabel lblPatientName;
    private javax.swing.JLabel lbldocternic;
    private javax.swing.JTable tblePres;
    private javax.swing.JTextField txtDos;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextField txtPNIC;
    private javax.swing.JTextField txtPrescriptionId;
    // End of variables declaration//GEN-END:variables
}
