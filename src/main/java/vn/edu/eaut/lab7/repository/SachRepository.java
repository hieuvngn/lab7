package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.Sach;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SachRepository extends AbstractRepository<Sach> {
    private static final List<Sach> DATA = new ArrayList<>();

    @Override
    protected List<Sach> data() { return DATA; }

    public List<Sach> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getTenSach().toLowerCase().contains(k)
                        || x.getTacGia().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
