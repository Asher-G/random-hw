package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.lang.NumberFormatException; 
import java.lang.IllegalArgumentException; 
import java.util.InputMismatchException;

/**
 *  
 * @author Asher Gust
 *
 */

/**
 * 
 * This class implements the mergesort algorithm.   
 *
 */

public class MergeSorter extends AbstractSorter
{
	// Other private instance variables if you need ... 
	
	/** 
	 * Constructor takes an array of points.  It invokes the superclass constructor, and also 
	 * set the instance variables algorithm in the superclass.
	 *  
	 * @param pts   input array of integers
	 */
	public MergeSorter(Point[] pts) 
	{
		super(pts);
		algorithm = "mergesort";
	}


	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter. 
	 * 
	 */
	@Override 
	public void sort()
	{
		points = mergeSortRec(points);
	}

	
	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of points. One 
	 * way is to make copies of the two halves of pts[], recursively call mergeSort on them, 
	 * and merge the two sorted subarrays into pts[].   
	 * 
	 * @param pts	point array
	 * @return the sorted array
	 */
	private Point[] mergeSortRec(Point[] pts)
	{
		if (pts.length <= 1) return pts;

		Point[] half1 = new Point[pts.length / 2];
		Point[] half2 = new Point[pts.length - half1.length];

		for (int i = 0; i < half1.length; i++){
			half1[i] = pts[i];
		}
		for (int i = 0; i < half2.length; i++){
			half2[i] = pts[half1.length + i];
		}

		Point[] sorted = new Point[pts.length];
		half1 = mergeSortRec(half1);
		half2 = mergeSortRec(half2);
		sorted = merge(half1, half2);

		return sorted;
	}

	private Point[] merge(Point[] half1, Point[] half2){

		Point[] sorted = new Point[half1.length + half2.length];
		int i = 0;
		int j = 0;
		int k = 0;

		while(i < half1.length || j < half2.length){

			if (i < half1.length && j < half2.length){

				if(pointComparator.compare(half1[i], half2[j]) == -1){
					sorted[k] = half1[i];
					i++;
					k++;
				} else {
					sorted[k] = half2[j];
					j++;
					k++;
				}
			} else if(i < half1.length) {
				sorted[k] = half1[i];
				i++;
				k++;
			} else if (j < half2.length){
				sorted[k] = half2[j];
				j++;
				k++;
			}
		}

		return sorted;
	}

	
	// Other private methods in case you need ...

}
