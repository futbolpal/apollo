package collection.analyzers;

import java.util.Iterator;

import framework.tasks.Task;

public abstract class Analyzer<T> extends Task {
	protected transient Iterator<T> iterator_;

	protected int failures_;

	public Analyzer(String name) {
		this(name, null);
	}

	public Analyzer(String name, Iterator<T> iterator) {
		super(name);
		iterator_ = iterator;
	}

	public void setIterator(Iterator<T> itr) {
		this.iterator_ = itr;
	}

	public int getFailures() {
		return this.failures_;
	}

	public void init()
	{
		super.init();
	}

	public synchronized void run() {
		this.init();
		while (iterator_.hasNext()) {
			long start = System.currentTimeMillis();
			T e = iterator_.next();
			analyze(e);
			average_time_ = (average_time_ * (double) progress_ + (double) (System
					.currentTimeMillis() - start))
					/ (progress_ + 1);
			++progress_;
			if (this.isDone())
				break;
		}
		this.setDone(true);
		this.done();
	}

	protected abstract void analyze(T next);
}