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
import managers.BooksManager;
import models.Book;

/**
 * The booksview for the user use
 * @author Sergio, Ericka, Alejandro, Carlos
 */
public class BooksView extends javax.swing.JFrame {

    boolean editState = false;

    /**
     * it Creates new Books form
     */
    public BooksView() {
        initComponents();
        setTitle("Libros");
        setLocationRelativeTo(null);
        BooksManager bookManager = BooksManager.getInstance();
        updateUI();
        /**
         * it Adds a new book object to the list
         */
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Book book = new Book(txtId.getText(), txtISBN.getText(), txtAuthor.getText(), txtName.getText(), txtEdition.getText(), txtEditorial.getText(), true);
                bookManager.addBook(book.getId(), book.getISBN(), book.getAuthor(), book.getName(), book.getEdition(), book.getEditorial(), book.getState());
                clearFields();
                JOptionPane.showMessageDialog(null, "Libro guardado, ya puedes prestarlo.");

            }
        });

        /**
         * Action to export file of books
         */
        btnExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Books *.bks", "bks");
                chooser.setFileFilter(filter);
                int selectedOption = chooser.showSaveDialog(null);
                if (selectedOption == JFileChooser.APPROVE_OPTION) {
                    FileOutputStream outputStream = null;
                    try {
                        System.out.println("Saved this file: "
                                + chooser.getSelectedFile().getName());
                        outputStream = new FileOutputStream(chooser.getSelectedFile());
                        try (ObjectOutputStream objectStream = new ObjectOutputStream(outputStream)) {
                            ArrayList<Book> books = BooksManager.getInstance().getBooks();
                            objectStream.writeInt(books.size());
                            for (Book c : books) {
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
         * Action to import file of books
         */
        btnImport.addActionListener((ActionEvent event) -> {
            JFileChooser chooser = new JFileChooser();

            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Books *.bks", "bks");
            chooser.setFileFilter(filter);
            int selectedOption = chooser.showOpenDialog(null);
            if (selectedOption == JFileChooser.APPROVE_OPTION) {
                try {
                    try (FileInputStream inputStream = new FileInputStream(chooser.getSelectedFile());
                            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                        int numberOfContacts = objectInputStream.readInt();
                        for (int i = 0; i < numberOfContacts; i++) {
                            Book b = (Book) objectInputStream.readObject();
                            bookManager.addBook(b.getId(), b.getISBN(), b.getAuthor(), b.getName(), b.getEdition(), b.getEditorial(), b.getState());
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
            int id = bookManager.getBookIndexById(txtSearch.getText());
            if (id != -1) {
                Book book = BooksManager.getInstance().getBook(id);
                if (book != null) {
                    updateFields(book);
                    editState = true;
                    updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el libro");
            }

        });

        /**
         * Event to delete a book object
         */
        btnDelete.addActionListener((ActionEvent ae) -> {
            int id = bookManager.getBookIndexById(txtSearch.getText());
            Book book = BooksManager.getInstance().getBook(id);
            if (book.getState() == true) {
                int dialogOption = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminar " + book.getName() + "?");
                if (dialogOption == JOptionPane.YES_OPTION) {
                    bookManager.deleteBook(id);
                    editState = false;
                    clearFields();
                    updateUI();
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se puede eliminar. El libro está prestado");
            }

        });
        /**
         * Event to edit a book object
         */
        btnEdit.addActionListener((ActionEvent ae) -> {
            int id = bookManager.getBookIndexById(txtSearch.getText());
            if (id != -1) {
                bookManager.updateBook(id, txtId.getText(), txtISBN.getText(), txtAuthor.getText(), txtName.getText(), txtEdition.getText(), txtEditorial.getText());
                JOptionPane.showMessageDialog(null, "Libro actualizado");
                clearFields();
                editState = false;
                updateUI();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el libro");
            }
        });

    }

    /**
     * it actives button edit and delete after we have provided an id to search for
     */
    private void updateUI() {
        btnEdit.setEnabled(editState);
        btnDelete.setEnabled(editState);
    }

    /**
     * update the fields with the received object 
     * @param book Book that we are going to get info from
     */
    private void updateFields(Book book) {
        String state = book.getState() ? "Disponible" : "Prestado";
        lblStateShow.setText(state);
        txtId.setText(book.getId());
        txtISBN.setText(book.getISBN());
        txtAuthor.setText(book.getAuthor());
        txtName.setText(book.getName());
        txtEdition.setText(book.getEdition());
        txtEditorial.setText(book.getEditorial());
    }

    /**
     * it clears all fields
     */
    private void clearFields() {
        lblStateShow.setText("-");
        txtId.setText("");
        txtISBN.setText("");
        txtAuthor.setText("");
        txtName.setText("");
        txtEdition.setText("");
        txtEditorial.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnNew = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        lblId = new javax.swing.JLabel();
        txtISBN = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtAuthor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtEdition = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtEditorial = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        bannerBooks1 = new views.BannerBooks();
        lblSearch = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        lblStateShow = new javax.swing.JLabel();
        lblState1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        btnFile = new javax.swing.JMenu();
        btnExport = new javax.swing.JMenuItem();
        btnImport = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnNew.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnNew.setText("Nuevo");

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnEdit.setText("Editar");

        btnDelete.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        btnDelete.setText("Borrar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        btnBack.setText("Regresar");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblId.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblId.setText("ID:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel2.setText("ISBN:");

        txtAuthor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuthorActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel3.setText("Autor:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel5.setText("Edición:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel6.setText("Editorial:");

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        jLabel4.setText("Nombre:");

        lblSearch.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblSearch.setText("Buscar:");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N

        btnSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSearch.setText("Buscar");

        lblStateShow.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblStateShow.setText("-");

        lblState1.setFont(new java.awt.Font("Tahoma", 0, 22)); // NOI18N
        lblState1.setText("Estado:");

        btnFile.setText("Archivo");

        btnExport.setText("Exportar libros");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        btnFile.add(btnExport);

        btnImport.setText("Importar libros");
        btnFile.add(btnImport);

        jMenuBar1.add(btnFile);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bannerBooks1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(8, 8, 8))
                            .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(txtEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(376, 376, 376)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addGap(304, 304, 304)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblState1)
                                .addGap(30, 30, 30)
                                .addComponent(lblStateShow, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(lblId))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtId, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(bannerBooks1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblState1)
                    .addComponent(lblStateShow))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtEdition, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblId, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNew, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtAuthorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuthorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAuthorActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExportActionPerformed

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
            java.util.logging.Logger.getLogger(BooksView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BooksView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BooksView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BooksView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BooksView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private views.BannerBooks bannerBooks1;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JMenuItem btnExport;
    private javax.swing.JMenu btnFile;
    private javax.swing.JMenuItem btnImport;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblSearch;
    private javax.swing.JLabel lblState1;
    private javax.swing.JLabel lblStateShow;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtEdition;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtISBN;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
