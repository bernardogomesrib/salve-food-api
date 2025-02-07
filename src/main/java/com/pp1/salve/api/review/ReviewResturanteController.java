package com.pp1.salve.api.review;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pp1.salve.model.reviewRestaurante.ReviewRestaurante;
import com.pp1.salve.model.reviewRestaurante.ReviewRestauranteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@Tag(name = "Review", description = "pontos de interação com review")
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewResturanteController {
    private final ReviewRestauranteService service;
    @PostMapping(consumes = "multipart/form-data")
    public ReviewRestaurante post(@ModelAttribute @Valid @RequestBody RestauranteReviewRequest request,Authentication authentication) throws Exception {
        return service.save(request,authentication);
    }
    @GetMapping("{id}")
    public ResponseEntity<Page<ReviewRestaurante>> get(@RequestParam Long id,
            @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("createdAt")));
        return ResponseEntity.ok(service.findAllByResturante(id, pageable));
    }

    @GetMapping("/verificar/{id}")
    public ResponseEntity<Boolean> verificarReview(@PathVariable Long id, Authentication authentication) {
        try {
            return ResponseEntity.ok(service.existsByLojaIdAndAuthUser(id, authentication));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/minha")
    public ResponseEntity<Page<ReviewRestaurante>> getMyReviews(@RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size, Authentication authentication) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("createdAt")));
        
        System.out.println("chegou aqui");
        return ResponseEntity.ok(service.findMyRestauranteReviews(pageable, authentication));
    }
    @PutMapping(consumes = "multipart/form-data")
    public ReviewRestaurante put(@ModelAttribute @Valid @RequestBody RestauranteReviewRequest request,Authentication authentication) throws Exception {
        return service.update(request,authentication);
    }
        
}
