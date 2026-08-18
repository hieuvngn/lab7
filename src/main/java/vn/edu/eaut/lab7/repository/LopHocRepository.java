package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.LopHoc;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LopHocRepository extends AbstractRepository<LopHoc> {
    private static final List<LopHoc> DATA = new ArrayList<>();

    @Override
    protected List<LopHoc> data() { return DATA; }

    public List<LopHoc> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getMaLop().toLowerCase().contains(k)
                        || x.getTenLop().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
