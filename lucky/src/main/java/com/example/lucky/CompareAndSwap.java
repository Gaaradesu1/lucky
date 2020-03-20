package com.example.lucky;

import java.util.concurrent.CountDownLatch;

public class CompareAndSwap {
	static volatile int count = 0;
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		int threadSize = 100;
		CountDownLatch countDownLatch = new CountDownLatch(threadSize);
		for(int i = 0; i < threadSize; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for(int j = 0; j < 10; j++) {
							request();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						countDownLatch.countDown();
					}
				}
			});
			thread.start();
		}
		countDownLatch.await();
		long endTime = System.currentTimeMillis();
		System.out.println(Thread.currentThread().getName()+",执行时间="+(endTime-startTime)+",count="+getCount());
	}
	private static int getCount() {
		return count;
	}
	private synchronized static boolean compareAndSwap(int expectCount, int newCount) {
		if(getCount() == expectCount) {
			count = newCount;
			return true;
		}
		return false;
	}
	private static void request() throws InterruptedException {
		Thread.sleep(5);
		int expectCount;
		while (!compareAndSwap((expectCount = getCount()), expectCount + 1)) {}
		//while (!compareAndSwap(count, count++)) {}
	}
}
