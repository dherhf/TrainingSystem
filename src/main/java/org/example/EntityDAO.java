package org.example;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Project;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class EntityDAO {

    private static final Logger log = LoggerFactory.getLogger(EntityDAO.class);

    public boolean addUser(String username, String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // 创建用户实体
            User newUser = new User(username, email, password);
            // 保存用户
            session.persist(newUser);

            session.getTransaction().commit();

            log.info("用户 {} 注册成功", username);
            return true;
        } catch (Exception e) {
            log.error("用户 {} 注册失败: {}", username, e.getMessage(), e);
        }
        return false;

    }

    public boolean auth(String username, String password) {
        User user = fetchUser(username);
        if (user.checkPassword(password)) {
            log.info("用户成功登录");
            return true;
        } else {
            log.warn("用户密码或账号错误");
            return false;
        }

    }

    public User fetchUser(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            // where name = username
            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("name"), username));

            return session.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            log.error("查询用户失败: {}", username, e);
            return new User();
        }
    }

    public boolean isUsernameExists(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(criteriaBuilder.count(root))
                    .where(criteriaBuilder.equal(root.get("name"), username));

            Long count = session.createQuery(criteriaQuery).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("用户名 {} 已存在", username, e);
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM User WHERE email = :email", Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("邮箱 {} 已存在", email, e);
            return false;
        }
    }

    public List<Project> getAllProject() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        } catch (Exception e) {
            log.error("", e);
        }
        return Collections.emptyList();
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

    public String getProjectNameById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Project.class, id).getProjectName();
        } catch (Exception e) {
            log.error("获取项目名称时出错", e);
            return null;
        }

    }

    public int getProjectIdByName(String projectName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT id FROM Project WHERE projectName = :projectName", Integer.class)
                    .setParameter("projectName", projectName)
                    .uniqueResult();
        } catch (Exception e) {
            log.error("获取项目ID时出错", e);
            return 0;
        }

    }

    public void addStudent(String id, int projectId, String studentName, LocalDate registrationDate, int tuition, int grades) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student student = new Student(id, projectId, studentName, registrationDate, tuition, grades);
            session.persist(student);
            session.getTransaction().commit();
        }
    }

    public void deleteStudent(String id, int projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Student student = session.createQuery(
                            "FROM Student WHERE id = :id AND projectId = :projectId", Student.class)
                    .setParameter("id", id)
                    .setParameter("projectId", projectId)
                    .uniqueResult();
            if (student != null) {
                // 如果学生记录存在，则删除该记录
                session.remove(student);
            }
            // 提交事务
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("删除学生时出错", e);
        }

    }
    public List<Student> getAllStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            log.error("获取学生列表时出错", e);
        }
        return Collections.emptyList();
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
        return Collections.emptyList();
    }

    public List<Student> getStudentsByProjectId(int projectId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student WHERE projectId = :projectId", Student.class)
                    .setParameter("projectId", projectId)
                    .list();
        } catch (Exception e) {
            log.error("根据项目ID获取学生列表时出错", e);
        }
        return Collections.emptyList();
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



}

