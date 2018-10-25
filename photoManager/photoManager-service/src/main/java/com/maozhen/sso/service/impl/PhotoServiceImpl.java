package com.maozhen.sso.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.maozhen.sso.dao.PhotoMapper;
import com.maozhen.sso.model.Photo;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.PhotoService;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

	@Override
	public Page<Photo> listByUserId(User user) {
		Wrapper<Photo> wrapper = new EntityWrapper<Photo>().eq("user_id", user.getId()).eq("is_deleted", 0);
		
		Page<Photo> page = this.selectPage(new Page<Photo>(user.getCurrentPage(),user.getPageSize()), wrapper);
		return page;
	}
}
