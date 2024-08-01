package com.steadon.saber.service.impl;

import com.steadon.saber.model.dao.Log;
import com.steadon.saber.model.result.LogHistogramResult;
import com.steadon.saber.service.LogService;
import com.steadon.saber.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.steadon.saber.common.LogField.*;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Log> getLogList(String keyword, String startLogId, String endLogId, Long startTime, Long endTime, String level) {
        Query query = new Query();

        // 设置日志等级条件
        Criteria criteria = switch (level.toUpperCase()) {
            case WARN -> Criteria.where(LEVEL).in(WARN, ERROR);
            case ERROR -> Criteria.where(LEVEL).in(ERROR);
            default -> Criteria.where(LEVEL).in(INFO, WARN, ERROR);
        };

        // 设置_id或时间检索条件
        if (StringUtils.isNotEmpty(startLogId)) {
            query.addCriteria(Criteria.where("_id").gt(new ObjectId(startLogId)));
            query.with(Sort.by(Sort.Direction.ASC, "_id"));
        } else if (StringUtils.isNotEmpty(endLogId)) {
            query.addCriteria(Criteria.where("_id").lt(new ObjectId(endLogId)));
            query.with(Sort.by(Sort.Direction.DESC, "_id"));
        } else if (startTime != null) {
            query.addCriteria(Criteria.where("timestamp").gte(startTime).lte(endTime));
            query.with(Sort.by(Sort.Direction.DESC, "_id"));
        } else if (endTime != null) {
            query.addCriteria(Criteria.where("timestamp").lte(endTime));
            query.with(Sort.by(Sort.Direction.DESC, "_id"));
        }

        // 设置tid或message检索条件
        if (StringUtils.isNotEmpty(keyword)) {
            Criteria messageCriteria = Criteria.where("message").regex(keyword, "i");
            Criteria traceIdCriteria = Criteria.where("traceId").regex(keyword, "i");
            criteria.orOperator(messageCriteria, traceIdCriteria);
        }

        query.addCriteria(criteria).limit(20);

        List<Log> logs = mongoTemplate.find(query, Log.class);
        if (StringUtils.isNotEmpty(startLogId)) {
            Collections.reverse(logs);
        }
        return logs;
    }

    @Override
    public LogHistogramResult getHistogramByTimeStamp(Long timeStamp) {
        long currentHour = timeStamp / 3600000 * 3600000;
        long oneHourBefore = currentHour - 3600000;
        // 创建匹配条件
        MatchOperation matchOperation = Aggregation.match(Criteria.where("timestamp").lte(currentHour).gt(oneHourBefore));
        Aggregation aggregation = Aggregation.newAggregation(
                matchOperation,
                Aggregation.group("level").count().as("count"),
                Aggregation.project("count").and("level").previousOperation()
        );
        // 执行聚合查询
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "log", Document.class);
        // 直接打印结果
        LogHistogramResult logHistogramResult = new LogHistogramResult();
        results.getMappedResults().forEach(
                doc -> {
                    String level = doc.getString("level");
                    int count = doc.getInteger("count");
                    switch (level) {
                        case "INFO" -> logHistogramResult.setInfoCount(count);
                        case "WARN" -> logHistogramResult.setWarnCount(count);
                        case "ERROR" -> logHistogramResult.setErrorCount(count);
                    }
                }
        );
        return logHistogramResult;
    }
}
