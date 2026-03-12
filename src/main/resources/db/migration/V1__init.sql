CREATE TABLE Persons (
    person_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    active BIT NOT NULL,
    password_digest VARCHAR(80) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE Accounts (
    account_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(150) NOT NULL,
    account_type TINYINT NOT NULL,
    balance MONEY NOT NULL CHECK (balance >= 0),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    person_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (person_id) REFERENCES Persons(person_id)
);

CREATE TABLE Categories (
    category_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    type TINYINT NOT NULL,
    description VARCHAR(500),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE FinancialTransactions (
    transaction_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    type TINYINT NOT NULL,
    value MONEY NOT NULL CHECK (value >= 0),
    description VARCHAR(500),
    date DATETIME NOT NULL,
    status TINYINT NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    category_id UNIQUEIDENTIFIER,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);

CREATE TABLE RecurrentTransactions (
    recurrent_transaction_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    value MONEY NOT NULL CHECK (value >= 0),
    initial_date DATE NOT NULL,
    end_date DATE,
    frequency TINYINT NOT NULL,
    custom_frequency SMALLINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    category_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    CONSTRAINT chk_dates CHECK (end_date IS NULL OR end_date >= initial_date)
);

CREATE TABLE LiquidityProjections (
    projection_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    calculation_date DATETIME NOT NULL,
    projected_balance MONEY NOT NULL,
    projection_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id)
);

CREATE TABLE Alerts (
    alert_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    type TINYINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    date DATE NOT NULL,
    status TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    liquidity_projection_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (liquidity_projection_id) REFERENCES LiquidityProjections(projection_id)
);

CREATE TABLE UserAccesses (
    user_access_id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    role TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    person_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(account_id),
    FOREIGN KEY (person_id) REFERENCES Persons(person_id)
);