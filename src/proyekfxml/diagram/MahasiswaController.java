/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyekfxml.diagram;

/**
 *
 * @author tf_uii_abc
 */
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller untuk mahasiswa.fxml.
 * Bertugas:
 *  1. Menghubungkan kolom tabel ke properti model.
 *  2. Menyiapkan data contoh.
 *  3. Membangun diagram batang dari data yang sama.
 */
public class MahasiswaController {

    // fx:id pada FXML harus SAMA PERSIS dengan nama variabel di bawah ini.
    @FXML private TableView<Mahasiswa> tabelMahasiswa;
    @FXML private TableColumn<Mahasiswa, String> kolomNim;
    @FXML private TableColumn<Mahasiswa, String> kolomNama;
    @FXML private TableColumn<Mahasiswa, Number> kolomNilai;
    @FXML private BarChart<String, Number> diagramNilai;

    // Sumber data tunggal: dipakai oleh tabel sekaligus diagram.
    private final ObservableList<Mahasiswa> dataMahasiswa = FXCollections.observableArrayList();

    /**
     * Dipanggil otomatis oleh FXMLLoader setelah semua @FXML selesai di-inject.
     */
    @FXML
    public void initialize() {
        // 1) Hubungkan tiap kolom ke properti model.
        //    Nama string ("nim", "nama", "nilai") merujuk ke metode
        //    nimProperty(), namaProperty(), nilaiProperty() pada model.
        kolomNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        kolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kolomNilai.setCellValueFactory(new PropertyValueFactory<>("nilai"));

        // 2) Data contoh.
        dataMahasiswa.addAll(
                new Mahasiswa("21523001", "Andi",  85),
                new Mahasiswa("21523002", "Bunga", 78),
                new Mahasiswa("21523003", "Cahya", 92),
                new Mahasiswa("21523004", "Dewi",  67),
                new Mahasiswa("21523005", "Eka",   88)
        );

        // 3) Tampilkan ke tabel.
        tabelMahasiswa.setItems(dataMahasiswa);

        // 4) Bangun diagram.
        muatDiagram();
    }

    /**
     * Mengubah daftar mahasiswa menjadi satu seri data untuk BarChart.
     * Sumbu-X = nama mahasiswa (CategoryAxis),
     * Sumbu-Y = nilai (NumberAxis).
     */
    private void muatDiagram() {
        XYChart.Series<String, Number> seri = new XYChart.Series<>();
        seri.setName("Nilai Mahasiswa");

        for (Mahasiswa m : dataMahasiswa) {
            seri.getData().add(new XYChart.Data<>(m.getNama(), m.getNilai()));
        }

        diagramNilai.getData().clear();   // kosongkan dulu agar tidak dobel
        diagramNilai.getData().add(seri);
    }
}
