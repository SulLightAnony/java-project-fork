Feature: Pengajuan Pinjaman oleh Borrower

  Scenario: Pengajuan berhasil karena data lengkap dan limit cukup
    Given Borrower dengan ID "B001" memiliki sisa limit 10000000
    When Borrower mengajukan pinjaman sebesar 4000000
    Then Sistem berhasil membuat pinjaman dengan status awal "DRAFT"

  Scenario: Pengajuan gagal karena melebihi limit
    Given Borrower dengan ID "B002" memiliki sisa limit 5000000
    When Borrower mengajukan pinjaman sebesar 8000000
    Then Sistem menolak pengajuan dengan error limit tidak cukup

  Scenario: Transisi status dari DRAFT ke PENDING
    Given Pengajuan pinjaman "L001" saat ini berada pada status "DRAFT"
    When Borrower melakukan submit pengajuan tersebut
    Then Status pinjaman berubah menjadi "PENDING"