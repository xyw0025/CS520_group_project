package com.group.cs520.documentation;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.group.cs520.model.Match;

import java.util.List;
import java.util.Map;

@Tag(name = "Match API")
public interface MatchApi {
    @Operation(summary = "Get all matches")
    public ResponseEntity<List<Match>> getAllMatches();

    @Operation(summary = "Create a match")
    public ResponseEntity<?> createMatch(@RequestBody(description = "Match payload", required = true, 
        content = @Content(schema = @Schema(implementation = MatchPayload.class))
        ) Map<String, Object> payload);

    @Operation(summary = "add match history")
    public ResponseEntity<?> updateMatchHistory(@RequestBody(description = "add Match history payload", required = true, 
        content = @Content(schema = @Schema(implementation = AddMatchHistoryPayload.class))
        )Map<String, Object> payload);

    
    class MatchPayload {
        @Schema(description = "user ids", example = "[\"user_id1\", \"user_id2\"]")
        public List<String> userIds;
        @Schema(description = "status", example = "0")
        public Integer status;
    }

    class AddMatchHistoryPayload {
        @Schema(description = "people who initiate the action", example = "655d3d90fee7474f900da830")
        public String senderId;
        @Schema(description = "people receive the action", example = "655d3d90fee7474f900da830")
        public String receiverId;
        @Schema(description = "0 means reject, 1 means accept", example = "0")
        public Integer behavior;
    }
}
