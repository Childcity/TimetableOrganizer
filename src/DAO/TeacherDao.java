package DAO;

import Data.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TeacherDao implements Dao<Teacher>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_TEACHER = "DROP TABLE IF EXISTS `teacher`;";
        final static String CREATE_TEACHER =
                "CREATE TABLE IF NOT EXISTS `teacher`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   first_name TEXT CHARSET utf8mb4 NOT NULL, " +
                "   last_name TEXT CHARSET utf8mb4 NOT NULL " +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `teacher` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `teacher`;";
        static String INSERT(String firstName, String lastName) {
            return String.format(
                    "INSERT OR IGNORE INTO `teacher` (first_name, last_name) " +
                    "VALUES ('%s', '%s');", firstName, lastName);
        }
        static String UPDATE(int id, String firstName, String lastName) {
            return String.format(
                    "UPDATE `teacher` " +
                    "SET first_name='%s', last_name='%s' " +
                    "WHERE id='%d';",  firstName, lastName, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `teacher` WHERE id = '%d';", id); }
    }

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_TEACHER);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_TEACHER);
    }

    public TeacherDao() {
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List teachers = new ArrayList<Teacher>();
        while (resultSet.next()){
            teachers.add(new Teacher(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
            ));
        }
        return (List<T>) teachers;
    }

    @Override
    public Optional<Teacher> getById(long id) {
        Teacher teacher = null;

        try {
            teacher = (Teacher) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("TeacherDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(teacher);
    }

    @Override
    public void saveAll(List<Teacher> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();

        try {
            teachers = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("TeacherDao.getAll fail: " + e.getMessage());
        }

        return teachers;
    }

    @Override
    public void save(Teacher teacher) {
        try {
            if(teacher.getId() == -1) {
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(teacher.getFirstName(), teacher.getLastName()));
            }else {
                update(teacher, new String[]{teacher.getId() + "", teacher.getFirstName(), teacher.getLastName()});
            }
        } catch (DatabaseException e) {
            System.out.println("TeacherDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Teacher teacher, String[] params) {
        try {
            teacher.setId(Integer.parseInt(params[0]));
            teacher.setFirstName(Objects.requireNonNull(params[1], "First name cannot be null"));
            teacher.setLastName(Objects.requireNonNull(params[2], "Last name cannot be null"));
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(teacher.getId(), teacher.getFirstName(), teacher.getLastName())
            );
        } catch (Exception e) {
            System.out.println("TeacherDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Teacher teacher) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(teacher.getId()));
        } catch (Exception e) {
            System.out.println("TeacherDao.delete fail: " + e.getMessage());
        }
    }
}
