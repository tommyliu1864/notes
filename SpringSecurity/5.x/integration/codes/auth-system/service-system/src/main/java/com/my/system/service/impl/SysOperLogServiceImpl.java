package com.my.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.model.po.system.SysOperLog;
import com.my.system.dao.SysOperLogMapper;
import com.my.system.service.SysOperLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {
}
