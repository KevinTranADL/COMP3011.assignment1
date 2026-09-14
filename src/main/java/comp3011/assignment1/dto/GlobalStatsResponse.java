package comp3011.assignment1.dto;

public class GlobalStatsResponse {
    private final long inputTokens;
    private final long outputTokens;
    public GlobalStatsResponse(long inputTokens, long outputTokens) {
        this.inputTokens = inputTokens;
        this.outputTokens = outputTokens;
    }
    public long getInputTokens() {
        return inputTokens;
    }
    public long getOutputTokens() {
        return outputTokens;
    }
}