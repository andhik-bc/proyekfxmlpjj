/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyekfxml.diagram;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

/**
 * Controller mahasiswa.fxml.
 *  - Tabel diisi dari data.
 *  - Jenis diagram (Pie / Bar / Line) dipilih lewat ComboBox.
 *  - Panel legenda menampilkan ringkasan data, dan berubah menjadi
 *    detail bagian yang diklik pada diagram.
 */
public class MahasiswaChooseDiagramController {

    @FXML private TableView<Mahasiswa> tabelMahasiswa;
    @FXML private TableColumn<Mahasiswa, String> kolomNim;
    @FXML private TableColumn<Mahasiswa, String> kolomNama;
    @FXML private TableColumn<Mahasiswa, Number> kolomNilai;

    @FXML private ComboBox<String> pilihanDiagram;
    @FXML private StackPane wadahDiagram;
    @FXML private Label lblLegenda;

    private static final String PIE  = "Pie Chart";
    private static final String BAR  = "Bar Chart";
    private static final String LINE = "Line Chart";

    private final ObservableList<Mahasiswa> dataMahasiswa = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Kolom tabel.
        kolomNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        kolomNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kolomNilai.setCellValueFactory(new PropertyValueFactory<>("nilai"));

        // Data contoh.
        dataMahasiswa.addAll(
                new Mahasiswa("21523001", "Andi",  85),
                new Mahasiswa("21523002", "Bunga", 78),
                new Mahasiswa("21523003", "Cahya", 92),
                new Mahasiswa("21523004", "Dewi",  67),
                new Mahasiswa("21523005", "Eka",   88)
        );
        tabelMahasiswa.setItems(dataMahasiswa);

        // Pemilih diagram: isi pilihan, pasang aksi, set default.
        pilihanDiagram.getItems().addAll(PIE, BAR, LINE);
        pilihanDiagram.valueProperty().addListener((obs, lama, baru) -> {
            if (baru != null) {
                gambarDiagram(baru);
            }
        });
        pilihanDiagram.setValue(PIE); // memicu listener -> diagram pertama digambar
    }

    /** Menggambar diagram sesuai jenis dan mengatur legenda default. */
    private void gambarDiagram(String jenis) {
        setLegendaDefault(jenis);

        // setAll() mengganti seluruh isi wadah dengan satu chart sekaligus,
        // sehingga chart lama dijamin terhapus (tidak menumpuk).
        switch (jenis) {
            case BAR  : wadahDiagram.getChildren().setAll(buatBar());
            break;
            case LINE : wadahDiagram.getChildren().setAll(buatLine());
            break;
            default   : wadahDiagram.getChildren().setAll(buatPie());
            break;
        }
    }

    // ===================== PIE =====================

    private PieChart buatPie() {
        int[] k = hitungKategori();              // {A, B, C, D}
        int total = dataMahasiswa.size();

        ObservableList<PieChart.Data> dp = FXCollections.observableArrayList();
        tambahPie(dp, "A (\u226585)", k[0]);
        tambahPie(dp, "B (70-84)",    k[1]);
        tambahPie(dp, "C (60-69)",    k[2]);
        tambahPie(dp, "D (<60)",      k[3]);

        PieChart pie = new PieChart(dp);
        pie.setAnimated(false);                   // cegah bayangan saat berganti diagram
        pie.setLegendVisible(false);             // legenda dipegang panel kita sendiri

        // Pasang aksi klik pada tiap potongan.
        for (PieChart.Data d : pie.getData()) {
            pasangKlik(d.nodeProperty(), () -> {
                double persen = total == 0 ? 0 : d.getPieValue() / total * 100;
                lblLegenda.setText(
                        "Bagian dipilih: Kategori " + d.getName()
                        + "\nJumlah: " + (int) d.getPieValue() + " mahasiswa"
                        + "\nPersentase: " + String.format("%.1f%%", persen));
            });
        }
        return pie;
    }

    private void tambahPie(ObservableList<PieChart.Data> dp, String nama, int jumlah) {
        if (jumlah > 0) {
            dp.add(new PieChart.Data(nama, jumlah));
        }
    }

    // ===================== BAR =====================

    private BarChart<String, Number> buatBar() {
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Nama Mahasiswa");
        NumberAxis y = new NumberAxis(0, 100, 10);
        y.setLabel("Nilai");

        BarChart<String, Number> chart = new BarChart<>(x, y);
        chart.setAnimated(false);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> seri = seriNilai();
        chart.getData().add(seri);
        pasangKlikXY(seri);
        return chart;
    }

    // ===================== LINE =====================

    private LineChart<String, Number> buatLine() {
        CategoryAxis x = new CategoryAxis();
        x.setLabel("Nama Mahasiswa");
        NumberAxis y = new NumberAxis(0, 100, 10);
        y.setLabel("Nilai");

        LineChart<String, Number> chart = new LineChart<>(x, y);
        chart.setAnimated(false);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> seri = seriNilai();
        chart.getData().add(seri);
        pasangKlikXY(seri);
        return chart;
    }

    /** Seri data nilai per mahasiswa, dipakai oleh Bar dan Line. */
    private XYChart.Series<String, Number> seriNilai() {
        XYChart.Series<String, Number> seri = new XYChart.Series<>();
        seri.setName("Nilai");
        for (Mahasiswa m : dataMahasiswa) {
            seri.getData().add(new XYChart.Data<>(m.getNama(), m.getNilai()));
        }
        return seri;
    }

    /** Memasang aksi klik pada tiap titik/batang sebuah seri. */
    private void pasangKlikXY(XYChart.Series<String, Number> seri) {
        for (XYChart.Data<String, Number> d : seri.getData()) {
            pasangKlik(d.nodeProperty(), () ->
                    lblLegenda.setText(
                            "Bagian dipilih: " + d.getXValue()
                            + "\nNilai: " + d.getYValue()));
        }
    }

    // ===================== UTILITAS =====================

    /**
     * Memasang handler klik pada node sebuah data chart.
     * Node chart dibuat secara lazy (bisa belum ada saat ini), maka selain
     * memasang langsung bila sudah ada, kita juga mengamati perubahan node.
     */
    private void pasangKlik(ObservableValue<? extends Node> nodeProp, Runnable aksi) {
        Node sekarang = nodeProp.getValue();
        if (sekarang != null) {
            sekarang.setOnMouseClicked(e -> aksi.run());
        }
        nodeProp.addListener((obs, lama, baru) -> {
            if (baru != null) {
                baru.setOnMouseClicked(e -> aksi.run());
            }
        });
    }

    /** Mengembalikan jumlah mahasiswa per kategori: {A, B, C, D}. */
    private int[] hitungKategori() {
        int a = 0, b = 0, c = 0, d = 0;
        for (Mahasiswa m : dataMahasiswa) {
            double n = m.getNilai();
            if (n >= 85)      a++;
            else if (n >= 70) b++;
            else if (n >= 60) c++;
            else              d++;
        }
        return new int[]{a, b, c, d};
    }

    /** Mengisi legenda dengan ringkasan seluruh data (kondisi awal). */
    private void setLegendaDefault(String jenis) {
        StringBuilder sb = new StringBuilder();

        if (PIE.equals(jenis)) {
            int[] k = hitungKategori();
            sb.append("Distribusi kategori:\n");
            sb.append("A (\u226585): ").append(k[0]).append("  |  ");
            sb.append("B (70-84): ").append(k[1]).append("  |  ");
            sb.append("C (60-69): ").append(k[2]).append("  |  ");
            sb.append("D (<60): ").append(k[3]);
        } else {
            sb.append("Daftar nilai:\n");
            for (Mahasiswa m : dataMahasiswa) {
                sb.append(m.getNama()).append(": ")
                  .append((int) m.getNilai()).append("   ");
            }
        }

        sb.append("\n\n(Klik bagian diagram untuk melihat detailnya.)");
        lblLegenda.setText(sb.toString());
    }
}