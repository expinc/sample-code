package expinc.practice;


import java.util.LinkedList;
import java.util.List;


public class MergeSortForkableJob extends MergeSortJob implements ForkableJob
{
	private static int	defaultForkThreshold	= 10;
	
	
	private int	forkThreshold;
	
	
	public MergeSortForkableJob(int[] data, int begin, int end, int forkThreshold)
	{
		super(data, begin, end);
		this.forkThreshold = forkThreshold; 
	}
	
	
	public MergeSortForkableJob(int[] data, int begin, int end)
	{
		this(data, begin, end, defaultForkThreshold);
	}


	@Override
	public List<Job> fork()
	{
		List<Job> forkedJobs = null;
		if (begin < end && end - begin + 1 > forkThreshold)
		{
			forkedJobs = new LinkedList<Job>();
			MergeSortForkableJob forkedJob = new MergeSortForkableJob(data, begin + (end - begin) / 2 + 1, end, forkThreshold);
			forkedJobs.add(forkedJob);
			end = begin + (end - begin) / 2;
		}
		return forkedJobs;
	}


	@Override
	public void join(List<Job> jobs)
	{
		for (Job job : jobs)
		{
			MergeSortJob otherJob = (MergeSortJob) job;
			result = MergeSort.merge(result, 0, result.length - 1, otherJob.result, 0, otherJob.result.length - 1);
		}
	}

}
