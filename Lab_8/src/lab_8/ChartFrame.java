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

        // IMPORTANT: Do NOT show automatically. Instructor chooses.
        // setVisible(true);
    }

    public void updateCharts(Course course, List<Student> students, Instructor instructor) {
tabbedPane.removeAll();

int courseId = Integer.parseInt(course.getCourseId().replaceAll("\\D", ""));

// -------------------- 1. Student Performance Chart --------------------
DefaultCategoryDataset studentDataset = new DefaultCategoryDataset();
for (Lesson lesson : course.getLessons()) {
    String lessonTitle = lesson.getTitle();
    Quiz quiz = lesson.getQuiz();
    for (Student s : students) {
        Integer score = (quiz != null) ? s.getQuizScore(courseId, quiz.getTitle()) : null;
        // Convert score to percentage (assuming max score is quiz.getQuestions().size())
        double percentage = (score != null && quiz != null) ? (score * 100.0 / quiz.getQuestions().size()) : 0;
        studentDataset.addValue(percentage, s.getUsername(), lessonTitle);
    }
}
JFreeChart studentChart = ChartFactory.createLineChart(
        "Student Performance",
        "Lesson",
        "Score (%)",
        studentDataset,
        PlotOrientation.VERTICAL,
        true, true, false
);
NumberAxis studentRangeAxis = (NumberAxis) studentChart.getCategoryPlot().getRangeAxis();
studentRangeAxis.setRange(0, 100); // Y-axis 0–100%
tabbedPane.addTab("Student Performance", new ChartPanel(studentChart));

// -------------------- 2. Quiz Averages per Lesson Chart --------------------
DefaultCategoryDataset quizDataset = new DefaultCategoryDataset();
Map<String, Double> quizAverages = instructor.calculateQuizAverages(course, students);
for (String quizTitle : quizAverages.keySet()) {
    Quiz quiz = course.getLessons().stream()
            .map(Lesson::getQuiz)
            .filter(q -> q != null && q.getTitle().equals(quizTitle))
            .findFirst().orElse(null);
    if (quiz != null) {
        double avgPercent = (quizAverages.get(quizTitle) * 100.0 / quiz.getQuestions().size());
        quizDataset.addValue(avgPercent, "Average Score", quizTitle);
    }
}
JFreeChart quizChart = ChartFactory.createBarChart(
        "Quiz Averages per Lesson",
        "Quiz",
        "Average Score (%)",
        quizDataset,
        PlotOrientation.VERTICAL,
        true, true, false
);
NumberAxis quizRangeAxis = (NumberAxis) quizChart.getCategoryPlot().getRangeAxis();
quizRangeAxis.setRange(0, 100); // Y-axis 0–100%
tabbedPane.addTab("Quiz Averages", new ChartPanel(quizChart));

// -------------------- 3. Course Completion Chart --------------------
DefaultCategoryDataset completionDataset = new DefaultCategoryDataset();
for (Student s : students) {
    double completionPercent = instructor.calculateCourseCompletion(course, List.of(s));
    completionDataset.addValue(completionPercent, s.getUsername(), course.getTitle());
}
JFreeChart completionChart = ChartFactory.createBarChart(
        "Course Completion",
        "Student",
        "Completion (%)",
        completionDataset,
        PlotOrientation.VERTICAL,
        true, true, false
);
NumberAxis completionRangeAxis = (NumberAxis) completionChart.getCategoryPlot().getRangeAxis();
completionRangeAxis.setRange(0, 100);
tabbedPane.addTab("Course Completion", new ChartPanel(completionChart));

}
}
