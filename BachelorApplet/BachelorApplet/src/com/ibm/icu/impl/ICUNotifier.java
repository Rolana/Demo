/**
 *******************************************************************************
 * Copyright (C) 2001-2002, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 *
 * $Source: /cvsroot/doubletype/doubletype/src/com/ibm/icu/impl/ICUNotifier.java,v $
 * $Date: 2004/01/17 21:28:03 $
 * $Revision: 1.1 $
 *
 *******************************************************************************
 */
package com.ibm.icu.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Abstract implementation of a notification facility.  Clients add
 * EventListeners with addListener and remove them with removeListener.
 * Notifiers call notifyChanged when they wish to notify listeners.
 * This queues the listener list on the notification thread, which
 * eventually dequeues the list and calls notifyListener on each
 * listener in the list.</p>
 *
 * <p>Subclasses override acceptsListener and notifyListener 
 * to add type-safe notification.  AcceptsListener should return
 * true if the listener is of the appropriate type; ICUNotifier
 * itself will ensure the listener is non-null and that the
 * identical listener is not already registered with the Notifier.
 * NotifyListener should cast the listener to the appropriate 
 * type and call the appropriate method on the listener.
 */

@SuppressWarnings("all")
public abstract class ICUNotifier {
    private final Object notifyLock = new Object();
    private NotifyThread notifyThread;
    private List listeners;

    /**
     * Add a listener to be notified when notifyChanged is called.
     * The listener must not be null. AcceptsListener must return
     * true for the listener.  Attempts to concurrently
     * register the identical listener more than once will be
     * silently ignored.  
     */
    public void addListener(EventListener l) {
	if (l == null) {
	    throw new NullPointerException();
	}

	if (acceptsListener(l)) {
	    synchronized (notifyLock) {
		if (listeners == null) {
		    listeners = new ArrayList(5);
		} else {
		    // identity equality check
		    Iterator iter = listeners.iterator();
		    while (iter.hasNext()) {
			if (iter.next() == l) {
			    return;
			}
		    }
		}

		listeners.add(l);
	    }
	} else {
	    throw new InternalError("Listener invalid for this notifier.");
	}
    }

    /**
     * Stop notifying this listener.  The listener must
     * not be null.  Attemps to remove a listener that is
     * not registered will be silently ignored.
     */
    public void removeListener(EventListener l) {
	if (l == null) {
	    throw new NullPointerException();
	}
	synchronized (notifyLock) {
	    if (listeners != null) {
		// identity equality check
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
		    if (iter.next() == l) {
			iter.remove();
			if (listeners.size() == 0) {
			    listeners = null;
			}
			return;
		    }
		}
	    }
	}
    }

    /**
     * Queue a notification on the notification thread for the current
     * listeners.  When the thread unqueues the notification, notifyListener
     * is called on each listener from the notification thread.
     */
    public void notifyChanged() {
	if (listeners != null) {
	    synchronized (notifyLock) {
		if (listeners != null) {
		    if (notifyThread == null) {
			notifyThread = new NotifyThread(this);
			notifyThread.setDaemon(true);
			notifyThread.start();
		    }
		    notifyThread.queue(listeners.toArray());
		}
	    }
	}
    }

    /**
     * The notification thread.
     */
    private static class NotifyThread extends Thread {
	private final ICUNotifier notifier;
	private final List queue = new LinkedList();

	NotifyThread(ICUNotifier notifier) {
	    this.notifier = notifier;
	}

	/**
	 * Queue the notification on the thread.
	 */
	public void queue(Object[] list) {
	    synchronized (this) {
		queue.add(list);
		notify();
	    }
	}

	/**
	 * Wait for a notification to be queued, then notify all
	 * listeners listed in the notification.
	 */
	public void run() {
	    Object[] list;
	    while (true) {
		try {
		    synchronized (this) {
			while (queue.isEmpty()) {
			    wait();
			}
			list = (Object[])queue.remove(0);
		    }

		    for (int i = 0; i < list.length; ++i) {
			notifier.notifyListener((EventListener)list[i]);
		    }
		}
		catch (InterruptedException e) {
		}
	    }
	}
    }

    /**
     * Subclasses implement this to return true if the listener is
     * of the appropriate type.
     */
    protected abstract boolean acceptsListener(EventListener l);

    /**
     * Subclasses implement this to notify the listener.
     */
    protected abstract void notifyListener(EventListener l);
}
