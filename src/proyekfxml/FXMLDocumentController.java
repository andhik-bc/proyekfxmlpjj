/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package proyekfxml;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controller untuk FXMLDocument.fxml (Form Registrasi).
 *
 * Tugas controller:
 *  1. Mengisi pilihan ComboBox jurusan.
 *  2. Membaca nilai dari setiap control saat tombol "registrasi" ditekan.
 *  3. Menampilkan hasil registrasi pada TextArea di sebelahnya.
 */
public class FXMLDocumentController implements Initializable {

    // ---- Field di-inject otomatis dari fx:id pada FXML ----
    @FXML 
    private TextField tfNama;
    
    @FXML 
    private TextField tfEmail;
    
    @FXML 
    private RadioButton rbLaki;
    
    @FXML 
    private RadioButton rbPerempuan;
    
    @FXML 
    private ToggleGroup jenisKelamin;
    
    @FXML 
    private ComboBox<String> cbJurusan;
    
    @FXML 
    private DatePicker dpTanggalLahir;
    
    @FXML 
    private Button btnRegistrasi;
    
    @FXML 
    private TextArea taDataRegistrasi;

    // Penghitung agar tiap entri diberi nomor urut
    private int nomorRegistrasi = 0;

    // Format tanggal ke gaya Indonesia, mis. "25 Juni 2026"
    private static final DateTimeFormatter FORMAT_TANGGAL =
            DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID"));

    /**
     * Dipanggil otomatis oleh FXMLLoader setelah semua field @FXML terisi.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1) Mengisi item ComboBox jurusan
        cbJurusan.setItems(FXCollections.observableArrayList(
                "Informatika", "Elektro", "Industri", "Kimia"));

        // 2) TextArea hanya untuk menampilkan hasil, jadi dibuat read-only
        taDataRegistrasi.setEditable(false);

        // 3) Menghubungkan tombol dengan method handler.
        //    (Alternatif: tambahkan onAction="#handleRegistrasi" di FXML,
        //     lalu baris ini boleh dihapus.)
        btnRegistrasi.setOnAction(this::handleRegistrasi);
    }

    /**
     * Aksi ketika tombol "registrasi" ditekan.
     */
    @FXML
    private void handleRegistrasi(ActionEvent event) {
        // ---- Validasi sederhana: pastikan semua terisi ----
        StringBuilder pesanError = new StringBuilder();
        if (tfNama.getText().trim().isEmpty()) {
            pesanError.append("\u2022 Nama belum diisi\n");
        }
        if (tfEmail.getText().trim().isEmpty()) {
            pesanError.append("\u2022 Email belum diisi\n");
        }
        if (jenisKelamin.getSelectedToggle() == null) {
            pesanError.append("\u2022 Jenis kelamin belum dipilih\n");
        }
        if (cbJurusan.getValue() == null) {
            pesanError.append("\u2022 Jurusan belum dipilih\n");
        }
        if (dpTanggalLahir.getValue() == null) {
            pesanError.append("\u2022 Tanggal lahir belum diisi\n");
        }

        if (pesanError.length() > 0) {
            tampilkanPeringatan("Data belum lengkap:\n" + pesanError);
            return; // hentikan proses jika ada yang kosong
        }

        // ---- Mengambil nilai dari tiap control ----
        String nama  = tfNama.getText().trim();
        String email = tfEmail.getText().trim();

        // RadioButton yang terpilih diambil dari ToggleGroup
        RadioButton rbTerpilih = (RadioButton) jenisKelamin.getSelectedToggle();
        String kelamin = rbTerpilih.getText();

        String jurusan = cbJurusan.getValue();

        LocalDate tanggalLahir = dpTanggalLahir.getValue();
        String tanggalStr = tanggalLahir.format(FORMAT_TANGGAL);

        // ---- Menyusun teks hasil ----
        nomorRegistrasi++;
        String hasil =
                "Registrasi #" + nomorRegistrasi + "\n"
              + "Nama                : " + nama + "\n"
              + "Email                 : " + email + "\n"
              + "Jenis kelamin : " + kelamin + "\n"
              + "Jurusan            : " + jurusan + "\n"
              + "Tanggal lahir  : " + tanggalStr + "\n"
              + "------------------------------\n";

        // ---- Menampilkan ke TextArea (ditambahkan, bukan menimpa) ----
        taDataRegistrasi.appendText(hasil);

        // ---- Mengosongkan form untuk entri berikutnya ----
        bersihkanForm();
    }

    /**
     * Mengosongkan kembali semua control pada form.
     */
    private void bersihkanForm() {
        tfNama.clear();
        tfEmail.clear();
        jenisKelamin.selectToggle(null);          // batalkan pilihan radio button
        cbJurusan.getSelectionModel().clearSelection();
        cbJurusan.setValue(null);                 // tampilkan kembali promptText
        dpTanggalLahir.setValue(null);
        tfNama.requestFocus();
    }

    /**
     * Menampilkan dialog peringatan sederhana.
     */
    private void tampilkanPeringatan(String pesan) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }
}