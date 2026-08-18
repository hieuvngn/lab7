package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.HasId;
import java.util.List;

public abstract class AbstractRepository<T extends HasId> {

    protected abstract List<T> data();

    public List<T> findAll() { return data(); }

    public T findById(int id) {
        return data().stream().filter(x -> x.getId() == id).findFirst().orElse(null);
    }

    public void add(T item) {
        int nextId = data().stream().mapToInt(HasId::getId).max().orElse(0) + 1;
        item.setId(nextId);
        data().add(item);
    }

    public void update(T item) {
        T old = findById(item.getId());
        if (old != null) {
            data().set(data().indexOf(old), item);
        }
    }

    public void delete(int id) {
        data().removeIf(x -> x.getId() == id);
    }
}
