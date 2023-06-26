import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Arrays;
import java.util.Scanner;
import java.awt.image.DataBufferByte;



public class LSBHideGUI extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage embeddedImage;
    private BMPImage embeddedBImage;
    private String filePath;
    private String filename;
    private String path;
    private byte[] imageData;
    private byte[] encryptData;
    private BMPImage image;
    private JPanel imagePanel;
    private JPanel controlPanel;
    private JButton openButton;
    private JButton embedButton;
    private JButton storeButton;
    private JButton extractButton;

    private JButton resetButton;
    private JTextArea informationArea;
    private JTextArea resultArea;

    public LSBHideGUI() {
        setTitle("LSB Digital Hide Algorithm");
        setSize(900, 385);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        imagePanel = new JPanel();
        controlPanel = new JPanel();

        openButton = new JButton("读取图片");
        embedButton = new JButton("嵌入");
        storeButton = new JButton("存储图片");
        extractButton = new JButton("提取");
        resetButton = new JButton("重置");

        informationArea = new JTextArea(5, 5);
        resultArea = new JTextArea(5, 5);

        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });

        embedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                embedInformation();
            }
        });

        storeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storeImage();
            }
        });

        extractButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                extractInformation();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        imagePanel.setLayout(new BorderLayout());

        JPanel originalImagePanel = new JPanel();
        originalImagePanel.setPreferredSize(new Dimension(300, 300));
        originalImagePanel.setLayout(new BorderLayout());
        originalImagePanel.add(new JLabel("原图"), BorderLayout.NORTH);

        JPanel embeddedImagePanel = new JPanel();
        embeddedImagePanel.setPreferredSize(new Dimension(300, 300));
        embeddedImagePanel.setLayout(new BorderLayout());
        embeddedImagePanel.add(new JLabel("嵌入信息后的图"), BorderLayout.NORTH);

        JPanel textArea = new JPanel();
        textArea.setLayout(new BorderLayout());
        textArea.add(new JScrollPane(informationArea), BorderLayout.NORTH);
        textArea.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        imagePanel.add(originalImagePanel, BorderLayout.WEST);
        imagePanel.add(textArea, BorderLayout.CENTER);
        imagePanel.add(embeddedImagePanel, BorderLayout.EAST);

        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        controlPanel.add(openButton);
        controlPanel.add(embedButton);
        controlPanel.add(storeButton);
        controlPanel.add(extractButton);
        controlPanel.add(resetButton);

        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);
    }

    private void openImage() {
        reset();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                originalImage = ImageIO.read(selectedFile);
                ImageIcon imageIcon = new ImageIcon(originalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
                JLabel originalLabel = new JLabel(imageIcon);
                JPanel originalImagePanel = (JPanel) imagePanel.getComponent(0);
                originalImagePanel.removeAll();
                originalImagePanel.add(originalLabel, BorderLayout.CENTER);
                originalImagePanel.revalidate();
                originalImagePanel.repaint();
            } catch (IOException e) {
                e.printStackTrace();
            }

            filePath = selectedFile.getAbsolutePath();
            filename = selectedFile.getName();
            path = selectedFile.getParent()+"\\";
            image = BMPImage.readBMP(filePath);

            if (image != null) {
                // 处理图像数据
                imageData = image.getData();
                String text = "最长加密长度为:" + imageData.length / 8 + "字节";  // 设置要显示的文本
                // 在informationArea中显示文本
                informationArea.setText(text);
            }
        }
    }

    private void embedInformation() {
        String text = resultArea.getText();  // 获取要嵌入的文本信息
        byte[] secretInfo = text.getBytes();
         //加密并获取加密后的data
                encryptData = EncryptionAndDecryption.encrypt8(secretInfo, imageData);
                if (encryptData.length == 0) {
                    JOptionPane.showMessageDialog(this, "加密失败", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "请先读取图片", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要嵌入的文本信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 嵌入信息并生成嵌入信息后的图像
        image.setData(encryptData);
        BMPImage.writeBMP(image, path+"encrypt_"+filename);
        File selectedFile = new File(path+"encrypt_"+filename);
        try {
            embeddedImage = ImageIO.read(selectedFile);
            // 在界面上显示嵌入信息后的图像
            ImageIcon embeddedIcon = new ImageIcon(embeddedImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
            JLabel embeddedLabel = new JLabel(embeddedIcon);
            JPanel embeddedImagePanel = (JPanel) imagePanel.getComponent(2);
            embeddedImagePanel.removeAll();
            embeddedImagePanel.add(embeddedLabel, BorderLayout.CENTER);
            embeddedImagePanel.revalidate();
            embeddedImagePanel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常情况
        }
    }

    private void storeImage() {
        if (embeddedImage != null) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter bmpFilter = new FileNameExtensionFilter("BMP Image", "bmp");
            fileChooser.addChoosableFileFilter(bmpFilter);

            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // 获取所选文件过滤器
                FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();

                // 获取所选文件过滤器的文件扩展名
                String extension = selectedFilter.getExtensions()[0];

                // 如果用户没有手动输入文件扩展名，则追加所选文件过滤器的文件扩展名
                String newfileName = selectedFile.getName();
                if (!newfileName.toLowerCase().endsWith("." + extension)) {
                    newfileName += "." + extension;
                }
                path = selectedFile.getParent()+"\\";
                // 创建带有追加文件扩展名的新文件
                BMPImage.writeBMP(image, path+newfileName);
                JOptionPane.showMessageDialog(this, "保存成功!", "图片已保存", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "没有嵌入后的图片可以保存", "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void extractInformation() {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "请先读取图片", "错误",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // 实现提取信息的逻辑
        JFileChooser fileChooser = new JFileChooser();
        JOptionPane.showMessageDialog(this, "请选择原图", "提示",
                JOptionPane.INFORMATION_MESSAGE);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "bmp"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String selectedFilePath = selectedFile.getAbsolutePath();
            // 读取图片
            BMPImage encryptImage = BMPImage.readBMP(path + filename);

            BMPImage originImage = BMPImage.readBMP(selectedFilePath);

            // 解密图片信息并打印
            if (encryptImage != null && originImage != null) {
                JOptionPane.showMessageDialog(this, "提取成功!", "信息已提取",
                        JOptionPane.INFORMATION_MESSAGE);
                String text = Until.byteToString(EncryptionAndDecryption.decrypt8(originImage.getData(), encryptImage.getData()));  // 设置要显示的文本
                // 在informationArea中显示文本
                resultArea.setText("嵌入的信息为:" + text);
            } else {
                JOptionPane.showMessageDialog(this, "该图片未加密!", "错误",
                        JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void reset() {
        originalImage = null;
        embeddedImage = null;
        embeddedBImage = null;
        filePath = null;
        filename = null;
        path = null;
        imageData = null;
        encryptData = null;
        image = null;

        informationArea.setText("");
        resultArea.setText("");

        JPanel originalImagePanel = (JPanel) imagePanel.getComponent(0);
        originalImagePanel.removeAll();
        originalImagePanel.revalidate();
        originalImagePanel.repaint();

        JPanel embeddedImagePanel = (JPanel) imagePanel.getComponent(2);
        embeddedImagePanel.removeAll();
        embeddedImagePanel.revalidate();
        embeddedImagePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LSBHideGUI gui = new LSBHideGUI();
                gui.setVisible(true);
            }
        });
    }
}
