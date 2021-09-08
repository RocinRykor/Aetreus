package studio.rrprojects.aetreus.gui;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.http.util.TextUtils;
import studio.rrprojects.aetreus.main.Main;
import studio.rrprojects.aetreus.utils.MessageUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainWindow extends JFrame{
    private JPanel panelMain;
    private JButton SUBMITButton;
    private JTextArea fieldInput;
    private JComboBox boxAction;
    private JComboBox boxNameQuote;
    private JComboBox<String> boxChannelList;
    private JButton buttonRefresh;
    private JDA jda;

    public MainWindow(String title, JDA jda) {
        super(title);
        this.jda = jda;
        
        BeginInit();
        
        setContentPane(panelMain);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        buttonRefresh.addActionListener(actionEvent -> {
            RefreshJDA();
            PopulateChannelList();
        });
        boxAction.addActionListener(actionEvent -> {
            ToggleQuoteBox();
        });
        SUBMITButton.addActionListener(actionEvent -> {
            SubmitText();
            ClearField();
        });
    }

    private void ClearField() {
        fieldInput.setText("");
    }

    private void SubmitText() {
        TextChannel channel = jda.getTextChannelsByName(Objects.requireNonNull(boxChannelList.getSelectedItem()).toString(), true).get(0);

        if (channel == null) {
            return;
        }

        String message = FormatInput(fieldInput.getText());
        MessageUtils.SendMessage(message, channel);
    }

    private String FormatInput(String input) {
        String processedString = "";

        if (boxAction.getSelectedIndex() == 0) {
            processedString = input;
        } else if (boxAction.getSelectedIndex() == 1) {
            processedString = "```css\n" + input + "\n```";
        } else if (boxAction.getSelectedIndex() == 2) {
            processedString = "```\n" + Objects.requireNonNull(boxNameQuote.getSelectedItem()).toString() + ": " + input + "\n```";
        }

        return processedString;
    }

    private void ToggleQuoteBox() {
        boxNameQuote.setEnabled(boxAction.getSelectedIndex() == 2);
    }

    private void RefreshJDA() {
        jda = Main.getJda();
    }

    private void BeginInit() {
        PopulateChannelList();
        ToggleQuoteBox();
    }

    private void PopulateChannelList() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        List<TextChannel> channels = jda.getTextChannels();
        for (TextChannel channel: channels) {
            model.addElement(channel.getName());
        }
        boxChannelList.setModel(model);
        boxChannelList.setSelectedItem("shadowrun");
    }
}
