// Import Matrix and Random
import Matrix.*;
import java.util.Random;

// Class that defines the neural network to train for XOR (multi layer perceptron)
public class NeuralNetwork {

    // Declare instance attributes
    // Input matrix
    private Matrix inputs;
    // Weight input -> hidden matrix
    private Matrix wih;
    // Hidden (not through activation function) matrix
    private Matrix hidden;
    // Activated hidden layer matrix
    private Matrix hiddenActive;
    // Weights hidden -> outputs matrix
    private Matrix who;
    // Output layer (not activated) matrix
    private Matrix outputs;
    // Output layer (activated, final output) matrix
    private Matrix outputsActive;
    // Learning rate
    private double lr;

    // Constructor that create multi layer perceptron with one hidden layer
    public NeuralNetwork(int iNodes, int hNodes, int oNodes, double learningRate) {
        // Create matrix for inputs
        inputs = new Matrix(1, iNodes);
        // Set learning rate
        lr = learningRate;
        // Create weight matricies
        wih = new Matrix(iNodes, hNodes);
        who = new Matrix(hNodes, oNodes);
        // Add random weights to matricies
        createWeights();
    }

    // Helper method that creates values for weight matricies
    private void createWeights() {
        // Create Random number generator
        Random rand = new Random();
        // Get number of weights needed for both weights
        int wihLen = wih.numRows() * wih.numCols();
        int whoLen = who.numRows() * who.numCols();

        // Create arrays to store weights, arrays used to set matrix
        double[] wihArr = new double[wihLen];
        double[] whoArr = new double[whoLen];

        // Create random weights for input -> hidden weights
        for(int i = 0; i < wihLen; i++) {
            wihArr[i] = (rand.nextDouble() * 0.98 + 0.01) / Math.sqrt((double)wih.numRows());
        }

        // Create random weights for hidden -> output weights
        for(int i = 0; i < whoLen; i++) {
            whoArr[i] = (rand.nextDouble() * 0.98 + 0.01) / Math.sqrt((double)who.numRows());
        }

        // Set the weight matricies
        try {
            wih.set(wihArr);
            who.set(whoArr);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    // Activation function -> Sigmoid Squishification Function -> 1 / (1 + e^-x)
    private Matrix sigmoid(Matrix matrix) {
        // Get array version of given matrix
        double[][] arr = matrix.asArray();
        // Create array for activated values
        double[][] activeArr = new double[arr.length][arr[0].length];

        // Put all matrix values through sigmoid function
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[0].length; j++) {
                activeArr[i][j] = 1 / (1 + Math.exp(-arr[i][j]));
            }
        }

        // Create and return activated matrix
        Matrix active = new Matrix(matrix.numRows(), matrix.numCols());
        try {
            active.set(activeArr);
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return active;
    }

    // Feed forward algorithm
    public void feedForward(double[] inputs) {
        try {
            // Set input matrix
            this.inputs.set(inputs);
            // Create hidden matrix as dot product of inputs and input -> hidden weights
            hidden = this.inputs.dot(wih);
            // Set hiddenActive matrix as hidden matrix passed through sigmoid
            hiddenActive = sigmoid(hidden);
            // Create outputs matrix as dot product as hiddenActive and hidden -> outputs weights
            outputs = hiddenActive.dot(who);
            // Set outputsActive matrix as outputs passed through sigmoid
            outputsActive = sigmoid(outputs);
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    // Backpropagation algorithm
    public void backPropagation(double[] targets) {
        try {
            // Set target matrix
            Matrix target = new Matrix(outputsActive.numRows(), outputsActive.numCols());
            target.set(targets);

            // Get output error as targets - actual output
            Matrix outputError = target.subtract(outputsActive);

            /**To change the weights the gradient (direction of steepest descent) must be found using partial derivatives
             * 
             * gradient of error with respect to hidden -> output weights =
             *      dError / dWeight = dError / dOutputActive * dOutputActive / dOutput * dOutput / dWeight
             * dError / dOutputActive = (target - actual)
             *      Error = 1/2(target - actual)^2 (must sqare it so negative error doesnt decrease total error when summed)
             *      dError / dOutputActive = target - OutputActive
             * dOutputActive / dOutput = OutputActive * (1 - OutputActive)
             *      sigmoid = 1 / (1 + e^-output)
             *      dSigmoid/dOutput = e^x / (1 + e^x)^2 = sigmoid * (1 - sigmoid)
             * dOutput / dWeight = hiddenOutput
             *      Output = hidden * weight
             *      dOutput / dWeight = hiddenOutput
             * dError / dWeight = (target - OutputActive) * outputActive * (1 - outputActive) * hiddenOutput
             * 
             * gradient of error with respect to input -> hidden weights =
             *      dError / dWeight = dError / dOutputActive * dOutputActive / dOutput * dOutput / dHiddenActive * dHiddenActive / dHidden * dHidden / dWeight
             * dError / dOutputActive = (target - actual)
             *      Error = 1/2(target - actual)^2 (must sqare it so negative error doesnt decrease total error when summed)
             *      dError / dOutputActive = target - OutputActive
             * dOutputActive / dOutput = OutputActive * (1 - OutputActive)
             *      sigmoid = 1 / (1 + e^-output)
             *      dSigmoid/dOutput = e^x / (1 + e^x)^2 = sigmoid * (1 - sigmoid)
             * dOutput / dHiddenActive = weight (who)
             *      Output = hiddenActive * weight
             *      dOutput / dHidden = weight
             * dHiddenActive / dHidden = HiddenActive * (1 - HiddenActive)
             *      Already explained in dOutputActive / dOutput
             * dHidden / dWeight = Input
             *      Already explained in dOutput / dWeight
             * dError / dWeight = (target - outputActive) * OutputActive * (1 - OutputActive) * weight (who) * HiddenActive * (1 - HiddenActive) * Input
             * 
             * Add these derivatives to their respective weight matricies to change the neural net to find correct answer
            **/

            // Compute derivative for who (excluding hidden matrix)
            Matrix derOutputError = outputError.multiply(outputsActive.multiply(outputsActive.subtractFrom(1.0)));
            // Transpose who matrix so it can be dotted with derOutputError
            Matrix whoT = who.transpose();
            // Find the Hidden error by dotting the derOutputError and transposed who
            Matrix hiddenError = derOutputError.dot(whoT);
            // Find derivative for wih excluding the input matrix
            Matrix derHiddenError = hiddenError.multiply(hiddenActive.multiply(hiddenActive.subtractFrom(1.0)));
            // Get transposed HiddenACtive for dotting with derOutputError (full gradient to add to who)
            Matrix hiddenActiveT = hiddenActive.transpose();
            // Addd gradient to who after it has been multiplied by learning rate
            who = who.add(hiddenActiveT.dot(derOutputError).multiply(lr));
            // Get transposed input matrix for dotting with derHiddenError
            Matrix inputT = inputs.transpose();
            // Add full gradient to wih after it is multiplied by learning rate
            wih = wih.add(inputT.dot(derHiddenError).multiply(lr));
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    // Method prints final output and meaning for test data given
    public void test(double[] inputs) {
        feedForward(inputs);
        double[][] out = outputsActive.asArray();
        double max = out[0][0];
        int maxNum = 0;
        for(int i = 0; i < 10; i++) {
            if(out[0][i] > max) {
                max = out[0][i];
                maxNum = i;
            }
        }
        System.out.print(maxNum);
    }

    // Example of training a functioning multilayer perceptron for XOR function
    public static void main(String[] args) {
        DataReader reader = new DataReader();
        Image[] images = reader.getImages();
        NeuralNetwork nn = new NeuralNetwork(784, 100, 10, 0.1);
        int epoch = 100;

        for(int a = 0; a < epoch; a++) {
            for(int i = 0; i < 100; i++) {
                nn.feedForward(images[i].getPixels());
                nn.backPropagation(images[i].getTargets());
                nn.test(images[i].getPixels());
                System.out.println(" " + images[i].getCorrect());
            }
        }
    }
}