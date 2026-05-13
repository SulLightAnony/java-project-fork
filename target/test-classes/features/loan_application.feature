Feature: Pengajuan Pinjaman oleh Borrower

  Scenario: Pengajuan berhasil karena data lengkap, score cukup dan limit cukup
    Given Borrower dengan ID "B001", nama "Ismail", credit score 600 memiliki sisa limit 10000000
    When Borrower mengajukan pinjaman sebesar 4000000 dengan tenor 12 bulan untuk "Modal Usaha"
    Then Sistem berhasil membuat pinjaman dengan status awal "DRAFT"
    And Limit borrower "B001" berkurang menjadi 6000000

  Scenario: Pengajuan gagal karena credit score rendah
    Given Borrower dengan ID "B002", nama "LowScore", credit score 400 memiliki sisa limit 5000000
    When Borrower mengajukan pinjaman sebesar 1000000 dengan tenor 12 bulan untuk "Kebutuhan"
    Then Sistem menolak pengajuan dengan error "Borrower is not eligible for a loan"

  Scenario: Pengajuan gagal karena melebihi limit
    Given Borrower dengan ID "B003", nama "NoLimit", credit score 600 memiliki sisa limit 5000000
    When Borrower mengajukan pinjaman sebesar 8000000 dengan tenor 12 bulan untuk "Renovasi"
    Then Sistem menolak pengajuan dengan error "Insufficient borrowing limit"

  Scenario: Transisi status dari DRAFT ke PENDING
    Given Pengajuan pinjaman "L001" saat ini berada pada status "DRAFT"
    When Borrower melakukan submit pengajuan tersebut
    Then Status pinjaman berubah menjadi "PENDING"