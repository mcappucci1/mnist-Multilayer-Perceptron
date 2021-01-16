package Matrix;

public class Matrix
{  
    // declare attributes
    private int row;
    private int col;
    private double[][] matrix;

    // constructor
    public Matrix(int row, int col)
    {
        this.row = row;
        this.col = col;
        matrix = new double[row][col];
    }

    // set the matrix
    public void set(double[][] array) throws MatrixIncorrectSize
    {
        // throw error if matrix and array aren't same size
        if(array.length != row || array[0].length != col) {
            throw new MatrixIncorrectSize("Array and matrix don't have same size in Matrix.set(double[][])");
        }

        // set the matrix to the values of the array
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                matrix[a][b] = array[a][b];
            }
        }
    }

    // set the matrix
    public void set(double[] array) throws MatrixIncorrectSize
    {
        // throw error if matrix and array aren't same size
        if(array.length != row * col) {
            throw new MatrixIncorrectSize("Array and matrix don't have same size in Matrix.set(double[])");
        }

        // set the matrix to the values of the array
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                matrix[a][b] = array[a * col + b];
            }
        }
    }

    // Returns the number of rows
    public int numRows() { return row; }

    // Returns the number of columns
    public int numCols() { return col; }

    // Returns the matrix
    public double[][] get() { return matrix; }

    // Returns the row in given index
    public double[] get(int row) throws IndexOutOfBoundsException
    {
        // Throw error if the row index is out of bounds
        if(row >= this.row) {
            throw new IndexOutOfBoundsException("Row index out of bounds in Matrix.get(int)");
        }
        // Return the row
        return matrix[row];
    }

    // Returns the item specified
    public double get(int row, int col) throws IndexOutOfBoundsException
    {
        // Throw error if row or col is out of bounds
        if(row >= this.row) {
            throw new IndexOutOfBoundsException("Row index out of bounds in Matrix.get(int, int)");
        }
        if(col >= this.col) {
            throw new IndexOutOfBoundsException("Col index out of bounds in Matrix.get(int, int)");
        }
        // Return the item
        return matrix[row][col];
    }

    // Multiply matricies
    public Matrix dot(Matrix mult) throws MatrixIncorrectSize
    {
        // Throw error if the multiplying matrix1 col != matrix2 row
        if(col != mult.numRows()) {
            throw new MatrixIncorrectSize("Error stemming from Matrix.multiply(). Matrix1 col != Matrix2 row");
        }
        // create array to store multiplied values
        double[][] resultant = new double[row][mult.numCols()];

        // loops that create resultant array
        // Loop goes through each row of resultant array
        for(int a = 0; a < row; a++) {
            // Loop that goes through each item in row of resulant array
            for(int b = 0; b < mult.numCols(); b++) {
                double sum = 0;
                // Loop sums product of row and col elements in multiplying matricies
                for(int c = 0; c < col; c++) {
                    sum += matrix[a][c] * mult.get(c, b);
                }
                // set element in resultant to sum
                resultant[a][b] = sum;
            }
        }

        // Create the return matrix containing the resultant array
        Matrix returnMatrix = new Matrix(row, mult.numCols());
        try {
            returnMatrix.set(resultant);
        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println("Error stemming from Matrix.multiply()");
        }
        // Return matrix
        return returnMatrix;
    }

    // Returns transpose matrix
    public Matrix transpose()
    {
        // Create resultant array to store transposed martix
        double[][] resultant = new double[col][row];

        // Loop that transposes matrix by assigning [row][col] to [col][row]
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[b][a] = matrix[a][b];
            }
        }

        // Create matrix object containing transposed matrix
        Matrix returnMatrix = new Matrix(col, row);
        try {
            returnMatrix.set(resultant);
        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println("Error stemming from Matrix.transpose()");
        }
        // Return transposed matrix
        return returnMatrix;
    }

    public Matrix subtract(Matrix sub) throws MatrixIncorrectSize
    {
        if(row != sub.numRows() || col != sub.numCols()) {
            throw new MatrixIncorrectSize("Matricies aren't the same size in Matrix.subtract(Matrix)");
        }

        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] - sub.get(a, b);
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix subtract(double num) throws MatrixIncorrectSize
    {
        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] - num;
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix subtractFrom(double num) throws MatrixIncorrectSize
    {
        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = num - matrix[a][b];
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix add(Matrix sub) throws MatrixIncorrectSize
    {
        if(row != sub.numRows() || col != sub.numCols()) {
            throw new MatrixIncorrectSize("Matricies aren't the same size in Matrix.subtract(Matrix)");
        }

        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] + sub.get(a, b);
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix add(double num) throws MatrixIncorrectSize
    {
        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] + num;
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix multiply(Matrix sub) throws MatrixIncorrectSize
    {
        if(row != sub.numRows() || col != sub.numCols()) {
            throw new MatrixIncorrectSize("Matricies aren't the same size in Matrix.subtract(Matrix)");
        }

        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] * sub.get(a, b);
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public Matrix multiply(double num) throws MatrixIncorrectSize
    {
        double[][] resultant = new double[row][col]; 
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                resultant[a][b] = matrix[a][b] * num;
            }
        }

        Matrix returnMatrix = new Matrix(row, col);
        returnMatrix.set(resultant);

        return returnMatrix;
    }

    public double[][] asArray() {
        return matrix;
    }

    // Print the matrix
    public void print()
    {
        for(int a = 0; a < row; a++) {
            for(int b = 0; b < col; b++) {
                System.out.print(matrix[a][b] + " ");
            }
            System.out.println();
        }
    }
}