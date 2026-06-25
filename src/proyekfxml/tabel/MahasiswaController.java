/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyekfxml.tabel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MahasiswaController implements Initializable {

    // --- Tabel ---
    @FXML private TableView<MahasiswaModel> tabelMahasiswa;
    @FXML private TableColumn<MahasiswaModel, String> kolomNim;
    @FXML private TableColumn<MahasiswaModel, String> kolomNama;
    @FXML private TableColumn<MahasiswaModel, String> kolomAsal;

    // --- Panel Pencarian ---
    @FXML private TextField txtCari;
    @FXML private Button    btnCari;
    @FXML private TextArea  textTampilHasil;

    // --- Panel Ubah / Hapus ---
    @FXML private TextField txtNimEdit;
    @FXML private TextField txtNamaEdit;
    @FXML private TextField txtAsalEdit;
    @FXML private Button    btnUbahData;
    @FXML private Button    btnHapusData;

    // Tombol "ubah data" di kiri bawah (fx:id-nya harus dipindah ke tombol ini di FXML)
    @FXML private Button    btnSiapUbahData;

    // Sumber kebenaran: seluruh data mahasiswa
    private final ObservableList<MahasiswaModel> dataMaster = FXCollections.observableArrayList();
    
    // Pembungkus yang bisa difilter saat pencarian; inilah yang dipasang ke tabel
    private FilteredList<MahasiswaModel> dataTampil;
    
    // MahasiswaModel yang sedang disiapkan untuk diubah / dihapus
    private MahasiswaModel mahasiswaTerpilih;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Hubungkan tiap kolom dengan properti pada model
        kolomNim.setCellValueFactory(c -> c.getValue().nimProperty());
        kolomNama.setCellValueFactory(c -> c.getValue().namaProperty());
        kolomAsal.setCellValueFactory(c -> c.getValue().asalProperty());

        // 2. Isi data contoh
        muatDataContoh();

        // 3. Bungkus dengan FilteredList lalu pasang ke tabel
        dataTampil = new FilteredList<>(dataMaster, m -> true);
        tabelMahasiswa.setItems(dataTampil);

        // 4. Wiring tombol (tanpa perlu onAction di FXML)
        btnCari.setOnAction(e -> cariMahasiswaModel());
        btnSiapUbahData.setOnAction(e -> siapUbahData());
        btnUbahData.setOnAction(e -> ubahData());
        btnHapusData.setOnAction(e -> hapusData());

        // Bonus: tekan Enter di kotak cari = klik tombol cari
        txtCari.setOnAction(e -> cariMahasiswaModel());
        // Bonus: jika kotak cari dikosongkan, tampilkan kembali semua data
        txtCari.textProperty().addListener((obs, lama, baru) -> {
            if (baru == null || baru.isBlank()) {
                dataTampil.setPredicate(m -> true);
            }
        });
    }

    private void muatDataContoh() {
        dataMaster.addAll(
                new MahasiswaModel("18523001", "Andi Pratama",    "Yogyakarta"),
                new MahasiswaModel("18523002", "Budi Santoso",     "Klaten"),
                new MahasiswaModel("18523003", "Citra Lestari",    "Sleman"),
                new MahasiswaModel("18523004", "Dewi Anggraini",   "Bantul"),
                new MahasiswaModel("18523005", "Eko Nugroho",      "Magelang")
        );
    }

    // ---------------- PENCARIAN ----------------
    private void cariMahasiswaModel() {
        String kunci = (txtCari.getText() == null) ? "" : txtCari.getText().trim().toLowerCase();

        if (kunci.isEmpty()) {
            tampilInfo("Pencarian", "Silakan masukkan kata kunci nama terlebih dahulu.");
            return;
        }

        // Tabel hanya menampilkan baris yang namanya mengandung kata kunci
        dataTampil.setPredicate(m -> m.getNama().toLowerCase().contains(kunci));

        if (dataTampil.isEmpty()) {
            textTampilHasil.clear();
            tampilInfo("Tidak Ditemukan",
                    "Data dengan nama mengandung \"" + txtCari.getText().trim() + "\" tidak ditemukan.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Ditemukan ").append(dataTampil.size()).append(" data:\n\n");
            for (MahasiswaModel m : dataTampil) {
                sb.append(m.getNim()).append("  -  ")
                  .append(m.getNama()).append("  -  ")
                  .append(m.getAsal()).append("\n");
            }
            textTampilHasil.setText(sb.toString());
        }
    }

    // ---------------- SIAP UBAH (isi form dari baris terpilih) ----------------
    private void siapUbahData() {
        MahasiswaModel dipilih = tabelMahasiswa.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            tampilInfo("Belum Memilih", "Klik salah satu baris pada tabel terlebih dahulu.");
            return;
        }
        mahasiswaTerpilih = dipilih;
        txtNimEdit.setText(dipilih.getNim());
        txtNamaEdit.setText(dipilih.getNama());
        txtAsalEdit.setText(dipilih.getAsal());
    }

    // ---------------- UBAH DATA ----------------
    private void ubahData() {
        if (mahasiswaTerpilih == null) {
            tampilInfo("Belum Siap", "Pilih baris lalu tekan tombol \"ubah data\" di kiri bawah dulu.");
            return;
        }
        String nim  = txtNimEdit.getText().trim();
        String nama = txtNamaEdit.getText().trim();
        String asal = txtAsalEdit.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || asal.isEmpty()) {
            tampilInfo("Data Kosong", "Nim, Nama, dan Asal tidak boleh kosong.");
            return;
        }

        mahasiswaTerpilih.setNim(nim);
        mahasiswaTerpilih.setNama(nama);
        mahasiswaTerpilih.setAsal(asal);

        tabelMahasiswa.refresh();          // pastikan baris tergambar ulang
        tampilInfo("Berhasil", "Data mahasiswa berhasil diubah.");
        bersihkanForm();
    }

    // ---------------- HAPUS DATA ----------------
    private void hapusData() {
        if (mahasiswaTerpilih == null) {
            tampilInfo("Belum Siap", "Pilih baris lalu tekan tombol \"ubah data\" di kiri bawah dulu.");
            return;
        }

        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus data " + mahasiswaTerpilih.getNama() + " ?",
                ButtonType.YES, ButtonType.NO);
        konfirmasi.setTitle("Konfirmasi Hapus");
        konfirmasi.setHeaderText(null);
        konfirmasi.showAndWait().ifPresent(jawab -> {
            if (jawab == ButtonType.YES) {
                dataMaster.remove(mahasiswaTerpilih);   // FilteredList ikut ter-update
                bersihkanForm();
                tampilInfo("Berhasil", "Data mahasiswa berhasil dihapus.");
            }
        });
    }

    // ---------------- UTIL ----------------
    private void bersihkanForm() {
        mahasiswaTerpilih = null;
        txtNimEdit.clear();
        txtNamaEdit.clear();
        txtAsalEdit.clear();
        tabelMahasiswa.getSelectionModel().clearSelection();
    }

    private void tampilInfo(String judul, String pesan) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(judul);
        a.setHeaderText(null);
        a.setContentText(pesan);
        a.showAndWait();
    }
}
