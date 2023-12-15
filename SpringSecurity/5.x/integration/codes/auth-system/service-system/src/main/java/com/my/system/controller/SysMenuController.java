package com.my.system.controller;

import com.my.common.result.Result;
import com.my.model.po.system.SysMenu;
import com.my.system.service.SysMenuService;
import com.my.system.utils.MenuHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单（权限）管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("菜单列表")
    @GetMapping("list")
    public Result<List<SysMenu>> list() {
        List<SysMenu> list = sysMenuService.list();
        return Result.ok(MenuHelper.buildTree(list));
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @ApiOperation("添加菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu) {
        if (sysMenuService.save(sysMenu)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation("删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        if (sysMenuService.removeById(id)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation("修改菜单")
    @PutMapping("update")
    public Result update(@RequestBody SysMenu sysMenu) {
        if (sysMenuService.updateById(sysMenu)) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
