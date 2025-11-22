/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_8;

public class Lesson {

    private String lessonId;
    private String title;
    private String content;
    private Quiz quiz;  // only one quiz
    private boolean completed;

    public Lesson(String lessonId, String title, String content) {
        this.lessonId = lessonId;
        this.title = title;
        this.content = content;
        this.quiz = null;  // no quiz yet
        this.completed = false;
    }

    // Getters
    public String getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Quiz getQuiz() { return quiz; }
    public boolean isCompleted() { return completed; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setQuiz(Quiz quiz) { this.quiz = quiz; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return lessonId + ": " + title + " - " + content;
    }
}
