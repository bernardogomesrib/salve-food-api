package com.pp1.salve.api.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PfpRequest {
    @NotNull
    private byte[] file;
    @NotNull
    private String mimetype;
}
