package com.example.springaventure.model.dao

import com.example.springaventure.model.entity.Qualite
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface QualiteDao :JpaRepository<Qualite,Long> {

    @Query("select q from Qualite q") // => Pour la pagination
    fun findAll1(pageable: Pageable): Page<Qualite>
}