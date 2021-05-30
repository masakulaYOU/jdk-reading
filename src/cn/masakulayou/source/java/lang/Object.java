/*
 * Copyright (c) 1994, 2012, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

/**
 * Class {@code Object} is the root of the class hierarchy.
 * Every class has {@code Object} as a superclass. All objects,
 * including arrays, implement the methods of this class.
 *
 * @author  unascribed
 * @see     java.lang.Class
 * @since   JDK1.0
 */
public class Object {

    private static native void registerNatives();
    static {
        registerNatives();
    }

    /**
     * 返回此对象的运行时类。返回的Class对象是被所表示类的静态同步方法锁定的对象。
     * 实际的结果类型是Class extends X>其中X是对调用getClass的表达式的静态类型泛型擦除后的类型
     * 例如，下面这个例子就不需要类型转换cast
     *
     * Number n = 0;
     * Class<? extends Number> c = n.getClass();
     *
     * @return The {@code Class} object that represents the runtime
     *         class of this object.
     * @jls 15.8.2 Class Literals
     */
    public final native Class<?> getClass();

    /**
     * 返回对象的哈希码值，这个方法有利于使用hash table（哈希表，散列表），比如java.util.HashMap
     * hashcode的一个通用概念是：
     * 1. 同一个对象在java运行期间多次调用hashcode方法时，必须连续地返回一个相同的整数值，
     * 前提是对象上的等号比较中使用的信息没有被修改。这个整数不需要从程序的一次执行到同一应用
     * 程序的另一次执行保持一致
     * 2. 如果使用equals方法判断两个对象相等，那么对两个对象调用hashcode方法则返回相同的整数值
     * 3. 如果使用euqals方法判断两个对象不相同，对这两个对象调用hashcode方法不一定产生不同的整数值
     * 但是开发者应该注意，对于不同的对象产生相同的哈希结果可能会影响哈希表的性能
     *
     * 实际上，Object类的hashcode方法对于不同的对象返回不同的整数值，其内在原理是将对象的内部地址
     * 转为整数值，但是java语言并不需要这种实现技术
     *
     * @return  a hash code value for this object.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.lang.System#identityHashCode
     */
    public native int hashCode();

    /**
     * 检测另一个对象与该对象是否“相等”
     * equals方法实现了在非空对象引用上的相等比较
     * - 反射性：对于任意非空引用值x，x.euqals(x)返回true
     * - 对称性：对于任意非空引用x和y，如果x.equals(y)返回true，则y.equals(x)一定返回true
     * - 可传递性：对于任意非空引用x，y，z。如果x.equals(y)返回true，y.equals(z)返回true，则x.equals(z)一定返回true
     * - 一致性：对于任意非空引用x和y，连续多次调用x.equals(y)会连续返回true或false，前提是对象上的equals方法中使用的信息没有被修改
     * - 对于任意非空指针x，x.equals(null)返回false
     *
     * Object类的equals方法实现对象上最可分辨的可能等价关系。也就是说，对于任意非空引用x和y，当且仅当x和y指向同一个对象时，该方法返回true
     *
     * 记住，不管什么时候这个方法被重载，都需要修改hashcode方法，保证同一个对象必须具有相同的hash值
     * @param   obj   the reference object with which to compare.
     * @return  {@code true} if this object is the same as the obj
     *          argument; {@code false} otherwise.
     * @see     #hashCode()
     * @see     java.util.HashMap
     */
    public boolean equals(Object obj) {
        return (this == obj);
    }

    /**
     * 创建这个对象的一个副本并返回该副本
     * copy的精确意义取决于对象所属的类
     * 一般情况下，对于任意对象x，表达式
     * <blockquote>
     *     <pre>
     *         x.clone() != x
     *     </pre>
     * </blockquote>
     * 返回true，表达式
     * <blockquote>
     *     <pre>
     *         x.clone().getClass() == x.getClass()
     *     </pre>
     * </blockquote>
     * 返回true。
     * 但是也不是绝对要求
     * 最典型的
     * <blockquote>
     *     <pre>
     *         x.clone().equals(x)
     *     </pre>
     * </blockquote>
     * 返回true，但是也不是绝对要求。
     *
     * 依照公约，返回的对象要通过super.clone()获得。如果一个类以及其所有的父类，除了Object
     * 遵循这个约定，就会满足x.clone().getClass() == x.getClass()
     *
     * 按照约定，这个方法返回的对象与原对象相互独立，为了达成这一目的，有必要在返回这个对象之前改变super.clone返回的对象的一个或者更多的域
     * 在返回之前克隆它，通常，这意味着复制包含被克隆对象内部山层结构的任何可变对象，并将对这些对象的引用替换为副本的引用。
     * 入托一个类只包含基本字段或不可改变的对象，那么super返回的对象中通常没有字段，clone方法需要重载
     *
     * 类Object的clone方法执行特定的操作，如果对象没有实现Cloneable接口，则抛出CloneNotSupportedException。
     * 注意所有的数组类型（T[]）都被认为是现实了Cloneable几口，并且类型为T[]的数组克隆方法返回类型是T[]
     * 其中T是任何引用或原始类型。否则，该方法创建该对象的类的一个实例，并使用该对象对应字段的内容初始所有字段，就像
     * 通过赋值一样，字段的内容本身没有被clone，因此，该方法执行对象的浅拷贝，而不是深拷贝操作
     *
     * 类Object本身不是先Cloneable接口，因此在类Object的对象上调用clone()方法将导致在运行时抛出异常。
     *
     *
     * @return     a clone of this instance.
     * @throws  CloneNotSupportedException  if the object's class does not
     *               support the {@code Cloneable} interface. Subclasses
     *               that override the {@code clone} method can also
     *               throw this exception to indicate that an instance cannot
     *               be cloned.
     * @see java.lang.Cloneable
     */
    protected native Object clone() throws CloneNotSupportedException;

    /**
     * 返回一个代表这个对象的字符串。一般情况下，toString方法返回一个字符串表达的对象。
     * 建议任何子类都要重写这个方法
     *
     * 对于类的toString方法返回一个字符串，包含了class名以及实例对象，还有一个@符号
     * 还有使用十六进制表示的实例的的hashcode
     * getClass().getName() + @ Integer.toHexString(hashCode())
     *
     * @return  a string representation of the object.
     */
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }

    /**
     * 用于唤醒一个在此对象监视器上等待的线程
     * 如果所有的现成都在此对象上登台，那么只会选择一个线程，选择是任意的，并在对实现做出决定时发生
     * 一个线程在对象监视器上等待可以调用wait()方法
     * nofity()方法只能被作为此对象监视器的所有者的线程来调用
     * 一个线程想要成为对象监视器的所有者，可以使用以下三种方法
     * - 执行对象的同步实例方法
     * - 使用synchronized内置锁
     * - 对于Class类型的对象，执行同步静态方法
     *
     * 一次只能有一个线程拥有对象的监视器
     * 如果当前线程不是此对象监视器的所有者会抛出IllegalMonitorStateException异常
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @see        java.lang.Object#notifyAll()
     * @see        java.lang.Object#wait()
     */
    public final native void notify();

    /**
     * 用于唤醒在该方法上等待的所有线程
     * notifyAll()方法跟notify()方法一样，区别在于notify()方法唤醒在此对象监视器上登台的所有线程
     * 而notify()只是一个线程
     * 如果当前线程不是对象监视器的所有者，则会发生IllegalMonitorStateException异常
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#wait()
     */
    public final native void notifyAll();

    /**
     * 让当前线程处于等待（阻塞）状态，直到其他线程调用此对象的notify()方法或notifyAll()方法
     * 或者超过参数timeout设置的超时时间
     * 如果timeout参数为0，则不会超时，会一致进行等待，同wait()方法
     * 当前线程必须是此对象的监视器所有者，否则会发生IllegalMonitorStateException异常
     * 如果当前线程在等待之前火灾等待时被任何线程中断，则会抛出InterruptedException异常
     * 如果传递的参数不合法，则会抛出IllegalArgumentException异常
     *
     *
     * Causes the current thread to wait until either another thread invokes the
     * {@link java.lang.Object#notify()} method or the
     * {@link java.lang.Object#notifyAll()} method for this object, or a
     * specified amount of time has elapsed.
     * <p>
     * The current thread must own this object's monitor.
     * <p>
     * This method causes the current thread (call it <var>T</var>) to
     * place itself in the wait set for this object and then to relinquish
     * any and all synchronization claims on this object. Thread <var>T</var>
     * becomes disabled for thread scheduling purposes and lies dormant
     * until one of four things happens:
     * <ul>
     * <li>Some other thread invokes the {@code notify} method for this
     * object and thread <var>T</var> happens to be arbitrarily chosen as
     * the thread to be awakened.
     * <li>Some other thread invokes the {@code notifyAll} method for this
     * object.
     * <li>Some other thread {@linkplain Thread#interrupt() interrupts}
     * thread <var>T</var>.
     * <li>The specified amount of real time has elapsed, more or less.  If
     * {@code timeout} is zero, however, then real time is not taken into
     * consideration and the thread simply waits until notified.
     * </ul>
     * The thread <var>T</var> is then removed from the wait set for this
     * object and re-enabled for thread scheduling. It then competes in the
     * usual manner with other threads for the right to synchronize on the
     * object; once it has gained control of the object, all its
     * synchronization claims on the object are restored to the status quo
     * ante - that is, to the situation as of the time that the {@code wait}
     * method was invoked. Thread <var>T</var> then returns from the
     * invocation of the {@code wait} method. Thus, on return from the
     * {@code wait} method, the synchronization state of the object and of
     * thread {@code T} is exactly as it was when the {@code wait} method
     * was invoked.
     * <p>
     * A thread can also wake up without being notified, interrupted, or
     * timing out, a so-called <i>spurious wakeup</i>.  While this will rarely
     * occur in practice, applications must guard against it by testing for
     * the condition that should have caused the thread to be awakened, and
     * continuing to wait if the condition is not satisfied.  In other words,
     * waits should always occur in loops, like this one:
     * <pre>
     *     synchronized (obj) {
     *         while (&lt;condition does not hold&gt;)
     *             obj.wait(timeout);
     *         ... // Perform action appropriate to condition
     *     }
     * </pre>
     * (For more information on this topic, see Section 3.2.3 in Doug Lea's
     * "Concurrent Programming in Java (Second Edition)" (Addison-Wesley,
     * 2000), or Item 50 in Joshua Bloch's "Effective Java Programming
     * Language Guide" (Addison-Wesley, 2001).
     *
     * <p>If the current thread is {@linkplain java.lang.Thread#interrupt()
     * interrupted} by any thread before or while it is waiting, then an
     * {@code InterruptedException} is thrown.  This exception is not
     * thrown until the lock status of this object has been restored as
     * described above.
     *
     * <p>
     * Note that the {@code wait} method, as it places the current thread
     * into the wait set for this object, unlocks only this object; any
     * other objects on which the current thread may be synchronized remain
     * locked while the thread waits.
     * <p>
     * This method should only be called by a thread that is the owner
     * of this object's monitor. See the {@code notify} method for a
     * description of the ways in which a thread can become the owner of
     * a monitor.
     *
     * @param      timeout   the maximum time to wait in milliseconds.
     * @throws  IllegalArgumentException      if the value of timeout is
     *               negative.
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of the object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#notifyAll()
     */
    public final native void wait(long timeout) throws InterruptedException;

    /**
     * 当前线程处于等待(阻塞)状态，直到其他线程调用此对象的 notify() 方法或 notifyAll() 方法，
     * 或者超过参数 timeout 与 nanos 设置的超时时间。
     * 该方法与 wait(long timeout) 方法类似，多了一个 nanos 参数，
     * 这个参数表示额外时间（以纳秒为单位，范围是 0-999999）。
     * 所以超时的时间还需要加上 nanos 纳秒。
     * 如果 timeout 与 nanos 参数都为 0，则不会超时，会一直进行等待，类似于 wait() 方法。
     * 当前线程必须是此对象的监视器所有者，否则还是会发生 IllegalMonitorStateException 异常。
     * 如果当前线程在等待之前或在等待时被任何线程中断，则会抛出 InterruptedException 异常。
     * 如果传递的参数不合法或 nanos 不在 0-999999 范围内，则会抛出 IllegalArgumentException 异常。
     *
     *
     * Causes the current thread to wait until another thread invokes the
     * {@link java.lang.Object#notify()} method or the
     * {@link java.lang.Object#notifyAll()} method for this object, or
     * some other thread interrupts the current thread, or a certain
     * amount of real time has elapsed.
     * <p>
     * This method is similar to the {@code wait} method of one
     * argument, but it allows finer control over the amount of time to
     * wait for a notification before giving up. The amount of real time,
     * measured in nanoseconds, is given by:
     * <blockquote>
     * <pre>
     * 1000000*timeout+nanos</pre></blockquote>
     * <p>
     * In all other respects, this method does the same thing as the
     * method {@link #wait(long)} of one argument. In particular,
     * {@code wait(0, 0)} means the same thing as {@code wait(0)}.
     * <p>
     * The current thread must own this object's monitor. The thread
     * releases ownership of this monitor and waits until either of the
     * following two conditions has occurred:
     * <ul>
     * <li>Another thread notifies threads waiting on this object's monitor
     *     to wake up either through a call to the {@code notify} method
     *     or the {@code notifyAll} method.
     * <li>The timeout period, specified by {@code timeout}
     *     milliseconds plus {@code nanos} nanoseconds arguments, has
     *     elapsed.
     * </ul>
     * <p>
     * The thread then waits until it can re-obtain ownership of the
     * monitor and resumes execution.
     * <p>
     * As in the one argument version, interrupts and spurious wakeups are
     * possible, and this method should always be used in a loop:
     * <pre>
     *     synchronized (obj) {
     *         while (&lt;condition does not hold&gt;)
     *             obj.wait(timeout, nanos);
     *         ... // Perform action appropriate to condition
     *     }
     * </pre>
     * This method should only be called by a thread that is the owner
     * of this object's monitor. See the {@code notify} method for a
     * description of the ways in which a thread can become the owner of
     * a monitor.
     *
     * @param      timeout   the maximum time to wait in milliseconds.
     * @param      nanos      additional time, in nanoseconds range
     *                       0-999999.
     * @throws  IllegalArgumentException      if the value of timeout is
     *                      negative or the value of nanos is
     *                      not in the range 0-999999.
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of this object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     */
    public final void wait(long timeout, int nanos) throws InterruptedException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (nanos < 0 || nanos > 999999) {
            throw new IllegalArgumentException(
                                "nanosecond timeout value out of range");
        }

        if (nanos > 0) {
            timeout++;
        }

        wait(timeout);
    }

    /**
     * 让当前线程进入等待状态。直到其他线程调用此对象的notify()或notifyAll()方法
     * 当前线程必须是此对象的监视器的所有者，否则会发生IllegalMonitorStateExceptiony异常
     * 如果当前线程在等待之前或在登台时被任何线程中断，则会抛出InterruptedException异常
     *
     * 此方法相当于调用wait(0)
     * 在一个参数的方法中，可能会发生中断或虚假唤醒，这个方法则必须放在循环中
     * <pre>
     *     synchronized(obj) {
     *         while (<condition does not hold>)
     *            obj.wait();
     *         ...//
     *     }
     * </pre>
     *
     * @throws  IllegalMonitorStateException  if the current thread is not
     *               the owner of the object's monitor.
     * @throws  InterruptedException if any thread interrupted the
     *             current thread before or while the current thread
     *             was waiting for a notification.  The <i>interrupted
     *             status</i> of the current thread is cleared when
     *             this exception is thrown.
     * @see        java.lang.Object#notify()
     * @see        java.lang.Object#notifyAll()
     */
    public final void wait() throws InterruptedException {
        wait(0);
    }

    /**
     * 用于实例被垃圾回收器回收时触发的操作
     * 当GC垃圾回收器确定不存在对该对象有更多的引用时，对象的垃圾回收器就会调用这个方法
     *
     * @throws Throwable the {@code Exception} raised by this method
     * @see java.lang.ref.WeakReference
     * @see java.lang.ref.PhantomReference
     * @jls 12.6 Finalization of Class Instances
     */
    protected void finalize() throws Throwable { }
}
