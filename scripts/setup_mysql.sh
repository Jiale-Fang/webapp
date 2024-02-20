#! /usr/bin/expect

set MYSQL_PASSWORD [lindex $argv 0]
# Secure Setting
spawn sudo mysql_secure_installation
expect {
  "Press y|Y for Yes, any other key for No:"
  {
    send "y\r"
    exp_continue
  }
  "Please enter 0 = LOW, 1 = MEDIUM and 2 = STRONG:"
  {
    send "0\r"
    exp_continue
  }
  "New password:"
  {
    send "$MYSQL_PASSWORD\r"
    exp_continue
  }
  "Re-enter new password:"
  {
    send "$MYSQL_PASSWORD\r"
    exp_continue
  }
  "Do you wish to continue with the password provided?(Press y|Y for Yes, any other key for No) :"
  {
    send "y\r"
    exp_continue
  }
  "Remove anonymous users? (Press y|Y for Yes, any other key for No) :"
  {
    send "y\r"
    exp_continue
  }
  "Disallow root login remotely? (Press y|Y for Yes, any other key for No) :"
  {
    send "y\r"
    exp_continue
  }
  "Remove test database and access to it? (Press y|Y for Yes, any other key for No) :"
  {
    send "y\r"
    exp_continue
  }
  "Reload privilege tables now? (Press y|Y for Yes, any other key for No) :"
  {
    send "y\r"
  }
}
expect eof

# Create a mysql user
spawn mysql -uroot -p
expect {
  "Enter password:"
  {
    send "$MYSQL_PASSWORD\r"
    exp_continue
  }
  "mysql>"
  {
    send "Set global validate_password.policy=LOW;\r"
    send "CREATE USER 'user'@'localhost' IDENTIFIED BY '$MYSQL_PASSWORD';\r"
    send "GRANT ALL PRIVILEGES ON `health\_check`.* TO 'user'@'localhost';\r"
    send "flush privileges;\r"
    send "exit;\r"
  }
}
expect eof
