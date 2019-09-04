package com.bud.code.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bud.code.common.api.vo.Result;
import com.bud.code.common.constant.CommonConstant;
import com.bud.code.entity.sysuser.SysUser;
import com.bud.code.mapper.SysUserMapper;
import com.bud.code.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @Author: zsq
 * @Date: 2018-12-20
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
	
	@Autowired
	private SysUserMapper userMapper;

	@Override
	public SysUser getUserByName(String username) {
		return userMapper.getUserByName(username);
	}
	
	@Override
	public Result<?> checkUserIsEffective(SysUser sysUser) {
		Result<?> result = new Result<Object>();
		//情况1：根据用户信息查询，该用户不存在
		if (sysUser == null) {
			result.error500("该用户不存在，请注册");
			return result;
		}
		//情况2：根据用户信息查询，该用户已注销
		if (CommonConstant.DEL_FLAG_1.toString().equals(sysUser.getDelFlag())) {
			result.error500("该用户已注销");
			return result;
		}
		//情况3：根据用户信息查询，该用户已冻结
		if (CommonConstant.USER_FREEZE.equals(sysUser.getStatus())) {
			result.error500("该用户已冻结");
			return result;
		}
		return result;
	}
}
