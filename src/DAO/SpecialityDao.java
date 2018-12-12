package DAO;

import Data.Speciality;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SpecialityDao implements Dao<Speciality> {
    private static final class SqlQuery{
        final static String DROP_SPECIALITY = "DROP TABLE IF EXISTS speciality;";
        final static String CREATE_SPECIALITY =
                    "CREATE TABLE IF NOT EXISTS speciality( " +
                    "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "   spec_name TEXT CHARSET utf8mb4 NOT NULL, " +
                    "   numeric_name INT(2) NOT NULL UNIQUE " +
                    ");";
        static String SELECT_BY_ID(long id) { return String.format("SELECT * FROM speciality WHERE id = '%d'", id); }
        final static String SELECT_ALL = "SELECT * FROM speciality;";
        static String INSERT(int numericName, String specName) {
            return String.format(
                    "INSERT OR IGNORE INTO speciality (numeric_name, spec_name ) " +
                    "VALUES ('%d', '%s');", numericName, specName);
        }
        static String UPDATE(int id, int numericName, String specName) {
            return String.format(
                    "UPDATE speciality " +
                    "SET numeric_name='%d', spec_name='%s' " +
                    "WHERE id='%d';", numericName, specName, id);
        }
        static String DELETE(long id){ return String.format("DELETE FROM speciality WHERE id = '%d'", id); }
    }

    public  static void Drop() throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.DROP_SPECIALITY);
    }

    public static void Create () throws DatabaseException {
        Database.getInstance().ExecuteWriteQuery(SqlQuery.CREATE_SPECIALITY);
    }

    public SpecialityDao() {
    }

    @Override
    public Optional<Speciality> getById(long id) {
        Speciality speciality = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet;

        try {
            conn = Database.getInstance().OpenConnection();
            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(SqlQuery.SELECT_BY_ID(id));

            if (resultSet.next()){
                speciality = new Speciality(
                        resultSet.getInt("id"),
                        resultSet.getString("spec_name"),
                        resultSet.getInt("numeric_name")
                );
            }

            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        } catch (Exception e) {
            System.out.println("SpecialityDao.getById fail: " + e.getMessage());
        } finally {
            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        }

        return Optional.ofNullable(speciality);
    }

    @Override
    public void saveAll(List<Speciality> list) {
        list.forEach(this::save);
    }

    @Override
    public List<Speciality> getAll() {
        List<Speciality> specialities = new ArrayList<Speciality>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet;

        try {
            conn = Database.getInstance().OpenConnection();
            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(SqlQuery.SELECT_ALL);

            while (resultSet.next()){
                specialities.add(new Speciality(
                        resultSet.getInt("id"),
                        resultSet.getString("spec_name"),
                        resultSet.getInt("numeric_name")
                ));
            }

            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        } catch (Exception e) {
            System.out.println("SpecialityDao.getAll fail: " + e.getMessage());
        } finally {
            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        }

        return specialities;
    }

    @Override
    public void save(Speciality speciality) {
        try {
            update(speciality, new String[]{speciality.getId()+"",speciality.getNumericName()+"", speciality.getSpecName()});
            Database.getInstance().ExecuteWriteQuery(SqlQuery.INSERT(speciality.getNumericName(), speciality.getSpecName()));
        } catch (DatabaseException e) {
            System.out.println("SpecialityDao.save fail: " + e.getMessage());
        }
    }

    @Override
    public void update(Speciality speciality, String[] params) {
        try {
            speciality.setId(Integer.parseInt(params[0]));
            speciality.setNumericName(Integer.parseInt(params[1]));
            speciality.setSpecName(Objects.requireNonNull(params[2], "Speciality name cannot be null"));
            Database.getInstance().ExecuteWriteQuery(
                    SqlQuery.UPDATE(speciality.getId(), speciality.getNumericName(), speciality.getSpecName()));
        } catch (Exception e) {
            System.out.println("SpecialityDao.update fail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Speciality speciality) {
        try {
            Database.getInstance().ExecuteWriteQuery(SqlQuery.DELETE(speciality.getId()));
        } catch (Exception e) {
            System.out.println("SpecialityDao.delete fail: " + e.getMessage());
        }
    }
}
