/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekfxml.csvLoader;

/**
 *
 * @author tf_uii_abc
 */


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MahasiswaCsvModel {

    private final StringProperty nim = new SimpleStringProperty(this, "nim", "");
    private final StringProperty nama = new SimpleStringProperty(this, "nama", "");
    private final StringProperty jurusan = new SimpleStringProperty(this, "jurusan", "");
    private final DoubleProperty ipk = new SimpleDoubleProperty(this, "ipk", 0);

    public MahasiswaCsvModel(String nim, String nama, String jurusan, double ipk) {
        setNim(nim); setNama(nama); setJurusan(jurusan); setIpk(ipk);
    }

    public String getNim() { return nim.get(); }
    public void setNim(String v) { nim.set(v); }
    public StringProperty nimProperty() { return nim; }

    public String getNama() { return nama.get(); }
    public void setNama(String v) { nama.set(v); }
    public StringProperty namaProperty() { return nama; }

    public String getJurusan() { return jurusan.get(); }
    public void setJurusan(String v) { jurusan.set(v); }
    public StringProperty jurusanProperty() { return jurusan; }

    public double getIpk() { return ipk.get(); }
    public void setIpk(double v) { ipk.set(v); }
    public DoubleProperty ipkProperty() { return ipk; }
}
