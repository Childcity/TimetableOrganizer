package DAO;

import Data.Course;
import Data.Group;
import Data.Speciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GroupDao implements Dao<Group>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_GROUP = "DROP TABLE IF EXISTS `group`;";
        final static String CREATE_GROUP =
                "CREATE TABLE IF NOT EXISTS `group`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   qr_name TEXT CHARSET utf8mb4 NOT NULL UNIQUE, " +
                "   students_number INT(2) NOT NULL DEFAULT 1, " +
                "   course_id INTEGER NOT NULL, " +
                "   speciality_id INTEGER NOT NULL, " +
                "   FOREIGN KEY (course_id) REFERENCES course(id), " +
                "   FOREIGN KEY (speciality_id) REFERENCES speciality(id)" +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `group` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `group`;";
        static String INSERT(String groupName, int studentsNum, int courseId, int specialityId) {
            return String.format(
                    "INSERT OR IGNORE INTO `group` (qr_name, students_number, course_id, speciality_id) " +
                            "VALUES ('%s', '%d', '%d', '%d');", groupName, studentsNum, courseId, specialityId);
        }
        static String UPDATE(int id, String groupName, int studentsNum, int courseId, int specialityId) {
            return String.format(
                    "UPDATE `group` " +
                    "SET qr_name='%s', students_number='%d', course_id='%d', speciality_id='%d' " +
                    "WHERE id='%d';",  groupName, studentsNum, courseId, specialityId, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `group` WHERE id = '%d'", id); }
    }

    private List<Course> courses;
    private List<Speciality> specialities;

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_GROUP);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_GROUP);
    }

    public GroupDao() {
        courses = new CourseDao().getAll();
        specialities = new SpecialityDao().getAll();
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List groups = new ArrayList<Group>();
        while (resultSet.next()){
            groups.add(new Group(
                    resultSet.getInt("id"),
                    resultSet.getString("qr_name"),
                    resultSet.getInt("students_number"),
                    courses.get(resultSet.getInt("course_id") - 1),
                    specialities.get(resultSet.getInt("speciality_id") - 1)
            ));
        }
        return (List<T>) groups;
    }

    @Override
    public Optional<Group> getById(long id) {
        Group group = null;

        try {
            group = (Group) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("GroupDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(group);
    }

    @Override
    public void saveAll(List<Group> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();

        try {
            groups = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("GroupDao.getAll fail: " + e.getMessage());
        }

        return groups;
    }

    @Override
    public void save(Group group) {
        try {
            update(group, new String[]{group.getId()+"",group.getGroupName(), group.getStudentsNumber()+"",
                group.getCourse().getId()+"", group.getSpeciality().getId()+""});
            Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(group.getGroupName(), group.getStudentsNumber(),
                    group.getCourse().getId(), group.getSpeciality().getId()));
        } catch (DatabaseException e) {
            System.out.println("GroupDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Group group, String[] params) {
        try {
            group.setId(Integer.parseInt(params[0]));
            group.setGroupName(Objects.requireNonNull(params[1], "Group name cannot be null"));
            group.setStudentsNumber(Integer.parseInt(params[2]));
            group.setCourse(courses.get(Integer.parseInt(params[3]) - 1));
            group.setSpeciality(specialities.get(Integer.parseInt(params[4]) - 1));
            //System.out.println(""+group.getId()+group.getGroupName()+group.getStudentsNumber()+group.getCourseId()+group.getSpecialityId());
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(group.getId(), group.getGroupName(), group.getStudentsNumber(),
                            group.getCourse().getId(), group.getSpeciality().getId())
            );
        } catch (Exception e) {
            System.out.println("GroupDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Group group) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(group.getId()));
        } catch (Exception e) {
            System.out.println("GroupDao.delete fail: " + e.getMessage());
        }
    }
}
