package com.github.web;

import com.github.common.annotation.NotNeedLogin;
import com.github.common.annotation.NotNeedPermission;
import com.github.common.encrypt.Encrypt;
import com.github.common.json.JsonResult;
import com.github.common.util.FileUtil;
import com.github.common.util.U;
import com.github.config.ManagerConfig;
import com.github.global.service.CacheService;
import com.github.liuanxin.api.annotation.ApiGroup;
import com.github.liuanxin.api.annotation.ApiMethod;
import com.github.liuanxin.api.annotation.ApiParam;
import com.github.manager.model.ManagerUser;
import com.github.manager.service.ManagerService;
import com.github.user.constant.UserConst;
import com.github.util.ManagerSessionUtil;
import com.github.vo.ManagerUserVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@ApiGroup(UserConst.MODULE_INFO)
@RestController
@RequestMapping("/user")
public class ManagerUserController {

    private static final int FAIL_LOGIN_COUNT = 3;
    private static final int FAIL_LOGIN_COUNT_EXPIRE_HOUR = 2;

    @Value("${spring.profiles.active:dev}")
    private String active;

    private final ManagerConfig config;
    private final CacheService cacheService;
    private final ManagerService adminService;
    public ManagerUserController(ManagerConfig config, CacheService cacheService, ManagerService adminService) {
        this.config = config;
        this.cacheService = cacheService;
        this.adminService = adminService;
    }

    @NotNeedLogin
    @ApiMethod(value = "登录", index = 0)
    @PostMapping("/login")
    public JsonResult<ManagerUserVo> login(@ApiParam("用户名") String username,
                                           @ApiParam("密码") String password,
                                           @ApiParam("验证码, 密码输错 " + FAIL_LOGIN_COUNT + " 次后需要") String code) {
        U.assertException(U.isBlank(username) || U.isBlank(password), "请输入用户名或密码");
        String failLoginKey = active + ":login:fail:" + username;
        long failCount = U.toLong(cacheService.get(failLoginKey));
        if (failCount >= FAIL_LOGIN_COUNT) {
            U.assertNil(code, "请输入验证码");
            if (!ManagerSessionUtil.checkImageCode(code)) {
                U.assertException("验证码有误");
            }
        }

        ManagerUser user = adminService.login(username, password);
        boolean cannotLogin = U.isNotBlank(user.getStatus()) && user.getStatus();
        U.assertException(cannotLogin, "用户无法登录");
        if (Encrypt.checkNotBcrypt(password, user.getPassword())) {
            cacheService.incr(failLoginKey);
            cacheService.expire(failLoginKey, FAIL_LOGIN_COUNT_EXPIRE_HOUR, TimeUnit.HOURS);
            U.assertException("用户名或密码不正确");
        } else {
            cacheService.delete(failLoginKey);
        }

        // 登录成功后填充菜单和权限, 平级放到用户上
        user.assignmentData(adminService.getUserRole(user));
        return JsonResult.success("登录成功并返回用户及菜单信息", getManagerUserVo(user));
    }
    private ManagerUserVo getManagerUserVo(ManagerUser user) {
        // 将用户和权限放入 session, 将用户和菜单返回
        ManagerSessionUtil.whenLogin(user, user.getPermissions());
        return ManagerUserVo.assemblyData(user, user.getMenus());
    }
    @NotNeedPermission
    @ApiMethod(value = "获取用户昵称信息及菜单信息", index = 1)
    @GetMapping("/info")
    public JsonResult<ManagerUserVo> info() {
        Long userId = ManagerSessionUtil.getUserId();
        ManagerUser user = adminService.getUser(userId);
        return JsonResult.success("获取用户及菜单信息", getManagerUserVo(user));
    }

    @NotNeedPermission
    @ApiMethod(value = "修改密码", index = 2)
    @PostMapping("/password")
    public JsonResult password(String oldPass, String newPass) {
        Long userId = ManagerSessionUtil.getUserId();
        adminService.updatePassword(userId, oldPass, newPass);
        return JsonResult.success("密码修改成功");
    }

    @NotNeedPermission
    @ApiMethod(value = "修改基本信息", index = 2)
    @PostMapping("/basic")
    public JsonResult update(@ApiParam("昵称") String nickName, @ApiParam("头像") MultipartFile file) {
        Long userId = ManagerSessionUtil.getUserId();
        ManagerUser user = new ManagerUser();
        user.setId(userId);
        user.setNickName(nickName);
        user.setAvatar(FileUtil.save(file, config.getFilePath(), config.getFileUrl(), false));
        adminService.addOrUpdateUser(user);
        return JsonResult.success("信息修改成功");
    }

    @NotNeedLogin
    @ApiMethod("退出")
    @GetMapping("/logout")
    public JsonResult login() {
        ManagerSessionUtil.signOut();
        return JsonResult.success("退出成功");
    }
}
