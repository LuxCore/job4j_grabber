package ru.job4j.grabber.service;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.stores.Store;

public class ScheduleManager {
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleManager.class);
	private Scheduler scheduler;

	public void init() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			LOG.error("Ошибка инициализации планировщика", e);
		}
	}

	public void load(int period, Class<SuperJobGrab> task, Store store) {
		try {
			JobDataMap data = new JobDataMap();
			data.put("store", store);
			JobDetail job = JobBuilder.newJob(task)
					.usingJobData(data)
					.build();
			SimpleScheduleBuilder times = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(period)
					.repeatForever();
			Trigger trigger = TriggerBuilder.newTrigger()
					.startNow()
					.withSchedule(times)
					.build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			LOG.error("Ошибка добавления задания в планировщик", e);
		}
	}

	public void close() {
		if (scheduler != null) {
			try {
				scheduler.shutdown();
			} catch (SchedulerException e) {
				LOG.error("Ошибка закрытия планировщика", e);
			}
		}
	}
}
