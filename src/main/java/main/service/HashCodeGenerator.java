package main.service;

import java.util.Random;

public class HashCodeGenerator {

    public HashCodeGenerator() {
    }

    public String generate(int minimumLength, int maximumLength) {

        int randomLength = (int) (Math.random() * ((maximumLength - minimumLength) + 1)) + minimumLength;

        System.out.println(randomLength);

        String[] charCategories = new String[] {
                "abcdefghijklmnopqrstuvwxyz",
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                "0123456789"
        };
        StringBuilder code = new StringBuilder(randomLength);
        Random random = new Random(System.nanoTime());

        for (int i = 0; i < randomLength; i++) {
            String charCategory = charCategories[random.nextInt(charCategories.length)];
            int position = random.nextInt(charCategory.length());
            code.append(charCategory.charAt(position));
        }

        return new String(code);
    }

}
