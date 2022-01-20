DROP TABLE IF EXISTS customers;

ALTER TABLE customers DROP COLUMN middle_name

CREATE TABLE customers(
	customer_id VARCHAR(10) PRIMARY KEY,
	first_name VARCHAR(20),
	last_name VARCHAR(20),
	address VARCHAR(30),
	city VARCHAR(20),
	state VARCHAR(20)
);

TRUNCATE customers CASCADE;

DROP TABLE IF EXISTS employees;

CREATE TABLE employees(
	employee_id VARCHAR(12) PRIMARY KEY,
	customer_id VARCHAR(10),
	admin_level BOOLEAN,
	FOREIGN KEY(customer_id) REFERENCES customers(customer_id)
);

DROP TABLE IF EXISTS login;

CREATE TABLE login(
	username VARCHAR(20) PRIMARY KEY,
	user_password VARCHAR(50),
	customer_id VARCHAR(10),
	employee_id VARCHAR(12),
	FOREIGN KEY(customer_id) REFERENCES customers(customer_id),
	FOREIGN KEY(employee_id) REFERENCES employees(employee_id)
);

ALTER TABLE login
	ALTER COLUMN user_password TYPE INTEGER;

DROP TABLE IF EXISTS user_accounts;

CREATE TABLE user_accounts(
	customer_id VARCHAR(10),
	account_number VARCHAR(17),
	FOREIGN KEY(customer_id) REFERENCES customers(customer_id),
	FOREIGN KEY(account_number) REFERENCES bank_accounts(account_number)
);



CREATE TABLE bank_accounts (
	account_number VARCHAR(17) PRIMARY KEY,
	account_balance NUMERIC(15, 2)
);

TRUNCATE bank_accounts CASCADE;

CREATE TABLE logs (
	log_id SERIAL PRIMARY KEY,
	name_id INTEGER,
	log_level VARCHAR(10),
	log_time VARCHAR(10),
	FOREIGN KEY(name_id) REFERENCES customers(name_id)
);

CREATE OR REPLACE FUNCTION transfer(account1 VARCHAR(17), account2 VARCHAR(17), amount NUMERIC(15, 2))
	RETURNS BOOLEAN
	LANGUAGE plpgsql
	AS
	
	$$
		DECLARE
			first_balance NUMERIC(15, 2) := (SELECT account_balance FROM bank_accounts ba WHERE ba.account_number = account1);
			second_balance NUMERIC(15, 2) := (SELECT account_balance FROM bank_accounts ba WHERE ba.account_number = account2);
		BEGIN
			IF first_balance >= amount THEN
				UPDATE bank_accounts SET account_balance = first_balance - amount WHERE account_number = account1;
				UPDATE bank_accounts SET account_balance = second_balance + amount WHERE account_number = account2;
				RETURN true;
			END IF;
			
			RETURN false;
		END;
	$$;

