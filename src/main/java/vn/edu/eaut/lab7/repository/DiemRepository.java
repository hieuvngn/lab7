package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.DiemSinhVien;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiemRepository extends AbstractRepository<DiemSinhVien> {
    private static final List<DiemSinhVien> DATA = new ArrayList<>();

    @Override
    protected List<DiemSinhVien> data() { return DATA; }

    public List<DiemSinhVien> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getMaSinhVien().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
