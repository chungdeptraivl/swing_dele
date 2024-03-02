public class Rule {
    private String ma;
    private  String ten;
    private String nhom;

    public Rule(String ma, String ten, String nhom) {
        this.ma = ma;
        this.ten = ten;
        this.nhom = nhom;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNhom() {
        return nhom;
    }

    public void setNhom(String nhom) {
        this.nhom = nhom;
    }
}
