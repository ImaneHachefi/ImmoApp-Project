package main.immoapp.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PhotoResponse {
    private Long id;
    private String url;
    private Integer ordre;
}