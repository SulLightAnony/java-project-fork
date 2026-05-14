Feature: Disbursement and Interest Calculation
  As a system
  I want to calculate loan interest and disburse fully funded loans
  So that borrowers get their money and repayment schedules are created

  Scenario: Calculate Fixed Interest correctly
    Given a loan principal amount is 10000000
    And the interest strategy is Fixed with a 10 percent rate
    When the interest is calculated
    Then the total interest should be 1000000

  Scenario: Successful loan disbursement
    Given a loan with ID "L-001" has the status "FUNDED"
    When the disbursement service processes the loan "L-001"
    Then the loan status should become "ACTIVE"
    And a repayment schedule should be generated