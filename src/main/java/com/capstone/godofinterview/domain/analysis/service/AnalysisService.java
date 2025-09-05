package com.capstone.godofinterview.domain.analysis.service;

import java.util.List;

public interface AnalysisService {
    void startAnalysisAsync(Long interviewId, List<String> videoUrls);
}
