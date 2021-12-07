package com.pb.mockproxy.repository.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.mockproxy.repository.rule.Rule;
import com.pb.mockproxy.repository.rule.RuleState;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class MockRepositoryImpl implements MockRepository {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public MockRepositoryImpl(MongoTemplate mongoTemplate, ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mock insert(Mock mock) {
        return mongoTemplate.insert(mock);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Mock update(Mock mock) {
        Map<String, Object> map = objectMapper.convertValue(mock, Map.class);
        map.values().removeIf(Objects::isNull);

        Update update = new Update();
        map.forEach(update::set);
        Query query = new Query().addCriteria(Criteria.where("id").is(mock.getId()));

        mongoTemplate.update(Mock.class).matching(query).apply(update).first();
        return getById(mock.getId());
    }

    @Override
    public Mock getById(String mockId) {
        Query query = Query.query(Criteria.where("id").is(mockId).and("state").is(MockState.ACTIVE));
        return mongoTemplate.findOne(query, Mock.class);
    }

    @Override
    public List<Mock> getByProjectId(String projectId) {
        return mongoTemplate.find(
                Query.query(
                        Criteria.where("projectId").is(projectId)
                                .and("state").is(MockState.ACTIVE)
                ),
                Mock.class
        );
    }

    @Override
    public Mock remove(String mockId) {
        Query query = new Query(Criteria.where("id").is(mockId));
        Update update = new Update().set("state", MockState.DELETED);
        mongoTemplate.updateFirst(query, update, Mock.class);
        return getById(mockId);
    }
}
