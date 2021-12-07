package com.pb.mockproxy.repository.project;

import com.pb.mockproxy.repository.rule.Rule;
import com.pb.mockproxy.repository.util.UpdateUtil;
import org.bson.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

class ProjectRepositoryImplTest {

    private UpdateUtil updateUtil;

    @BeforeEach
    public void init() {
        updateUtil = new UpdateUtil();
    }

    @Test
    void updateUtilTest() {

        Update updateProject = updateUtil.createUpdateFromObject(getProject());
        Assertions.notNull("update", updateProject);

        Update updateRule = updateUtil.createUpdateFromObject(getRule());
        Assertions.notNull("update", updateRule);
    }

    private Project getProject() {

        List<User> users = new ArrayList<>();
        users.add(new User("login1", "name1", "photoUrl1"));

        Project project = new Project();
        project.setId("12345");
        project.setName("ProjectName");
        project.setUsers(users);
        return project;
    }

    private Rule getRule() {
        Rule rule = new Rule();
        rule.setId("12345");
        rule.setName("RuleName");
        rule.setProjectId("ProjectId");
        rule.setDescription("ProjectDescription");
        return rule;
    }
}