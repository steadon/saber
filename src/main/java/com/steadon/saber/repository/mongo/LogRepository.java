package com.steadon.saber.repository.mongo;

import com.steadon.saber.model.dao.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogRepository extends MongoRepository<Log, Long> {
    List<Log> findByTraceId(String traceId);
}
