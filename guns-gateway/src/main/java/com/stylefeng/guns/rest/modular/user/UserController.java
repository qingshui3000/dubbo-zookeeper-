package com.stylefeng.guns.rest.modular.user;

import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserController {
    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;
    @PostMapping("register")
    public ResponseVO register(UserModel userModel){
        if(userModel.getUsername() == null || userModel.getUsername().trim().length() == 0){
            return ResponseVO.serviceFail("用户名不能为空！");
        }
        if (userModel.getPassword() == null || userModel.getPassword().trim().length() == 0) {
            return  ResponseVO.serviceFail("密码不能为空！");
        }
        boolean result = userAPI.registor(userModel);
        if(result){
            return ResponseVO.success("注册成功！");
        }
        return ResponseVO.serviceFail("注册失败！");
    }

    @PostMapping("check")
    public ResponseVO check(String userName){
        System.out.println("this instance is " + userAPI);
        if(userName == null || userName.trim().length() == 0){
            return ResponseVO.serviceFail("用户名不能为空！");
        }
        boolean isExist = userAPI.checkUsername(userName);
        if(isExist){
            return ResponseVO.success("用户名可用");
        }
        return ResponseVO.serviceFail("用户名已存在！");
    }

    @GetMapping("logout")
    public ResponseVO logout(){
        return ResponseVO.success("用户退出登录成功！");
    }

    @GetMapping("/getUserInfo")
    public ResponseVO getUserInfo(){
        String uuid = CurrentUser.getCurrentUserId();
        if(uuid == null || uuid.trim().length() == 0){
            return ResponseVO.serviceFail("用户未登录");
        }
        UserInfoModel userInfo = userAPI.getUserInfo(Integer.parseInt(uuid));
        if(userInfo == null){
            return ResponseVO.appFail("用户信息查询失败！");
        }
        return ResponseVO.success(userInfo);
    }

    @PostMapping("/updateUserInfo")
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel){
        String userId = CurrentUser.getCurrentUserId();
        if(userId == null || userId.trim().length() == 0){
            return ResponseVO.serviceFail("用户未登录");
        }
        int uuid = Integer.parseInt(CurrentUser.getCurrentUserId());
        if(userInfoModel.getUuid() != uuid){
            return ResponseVO.serviceFail("请修改自己的信息！");
        }
        UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
        if(userInfo == null){
            return ResponseVO.appFail("用户信息查询失败！");
        }
        return ResponseVO.success(userInfo);
    }


}
