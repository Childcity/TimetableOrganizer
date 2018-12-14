package TableModels;

import DAO.Dao;
import java.util.List;

public interface TableData<T> {
    List<T> getTableData();
    T getNewRow();
    Dao<T> getDao();
}

