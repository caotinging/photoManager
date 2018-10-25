package com.maozhen.sso.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@TableName("t_image")
@Data
public class Image extends DataEntity<Image> {

	@TableField("image_url")
	private String imageUrl;
	
	@TableField("photo_id")
	private Long photoId;
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
