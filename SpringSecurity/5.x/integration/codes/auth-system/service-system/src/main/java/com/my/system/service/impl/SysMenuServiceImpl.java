package com.my.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.system.dao.SysMenuMapper;
import com.my.model.po.system.SysMenu;
import com.my.system.service.SysMenuService;
import org.springframework.stereotype.Service;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

}
