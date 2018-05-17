package com.jinhui.common.mapper;


import com.jinhui.common.entity.domain.Venue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface VenueMapper {

    int insertSelective(Venue record);

    Venue selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Venue record);

}