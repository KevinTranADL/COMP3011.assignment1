package comp3011.assignment1;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class AudioController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/upload")
    public CompletableFuture<ResponseEntity<String>> handleFileUpload(@RequestParam("audio") MultipartFile audioFile) {
        // CompletableFuture pushes this blocking I/O task to a separate thread pool, keeping the main server responsive under high load
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Retrieves the key strictly at runtime from the OS environment to prevent leaks
                String apiKey = System.getenv("OPENAI_API_KEY");
                if (apiKey == null || apiKey.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API Key missing from environment variables.");
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                headers.setBearerAuth(apiKey);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("file", audioFile.getResource());
                body.add("model", "gpt-4o-mini-transcribe");

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                // Makes the POST request to the external Cloud STT service
                ResponseEntity<Map> response = restTemplate.postForEntity(
                        "https://api.openai.com/v1/audio/transcriptions",
                        requestEntity,
                        Map.class
                );

                String transcription = (String) response.getBody().get("text");
                return ResponseEntity.ok(transcription);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing audio: " + e.getMessage());
            }
        });
    }
}