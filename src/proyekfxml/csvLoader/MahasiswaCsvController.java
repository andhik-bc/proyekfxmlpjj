/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyekfxml.csvLoader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MahasiswaCsvController implements Initializable {

    @FXML private Button btnPilihBerkas;
    @FXML private Label lblPathBerkas;
    @FXML private Button btnImpor;
    @FXML private TableView<MahasiswaCsvModel> tabelMahasiswaCsvModel;
    @FXML private TableColumn<MahasiswaCsvModel, String> kolomNim;
    @FXML private TableColumn<MahasiswaCsvModel, String> kolomNama;
    @FXML private TableColumn<MahasiswaCsvModel, String> kolomJurusan;
    @FXML private TableColumn<MahasiswaCsvModel, Number> kolomIpk;
    @FXML private Label lblStatus;

    // Form tambah/edit
    @FXML private TextField txtNim;
    @FXML private TextField txtNama;
    @FXML private TextField txtJurusan;
    @FXML private TextField txtIpk;
    @FXML private Button btnTambah;
    @FXML private Button btnSimpanEdit;
    @FXML private Button btnBatalEdit;
    @FXML private Label lblFormInfo;

    private MahasiswaCsvModel dataSedangDiedit; // null = mode tambah, non-null = mode edit

    private File berkasTerpilih;
    private final ObservableList<MahasiswaCsvModel> dataMahasiswaCsvModel = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kolomNim.setCellValueFactory(c -> c.getValue().nimProperty());
        kolomNama.setCellValueFactory(c -> c.getValue().namaProperty());
        kolomJurusan.setCellValueFactory(c -> c.getValue().jurusanProperty());
        kolomIpk.setCellValueFactory(c -> c.getValue().ipkProperty());
        tabelMahasiswaCsvModel.setItems(dataMahasiswaCsvModel);

        btnPilihBerkas.setOnAction(e -> pilihBerkas());
        btnImpor.setOnAction(e -> imporData());

        btnTambah.setOnAction(e -> tambahData());
        btnSimpanEdit.setOnAction(e -> simpanPerubahan());
        btnBatalEdit.setOnAction(e -> batalEdit());

        // Klik baris di tabel -> isi otomatis ke form untuk mode edit
        tabelMahasiswaCsvModel.getSelectionModel().selectedItemProperty()
                .addListener((obs, lama, terpilih) -> {
                    if (terpilih != null) {
                        muatKeForm(terpilih);
                    }
                });
    }

    private void pilihBerkas() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Pilih Berkas CSV Data MahasiswaCsvModel");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Berkas CSV", "*.csv"));
        File hasil = fc.showOpenDialog(btnPilihBerkas.getScene().getWindow());
        if (hasil != null) {
            berkasTerpilih = hasil;
            lblPathBerkas.setText(hasil.getAbsolutePath());
            lblStatus.setText("");
        }
    }

    private void imporData() {
        if (berkasTerpilih == null) {
            lblStatus.setText("Pilih berkas CSV terlebih dahulu.");
            return;
        }
        dataMahasiswaCsvModel.clear();
        int berhasil = 0, gagal = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(berkasTerpilih))) {
            String baris;
            boolean headerDilewati = false;
            while ((baris = br.readLine()) != null) {
                if (!headerDilewati) { headerDilewati = true; continue; }
                if (baris.trim().isEmpty()) continue;
                String[] kolom = baris.split(",");
                if (kolom.length < 4) { gagal++; continue; }
                try {
                    String nim = kolom[0].trim();
                    String nama = kolom[1].trim();
                    String jurusan = kolom[2].trim();
                    double ipk = Double.parseDouble(kolom[3].trim());
                    dataMahasiswaCsvModel.add(new MahasiswaCsvModel(nim, nama, jurusan, ipk));
                    berhasil++;
                } catch (NumberFormatException ex) {
                    gagal++;
                }
            }
            String pesan = berhasil + " data berhasil diimpor";
            if (gagal > 0) pesan += ", " + gagal + " baris dilewati (format tidak valid)";
            lblStatus.setText(pesan + ".");
        } catch (IOException ex) {
            lblStatus.setText("Gagal membaca berkas: " + ex.getMessage());
        }
    }

    // ================= FORM TAMBAH & EDIT =================

    /** Mengisi form dari baris tabel yang dipilih (mode edit). */
    private void muatKeForm(MahasiswaCsvModel m) {
        dataSedangDiedit = m;
        txtNim.setText(m.getNim());
        txtNama.setText(m.getNama());
        txtJurusan.setText(m.getJurusan());
        txtIpk.setText(String.valueOf(m.getIpk()));
        btnSimpanEdit.setDisable(false);
        btnBatalEdit.setDisable(false);
        btnTambah.setDisable(true);
        lblFormInfo.setText("Mode edit: " + m.getNim());
    }

    /** Mengosongkan form dan mengembalikan ke mode tambah. */
    private void batalEdit() {
        dataSedangDiedit = null;
        kosongkanForm();
        tabelMahasiswaCsvModel.getSelectionModel().clearSelection();
        btnSimpanEdit.setDisable(true);
        btnBatalEdit.setDisable(true);
        btnTambah.setDisable(false);
        lblFormInfo.setText("");
    }

    private void kosongkanForm() {
        txtNim.clear();
        txtNama.clear();
        txtJurusan.clear();
        txtIpk.clear();
    }

    /** Validasi input form. Mengembalikan true jika valid. */
    private boolean validasiForm() {
        String nim = txtNim.getText() == null ? "" : txtNim.getText().trim();
        String nama = txtNama.getText() == null ? "" : txtNama.getText().trim();
        String jurusan = txtJurusan.getText() == null ? "" : txtJurusan.getText().trim();
        String ipkStr = txtIpk.getText() == null ? "" : txtIpk.getText().trim();

        if (nim.isEmpty() || nama.isEmpty() || jurusan.isEmpty() || ipkStr.isEmpty()) {
            lblFormInfo.setText("Semua field wajib diisi.");
            return false;
        }
        try {
            double ipk = Double.parseDouble(ipkStr);
            if (ipk < 0 || ipk > 4) {
                lblFormInfo.setText("IPK harus di antara 0.0 - 4.0.");
                return false;
            }
        } catch (NumberFormatException ex) {
            lblFormInfo.setText("IPK harus berupa angka, contoh: 3.45");
            return false;
        }
        lblFormInfo.setText("");
        return true;
    }

    /** Tombol "Tambah Data": menambah baris baru ke tabel + CSV. */
    private void tambahData() {
        if (berkasTerpilih == null) {
            lblFormInfo.setText("Pilih dan impor berkas CSV terlebih dahulu.");
            return;
        }
        if (!validasiForm()) {
            return;
        }
        String nim = txtNim.getText().trim();
        boolean duplikat = dataMahasiswaCsvModel.stream()
                .anyMatch(m -> m.getNim().equalsIgnoreCase(nim));
        if (duplikat) {
            lblFormInfo.setText("NIM " + nim + " sudah ada di tabel.");
            return;
        }

        double ipk = Double.parseDouble(txtIpk.getText().trim());
        MahasiswaCsvModel baru = new MahasiswaCsvModel(
                nim, txtNama.getText().trim(), txtJurusan.getText().trim(), ipk);
        dataMahasiswaCsvModel.add(baru);

        tulisSemuaKeCsv();
        kosongkanForm();
        lblStatus.setText("Data " + nim + " berhasil ditambahkan dan disimpan ke CSV.");
    }

    /** Tombol "Simpan Perubahan": menyimpan hasil edit baris terpilih. */
    private void simpanPerubahan() {
        if (dataSedangDiedit == null) {
            lblFormInfo.setText("Pilih data di tabel yang ingin diedit terlebih dahulu.");
            return;
        }
        if (!validasiForm()) {
            return;
        }
        String nimBaru = txtNim.getText().trim();
        boolean duplikat = dataMahasiswaCsvModel.stream()
                .anyMatch(m -> m != dataSedangDiedit && m.getNim().equalsIgnoreCase(nimBaru));
        if (duplikat) {
            lblFormInfo.setText("NIM " + nimBaru + " sudah dipakai data lain.");
            return;
        }

        double ipk = Double.parseDouble(txtIpk.getText().trim());
        dataSedangDiedit.setNim(nimBaru);
        dataSedangDiedit.setNama(txtNama.getText().trim());
        dataSedangDiedit.setJurusan(txtJurusan.getText().trim());
        dataSedangDiedit.setIpk(ipk);

        tabelMahasiswaCsvModel.refresh();
        tulisSemuaKeCsv();

        String nimTersimpan = nimBaru;
        batalEdit();
        lblStatus.setText("Perubahan data " + nimTersimpan + " berhasil disimpan ke CSV.");
    }

    /** Menulis ulang seluruh isi tabel ke berkas CSV yang sedang dipilih. */
    private void tulisSemuaKeCsv() {
        if (berkasTerpilih == null) {
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(berkasTerpilih))) {
            bw.write("nim,nama,jurusan,ipk");
            bw.newLine();
            for (MahasiswaCsvModel m : dataMahasiswaCsvModel) {
                bw.write(m.getNim() + "," + m.getNama() + "," + m.getJurusan() + "," + m.getIpk());
                bw.newLine();
            }
        } catch (IOException ex) {
            lblStatus.setText("Gagal menyimpan perubahan ke CSV: " + ex.getMessage());
        }
    }
}