package com.spdbccc.airm.jmc.scala

import java.util.concurrent.locks.ReentrantReadWriteLock

class ReadWriteLockClass {
  //这样写的好处是，不用每次都new一个lock，而是直接用这个lock 但是这个lock是线程不安全的
  private val (readLock, writeLock) = {
    val lock = new ReentrantReadWriteLock()
    (lock.readLock(), lock.writeLock())
  }

  // All accesses to the following state must be guarded with `withReadLock` or `withWriteLock`.
  private def withReadLock[B](fn: => B): B = {
    readLock.lock()
    try {
      fn
    } finally {
      readLock.unlock()
    }
  }

  private def withWriteLock[B](fn: => B): B = {
    writeLock.lock()
    try {
      fn
    } finally {
      writeLock.unlock()
    }
  }

}

object ReadWriteLockClass {
  def main(args: Array[String]): Unit = {
    val testClass = new ReadWriteLockClass
    testClass.withReadLock {
      println("read")
    }
    testClass.withWriteLock {
      println("write")
    }
  }
}

