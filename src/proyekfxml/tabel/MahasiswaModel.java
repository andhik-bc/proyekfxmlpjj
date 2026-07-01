/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekfxml.tabel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model data mahasiswa.
 * Memakai StringProperty (bukan String biasa) supaya TableView otomatis
 * memperbarui tampilannya ketika nilai diubah lewat fitur "ubah data".
 */
public class MahasiswaModel {

    private final StringProperty nim  = new SimpleStringProperty(this, "nim",  "");
    private final StringProperty nama = new SimpleStringProperty(this, "nama", "");
    private final StringProperty asal = new SimpleStringProperty(this, "asal", "");

    public MahasiswaModel(String nim, String nama, String asal) {
        setNim(nim);
        setNama(nama);
        setAsal(asal);
    }

    // --- nim ---
    public String getNim()            { return nim.get(); }
    public void   setNim(String v)    { nim.set(v); }
    //kode bagian ini nanti dipakai untuk setup value pada kolom menggunakan lamda expression
    public StringProperty nimProperty()  { return nim; }

    // --- nama ---
    public String getNama()           { return nama.get(); }
    public void   setNama(String v)   { nama.set(v); }
    public StringProperty namaProperty() { return nama; }

    // --- asal ---
    public String getAsal()           { return asal.get(); }
    public void   setAsal(String v)   { asal.set(v); }
    public StringProperty asalProperty() { return asal; }
}