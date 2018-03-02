package com.seeu.artshow.globalconfig.repository;

import com.seeu.artshow.globalconfig.model.GlobalConfigurer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigurerRepository extends JpaRepository<GlobalConfigurer, String> {
}
