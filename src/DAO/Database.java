package DAO;

import Data.Course;
import Data.Group;
import Data.Speciality;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {
    private static String dbPath = null;

    private Database() {}
    private static Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    public static String getDbPath() {return dbPath;}
    public static void setDbPath(String dbPath) {Database.dbPath = dbPath;}

    public interface QueryParser{
        public <T> List<T> parse(ResultSet resultSet) throws SQLException;
    }

    public Connection OpenConnection() throws DatabaseException {
        Connection conn = null;

        try {
            String connectionUrl = "jdbc:sqlite:" + Objects.requireNonNull(dbPath.replace("\\","/"), "db path is null");
            //System.out.println("Connection Url: " + connectionUrl);
            Class.forName("org.sqlite.JDBC"); // setup an sqlite driver, which we use
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
            CloseStatement(stmt);
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

    public void Create() throws DatabaseException {
        try {
            Dao dao = new SpecialityDao();
            GroupDao.Drop();
            SpecialityDao.Drop();
            CourseDao.Drop();

            SpecialityDao.Create();
            System.out.println("Created table 'speciality'");

            dao.save(new Speciality("Math", 111));
            dao.save(new Speciality("Physics", 112));
            dao.save(new Speciality("Ukrmova", 117));

            CourseDao.Create();
            System.out.println("Created table 'course'");
            dao = new CourseDao();
            dao.save(new Course("First"));
            dao.save(new Course("Second"));
            dao.save(new Course("Third"));

            GroupDao.Create();
            System.out.println("Created table 'group'");
            dao = new GroupDao();
            dao.save(new Group("TP-62", 25, new CourseDao().getAll().get(0), new SpecialityDao().getAll().get(0)));
            dao.save(new Group("TM-51", 17, new CourseDao().getAll().get(2), new SpecialityDao().getAll().get(2)));
            dao.save(new Group("TK-13", 27, new CourseDao().getAll().get(1), new SpecialityDao().getAll().get(1)));
            //dao.getAll().forEach(speciality -> System.out.println(speciality.getNumericName()+speciality.getSpecName()));
        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
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

    public <T> List<T> ExecuteReadQuery(QueryParser queryParser, String query) {
        List<T> resultArr = new ArrayList<T>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet;

        try {
            conn = Database.getInstance().OpenConnection();
            stmt = conn.createStatement();

            resultSet = stmt.executeQuery(query);
            resultArr = queryParser.parse(resultSet);

            CloseStatement(stmt);
            CloseConnection(conn);
        } catch (Exception e) {
            System.out.println("ExecuteReadQuery fail: " + e.getMessage());
        } finally {
            Database.getInstance().CloseStatement(stmt);
            Database.getInstance().CloseConnection(conn);
        }

        return resultArr;
    }


}
