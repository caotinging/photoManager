package com.maozhen.sso.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.maozhen.sso.common.ex.BaseException;
import com.maozhen.sso.common.utils.CommomUtils;
import com.maozhen.sso.dao.ImageMapper;
import com.maozhen.sso.model.Image;
import com.maozhen.sso.model.Photo;
import com.maozhen.sso.service.ImageService;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

	@Override
	public Page<Image> listByPhotoId(Photo photo) throws BaseException {
		// 根据相册id获取相册下图片
		Wrapper<Image> wrapper = new EntityWrapper<Image>()
				.eq("photo_id", photo.getId())
				.eq("is_deleted", 0);

		Page<Image> page = this.selectPage(new Page<Image>(photo.getCurrentPage(), photo.getPageSize()), wrapper);
		return page;
	}

	@Override
	public Photo listImage(Photo photo) {
		List<Image> imageList = baseMapper.selectList(new EntityWrapper<Image>()
				.eq("photo_id", photo.getId())
				.eq("is_deleted", 0));
		
		if(CommomUtils.listIsNotEmpty(imageList)) {
			photo.setImageList(imageList);
			photo.setImgSize(imageList.size());
			
			// 封装相册全照片路径
			List<String> imageUrls = Lists.newArrayList();
			for (Image image : imageList) {
				imageUrls.add(image.getImageUrl());
			}
			photo.setImageUrls(imageUrls);
		}
		return photo;
	}

}
