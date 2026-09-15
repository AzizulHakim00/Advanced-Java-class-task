package com.azizul.azizul.repository;

import com.azizul.azizul.model.Mission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepo extends MongoRepository<Mission, String> {

}