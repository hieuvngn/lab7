package vn.edu.eaut.lab7.model;

public class DiemSinhVien implements HasId {
    private int id;
    private String maSinhVien;
    private double chuyenCan;
    private double giuaKy;
    private double cuoiKy;

    public DiemSinhVien() {}

    public DiemSinhVien(int id, String maSinhVien, double chuyenCan, double giuaKy, double cuoiKy) {
        this.id = id;
        this.maSinhVien = maSinhVien;
        this.chuyenCan = chuyenCan;
        this.giuaKy = giuaKy;
        this.cuoiKy = cuoiKy;
    }

    public double getTongKet() {
        return Math.round((0.1 * chuyenCan + 0.3 * giuaKy + 0.6 * cuoiKy) * 10) / 10.0;
    }

    public String getXepLoai() {
        double t = getTongKet();
        if (t >= 8.5) return "A";
        if (t >= 7.0) return "B";
        if (t >= 5.5) return "C";
        if (t >= 4.0) return "D";
        return "F";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMaSinhVien() { return maSinhVien; }
    public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    public double getChuyenCan() { return chuyenCan; }
    public void setChuyenCan(double chuyenCan) { this.chuyenCan = chuyenCan; }
    public double getGiuaKy() { return giuaKy; }
    public void setGiuaKy(double giuaKy) { this.giuaKy = giuaKy; }
    public double getCuoiKy() { return cuoiKy; }
    public void setCuoiKy(double cuoiKy) { this.cuoiKy = cuoiKy; }
}
