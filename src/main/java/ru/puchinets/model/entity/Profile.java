package ru.puchinets.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "user_id")
    private Long id;
    @OneToOne
    @PrimaryKeyJoinColumn
    @ToString.Exclude
    private User user;
    private String email;
    private String firstname;
    @Column(name = "middle_name")
    private String middleName;
    private String lastname;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    private String phone;
    private String photo;
}
