package ru.job4j.grabber.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.stores.Store;

public class SuperJobGrab implements Job {
	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		Store store = (Store) jobExecutionContext.getJobDetail().getJobDataMap().get("store");
		for (Post post : store.getAll()) {
			System.out.printf("id = %s, title = %s%n", post.getId(), post.getTitle());
		}
	}
}
