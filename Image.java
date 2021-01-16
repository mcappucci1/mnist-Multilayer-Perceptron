public class Image {
    double[] arr;
    double num;

    public Image(String image) {
        arr = new double[784];
        createArray(image);
    }

    public void createArray(String image) {
        String[] imageArray = image.split(",");
        num = Double.parseDouble(imageArray[0]);
        for(int i = 0; i < 784; i++) {
            arr[i] = Double.parseDouble(imageArray[i+1]);
            arr[i] = ((arr[i] / 256) * 0.98) + 0.01;
        }
    }

    public double[] getPixels() {
        return arr;
    }

    public double getCorrect() {
        return num;
    }

    public double[] getTargets() {
        double[] targets = new double[10];
        for(int i = 0; i < 10; i++) {
            targets[i] = 0.01;
            if((int)num == i) {
                targets[i] = 0.99;
            }
        }
        return targets;
    }
}