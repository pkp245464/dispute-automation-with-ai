package com.automation.dispute.service;

import java.util.List;

import com.automation.dispute.dto.DisputeDto;

public interface DisputeService {
    public DisputeDto createDispute(DisputeDto disputeDto);
    public DisputeDto getDispute(String customerId);
    public List<DisputeDto> getAllDisputes();
    public DisputeDto updateDispute(DisputeDto disputeDto);
    public void deleteDispute(String customerId);
}
