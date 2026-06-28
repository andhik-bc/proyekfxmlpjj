package proyekfxml.diagram;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author tf_uii_abc
 */
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model data mahasiswa.
 * Menggunakan JavaFX property agar nilai bisa langsung diikat (binding)
 * ke TableView maupun chart.
 */
public class Mahasiswa {

    private final StringProperty nim;
    private final StringProperty nama;
    private final DoubleProperty nilai;

    public Mahasiswa(String nim, String nama, double nilai) {
        this.nim = new SimpleStringProperty(nim);
        this.nama = new SimpleStringProperty(nama);
        this.nilai = new SimpleDoubleProperty(nilai);
    }

    // --- NIM ---
    public String getNim() {
        return nim.get();
    }

    public void setNim(String value) {
        nim.set(value);
    }

    public StringProperty nimProperty() {
        return nim;
    }

    // --- Nama ---
    public String getNama() {
        return nama.get();
    }

    public void setNama(String value) {
        nama.set(value);
    }

    public StringProperty namaProperty() {
        return nama;
    }

    // --- Nilai ---
    public double getNilai() {
        return nilai.get();
    }

    public void setNilai(double value) {
        nilai.set(value);
    }

    public DoubleProperty nilaiProperty() {
        return nilai;
    }
}
