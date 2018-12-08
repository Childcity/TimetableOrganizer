package DAO;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> getById(long id);

    void saveAll(List<T> list);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);
}
