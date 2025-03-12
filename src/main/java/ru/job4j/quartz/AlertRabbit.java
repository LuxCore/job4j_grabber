package ru.job4j.quartz;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AlertRabbit {
	public static void main(String[] args) {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			JobDetail job = JobBuilder.newJob(Rabbit.class).build();
			Properties rabbitProps = loadProperties("rabbit.properties");
			int interval = Integer.parseInt(rabbitProps.getProperty("rabbit.interval"));
			SimpleScheduleBuilder times = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(interval)
					.repeatForever();
			Trigger trigger = TriggerBuilder.newTrigger()
					.startNow()
					.withSchedule(times)
					.build();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static class Rabbit implements Job {
		@Override
		public void execute(JobExecutionContext jobExecutionContext) {
			System.out.println("Rabbit runs here...");
		}
	}

	private static Properties loadProperties(String filename) throws IOException {
		try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		}
	}
}
