package com.runapp.shoesservice.repository;

import com.runapp.shoesservice.model.DefaultShoesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultShoesRepository extends JpaRepository<DefaultShoesModel, Long> {
}
