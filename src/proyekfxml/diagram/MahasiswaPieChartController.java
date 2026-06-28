/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyekfxml.diagram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Controller untuk mahasiswa.fxml.
 *  - Mengisi tabel dari data mahasiswa.
 *  - Membangun PieChart distribusi kategori nilai (A/B/C/D).
 *  - Menghitung ringkasan statistik untuk bilah bawah.
 */
public class MahasiswaPieChartController {

    @FXML private TableView<Mahasiswa> tabelMahasiswa;
    @FXML private TableColumn<Mahasiswa, String> kolomNim;
    @FXML private TableColumn<Mahasiswa, String> kolomNama;
    @FXML private TableColumn<Mahasiswa, Number> kolomNilai;

    @FXML private VBox wadahDiagram;

    // Label ringkasan di bilah bawah.
    @FXML private Label lblJumlah;
    @FXML private Label lblRata;
    @FXML private Label lblTertinggi;
    @FXML private Label lblTerendah;

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

        // 3) Tabel.
        tabelMahasiswa.setItems(dataMahasiswa);

        // 4) Diagram + ringkasan.
        muatDiagram();
        hitungRingkasan();
    }

    /**
     * Membuat PieChart yang menampilkan jumlah mahasiswa per kategori nilai.
     * Kategori: A (>=85), B (70-84), C (60-69), D (<60).
     * Hanya kategori yang berisi data yang ditampilkan sebagai slice.
     */
    private void muatDiagram() {
        int a = 0, b = 0, c = 0, d = 0;
        for (Mahasiswa m : dataMahasiswa) {
            double n = m.getNilai();
            if (n >= 85)      a++;
            else if (n >= 70) b++;
            else if (n >= 60) c++;
            else              d++;
        }

        ObservableList<PieChart.Data> dataPie = FXCollections.observableArrayList();
        if (a > 0) dataPie.add(new PieChart.Data("A (\u226585): " + a, a));
        if (b > 0) dataPie.add(new PieChart.Data("B (70-84): " + b, b));
        if (c > 0) dataPie.add(new PieChart.Data("C (60-69): " + c, c));
        if (d > 0) dataPie.add(new PieChart.Data("D (<60): " + d, d));

        PieChart diagramNilai = new PieChart(dataPie);
        diagramNilai.setLabelsVisible(true);

        VBox.setVgrow(diagramNilai, Priority.ALWAYS);
        wadahDiagram.getChildren().add(diagramNilai);
    }

    /**
     * Menghitung jumlah, rata-rata, nilai tertinggi, dan terendah,
     * lalu menampilkannya pada bilah ringkasan.
     */
    private void hitungRingkasan() {
        int jumlah = dataMahasiswa.size();
        if (jumlah == 0) {
            return;
        }

        double total = 0;
        double tertinggi = Double.NEGATIVE_INFINITY;
        double terendah  = Double.POSITIVE_INFINITY;

        for (Mahasiswa m : dataMahasiswa) {
            double n = m.getNilai();
            total += n;
            tertinggi = Math.max(tertinggi, n);
            terendah  = Math.min(terendah, n);
        }

        lblJumlah.setText(jumlah + " mahasiswa");
        lblRata.setText(String.format("%.1f", total / jumlah));
        lblTertinggi.setText(String.format("%.0f", tertinggi));
        lblTerendah.setText(String.format("%.0f", terendah));
    }
}