package expinc.practice;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Program
{
	public static void main(String[] args) throws InterruptedException
	{
		testMergeSort();
	}
	
	
	private static void testSum() throws InterruptedException
	{
		System.out.println("Test sum job:");
		int countData = 1000000000;
		int[] data = new int[countData];
		for (int i = 0; i < data.length; ++i)
		{
			data[i] = 1;
		}
		
		// single thread
		long startTime = System.currentTimeMillis();
		int sum = 0;
		for (int i = 0; i < data.length; ++i)
		{
			sum += data[i];
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Single thread results " + sum + " in " + (endTime - startTime) + " ms.");
		
		// parallel
		int poolSize = 10;
		JobExecutorPool pool = new JobExecutorPoolImpl(poolSize);
		startTime = System.currentTimeMillis();
		List<JobExecutor> executors = new LinkedList<JobExecutor>();
		int stride = countData / poolSize + 1;
		for (int i = 0; i < data.length; i += stride)
		{
			JobExecutor executor = pool.acquireExecutor();
			executor.execute(new SumJob(data, i, Math.min(i + stride - 1, data.length - 1)));
			executors.add(executor);
		}
		sum = 0;
		for (JobExecutor executor : executors)
		{
			SumJob job = (SumJob) executor.getResultedJob();
			pool.releaseExecutor(executor);
			sum += job.getResult();
		}
		endTime = System.currentTimeMillis();
		pool.dispose();
		System.out.println("Parallel results " + sum + " in " + (endTime - startTime) + " ms.");
		
		// fork and join
		int forkThreshold = countData / poolSize + 1;
		JobExecutorPool forkablePool = new ForkableJobExecutorPool(poolSize);
		startTime = System.currentTimeMillis();
		JobExecutor executor = forkablePool.acquireExecutor();
		executor.execute(new SumForkableJob(data, 0, countData - 1, forkThreshold));
		SumJob job = (SumJob) executor.getResultedJob();
		forkablePool.releaseExecutor(executor);
		sum = job.getResult();
		endTime = System.currentTimeMillis();
		forkablePool.dispose();
		System.out.println("Fork and join results " + sum + " in " + (endTime - startTime) + " ms.");
	}
	
	
	private static void testMergeSort() throws InterruptedException
	{
		System.out.println("Test merge sort job:");
		int countData = 10000000;
		boolean printResult = countData <= 10;
		int[] data = new int[countData];
		Random random = new Random();
		for (int i = 0; i < data.length; ++i)
		{
			data[i] = random.nextInt();
		}
		
		// single thread
		long startTime = System.currentTimeMillis();
		int[] sortedData = MergeSort.sort(data, 0, countData - 1);
		long endTime = System.currentTimeMillis();
		if (printResult)
		{
			System.out.println("Single thread results:");
			for (int i : sortedData)
			{
				System.out.println("\t" + i);
			}
		}
		System.out.println("Single thread spent " + (endTime - startTime) + " ms.");
		
		// parallel
		int poolSize = 10;
		JobExecutorPool pool = new JobExecutorPoolImpl(poolSize);
		startTime = System.currentTimeMillis();
		List<JobExecutor> executors = new LinkedList<JobExecutor>();
		int stride = countData / poolSize + 1;
		for (int i = 0; i < data.length; i += stride)
		{
			JobExecutor executor = pool.acquireExecutor();
			executor.execute(new MergeSortJob(data, i, Math.min(i + stride - 1, data.length - 1)));
			executors.add(executor);
		}
		boolean first = true;
		for (JobExecutor executor : executors)
		{
			MergeSortJob job = (MergeSortJob) executor.getResultedJob();
			pool.releaseExecutor(executor);
			if (first)
			{
				sortedData = job.getResult();
				first = false;
			}
			else
			{
				int[] dataToMerge = job.getResult();
				sortedData = MergeSort.merge(sortedData, 0, sortedData.length - 1, dataToMerge, 0, dataToMerge.length - 1);
			}
		}
		endTime = System.currentTimeMillis();
		pool.dispose();
		if (printResult)
		{
			System.out.println("Parallel results:");
			for (int i : sortedData)
			{
				System.out.println("\t" + i);
			}
		}
		System.out.println("Parallel spent " + (endTime - startTime) + " ms.");
		
		// fork and join
		int forkThreshold = countData / poolSize + 1;
		JobExecutorPool forkablePool = new ForkableJobExecutorPool(poolSize);
		startTime = System.currentTimeMillis();
		JobExecutor executor = forkablePool.acquireExecutor();
		executor.execute(new MergeSortForkableJob(data, 0, countData - 1, forkThreshold));
		MergeSortJob job = (MergeSortJob) executor.getResultedJob();
		forkablePool.releaseExecutor(executor);
		sortedData = job.getResult();
		endTime = System.currentTimeMillis();
		forkablePool.dispose();
		if (printResult)
		{
			System.out.println("Fork and join results:");
			for (int i : sortedData)
			{
				System.out.println("\t" + i);
			}
		}
		System.out.println("Fork and join spent " + (endTime - startTime) + " ms.");
	}
}
