IF COL_LENGTH('RecurrentTransactions', 'status') IS NULL
BEGIN
    ALTER TABLE RecurrentTransactions ADD status TINYINT NULL;
END
GO
