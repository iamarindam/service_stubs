Feature: McSend Payments service
  Scenario: McSend Payments : Positive testCase
    Given user wants to make a payment request with the payment reference '951234001'
    When user generates payments signature
    And  user sends payments post request
    Then the response is 'IS SUCCESSFUL'
    And verify payments response is valid

  Scenario: McSend Payments : Negative testCase
    Given user wants to make a payment request with the payment reference ''
    When user generates payments signature
    And  user sends payments post request
    Then the response is 'FAILS'
    And verify error response is valid


