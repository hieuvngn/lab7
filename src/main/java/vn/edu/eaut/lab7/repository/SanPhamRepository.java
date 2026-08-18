package vn.edu.eaut.lab7.repository;

import vn.edu.eaut.lab7.model.SanPham;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SanPhamRepository extends AbstractRepository<SanPham> {
    private static final List<SanPham> DATA = new ArrayList<>();

    @Override
    protected List<SanPham> data() { return DATA; }

    public List<SanPham> search(String key) {
        if (key == null || key.trim().isEmpty()) return DATA;
        String k = key.trim().toLowerCase();
        return DATA.stream()
                .filter(x -> x.getTenSanPham().toLowerCase().contains(k)
                        || x.getMaSanPham().toLowerCase().contains(k))
                .collect(Collectors.toList());
    }
}
