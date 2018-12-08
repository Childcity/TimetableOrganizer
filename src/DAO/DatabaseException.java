package DAO;

public class DatabaseException extends Exception{
    private String msg_;

    public DatabaseException(String msg){
        msg_ = "DbError: " + msg;
    }

    @Override
    public String getMessage() {
        return msg_;
    }
}
