package com.pp1.salve.api.review;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestauranteReviewRequest {
    @NotNull
    @NotBlank
    private String comentario;
    @NotNull
    @Max(5)
    @Min(1)
    private Integer nota;
    @NotNull
    private Long idLoja;

    private MultipartFile imagem;
    
}
