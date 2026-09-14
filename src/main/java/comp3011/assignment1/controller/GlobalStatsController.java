package comp3011.assignment1.controller;

import comp3011.assignment1.dto.GlobalStatsResponse;
import comp3011.assignment1.service.GlobalStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/global")
public class GlobalStatsController {

    private final GlobalStatsService statsService;

    public GlobalStatsController(GlobalStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<GlobalStatsResponse> getStats() {
        GlobalStatsResponse body = new GlobalStatsResponse(
                statsService.getInputTokens(),
                statsService.getOutputTokens()
        );
        return ResponseEntity.ok(body);
    }
}