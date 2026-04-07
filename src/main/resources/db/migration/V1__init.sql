CREATE TABLE Users (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    last_name NVARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    active BIT NOT NULL,
    password_digest VARCHAR(80) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE Accounts (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(150) NOT NULL,
    account_type TINYINT NOT NULL,
    balance MONEY NOT NULL CHECK (balance >= 0),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE Categories (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE,
    type TINYINT NOT NULL,
    description VARCHAR(500),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE FinancialTransactions (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    value MONEY NOT NULL CHECK (value >= 0),
    description VARCHAR(500),
    date DATETIME NOT NULL,
    status TINYINT NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    category_id UNIQUEIDENTIFIER,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(id),
    FOREIGN KEY (category_id) REFERENCES Categories(id)
);

CREATE TABLE RecurrentTransactions (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    value MONEY NOT NULL CHECK (value >= 0),
    initial_date DATE NOT NULL,
    end_date DATE,
    frequency TINYINT NOT NULL,
    custom_frequency SMALLINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    category_id UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT chk_recurrent_transactions_custom_frequency_positive CHECK (custom_frequency IS NULL OR custom_frequency > 0),
    FOREIGN KEY (account_id) REFERENCES Accounts(id),
    FOREIGN KEY (category_id) REFERENCES Categories(id),
    CONSTRAINT chk_recurrent_transactions_dates CHECK (end_date IS NULL OR end_date >= initial_date)
);

CREATE TABLE LiquidityProjections (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    calculation_date DATETIME NOT NULL,
    projected_balance MONEY NOT NULL,
    projection_date DATE NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (account_id) REFERENCES Accounts(id)
);

CREATE TABLE Alerts (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    type TINYINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    date DATE NOT NULL,
    status TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    liquidity_projection_id UNIQUEIDENTIFIER NOT NULL,
    FOREIGN KEY (liquidity_projection_id) REFERENCES LiquidityProjections(id)
);

CREATE TABLE UserAccesses (
    id UNIQUEIDENTIFIER DEFAULT NEWSEQUENTIALID() PRIMARY KEY,
    role TINYINT NOT NULL,
    created_at DATETIME NOT NULL,
    account_id UNIQUEIDENTIFIER NOT NULL,
    user_id UNIQUEIDENTIFIER NOT NULL,
    CONSTRAINT uq_user_accesses_account_user UNIQUE (account_id, user_id),
    FOREIGN KEY (account_id) REFERENCES Accounts(id),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE INDEX ix_users_last_name_name ON Users(last_name, name);
CREATE INDEX ix_users_active ON Users(active);

CREATE INDEX ix_accounts_user_id ON Accounts(user_id);
CREATE INDEX ix_accounts_account_type ON Accounts(account_type);

CREATE INDEX ix_categories_type ON Categories(type);

CREATE INDEX ix_financial_transactions_account_id_date ON FinancialTransactions(account_id, date DESC);
CREATE INDEX ix_financial_transactions_category_id_date ON FinancialTransactions(category_id, date DESC);
CREATE INDEX ix_financial_transactions_status_date ON FinancialTransactions(status, date DESC);

CREATE INDEX ix_recurrent_transactions_account_id_initial_date ON RecurrentTransactions(account_id, initial_date);
CREATE INDEX ix_recurrent_transactions_category_id ON RecurrentTransactions(category_id);
CREATE INDEX ix_recurrent_transactions_end_date ON RecurrentTransactions(end_date);

CREATE INDEX ix_liquidity_projections_account_id_projection_date ON LiquidityProjections(account_id, projection_date);
CREATE INDEX ix_liquidity_projections_calculation_date ON LiquidityProjections(calculation_date);

CREATE INDEX ix_alerts_liquidity_projection_id ON Alerts(liquidity_projection_id);
CREATE INDEX ix_alerts_status_date ON Alerts(status, date);
CREATE INDEX ix_alerts_type_date ON Alerts(type, date);

CREATE INDEX ix_user_accesses_user_id ON UserAccesses(user_id);
