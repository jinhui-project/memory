package com.jinhui.common.mapper;


import com.jinhui.common.entity.domain.VenueUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface VenueUserMapper {

    int insertSelective(VenueUser record);

    VenueUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VenueUser record);

    List<VenueUser> selectListByVenueId(@Param("openId") String openId);

    List<VenueUser> selectAllList();

}