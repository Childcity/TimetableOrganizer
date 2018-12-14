package DAO;

import Data.Group;
import Data.GroupTeacherDiscipline;
import Data.TeacherDiscipline;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GroupTeacherDisciplineDao implements Dao<GroupTeacherDiscipline>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_GRP_TEACH_DISC = "DROP TABLE IF EXISTS `group_teacher_discipline`;";
        final static String CREATE_GRP_TEACH_DISC =
                "CREATE TABLE IF NOT EXISTS `group_teacher_discipline`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   group_id INTEGER NOT NULL, " +
                "   teacher_discipline_id INTEGER NOT NULL, " +
                "   FOREIGN KEY (group_id) REFERENCES `group`(id) ON DELETE CASCADE, " +
                "   FOREIGN KEY (teacher_discipline_id) REFERENCES teacher_discipline(id) ON DELETE CASCADE" +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `group_teacher_discipline` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `group_teacher_discipline`;";
        static String INSERT(int groupId, int teacherDisciplineId) {
            return String.format(
                    "INSERT OR IGNORE INTO `group_teacher_discipline` (group_id, teacher_discipline_id) " +
                            "VALUES ('%d', '%d');", groupId, teacherDisciplineId);
        }
        static String UPDATE(int id, int groupId, int teacherDisciplineId) {
            return String.format(
                    "UPDATE `group_teacher_discipline` " +
                    "SET group_id='%d', teacher_discipline_id='%d' " +
                    "WHERE id='%d';", groupId, teacherDisciplineId, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `group_teacher_discipline` WHERE id = '%d'", id); }
    }

    private Dao groupDao;
    private Dao teacherDisciplineDao;

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_GRP_TEACH_DISC);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_GRP_TEACH_DISC);
    }

    public GroupTeacherDisciplineDao() {
        groupDao = new GroupDao();
        teacherDisciplineDao = new TeacherDisciplineDao();
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List groupTeacherDisciplines = new ArrayList<GroupTeacherDiscipline>();
        while (resultSet.next()){
            groupTeacherDisciplines.add(new GroupTeacherDiscipline(
                    resultSet.getInt("id"),
                    (Group) groupDao.getById(resultSet.getInt("group_id")).orElse(new Group()),
                    (TeacherDiscipline) teacherDisciplineDao.getById(resultSet.getInt("teacher_discipline_id")).orElse(new TeacherDiscipline())
            ));
        }
        return (List<T>) groupTeacherDisciplines;
    }

    @Override
    public Optional<GroupTeacherDiscipline> getById(long id) {
        GroupTeacherDiscipline group_teacher_discipline = null;

        try {
            group_teacher_discipline = (GroupTeacherDiscipline) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("GroupTeacherDisciplineDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(group_teacher_discipline);
    }

    @Override
    public void saveAll(List<GroupTeacherDiscipline> list) {
        list.forEach(this::save);
    }

    @Override
    public List<GroupTeacherDiscipline> getAll() {
        List<GroupTeacherDiscipline> groupTeacherDisciplines = new ArrayList<>();

        try {
            groupTeacherDisciplines = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("GroupTeacherDisciplineDao.getAll fail: " + e.getMessage());
        }

        return groupTeacherDisciplines;
    }

    @Override
    public void save(GroupTeacherDiscipline group_teacher_discipline) {
        try {
            if(group_teacher_discipline.getId() != -1){
                update(group_teacher_discipline, new String[]{group_teacher_discipline.getId()+"",
                        group_teacher_discipline.getGroup().getId()+"", group_teacher_discipline.getTeacherDiscipline().getId()+""});
            }else {
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(group_teacher_discipline.getGroup().getId(),
                        group_teacher_discipline.getTeacherDiscipline().getId()));
            }

        } catch (DatabaseException e) {
            System.out.println("GroupTeacherDisciplineDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(GroupTeacherDiscipline group_teacher_discipline, String[] params) {
        try {
            Group newGroup = (Group) groupDao.getById(Integer.parseInt(params[1])).orElse(new Group());
            TeacherDiscipline newTeacherDiscipline = (TeacherDiscipline) teacherDisciplineDao.getById(Integer.parseInt(params[2])).orElse(new TeacherDiscipline());

            group_teacher_discipline.setId(Integer.parseInt(params[0]));
            group_teacher_discipline.setGroup(newGroup);
            group_teacher_discipline.setTeacherDiscipline(newTeacherDiscipline);

            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(group_teacher_discipline.getId(), group_teacher_discipline.getGroup().getId(),
                            group_teacher_discipline.getTeacherDiscipline().getId())
            );
        } catch (Exception e) {
            System.out.println("GroupTeacherDisciplineDao.update fail: " + e.getMessage());
            //e.printStackTrace();

        }
    }

    @Override
    public void delete(GroupTeacherDiscipline group_teacher_discipline) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(group_teacher_discipline.getId()));
        } catch (Exception e) {
            System.out.println("GroupTeacherDisciplineDao.delete fail: " + e.getMessage());
        }
    }
}
