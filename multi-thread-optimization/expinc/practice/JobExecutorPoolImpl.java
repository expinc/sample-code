package expinc.practice;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class JobExecutorPoolImpl implements JobExecutorPool
{
	protected List<JobExecutor>			idleExecutors;
	protected Map<Long, JobExecutor>	workExecutors;
	protected Semaphore					semExecutors;
	
	
	protected JobExecutorPoolImpl()
	{}


	public JobExecutorPoolImpl(int countExecutors)
	{
		semExecutors = new Semaphore(countExecutors);
		workExecutors = new HashMap<Long, JobExecutor>();
		idleExecutors = new LinkedList<JobExecutor>();
		for (int i = 0; i < countExecutors; ++i)
		{
			JobExecutor executor = new JobExecutor(this);
			executor.start();
			idleExecutors.add(executor);
		}
	}


	@Override
	public JobExecutor acquireExecutor() throws InterruptedException
	{
		semExecutors.acquire();
		JobExecutor result = null;
		synchronized (this)
		{
			result = idleExecutors.get(0);
			workExecutors.put(result.id, result);
			idleExecutors.remove(0);
		}
		return result;
	}


	@Override
	public synchronized boolean releaseExecutor(JobExecutor executor) throws InterruptedException
	{
		if (null != workExecutors.get(executor.id))
		{
			executor.getResultedJob();
			workExecutors.remove(executor.id);
			idleExecutors.add(executor);
			semExecutors.release();
			return true;
		}
		else
			return false;
	}
	
	
	public synchronized void dispose() throws InterruptedException
	{
		while (!workExecutors.isEmpty())
		{
			releaseExecutor(workExecutors.entrySet().iterator().next().getValue());
		}
		
		for (JobExecutor executor : idleExecutors)
		{
			executor.dispose();
		}
	}


	@Override
	public JobExecutor acquireExecutor(long timeOut) throws InterruptedException
	{
		JobExecutor result = null;
		if (semExecutors.tryAcquire(timeOut, TimeUnit.MILLISECONDS))
		{
			synchronized (this)
			{
				result = idleExecutors.get(0);
				workExecutors.put(result.id, result);
				idleExecutors.remove(0);
			}
		}
		return result;
	}
}
