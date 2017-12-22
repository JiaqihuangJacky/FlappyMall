package com.mmall.dao;

/**
 * this file is create for the user mapping
 * this interface contains all the functions that we use
 * to manage the users
 */

import com.mmall.pojo.User;

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

    int checkUsername(String username);
}