/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ChartFrame extends JFrame {
private JTabbedPane tabbedPane;

public ChartFrame(String title) {
    super(title);
    setSize(900, 600);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    tabbedPane = new JTabbedPane();
    setContentPane(tabbedPane);
    setVisible(true);
}

public void updateCharts(Course course, List<Student> students, Instructor instructor) {
    tabbedPane.removeAll();

    int courseId = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));

    // 1. Student Performance Chart
    DefaultCategoryDataset studentDataset = new DefaultCategoryDataset();
    for (Student s : students) {
        for (Lesson lesson : course.getLessons()) {
            Quiz quiz = lesson.getQuiz();
            if (quiz != null) {
                Integer score = s.getQuizScore(courseId, quiz.getTitle());
                studentDataset.addValue(score != null ? score : 0, s.getUsername(), lesson.getTitle());
            }
        }
    }
    JFreeChart studentChart = ChartFactory.createLineChart(
            "Student Performance",
            "Lesson",
            "Score",
            studentDataset,
            PlotOrientation.VERTICAL,
            true, true, false
    );
    tabbedPane.addTab("Student Performance", new ChartPanel(studentChart));

    // 2. Quiz Averages per Lesson Chart
    DefaultCategoryDataset quizDataset = new DefaultCategoryDataset();
    Map<String, Double> quizAverages = instructor.calculateQuizAverages(course, students);
    for (String quizTitle : quizAverages.keySet()) {
        quizDataset.addValue(quizAverages.get(quizTitle), "Average Score", quizTitle);
    }
    JFreeChart quizChart = ChartFactory.createBarChart(
            "Quiz Averages per Lesson",
            "Quiz Title",
            "Average Score",
            quizDataset,
            PlotOrientation.VERTICAL,
            true, true, false
    );
    tabbedPane.addTab("Quiz Averages", new ChartPanel(quizChart));

    // 3. Course Completion Chart
    DefaultCategoryDataset completionDataset = new DefaultCategoryDataset();
    double courseCompletion = instructor.calculateCourseCompletion(course, students);
    completionDataset.addValue(courseCompletion, "Course Completion", course.getTitle());

    JFreeChart completionChart = ChartFactory.createBarChart(
            "Course Completion",
            "Course",
            "Completion %",
            completionDataset,
            PlotOrientation.VERTICAL,
            true, true, false
    );

    // Fix Y-axis to 0–100% and format as percentage
    NumberAxis rangeAxis = (NumberAxis) completionChart.getCategoryPlot().getRangeAxis();
    rangeAxis.setRange(0, 100);
    rangeAxis.setNumberFormatOverride(new java.text.DecimalFormat("0'%"));

    tabbedPane.addTab("Course Completion", new ChartPanel(completionChart));
}


}
