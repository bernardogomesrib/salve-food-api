package com.pp1.salve.model.loja;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pp1.salve.model.reviewRestaurante.ReviewRestaurante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Loja")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loja {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, length = 45)
  private String nome;

  @Column(length = 500)
  private String descricao;

  @Column(nullable = false, length = 45)
  private String rua;

  @Column(nullable = false, length = 45)
  private String numero;

  @Column(nullable = false, length = 45)
  private String bairro;

  @Column(nullable = false, length = 45)
  private String cidade;

  @Column(nullable = false, length = 45)
  private String estado;

  @ManyToOne
  @JoinColumn(name = "segmento_loja_id", nullable = false)
  private SegmentoLoja segmentoLoja;

  @Column(nullable = false, precision = 10)
  private double longitude;

  @Column(nullable = false, precision = 10)
  private double latitude;
  @Transient
  private String image;

  @OneToMany
  @JsonManagedReference
  private List<ReviewRestaurante> reviews;

  @Transient
  private double rating;
  @Transient
  private double deliveryTime;

  public double getRating() {
    double rating = 0;
    for (ReviewRestaurante reviewRestaurante : reviews) {
      rating += reviewRestaurante.getNota();
    }
    this.rating = rating / reviews.size();
    return this.rating;
  }
  public double getDeliveryTime(double latitude, double longitude) {
    double lojaLatitude = this.latitude;
    double lojaLongitude = this.longitude;

    double earthRadius = 6371; // Radius of the earth in km
    double dLat = Math.toRadians(latitude - lojaLatitude);
    double dLon = Math.toRadians(longitude - lojaLongitude);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
      + Math.cos(Math.toRadians(lojaLatitude)) * Math.cos(Math.toRadians(latitude))
      * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = earthRadius * c; // Distance in km

    double averageBikeSpeed = 15; // Average speed of a bicycle in km/h
    double deliveryTime = distance / averageBikeSpeed * 60; // Delivery time in minutes
    this.deliveryTime = deliveryTime;
    return this.deliveryTime;
  }
}
