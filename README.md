# Event Registration Form (Interactive Compose State Challenge)

Tugas Pertemuan 4 - Pemrograman Bergerak
Nama: M Ikmal | NIM: 0102523033 | Kelas: IF23A
Tema: **Form Pendaftaran Event** (digit terakhir NIM = 3)
Repository: https://github.com/ikmaale/event-registration

## Deskripsi
Aplikasi Android sederhana (Kotlin + Jetpack Compose + Material 3) untuk mendaftar
ke event "Workshop Jetpack Compose State". Fokusnya pada state, event, validasi,
recomposition, dan state hoisting.

## Fitur
- Satu screen Compose dengan layout Material 3.
- 2 text input: nama lengkap dan email.
- Counter jumlah tiket (minimal 1, maksimal 5).
- Switch: pengingat event via email.
- Checkbox: persetujuan syarat & ketentuan.
- Validasi: tombol **Daftar** hanya aktif jika nama terisi, email valid, dan syarat disetujui.
  Pesan feedback berubah sesuai kondisi form, dan field email menampilkan pesan error jika formatnya salah.
- Komponen stateless dengan state hoisting: `TicketCounter`, `LabeledSwitch`, `LabeledCheckbox`.
- `rememberSaveable` untuk semua state (aman saat rotasi layar).
- Tombol **Reset** mengembalikan semua state ke nilai awal.

## Daftar State
| State | Tipe | Owner | Dipakai oleh |
|---|---|---|---|
| `name` | String | EventRegistrationScreen | Field nama, validasi, feedback |
| `email` | String | EventRegistrationScreen | Field email, validasi |
| `ticketCount` | Int | EventRegistrationScreen | TicketCounter, feedback |
| `agreedToTerms` | Boolean | EventRegistrationScreen | LabeledCheckbox, validasi tombol Daftar |
| `reminderEnabled` | Boolean | EventRegistrationScreen | LabeledSwitch, feedback |
| `isRegistered` | Boolean | EventRegistrationScreen | FeedbackMessage |

`isNameValid`, `isEmailValid`, dan `isFormValid` bukan state baru; nilainya diturunkan dari state di atas.

## Alur Interaksi
1. Pengguna mengisi nama dan email, lalu memilih jumlah tiket.
2. Pengguna mencentang syarat & ketentuan (Switch pengingat bersifat opsional).
3. Saat semua syarat terpenuhi, tombol **Daftar** aktif dan pesan berubah menjadi "Data lengkap".
4. Setelah menekan **Daftar**, muncul pesan berhasil berisi nama, jumlah tiket, dan status pengingat.
5. Mengubah isi form setelah mendaftar mengembalikan status ke belum terdaftar.
6. Tombol **Reset** mengosongkan semua input.

## Screenshot
Screenshot aplikasi ada di folder `screenshot` (form kosong, email tidak valid, dan form berhasil).

## Cara Menjalankan
1. Clone repository ini, lalu buka di Android Studio (Koala atau lebih baru).
2. Tunggu Gradle sync selesai.
3. Pilih emulator atau perangkat Android, lalu klik **Run > Run 'app'**.
4. Uji rotasi layar: isi form, putar layar, data harus tetap ada.

## Deklarasi Penggunaan AI
- [x] Saya menggunakan AI generatif sebagai alat bantu.
  Tools yang digunakan: Claude (Anthropic).
  Bagian yang dibantu AI: beberapa kode `MainActivity.kt`,draf README, rencana commit, serta panduan langkah setup Android Studio, Git, dan GitHub.

