Feature: Transfer eligibility

  Scenario: Transfer Eligibility : Positive testCase
    Given user wants to check transfer eligibility with the following attributes
      | acquirerCountry   | amount   | currency   | recipientAccountUri   | transferAcceptorCountry   |
      | USA               | 10000    | USD        | 951234001             | USA                       |
    When user generates transfer eligibility signature
    And user sends transfer eligibility post request
    Then the response is 'IS SUCCESSFUL'
    And verify transfer eligibility response is valid

  Scenario: Transfer Eligibility : Negative testCase
    Given user wants to check transfer eligibility with the following attributes
      | acquirerCountry   | amount   | currency   | recipientAccountUri   | transferAcceptorCountry   |
      |                   | 10000    | USD        | 951234001             | USA                       |
    When user generates transfer eligibility signature
    And user sends transfer eligibility post request
    Then the response is 'FAILS'
    And verify error response is valid