package org.example;

import org.example.entity.Project;
import org.example.entity.Student;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntityDAO {

    private static final Logger log = LoggerFactory.getLogger(EntityDAO.class);

    public List<Project> getAllProject() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }

    public void addProject(String projectName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Project project = new Project();
            project.setProjectName(projectName);
            session.persist(project);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("添加项目时出错", e);
        }
    }

    public void updateProjectName(int id, String projectName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Project project = session.get(Project.class, id);
            if (project != null) {
                project.setProjectName(projectName);
                session.merge(project);
                session.getTransaction().commit();
            } else {
                log.error("未找到id为 {} 的项目", id);
            }
        } catch (Exception e) {
            log.error("更新项目名称时出错", e);
        }
    }

    public List<Student> getAllStudent() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            log.error("获取学生列表时出错", e);
        }
        return null;
    }

    public List<Student> getStudentByProjectId(int projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student WHERE projectId = :programId", Student.class)
                    .setParameter("projectId", projectId)
                    .list();
        } catch (Exception e) {
            log.error("根据项目ID获取学生列表时出错", e);
        }
        return null;
    }
}

