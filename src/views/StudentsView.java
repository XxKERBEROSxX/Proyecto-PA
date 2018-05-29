package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import managers.StudentsManager;
import models.Student;

/**
 * The Studentview for the user use
 *
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class StudentsView extends javax.swing.JFrame {

    /**
     * it Creates new Students form
     * @param editState = tells us if the buttons delete and edit are active or not
     */
    boolean editState = false;

    public StudentsView() {
        initComponents();
        setTitle("Libros");
        setLocationRelativeTo(null);
        StudentsManager studentManager = StudentsManager.getInstance();
        updateUI();

        /**
         * it Adds a new Student object to the list
         */
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Student student = new Student(txtId.getText(), txtName.getText(), txtAddress.getText(), txtPhoneNumber.getText());
                studentManager.addStudent(student.getId(), student.getName(), student.getAddress(), student.getPhone_number());
                clearFields();
                JOptionPane.showMessageDialog(null, "Estudiante añadido");

            }
        });

        /**
         * Action to export file of Students
         */
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Student *.std", "std");
                chooser.setFileFilter(filter);
                int selectedOption = chooser.showSaveDialog(null);
                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    FileOutputStream outputStream = null;
                    try {
                        System.out.println("Saved this file: "
                                + chooser.getSelectedFile().getName());
                        outputStream = new FileOutputStream(chooser.getSelectedFile());
                        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
                            ArrayList<Student> student = StudentsManager.getInstance().getStudents();
                            objectStream.writeInt(student.size());
                            for (Student c : student) {
                                objectStream.writeObject(c);
                            }
                        }
                    } catch (IOException ex) {
                    } finally {
                        try {
                            outputStream.close();
                        } catch (IOException ex) {
                        }
                    }

                }
            }
        });

        /**
         * Action to import file of Students
         */
        btnImport.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Student *.std", "std");
            chooser.setFileFilter(filter);
            int selectedOption = chooser.showOpenDialog(null);
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                try {
                    try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile());
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                        int numberOfStudent = objectInputStream.readInt();
                        for (int i = 0; i < numberOfStudent; i++) {
                            Student b = (Student) objectInputStream.readObject();
                            studentManager.addStudent(b.getId(), b.getName(), b.getAddress(), b.getPhone_number());
                        }
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "File does not exist");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Can't open the file");
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Class is not a book.");
                }
            }
        });

        /**
         * Event to find a Student and show it to the user
         */
        btnSearch.addActionListener((ActionEvent ae) -> {
            int id = studentManager.getStudentIndexById(txtSearch.getText());
            if (id != -1) {
                Student student = StudentsManager.getInstance().getStudent(id);
                if (student != null) {
                    updateFields(student);
                    editState = true;
                    updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el estudiante");
            }

        });
        /**
         * Action to delete a Student
         */

        btnDelete.addActionListener((ActionEvent ae) -> {
            int id = studentManager.getStudentIndexById(txtSearch.getText());
            Student student = StudentsManager.getInstance().getStudent(id);

            int dialogOption = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar " + student.getName() + "?");
            if (dialogOption == JOptionPane.YES_OPTION) {
                studentManager.deleteStudent(id);
                editState = false;
                clearFields();
                updateUI();
            }

        });

        /**
         * Event to edit a Student object
         */
        btnEdit.addActionListener((ActionEvent ae) -> {
            int id = studentManager.getStudentIndexById(txtSearch.getText());
            if (id != -1) {
                studentManager.updateStudent(id, txtId.getText(), txtName.getText(), txtAddress.getText(), txtPhoneNumber.getText());
                JOptionPane.showMessageDialog(null, "Estudiante actualizado");
                clearFields();
                editState = false;
                updateUI();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el estudiante");
            }
        });
    }

    /**
     * it actives button edit
     */
    private void updateUI() {
        btnEdit.setEnabled(editState);
        btnDelete.setEnabled(editState);
    }

    /**
     * it clears all fields
     */
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPhoneNumber.setText("");
        txtAddress.setText("");

    }

    /**
     * it updates the fields with the received object
     *
     * @param student Student of which we are going to show the data
     */
    private void updateFields(Student student) {
        txtId.setText(student.getId());
        txtName.setText(student.getName());
        txtPhoneNumber.setText(student.getPhone_number());
        txtAddress.setText(student.getAddress());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblName = new javax.swing.JLabel();
        lblPhoneNumber = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtPhoneNumber = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtId = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        bannerStudents1 = new views.BannerStudents();
        txtSearch = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnFile = new javax.swing.JMenu();
        btnExport = new javax.swing.JMenuItem();
        btnImport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblName.setText("Nombre del estudiante:");

        lblPhoneNumber.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblPhoneNumber.setText("Número de teléfono:");

        lblAddress.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblAddress.setText("Dirección:");

        lblId.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblId.setText("ID:");

        txtName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N

        txtPhoneNumber.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N

        txtId.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnNew.setText("Nuevo");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnEdit.setText("Editar");

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnDelete.setText("Borrar");

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnBack.setText("Regresar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        lblSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSearch.setText("Buscar estudiante por ID:");

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSearch.setText("Buscar");

        btnFile.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnFile.setText("Archivo");

        btnExport.setText("Exportar estudiantes");
        btnExport.setToolTipText("Se exportarán todos los estudiantes.");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        btnFile.add(btnExport);

        btnImport.setText("Importar estudiantes");
        btnImport.setToolTipText("Se importaran todos los estudiantes.");
        btnFile.add(btnImport);

        jMenuBar1.add(btnFile);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bannerStudents1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(lblAddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblPhoneNumber, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(txtName)
                            .addComponent(txtPhoneNumber)
                            .addComponent(txtAddress))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bannerStudents1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSearch)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(lblSearch)))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhoneNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

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
            java.util.logging.Logger.getLogger(StudentsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StudentsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StudentsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StudentsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* it Creates and displays the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new StudentsView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private views.BannerStudents bannerStudents1;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JMenuItem btnExport;
    private javax.swing.JMenu btnFile;
    private javax.swing.JMenuItem btnImport;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPhoneNumber;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
