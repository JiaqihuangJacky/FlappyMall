package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * this file is used to deal with the user service
 * it include all the implementation of the user services
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password) {

        /*using mysql to obtain the current user by name*/
        int resultCount = userMapper.checkUsername(username);

        /*indiating that there are no such use*/
        if(resultCount == 0 ){
            return ServerResponse.createByErrorMessage("The user does not exist");
        }

        /*check if the password is correct, we will use the password after encoding*/
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("Password is incorrect");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess("Log in Success",user);
    }



    public ServerResponse<String> register(User user){
        //check if the user have exist in the database
        //return the response message as result
        //we use the useer name as the validation
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        //indicating that this is the customer not the admin staff
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5 encryption; it will let the database set the specific codes for password
        //other people will not knwo the password in the database.
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //inserting the new user after we set up the name and password
        int resultCount = userMapper.insert(user);
        //0 means fail and 1 means success
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("Registration fail");
        }
        //return a server's response as we register successfully
        return ServerResponse.createBySuccessMessage("Registration success");
    }

    public ServerResponse<String> checkValid(String str,String type){

        //we need to void the " " case, since it is invalid and usefuless
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //start to verify the user
            //check the username
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("User has existed");
                }
            }
            //check the email
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email has existed");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("Parameter Error");
        }
        //if success, return the server response
        return ServerResponse.createBySuccessMessage("Verify Success");
    }

    //select the question to answeer
    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //user does not exist
            return ServerResponse.createByErrorMessage("User does not exist");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("Question of finding password is empty");
    }

    //check the user's answer
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //indicates that the answer of the user is correct.
            String forgetToken = UUID.randomUUID().toString();
            //local cache
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            //assign the forage token into the parameter
            return ServerResponse.createBySuccess(forgetToken);
        }
        //create an error message
        return ServerResponse.createByErrorMessage("Answer is incorrect");
    }


    //forget the password, and reset it
    public ServerResponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        //we can get a blank token, but this is invalid, and incorrect, we will have to reject it
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("Parameter is incorrect,token need to be passed");
        }
        //check if we have the correct user
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //user does not exist
            return ServerResponse.createByErrorMessage("User does not exist");
        }
        //check if the token is correct by the username value
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        //check if the token is empty, if empty then display error message
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token is invalid and incorrect or expire");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            //get the new password
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            //pass the userMap to get the count
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            //check the count status
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("Change password successfully");
            }
        }else{
            return ServerResponse.createByErrorMessage("token is incorrect,please re-get the new password's token");
        }
        return ServerResponse.createByErrorMessage("Change password fail");
    }

    //reset the password when the user is in the log status
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //Avoid the user violate the right of the other users, it must be the current user, since we will
        //check the count(1), if not the corresponing id, then count > 0 is true;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("Old password is incorrect");
        }

        //update the new password
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponse.createBySuccessMessage("Password update successfully");
        }
        return ServerResponse.createByErrorMessage("Password update fail");
    }

    //update the user's information
    public ServerResponse<User> updateInformation(User user){
        //username cannot be update
        //we need to check the email, so that we can verify if the new email is there
        //if the email is the same, then we cannot use this user. It cannot be the current user
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponse.createByErrorMessage("email has existed, please change to new email");
        }
        //create a new user and set up all of its information
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        //update the count in the map, only update if this is not null
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("Update personal information successfully",updateUser);
        }
        return ServerResponse.createByErrorMessage("Update personal information fail");
    }



    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("Cannot find the current User");
        }
        //set the password to empty
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        //success message
        return ServerResponse.createBySuccess(user);
    }




    //backend

    /**
     * check if adminstrator
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }



}
