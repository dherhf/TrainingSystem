package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "project")
@Data
public class Project {
    @Id
    private int id;
    @Column(name = "project_name", nullable = false)
    private String projectName;

    public Project(){

    }
    public Project(int id, String projectName){
        this.id = id;
        this.projectName = projectName;
    }
}
