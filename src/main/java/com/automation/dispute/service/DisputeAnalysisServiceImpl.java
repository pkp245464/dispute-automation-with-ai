package com.automation.dispute.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.automation.dispute.dto.DisputeAnalysisDto;
import com.automation.dispute.dto.DisputeDto;
import com.automation.dispute.model.Dispute;
import com.automation.dispute.repository.DisputeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

@Service
public class DisputeAnalysisServiceImpl implements DisputeAnalysisService {

    private final DisputeRepository disputeRepository;
    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public DisputeAnalysisServiceImpl(DisputeRepository disputeRepository, WebClient.Builder webClientBuilder) {
        this.disputeRepository = disputeRepository;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public DisputeAnalysisDto analyzeDisputes() {
        List<DisputeDto>disputes = disputeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
                                    
        String prompt = buildAnalysisPrompt(disputes);
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(Map.of("contents", new Object[] { Map.of("parts", new Object[]{ Map.of("text", prompt) }) }))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        
        return extractAnalysisResult(response);
    }

    private String buildAnalysisPrompt(List<DisputeDto> disputes) {
        StringBuilder prompt = new StringBuilder("Analyze the following dispute data and provide insights:\n");
        for (DisputeDto dispute : disputes) {
            prompt.append("Customer ID: ").append(dispute.getCustomerId())
                  .append(", Transaction ID: ").append(dispute.getTransactionId())
                  .append(", Type: ").append(dispute.getDisputeType())
                  .append(", Priority: ").append(dispute.getPriority())
                  .append(", Resolved: ").append(dispute.getIsResolved())
                  .append(", Description: ").append(dispute.getDescription())
                  .append("\n---\n");
        }
        return prompt.toString();
    }

    private DisputeAnalysisDto extractAnalysisResult(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            String insights = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();

            DisputeAnalysisDto analysisDto = new DisputeAnalysisDto();
            analysisDto.setInsights(insights);
            return analysisDto;
        } 
        catch (Exception ex) {
            throw new IllegalStateException("Failed to parse Gemini response: " + ex.getMessage());
        }
    }

    private DisputeDto mapToDto(Dispute dispute) {
        DisputeDto dto = new DisputeDto();
        dto.setCustomerId(dispute.getCustomerId());
        dto.setTransactionId(dispute.getTransactionId());
        dto.setDisputeType(dispute.getDisputeType());
        dto.setDescription(dispute.getDescription());
        dto.setPriority(dispute.getPriority());
        dto.setIsResolved(dispute.getIsResolved());
        return dto;
    }
}
