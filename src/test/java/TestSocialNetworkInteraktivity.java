import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSocialNetworkInteraktivity {

    private static SocialNetworkInteractivity socialNetworkInteractivity;
    private final String observedPerson = "Escalera";

    @BeforeClass
    public static void BeforeClass() {
        socialNetworkInteractivity = new SocialNetworkInteractivity();
    }

    @AfterClass
    public static void AfterClass() {
        try {
            socialNetworkInteractivity.close();
        } catch (Exception e) {
            // Can't close socialNetworkInteractivity
        }
    }

    @Test
    public void getSortedNamesList() {
        Assert.assertNotNull(socialNetworkInteractivity.getSortedNamesList());
    }

    @Test
    public void getMalesWithAge() {
        Assert.assertNotNull(socialNetworkInteractivity.getMalesWithAge());
    }

    @Test
    public void getFriendsFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getFriendsFor(observedPerson));
    }

    @Test
    public void getFriendsFriendsFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getFriendsFriendsFor(observedPerson));
    }

    @Test
    public void getFriendsCount() {
        Assert.assertNotNull(socialNetworkInteractivity.getFriendsCount());
    }

    @Test
    public void getSocialNetworks() {
        Assert.assertNotNull(socialNetworkInteractivity.getSocialNetworks());
    }

    @Test
    public void getSocialNetworksFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getSocialNetworksFor(observedPerson));
    }

    @Test
    public void getSocialNetworksWithUsersCount() {
        Assert.assertNotNull(socialNetworkInteractivity.getSocialNetworksWithUsersCount());
    }

    @Test
    public void getUsersWithSocialNetworksCount() {
        Assert.assertNotNull(socialNetworkInteractivity.getUsersWithSocialNetworksCount());
    }

    @Test
    public void getFriendsSocialNetworksCountFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getFriendsSocialNetworksCountFor(observedPerson));
    }

    @Test
    public void getTweetsFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getTweetsFor(observedPerson));
    }

    @Test
    public void getTweetsAverageLengthForUsers() {
        Assert.assertNotNull(socialNetworkInteractivity.getTweetsAverageLengthForUsers());
    }

    @Test
    public void getUsersWithTweets() {
        Assert.assertNotNull(socialNetworkInteractivity.getUsersWithTweets());
    }

    @Test
    public void getFriendsFriendsTweetsFor() {
        Assert.assertNotNull(socialNetworkInteractivity.getFriendsFriendsTweetsFor(observedPerson));
    }
}
