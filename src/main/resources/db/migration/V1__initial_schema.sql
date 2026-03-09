CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  address VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  phone VARCHAR(11) NOT NULL,
  state VARCHAR(2) NOT NULL,
  zip_code VARCHAR(8) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_user_cpf UNIQUE (cpf),
  CONSTRAINT uk_user_email UNIQUE (email)
);

CREATE TABLE dependent (
  id BIGINT NOT NULL AUTO_INCREMENT,
  address VARCHAR(255) NOT NULL,
  city VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) NOT NULL,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(11) NOT NULL,
  state VARCHAR(2) NOT NULL,
  zip_code VARCHAR(8) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_dependent_cpf UNIQUE (cpf)
);

CREATE TABLE contract (
  id BIGINT NOT NULL AUTO_INCREMENT,
  end_date DATE,
  start_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL,
  dependent_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_contract_dependent FOREIGN KEY (dependent_id) REFERENCES dependent(id),
  CONSTRAINT fk_contract_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_contract_user ON contract(user_id);
CREATE INDEX idx_contract_dependent ON contract(dependent_id);
CREATE INDEX idx_contract_validation ON contract(dependent_id, user_id, status);

CREATE TABLE expense (
  id BIGINT NOT NULL AUTO_INCREMENT,
  data_expense DATE NOT NULL,
  description_expense VARCHAR(255) NOT NULL,
  receipt_url VARCHAR(255),
  value_expense DECIMAL(10,2) NOT NULL,
  contract_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_expense_contract FOREIGN KEY (contract_id) REFERENCES contract(id)
);

CREATE INDEX idx_expense_contract ON expense(contract_id);
CREATE INDEX idx_expense_validation ON expense(contract_id, data_expense, value_expense);

CREATE TABLE income (
  id BIGINT NOT NULL AUTO_INCREMENT,
  data_income DATE NOT NULL,
  description_income VARCHAR(255) NOT NULL,
  receipt_url VARCHAR(255),
  value_income DECIMAL(10,2) NOT NULL,
  contract_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_income_contract FOREIGN KEY (contract_id) REFERENCES contract(id)
);

CREATE INDEX idx_income_contract ON income(contract_id);
CREATE INDEX idx_income_validation ON income(contract_id, data_income, value_income);