package DAO;

import Data.Auditorium;
import Data.AuditoriumType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AuditoriumDao implements Dao<Auditorium>, Database.QueryParser {
    private static final class SqlQuery{
        final static String DROP_AUDITORIUM = "DROP TABLE IF EXISTS `auditorium`;";
        final static String CREATE_AUDITORIUM =
                "CREATE TABLE IF NOT EXISTS `auditorium`( " +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "   audit_number INTEGER NOT NULL UNIQUE, " +
                "   capacity INTEGER NOT NULL, " +
                "   audit_type TEXT CHARSET utf8mb4 NOT NULL " +
                ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM `auditorium` WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM `auditorium`;";
        static String INSERT(int auditNumber, int capacity, String auditType) {
            return String.format(
                    "INSERT OR IGNORE INTO `auditorium` (audit_number, capacity, audit_type) " +
                    "VALUES ('%d', '%d', '%s');", auditNumber, capacity, auditType);
        }
        static String UPDATE(int id, int auditNumber, int capacity, String auditType) {
            return String.format(
                    "UPDATE `auditorium` " +
                    "SET audit_number='%d', capacity='%d', audit_type='%s' " +
                    "WHERE id='%d';",  auditNumber, capacity, auditType, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM `auditorium` WHERE id = '%d';", id); }
    }

    public static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_AUDITORIUM);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_AUDITORIUM);
    }

    public AuditoriumDao() {
    }

    @Override
    public <T> List<T> parse(ResultSet resultSet) throws SQLException {
        List auditoriums = new ArrayList<Auditorium>();
        while (resultSet.next()){
            auditoriums.add(new Auditorium(
                    resultSet.getInt("id"),
                    resultSet.getInt("audit_number"),
                    resultSet.getInt("capacity"),
                    new AuditoriumType(resultSet.getString("audit_type"))
            ));
        }
        return (List<T>) auditoriums;
    }

    @Override
    public Optional<Auditorium> getById(long id) {
        Auditorium auditorium = null;

        try {
            auditorium = (Auditorium) Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_BY_ID(id)).get(0);
        } catch (Exception e) {
            System.out.println("AuditoriumDao.getById fail: " + e.getMessage());
        }

        return Optional.ofNullable(auditorium);
    }

    @Override
    public void saveAll(List<Auditorium> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Auditorium> getAll() {
        List<Auditorium> auditoriums = new ArrayList<>();

        try {
            auditoriums = Database.getInstance().ExecuteReadQuery(this, SqlQuery.SELECT_ALL);
        } catch (Exception e) {
            System.out.println("AuditoriumDao.getAll fail: " + e.getMessage());
        }

        return auditoriums;
    }

    @Override
    public void save(Auditorium auditorium) {
        try {
            if(auditorium.getId() == -1) {
                Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(auditorium.getAuditNumber(), auditorium.getCapacity(), auditorium.getAuditType().getType()));
            }else {
                update(auditorium, new String[]{auditorium.getId() + "", auditorium.getAuditNumber() + "",
                        auditorium.getCapacity() + "", auditorium.getAuditType().getType()});
            }
        } catch (DatabaseException e) {
            System.out.println("AuditoriumDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Auditorium auditorium, String[] params) {
        try {
            auditorium.setId(Integer.parseInt(params[0]));
            auditorium.setAuditNumber(Integer.parseInt(params[1]));
            auditorium.setCapacity(Integer.parseInt(params[2]));
            auditorium.setAuditType(new AuditoriumType(Objects.requireNonNull(params[3], "Auditorium type cannot be null")));
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(auditorium.getId(), auditorium.getAuditNumber(), auditorium.getCapacity(), auditorium.getAuditType().getType())
            );
        } catch (Exception e) {
            System.out.println("AuditoriumDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Auditorium auditorium) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(auditorium.getId()));
        } catch (Exception e) {
            System.out.println("AuditoriumDao.delete fail: " + e.getMessage());
        }
    }
}
