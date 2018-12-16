package DAO;

import Data.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TimetableDao implements Dao<Timetable>{
    private static final class SqlQuery{
        final static String DROP_TIMETABLE = "DROP TABLE IF EXISTS `timetable`;";
        final static String CREATE_TIMETABLE =
                "CREATE TABLE IF NOT EXISTS `timetable`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   week INTEGER NOT NULL DEFAULT 1, " +
                "   day INTEGER NOT NULL DEFAULT 1, " +
                "   lesson INTEGER NOT NULL DEFAULT 1, " +
                "   auditorium_id INTEGER NOT NULL, " +
                "   group_teacher_discipline_id INTEGER NOT NULL, " +
                "   FOREIGN KEY (auditorium_id) REFERENCES auditorium(id) ON DELETE CASCADE, " +
                "   FOREIGN KEY (group_teacher_discipline_id) REFERENCES group_teacher_discipline(id) ON DELETE CASCADE" +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `timetable` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `timetable`;";
        static String INSERT(int week, int day, int lesson, int auditoryId, int groupTeacherDisciplineId) {
            return String.format(
                    "INSERT OR IGNORE INTO `timetable` (week, day, lesson, auditorium_id, group_teacher_discipline_id) " +
                            "VALUES ('%d', '%d', '%d', '%d', '%d');", week, day, lesson, auditoryId, groupTeacherDisciplineId);
        }
        static String UPDATE(int id, int week, int day, int lesson, int auditoryId, int groupTeacherDisciplineId) {
            return String.format(
                    "UPDATE `timetable` " +
                    "SET week='%d', day='%d', lesson='%d', auditorium_id='%d', group_teacher_discipline_id='%d' " +
                    "WHERE id='%d';", week, day, lesson, auditoryId, groupTeacherDisciplineId, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `timetable` WHERE id = '%d'", id); }

        static String SELECT_GROUP_LESSON_IN_DAY_WEEK(int week, int day, int groupId){
            return String.format(
                    "SELECT tmtbl.id, tmtbl.lesson, tmtbl.auditorium_id, gtd.id as group_teacher_discipline_id\n" +
                    "FROM timetable tmtbl\n" +
                    "  INNER JOIN group_teacher_discipline gtd ON tmtbl.group_teacher_discipline_id = gtd.id\n" +
                    "  WHERE tmtbl.week = '%d' AND tmtbl.day = '%d' AND gtd.group_id = '%d';", week, day, groupId
            );
        }
    }

    private Dao auditoriumDao;
    private Dao groupTeacherDisciplineDao;
    private SelectAllParser selectAllParser;

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_TIMETABLE);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_TIMETABLE);
    }

    public TimetableDao() {
        auditoriumDao = new AuditoriumDao();
        groupTeacherDisciplineDao = new GroupTeacherDisciplineDao();
        selectAllParser = new SelectAllParser(auditoriumDao, groupTeacherDisciplineDao);
    }

    @Override
    public Optional<Timetable> getById(long id) {
        Timetable timetable = null;

        try {
            timetable = (Timetable) Database.getInstance().ExecuteReadQuery(selectAllParser, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("TimetableDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(timetable);
    }

    @Override
    public void saveAll(List<Timetable> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Timetable> getAll() {
        List<Timetable> timetables = new ArrayList<>();

        try {
            timetables = Database.getInstance().ExecuteReadQuery(selectAllParser, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("TimetableDao.getAll fail: " + e.getMessage());
        }

        return timetables;
    }

    @Override
    public void save(Timetable timetable) {
        try {
            if(timetable.getId() != -1){
                update(timetable, new String[]{timetable.getId()+"", timetable.getWeek()+"", timetable.getDay()+"", timetable.getLesson()+"",
                        timetable.getAuditorium().getId()+"", timetable.getGroupTeacherDiscipline().getId()+""});
            }else{
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(timetable.getWeek(), timetable.getDay(), timetable.getLesson(),
                        timetable.getAuditorium().getId(), timetable.getGroupTeacherDiscipline().getId()));
            }
        } catch (DatabaseException e) {
            System.out.println("TimetableDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Timetable timetable, String[] params) {
        try {
            Auditorium newAuditorium = (Auditorium) auditoriumDao.getById(Integer.parseInt(params[4])).orElse(new Auditorium());
            GroupTeacherDiscipline newGroupTeacherDiscipline = (GroupTeacherDiscipline) groupTeacherDisciplineDao.getById(Integer.parseInt(params[5])).orElse(new GroupTeacherDiscipline());

            timetable.setId(Integer.parseInt(params[0]));
            timetable.setWeek(Integer.parseInt(params[1]));
            timetable.setDay(Integer.parseInt(params[2]));
            timetable.setLesson(Integer.parseInt(params[3]));
            timetable.setAuditorium(newAuditorium);
            timetable.setGroupTeacherDiscipline(newGroupTeacherDiscipline);

            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(timetable.getId(), timetable.getWeek(), timetable.getDay(), timetable.getLesson(),
                            timetable.getAuditorium().getId(), timetable.getGroupTeacherDiscipline().getId())
            );
        } catch (Exception e) {
            System.out.println("TimetableDao.update fail: " + e.getMessage());
            //e.printStackTrace();

        }
    }

    @Override
    public void delete(Timetable timetable) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(timetable.getId()));
        } catch (Exception e) {
            System.out.println("TimetableDao.delete fail: " + e.getMessage());
        }
    }

    public List<Day> getAllDays(int week, Group group){
        List<Day> dayList = new ArrayList<>();
        SelectLessonsInDay selectLessonsInDay = new SelectLessonsInDay();
        Database db = Database.getInstance();

        for(int day = 0; day < 6; day++){
            try {
                List<Lesson> lessons = new ArrayList<>();
                List<Lesson> bookedLessons = db.ExecuteReadQuery(selectLessonsInDay, SqlQuery.SELECT_GROUP_LESSON_IN_DAY_WEEK(week, day, group.getId()));

                //feel lessons in day
                for (int lessonNum = 0; lessonNum < 5; lessonNum++){
                    Lesson lesson = new Lesson(lessonNum);
                    for (Lesson bookedLesson : bookedLessons) {
                        if (bookedLesson.getLessonNumber() == lessonNum) {
                            lesson = bookedLesson;
                        }
                    }
                    lessons.add(lesson);
                    System.out.println("lesNum: " + lesson.getLessonNumber()
                            +"\nlesAudNumber" + lesson.getAuditorium().getAuditNumber()
                            +"\nlesGr" + lesson.getGroupTeacherDiscipline().getGroup().getGroupName()
                    );
                }
                dayList.add(new Day(day, week, lessons));
            } catch (Exception e) {
                System.out.println("TimetableDao.getAllDays fail on day " + day + " :" + e.getMessage());
            }
        }

        return dayList;
    }

    class SelectLessonsInDay implements Database.QueryParser {
        @Override
        public <T> List<T> parse(ResultSet resultSet) throws SQLException {
            List<Lesson> lessonsList = new ArrayList<>();
            while (resultSet.next()){
                lessonsList.add(new Lesson(
                        resultSet.getInt("id"),
                        resultSet.getInt("lesson"),
                        (Auditorium) auditoriumDao.getById(resultSet.getInt("auditorium_id")).orElse(new Auditorium()),
                        (GroupTeacherDiscipline) groupTeacherDisciplineDao.getById(resultSet.getInt("group_teacher_discipline_id")).orElse(new GroupTeacherDiscipline())
                ));
            }
            return (List<T>) lessonsList;
        }
    }

    class SelectAllParser implements Database.QueryParser {
        Dao auditoriumDao, groupTeacherDisciplineDao;

        SelectAllParser(Dao auditoriumDao_, Dao groupTeacherDisciplineDao_){
            auditoriumDao = auditoriumDao_;
            groupTeacherDisciplineDao = groupTeacherDisciplineDao_;
        }

        @Override
        public <T> List<T> parse(ResultSet resultSet) throws SQLException {
            List timetableList = new ArrayList<Timetable>();
            while (resultSet.next()){
                timetableList.add(new Timetable(
                        resultSet.getInt("id"),
                        resultSet.getInt("week"),
                        resultSet.getInt("day"),
                        resultSet.getInt("lesson"),
                        (Auditorium) auditoriumDao.getById(resultSet.getInt("auditorium_id")).orElse(new Auditorium()),
                        (GroupTeacherDiscipline) groupTeacherDisciplineDao.getById(resultSet.getInt("group_teacher_discipline_id")).orElse(new GroupTeacherDiscipline())
                ));
            }
            return (List<T>) timetableList;
        }
    }
}
