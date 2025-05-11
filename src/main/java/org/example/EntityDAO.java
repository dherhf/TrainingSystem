package org.example;

import org.example.entity.Project;
import org.example.entity.Student;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class EntityDAO {

    private static final Logger log = LoggerFactory.getLogger(EntityDAO.class);

    public List<Project> getAllProject() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        } catch (Exception e) {
            log.error("", e);
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

    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            log.error("获取学生列表时出错", e);
        }
        return null;
    }

    public List<Student> getStudentsByPage(int projectId, int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student WHERE projectId = :projectId", Student.class)
                    .setParameter("projectId", projectId)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        } catch (Exception e) {
            log.error("分页查询学生列表时出错", e);
        }
        return null;
    }

    public List<Student> getStudentsByProjectId(int projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student WHERE projectId = :projectId", Student.class)
                    .setParameter("projectId", projectId)
                    .list();
        } catch (Exception e) {
            log.error("根据项目ID获取学生列表时出错", e);
        }
        return null;
    }

    public int getTotalStudentsByProjectId(int projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(*) FROM Student WHERE projectId = :projectId", Long.class)
                    .setParameter("projectId", projectId)
                    .uniqueResult()
                    .intValue();
        } catch (Exception e) {
            log.error("根据项目ID获取学生总数时出错", e);
        }
        return 0;
    }

    public void addStudentsToProjects() {
        List<Project> projects = getAllProject();
        if (projects != null) {
            for (Project project : projects) {
                try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                    session.beginTransaction();
                    for (int i = 1; i <= 200; i++) {
                        Student student = new Student();
                        student.setId("B" + String.format("%04d", project.getId()) + String.format("%03d", i));
                        student.setStudentName("Student " + i);
                        student.setProjectId(project.getId());
                        student.setRegistrationDate(LocalDate.now()); // 初始化 registrationDate 字段
                        student.setTuition(0); // 初始化 tuition 字段
                        student.setGrades(0); // 初始化 grades 字段
                        session.persist(student);
                    }
                    session.getTransaction().commit();
                } catch (Exception e) {
                    log.error("为项目添加学生时出错", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        EntityDAO dao = new EntityDAO();
        dao.addStudentsToProjects();
    }



}

