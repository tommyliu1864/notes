package com.my.model.po.system;

import com.my.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole extends BaseEntity {

    private Long userId;

    private Long roleId;

}
