package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CourseDao implements Dao<Course> {
    private static final class SqlQuery{
        final static String DROP_COURSE = "DROP TABLE IF EXISTS course;";
        final static String CREATE_COURSE =
                    "CREATE TABLE IF NOT EXISTS course( " +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "   course_name TEXT CHARSET utf8mb4 NOT NULL" +
                    ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM course WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM course;";
        static String INSERT(String courseName) {
            return String.format(
                    "INSERT OR IGNORE INTO course (course_name ) " +
                    "VALUES ('%s');", courseName);
        }
        static String UPDATE(int id, String courseName) {
            return String.format(
                    "UPDATE course " +
                    "SET course_name = '%s' " +
                    "WHERE id = '%d';", courseName, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM course WHERE id = '%d';", id); }
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_COURSE);
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_COURSE);
    }

    public CourseDao() {
    }

    @Override
    public Optional<Course> getById(long id) {
        Course course = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet;

        try {
            conn = Database.getInstance().OpenConnection();
            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(SqlQuery.SELECT_BY_ID(id));

            if (resultSet.next()){
                course = new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("course_name")
                );
            }

            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        } catch (Exception e) {
            System.out.println("CourseDao.getById fail: " + e.getMessage());
        } finally {
            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        }

        return Optional.ofNullable(course);
    }

    @Override
    public void saveAll(List<Course> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Course> getAll() {
        List<Course> courses = new ArrayList<Course>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet;

        try {
            conn = Database.getInstance().OpenConnection();
            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(SqlQuery.SELECT_ALL);

            while (resultSet.next()){
                courses.add(new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("course_name")
                ));
            }

            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        } catch (Exception e) {
            System.out.println("CourseDao.getAll fail: " + e.getMessage());
        } finally {
            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        }

        return courses;
    }

    @Override
    public void save(Course course) {
        try {
            update(course, new String[]{course.getId()+"", course.getCourseName()});
            Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(course.getCourseName()));
        } catch (DatabaseException e) {
            System.out.println("CourseDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Course course, String[] params) {
        try {
            course.setId(Integer.parseInt(params[0]));
            course.setCourseName(Objects.requireNonNull(params[1], "Course name cannot be null"));
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(course.getId(), course.getCourseName()));
        } catch (Exception e) {
            System.out.println("CourseDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Course course) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(course.getId()));
        } catch (Exception e) {
            System.out.println("CourseDao.delete fail: " + e.getMessage());
        }
    }
}
