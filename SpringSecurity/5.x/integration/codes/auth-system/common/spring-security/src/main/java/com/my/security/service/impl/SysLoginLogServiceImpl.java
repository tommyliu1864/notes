package com.my.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.model.po.system.SysLoginLog;
import com.my.security.dao.SysLoginLogMapper;
import com.my.security.service.SysLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
}
