package me.bytebeats.asp.analyzer;

import me.bytebeats.asp.analyzer.data.DebugDevice;
import me.bytebeats.asp.analyzer.data.DebugProcess;
import me.bytebeats.asp.analyzer.util.ResourcesKt;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2021/8/5 11:49
 * @Version 1.0
 * @Description TO-DO
 */

public class MainForm {
    private JPanel panel;
    private JComboBox<DebugDevice> deviceList;
    private JComboBox<DebugProcess> appList;
    private JPanel mainContainer;
    private JEditorPane initialHtml;
    private JPanel buttonContainer;
    private JComboBox<String> methodList;
    private JTextField keywordFilter;
    private final JButton scrollToBottomButton;
    private final JButton clearButton;
    private final JButton donateButton;

    private final Set<String> methods = new HashSet<>();

    private ItemListener methodItemListener;
    private AbstractAction onEnter;

    public MainForm() {
        donateButton = new JButton();
        donateButton.setPreferredSize(new Dimension(30, 30));
        donateButton.setIcon(ResourcesKt.getIcon("donate.png"));
        GridBagConstraints donateButtonConstraints = new GridBagConstraints();
        donateButtonConstraints.gridx = 1;
        donateButtonConstraints.gridy = 0;

        scrollToBottomButton = new JButton();
        scrollToBottomButton.setIcon(ResourcesKt.getIcon("scroll.png"));
        scrollToBottomButton.setPreferredSize(new Dimension(30, 30));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 2;
        scrollConstraints.gridy = 0;

        clearButton = new JButton();
        clearButton.setPreferredSize(new Dimension(30, 30));
        clearButton.setIcon(ResourcesKt.getIcon("delete.png"));
        GridBagConstraints clearConstraints = new GridBagConstraints();
        clearConstraints.gridx = 3;
        clearConstraints.gridy = 0;

        buttonContainer.add(donateButton, donateButtonConstraints);
        buttonContainer.add(scrollToBottomButton, scrollConstraints);
        buttonContainer.add(clearButton, clearConstraints);

        initialHtml.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        initialHtml.setEditable(false);

        initialHtml.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });

        URL initialFile = getClass().getClassLoader().getResource("initial.html");
        if (initialFile != null) {
            try {
                initialHtml.setPage(initialFile);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        resetMethodList();
        keywordFilter.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onEnter != null) {
                    onEnter.actionPerformed(e);
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public JComboBox<DebugDevice> getDeviceList() {
        return deviceList;
    }

    public JComboBox<DebugProcess> getAppList() {
        return appList;
    }

    public JComboBox<String> getMethodList() {
        return methodList;
    }

    public JButton getScrollToBottomButton() {
        return scrollToBottomButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getDonateButton() {
        return donateButton;
    }

    public JPanel getMainContainer() {
        return mainContainer;
    }

    public JEditorPane getInitialHtml() {
        return initialHtml;
    }

    public void setMethodItemListener(ItemListener listener) {
        this.methodItemListener = listener;
    }

    public void setOnEnter(AbstractAction action) {
        this.onEnter = action;
    }

    public String getKeyword() {
        return keywordFilter.getText();
    }

    public void resetMethodList() {
        methods.clear();
        methods.add("ALL");
        methodList.setModel(new DefaultComboBoxModel<>(new Vector<>(methods)));
        methodList.addItemListener(methodItemListener);
    }

    public void updateMethodList(String method) {
        if (!methods.contains(method)) {
            methods.add(method);
            methodList.setModel(new DefaultComboBoxModel<>(new Vector<>(methods)));
            methodList.addItemListener(methodItemListener);
        }
    }
}
