package lab_8;

import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Gehad
 */
public class InstructorLessonsFrame extends javax.swing.JFrame {
private JsonDatabaseManager dbManager = new JsonDatabaseManager();
private LessonManager lessonManager = new LessonManager(); 
private Course selectedCourse=null; // Set this when opening the frame
private DefaultTableModel tableModel;
private Instructor instructor;
private CourseManager courseManager;

    public InstructorLessonsFrame(Instructor instructor) {
    initComponents();
    this.instructor=instructor;
    tableModel = (DefaultTableModel) LessonsTable.getModel();
    courseManager = new CourseManager(dbManager); 
    loadLessons();
}
public InstructorLessonsFrame() {
    initComponents();
}
 
private void loadLessons() {
        tableModel.setRowCount(0); // Clear existing rows

        List<Course> myCourses = dbManager.getCourses().stream()
                .filter(c -> c.getInstructorId().equals(instructor.getUserId()))
                .toList();

        for (Course course : myCourses) {
            for (Lesson lesson : course.getLessons()) {
                tableModel.addRow(new Object[]{
                        lesson.getLessonId(),   // first column: lesson ID
                        lesson.getTitle(),      // second column: lesson title
                        lesson.getContent(),    // third column: content
                        course.getCourseId()    // last column: course ID
                });
            }
        }
        addExtraRows(5);
        setupCourseIdDropdown();
    }
private void addExtraRows(int extraRows) {
for (int i = 0; i < extraRows; i++) {
tableModel.addRow(new Object[]{"", "", ""});
}
}
private void setupCourseIdDropdown() {
// Get the Course ID column index (assuming it's the 4th column, index 3)
int courseIdCol = 3;
// Create a JComboBox containing this instructor's courses
List<Course> myCourses = courseManager.getCoursesByInstructor(instructor.getUserId());
JComboBox<String> courseComboBox = new JComboBox<>();
for (Course c : myCourses) {
    courseComboBox.addItem(c.getCourseId());
}

// Set JComboBox as editor for the Course ID column
LessonsTable.getColumnModel().getColumn(courseIdCol).setCellEditor(new DefaultCellEditor(courseComboBox));

}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        LessonsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        CreateButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        BackButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("==Manage Your Lessons==");

        LessonsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Lesson ID", "Tilte", "Content", "Course ID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(LessonsTable);

        CreateButton.setText("Create");
        CreateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreateButtonActionPerformed(evt);
            }
        });

        UpdateButton.setText("Update");
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });

        DeleteButton.setText("Delete");
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });

        BackButton.setText("Back");
        BackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(CreateButton)
                        .addGap(34, 34, 34)
                        .addComponent(UpdateButton)
                        .addGap(46, 46, 46)
                        .addComponent(DeleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BackButton)
                        .addGap(31, 31, 31))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CreateButton)
                    .addComponent(UpdateButton)
                    .addComponent(DeleteButton)
                    .addComponent(BackButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CreateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreateButtonActionPerformed
     
int selectedRow = LessonsTable.getSelectedRow();
if (selectedRow == -1) {
JOptionPane.showMessageDialog(this, "Please select a row to create the lesson.");
return;
}
// Get course ID from the last column
String courseId = (tableModel.getValueAt(selectedRow, 3) != null) ? 
                  tableModel.getValueAt(selectedRow, 3).toString().trim() : "";

if (courseId.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Please enter a valid Course ID in the last column.");
    return;
}

// Find the course
selectedCourse = dbManager.getCourses().stream()
        .filter(c -> c.getCourseId().equals(courseId) &&
                     c.getInstructorId().equals(instructor.getUserId()))
        .findFirst()
        .orElse(null);

if (selectedCourse == null) {
    JOptionPane.showMessageDialog(this, "Course not found or you don't own this course.");
    return;
}

// Get title and content from the table row
String lessonTitle = (tableModel.getValueAt(selectedRow, 1) != null) ? 
                      tableModel.getValueAt(selectedRow, 1).toString().trim() : "New Lesson";
String lessonContent = (tableModel.getValueAt(selectedRow, 2) != null) ? 
                        tableModel.getValueAt(selectedRow, 2).toString().trim() : "";

// Generate a new lesson ID
int maxId = 0;
for (Lesson l : selectedCourse.getLessons()) {
    try {
        int id = Integer.parseInt(l.getLessonId().replaceAll("\\D", ""));
        if (id > maxId) maxId = id;
    } catch (Exception e) {}
}
String newLessonId = "L" + (maxId + 1);

// Create lesson using user's input
Lesson lesson = new Lesson(newLessonId, lessonTitle, lessonContent);
selectedCourse.addLesson(lesson);
dbManager.saveCourses();
loadLessons();
new InstructorCreateQuiz(instructor,courseManager,lessonManager).setVisible(true);

    }//GEN-LAST:event_CreateButtonActionPerformed

    private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
        if (selectedCourse == null) {
    javax.swing.JOptionPane.showMessageDialog(this, "Please load a course first.");
    return;
}

int row = LessonsTable.getSelectedRow();
if (row >= 0) {
    String lessonId = tableModel.getValueAt(row, 0).toString();
    boolean deleted = selectedCourse.deleteLesson(lessonId);

    if (deleted) {
        dbManager.saveCourses();   
        loadLessons();  // reload 
        javax.swing.JOptionPane.showMessageDialog(this, "Lesson deleted successfully.");
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Failed to delete lesson.");
    }
} else {
    javax.swing.JOptionPane.showMessageDialog(this, "Select a lesson to delete.");
}

    }//GEN-LAST:event_DeleteButtonActionPerformed

    private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
        if (selectedCourse == null) {
    javax.swing.JOptionPane.showMessageDialog(this, "Please load a course first.");
    return;
}

int row = LessonsTable.getSelectedRow();
if (row >= 0) {
    String lessonId = tableModel.getValueAt(row, 0).toString();
    String newTitle = tableModel.getValueAt(row, 1).toString();
    String newContent = tableModel.getValueAt(row, 2).toString();
    Lesson lessonToEdit = null;
    for (Lesson l : selectedCourse.getLessons()) {
        if (l.getLessonId().equals(lessonId)) {
            lessonToEdit = l;
            break;
        }
    }

    if (lessonToEdit != null) {
        lessonToEdit.setTitle(newTitle);
        lessonToEdit.setContent(newContent);
        dbManager.saveCourses();  
        loadLessons();  
        javax.swing.JOptionPane.showMessageDialog(this, "Lesson updated successfully!");
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Failed to find the lesson.");
    }
} else {
    javax.swing.JOptionPane.showMessageDialog(this, "Please select a lesson to update.");
}

    }//GEN-LAST:event_UpdateButtonActionPerformed


    private void BackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackButtonActionPerformed

    this.dispose();
    InstructorDashboardFrame dashboard = new InstructorDashboardFrame(instructor);
    dashboard.setVisible(true);
    }//GEN-LAST:event_BackButtonActionPerformed

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
            java.util.logging.Logger.getLogger(InstructorLessonsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InstructorLessonsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InstructorLessonsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InstructorLessonsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InstructorLessonsFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BackButton;
    private javax.swing.JButton CreateButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JTable LessonsTable;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
