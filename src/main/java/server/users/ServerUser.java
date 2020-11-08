package server.users;

import server.database.entity.User;

import static server.users.UserStatement.NO_STATEMENT;

public class ServerUser {
    private User databaseUser;
    private UserStatement statement = NO_STATEMENT;

    public User getDatabaseUser() {
        return databaseUser;
    }

    public void setDatabaseUser(User databaseUser) {
        this.databaseUser = databaseUser;
    }

    public UserStatement getStatement() {
        return statement;
    }

    public void setStatement(UserStatement statement) {
        this.statement = statement;
    }
}
