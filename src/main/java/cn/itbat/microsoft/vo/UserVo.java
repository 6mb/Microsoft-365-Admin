package cn.itbat.microsoft.vo;

import lombok.Data;

/**
 * @author mjj
 * @date 2020年05月28日 16:46:28
 */
@Data
public class UserVo {
    private String id;
    private Boolean accountEnabled;
    private String name;
    private String surname;
    private String gender;
    private String region;
    private int age;
    private String title;
    private String phone;
    private String email;
    private String password;
    private String photo;

}