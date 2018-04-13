package com.qiyue.repository.announcement;

import org.springframework.data.repository.CrudRepository;

import com.qiyue.entity.announcement.AnnouncementEntity;

public interface HtmlSelectorRepository extends CrudRepository<AnnouncementEntity,String> {
	
}
