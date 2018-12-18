package DAO;

import Data.Group;
import Data.Teacher;
import Data.TeacherDiscipline;
import Data.Discipline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TeacherDisciplineDao implements Dao<TeacherDiscipline>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_TEACHER_DISCIPLINE = "DROP TABLE IF EXISTS `teacher_discipline`;";
        final static String CREATE_TEACHER_DISCIPLINE  =
                "CREATE TABLE IF NOT EXISTS `teacher_discipline`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   teacher_id INTEGER NOT NULL, " +
                "   discipline_id INTEGER NOT NULL, " +
                "   FOREIGN KEY (teacher_id) REFERENCES `teacher`(id) ON DELETE CASCADE, " +
                "   FOREIGN KEY (discipline_id) REFERENCES `discipline`(id) ON DELETE CASCADE" +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `teacher_discipline` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `teacher_discipline`;";
        static String INSERT(int teacherId, int disciplineId) {
            return String.format(
                    "INSERT OR IGNORE INTO `teacher_discipline` (teacher_id, discipline_id) " +
                            "VALUES ('%d', '%d');", teacherId, disciplineId);
        }
        static String UPDATE(int id, int teacherId, int disciplineId) {
            return String.format(
                    "UPDATE `teacher_discipline` " +
                    "SET teacher_id='%d', discipline_id='%d' " +
                    "WHERE id='%d';", teacherId, disciplineId, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `teacher_discipline` WHERE id = '%d'", id); }

        static String SELECT_FOR_GROUP(long group_id) {
            return String.format(
                    "SELECT td.id, teacher_id, discipline_id\n" +
                    "FROM teacher_discipline td\n" +
                    "  INNER JOIN discipline d on td.discipline_id = d.id\n" +
                    "WHERE speciality_id = (SELECT \"group\".speciality_id FROM main.\"group\" WHERE \"group\".id = '%d');", group_id);
        }
    }

    private Dao teacherDao;
    private Dao disciplineDao;

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_TEACHER_DISCIPLINE);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_TEACHER_DISCIPLINE);
    }

    public TeacherDisciplineDao() {
        teacherDao = new TeacherDao();
        disciplineDao = new DisciplineDao();
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List teachersDisciplines = new ArrayList<TeacherDiscipline>();
        while (resultSet.next()){
            teachersDisciplines.add(new TeacherDiscipline(
                    resultSet.getInt("id"),
                    (Teacher) teacherDao.getById(resultSet.getInt("teacher_id")).orElse(new Teacher()),
                    (Discipline) disciplineDao.getById(resultSet.getInt("discipline_id")).orElse(new Discipline())
            ));
        }
        return (List<T>) teachersDisciplines;
    }

    @Override
    public Optional<TeacherDiscipline> getById(long id) {
        TeacherDiscipline teacherDiscipline = null;

        try {
            teacherDiscipline = (TeacherDiscipline) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("TeacherDisciplineDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(teacherDiscipline);
    }

    @Override
    public void saveAll(List<TeacherDiscipline> list) {
        list.forEach(this::save);
    }

    @Override
    public List<TeacherDiscipline> getAll() {
        List<TeacherDiscipline> teachersDisciplines = new ArrayList<>();

        try {
            teachersDisciplines = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("TeacherDisciplineDao.getAll fail: " + e.getMessage());
        }

        return teachersDisciplines;
    }

    @Override
    public void save(TeacherDiscipline teacherDiscipline) {
        try {
            if(teacherDiscipline.getId() != -1){
                update(teacherDiscipline,
                        new String[]{teacherDiscipline.getId()+"",teacherDiscipline.getTeacher().getId()+"", teacherDiscipline.getDiscipline().getId()+""});
            }else{
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(teacherDiscipline.getTeacher().getId(),
                        teacherDiscipline.getDiscipline().getId()));
            }

        } catch (DatabaseException e) {
            System.out.println("TeacherDisciplineDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(TeacherDiscipline teacherDiscipline, String[] params) {
        try {
            Teacher newTeacher = (Teacher) teacherDao.getById(Integer.parseInt(params[1])).orElse(new Teacher());
            Discipline newDiscipline = (Discipline) disciplineDao.getById(Integer.parseInt(params[2])).orElse(new Discipline());

            teacherDiscipline.setId(Integer.parseInt(params[0]));
            teacherDiscipline.setTeacher(newTeacher);
            teacherDiscipline.setDiscipline(newDiscipline);

            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(teacherDiscipline.getId(), teacherDiscipline.getTeacher().getId(), teacherDiscipline.getDiscipline().getId())
            );
        } catch (Exception e) {
            System.out.println("TeacherDisciplineDao.update fail: " + e.getMessage());
            //e.printStackTrace();
        }
    }

    @Override
    public void delete(TeacherDiscipline teacherDiscipline) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(teacherDiscipline.getId()));
        } catch (Exception e) {
            System.out.println("TeacherDisciplineDao.delete fail: " + e.getMessage());
        }
    }


    public List<TeacherDiscipline> getAllForGroup(Group group) {
        List<TeacherDiscipline> teacherDisciplines = new ArrayList<>();

        try {
            teacherDisciplines = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_FOR_GROUP(group.getId()));
        } catch (Exception e) {
            System.out.println("TeacherDisciplineDao.getAllForGroup fail: " + e.getMessage());
        }

        return teacherDisciplines;
    }
}
