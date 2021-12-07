package com.pb.mockproxy.repository.rule;

import java.util.List;

public interface RuleRepository {

    Rule insert(Rule rule);
    Rule update(Rule rule);
    Rule updateState(String ruleId, String state);
    Rule get(String ruleId);
    List<Rule> getAllRules(String projectId);
    List<Rule> getActiveRules(String projectId);
    Rule remove(String ruleId);
}
