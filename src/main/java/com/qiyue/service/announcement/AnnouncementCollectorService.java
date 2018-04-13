package com.qiyue.service.announcement;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.qiyue.interf.web.announcement.AnnouncementCollectorHandler;

@Service
public class AnnouncementCollectorService {
	@Resource(name="gzGovAnnouncementHandler")
	private AnnouncementCollectorHandler gzgov;
	@Resource(name="szGovAnnouncementHandler")
	private AnnouncementCollectorHandler szgov;

	@Async
	@Scheduled(cron="0 0 0/1 * * *")
	public void gzgov() throws Exception {
		gzgov.collect();
	}
	
	@Async
	@Scheduled(cron="0 0 0/1 * * *")
	public void szgov() throws Exception {
		szgov.collect();
	}
}
