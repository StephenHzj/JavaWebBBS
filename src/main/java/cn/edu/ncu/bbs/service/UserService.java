package cn.edu.ncu.bbs.service;

import cn.edu.ncu.bbs.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService  {
    User findById(String id);
    Integer userRegister(User user);
    void deleteUser(Integer userId);
    void userChange(User user, MultipartFile file);
    User changeIntegral(String type,Integer userId,Integer changeIntegral);
    User selectUserById(Integer userId);

}
