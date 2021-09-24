package me.bytebeats.asp.analyzer;

import me.bytebeats.asp.analyzer.data.DebugDevice;
import me.bytebeats.asp.analyzer.data.DebugProcess;
import me.bytebeats.asp.analyzer.util.Resources;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
    private final JButton scrollToBottomButton;
    private final JButton clearButton;
    private final JButton donateButton;

    public MainForm() {
        donateButton = new JButton();
        donateButton.setPreferredSize(new Dimension(100, 30));
        donateButton.setText(Resources.INSTANCE.getString("donate"));
        donateButton.setIcon(Resources.INSTANCE.getIcon("donate.png"));
        GridBagConstraints donateButtonConstraints = new GridBagConstraints();
        donateButtonConstraints.gridx = 1;
        donateButtonConstraints.gridy = 0;

        scrollToBottomButton = new JButton();
        scrollToBottomButton.setIcon(Resources.INSTANCE.getIcon("scroll.png"));
        scrollToBottomButton.setPreferredSize(new Dimension(30, 30));
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.gridx = 2;
        scrollConstraints.gridy = 0;

        clearButton = new JButton();
        clearButton.setPreferredSize(new Dimension(30, 30));
        clearButton.setIcon(Resources.INSTANCE.getIcon("delete.png"));
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
}
