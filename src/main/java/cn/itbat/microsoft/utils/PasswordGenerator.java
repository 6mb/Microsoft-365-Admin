package cn.itbat.microsoft.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author mjj
 * @date 2020年05月28日 16:37:46
 */

public class PasswordGenerator {
    /**
     * 密码能包含的特殊字符
     */
    public static final char[] ALLOWED_SPECIAL_CHARACTORS = {
            '`', '~', '@', '#', '$', '%', '^', '&',
            '*', '(', ')', '-', '_', '=', '+', '[',
            '{', '}', ']', '\\', '|', ';', ':', '"',
            '\'', ',', '<', '.', '>', '/', '?'};
    private static final int LETTER_RANGE = 26;
    private static final int NUMBER_RANGE = 10;
    private static final Random RANDOM = new Random();
    /**
     * 密码的长度
     */
    private int passwordLength;
    /**
     * 密码包含字符的最少种类
     */
    private int minVariousType;

    public PasswordGenerator(int passwordLength, int minVariousType) {
        if (minVariousType > CharacterType.values().length) {
            minVariousType = CharacterType.values().length;
        }
        if (minVariousType > passwordLength) {
            minVariousType = passwordLength;
        }
        this.passwordLength = passwordLength;
        this.minVariousType = minVariousType;
    }

    public String generateRandomPassword() {
        char[] password = new char[passwordLength];
        List<Integer> pwCharsIndex = new ArrayList();
        for (int i = 0; i < password.length; i++) {
            pwCharsIndex.add(i);
        }
        List<CharacterType> takeTypes = new ArrayList(Arrays.asList(CharacterType.values()));
        List<CharacterType> fixedTypes = Arrays.asList(CharacterType.values());
        int typeCount = 0;
        while (pwCharsIndex.size() > 0) {
            //随机填充一位密码
            int pwIndex = pwCharsIndex.remove(RANDOM.nextInt(pwCharsIndex.size()));
            Character c;
            //生成不同种类字符
            if (typeCount < minVariousType) {
                c = generateCharacter(takeTypes.remove(RANDOM.nextInt(takeTypes.size())));
                typeCount++;
            } else {
                //随机生成所有种类密码
                c = generateCharacter(fixedTypes.get(RANDOM.nextInt(fixedTypes.size())));
            }
            password[pwIndex] = c;
        }
        return String.valueOf(password);
    }

    private Character generateCharacter(CharacterType type) {
        Character c = null;
        int rand;
        switch (type) {
            //随机小写字母
            case LOWERCASE:
                rand = RANDOM.nextInt(LETTER_RANGE);
                rand += 97;
                c = (char) rand;
                break;
            //随机大写字母
            case UPPERCASE:
                rand = RANDOM.nextInt(LETTER_RANGE);
                rand += 65;
                c = (char) rand;
                break;
            //随机数字
            case NUMBER:
                rand = RANDOM.nextInt(NUMBER_RANGE);
                rand += 48;
                c = (char) rand;
                break;
            default:
        }
        return c;
    }
}

enum CharacterType {
    /**
     * LOWERCASE
     */
    LOWERCASE,
    /**
     * UPPERCASE
     */
    UPPERCASE,
    /**
     * NUMBER
     */
    NUMBER
}
