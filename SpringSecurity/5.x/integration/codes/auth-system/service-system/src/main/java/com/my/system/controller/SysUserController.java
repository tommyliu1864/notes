package com.my.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.result.Result;
import com.my.model.po.system.SysUser;
import com.my.model.vo.system.AssignRoleVO;
import com.my.model.vo.system.SysUserQueryVO;
import com.my.model.vo.system.SysUserRoleResultVO;
import com.my.security.custom.Sha1PasswordEncoder;
import com.my.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "系统用户管理")
@RequestMapping("/admin/system/sysUser")
@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private Sha1PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(
            @ApiParam(value = "当前页", required = true) @PathVariable int currentPage,
            @ApiParam(value = "每页的数据条数", required = true) @PathVariable int pageSize,
            SysUserQueryVO vo
    ) {
        IPage page = new Page(currentPage, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().lambda()
                .or().like(StringUtils.hasText(vo.getKeyword()), SysUser::getUsername, vo.getKeyword())
                .or().like(StringUtils.hasText(vo.getKeyword()), SysUser::getName, vo.getKeyword())
                .ge(StringUtils.hasText(vo.getCreateTimeBegin()), SysUser::getCreateTime, vo.getCreateTimeBegin())
                .le(StringUtils.hasText(vo.getCreateTimeEnd()), SysUser::getCreateTime, vo.getCreateTimeEnd());
        IPage pages = sysUserService.page(page, wrapper);
        return Result.ok(pages);
    }


    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation("添加用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) {
        // 对密码进行加密
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        // 设置默认的头像
        sysUser.setHeadUrl("https://p.qqan.com/up/2023-12/2023121281748142.jpg");
        if (sysUserService.save(sysUser)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation("根据ID删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        if (sysUserService.removeById(id)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation("根据ID列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@ApiParam("ID列表") @RequestBody List<Long> ids) {
        if (sysUserService.removeBatchByIds(ids)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("分配角色")
    @PutMapping("update")
    public Result update(@RequestBody SysUser sysUser) {
        if (sysUserService.updateById(sysUser)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("根据ID查询")
    @GetMapping("get/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        return Result.ok(sysUserService.getById(id));
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("更新用户状态")
    @PutMapping("updateStatus/{id}/{status}")
    public Result updateStatus(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long id,
            @ApiParam(value = "状态（1：正常 0：停用）", required = true) @PathVariable Integer status) {
        SysUser user = sysUserService.getById(id);
        user.setStatus(status);
        if (sysUserService.updateById(user)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("获取用户的角色列表")
    @GetMapping("getRoles/{userId}")
    public Result<SysUserRoleResultVO> getRoles(
            @ApiParam(value = "用户ID", required = true) @PathVariable Long userId
    ) {
        return Result.ok(sysUserService.getRoles(userId));
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("给用户分配角色")
    @PostMapping("/assignRoles")
    public Result assignRoles(@RequestBody AssignRoleVO vo) {
        sysUserService.assignRoles(vo);
        return Result.ok();
    }

}
