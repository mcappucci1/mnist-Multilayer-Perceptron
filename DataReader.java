import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class DataReader {
    BufferedReader reader;
    Image[] images;

    public DataReader() {
        try {
            File file = new File("mnist_train_100.csv.txt");
            FileReader fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            images = new Image[100];
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public Image[] getImages() {
        try {
            for(int i = 0; i < 100; i++) {
                String image = reader.readLine();
                images[i] = new Image(image);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return images;
    }

    public static void main(String[] args) {
        DataReader read = new DataReader();
        Image[] images = read.getImages();
        System.out.println(images[0].getPixels());
    }
}