package com.my.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.result.Result;
import com.my.model.po.system.SysMenu;
import com.my.model.po.system.SysRole;
import com.my.model.vo.system.AssignMenuVO;
import com.my.model.vo.system.SysRoleQueryVO;
import com.my.system.service.SysRoleService;
import com.my.system.utils.MenuHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(@ApiParam(value = "当前页", required = true) @PathVariable int currentPage,
                       @ApiParam(value = "每页的数据条数", required = true) @PathVariable int pageSize,
                       SysRoleQueryVO vo) {
        IPage page = new Page(currentPage, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>()
                .lambda()
                .like(StringUtils.hasText(vo.getRoleName()), SysRole::getRoleName, vo.getRoleName());
        IPage pages = sysRoleService.page(page, wrapper);
        return Result.ok(pages);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole) {
        if (sysRoleService.save(sysRole)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据ID删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        if (sysRoleService.removeById(id)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据ID列表删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@ApiParam("ID列表") @RequestBody List<Long> ids) {
        if (sysRoleService.removeBatchByIds(ids)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole) {
        if (sysRoleService.updateById(sysRole)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据ID查询")
    @GetMapping("get/{id}")
    public Result<SysRole> get(@PathVariable Long id) {
        return Result.ok(sysRoleService.getById(id));
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("获取菜单（权限）列表")
    @GetMapping("getMenus/{roleId}")
    public Result<List<SysMenu>> getMenus(@PathVariable Long roleId) {
        return Result.ok(MenuHelper.buildTree(sysRoleService.getMenus(roleId)));
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("给角色分配菜单（权限）")
    @PostMapping("assignMenus")
    public Result assignMenus(@RequestBody AssignMenuVO vo) {
        sysRoleService.assignMenus(vo);
        return Result.ok();
    }
}
