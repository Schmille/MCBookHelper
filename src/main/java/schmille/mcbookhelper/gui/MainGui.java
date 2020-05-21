package schmille.mcbookhelper.gui;

import schmille.mcbookhelper.Control;
import schmille.mcbookhelper.data.enums.EnumGameEdition;
import schmille.mcbookhelper.data.enums.EnumMethod;
import schmille.mcbookhelper.exceptions.WillNotFitException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainGui extends JFrame {
    private JPanel mainPanel;
    private JPanel bottomPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JButton btnCopyPage;
    private JButton btnCopyAll;
    private JButton btnClearPage;
    private JButton btnClearAll;
    private JPanel editionPanel;
    private JPanel timePanel;
    private JComboBox cobEdition;
    private JComboBox cobMethod;
    private JButton btnImport;
    private JSpinner spInterval;
    private JButton btnStart;
    private JPanel navigationPanel;
    private JScrollPane scrollPane;
    private JLabel labelPage;
    private JButton btnNext;
    private JButton btnPrevious;
    private JLabel labelInterval;
    private JLabel labelTime;
    private JTextArea textArea;
    private JProgressBar pbTimer;

    private Control control;
    private boolean timerRunning;
    private Timer timer;

    public MainGui(Control control) {
        super("Minecraft book helper");
        $$$setupUI$$$();
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this.btnImport.addActionListener(new ActionEventAllocator(this::btnImportPressed));
        this.btnStart.addActionListener(new ActionEventAllocator(this::btnStartPressed));
        this.btnPrevious.addActionListener(new ActionEventAllocator(this::btnPreviousPressed));
        this.btnNext.addActionListener(new ActionEventAllocator(this::btnNextPressed));
        this.btnCopyPage.addActionListener(new ActionEventAllocator(this::btnCopyPagePressed));
        this.btnCopyAll.addActionListener(new ActionEventAllocator(this::btnCopyAllPressed));
        this.btnClearPage.addActionListener(new ActionEventAllocator(this::btnClearPagePressed));
        this.btnClearAll.addActionListener(new ActionEventAllocator(this::btnClearAllPressed));

        configureComponents();

        this.control = control;
        this.setVisible(true);
    }

    private void btnImportPressed(ActionEvent event) {
        try {
            String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
            triggerAutopage(clipboard);
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
    }

    private void btnStartPressed(ActionEvent event) {

        if(timerRunning) {
            timer.cancel();
            timer.purge();
            btnStart.setText("Start");
            setControlStatus(true);
            return;
        }

        btnStart.setText("Stop");
        final long interval = ((Number) spInterval.getValue()).longValue() * 1000 + 1000;

        copyToClipboard("");
        timer = new Timer();
        control.beforeFirst();

        pbTimer.setMaximum(100);
        pbTimer.setMinimum(0);

        TimerTask t = new TimerTask() {

            long currentInterval = interval;

            @Override
            public void run() {

                currentInterval -= 1000;
                long seconds = currentInterval / 1000;
                DecimalFormat df = new DecimalFormat("00");

                labelTime.setText(df.format(seconds / 60) + ":" + df.format(seconds % 60));
                pbTimer.setValue(((Number) ((currentInterval / (double) (interval - 1000)) * 100)).intValue());

                if (currentInterval > 0)
                    return;

                currentInterval = interval;

                String p = control.nextPage();
                textArea.setText(p);
                copyToClipboard(p);
                updatePageCount();

                if (control.isOnLast()) {
                    setControlStatus(true);
                    btnStart.setText("Start");
                    this.cancel();
                }
            }
        };

        setControlStatus(false);

        timer.scheduleAtFixedRate(t, 1000, 1000);
    }

    private void btnPreviousPressed(ActionEvent event) {
        textArea.setText(control.previousPage());
        updatePageCount();
    }

    private void btnNextPressed(ActionEvent event) {
        textArea.setText(control.nextPage());
        updatePageCount();
    }

    private void btnCopyPagePressed(ActionEvent event) {
        copyToClipboard(control.currentPage());
    }

    private void btnCopyAllPressed(ActionEvent event) {
        copyToClipboard(control.getAllPages());
    }

    private void btnClearPagePressed(ActionEvent event) {
        control.clearCurrent();
    }

    private void btnClearAllPressed(ActionEvent event) {
        control.clearPages();
    }

    private void updatePageCount() {
        labelPage.setText(String.format("Page %d / %d", control.getCurrentPageIndex() + 1, control.getPageCount()));
    }

    private String copyFromClipboard() {
        try {
            return (String) getToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {

        } catch (IOException e) {

        }
        return "";
    }

    private void copyToClipboard(String input) {
        StringSelection s = new StringSelection(input);
        getToolkit().getSystemClipboard().setContents(s, null);
    }

    private void triggerAutopage(String input) {
        control.clearPages();
        EnumGameEdition edition = cobEdition.getSelectedIndex() == 0 ? EnumGameEdition.JAVA : EnumGameEdition.BEDROCK;

        if (cobMethod.getSelectedIndex() == 0) {
            try {
                control.autopage(input, EnumMethod.FANCY, edition);
            } catch (WillNotFitException e) {

                try {
                    control.autopage(input, EnumMethod.SIZE, edition);
                } catch (WillNotFitException willNotFitException) {
                    JOptionPane.showMessageDialog(this, willNotFitException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (cobMethod.getSelectedIndex() == 1) {
            try {
                control.autopage(input, EnumMethod.SIZE, edition);
            } catch (WillNotFitException willNotFitException) {
                JOptionPane.showMessageDialog(this, willNotFitException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            try {
                control.autopage(input, EnumMethod.FANCY, edition);
            } catch (WillNotFitException willNotFitException) {
                JOptionPane.showMessageDialog(this, willNotFitException.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        textArea.setText(control.currentPage());
        updatePageCount();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        btnCopyPage = new JButton();
        btnCopyPage.setText("Copy page");
        bottomPanel.add(btnCopyPage);
        btnCopyAll = new JButton();
        btnCopyAll.setText("Copy all");
        bottomPanel.add(btnCopyAll);
        btnClearPage = new JButton();
        btnClearPage.setText("Clear page");
        bottomPanel.add(btnClearPage);
        btnClearAll = new JButton();
        btnClearAll.setText("Clear all");
        bottomPanel.add(btnClearAll);
        topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        editionPanel = new JPanel();
        editionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        topPanel.add(editionPanel, gbc);
        cobEdition = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Java Edition");
        defaultComboBoxModel1.addElement("Bedrock Edition");
        cobEdition.setModel(defaultComboBoxModel1);
        editionPanel.add(cobEdition);
        cobMethod = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("Prefer fancy print");
        defaultComboBoxModel2.addElement("Use minimal size");
        defaultComboBoxModel2.addElement("Print fancy");
        cobMethod.setModel(defaultComboBoxModel2);
        editionPanel.add(cobMethod);
        btnImport = new JButton();
        btnImport.setText("Import");
        editionPanel.add(btnImport);
        timePanel = new JPanel();
        timePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        topPanel.add(timePanel, gbc);
        final JPanel spacer1 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        timePanel.add(spacer1, gbc);
        final JPanel spacer2 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        timePanel.add(spacer2, gbc);
        spInterval = new JSpinner();
        spInterval.setOpaque(false);
        spInterval.setPreferredSize(new Dimension(78, 30));
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        timePanel.add(spInterval, gbc);
        labelInterval = new JLabel();
        labelInterval.setText("Interval");
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 3);
        timePanel.add(labelInterval, gbc);
        btnStart = new JButton();
        btnStart.setText("Start");
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        timePanel.add(btnStart, gbc);
        final JPanel spacer3 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        timePanel.add(spacer3, gbc);
        pbTimer = new JProgressBar();
        pbTimer.setPreferredSize(new Dimension(146, 15));
        pbTimer.setStringPainted(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.weightx = 0.2;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 0, 0);
        timePanel.add(pbTimer, gbc);
        labelTime = new JLabel();
        Font labelTimeFont = this.$$$getFont$$$(null, -1, 18, labelTime.getFont());
        if (labelTimeFont != null) labelTime.setFont(labelTimeFont);
        labelTime.setText("00:00");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        timePanel.add(labelTime, gbc);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        navigationPanel = new JPanel();
        navigationPanel.setLayout(new GridBagLayout());
        centerPanel.add(navigationPanel, BorderLayout.NORTH);
        btnPrevious = new JButton();
        btnPrevious.setText("<");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.ipadx = 2;
        gbc.ipady = 2;
        navigationPanel.add(btnPrevious, gbc);
        labelPage = new JLabel();
        Font labelPageFont = this.$$$getFont$$$(null, -1, 16, labelPage.getFont());
        if (labelPageFont != null) labelPage.setFont(labelPageFont);
        labelPage.setText("Page 0 / 100");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipadx = 2;
        gbc.ipady = 2;
        navigationPanel.add(labelPage, gbc);
        btnNext = new JButton();
        btnNext.setText(">");
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.ipadx = 2;
        gbc.ipady = 2;
        navigationPanel.add(btnNext, gbc);
        final JPanel spacer4 = new JPanel();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        navigationPanel.add(spacer4, gbc);
        scrollPane = new JScrollPane();
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(50, 50));
        scrollPane.setViewportView(textArea);
    }

    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    private void setControlStatus(boolean enabled) {
        this.btnNext.setEnabled(enabled);
        this.btnPrevious.setEnabled(enabled);
        this.btnImport.setEnabled(enabled);

        this.btnClearAll.setEnabled(enabled);
        this.btnClearPage.setEnabled(enabled);
        this.btnCopyPage.setEnabled(enabled);
        this.btnCopyAll.setEnabled(enabled);

        this.textArea.setEditable(enabled);

        this.spInterval.setEnabled(enabled);
        this.cobMethod.setEnabled(enabled);
        this.cobEdition.setEnabled(enabled);

        this.timerRunning = !enabled;
    }

    private void configureComponents() {
        SpinnerNumberModel sm1 = new SpinnerNumberModel();
        sm1.setMaximum(5940);
        sm1.setMinimum(0);
        sm1.setValue(5);
        this.spInterval.setModel(sm1);
    }

}
