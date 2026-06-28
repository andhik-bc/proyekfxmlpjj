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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Controller untuk mahasiswa.fxml.
 * Tabel diisi dari data, lalu BarChart DIBANGUN DI SINI (bukan di FXML)
 * supaya tidak terjadi error instansiasi chart oleh FXMLLoader.
 */
public class MahasiswaController2 {

    @FXML private TableView<Mahasiswa> tabelMahasiswa;
    @FXML private TableColumn<Mahasiswa, String> kolomNim;
    @FXML private TableColumn<Mahasiswa, String> kolomNama;
    @FXML private TableColumn<Mahasiswa, Number> kolomNilai;

    // Wadah di sisi kanan tempat chart akan disisipkan.
    @FXML private VBox wadahDiagram;

    private final ObservableList<Mahasiswa> dataMahasiswa = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1) Hubungkan kolom ke properti model.
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

        // 4) Bangun dan sisipkan diagram.
        muatDiagram();
    }

    /**
     * Membuat BarChart secara terprogram, mengisinya dengan data nilai,
     * lalu menambahkannya ke wadahDiagram agar memenuhi sisi kanan.
     */
    private void muatDiagram() {
        // Sumbu dibuat lewat konstruktor Java (cara yang selalu valid).
        CategoryAxis sumbuX = new CategoryAxis();
        sumbuX.setLabel("Nama Mahasiswa");

        NumberAxis sumbuY = new NumberAxis(0, 100, 10); // min, max, jarak tick
        sumbuY.setLabel("Nilai");

        BarChart<String, Number> diagramNilai = new BarChart<>(sumbuX, sumbuY);
        diagramNilai.setTitle("Nilai Mahasiswa");

        // Susun seri data dari daftar mahasiswa.
        XYChart.Series<String, Number> seri = new XYChart.Series<>();
        seri.setName("Nilai");
        for (Mahasiswa m : dataMahasiswa) {
            seri.getData().add(new XYChart.Data<>(m.getNama(), m.getNilai()));
        }
        diagramNilai.getData().add(seri);

        // Buat chart memanjang penuh, lalu sisipkan ke wadah.
        VBox.setVgrow(diagramNilai, Priority.ALWAYS);
        wadahDiagram.getChildren().add(diagramNilai);
    }
}
