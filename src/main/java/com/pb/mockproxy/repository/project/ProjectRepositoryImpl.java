package com.pb.mockproxy.repository.project;

import com.pb.mockproxy.bean.auth.role.Roles;
import com.pb.mockproxy.bean.auth.role.UserData;
import com.pb.mockproxy.repository.util.UpdateUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final MongoTemplate mongoTemplate;
    private final UpdateUtil updateUtil;

    public ProjectRepositoryImpl(MongoTemplate mongoTemplate, UpdateUtil updateUtil) {
        this.mongoTemplate = mongoTemplate;
        this.updateUtil = updateUtil;
    }

    @Override
    public Project insert(Project project) {
        return mongoTemplate.insert(project);
    }

    @Override
    public Project update(Project project) {
        Query query = new Query(Criteria.where("id").is(project.getId()));
        Update update = updateUtil.createUpdateFromObject(project);
        mongoTemplate.updateFirst(query, update, Project.class);
        return get(project.getId());
    }

    @Override
    public Project get(String projectId) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(projectId)), Project.class);
    }

    @Override
    public Project getActiveById(String projectId) {
        return mongoTemplate.findOne(
                new Query(Criteria.where("id").is(projectId).and("state").is(ProjectState.ACTIVE)),
                Project.class
        );
    }

    @Override
    public Project get(String projectId, UserData userData) {
        Criteria criteria = getOneProjectQueryByRole(projectId, userData);
        return criteria == null ? null : mongoTemplate.findOne(new Query(criteria), Project.class);
    }

    @Override
    public List<Project> getAllProjects(UserData userData) {
        Criteria criteria = getAllProjectsQueryByRole(userData);
        return criteria == null ? new ArrayList<>() : mongoTemplate.find(new Query(criteria), Project.class);
    }

    @Override
    public Project remove(String projectId) {
        Query query = new Query(Criteria.where("id").is(projectId));
        Update update = new Update().set("state", ProjectState.DELETED);
        mongoTemplate.updateFirst(query, update, Project.class);
        return get(projectId);
    }

    @Override
    public Project addUserToProject(String projectId, User user) {
        Query query = new Query(Criteria.where("id").is(projectId));
        Update update = new Update().addToSet("users", user);
        mongoTemplate.updateFirst(query, update, Project.class);
        return get(projectId);
    }

    @Override
    public Project removeUserFromProject(String projectId, String login) {
        Query query = new Query(Criteria.where("id").is(projectId));
        Update update = new Update().pull("users", Query.query(Criteria.where("login").is(login)));
        mongoTemplate.updateFirst(query, update, Project.class);
        return get(projectId);
    }

    protected Criteria getOneProjectQueryByRole(String projectId, UserData userData) {
        if (userData == null || userData.getRoles() == null) {
            return null;
        }
        if (userData.getRoles().contains(Roles.RA)) {
            return Criteria.where("id").is(projectId);
        }
        if (userData.getRoles().contains(Roles.RR) || userData.getRoles().contains(Roles.RW)) {
            return Criteria.where("id").is(projectId).and("users.login").is(userData.getLogin());
        }
        return null;
    }

    protected Criteria getAllProjectsQueryByRole(UserData userData) {
        if (userData == null || userData.getRoles() == null) {
            return null;
        }
        if (userData.getRoles().contains(Roles.RA)) {
            return Criteria.where("state").is(ProjectState.ACTIVE);
        }
        if (userData.getRoles().contains(Roles.RR) || userData.getRoles().contains(Roles.RW)) {
            return Criteria.where("state").is(ProjectState.ACTIVE).and("users.login").is(userData.getLogin());
        }
        return null;
    }
}
