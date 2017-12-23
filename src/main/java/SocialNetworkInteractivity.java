import org.neo4j.driver.v1.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;

public class SocialNetworkInteractivity implements AutoCloseable{

    private final Driver driver;

    public SocialNetworkInteractivity() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("", ""));
        executeInitialScript();
    }

    private List<Record> executeStatement(String statement) {
        try ( Session session = driver.session() )
        {
            // Execute transaction on current session
            return session.writeTransaction(work -> {

                // Prepare statement for execution and execute it
                StatementResult statementResult = work.run(
                        statement
                );

                // Exhaust the result
                // Cast result to list
                return statementResult.list();
            });
        }
    }

    private void executeInitialScript() {
        try {
            // Read initial script from initialScript.dat
            URI uriToInitialSqlScript = getClass().getResource("initialScript.dat").toURI();
            String initialScriptContent = new String(Files.readAllBytes(Paths.get(uriToInitialSqlScript)));

            // Delete all existing
            executeStatement("MATCH (n) DETACH DELETE n");

            // Execute initial script
            executeStatement(initialScriptContent);

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Record> getSortedNamesList() {
        String statement = "MATCH (person:Person) RETURN person.surname, " +
                "person.name ORDER BY person.surname, person.name";

        return executeStatement(statement);
    }
    public List<Record> getMalesWithAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String statement = String.format("MATCH (person:Person) WHERE person.gender=1 " +
                "RETURN person.surname, person.name, %d-person.birthdate as age " +
                "ORDER BY age, person.surname, person.name", currentYear);

        return executeStatement(statement);
    }
    public List<Record> getFriendsFor(String login) {
        String statement = String.format("MATCH (person:Person)-[:FRIENDS]-(friends) " +
                "WHERE person.surname=\"%s\" RETURN friends.surname, friends.name", login);

        return executeStatement(statement);
    }
    public List<Record> getFriendsFriendsFor(String login) {
        String statement = String.format("MATCH (person:Person)-[:FRIENDS]-(firstFriends)-" +
                "[:FRIENDS]-(secondFriends) WHERE person.surname=\"%s\" " +
                "RETURN DISTINCT secondFriends.surname, secondFriends.name", login);

        return executeStatement(statement);
    }
    public List<Record> getFriendsCount() {
        String statement = "MATCH (person:Person)-[:FRIENDS]-(friends) " +
                "RETURN person.surname, count(friends) as friendsCount ORDER BY friendsCount";

        return executeStatement(statement);
    }
    public List<Record> getSocialNetworks() {
        String statement = "MATCH (group:Group) RETURN group.groupname ORDER BY group.groupname";

        return executeStatement(statement);
    }
    public List<Record> getSocialNetworksFor(String login) {
        String statement = String.format("MATCH (person:Person)-[:IN_GROUP]-(groups) WHERE " +
                "person.surname=\"%s\" RETURN person.surname, " +
                "groups.groupname ORDER BY groups.groupname", login);

        return executeStatement(statement);
    }
    public List<Record> getSocialNetworksWithUsersCount() {
        String statement = "MATCH (group:Group)-[:IN_GROUP]-(person) RETURN " +
                "group.groupname, count(person) as personsCount ORDER BY personsCount DESC";

        return executeStatement(statement);
    }
    public List<Record> getUsersWithSocialNetworksCount() {
        String statement = "MATCH (person:Person)-[:IN_GROUP]-(groups) RETURN person.surname, " +
                "count(groups) as groupsCount ORDER BY groupsCount DESC";

        return executeStatement(statement);
    }
    public List<Record> getFriendsSocialNetworksCountFor(String login) {
        String statement = String.format("MATCH (person:Person)-[:FRIENDS]-(firstFriends)-[:FRIENDS]-(secondFriends)-[:IN_GROUP]-" +
                "(groups) WHERE person.surname=\"%s\" RETURN " +
                "count(DISTINCT groups.groupname)", login);

        return executeStatement(statement);
    }

    public List<Record> getTweetsFor(String login) {
        String statement = String.format("MATCH (person:Person) WHERE person.surname=\"%s\" RETURN person.tweets", login);

        return executeStatement(statement);
    }
    public List<Record> getTweetsAverageLengthForUsers() {
        String statement = "MATCH (person:Person) WITH " +
                "person.surname as surname, " +
                "EXTRACT(tweet in person.tweets | size(tweet)) as tweetsSize " +
                "UNWIND tweetsSize as tweetsSizeC " +
                "RETURN surname, AVG(tweetsSizeC) ";

        return executeStatement(statement);
    }
    public List<Record> getTweetsWithLengthMore(int length) {
        String statement = String.format("MATCH (person:Person) WITH COLLECT(person.tweets) as tweets RETURN " +
                "filter(x IN REDUCE(out=[], r IN tweets | out + r) WHERE size(x)>%d)", length);

        return executeStatement(statement);
    }
    public List<Record> getUsersWithTweets() {
        String statement = "MATCH (person:Person) RETURN person.surname, " +
                "size(person.tweets) as tweetsCount ORDER BY tweetsCount DESC";

        return executeStatement(statement);
    }
    public List<Record> getFriendsFriendsTweetsFor(String login) {
        String statement = String.format("MATCH (person:Person)-[:FRIENDS]-(firstFriends)-[:FRIENDS]-" +
                "(secondFriends) WHERE person.surname=\"%s\" RETURN DISTINCT secondFriends.tweets", login);

        return executeStatement(statement);
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}
