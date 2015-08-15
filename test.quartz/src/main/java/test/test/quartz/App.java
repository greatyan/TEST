package test.test.quartz;

import java.io.IOException;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws SchedulerException, IOException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		JobDetail jobDetail = JobBuilder.newJob(TestJob.class).withIdentity("sample").build();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("sample").forJob(jobDetail)
				.withSchedule(CronScheduleBuilder.cronSchedule("*/5,*/3 * * * * ? *")).build();
		scheduler.scheduleJob(jobDetail, trigger);

		System.out.print("Press enter to exit");
		scheduler.start();
		System.in.read();
		scheduler.shutdown();
	}

	public static class TestJob implements Job {

		public void execute(JobExecutionContext context) throws JobExecutionException {
			System.out.println(context.getFireTime() + context.getFireInstanceId());
			System.out.println("Test Job executed");
		}

	}
}
