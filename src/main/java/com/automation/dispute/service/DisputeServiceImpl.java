package com.automation.dispute.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.automation.dispute.dto.DisputeDto;
import com.automation.dispute.model.Dispute;
import com.automation.dispute.repository.DisputeRepository;

@Service
public class DisputeServiceImpl implements DisputeService {

    private final DisputeRepository disputeRepository;

    public DisputeServiceImpl(DisputeRepository disputeRepository) {
        this.disputeRepository = disputeRepository;
    }


    @Override
    public DisputeDto createDispute(DisputeDto disputeDto) {
        if(disputeRepository.existsById(disputeDto.getCustomerId())) {
            throw new IllegalArgumentException("Dispute for customer ID " + disputeDto.getCustomerId() + " already exists.");
        }

        Dispute dispute = new Dispute();
        dispute.setCustomerId(disputeDto.getCustomerId());
        dispute.setTransactionId(disputeDto.getTransactionId());
        dispute.setDisputeType(disputeDto.getDisputeType());
        dispute.setDescription(disputeDto.getDescription());
        dispute.setPriority(disputeDto.getPriority());
        dispute.setIsResolved(disputeDto.getIsResolved());

        dispute = disputeRepository.save(dispute);

        return mapToDto(dispute);
    }

    //TODO: we can also list only those disputes which are not resolved
    
    @Override
    public DisputeDto getDispute(String customerId) {
        Dispute dispute = disputeRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("No dispute found for customer ID " + customerId));
        return mapToDto(dispute);
    }

    @Override
    public List<DisputeDto> getAllDisputes() {
        return disputeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DisputeDto updateDispute(DisputeDto disputeDto) {
        return null;
    }

    @Override
    public void deleteDispute(String customerId) {
        Dispute dispute = disputeRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("No dispute found for customer ID " + customerId));
        dispute.setIsResolved(true);        
        disputeRepository.delete(dispute);
    }

    public DisputeDto mapToDto(Dispute dispute) {
        DisputeDto disputeDto = new DisputeDto();
        disputeDto.setCustomerId(dispute.getCustomerId());
        disputeDto.setTransactionId(dispute.getTransactionId());
        disputeDto.setDisputeType(dispute.getDisputeType());
        disputeDto.setDescription(dispute.getDescription());
        disputeDto.setPriority(dispute.getPriority());
        disputeDto.setIsResolved(dispute.getIsResolved());
        return disputeDto;
    }
}
