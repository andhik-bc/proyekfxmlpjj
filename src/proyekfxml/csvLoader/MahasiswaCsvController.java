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
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
}
