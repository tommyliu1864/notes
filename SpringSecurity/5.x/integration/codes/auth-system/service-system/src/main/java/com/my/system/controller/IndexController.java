package com.my.system.controller;

import com.my.common.result.Result;
import com.my.common.utils.JwtHelper;
import com.my.model.vo.system.LoginVO;
import com.my.model.vo.system.SysUserAuthVO;
import com.my.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    // 这里的代码只是为了让swagger生成文档，具体实现已经在SpringSecurity中了
    @ApiOperation("登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVO vo) {
        return null;
    }

    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public Result<SysUserAuthVO> info(HttpServletRequest request) {
        // 从token中解析出用户名
        String token = request.getHeader("token");
        String username = JwtHelper.getUsername(token);
        return Result.ok(sysUserService.getSysUserAuthInfo(username));
    }

    @ApiOperation("退出登录")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

}
