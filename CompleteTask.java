import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class User {

    private final String username;
    private final String password;
    private final String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridBagLayout());

        Panel content = new Panel(new GridLayout(2, 1, 0, 10));

        Label hello = new Label(
            "Hello, " + user.getUsername(),
            Label.CENTER
        );
        hello.setFont(new Font("SansSerif", Font.BOLD, 22));

        Label role = new Label(
            "Role: " + user.getRole(),
            Label.CENTER
        );
        role.setFont(new Font("SansSerif", Font.PLAIN, 16));

        content.add(hello);
        content.add(role);
        add(content);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 240);
        setLocationRelativeTo(null);
    }
}

public class CompleteTask extends Frame {

    private static final int MAX_ATTEMPTS = 3;

    private final ArrayList<User> users = new ArrayList<>();
    private int failedAttempts = 0;

    public CompleteTask() {
        super("Login");

        loadUsers();

        setLayout(new GridBagLayout());

        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));

        form.add(new Label("Username:", Label.RIGHT));

        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));

        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));

        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(
            new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        Button cancel = new Button("Cancel");
        Button login = new Button("Login");

        buttons.add(cancel);
        buttons.add(login);

        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            if (failedAttempts >= MAX_ATTEMPTS) {
                login.setEnabled(false);
                return;
            }

            String name = username.getText().trim();
            String pass = password.getText();

            if (name.isEmpty() || pass.isEmpty()) {
                showMessage("Please fill in all fields.");
                return;
            }

            User user = findUser(name);

            if (user == null) {
                showMessage("User not found.");
                return;
            }

            if (!user.getPassword().equals(pass)) {
                failedAttempts++;

                int left = MAX_ATTEMPTS - failedAttempts;

                if (left == 0) {
                    login.setEnabled(false);
                    showMessage(
                        "Too many failed attempts. Login is disabled."
                    );
                } else {
                    showMessage(
                        "Wrong password. Attempts left: " + left
                    );
                }

                password.setText("");

                EventQueue.invokeLater(() -> password.requestFocus());

                return;
            }

            if (!user.getRole().equals(role.getSelectedItem())) {
                showMessage(
                    "Selected role does not match this user."
                );
                return;
            }

            new WelcomePage(user).setVisible(true);
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {
        users.add(new User("ahmed", "1234", "Student"));
        users.add(new User("sara", "abcd", "TA"));
        users.add(new User("drmohamed", "pass123", "Professor"));
    }

    private User findUser(String name) {

        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                return user;
            }
        }

        return null;
    }

    private void showMessage(String message) {
        Dialog dialog = new Dialog(this, "Message", true);

        dialog.setLayout(new BorderLayout(10, 10));

        dialog.add(
            new Label(message, Label.CENTER),
            BorderLayout.CENTER
        );

        Button ok = new Button("OK");

        ok.addActionListener(e -> dialog.dispose());

        Panel panel = new Panel(new FlowLayout(FlowLayout.CENTER));
        panel.add(ok);

        dialog.add(panel, BorderLayout.SOUTH);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dialog.dispose();
            }
        });

        dialog.setSize(320, 130);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new CompleteTask().setVisible(true);
    }
}