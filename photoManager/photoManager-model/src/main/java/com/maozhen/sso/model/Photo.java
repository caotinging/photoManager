package com.maozhen.sso.model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.maozhen.sso.vo.Carouse;

import lombok.Data;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@TableName("t_photo")
@Data
public class Photo extends DataEntity<Photo> {
	
	@TableField("user_id")
	private Long userId;
	
	@TableField("photo_name")
	private String photoName;
	
	@TableField("photo_type")
	private Integer photoType;
	
	@TableField("photo_desc")
	private String photoDesc;
	
	@TableField(exist = false)
	private List<Carouse> carouselList;
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
}
