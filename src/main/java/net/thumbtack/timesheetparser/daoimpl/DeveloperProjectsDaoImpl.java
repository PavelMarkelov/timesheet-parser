package net.thumbtack.timesheetparser.daoimpl;

import net.thumbtack.timesheetparser.dao.DeveloperProjectsDao;
import net.thumbtack.timesheetparser.dataBase.DataBase;
import net.thumbtack.timesheetparser.models.Project;
import org.springframework.stereotype.Component;

@Component
public class DeveloperProjectsDaoImpl implements DeveloperProjectsDao {

    private final DataBase database;

    public DeveloperProjectsDaoImpl(DataBase database) {
        this.database = database;
    }

    @Override
    public void save(String developerName, Project project) {
        database.addToDeveloperProjectsCollection(developerName, project);
    }
}
