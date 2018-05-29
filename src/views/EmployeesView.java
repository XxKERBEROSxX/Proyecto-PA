package views;

import java.awt.event.ActionListener;
import managers.EmployeesManager;
import models.Employee;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The employeesview for the user use
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class EmployeesView extends javax.swing.JFrame {

    boolean editState = false;
    public static final int ADMINISTRATOR_ACCOUNT_TYPE = 1;
    public static final int NORMAL_ACCOUNT_TYPE = 2;
    int accountType = NORMAL_ACCOUNT_TYPE;

    /**
     * it Creates new Employees form
     */
    public EmployeesView() {
        initComponents();
        setLocationRelativeTo(null);
        EmployeesManager employeeManager = EmployeesManager.getInstance();
        setTitle("Empleados");
        rbtnAdministrator.setSelected(true);
        updateUI();
        
         /**
         * it Adds a new employee object to the list
         */
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (rbtnAdministrator.isSelected()) {
                    accountType = ADMINISTRATOR_ACCOUNT_TYPE;
                } else {
                    accountType = NORMAL_ACCOUNT_TYPE;
                }
                Employee employee = new Employee(txtUserName.getText(), txtName.getText(), txtPassword.getText(), accountType);
                employeeManager.addEmployee(employee.getUsername(), employee.getName(), employee.getPassword(), employee.getAccountType());
                clearFields();
                JOptionPane.showMessageDialog(null, "Empleado guardado");
                
            }

        });

        /**
         * Action to export file of Employees
         */
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Employee *.emp", "emp");
                chooser.setFileFilter(filter);
                int selectedOption = chooser.showSaveDialog(null);
                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    FileOutputStream outputStream = null;
                    try {
                        System.out.println("Saved this file: "
                                + chooser.getSelectedFile().getName());
                        outputStream = new FileOutputStream(chooser.getSelectedFile());
                        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
                            ArrayList<Employee> Employees = EmployeesManager.getInstance().getEmp();
                            objectStream.writeInt(Employees.size());
                            for (Employee c : Employees) {
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
         * Action to import file of employes
         */
        btnImport.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Employee *.emp", "emp");
            chooser.setFileFilter(filter);
            int selectedOption = chooser.showOpenDialog(null);
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                try {
                    try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile());
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                        int numberOfEmployees = objectInputStream.readInt();
                        for (int i = 0; i < numberOfEmployees; i++) {
                            Employee b = (Employee) objectInputStream.readObject();
                            employeeManager.addEmployee(b.getUsername(), b.getName(), b.getPassword(), b.getAccountType());
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
         * Event to find a book and show it to the user 
         */
        btnSearch.addActionListener((ActionEvent ae) -> {
            int id = employeeManager.getEmployeeIndexByUserName(txtSearch.getText());
            if (id != -1) {
                Employee employee = EmployeesManager.getInstance().getEmployee(id);
                if (employee != null) {
                    updateFields(employee);
                    editState = true;
                    updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el usuario");
            }

        });

        /**
         * Event to delete a employee object
         */
        btnDelete.addActionListener((ActionEvent ae) -> {
            int id = employeeManager.getEmployeeIndexByUserName(txtSearch.getText());
            Employee employee = EmployeesManager.getInstance().getEmployee(id);
            int dialogOption = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar " + employee.getName() + "?");
            if (dialogOption == JOptionPane.YES_OPTION) {
                employeeManager.deleteEmployee(id);
                editState = false;
                clearFields();
                updateUI();
            }

        });
        /**
         * Event to edit a employee object
         */
        btnEdit.addActionListener((ActionEvent ae) -> {
            int id = employeeManager.getEmployeeIndexByUserName(txtSearch.getText());
            if (rbtnAdministrator.isSelected()) {
                accountType = ADMINISTRATOR_ACCOUNT_TYPE;
            }
            if (rbtnNormalUser.isSelected()) {
                accountType = NORMAL_ACCOUNT_TYPE;
            }
            if (id != -1) {
                employeeManager.updateEmployee(id, txtUserName.getText(), txtName.getText(), txtPassword.getText(), accountType);
                JOptionPane.showMessageDialog(null, "Empleado actualizado");
                clearFields();
                editState = false;
                updateUI();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el empleado");
            }
        });
    }
    /**
     * it activates the button edit and the button delete after we have given an id to search
     */
    private void updateUI() {
        btnDelete.setEnabled(editState);
        btnEdit.setEnabled(editState);
    }
    /**
     * it updates the fields with the received object
     *
     * @param employee Employee that we are going to show info for
     */
    private void updateFields(Employee employee) {
        txtName.setText(employee.getName());
        txtUserName.setText(employee.getUsername());
        txtPassword.setText(String.valueOf(employee.getPassword()));
        if (employee.getAccountType() == ADMINISTRATOR_ACCOUNT_TYPE) {
            rbtnAdministrator.setSelected(true);
        } else {
            rbtnNormalUser.setSelected(true);
        }
    }
    /**
     * it clears all fields
     */
    private void clearFields() {
        txtName.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
        rbtnNormalUser.setSelected(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupUserType = new javax.swing.ButtonGroup();
        jScrollBar1 = new javax.swing.JScrollBar();
        lblUserName = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtUserName = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        lblSearchId = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        bannerEmployee1 = new views.BannerEmployee();
        btnSearch = new javax.swing.JButton();
        rbtnNormalUser = new javax.swing.JRadioButton();
        rbtnAdministrator = new javax.swing.JRadioButton();
        lblUserType = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnFile = new javax.swing.JMenu();
        btnExport = new javax.swing.JMenuItem();
        btnImport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblUserName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblUserName.setText("Nombre de usuario:");

        lblName.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblName.setText("Nombre:");

        lblPassword.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblPassword.setText("Contraseña:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        txtPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPasswordActionPerformed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnNew.setText("Nuevo");

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnEdit.setText("Modificar");

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnDelete.setText("Borrar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lblSearchId.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSearchId.setText("Buscar por nombre de usuario: ");

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnBack.setText("Regresar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSearch.setText("Buscar");

        btnGroupUserType.add(rbtnNormalUser);
        rbtnNormalUser.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rbtnNormalUser.setText("Usuario Normal");

        btnGroupUserType.add(rbtnAdministrator);
        rbtnAdministrator.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        rbtnAdministrator.setText("Administrador");

        lblUserType.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblUserType.setText("Tipo de usuario:");

        btnFile.setText("Archivo");

        btnExport.setText("Exportar empleados");
        btnExport.setToolTipText("Se exportaran todos los empleados.");
        btnFile.add(btnExport);

        btnImport.setText("Importar empleados");
        btnImport.setToolTipText("Se importaran todos los empleados.");
        btnFile.add(btnImport);

        jMenuBar1.add(btnFile);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bannerEmployee1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSearchId)
                        .addGap(37, 37, 37)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblUserName, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(lblUserType))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(rbtnNormalUser)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(rbtnAdministrator))
                                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtPassword)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(8, 8, 8)))))
                .addGap(42, 42, 42))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(bannerEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblSearchId)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserType)
                    .addComponent(rbtnNormalUser)
                    .addComponent(rbtnAdministrator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void txtPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPasswordActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(EmployeesView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeesView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeesView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeesView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeesView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private views.BannerEmployee bannerEmployee1;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JMenuItem btnExport;
    private javax.swing.JMenu btnFile;
    private javax.swing.ButtonGroup btnGroupUserType;
    private javax.swing.JMenuItem btnImport;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSearchId;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblUserType;
    private javax.swing.JRadioButton rbtnAdministrator;
    private javax.swing.JRadioButton rbtnNormalUser;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables
}
