/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

/**
 *
 * @author New Eng
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AdminFrame extends javax.swing.JFrame {

    private Admin admin;
    private CourseManager courseManager;

    private JTable pendingTable;
    private DefaultTableModel model;

    private JButton approveBtn;
    private JButton rejectBtn;
    private JButton refreshBtn;
    private JButton logoutBtn;

    public AdminFrame(Admin admin, CourseManager courseManager) {
        this.admin = admin;
        this.courseManager = courseManager;

        setTitle("Admin Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadPendingCourses();
    }

    private void initComponents() {

        model = new DefaultTableModel();
        model.addColumn("Course ID");
        model.addColumn("Title");
        model.addColumn("Description");
        model.addColumn("Instructor");

        pendingTable = new JTable(model);
        JScrollPane scroll = new JScrollPane(pendingTable);

        approveBtn = new JButton("Approve");
        rejectBtn = new JButton("Reject");
        refreshBtn = new JButton("Refresh");
        logoutBtn = new JButton("Logout");

        approveBtn.addActionListener(e -> approveCourse());
        rejectBtn.addActionListener(e -> rejectCourse());
        refreshBtn.addActionListener(e -> loadPendingCourses());
        logoutBtn.addActionListener(e -> logout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(logoutBtn);

        add(scroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }


    private void loadPendingCourses() {
        model.setRowCount(0);  

        ArrayList<Course> pending = courseManager.getPendingCourses();

        for (Course c : pending) {
            model.addRow(new Object[]{
                    c.getCourseId(),
                    c.getTitle(),
                    c.getDescription(),
                    c.getInstructorId()
            });
        }
    }


    private void approveCourse() {
        int row = pendingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course.");
            return;
        }

        String courseId = model.getValueAt(row, 0).toString();

        if (admin.approveCourse(courseManager, courseId)) {
            JOptionPane.showMessageDialog(this, "Course approved!");
            loadPendingCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to approve course.");
        }
    }


    private void rejectCourse() {
        int row = pendingTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to reject.");
            return;
        }

        String courseId = model.getValueAt(row, 0).toString();

        if (admin.rejectCourse(courseManager, courseId)) {
            JOptionPane.showMessageDialog(this, "Course rejected!");
            loadPendingCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to reject course.");
        }
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}

