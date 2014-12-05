// Adam Kay
// CS2420

import java.util.ArrayList;
import java.util.Random;


public class Code {
	
	public static void main (String[] args) {
		for (int i = 10; i <= 1000; i = i*10) {
			RunTest(0, i);
		}
		
		for (int i = 10; i <= 1000; i = i*10) {
			RunTest(1, i);
		}
		
		for (int i = 10; i <= 1000; i = i*10) {
			RunTest(2, i);
		}
		
		for (int i = 10; i <= 1000; i = i*10) {
			RunTest(3, i);
		}
	}
	
	
	/**
	 * Creates a Stack using a SinglyLinkedList
	 * @author Adam
	 *
	 * @param <T>
	 */
	public class ListStack<T> {
		private SingleListNode<T> header;
		
		public ListStack() {
			header = new SingleListNode<T>(null, null);
		}
		
		public void push(T data) {
			SingleListNode<T> newNode = new SingleListNode<T>(data, header.next);
			header.next = newNode;
		}
		
		public T pop() {
			if (header.next != null) {
				T data = (T) header.next.data;
				header.next = header.next.next;
				return data;
			}
			else {
				return null;
			}
		}

		public boolean isEmpty() {
			if (header.next == null) {
				return true;
			}
			else return false;
		}
	}
	
	private class SingleListNode<T> {
		SingleListNode<T> next;
		T data;
		
		public SingleListNode(T input, SingleListNode<T> n) {
			data = input;
			next = n;
		}
	}
	
	private class DoubleListNode<T> {
		DoubleListNode<T> next, previous;
		T data;
		
		public DoubleListNode(T input, DoubleListNode<T> n, DoubleListNode<T> p) {
			data = input;
			next = n;
			previous = p;
		}
	}
	
	/**
	 * Creates a Queue using a DoublyLinkedList
	 * @author Adam
	 *
	 * @param <T>
	 */
	public class ListQueue<T> {
		private DoubleListNode<T> header, footer;
		
		public ListQueue() {
			header = new DoubleListNode<T>(null, null, null);
			footer = new DoubleListNode<T>(null, null, header);
			header.next = footer;
		}
		
		public void enqueue(T data) {
			DoubleListNode<T> newNode = new DoubleListNode<T>(data, header.next, header);
			header.next.previous = newNode;
			header.next = newNode;
		}
		
		public T dequeue() {
			if (header.next == footer) {
				return null;
			}
			else {
				T data = footer.previous.data;
				footer.previous.previous.next = footer;
				footer.previous = footer.previous.previous;
				return data;
			}
		}
		
		public boolean isEmpty() {
			if (header.next == footer) {
				return true;
			}
			else return false;
		}
	}
	
	/**
	 * Creates a Stack using an Array of initial size 3
	 * @author Adam
	 *
	 * @param <T>
	 */
	public class ArrayStack<T> {
		private T[] array;
		int index = 0;
		
		@SuppressWarnings("unchecked")
		public ArrayStack() {
			array = (T[]) new Object[3];
			for (int i = 0; i < array.length; i++) {
				array[i] = null;
			}
		}
		
		public void push(T data) {
			if (data == null) return;
			
			array[index] = data;
			index++;
			if (index == array.length) {
				T[] newArray = (T[]) new Object[array.length * 2];
				for (int i = 0; i < array.length; i++) {
					newArray[i] = array[i];
				}
				array = newArray;
			}
		}
		
		public T pop() {
			T data;
			if (index > 0) {
				data = array[index];
				index--;
			}
			else {
				data = null;
			}
			if (index < (array.length / 4) && array.length > 3) {
				T[] newArray = (T[]) new Object[(array.length / 2) + 1];
				for (int i = 0; i < newArray.length; i++) {
					newArray[i] = array[i];
				}
				array = newArray;
			}
			return data;
		}
		
		public boolean isEmpty() {
			if (index == 0) {
				return true;
			}
			else return false;
		}
	}
	
	/**
	 * Creates a Queue using an Array of initial size 3
	 * @author Adam
	 *
	 * @param <T>
	 */
	public class ArrayQueue<T> {
		private T[] array;
		int front, back, size;
		
		@SuppressWarnings("unchecked")
		public ArrayQueue() {
			front = 0;
			back = -1;
			size = 0;
			
			array = (T[]) new Object[3];
			for (int i = 0; i < array.length; i++) {
				array[i] = null;
			}
		}
		
		public void enqueue(T data) {
			back++;
			if (back == array.length) {
				back = 0;
			}
				
			array[back] = data;
			size++;
			
			if (array.length == size) {
				T[] newArray = (T[]) new Object[array.length * 2];
				for (int i = 0; i < array.length; i++) {
					newArray[i] = array[i];
				}
				array = newArray;
			}
		}
		
		public T dequeue() {
			//if (array[front] == null) {
			//	return null;
			//}
			
			T data = array[front];
			array[front] = null;
			
			size--;
			front++;
			
			if (size < array.length / 4 && array.length > 3) {
				T[] newArray = (T[]) new Object[(array.length / 2) + 1];
				for (int i = 0; i < newArray.length; i++) {
					newArray[i] = array[i];
				}
				array = newArray;
			}
			
			if (front >= array.length) {
				front = 0;
			}
			
			return data;
		}
	}
	
	/**
	 * Runs a bunch of tests
	 * @param whichTest			Which test you would like to run
	 * @param elementsInArray	How many elements to add/remove
	 */
	public static void RunTest(int whichTest, int elementsInArray) {
		Random rand = new Random();
		Code code = new Code();
		long startTime;
		long endTime;
		
		switch (whichTest) {
		case 0: // ListStack
			ListStack<Integer> ls = code.new ListStack<Integer>();
			startTime = System.nanoTime();
			
			for (int i = 0; i < elementsInArray; i++) {
				ls.push(rand.nextInt());
			}
			endTime = System.nanoTime();
			
			System.out.println("ListStack " + elementsInArray + " push time: " + (endTime - startTime));
			
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				ls.pop();
			}
			endTime = System.nanoTime();
			
			System.out.println("ListStack " + elementsInArray + " pop time: " + (endTime - startTime));
			break;
		case 1: // ListQueue
			ListQueue<Integer> lq = code.new ListQueue<Integer>();
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				lq.enqueue(rand.nextInt());
			}
			endTime = System.nanoTime();
			
			System.out.println("ListQueue " + elementsInArray + " enqueue time: " + (endTime - startTime));
			
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				lq.dequeue();
			}
			endTime = System.nanoTime();
			
			System.out.println("ListQueue " + elementsInArray + " dequeue time: " + (endTime - startTime));
			break;
		case 2: // ArrayStack
			ArrayStack<Integer> as = code.new ArrayStack<Integer>();
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				as.push(rand.nextInt());
			}
			endTime = System.nanoTime();
			
			System.out.println("ArrayStack " + elementsInArray + " push time: " + (endTime - startTime));
			
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				as.pop();
			}
			endTime = System.nanoTime();
			
			System.out.println("ArrayStack " + elementsInArray + " pop time: " + (endTime - startTime));
			break;
		case 3: // ArrayQueue
			ArrayQueue<Integer> aq = code.new ArrayQueue<Integer>();
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				aq.enqueue(rand.nextInt());
			}
			endTime = System.nanoTime();
			
			System.out.println("ArrayQueue " + elementsInArray + " enqueue time: " + (endTime - startTime));
			
			startTime = System.nanoTime();
			for (int i = 0; i < elementsInArray; i++) {
				aq.dequeue();
			}
			endTime = System.nanoTime();
			
			System.out.println("ArrayQueue " + elementsInArray + " dequeue time: " + (endTime - startTime));
			break;
		
		}
	}
	
	

}
