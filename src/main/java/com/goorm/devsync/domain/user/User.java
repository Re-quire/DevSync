package com.goorm.devsync.domain.user;

import com.goorm.devsync.domain.Inquiries;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String name;

   @Builder
   public User(String name) {
      this.name = name;
   }

}
