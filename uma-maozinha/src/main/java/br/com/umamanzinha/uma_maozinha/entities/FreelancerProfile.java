package br.com.umamanzinha.uma_maozinha.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "freelancer_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "freelancer_profile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_fk", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_fk", nullable = false)
    private Category category;

    @Column(nullable = false, name = "average_rating")
    private Double averageRating;
}
