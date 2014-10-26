package com.wangyin.ak47.common;

import java.util.Collection;
import java.util.LinkedList;


/**
 * A {@link LinkedList} version with a maximum number of elements. When adding
 * elements to the end of the list, first elements in the list are discarded if
 * the maximum size is reached.
 * 
 * @author Kevin Gaudin
 *
 * @param <E>
 */
public class BoundedLinkedList<E> extends LinkedList<E> {

    /**
     * 
     */
    private static final long serialVersionUID = -2941042414852193810L;
    private final int maxSize;

    public BoundedLinkedList(int maxSize) {
        this.maxSize = maxSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#add(java.lang.Object)
     */
    @Override
    public boolean add(E object) {
        if (size() == maxSize) {
            removeFirst();
        }
        return super.add(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#add(int, java.lang.Object)
     */
    @Override
    public void add(int location, E object) {
        if (size() == maxSize) {
            removeFirst();
        }
        super.add(location, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        final int totalNeededSize = size() + collection.size();
        final int overhead = totalNeededSize - maxSize;
        if (overhead > 0) {
            removeRange(0, overhead);
        }
        return super.addAll(collection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(int location, Collection<? extends E> collection) {
        // int totalNeededSize = size() + collection.size();
        // int overhead = totalNeededSize - maxSize;
        // if(overhead > 0) {
        // removeRange(0, overhead);
        // }
        // return super.addAll(location, collection);
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#addFirst(java.lang.Object)
     */
    @Override
    public void addFirst(E object) {
        // super.addFirst(object);
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.LinkedList#addLast(java.lang.Object)
     */
    @Override
    public void addLast(E object) {
        add(object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#toString()
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        for (E object : this) {
            result.append(object.toString());
        }
        return result.toString();
    }
    
//    /**
//     * Returns the last element in this list, 
//     * or null if this list is empty.
//     * 
//     */
//    @Override
//    public E getLast(){
//        try{
//            return super.getLast();
//        }catch(NoSuchElementException e){
//            return null;
//        }
//    }
//    /**
//     * Returns the first element in this list, 
//     * or null if this list is empty.
//     * 
//     */
//    @Override
//    public E getFirst(){
//        try{
//            return super.getFirst();
//        }catch(NoSuchElementException e){
//            return null;
//        }
//    }
    
    
}