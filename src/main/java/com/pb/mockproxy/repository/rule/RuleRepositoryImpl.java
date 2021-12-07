package com.pb.mockproxy.repository.rule;

import com.pb.mockproxy.repository.util.UpdateUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RuleRepositoryImpl implements RuleRepository {

    private static final String ID = "id";
    private static final String STATE = "state";
    private static final String PROJECT_ID = "projectId";

    private final MongoTemplate mongoTemplate;
    private final UpdateUtil updateUtil;

    public RuleRepositoryImpl(MongoTemplate mongoTemplate, UpdateUtil updateUtil) {
        this.mongoTemplate = mongoTemplate;
        this.updateUtil = updateUtil;
    }

    @Override
    public Rule insert(Rule rule) {
        return mongoTemplate.insert(rule);
    }

    @Override
    public Rule update(Rule rule) {
        Query query = new Query(Criteria.where(ID).is(rule.getId()));
        Update update = updateUtil.createUpdateFromObject(rule);
        mongoTemplate.updateFirst(query, update, Rule.class);
        return get(rule.getId());
    }

    @Override
    public Rule updateState(String ruleId, String state) {
        Query query = new Query(Criteria.where(ID).is(ruleId));
        Update update = new Update().set(STATE, state);
        mongoTemplate.updateFirst(query, update, Rule.class);
        return get(ruleId);
    }

    @Override
    public Rule remove(String ruleId) {
        return updateState(ruleId, RuleState.DELETED);
    }

    @Override
    public List<Rule> getAllRules(String projectId) {
        Query query = new Query(Criteria.where(STATE).in(RuleState.ACTIVE, RuleState.DISABLED).and(PROJECT_ID).is(projectId));
        return mongoTemplate.find(query, Rule.class);
    }

    @Override
    public List<Rule> getActiveRules(String projectId) {
        Query query = new Query(Criteria.where(PROJECT_ID).is(projectId).and(STATE).is(RuleState.ACTIVE));
        return mongoTemplate.find(query, Rule.class);
    }

    @Override
    public Rule get(String ruleId) {
        return mongoTemplate.findOne(Query.query(Criteria.where(ID).is(ruleId)), Rule.class);
    }
}
