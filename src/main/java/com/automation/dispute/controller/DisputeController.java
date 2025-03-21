package com.automation.dispute.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automation.dispute.dto.DisputeAnalysisDto;
import com.automation.dispute.dto.DisputeDto;
import com.automation.dispute.service.DisputeAnalysisService;
import com.automation.dispute.service.DisputeService;

@RestController
@RequestMapping("/dispute-automation")
public class DisputeController {
    
    private final DisputeService disputeService;

    private final DisputeAnalysisService analysisService;

    public DisputeController(DisputeAnalysisService analysisService, DisputeService disputeService) {
        this.analysisService = analysisService;
        this.disputeService = disputeService;
    }

    @PostMapping("/create")
    public ResponseEntity<DisputeDto> createDispute(@RequestBody DisputeDto disputeDto) {
        DisputeDto createdDispute = disputeService.createDispute(disputeDto);
        return ResponseEntity.ok(createdDispute);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<DisputeDto> getDispute(@PathVariable String customerId) {
        DisputeDto dispute = disputeService.getDispute(customerId);
        return ResponseEntity.ok(dispute);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DisputeDto>> getAllDisputes() {
        List<DisputeDto> disputes = disputeService.getAllDisputes();
        return ResponseEntity.ok(disputes);
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Void> deleteDispute(@PathVariable String customerId) {
        disputeService.deleteDispute(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/analyze")
    public ResponseEntity<DisputeAnalysisDto> analyzeDisputes() {
        DisputeAnalysisDto analysisResult = analysisService.analyzeDisputes();
        return ResponseEntity.ok(analysisResult);
    }
}
