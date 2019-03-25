package expinc.practice;


import java.util.ArrayList;
import java.util.List;


public class ForkableJobExecutor extends JobExecutor
{
	private static long		defaultRequestFromPoolTimeOut	= 1000 * 1;
	private JobExecutorPool	executorPool;
	private long			requestFromPoolTimeOut;


	ForkableJobExecutor(JobExecutorPool executorPool)
	{
		super(executorPool);
		this.executorPool = executorPool;
		requestFromPoolTimeOut = defaultRequestFromPoolTimeOut;
	}
	
	
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				semJob.acquire();
				if (job instanceof ForkableJob)
					forkAndExecute();
				else
					job.execute();
				semIdle.release();
			}
		} catch (InterruptedException e)
		{
		}
	}
	
	
	private void forkAndExecute() throws InterruptedException
	{
		List<Job> forkedJobs = ((ForkableJob) job).fork(); // self not included

		if (null != forkedJobs)
		{
			List<JobExecutor> forkedExecutors = new ArrayList<JobExecutor>(forkedJobs.size());
			for (Job forkedJob : forkedJobs)
			{
//				JobExecutor forkedExecutor = executorPool.acquireExecutor(requestFromPoolTimeOut);
				JobExecutor forkedExecutor = executorPool.acquireExecutor();
				if (null != forkedExecutor)
				{
					forkedExecutor.execute(forkedJob);
					forkedExecutors.add(forkedExecutor);
				}
//				else
//				{
//					System.out.println("Failed to acquire executor!");
//					return; // TODO: how to let user know the job is failed due to executor request failure?
//				}
			}

			forkAndExecute(); // execute self

			for (JobExecutor forkedExecutor : forkedExecutors)
			{
				forkedExecutor.getResultedJob();
				executorPool.releaseExecutor(forkedExecutor);
			}
			((ForkableJob) job).join(forkedJobs);
		}
		else
			job.execute();
	}

}
