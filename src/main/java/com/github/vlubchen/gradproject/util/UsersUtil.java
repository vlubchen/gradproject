package com.github.vlubchen.gradproject.util;

import com.github.vlubchen.gradproject.to.UserTo;
import lombok.experimental.UtilityClass;
import com.github.vlubchen.gradproject.model.Role;
import com.github.vlubchen.gradproject.model.User;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }
}