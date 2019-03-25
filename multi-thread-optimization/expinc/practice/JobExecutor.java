package expinc.practice;


import java.util.concurrent.Semaphore;


public class JobExecutor extends Thread
{
	private static long	nextId	= 1;

	long				id;
	protected Job		job;
	protected Semaphore	semJob;
	protected Semaphore	semIdle;


	private static synchronized long getNextId()
	{
		return nextId++;
	}


	JobExecutor(JobExecutorPool executorPool)
	{
		id = getNextId();
		job = null;
		semJob = new Semaphore(0);
		semIdle = new Semaphore(1);
	}
	
	
	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				semJob.acquire();
				job.execute();
				semIdle.release();
			}
		} catch (InterruptedException e)
		{}
	}
	
	
	void dispose()
	{
		this.interrupt();
	}


	public void execute(Job job) throws InterruptedException
	{
		semIdle.acquire();
		this.job = job;
		semJob.release();
	}
	

	public Job getResultedJob() throws InterruptedException
	{
		Job result = null;
		semIdle.acquire();
		result = job;
		semIdle.release();
		return result;
	}
	
}
