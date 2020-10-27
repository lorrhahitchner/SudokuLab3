package pkgGame;

import java.security.SecureRandom;
import java.util.Random;

import pkgEnum.ePuzzleViolation;
import pkgHelper.LatinSquare;
import pkgHelper.PuzzleViolation;

/**
 * Sudoku - This class extends LatinSquare, adding methods, constructor to
 * handle Sudoku logic
 * 
 * @since Lab #2
 * @author Bert.Gibbons
 *
 */
public class Sudoku extends LatinSquare {

	/**
	 * 
	 * iSize - the length of the width/height of the Sudoku puzzle.
	 * 
	 * @since Lab #2
	 */
	private int iSize;

	/**
	 * iSqrtSize - SquareRoot of the iSize. If the iSize is 9, iSqrtSize will be
	 * calculated as 3
	 * 
	 * @since Lab #2
	 */

	private int iSqrtSize;

	/**
	 * Sudoku - for Lab #2... do the following:
	 * 
	 * set iSize If SquareRoot(iSize) is an integer, set iSqrtSize, otherwise throw
	 * exception
	 * 
	 * @since Lab #2
	 * @param iSize-
	 *            length of the width/height of the puzzle
	 * @throws Exception
	 *             if the iSize given doesn't have a whole number square root
	 */
	public Sudoku(int iSize) throws Exception {
		this.iSize = iSize;

		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}
	}

	/**
	 * Sudoku - pass in a given two-dimensional array puzzle, create an instance.
	 * Set iSize and iSqrtSize
	 * 
	 * @since Lab #2
	 * @param puzzle
	 *            - given (working) Sudoku puzzle. Use for testing
	 * @throws Exception will be thrown if the length of the puzzle do not have a whole number square root
	 */
	public Sudoku(int[][] puzzle) throws Exception {
		super(puzzle);
		this.iSize = puzzle.length;
		double SQRT = Math.sqrt(iSize);
		if ((SQRT == Math.floor(SQRT)) && !Double.isInfinite(SQRT)) {
			this.iSqrtSize = (int) SQRT;
		} else {
			throw new Exception("Invalid size");
		}

	}

	/**
	 * getPuzzle - return the Sudoku puzzle
	 * 
	 * @since Lab #2
	 * @return - returns the LatinSquare instance
	 */
	public int[][] getPuzzle() {
		return super.getLatinSquare();
	}

	/**
	 * getRegion - figure out what region you're in based on iCol and iRow and call
	 * getRegion(int)<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(0,3) would call getRegion(1) and return [2],[3],[3],[4]
	 * 
	 * @since Lab #2
	 * @param iCol
	 *            given column
	 * @param iRow
	 *            given row
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */
	public int[] getRegion(int iCol, int iRow) {

		int i = (iCol / iSqrtSize) + ((iRow / iSqrtSize) * iSqrtSize);

		return getRegion(i);
	}

	/**
	 * getRegion - pass in a given region, get back a one-dimensional array of the
	 * region's content<br>
	 * 
	 * Example, the following Puzzle:
	 * 
	 * 0 1 2 3 <br>
	 * 1 2 3 4 <br>
	 * 3 4 1 2 <br>
	 * 4 1 3 2 <br>
	 * 
	 * getRegion(2) and return [3],[4],[4],[1]
	 * 
	 * @since Lab #2
	 * @param r
	 *            given region
	 * @return - returns a one-dimensional array from a given region of the puzzle
	 */

	public int[] getRegion(int r) {

		int[] reg = new int[super.getLatinSquare().length];


		int i = (r / iSqrtSize) * iSqrtSize;
		int j = (r % iSqrtSize) * iSqrtSize;		
		int jMax = j + iSqrtSize;
		int iMax = i + iSqrtSize;
		int iCnt = 0;

		for (; i < iMax; i++) {
			for (j = (r % iSqrtSize) * iSqrtSize; j < jMax; j++) {
				reg[iCnt++] = super.getLatinSquare()[i][j];
			}
		}

		return reg;
	}
	
 
	
	@Override
	public boolean hasDuplicates()
	{
		if (super.hasDuplicates())
			return true;
		
		for (int k = 0; k < this.getPuzzle().length; k++) {
			if (super.hasDuplicates(getRegion(k))) {
				super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.DupRegion,k));
			}
		}
	
		return (super.getPV().size() > 0);
	}

	/**
	 * isPartialSudoku - return 'true' if...
	 * 
	 * It's a LatinSquare If each region doesn't have duplicates If each element in
	 * the first row of the puzzle is in each region of the puzzle At least one of
	 * the elemnts is a zero
	 * 
	 * 
	 * @since Lab #2
	 * @return true if the given puzzle is a partial sudoku
	 */
	public boolean isPartialSudoku() {

		super.setbIgnoreZero(true);
		
		super.ClearPuzzleViolation();
		
		if (hasDuplicates())
			return false;

		if (!ContainsZero()) {
			super.AddPuzzleViolation(new PuzzleViolation(ePuzzleViolation.MissingZero, -1));
			return false;
		}
		return true;

	}

	/**
	 * isSudoku - return 'true' if...
	 * 
	 * Is a partialSudoku Each element doesn't a zero
	 * 
	 * @since Lab #2
	 * @return - returns 'true' if it's a partialSudoku, element match (row versus column) and no zeros
	 */
	public boolean isSudoku() {

		this.setbIgnoreZero(false);
		
		super.ClearPuzzleViolation();
		
		if (hasDuplicates())
			return false;
		
		if (!super.isLatinSquare())
			return false;
		
		for (int i = 1; i < super.getLatinSquare().length; i++) {

			if (!hasAllValues(getRow(0), getRegion(i))) {
				return false;
			}
		}

		if (ContainsZero()) {
			return false;
		}

		return true;
	}

	/**
	 * isValidValue - test to see if a given value would 'work' for a given column /
	 * row
	 * 
	 * @since Lab #2
	 * @param iCol
	 *            puzzle column
	 * @param iRow
	 *            puzzle row
	 * @param iValue
	 *            given value
	 * @return - returns 'true' if the proposed value is valid for the row and column
	 */
	public boolean isValidValue(int iCol, int iRow, int iValue) {
		
		if (doesElementExist(super.getRow(iRow),iValue))
		{
			return false;
		}
		if (doesElementExist(super.getColumn(iCol),iValue))
		{
			return false;
		}
		if (doesElementExist(this.getRegion(iCol, iRow),iValue))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Pass in the column and row, and this will return the region number.  
	 * Example... For a 9x9 Sudoku, parameters 4,0 (col=4, row=0), this will return 1 (4,0 sits in region 1)
	 */
	public int getRegionNbr(int iCol, int iRow) {
		int rowRegion = iRow / iSqrtSize;
		int colRegion = iCol / iSqrtSize;
		return rowRegion * iSqrtSize + colRegion;
	}
	
	/**
	 * Write a method that will print the puzzle in the current state to the console. 
	 */
	public void printPuzzle() {
		System.out.println(getPuzzle());
	}
	
	/**
	 * This method will set the diagonal regions in the puzzle.  
	 * For a 9x9 Sudoku, this will set region 0, 4, 8 with shuffled values
	 */
	public void fillDiagonalRegions() {
		/**
		 * for (int i = 0; i <= iSize - 1; i += iSqrtSize) {
			setRegion(getRegionNbr(i, i));
			shuffleRegion(getRegionNbr(i, i));
			
			System.out.println("Region number: " + i);
		}
		*/
		setRegion(0);
		shuffleRegion(0);
		setRegion(4);
		shuffleRegion(4);
		setRegion(8);
		shuffleRegion(8);
	}
	

	
	/**
	 * This method will set the un-shuffled values in a given region
	 */
	public void setRegion(int regNbr) {
		/*
		for(int i = 0; i < iSize; i++ ) {
			int[] arr = getRegion(regNbr);
			arr[i] = i + 1;
		}
		*/

		int value = 1;
		int col;
		int row;
		for (col= (regNbr / iSqrtSize) * iSqrtSize; col< regNbr + iSqrtSize; col++) {
			for (row= (regNbr / iSqrtSize) * iSqrtSize; row< ((regNbr % iSqrtSize) * iSqrtSize) + iSqrtSize; row++) {
				super.setIndex(row, col, value);
				value++;
			}
		}

	}
	
	/**
	 * This method will shuffle the values of a given region
	 */
	public void shuffleRegion(int regNbr) {
		int col;
		int row;
		int index = 0;
		int[] regionArr = getRegion(regNbr);
		shuffleArray(regionArr);
		
		for (col= (regNbr / iSqrtSize) * iSqrtSize; col< regNbr + iSqrtSize; col++) {
			for (row= (regNbr % iSqrtSize) * iSqrtSize; row< ((regNbr % iSqrtSize) * iSqrtSize) + iSqrtSize; row++) {
				super.setIndex(col, row, regionArr[index++]);
			}
	}
	}
	
	/**
	 * This method will shuffle a given array of values
	 */
	public void shuffleArray(int[] nbrArr) {
		Random random = new SecureRandom();
		int i;
		for(i= nbrArr.length - 1; i>0; i--) {
			int x = random.nextInt(i+1);
			int swap = nbrArr[x];
			nbrArr[x] = nbrArr[i];
			nbrArr[i] = swap;
		}
	}
}




