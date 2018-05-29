package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import managers.BooksManager;
import managers.LoansManager;
import managers.StudentsManager;
import models.Book;
import models.Loan;
import models.Student;
import utils.DateUtils;

/**
 *
 * @author Sergio, Carlos, Alex, Ericka
 */
public class LoanBookView extends javax.swing.JFrame implements Serializable {

    boolean editState = false;
    private static final int DAYS_TO_LOAN = 3;
    private static final double TICKET_PRICE = 15.00;

    /**
     * it Creates new Loanbook form
     */
    public LoanBookView() {
        initComponents();
        setTitle("Libros");
        setLocationRelativeTo(null);
        LoansManager loanManager = LoansManager.getInstance();
        BooksManager bookManager = BooksManager.getInstance();
        StudentsManager studentManager = StudentsManager.getInstance();
        updateUI();

        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                int currentBook = bookManager.getBookIndexById(txtBookId.getText());
                int currentStudent = studentManager.getStudentIndexById(txtUser.getText());
                if (currentBook != -1 && currentStudent != -1) {
                    if (loanManager.getLoan(currentBook) == null) {
                        Date startDate = new Date();
                        Loan loan = new Loan(txtBookId.getText(), txtUser.getText(), startDate, DateUtils.addDays(startDate, DAYS_TO_LOAN), 0.0);
                        loanManager.addLoan(loan.getUserId(), loan.getBookId());
                        JOptionPane.showMessageDialog(null, "Préstamo guardado");
                    }else{
                        JOptionPane.showMessageDialog(null, "El libro ya está prestado");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Libro o estudiante no existen");
                }

            }
        });

        /**
         * Event to export file of Loans
         */
        
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Prestamos *.psrt", "psrt");
                chooser.setFileFilter(filter);
                int selectedOption = chooser.showSaveDialog(null);
                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    FileOutputStream outputStream = null;
                    try {
                        System.out.println("Saved this file: "
                                + chooser.getSelectedFile().getName());
                        outputStream = new FileOutputStream(chooser.getSelectedFile());
                        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
                            ArrayList<Loan> loans = LoansManager.getInstance().getLoans();
                            objectStream.writeInt(loans.size());
                            for (Loan c : loans) {
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
         * Event to import file of Loans
         */
         btnImport.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                   "Préstamos *.psrt", "psrt");
            chooser.setFileFilter(filter);
            int selectedOption = chooser.showOpenDialog(null);
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                try {
                    try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile());
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                        int numberOfLoan = objectInputStream.readInt();
                        for (int i = 0; i < numberOfLoan; i++) {
                            Loan b = (Loan) objectInputStream.readObject();
                            loanManager.addLoan(b.getBookId(),b.getUserId());
                        }
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "File does not exist");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Can't open the file");
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Class is not a loan.");
                }
            }
        });

        
        /**
         * Event to find a loan details and show them to the user
         */
        btnSearch.addActionListener((ActionEvent ae) -> {
            int loanId = loanManager.getLoanIndexByBookId(txtSearch.getText());
            if (loanId != -1) {
                Loan currentLoan = loanManager.getLoan(loanId);
                int bookId = bookManager.getBookIndexById(txtSearch.getText());
                if (bookId != -1) {
                    Book book = BooksManager.getInstance().getBook(bookId);
                    if (book != null) {
                        int studentIndex = studentManager.getStudentIndexById(currentLoan.getUserId());
                        Student currentStudent = studentManager.getStudent(studentIndex);
                        Date todaysDate = new Date();
                        lbl2UserName.setText(currentStudent.getName());
                        lbl2BookName.setText(book.getName());
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        lbl2Expire.setText(df.format(currentLoan.getDueDate()));
                        double daysExpired = DateUtils.differenceBetweenDays(todaysDate, currentLoan.getDueDate());
                        if (daysExpired > 0) {
                            lbl2Ticket.setText("$0.0");
                        } else {
                            double ticket = Math.abs(daysExpired) * TICKET_PRICE;
                            lbl2Ticket.setText(String.valueOf(ticket));
                        }

                        editState = true;
                        updateUI();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró el libro");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El libro no está prestado");
            }

        });

        /**
         * Event to return a book
         */
        btnDelete.addActionListener((ActionEvent ae) -> {
            int loanId = loanManager.getLoanIndexByBookId(txtSearch.getText());
            boolean returnComplete = true;
            if (loanId != -1) {
                Loan loan = LoansManager.getInstance().getLoan(loanId);
                Date todaysDate = new Date();
                double daysExpired = DateUtils.differenceBetweenDays(todaysDate, loan.getDueDate());
                if (daysExpired < 0) {
                    double ticket = Math.abs(daysExpired) * TICKET_PRICE;
                    int dialogOption = JOptionPane.showConfirmDialog(null, "¿Pagar multa de $" + ticket + "?");
                    if (dialogOption == JOptionPane.NO_OPTION) {
                        returnComplete = false;
                    }
                }
                if (returnComplete) {
                    loanManager.returnBook(loanId);
                    clearFields();
                    updateUI();
                    JOptionPane.showMessageDialog(null, "Libro retornado con éxito");
                } else {
                    JOptionPane.showMessageDialog(null, "El usuario tiene que pagar antes de regresar el libro");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el libro");
            }

        });

        /**
         * Event to add extra days to the book
         */
        btnEdit.addActionListener((ActionEvent ae) -> {
            int loanId = loanManager.getLoanIndexByBookId(txtSearch.getText());
            if (loanId != -1) {
                loanManager.updateLoan(txtSearch.getText());
                JOptionPane.showMessageDialog(null, "Nueva fecha: " + loanManager.getLoan(loanId).getDueDate());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                lbl2Expire.setText(df.format(loanManager.getLoan(loanId).getDueDate()));

            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el libro");
            }
        });
    }

    /**
     * enables buttons edit and delete after a book has been looked up
    */
    private void updateUI() {
        btnEdit.setEnabled(editState);
        btnDelete.setEnabled(editState);

    }

    /**
     * clears fields
     */
    private void clearFields() {
        txtBookId.setText("");
        txtUser.setText("");
        lbl2BookName.setText("");
        lbl2Expire.setText("");
        lbl2Ticket.setText("");
        lbl2UserName.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblBookId = new javax.swing.JLabel();
        txtBookId = new javax.swing.JTextField();
        lblUser = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        lblSearch = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        bannerBooks1 = new views.BannerBooks();
        jSeparator1 = new javax.swing.JSeparator();
        lblTicket = new javax.swing.JLabel();
        lbl2Ticket = new javax.swing.JLabel();
        lblExpire = new javax.swing.JLabel();
        lbl2Expire = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblUserName = new javax.swing.JLabel();
        lbl2UserName = new javax.swing.JLabel();
        lblBookName = new javax.swing.JLabel();
        lbl2BookName = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        btnExport = new javax.swing.JMenuItem();
        btnImport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBookId.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblBookId.setText("Id libro:");

        txtBookId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBookIdActionPerformed(evt);
            }
        });

        lblUser.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblUser.setText("Usuario:");

        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnNew.setText("Prestar");
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnEdit.setText("Renovar");

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnDelete.setText("Retorno");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        lblSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblSearch.setText("Buscar Libro: ");

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnSearch.setText("Buscar");

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnBack.setText("Regresar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTicket.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblTicket.setText("Multa:");

        lbl2Ticket.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lbl2Ticket.setText("---------");

        lblExpire.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblExpire.setText("Vencimiento:");

        lbl2Expire.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lbl2Expire.setText("---------");

        lblUserName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblUserName.setText("Nombre estudiante:");

        lbl2UserName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lbl2UserName.setText("---------");

        lblBookName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblBookName.setText("Nombre libro:");

        lbl2BookName.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lbl2BookName.setText("---------");

        jMenu1.setText("Archivo");

        btnExport.setText("Exportar Prestamos...");
        jMenu1.add(btnExport);

        btnImport.setText("Importar Prestamos..");
        jMenu1.add(btnImport);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblUser, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblBookId, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnSearch))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtBookId, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                                .addComponent(txtUser))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addGap(3, 3, 3))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(bannerBooks1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(lbl2UserName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(lblBookName, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbl2BookName, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl2Ticket, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbl2Expire, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(35, 35, 35)
                            .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblExpire, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(bannerBooks1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSearch)
                    .addComponent(btnSearch))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBookId, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBookId, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUser)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUserName)
                    .addComponent(lbl2UserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBookName)
                    .addComponent(lbl2BookName))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExpire)
                    .addComponent(lbl2Expire))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTicket)
                    .addComponent(lbl2Ticket))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void txtBookIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBookIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBookIdActionPerformed

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
            java.util.logging.Logger.getLogger(LoanBookView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoanBookView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoanBookView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoanBookView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new LoanBookView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private views.BannerBooks bannerBooks1;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JMenuItem btnExport;
    private javax.swing.JMenuItem btnImport;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbl2BookName;
    private javax.swing.JLabel lbl2Expire;
    private javax.swing.JLabel lbl2Ticket;
    private javax.swing.JLabel lbl2UserName;
    private javax.swing.JLabel lblBookId;
    private javax.swing.JLabel lblBookName;
    private javax.swing.JLabel lblExpire;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblTicket;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JTextField txtBookId;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
