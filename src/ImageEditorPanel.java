import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ImageEditorPanel extends JPanel implements KeyListener {

    Color[][] pixels;

    public ImageEditorPanel() {
        BufferedImage imageIn = null;

        try {
            // the image should be in the main project folder, not in \src or \bin
            imageIn = ImageIO.read(new File("pic2.jpg"));

        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
        pixels = makeColorArray(imageIn);
        setPreferredSize(new Dimension(pixels[0].length, pixels.length));
        setBackground(Color.BLACK);
        addKeyListener(this);
    }

    public void paintComponent(Graphics g) {
        // paints the array pixels onto the screen
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                g.setColor(pixels[row][col]);
                g.fillRect(col, row, 1, 1);
            }
        }
    }

    public void run() {

    }

    public Color[][] flipHoriz(Color[][] pic) {
        Color[][] flipPic = new Color[pic.length][pic[0].length];
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                flipPic[r][c] = pic[r][Math.abs(c - (pixels[0].length - 1))];
            }
        }
        return flipPic;
    }

    public Color[][] flipVert(Color[][] pic) {
        Color[][] newPic = new Color[pic.length][pic[0].length];
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                newPic[r][c] = pic[Math.abs(r - (pixels.length - 1))][c];
            }
        }
        return newPic;
    }

    public Color[][] grayscale(Color[][] pic) {
        Color tempColor;
        int avg;
        Color gray;
        Color[][] newPic = new Color[pic.length][pic[0].length];
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                newPic[r][c] = pic[r][c];
                tempColor = pic[r][c];
                avg = (tempColor.getRed() + tempColor.getGreen() + tempColor.getBlue()) / 3;
                gray = new Color(avg, avg, avg);
                newPic[r][c] = gray;
            }
        }
        return newPic;
    }

    public Color[][] blur(Color[][] pic) {
        Color[][] newPic = new Color[pic.length][pic[0].length];
        final int BLUR_SIZE = 3;
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                int red = 0;
                int blue = 0;
                int green = 0;
                int numPixels = 0;
                for (int i = r - BLUR_SIZE; i <= r + BLUR_SIZE; i++) {
                    for (int j = c - BLUR_SIZE; j <= c + BLUR_SIZE; j++) {
                        if (i < 0 || j < 0 || i >= pic.length || j >= pic[0].length) {
                            numPixels += 0;
                        } else {
                            red += pic[i][j].getRed();
                            blue += pic[i][j].getBlue();
                            green += pic[i][j].getGreen();
                            numPixels++;
                        }
                    }
                }
                newPic[r][c] = new Color(red / numPixels, green / numPixels, blue / numPixels);
            }
        }
        return newPic;
    }

    public Color[][] blackAndWhite(Color[][] pic) {
        Color[][] newPic = new Color[pic.length][pic[0].length];
        Color tempColor;
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                tempColor = pic[r][c];
                if ((tempColor.getRed() + tempColor.getGreen() + tempColor.getBlue()) / 3 > 127) {
                    newPic[r][c] = new Color(255, 255, 255);
                } else {
                    newPic[r][c] = new Color(0, 0, 0);
                }
            }
        }
        return newPic;
    }

    public Color[][] makeColorArray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Color[][] result = new Color[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Color c = new Color(image.getRGB(col, row), true);
                result[row][col] = c;
            }
        }
        // System.out.println("Loaded image: width: " +width + " height: " + height);
        return result;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
            if (e.getKeyChar() == 'h') {
                pixels = flipHoriz(pixels);
            }
            if (e.getKeyChar() == 'v') {
                pixels = flipVert(pixels);
            }
            if (e.getKeyChar() == 'g') {
                pixels = grayscale(pixels);
            }
            if (e.getKeyChar() == 'b') {
                pixels = blur(pixels);
            }
            if (e.getKeyChar() == 'w') {
                pixels = blackAndWhite(pixels);
            }
            repaint();
            if (e.getKeyChar()=='s'){
                try {
                    Container contentPane = getRootPane();
                    BufferedImage image = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = image.createGraphics();
                    contentPane.printAll(g2d);
                    g2d.dispose();

                    ImageIO.write(image, "jpeg", new File("Your Edited Image.png"));
                } catch (IOException ex) {
                }
            }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
