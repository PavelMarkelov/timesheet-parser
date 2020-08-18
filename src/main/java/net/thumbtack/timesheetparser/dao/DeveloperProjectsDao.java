package net.thumbtack.timesheetparser.dao;

import net.thumbtack.timesheetparser.models.Project;

public interface DeveloperProjectsDao {
    void save(String developerName, Project project);
}
