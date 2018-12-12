package DAO;

import Data.Discipline;
import Data.DisciplineType;
import Data.Speciality;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DisciplineDao implements Dao<Discipline>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_DISCIPLINE = "DROP TABLE IF EXISTS `discipline`;";
        final static String CREATE_DISCIPLINE =
                "CREATE TABLE IF NOT EXISTS `discipline`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   dis_name TEXT CHARSET utf8mb4 NOT NULL, " +
                "   hours_total INT(2) NOT NULL, " +
                "   dis_type TEXT CHARSET utf8mb4 NOT NULL, " +
                "   speciality_id INTEGER NOT NULL, " +
                "   FOREIGN KEY (speciality_id) REFERENCES speciality(id)" +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `discipline` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `discipline`;";
        static String INSERT(String disName, int hoursTotal, String disType, int specialityId) {
            return String.format(
                    "INSERT OR IGNORE INTO `discipline` (dis_name, hours_total, dis_type, speciality_id) " +
                    "VALUES ('%s', '%d', '%s', '%d');", disName, hoursTotal, disType, specialityId);
        }
        static String UPDATE(int id, String disName, int hoursTotal, String disType, int specialityId) {
            return String.format(
                    "UPDATE `discipline` " +
                    "SET dis_name='%s', hours_total='%d', dis_type='%s', speciality_id='%d'" +
                    "WHERE id='%d';", disName, hoursTotal, disType, specialityId, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `discipline` WHERE id = '%d'", id); }
    }

    private List<Speciality> specialities;

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_DISCIPLINE);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_DISCIPLINE);
    }

    public DisciplineDao() {
        specialities = new SpecialityDao().getAll();
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List disciplines = new ArrayList<Discipline>();
        while (resultSet.next()){
            disciplines.add(new Discipline(
                    resultSet.getInt("id"),
                    resultSet.getString("dis_name"),
                    resultSet.getInt("hours_total"),
                    new DisciplineType(resultSet.getString("dis_type")),
                    specialities.get(resultSet.getInt("speciality_id") - 1)
            ));
        }
        return (List<T>) disciplines;
    }

    @Override
    public Optional<Discipline> getById(long id) {
        Discipline discipline = null;

        try {
            discipline = (Discipline) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("DisciplineDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(discipline);
    }

    @Override
    public void saveAll(List<Discipline> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Discipline> getAll() {
        List<Discipline> disciplines = new ArrayList<>();

        try {
            disciplines = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("DisciplineDao.getAll fail: " + e.getMessage());
        }

        return disciplines;
    }

    @Override
    public void save(Discipline discipline) {
        try {
            if(discipline.getId() == -1){
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(discipline.getDisName(), discipline.getHoursTotal(),
                        discipline.getDisType().getType(), discipline.getSpeciality().getId()));
            }else {
                update(discipline, new String[]{discipline.getId() + "", discipline.getDisName(), discipline.getHoursTotal() + "",
                        discipline.getDisType().getType(), discipline.getSpeciality().getId() + ""});
            }
        } catch (DatabaseException e) {
            System.out.println("DisciplineDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Discipline discipline, String[] params) {
        try {
            discipline.setId(Integer.parseInt(params[0]));
            discipline.setDisName(Objects.requireNonNull(params[1], "Discipline name cannot be null"));
            discipline.setHoursTotal(Integer.parseInt(params[2]));
            discipline.setDisType(new DisciplineType(Objects.requireNonNull(params[3], "Discipline type cannot be null")));
            discipline.setSpeciality(specialities.get(Integer.parseInt(params[4]) - 1));
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(discipline.getId(), discipline.getDisName(), discipline.getHoursTotal(),
                            discipline.getDisType().getType(), discipline.getSpeciality().getId())
            );
        } catch (Exception e) {
            System.out.println("DisciplineDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Discipline discipline) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(discipline.getId()));
        } catch (Exception e) {
            System.out.println("DisciplineDao.delete fail: " + e.getMessage());
        }
    }
}
