package com.bud.code.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bud.code.common.api.vo.Result;
import com.bud.code.entity.sysuser.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author zsq
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {
	
	SysUser getUserByName(String username);

	/**
	   * 校验用户是否有效
	 * @param sysUser
	 * @return
	 */
	Result checkUserIsEffective(SysUser sysUser);

}
