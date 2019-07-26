package com.github.web;

import com.github.common.annotation.NeedLogin;
import com.github.common.json.JsonResult;
import com.github.common.mvc.AppTokenHandler;
import com.github.common.util.LogUtil;
import com.github.common.util.U;
import com.github.global.constant.Develop;
import com.github.liuanxin.api.annotation.ApiGroup;
import com.github.liuanxin.api.annotation.ApiMethod;
import com.github.liuanxin.api.annotation.ApiParam;
import com.github.liuanxin.api.annotation.ApiTokens;
import com.github.user.constant.UserConst;
import com.github.util.BackendSessionUtil;
import com.github.vo.UserLoginVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@NeedLogin
@ApiGroup(UserConst.MODULE_INFO)
@RestController
@RequestMapping("/user")
public class BackendUserController {

    @ApiMethod(value = "刷新 token", develop = Develop.USER, desc = "每次打开 app 且本地有 token 值就请求此接口(pc 无视)", index = 0)
    @GetMapping("/refresh-token")
    public JsonResult<String> index() {
        String refreshToken = AppTokenHandler.resetTokenExpireTime();
        U.assertNil(refreshToken, "token 刷新失败, 请重新登录!");
        return JsonResult.success("token 刷新成功!", refreshToken);
    }

    // 请求当前接口时不需要登录, 文档示例中也不需要全局 token
    @NeedLogin(false)
    @ApiTokens(false)
    @ApiMethod(value = "登录", develop = Develop.USER)
    @GetMapping("/login")
    public JsonResult<UserLoginVo> login(@ApiParam(value = "用户名", must = true) String userName,
                                         @ApiParam("密码, 当验证码为空时") String password,
                                         @ApiParam("验证码") String code) {
        U.assertNil(userName, "请输入用户名");
        U.assertException(U.isBlank(password) && U.isBlank(code), "请使用密码或验证码登录");

        /*
        User user;
        if (U.isNotBlank(code)) {
            // 使用验证码登录
            U.assertException(!U.checkPhone(userName), "请输入手机号");

            String smsCode = smsService.getCode(userName);
            U.assertException(!code.equals(smsCode), "验证码错误");

            user = userService.get(userName);
        } else {
            // 使用密码登录
            user = userService.get(userName);
            U.assertException(U.isBlank(user) || !user.samePassword(password), "用户名或密码错误");
        }
        */

        UserLoginVo user = new UserLoginVo(); // 假定这是从上面操作之后得到的用户对象(上面的 User)
        user.setId(123L).setName("abc");

        // 登录成功后将用户信息存入 session 并生成 token 后返回给前端
        String token = BackendSessionUtil.whenLogin(user);
        return JsonResult.success("登录成功", UserLoginVo.assemblyData(user, token));
    }

    @ApiMethod(value = "获取登录信息", develop = Develop.USER)
    @GetMapping("/login-info")
    public JsonResult loginInfo() {
        // 如果没有登录, 获取当前接口将会直接返回 401, 如果登录了(session 或 token), 可以直接在方法内获取 session 里的数据
        // 有两种方式表示登录
        //   1. pc 的 session
        //   2. 上面接口返回的数据传了过来 x-token 并可以成功解码
        Long userId = BackendSessionUtil.getUserId();
        String userName = BackendSessionUtil.getUserName();

        if (LogUtil.ROOT_LOG.isDebugEnabled()) {
            LogUtil.ROOT_LOG.debug("session: " + userId + ", " + userName);
        }
        return JsonResult.success("获取成功");
    }

    @ApiMethod(value = "退出", develop = Develop.USER, desc = "如果本地有存 token, 请先删除再请求")
    @GetMapping("/logout")
    public JsonResult login() {
        BackendSessionUtil.signOut();
        return JsonResult.success("退出成功");
    }
}
