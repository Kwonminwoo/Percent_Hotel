package com.yanolja.scbj.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public record ProductPostRequest(
    @NotNull(message = "1차 양도 가격을 입력하세요.")
    int firstPrice,
    int secondPrice,
    @NotBlank(message = "정산 은행을 입력하세요.")
    String bank,
    @NotBlank(message = "정산 계좌를 입력하세요.")
    String accountNumber,
    int secondGrantPeriod,
    boolean isRegistered
) {

    @Builder
    public ProductPostRequest(int firstPrice, int secondPrice, String bank,
        String accountNumber, int secondGrantPeriod, boolean isRegistered) {
        this.firstPrice = firstPrice;
        this.secondPrice = secondPrice;
        this.bank = bank;
        this.accountNumber = accountNumber;
        this.secondGrantPeriod = secondGrantPeriod;
        this.isRegistered = isRegistered;
    }
}
