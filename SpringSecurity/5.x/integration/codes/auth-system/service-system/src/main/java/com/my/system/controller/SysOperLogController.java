package com.my.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.common.result.Result;
import com.my.model.po.system.SysOperLog;
import com.my.model.vo.system.SysOperLogQueryVO;
import com.my.system.service.SysOperLogService;
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

@Api(tags = "系统操作日志")
@RestController
@RequestMapping("/admin/system/sysOperLog")
public class SysOperLogController {

    @Autowired
    private SysOperLogService sysOperLogService;

    @PreAuthorize("hasAuthority('bnt.sysOperLog.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("{currentPage}/{pageSize}")
    public Result page(
            @ApiParam(value = "当前页", required = true) @PathVariable int currentPage,
            @ApiParam(value = "每页的数据条数", required = true) @PathVariable int pageSize,
            SysOperLogQueryVO vo
    ) {
        IPage page = new Page(currentPage, pageSize);
        LambdaQueryWrapper<SysOperLog> wrapper = new QueryWrapper<SysOperLog>().lambda()
                .like(StringUtils.hasText(vo.getUsername()), SysOperLog::getUsername, vo.getUsername())
                .ge(StringUtils.hasText(vo.getCreateTimeBegin()), SysOperLog::getCreateTime, vo.getCreateTimeBegin())
                .le(StringUtils.hasText(vo.getCreateTimeEnd()), SysOperLog::getCreateTime, vo.getCreateTimeEnd())
                .orderByDesc(SysOperLog::getCreateTime);
        IPage pages = sysOperLogService.page(page, wrapper);
        return Result.ok(pages);
    }

}
