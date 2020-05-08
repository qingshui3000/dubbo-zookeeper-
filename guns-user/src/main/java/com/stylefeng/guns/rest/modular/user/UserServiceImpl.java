package com.stylefeng.guns.rest.modular.user;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements UserAPI {

    @Autowired
    private MoocUserTMapper moocUserTMapper;
    @Override
    public int login(String username, String password) {
        //根据用户名去数据库查找
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT result = moocUserTMapper.selectOne(moocUserT);
        //对查找结果做检验
        if(result != null && result.getUuid() > 0){
            String MD5Pwd = MD5Util.encrypt(password);
            if(MD5Pwd.equals(result.getUserPwd())){
                return result.getUuid();
            }
        }
        return 0;
    }

    @Override
    public boolean registor(UserModel userModel) {
        //构建数据实体
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());
        //密码加密
        String md5Pwd = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(md5Pwd);
        //存入数据库
        int insert = moocUserTMapper.insert(moocUserT);
        if(insert > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsername(String username) {
        //根据用户名做匹配
        EntityWrapper<MoocUserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name",username);
        //对匹配结果校验
        Integer result = moocUserTMapper.selectCount(entityWrapper);
        if(result != null && result > 0){
            return false;
        }
        return true;
    }
    private UserInfoModel do2UserInfo(MoocUserT moocUserT){
        UserInfoModel userInfoModel = new UserInfoModel();
        userInfoModel.setUsername(moocUserT.getUserName());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime().getTime());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setLifeState(""+moocUserT.getLifeState());
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setBeginTime(moocUserT.getBeginTime().getTime());
        userInfoModel.setAddress(moocUserT.getAddress());
        return userInfoModel;
    }
    private Date time2Date(long time){
        return new Date(time);
    }
    @Override
    public UserInfoModel getUserInfo(int uuid) {
        //根据主键查询用户信息
        MoocUserT moocUserT = moocUserTMapper.selectById(uuid);
        //将MoocUserT转换为xxInfo并返回
        UserInfoModel userInfoModel = this.do2UserInfo(moocUserT);
        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        //将传入的数据转换为MoocUserT
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(null);
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(null);
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        //更新数据库记录
        Integer count = moocUserTMapper.updateById(moocUserT);
        if(count > 0){
            //按照id查出更新记录
            UserInfoModel userInfo = getUserInfo(moocUserT.getUuid());
            return userInfo;
        }
        return null;
    }
}
