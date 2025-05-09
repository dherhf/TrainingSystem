package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Data
public class Student {
    @Id
    private String id;//学生ID
    @Column(name = "project_id", nullable = false)
    private int projectId;//项目ID
    @Column(name = "student_name", nullable = false)
    private String studentName;//学生名字
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;//入学日期
    @Column(name = "tuition", nullable = false)
    private int tuition;//学费
    @Column(name = "grades", nullable = false)
    private int grades;//成绩

    public Student() {
    }

    public Student(String id, int projectId, String studentName, String registrationDate, int tuition, int grades) {
        this.id = id;
        this.projectId = projectId;
        this.studentName = studentName;
        this.registrationDate = LocalDate.parse(registrationDate);
        this.tuition = tuition;
        this.grades = grades;
    }
}
