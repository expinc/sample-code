package expinc.practice;


public interface JobExecutorPool
{
	public JobExecutor acquireExecutor() throws InterruptedException;


	public JobExecutor acquireExecutor(long timeOut) throws InterruptedException;


	public boolean releaseExecutor(JobExecutor executor) throws InterruptedException;
	
	
	public void dispose() throws InterruptedException;
}
