package com.my.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.result.Result;
import com.my.model.po.system.SysLoginLog;
import com.my.model.vo.system.SysLoginLogQueryVO;
import com.my.security.service.SysLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统登录日志")
@RestController
@RequestMapping("/admin/system/sysLoginLog")
public class SysLoginLogController {

    @Autowired
    private SysLoginLogService sysLoginLogService;

    @PreAuthorize("hasAuthority('bnt.sysLoginLog.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(
            @ApiParam(value = "当前页", required = true) @PathVariable int currentPage,
            @ApiParam(value = "每页的数据条数", required = true) @PathVariable int pageSize,
            SysLoginLogQueryVO vo
    ) {
        IPage page = new Page(currentPage, pageSize);
        LambdaQueryWrapper<SysLoginLog> wrapper = new QueryWrapper<SysLoginLog>().lambda()
                .like(StringUtils.hasText(vo.getUsername()), SysLoginLog::getUsername, vo.getUsername())
                .ge(StringUtils.hasText(vo.getCreateTimeBegin()), SysLoginLog::getCreateTime, vo.getCreateTimeBegin())
                .le(StringUtils.hasText(vo.getCreateTimeEnd()), SysLoginLog::getCreateTime, vo.getCreateTimeEnd())
                .orderByDesc(SysLoginLog::getCreateTime);
        IPage pages = sysLoginLogService.page(page, wrapper);
        return Result.ok(pages);
    }

}
