package comp3011.assignment1.controller;

import comp3011.assignment1.dto.ShutdownResponse;
import comp3011.assignment1.dto.UptimeResponse;
import comp3011.assignment1.exception.ShutdownInProgressException;
import comp3011.assignment1.service.GlobalStatsService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final GlobalStatsService statsService;
    private final ConfigurableApplicationContext applicationContext;

    public AdminController(GlobalStatsService statsService,
                            ConfigurableApplicationContext applicationContext) {
        this.statsService = statsService;
        this.applicationContext = applicationContext;
    }

    @GetMapping("/uptime")
    public ResponseEntity<UptimeResponse> getUptime() {
        Instant now = Instant.now();
        UptimeResponse body = new UptimeResponse(
                statsService.getServerStartTime(),
                now,
                statsService.getUptimeSeconds()
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/shutdown")
    public ResponseEntity<ShutdownResponse> shutdown() {
        if (!statsService.tryBeginShutdown()) {
            throw new ShutdownInProgressException("Graceful shutdown is already in progress.");
        }
        
        Executors.newSingleThreadScheduledExecutor().schedule(
                applicationContext::close,
                500,
                TimeUnit.MILLISECONDS
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ShutdownResponse("Graceful shutdown requested."));
    }
}