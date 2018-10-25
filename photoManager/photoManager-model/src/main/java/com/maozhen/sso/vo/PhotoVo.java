package com.maozhen.sso.vo;

import java.util.List;

import com.maozhen.sso.model.Photo;

import lombok.Data;
import lombok.ToString;

/**
 * @author caoting
 * @date 2018年10月18日
 */
@Data
@ToString
public class PhotoVo {

	private Integer carouseRowSize;
	
	private Integer carouseColSize;
	
	private List<Photo> photoList;
}
