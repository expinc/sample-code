package expinc.practice;


import java.util.List;


public interface ForkableJob extends Job
{
	public List<Job> fork();


	public void join(List<Job> jobs);
}
