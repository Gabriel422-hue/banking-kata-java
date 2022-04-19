package com.optivem.kata.banking.core.usecases;

import com.optivem.kata.banking.core.domain.accounts.BankAccount;
import com.optivem.kata.banking.core.domain.exceptions.ValidationMessages;
import com.optivem.kata.banking.core.usecases.withdrawfunds.WithdrawFundsRequest;
import com.optivem.kata.banking.core.usecases.withdrawfunds.WithdrawFundsResponse;
import com.optivem.kata.banking.core.usecases.withdrawfunds.WithdrawFundsUseCase;
import com.optivem.kata.banking.infra.fake.accounts.FakeBankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.optivem.kata.banking.core.common.Assertions.assertThrowsValidationException;
import static com.optivem.kata.banking.core.common.MethodSources.NON_POSITIVE_INTEGERS;
import static com.optivem.kata.banking.core.common.MethodSources.NULL_EMPTY_WHITESPACE;
import static org.assertj.core.api.Assertions.assertThat;

class WithdrawFundsUseCaseTest {

    private FakeBankAccountRepository repository;
    private WithdrawFundsUseCase useCase;

    @BeforeEach
    void init() {
        this.repository = new FakeBankAccountRepository();
        this.useCase = new WithdrawFundsUseCase(repository);
    }

    @Test
    void should_withdraw_funds_given_valid_request() {
        var accountNumber = "GB10BARC20040184197751";
        var initialBalance = 70;
        var amount = 30;
        var expectedFinalBalance = 40;

        var bankAccount = new BankAccount(accountNumber, initialBalance);
        repository.add(bankAccount);

        var request = new WithdrawFundsRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);

        var expectedResponse = new WithdrawFundsResponse();
        expectedResponse.setBalance(expectedFinalBalance);

        var response = useCase.handle(request);

        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @ParameterizedTest
    @MethodSource(NULL_EMPTY_WHITESPACE)
    void should_throw_exception_given_empty_account_number(String accountNumber) {
        var request = new WithdrawFundsRequest();
        request.setAccountNumber(accountNumber);

        assertThrows(request, ValidationMessages.ACCOUNT_NUMBER_EMPTY);
    }

    @Test
    void should_throw_exception_given_non_existent_account_number() {
        var accountNumber = "GB10BARC20040184197751";
        var amount = 30;

        var request = new WithdrawFundsRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);

        assertThrows(request, ValidationMessages.ACCOUNT_NUMBER_NOT_EXIST);
    }

    @ParameterizedTest
    @MethodSource(NON_POSITIVE_INTEGERS)
    void should_throw_exception_given_non_positive_amount(int amount) {
        var accountNumber = "GB10BARC20040184197751";

        var request = new WithdrawFundsRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);

        assertThrows(request, ValidationMessages.NON_POSITIVE_TRANSACTION_AMOUNT);
    }

    private void assertThrows(WithdrawFundsRequest request, String message) {
        assertThrowsValidationException(useCase, request, message);
    }

}