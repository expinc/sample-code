package expinc.practice;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ForkableJobExecutorPool extends JobExecutorPoolImpl
{

	public ForkableJobExecutorPool(int countExecutors)
	{
		semExecutors = new Semaphore(countExecutors);
		workExecutors = new HashMap<Long, JobExecutor>();
		idleExecutors = new LinkedList<JobExecutor>();
		for (int i = 0; i < countExecutors; ++i)
		{
			JobExecutor executor = new ForkableJobExecutor(this);
			executor.start();
			idleExecutors.add(executor);
		}
	}

}
