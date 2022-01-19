package edu.iastate.cs228.hw1;

/**
 * 
 * @author Asher Gust
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * 
 * This class sorts all the points in an array of 2D points to determine a reference point whose x and y 
 * coordinates are respectively the medians of the x and y coordinates of the original points. 
 * 
 * It records the employed sorting algorithm as well as the sorting time for comparison. 
 *
 */
public class PointScanner  
{
	private Point[] points; 
	
	private Point medianCoordinatePoint;  // point whose x and y coordinates are respectively the medians of 
	                                      // the x coordinates and y coordinates of those points in the array points[].
	private Algorithm sortingAlgorithm;
	
		
	protected long scanTime; 	       // execution time in nanoseconds. 
	
	/**
	 * This constructor accepts an array of points and one of the four sorting algorithms as input. Copy 
	 * the points into the array points[].
	 * 
	 * @param  pts  input array of points 
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public PointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException
	{
		if (pts == null || pts.length == 0) throw new IllegalArgumentException("Input must be an array of points with a length of 1 or greater");
		points = pts;
		sortingAlgorithm = algo;
	}

	
	/**
	 * This constructor reads points from a file. 
	 * 
	 * @param  inputFileName
	 * @throws FileNotFoundException 
	 * @throws InputMismatchException   if the input file contains an odd number of integers
	 */
	protected PointScanner(String inputFileName, Algorithm algo) throws FileNotFoundException, InputMismatchException
	{
		Scanner scnr = new Scanner(new File(inputFileName));
		int size = 0;

		while(scnr.hasNextInt()) {
			scnr.nextInt();
			if (!scnr.hasNextInt()) throw new InputMismatchException("File must have an even number of integers");
			scnr.nextInt();
			size++;
		}
		scnr.close();
		scnr = new Scanner(new File(inputFileName));
		points = new Point[size];

		for(int i = 0; i < size; i++){
			points[i] = new Point(scnr.nextInt(), scnr.nextInt());
		}
		scnr.close();
		sortingAlgorithm = algo;
	}

	
	/**
	 * Carry out two rounds of sorting using the algorithm designated by sortingAlgorithm as follows:  
	 *    
	 *     a) Sort points[] by the x-coordinate to get the median x-coordinate. 
	 *     b) Sort points[] again by the y-coordinate to get the median y-coordinate.
	 *     c) Construct medianCoordinatePoint using the obtained median x- and y-coordinates.     
	 *  
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter, InsertionSorter, MergeSorter,
	 * or QuickSorter to carry out sorting.       
	 * @param
	 * @return
	 */
	public void scan()
	{
		int medianX;
		int medianY;
		AbstractSorter aSorter;
		if (sortingAlgorithm == Algorithm.SelectionSort) aSorter = new SelectionSorter(points);
		else if (sortingAlgorithm == Algorithm.InsertionSort) aSorter = new InsertionSorter(points);
		else if (sortingAlgorithm == Algorithm.MergeSort) aSorter = new MergeSorter(points);
		else aSorter = new QuickSorter(points);

		long start = System.nanoTime();

		aSorter.setComparator(0);
		aSorter.sort();
		medianX = aSorter.getMedian().getX();

		aSorter.setComparator(1);
		aSorter.sort();
		medianY = aSorter.getMedian().getY();

		medianCoordinatePoint = new Point(medianX, medianY);

		long end = System.nanoTime();
		scanTime = end-start;

		
		// create an object to be referenced by aSorter according to sortingAlgorithm. for each of the two 
		// rounds of sorting, have aSorter do the following: 
		// 
		//     a) call setComparator() with an argument 0 or 1. 
		//
		//     b) call sort(). 		
		// 
		//     c) use a new Point object to store the coordinates of the Median Coordinate Point
		//
		//     d) set the medianCoordinatePoint reference to the object with the correct coordinates.
		//
		//     e) sum up the times spent on the two sorting rounds and set the instance variable scanTime. 
		
	}
	
	
	/**
	 * Outputs performance statistics in the format: 
	 * 
	 * <sorting algorithm> <size>  <time>
	 * 
	 * For instance, 
	 * 
	 * selection sort   1000	  9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description. 
	 */
	public String stats()
	{
		String indent = " ";
		if (sortingAlgorithm == Algorithm.MergeSort || sortingAlgorithm == Algorithm.QuickSort) indent += "    ";
		return sortingAlgorithm.toString() + indent + points.length + "   " + scanTime;

	}
	
	
	/**
	 * Write MCP after a call to scan(),  in the format "MCP: (x, y)"   The x and y coordinates of the point are displayed on the same line with exactly one blank space 
	 * in between. 
	 */
	@Override
	public String toString()
	{
		return "MCP: (" + medianCoordinatePoint.getX() + ", " + medianCoordinatePoint.getY() + ")";
	}

	
	/**
	 *  
	 * This method, called after scanning, writes point data into a file by outputFileName. The format 
	 * of data in the file is the same as printed out from toString().  The file can help you verify 
	 * the full correctness of a sorting result and debug the underlying algorithm. 
	 * 
	 * @throws FileNotFoundException
	 */
	public void writeMCPToFile() throws FileNotFoundException
	{
		// TODO 
	}	

	

		
}
