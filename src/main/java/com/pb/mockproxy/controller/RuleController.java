package com.pb.mockproxy.controller;

import com.pb.mockproxy.repository.rule.Rule;
import com.pb.mockproxy.repository.rule.RuleRepository;
import com.pb.mockproxy.repository.rule.RuleState;
import com.pb.mockproxy.validator.ProjectIdExistConstraint;
import com.pb.mockproxy.validator.RuleIdExistConstraint;
import com.pb.mockproxy.validator.RuleStateUpdateConstraint;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rule")
@Validated
public class RuleController {

    private final RuleRepository ruleRepository;

    public RuleController(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @GetMapping("all/{projectId}")
    public List<Rule> getAllRules(@PathVariable @ProjectIdExistConstraint String projectId) {
        return ruleRepository.getAllRules(projectId);
    }

    @GetMapping("{ruleId}")
    public Rule getRule(@PathVariable @RuleIdExistConstraint String ruleId) {
        return ruleRepository.get(ruleId);
    }

    @PostMapping
    public Rule insertRule(@Validated(Rule.Create.class) @RequestBody Rule rule) {
        rule.setState(RuleState.ACTIVE);
        if (rule.getTimeWait() == null) {
            rule.setTimeWait(0L);
        }
        return ruleRepository.insert(rule);
    }

    @PutMapping
    public Rule updateRule(@RequestBody @Validated(Rule.Update.class) Rule rule) {
        return ruleRepository.update(rule);
    }

    @PutMapping("{ruleId}/state/{state}")
    public Rule updateRuleState(@PathVariable @RuleIdExistConstraint String ruleId,
                                @PathVariable @RuleStateUpdateConstraint String state) {
        return ruleRepository.updateState(ruleId, state);
    }

    @DeleteMapping("{ruleId}")
    public Rule removeRule(@PathVariable @RuleIdExistConstraint String ruleId) {
        return ruleRepository.remove(ruleId);
    }
}
