package com.mmall.dao;

/**
 * this file is create for the user mapping
 * this interface contains all the functions that we use
 * to manage the users
 */

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
public interface UserMapper {

    //delete the primary key function
    int deleteByPrimaryKey(Integer id);

    //insert the new user into the reocrd
    int insert(User record);

    //insert the new user into the record by selection
    int insertSelective(User record);

    //choose a user object via their ids
    User selectByPrimaryKey(Integer id);

    //update the use record via selection
    int updateByPrimaryKeySelective(User record);

    //update by their primary key
    int updateByPrimaryKey(User record);

    //verify the user's name
    int checkUsername(String username);

    //verify the user's email
    int checkEmail(String email);

    //check the user login status
    User selectLogin(@Param("username") String username, @Param("password")String password);

    String selectQuestionByUsername(String username);

    int checkAnswer(@Param("username")String username, @Param("question")String question, @Param("answer")String answer);

    int updatePasswordByUsername(@Param("username")String username, @Param("passwordNew")String passwordNew);

    int checkPassword(@Param(value="password")String password, @Param("userId")Integer userId);

    int checkEmailByUserId(@Param(value="email")String email, @Param(value="userId")Integer userId);
}