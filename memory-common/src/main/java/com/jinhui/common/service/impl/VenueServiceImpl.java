package com.jinhui.common.service.impl;

import com.jinhui.common.controller.VenueController;
import com.jinhui.common.entity.domain.Message;
import com.jinhui.common.entity.domain.Venue;
import com.jinhui.common.entity.domain.VenueUser;
import com.jinhui.common.mapper.MessageMapper;
import com.jinhui.common.mapper.VenueMapper;
import com.jinhui.common.mapper.VenueUserMapper;
import com.jinhui.common.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 馆服务层实现
 *
 * @autor wsc
 * @create 2018-05-16 9:53
 **/
@Service("venueService")
public class VenueServiceImpl implements VenueService {
    private static Logger logger = LoggerFactory.getLogger(VenueServiceImpl.class);

    @Autowired
    private VenueUserMapper venueUserMapper;
    @Autowired
    private VenueMapper venueMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public int createVenue(VenueUser venueUser,String createId) {
        int i = venueUserMapper.insertSelective(venueUser);
        logger.info("新建的馆ID: " + venueUser.getId());

        VenueUser queryVenueUser = venueUserMapper.selectByPrimaryKey(venueUser.getId());
        logger.info("新建的馆ID: " + queryVenueUser.getVenueId());
        Venue venue = new Venue();
        venue.setVenueId(queryVenueUser.getVenueId());
        venue.setVenueName(venueUser.getUserName());
        venue.setStatus("1");
        venue.setCreateId(createId);

        venueMapper.insertSelective(venue);

        return 0;
    }

    @Override
    public List<VenueUser> queryPrivateVenue(String openId) {
        return venueUserMapper.selectListByVenueId(openId);
    }

    @Override
    public List<VenueUser> queryPublicVenue() {
        return venueUserMapper.selectAllList();
    }

    @Override
    public VenueUser queryVenue(String venueId) {
        return venueUserMapper.queryVenue(venueId);
    }

    @Override
    public int addMessage(Message message) {
        return messageMapper.insertSelective(message);
    }

    @Override
    public List<Message> queryMessageListByVenueId(String venueId) {
        return messageMapper.selectMessageListByVenueId(venueId);
    }
}
