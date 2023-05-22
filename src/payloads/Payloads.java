package payloads;

public class Payloads {

    public static String addNewRepository(String repositoryName) {

        return "{ \"name\": \"" + repositoryName + "\", \"description\": \"Creating an example repo\" }";
    }
}
