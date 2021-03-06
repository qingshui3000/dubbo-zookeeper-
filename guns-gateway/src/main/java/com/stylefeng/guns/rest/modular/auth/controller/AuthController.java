package com.stylefeng.guns.rest.modular.auth.controller;

import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Reference(interfaceClass = UserAPI.class,check = false)
    private UserAPI userAPI;
    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVO<?> createAuthenticationToken(AuthRequest authRequest) {
        boolean validate = true;
        System.out.println("this instance is " + userAPI);
        int userId = userAPI.login(authRequest.getUserName(),authRequest.getPassword());
        if(userId == 0) validate = false;
        if (validate) {
            //生成randomKey和token
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(""+userId, randomKey);
            return ResponseVO.success(new AuthResponse(token,randomKey));
        } else {
            return ResponseVO.serviceFail("用户名或密码错误");
        }
    }
}
