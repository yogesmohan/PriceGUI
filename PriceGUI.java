import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class PriceGUI extends JFrame implements ActionListener {

    private JTextField nameField;
    private JTextField salaryField;
    private JComboBox<String> productBox;
    private JTextArea resultArea;

    private JButton checkButton;
    private JButton originalButton;
    private JButton clearButton;

    public PriceGUI() {

        setTitle("Price GUI Application");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel(
            "Price GUI Application",
            SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(
            BorderFactory.createEmptyBorder(15, 0, 10, 0)
        );

        add(titleLabel, BorderLayout.NORTH);

        // Input panel
        JPanel inputPanel = new JPanel(
            new GridLayout(3, 2, 10, 10)
        );

        inputPanel.setBorder(
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        inputPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Monthly Salary (RM):"));
        salaryField = new JTextField();
        inputPanel.add(salaryField);

        inputPanel.add(new JLabel("Select Product:"));

        String[] products = {
            "SmartPhone - Iphone 14 ProMax",
            "Laptop - Lenovo Yoga",
            "Car - BMW"
        };

        productBox = new JComboBox<>(products);
        inputPanel.add(productBox);

        // Result area
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        JScrollPane scrollPane = new JScrollPane(resultArea);

        JPanel centerPanel = new JPanel(
            new BorderLayout(10, 10)
        );

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.setBorder(
            BorderFactory.createEmptyBorder(0, 20, 10, 20)
        );

        add(centerPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        checkButton = new JButton("Check Eligibility");
        originalButton = new JButton("Show Original Output");
        clearButton = new JButton("Clear");

        buttonPanel.add(checkButton);
        buttonPanel.add(originalButton);
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event handling
        checkButton.addActionListener(this);
        originalButton.addActionListener(this);
        clearButton.addActionListener(this);

        setVisible(true);
    }

    // Capture output from the original getPrice() methods
    private String capturePriceOutput(Price item, Customer customer) {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        try {
            PrintStream capture = new PrintStream(
                output, true, "UTF-8"
            );

            try {
                System.setOut(capture);
                item.getPrice(customer);
            } finally {
                System.setOut(originalOut);
                capture.close();
            }

            return new String(
                output.toByteArray(),
                StandardCharsets.UTF_8
            );

        } catch (Exception ex) {
            System.setOut(originalOut);
            return "Unable to display product details.";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == checkButton) {

            String customerName =
                nameField.getText().trim();

            String salaryText =
                salaryField.getText().trim();

            int selectedProduct =
                productBox.getSelectedIndex();

            if (customerName.isEmpty() ||
                salaryText.isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter customer name and salary.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {

                double salary =
                    Double.parseDouble(salaryText);

                if (!Double.isFinite(salary) || salary < 0) {
                    throw new NumberFormatException();
                }

                Customer customer =
                    new Customer(customerName, salary);

                Price item;

                if (selectedProduct == 0) {

                    item = new SmartPhone(
                        "SmartPhone",
                        "Iphone 14 ProMax",
                        5500.00
                    );

                } else if (selectedProduct == 1) {

                    item = new Laptop(
                        "Laptop",
                        "Lenovo Yoga",
                        6000.00
                    );

                } else {

                    item = new Car("BMW", 200000.00);
                }

                resultArea.setText(
                    capturePriceOutput(item, customer)
                );

                resultArea.setCaretPosition(0);

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid non-negative salary.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }

        else if (e.getSource() == originalButton) {

            SmartPhone sp = new SmartPhone(
                "SmartPhone",
                "Iphone 14 ProMax",
                5500.00
            );

            Laptop lp = new Laptop(
                "Laptop",
                "Lenovo Yoga",
                6000.00
            );

            Car car = new Car("BMW", 200000.00);

            Customer c1 = new Customer("Sally", 3000.00);
            Customer c2 = new Customer("Aaron", 8000.00);
            Customer c3 = new Customer("Simon", 10000.00);

            String output =
                capturePriceOutput(sp, c1) + "\n" +
                capturePriceOutput(lp, c2) + "\n" +
                capturePriceOutput(car, c3);

            resultArea.setText(output);
            resultArea.setCaretPosition(0);
        }

        else if (e.getSource() == clearButton) {

            nameField.setText("");
            salaryField.setText("");
            productBox.setSelectedIndex(0);
            resultArea.setText("");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new PriceGUI());

    }
}
