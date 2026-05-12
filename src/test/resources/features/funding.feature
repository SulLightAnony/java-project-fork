# language: id
Feature: Crowdfunding pinjaman oleh lender
# Lender berkontribusi dana, progres terpantau, dan status berubah otomatis saat 100% terpenuhi
Background
Background: Loan sudah lolos risk engine
Given terdapat loan dengan id "LOAN-001" dan target dana Rp 10.000.000
And status loan adalah FUNDING

Scenario 1 — Kontribusi berhasil
@happy-path
Scenario: Lender berhasil berkontribusi pada loan yang sedang funding
Given terdapat lender dengan id "LENDER-A" dan saldo cukup
When lender berkontribusi sebesar Rp 3.000.000 pada loan "LOAN-001"
Then kontribusi berhasil dicatat
And total dana terkumpul menjadi Rp 3.000.000
And status loan tetap FUNDING

Scenario 2 — Progres akumulasi dari banyak lender
@happy-path
Scenario: Dana bertambah secara akurat dari beberapa lender
Given lender "LENDER-A" sudah berkontribusi Rp 4.000.000
And lender "LENDER-B" sudah berkontribusi Rp 3.000.000
When sistem menghitung total dana terkumpul
Then total dana terkumpul adalah Rp 7.000.000
And persentase progres adalah 70%
Scenario 3 — Fully funded & notifikasi
@happy-path
Scenario: Status berubah ke FUNDED saat dana terkumpul 100%
Given dana sudah terkumpul Rp 9.000.000 dari target Rp 10.000.000
When lender "LENDER-C" berkontribusi sebesar Rp 1.000.000
Then total dana terkumpul menjadi Rp 10.000.000
And status loan berubah menjadi FUNDED
And notifikasi dikirimkan kepada semua lender yang berkontribusi

Scenario 4 — Kontribusi melebihi sisa target
@edge-case
Scenario: Kontribusi ditolak jika melebihi sisa dana yang dibutuhkan
Given dana sudah terkumpul Rp 9.500.000 dari target Rp 10.000.000
When lender "LENDER-D" mencoba berkontribusi sebesar Rp 2.000.000
Then kontribusi ditolak dengan pesan "Jumlah kontribusi melebihi sisa dana yang dibutuhkan"
And total dana terkumpul tetap Rp 9.500.000

Scenario 5 — Loan bukan status FUNDING
@sad-path
Scenario: Kontribusi ditolak jika loan tidak dalam status FUNDING
Given terdapat loan "LOAN-002" dengan status DRAFT
When lender "LENDER-A" mencoba berkontribusi sebesar Rp 1.000.000
Then kontribusi ditolak dengan pesan "Loan tidak dalam fase funding"

Scenario 6 — Kontribusi Rp 0 atau negatif
@sad-path
Scenario Outline: Kontribusi dengan jumlah tidak valid ditolak
When lender "LENDER-A" mencoba berkontribusi sebesar <jumlah>
Then kontribusi ditolak dengan pesan "Jumlah kontribusi harus lebih dari nol"

Examples:
| jumlah |
| Rp 0 |
| Rp -500.000 |

Scenario 7 — Lender yang sama berkontribusi lebih dari sekali
@edge-case
Scenario: Lender bisa berkontribusi beberapa kali selama total tidak melebihi target
Given lender "LENDER-A" sudah berkontribusi Rp 2.000.000
When lender "LENDER-A" berkontribusi lagi sebesar Rp 1.000.000
Then kontribusi berhasil dicatat
And total kontribusi lender "LENDER-A" menjadi Rp 3.000.000