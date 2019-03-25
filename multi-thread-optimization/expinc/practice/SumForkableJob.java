package expinc.practice;


import java.util.LinkedList;
import java.util.List;


public class SumForkableJob extends SumJob implements ForkableJob
{
	private static int	defaultForkThreshold	= 10;
	
	
	private int	forkThreshold;
	
	
	public SumForkableJob(int[] data, int begin, int end, int forkThreshold)
	{
		super(data, begin, end);
		this.forkThreshold = forkThreshold; 
	}


	public SumForkableJob(int[] data, int begin, int end)
	{
		this(data, begin, end, defaultForkThreshold);
	}


	@Override
	public List<Job> fork()
	{
		List<Job> forkedJobs = null;
		if (end - begin + 1 > forkThreshold)
		{
//			System.out.println("forking from " + begin + " to " + end);
			forkedJobs = new LinkedList<Job>();
			SumForkableJob forkedJob = new SumForkableJob(data, begin + (end - begin) / 2 + 1, end, forkThreshold);
			forkedJobs.add(forkedJob);
			end = begin + (end - begin) / 2;
		}
//		else
//		{
//			System.out.println("executing from " + begin + " to " + end);
//		}
		return forkedJobs;
	}


	@Override
	public void join(List<Job> jobs)
	{
//		int minBegin = this.begin;
//		int maxEnd = this.end;
		for (Job job : jobs)
		{
//			minBegin = Math.min(minBegin, ((SumJob)job).begin);
//			maxEnd = Math.max(maxEnd, ((SumJob)job).end);
			result += ((SumJob) job).result;
		}
//		System.out.println("joined from " + minBegin + " to " + maxEnd);
	}

}
