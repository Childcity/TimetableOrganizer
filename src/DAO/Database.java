package DAO;

import java.sql.*;
import java.util.Objects;

class SqlQuery {
    public final String DROP_TIMETABLE = "DROP TABLE IF EXISTS timetable;";
    public final String DROP_TEACHER_DISCIPLINE = "DROP TABLE IF EXISTS teacher_discipline;";
    public final String DROP_GROUP_DISCIPLINE_TEACHER = "DROP TABLE IF EXISTS group_discipline_teacher;";
    public final String DROP_GROUPS = "DROP TABLE IF EXISTS groups;";
    public final String DROP_COURSES = "DROP TABLE IF EXISTS courses;";
    public final String DROP_TEACHER = "DROP TABLE IF EXISTS teachers;";
    public final String DROP_DISCIPLINES = "DROP TABLE IF EXISTS disciplines;";
    public final String DROP_DAYS = "DROP TABLE IF EXISTS days;";
    public final String DROP_LESSONS = "DROP TABLE IF EXISTS lessons;";
    public final String DROP_AUDIT = "DROP TABLE IF EXISTS audit;";
}

public class Database {
    private static String dbPath = null;

    private Database() {}
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    public static String getDbPath() {return dbPath;}
    public static void setDbPath(String dbPath) {Database.dbPath = dbPath;}

    public Connection OpenConnection() throws DatabaseException {
        Connection conn = null;

        try {
            String connectionUrl = "jdbc:sqlite:" + Objects.requireNonNull(dbPath.replace("\\","/"), "db path is null");
            //System.out.println("Connection Url: " + connectionUrl);
            Class.forName("org.sqlite.JDBC"); // setup an sqlite driver, which we use
            conn = DriverManager.getConnection(connectionUrl);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new DatabaseException("Fail to open connection: " + e.getMessage());
        }

        return conn;
    }

    public void CloseConnection(Connection conn){
        try {
            if(conn != null)
                conn.close();
        } catch (SQLException e) {
            System.out.println("Fail to close connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void CloseStatement(Statement stmt) {
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Fail to close statement: " + e.getMessage());
            }
        }
    }

    static Dao<Speciality> dao = new SpecialityDao();
    public void Create() throws DatabaseException {
        try {
            SpecialityDao.Create();
            System.out.println("Created table 'speciality'");

            dao.save(new Speciality("Math", 111));
            dao.save(new Speciality("Physics", 112));
            dao.save(new Speciality("Ukrmova", 117));
            //dao.getAll().forEach(speciality -> System.out.println(speciality.getNumericName()+speciality.getSpecName()));
        } catch (Exception e){
            System.out.println(e.getMessage());
            //e.printStackTrace();
            throw new DatabaseException("Fail to create database");
        }
    }

    public void ExecuteWriteQuery(String sqlQuery) throws DatabaseException {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = OpenConnection();
            stmt = conn.createStatement();
            stmt.execute(sqlQuery);
            CloseStatement(stmt);
            CloseConnection(conn);
        }catch (Exception e){
            System.out.println(e.getMessage());
            //e.printStackTrace();
            throw new DatabaseException("Fail to execute query: " + e.getMessage());
        } finally {
            CloseStatement(stmt);
            CloseConnection(conn);
        }
    }


}
