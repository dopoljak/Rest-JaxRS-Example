package com.example.rest.jaxrsutils;

/**
 * Simple class for measuring execution time
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class StopWatch
{
    private long start_time;
    private long inline_time;

    public StopWatch()
    {
	start_time = inline_time = System.nanoTime();
    }

    public void reset()
    {
	start_time = inline_time = System.nanoTime();
    }

    public long getExecutionMilis()
    {
	return (System.nanoTime() - start_time) / 1000000;
    }

    public String getFormatedExecTime()
    {
	return Dates.formatElapsed(getExecutionMilis());
    }

    public long getInlineMilis()
    {
	long execMilis = (System.nanoTime() - inline_time) / 1000000;
	inline_time = System.nanoTime();
	return execMilis;
    }

    public String getFormatedInlineTime()
    {
	return Dates.formatElapsed(getInlineMilis());
    }
}
