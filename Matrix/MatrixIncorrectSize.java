package Matrix;

public class MatrixIncorrectSize extends Exception
{
    private static final long serialVersionUID = 0;

    public MatrixIncorrectSize(String str)
    {
        super(str);
    }
    public MatrixIncorrectSize()
    {
        super();
    }
}