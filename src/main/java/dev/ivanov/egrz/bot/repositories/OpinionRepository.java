package dev.ivanov.egrz.bot.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ivanov.egrz.bot.entities.Opinion;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, String> {
}
