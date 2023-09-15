package com.runapp.shoesservice.repository;

import com.runapp.shoesservice.model.ShoesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesRepository extends JpaRepository<ShoesModel, Long> {
}
