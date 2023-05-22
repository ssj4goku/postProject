package utilities;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomValueGenerator {

    public static String getRepositoryName() {

        return "test_repo_" + RandomStringUtils.randomAlphabetic(6);
    }

}
