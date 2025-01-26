package com.pp1.salve.model.reviewRestaurante;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pp1.salve.api.review.RestauranteReviewRequest;
import com.pp1.salve.exceptions.NoDuplicatedEntityException;
import com.pp1.salve.minio.MinIOInterfacing;
import com.pp1.salve.model.loja.Loja;
import com.pp1.salve.model.loja.LojaService;

import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewRestauranteService {
    public final ReviewRestauranteRepository repository;
    public final LojaService lojaService;
    public final MinIOInterfacing minioService;

    @Transactional(rollbackFor = Exception.class)
    public ReviewRestaurante save(RestauranteReviewRequest request,Authentication authentication) throws Exception {
        Loja loja = lojaService.findById(request.getIdLoja());
        if(repository.existsByLojaAndCriadoPorId(loja, authentication.getName())) {
            throw new NoDuplicatedEntityException("Você já fez uma review para esse restaurante");
        }

        ReviewRestaurante review = ReviewRestaurante.builder()
                .loja(loja)
                .nota(request.getNota())
                .comentario(request.getComentario())
                .build();
        review = repository.save(review);
        if (request.getImagem() != null) {
            review.setImagem(
                    minioService.uploadFile(loja.getId() + "loja", "review" + review.getId(), request.getImagem()));
        }
        return review;
    }

    @Transactional(readOnly = true)
    public Page<ReviewRestaurante> findAllByResturante(Long id, Pageable pageable) throws Exception {

        return monta(repository.findByLoja(lojaService.findById(id), pageable));
    }

    @Transactional(readOnly = true)
    public Page<ReviewRestaurante> findMyRestauranteReviews(Pageable pageable, Authentication authentication)
            throws Exception {
        return monta(repository.findByLoja(lojaService.findMyLoja(authentication), pageable));
    }

    @Transactional(readOnly = true)
    private Page<ReviewRestaurante> monta(Page<ReviewRestaurante> reviews) throws Exception {
        
        for (ReviewRestaurante review : reviews) {
            review = monta(review);
        }
        return reviews;
    }

    @Transactional(readOnly = true)
    private ReviewRestaurante monta(ReviewRestaurante review) throws Exception {
        try {
            review.setImagem(minioService.getSingleUrl(review.getLoja().getId() + "loja", "review" + review.getId()));
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                review.setImagem(null);
            } else {
                throw e;
            } 
        }
        return review;
    }
}
