package DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



public class GroupDao implements Dao<Group> {
    private static Database db;
    private List<Group> groups = new ArrayList<>();

    public GroupDao() {
        db = Database.getInstance();
        //users.add(new User("John", "john@domain.com"));
        //users.add(new User("Susan", "susan@domain.com"));
    }

    public static void Create() throws DatabaseException {
        db.ExecuteWriteQuery("");
    }

    @Override
    public Optional<Group> getById(long id) {
        return Optional.ofNullable(groups.get((int) id));
    }

    @Override
    public void saveAll(List<Group> list) {

    }

    @Override
    public List<Group> getAll() {
        return groups;
    }

    @Override
    public void save(Group user) {
        groups.add(user);
    }

    @Override
    public void update(Group group, String[] params) {
        //group.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        //group.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));

        //users.add(user);
    }

    @Override
    public void delete(Group group) {
        //group.remove(user);
    }
}
